package photo_viewer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import photo_manager.Photo;
import photo_manager.Tag;

import java.util.ArrayList;

class ViewTagsButton {

    Button viewTags = new Button("View Tags");

    ViewTagsButton(TextArea tagText, TextArea pathText, ListView<Photo> listView, ObservableList<Photo> list,
                   ArrayList<Photo> photos, PhotoManager manager) {
        viewTags.setOnAction(
                (event -> {
                    if (listView.getSelectionModel().getSelectedItem() != null) {
                        // Creates a new stage and scene for the Tags window.
                        Stage tagViewer = new Stage();
                        tagViewer.setTitle("Tags");
                        VBox tagViewerPane = new VBox();
                        Scene tagViewerScene = new Scene(tagViewerPane, 600, 400);
                        tagViewer.setScene(tagViewerScene);

                        // Creates a list of all the tags of the selected photo.
                        ListView<Tag> tagView = new ListView<>();
                        tagView.setItems(
                                FXCollections.observableArrayList(
                                        listView.getSelectionModel().getSelectedItem().getTags()));
                        tagViewerPane.getChildren().add(tagView);
                        HBox addRemoveBox = new HBox();

                        // Creates an Add Tag button.
                        Button addTag = new Button("Add Tag");
                        addTag.setMinWidth(tagViewerPane.getWidth() / 2);

                        // Creates a text area for Add Tag button.
                        TextArea addTagTextArea = new TextArea();
                        addTagTextArea.setPrefColumnCount(1);
                        addRemoveBox.getChildren().add(addTag);
                        tagViewerPane.getChildren().add(addTagTextArea);

                        // Makes the Add Tag button add a tag with text from the text area.
                        addTag.setOnAction(
                                (event2 -> {
                                    Photo photo2 = listView.getSelectionModel().getSelectedItem();

                                    if (photo2 != null) {
                                        for (Photo p : photos) {
                                            if (photo2.equals(p)) {
                                                photo2 = p;
                                            }
                                        }
                                        if (!addTagTextArea.getText().equals("")) {
                                            Tag newtag = new Tag(addTagTextArea.getText().trim());
                                            photo2.addTag(newtag);
                                            // adds tag here
                                            list.set(list.indexOf(photo2), photo2);
                                            listView.getSelectionModel().select(list.indexOf(photo2));
                                            listView.setItems(list);
                                            manager.changeNameOfPhoto(photo2);
                                            tagText.setText(listView.getSelectionModel().getSelectedItem().getStringOfTags());
                                            pathText.setText(photo2.getPath());
                                        } else {
                                            AlertBox.display("Can't add empty tags!");
                                        }
                                    }
                                    tagView.setItems(
                                            FXCollections.observableArrayList(
                                                    listView.getSelectionModel().getSelectedItem().getTags()));
                                }));

                        // Creates a Remove Tag button.
                        Button removeTag = new Button("Remove Tag");
                        removeTag.setMinWidth(tagViewerPane.getWidth() / 2);
                        addRemoveBox.getChildren().add(removeTag);

                        // Makes Remove Tag remove the selected tag in the list of tags.
                        removeTag.setOnAction(
                                (event2 -> {
                                    Photo photo = listView.getSelectionModel().getSelectedItem();
                                    for (Photo p : photos) {
                                        if (photo.equals(p)) {
                                            photo = p;
                                        }
                                    }

                                    if (tagView.getSelectionModel().getSelectedItems().size() != 0) {
                                        for (Tag t : tagView.getSelectionModel().getSelectedItems()) {
                                            photo.removeTag(t);
                                            pathText.setText(photo.getPath());
                                        }
                                        tagView.setItems(FXCollections.observableArrayList(photo.getTags()));
                                        list.set(list.indexOf(photo), photo);
                                        listView.getSelectionModel().select(list.indexOf(photo));
                                        listView.setItems(list);
                                        manager.changeNameOfPhoto(photo);
                                        tagText.setText(listView.getSelectionModel().getSelectedItem().getStringOfTags());
                                    } else {
                                        AlertBox.display("Select tag to remove!");
                                    }
                                }));

                        tagViewerPane.getChildren().add(addRemoveBox);

                        tagViewer.show();
                    } else {
                        AlertBox.display("Please select an image!");
                    }
                }));

        viewTags.setMinWidth(110);
    }
}
