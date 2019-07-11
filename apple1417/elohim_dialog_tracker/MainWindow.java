package apple1417.elohim_dialog_tracker;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.File;
import javafx.geometry.Pos;

public class MainWindow extends Application {
    public static void main(String[] args) {
        launch();
    }

    public void start(Stage mainStage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(7.5, 7.5, 7.5, 7.5));

        HBox logChooser = new HBox(5);
        logChooser.prefWidthProperty().bind(root.widthProperty());

        Button logButton = new Button("Select log file");
        logButton.setMinWidth(90);
        logButton.setMaxWidth(90);

        TextField logLocation = new TextField();
        logLocation.setEditable(false);
        logLocation.prefWidthProperty().bind(logChooser.widthProperty().subtract(logButton.widthProperty()));

        logChooser.getChildren().add(logLocation);
        logChooser.getChildren().add(logButton);

        root.getChildren().add(logChooser);

        Button resetButton = new Button("Reset All");
        resetButton.setMinWidth(90);
        resetButton.setMaxWidth(90);

        HBox resetBox = new HBox(5);
        resetBox.getChildren().add(resetButton);
        resetBox.setAlignment(Pos.BASELINE_CENTER);

        DialogDisplay display = new DialogDisplay();
        display.prefWidthProperty().bind(root.widthProperty());
        display.prefHeightProperty().bind(
            root.heightProperty()
                .subtract(logChooser.heightProperty())
                .subtract(resetBox.heightProperty())
        );

        root.getChildren().add(display);
        root.getChildren().add(resetBox);

        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Talos Log File", "Talos.log", "Talos_Unrestricted.log");
        chooser.getExtensionFilters().add(filter);
        chooser.setSelectedExtensionFilter(filter);

        LogTracker tracker = new LogTracker(display.getItems());

        logButton.setOnAction((obs) -> {
            File logFile = chooser.showOpenDialog(mainStage);
            if (logFile != null) {
                logLocation.setText(logFile.getPath());
                tracker.updateLogFile(logFile);
            }
        });

        resetButton.setOnAction((obs) -> {
            display.getItems().forEach((item) -> item.setRawState(DialogState.UNCOLLECTED));
        });

        mainStage.setScene(new Scene(root, 800, 800));
        mainStage.setTitle("Elohim Dialog Tracker");
        mainStage.setMinWidth(500);
        mainStage.setMinHeight(500);
        mainStage.show();
    }
}
