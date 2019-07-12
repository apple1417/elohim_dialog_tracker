package apple1417.elohim_dialog_tracker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.CharBuffer;
import java.util.function.Consumer;
import java.util.List;
import java.util.Scanner;

/*
  Reads the tail of a file and when it updates sends new lines to the lineConsumer
  May have issues if the file shrinks
*/
public class FileTailer extends Thread {
    private String name;
    private Consumer<String> lineConsumer;
    private int sleepMS;

    private FileChannel channel;
    private long lastSize;

    FileTailer(String name, File file, Consumer<String> lineConsumer, int sleepMS) throws IOException {
        this.name = name;
        this.lineConsumer = lineConsumer;
        this.sleepMS = sleepMS;

        channel = FileChannel.open(file.toPath());
        lastSize = channel.size();
        channel.position(lastSize);
    }
    FileTailer(String name, File file, Consumer<String> lineConsumer) throws IOException {
        this(name, file, lineConsumer, 100);
    }

    private Thread t;
    public void start() {
        if (t == null) {
            t = new Thread(this, name);
            t.setDaemon(true);
            t.start();
        } else {
            System.err.printf("Thread '%s' is already running", name);
        }
    }

    public void run() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        boolean lastCR = false;
        while (true) {
            try {
                if (channel.size() > lastSize) {
                    int size = (int) (channel.size() - lastSize);
                    ByteBuffer bytes = ByteBuffer.allocate(size);
                    channel.read(bytes);
                    lastSize += size;

                    for (int i = 0; i < size; i++) {
                        int character = bytes.get(i);

                        if (lastCR) {
                            if (character == '\n') {
                                // If we had a CRLF pair output both together
                                output.write(character);
                                lineConsumer.accept(output.toString("UTF-8"));
                                output = new ByteArrayOutputStream();
                                continue;
                            } else {
                                // If just a CR then don't output the new char yet
                                lineConsumer.accept(output.toString("UTF-8"));
                                output = new ByteArrayOutputStream();
                            }
                            lastCR = false;
                        }

                        output.write(character);

                        if (character == '\n') {
                            lineConsumer.accept(output.toString("UTF-8"));
                            output = new ByteArrayOutputStream();
                        } else if (character == '\r') {
                            // We might have a CRLF pair so don't output yet
                            lastCR = true;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            try {
                Thread.sleep(sleepMS);
            } catch (InterruptedException ex) {
                t.interrupt();
                try {
                    channel.close();
                } catch (IOException e) {}
                break;
            }
        }
    }
}
