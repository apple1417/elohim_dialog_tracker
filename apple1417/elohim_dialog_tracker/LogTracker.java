package apple1417.elohim_dialog_tracker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Scanner;

public class LogTracker {
    private static int tailerAmount = 0;

    private List<DialogLine> dialogList;
    public LogTracker(List<DialogLine> dialogList, File logFile) {
        this.dialogList = dialogList;

        if (logFile != null) {
            updateLogFile(logFile);
        }
    }
    public LogTracker(List<DialogLine> dialogList) {
        this(dialogList, null);
    }

    FileTailer tailer;
    public void updateLogFile(File logFile) {
        try {
            FileTailer newTailer = new FileTailer(
                "Log Scanner " + Integer.toString(++tailerAmount),
                logFile,
                (str) -> consumeLine(str)
            );

            if (tailer != null) {
                tailer.interrupt();
            }
            newTailer.start();
            tailer = newTailer;
        } catch (IOException e) {
            return;
        }
    }

    public void consumeLine(String line) {
        try {
            // Start with the shortest substring so that a single index out of bounds applies to all
            if (line.substring(16, 29).equals("Changing to '")) {
                saveAll();
            } else if (line.substring(16, 31).equals("Elohim speaks: ")) {
                String dialog = line.substring(31, line.length() - 2);
                boolean foundDialog = false;
                for (DialogLine item : dialogList) {
                    /*
                      Some dialogs have multiple options cause croteam renamed them at some point
                      I forced the csv to just just newlines so there won't be any CRs left over
                    */
                    String[] allOptions = item.getDialog().split("\n");
                    for (String option : allOptions) {
                        if (option.equals(dialog) && item.getRawState() == DialogState.UNCOLLECTED) {
                            item.setRawState(DialogState.NOT_SAVED);
                            foundDialog = true;
                            break;
                        }
                    }
                    if (foundDialog) {
                        break;
                    }
                }
                if (!foundDialog) {
                    System.err.println("Couldn't match dialog '" + dialog + "'");
                }
            } else if (line.substring(16, 39).equals("Started simulation on '")) {
                resetUnsaved();
            } else if (line.substring(16, 47).equals("Player profile saved with size ")) {
                saveAll();
            }
        } catch (StringIndexOutOfBoundsException e) {}
    }

    /*
      Dialog 63-1 is awarded close enough to a purple that it's line is written first, but it
       doesn't actually get saved
      Because of this we just have a quick workaround that only saves it on the second save.
    */
    private boolean seenDialog63_1 = false;
    private void saveAll() {
        for (DialogLine item : dialogList) {
            if (item.getRawState() == DialogState.NOT_SAVED) {
                if (item.getDialog().equals("Elohim-063_Nexus_Ascent_01")) {
                    seenDialog63_1 = !seenDialog63_1;
                    if (seenDialog63_1) {
                        continue;
                    }
                }
                item.setRawState(DialogState.COLLECTED);
            }
        }
    }

    private void resetUnsaved() {
        for (DialogLine item : dialogList) {
            if (item.getRawState() == DialogState.NOT_SAVED) {
                if (item.getDialog().equals("Elohim-063_Nexus_Ascent_01")) {
                    seenDialog63_1 = false;
                }
                item.setRawState(DialogState.UNCOLLECTED);
            }
        }
    }

    public void resetAll() {
        dialogList.forEach((item) -> item.setRawState(DialogState.UNCOLLECTED));
    }
}
