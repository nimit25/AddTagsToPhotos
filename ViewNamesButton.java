package photo_viewer;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import photo_manager.Photo;

import javafx.collections.ObservableList;

import java.util.ArrayList;

class ViewNamesButton {

    Button viewNames = new Button("View Past Names");

    ViewNamesButton(TextArea tagText, TextArea pathText, ListView<Photo> listView, ObservableList<Photo> list, ArrayList<Photo> photos,
                    PhotoManager manager) {
        viewNames.setMinWidth(120);
        viewNames.setOnAction(
                (event -> {
                    Photo selectedPhoto = listView.getSelectionModel().getSelectedItem();

                    if (selectedPhoto != null) {
                        for (Photo p : photos) {
                            if (selectedPhoto.equals(p)) {
                                selectedPhoto = p;
                            }
                        }

                        // Creates a new stage and scene for the Names window.
                        Stage nameViewer = new Stage();
                        nameViewer.setTitle("Past Names");
                        VBox nameViewerPane = new VBox();
                        Scene nameViewerScene = new Scene(nameViewerPane, 800, 400);
                        nameViewer.setScene(nameViewerScene);

                        // Creates a list of all the past names of the selected photo.
                        ListView<String> nameView = new ListView<>();
                        nameView.setItems(FXCollections.observableArrayList(selectedPhoto.getNameLog()));
                        nameViewerPane.getChildren().add(nameView);

                        nameViewer.show();

                        // Creates a RevertToOldName Tag button.
                        Button revert = new Button("Revert");
                        revert.setMinWidth(nameViewerPane.getWidth());
                        nameViewerPane.getChildren().add(revert);

                        // Makes Remove Tag remove the selected tag in the list of tags.
                        Photo finalSelectedPhoto = selectedPhoto;
                        selectedPhoto.transferValues(finalSelectedPhoto);
                        revert.setOnAction(
                                (event2 -> {
                                    if (nameView.getSelectionModel().getSelectedItems().size() != 0) {
                                        finalSelectedPhoto.revertToThisStage(
                                                nameView.getSelectionModel().getSelectedItem(),
                                                manager);
                                        list.set(list.indexOf(finalSelectedPhoto), finalSelectedPhoto);
                                        listView.getSelectionModel().select(list.indexOf(finalSelectedPhoto));
                                        listView.setItems(list);
                                        nameView.setItems(FXCollections.observableArrayList(finalSelectedPhoto.getNameLog()));
                                        tagText.setText(finalSelectedPhoto.getStringOfTags());
                                        pathText.setText(finalSelectedPhoto.getPath());// Added this here
                                        listView.getSelectionModel().getSelectedItem().setTags(finalSelectedPhoto.getTags());
                                        AlertBox.display("Will revert once you close this screen");
                                    } else {
                                        AlertBox.display("Select name to revert to!");
                                    }
                                }));
                    } else {
                        AlertBox.display("Please select an image!");
                    }
                }
                )
        );
    }
}
