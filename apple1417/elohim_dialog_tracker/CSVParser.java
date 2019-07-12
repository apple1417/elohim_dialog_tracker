package apple1417.elohim_dialog_tracker;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.lang.StringBuilder;
import java.util.ArrayList;

public class CSVParser{
    private static final int SEPERATOR = ',';
    // I should probably make this suport CRLF endings but for now I forced the file to use just LF
    private static final int TERMINATOR = '\n';
    private static final int QUOTE = '"';

    private BufferedInputStream stream;
    private boolean closed = false;
    public CSVParser(InputStream in) {
        stream = new BufferedInputStream(in);
    }

    public boolean isClosed() {
        return closed;
    }

    public ArrayList<String> nextRow() throws IOException {
        if (closed) {
            throw new IOException("Attempted to read next row while closed");
        }

        ArrayList<String> output = new ArrayList<String>();
        int currentChar;
        do {
            output.add(nextItem());
            currentChar = stream.read();
        } while (currentChar == SEPERATOR);

        // Read an extra char incase we end on a newline
        stream.mark(1);
        closed = currentChar == -1 || stream.read() == -1;
        if (!closed) {
            stream.reset();
        }

        return output;
    }

    private String nextItem() throws IOException {
        if (closed) {
            throw new IOException("Attempted to read next item while closed");
        }

        StringBuilder sb = new StringBuilder();
        int currentChar = stream.read();
        boolean inQuotes = currentChar == QUOTE;
        if (!inQuotes) {
            sb.appendCodePoint(currentChar);
        }

        while (true) {
            stream.mark(2);
            currentChar = stream.read();
            if (currentChar == -1) {
                closed = true;
                break;
            } else if (!inQuotes && (currentChar == SEPERATOR || currentChar == TERMINATOR)) {
                // Incase we end on a newline
                closed = stream.read() == -1;
                // Don't include the seperator so that nextRow() can tell if the row ended
                stream.reset();
                break;
            } else if (inQuotes && currentChar == QUOTE) {
                int nextChar = stream.read();
                if (nextChar != QUOTE) {
                    // Incase we end on a newline
                    closed = nextChar == -1;
                    // Want to consume the quote but not the seperator
                    stream.reset();
                    stream.read();
                    break;
                }
            }
            sb.appendCodePoint(currentChar);
        }
        return sb.toString();
    }
}
