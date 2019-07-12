package apple1417.elohim_dialog_tracker;

import java.io.File;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainWindow extends Application {
    public static final String version = "v1.1";

    public static void main(String[] args) {
        launch();
    }

    public void start(Stage mainStage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(7.5));

        HBox logChooser = new HBox(5);
        logChooser.prefWidthProperty().bind(root.widthProperty());

        Button logButton = new Button("Select log file");
        logButton.setMinWidth(90);
        logButton.setMaxWidth(90);

        TextField logLocation = new TextField();
        logLocation.setEditable(false);
        logLocation.setStyle("-fx-control-inner-background: #F4C7C3;");
        logLocation.prefWidthProperty().bind(logChooser.widthProperty().subtract(logButton.widthProperty()));

        logChooser.getChildren().add(logLocation);
        logChooser.getChildren().add(logButton);

        root.getChildren().add(logChooser);

        Button resetButton = new Button("Reset All");
        resetButton.setMinWidth(90);
        resetButton.setMaxWidth(90);

        Text amountText = new Text();

        BorderPane lowerBar = new BorderPane();
        // Only pad horizontally
        lowerBar.setPadding(new Insets(0, 5, 0, 5));
        lowerBar.setCenter(resetButton);
        lowerBar.setRight(amountText);

        DialogDisplay display = new DialogDisplay();
        display.prefWidthProperty().bind(root.widthProperty());
        display.prefHeightProperty().bind(
            root.heightProperty()
                .subtract(logChooser.heightProperty())
                .subtract(lowerBar.heightProperty())
        );

        amountText.textProperty().bind(
            display.collectedAmountProperty().asString().concat(
                "/" + Integer.toString(display.getItems().size())
            )
        );

        root.getChildren().add(display);
        root.getChildren().add(lowerBar);

        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Talos Log File", "Talos.log", "Talos_Unrestricted.log");
        chooser.getExtensionFilters().add(filter);
        chooser.setSelectedExtensionFilter(filter);

        LogTracker tracker = new LogTracker(display.getItems());

        logButton.setOnAction((obs) -> {
            File logFile = chooser.showOpenDialog(mainStage);
            if (logFile != null) {
                logLocation.setText(logFile.getPath());
                logLocation.setStyle(null);
                tracker.updateLogFile(logFile);
            }
        });


        resetButton.setOnAction((obs) -> {
            tracker.resetAll();
        });

        mainStage.setScene(new Scene(root, 800, 800));
        mainStage.setTitle("Elohim Dialog Tracker " + version);
        mainStage.setMinWidth(500);
        mainStage.setMinHeight(500);
        mainStage.show();
    }
}
