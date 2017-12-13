package photo_viewer;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import photo_manager.Photo;

import java.io.File;
import java.util.ArrayList;

class MovePhotoButton {

    Button movePhoto = new Button("Move Photo");

    MovePhotoButton(Stage primary, StackPane picPane, TextArea tagText, ListView<Photo> listView,
                           ObservableList<Photo> list, ArrayList<Photo> photos, PhotoManager manager) {
        // Creates a Move Photo button.
        movePhoto.setMinWidth(110);
        movePhoto.setOnAction(
                (event -> {
                    Photo photo = listView.getSelectionModel().getSelectedItem();
                    for (Photo p : photos) {
                        if (p.equals(photo)) photo = p;
                    }
                    if (photo != null) {
                        DirectoryChooser directoryChooser = new DirectoryChooser();
                        directoryChooser.setTitle("Select Directory");
                        File result = directoryChooser.showDialog(primary);
                        if (result != null) {
                            manager.movePhoto(photo.getPhotoName(), result.getPath(), photo);
                            //                 photo.setName(photo.getName(), result.getPath());
                            list.remove(photo);
                            listView.setItems(list);
                            picPane.getChildren().clear();
                            tagText.setText("");
                            //                     photoManager.changeNameOfPhoto(photo);
                        }
                    } else {
                        AlertBox.display("Please select an image!");
                    }
                }
                )
        );
    }

}


