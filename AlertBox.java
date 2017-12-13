package photo_viewer;
import javafx.scene.Scene;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

/**
 * An AlertBox to be displayed when the user preforms an unwanted action.
 */
class AlertBox {

    /**
     * Displays an alert message popup.
     * @param message the message to be displayed.
     */
    static void display(String message){
        //Create a pop up window
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("ALERT");
        window.setMinWidth(300);

        //Create a label to show message to user
        Label label = new Label();
        label.setText(message);

        //Create a button so user can close pop up window
        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> window.close());

        //Format the label and button onto the window
        VBox format = new VBox(20);
        format.getChildren().addAll(label, closeButton);
        format.setAlignment(Pos.CENTER);

        // Add a scene to the window so the window needs to be closed to continue
        // the other window.
        Scene scene = new Scene(format);
        window.setScene(scene);
        window.showAndWait();
    }
}
