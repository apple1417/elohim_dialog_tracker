package apple1417.elohim_dialog_tracker;

import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.geometry.Pos;

public class DialogGridCell extends Region implements Comparable<DialogGridCell> {
    public static final int SIZE = 32;

    private DialogLine line;
    public DialogGridCell(DialogLine line) {
        this.line = line;

        setMaxWidth(SIZE);
        setMinWidth(SIZE);
        setMaxHeight(SIZE);
        setMinHeight(SIZE);

        Text shortName = new Text(line.getShortName());
        shortName.relocate(
            (SIZE - shortName.getLayoutBounds().getWidth()) / 2,
            (SIZE - shortName.getLayoutBounds().getHeight()) / 2
        );
        getChildren().add(shortName);

        setStyle("-fx-background-color: " + line.getRawState().getColour());

        line.StateProperty().addListener((v, oldVal, newVal) -> {
            setStyle("-fx-background-color: " + line.getRawState().getColour());
        });
    }

    public DialogLine getLine() {
    	return line;
    }

    public int compareTo(DialogGridCell o) {
        return line.getShortName().compareTo(o.getLine().getShortName());
    }
}
