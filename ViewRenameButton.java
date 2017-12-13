package photo_viewer;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import photo_manager.Photo;

class ViewRenameButton {

    Button viewRename = new Button("View Rename History");

    ViewRenameButton(ListView<Photo> listView) {
        viewRename.setMinWidth(150);
        viewRename.setOnAction(
                (event -> {
                    if (listView.getSelectionModel().getSelectedItem() != null) {
                        // Creates a new stage and scene for the Rename History window.
                        Stage renameViewer = new Stage();
                        renameViewer.setTitle("Rename History");
                        VBox renameViewerPane = new VBox();
                        Scene renameViewerScene = new Scene(renameViewerPane, 800, 400);
                        renameViewer.setScene(renameViewerScene);

                        // Creates a list of all the renaming of the selected photo.
                        ListView<String> renameView = new ListView<>();
                        renameView.setItems(
                                FXCollections.observableArrayList(
                                        listView.getSelectionModel().getSelectedItem().getTimeStampsString()));
                        renameViewerPane.getChildren().add(renameView);

                        renameViewer.show();
                    } else {
                        AlertBox.display("Please select an image!");
                    }
                }
                )
        );
    }
}
