package photo_viewer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import photo_manager.Photo;
import photo_manager.Tag;

import java.util.ArrayList;

class DefaultTagsButton {

    Button defaultTags = new Button("Default Tags");

    DefaultTagsButton(TextArea tagText,TextArea pathText, ListView<Photo> listView, ObservableList<Photo> list,
                      ArrayList<Photo> photos, ArrayList<Tag> tags, PhotoManager managerPhoto, TagManager managerTag) {
        defaultTags.setMinWidth(115);
        defaultTags.setOnAction(
                (event -> {
                    if (listView.getSelectionModel().getSelectedItem() != null) {
                        // Creates a new stage and scene for the Tags window.
                        Stage tagViewer = new Stage();
                        tagViewer.setTitle("Tags");
                        VBox tagViewerPane1 = new VBox();



                        // Creates a list of all the tags of the selected photo.
                        Label defaultTagView = new Label("Default Tags");
                        defaultTagView.setAlignment(Pos.CENTER);
                        Label photosTags = new Label("Photo's Tags");
                        photosTags.setAlignment(Pos.CENTER);
                        ListView<Tag> tagView = new ListView<>();
                        tagView.setItems(
                                FXCollections.observableArrayList(
                                        listView.getSelectionModel().getSelectedItem().getTags()));
                        tagViewerPane1.getChildren().addAll(photosTags , tagView);
                        tagViewerPane1.setMinWidth(400);
                        VBox tagViewerPane2 = new VBox();
                        ListView<Tag> tagView1 = new ListView<>();
                        tagView1.setItems(FXCollections.observableArrayList(tags));
                        tagViewerPane2.getChildren().addAll(defaultTagView, tagView1);
                        tagViewerPane2.setMinWidth(300);
                        GridPane pane = new GridPane();


                        pane.addColumn(0, tagViewerPane1);

                        pane.addColumn(1, tagViewerPane2);


                        HBox addRemoveBox = new HBox();

                        // Creates an Add Tag button.
                        Button addTag = new Button("Add Default Tag to Photo");
                        addTag.setMinWidth(180);

                        addRemoveBox.getChildren().add(addTag);
                        tagView1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

                        // Makes the Add Tag button add a tag with text from the text area.
                        addTag.setOnAction(
                                (event2 -> {
                                    Photo photo = listView.getSelectionModel().getSelectedItem();
                                    for (Photo p : photos) {
                                        if (photo.equals(p)) {
                                            photo = p;
                                        }
                                    }

                                    if (tagView1.getSelectionModel().getSelectedItems().size() != 0) {
                                        photo.addTag(tagView1.getSelectionModel().getSelectedItems());
                                        tagView.setItems(FXCollections.observableArrayList(photo.getTags()));
                                        managerPhoto.changeNameOfPhoto(photo);
                                        tagText.setText(photo.getStringOfTags());
                                        list.set(list.indexOf(photo), photo);
                                        listView.getSelectionModel().select(list.indexOf(photo));
                                        listView.setItems(list);
                                        tagText.setText(listView.getSelectionModel().getSelectedItem().getStringOfTags());
                                        pathText.setText(photo.getPath());

                                    } else {
                                        AlertBox.display("Select tag to remove!");
                                    }
                                    tagView.setItems(
                                            FXCollections.observableArrayList(
                                                    listView.getSelectionModel().getSelectedItem().getTags()));
                                }));

                        // Creates a Remove Tag button.
                        Button removeTag = new Button("Remove Default Tag from Photo");
                        removeTag.setMinWidth(220);
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
                                        for (Tag t : tagView.getSelectionModel().getSelectedItems())
                                            photo.removeTag(t);
                                        tagView.setItems(FXCollections.observableArrayList(photo.getTags()));
                                        managerPhoto.changeNameOfPhoto(photo);
                                        list.set(list.indexOf(photo), photo);
                                        listView.getSelectionModel().select(list.indexOf(photo));
                                        listView.setItems(list);
                                        tagText.setText(listView.getSelectionModel().getSelectedItem().getStringOfTags());
                                        pathText.setText(photo.getPath());

                                    } else {
                                        AlertBox.display("Select tag to remove!");
                                    }
                                }));

                        tagViewerPane1.getChildren().add(addRemoveBox);
                        Scene tagViewerScene = new Scene(pane, 700, 400);
                        tagViewer.setScene(tagViewerScene);
                        tagViewer.show();

                    } else {
                        // Creates a new stage and scene for the Tags window.
                        Stage tagViewer = new Stage();
                        tagViewer.setTitle("Default Tags");
                        VBox tagViewerPane = new VBox();
                        Scene tagViewerScene = new Scene(tagViewerPane, 600, 400);
                        tagViewer.setScene(tagViewerScene);

                        // Creates a list of all the tags of the selected photo.
                        ListView<Tag> tagView = new ListView<>();
                        tagView.setItems(FXCollections.observableArrayList(tags));
                        tagViewerPane.getChildren().add(tagView);
                        HBox addRemoveBox = new HBox();

                        // Creates an Add Tag button.
                        Button addTag = new Button("Add a Default Tag");
                        addTag.setMinWidth(tagViewerPane.getWidth() / 2);

                        // Creates a text area for Add Tag button.
                        TextArea addTagTextArea = new TextArea();
                        addTagTextArea.setPrefColumnCount(1);
                        addRemoveBox.getChildren().add(addTag);
                        tagViewerPane.getChildren().add(addTagTextArea);

                        // Makes the Add Tag button add a tag with text from the text area.
                        addTag.setOnAction(
                                (event2 -> {
                                    if (!addTagTextArea.getText().equals("")) {
                                        Tag newtag = new Tag(addTagTextArea.getText().trim());
                                        managerTag.addTags(newtag);

                                    } else {
                                        AlertBox.display("Can't add empty tags!");
                                    }

                                    tagView.setItems(FXCollections.observableArrayList(tags));
                                }));

                        Button removeTag = new Button("Remove a Default Tag");
                        removeTag.setMinWidth(tagViewerPane.getWidth() / 2);
                        addRemoveBox.getChildren().add(removeTag);

                        // Makes Remove Tag remove the selected tag in the list of tags.
                        removeTag.setOnAction(
                                (event2 -> {
                                    if (tagView.getSelectionModel().getSelectedItems().size() != 0) {
                                        for (Tag t : tagView.getSelectionModel().getSelectedItems())
                                            managerTag.removeTag(t);
                                        tagView.setItems(FXCollections.observableArrayList(tags));
                                    } else {
                                        AlertBox.display("Select tag to remove!");
                                    }
                                }));

                        tagViewerPane.getChildren().add(addRemoveBox);

                        tagViewer.show();
                    }
                }
                )
        );
    }
}
