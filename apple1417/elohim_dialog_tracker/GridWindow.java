package apple1417.elohim_dialog_tracker;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import java.util.ArrayList;

public class GridWindow extends Application {
    public static final int INTIAL_X_SIZE = 8;
    public static final int INITAL_Y_SIZE = 11;
    public static final int CELL_PADDING = 2;
    public static final int BORDER_PADDING = 5;

    private ObservableList<DialogLine> dialogList;
    public GridWindow(ObservableList<DialogLine> dialogList) {
        this.dialogList = dialogList;
    }

    public void start(Stage rootStage) {
        FlowPane rootPane = new FlowPane(CELL_PADDING, CELL_PADDING);
        rootPane.setPadding(new Insets(BORDER_PADDING));

        ArrayList<DialogGridCell> cells = new ArrayList<DialogGridCell>();
        for (DialogLine item : dialogList) {
            cells.add(new DialogGridCell(item));
        }
        cells.sort((a, b) -> a.compareTo(b));
        rootPane.getChildren().addAll(cells);

        rootStage.setScene(new Scene(rootPane, 280, 382));
        rootStage.setTitle("Elohim Dialog Tracker - Grid View");
        rootStage.setResizable(false);
        // For whatever reason setting it to not be resizeable adds extra padding we have to remove
        rootStage.sizeToScene();
        rootStage.show();
    }
}
