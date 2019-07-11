package apple1417.elohim_dialog_tracker;

import javafx.scene.control.TableCell;
import javafx.scene.text.Text;

public class DialogTableCell extends TableCell<DialogLine, String> {
    private boolean shouldColour;
    public DialogTableCell(boolean shouldColour) {
        this.shouldColour = shouldColour;

        Text cellText = new Text();
        setGraphic(cellText);
        cellText.wrappingWidthProperty().bind(widthProperty());
        cellText.textProperty().bind(itemProperty());
    }

    protected void updateItem(String text, boolean empty) {
        super.updateItem(text, empty);
        if (text == null || empty) {
            setText(null);
            setStyle("");
        } else {
            setText(text);

            DialogLine item = getTableView().getItems().get(getIndex());
            if (shouldColour && item != null) {
                setStyle("-fx-background-color: " + item.getRawState().getColour());
            }
        }
    }
}
