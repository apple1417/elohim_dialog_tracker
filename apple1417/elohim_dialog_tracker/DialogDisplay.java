package apple1417.elohim_dialog_tracker;

import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

public class DialogDisplay extends TableView<DialogLine> {
    private static final String[] allHeaders = new String[] {
        "State",
        "Level",
        "Dialog",
        "Trigger"
    };
    private static final double[] allWidths = new double[] {
        0.15,
        0.1,
        0.25,
        0.5
    };

    public DialogDisplay() {
        super();

        // Setup Columns
        for (int i = 0; i < allHeaders.length; i++) {
            String header = allHeaders[i];

            TableColumn<DialogLine, String> col = new TableColumn<DialogLine, String>(header);
            // Subtract 19px for the border + scrollbar
            col.prefWidthProperty().bind(widthProperty().subtract(19).multiply(allWidths[i]));
            col.setCellValueFactory(new PropertyValueFactory<DialogLine, String>(header));
            if (i == 0) {
                col.setCellFactory((tc) -> {return new DialogTableCell(true);});
            } else {
                col.setCellFactory((tc) -> {return new DialogTableCell(false);});
            }

            getColumns().add(col);
        }

        setRowFactory((tv) -> {
            TableRow<DialogLine> row = new TableRow<DialogLine>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    DialogLine item = row.getItem();
                    item.setRawState(item.getRawState().next());
                }
            });
            return row;
        });

        // Populate table with data from the file "elohimInfo.csv"
        try {
            ObservableList<DialogLine> allItems = getItems();
            CSVParser csv = new CSVParser(getClass().getResourceAsStream("elohimInfo.csv"));
            while (!csv.isClosed()) {
                ArrayList<String> row = csv.nextRow();
                allItems.add(new DialogLine(row.get(0), row.get(1), row.get(2)));
            }
        } catch (IOException e) {}
    }

    public void refresh() {
        super.refresh();
        // Force word wrapping to be recalculated, otherwise it'll wrap without making the cell taller
        TableColumn col = getColumns().get(0);
        col.prefWidthProperty().unbind();
        col.setPrefWidth(col.getWidth() + 1);
        col.prefWidthProperty().bind(widthProperty().subtract(19).multiply(allWidths[0]));
    }
}
