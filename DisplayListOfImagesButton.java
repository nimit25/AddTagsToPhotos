package photo_viewer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class DisplayListOfImagesButton { // part of code taken from
    // https://stackoverflow.com/questions/29515396/javafx-listview-with-images-instead-of-strings
    Button displayList = new Button("Display List");


    DisplayListOfImagesButton(EventHandler<ActionEvent> loadHandler, ObservableList<Path> imageFiles, TextField directoryField) {
        displayList.setOnAction(event -> {
                    Stage imageViewer = new Stage();
                    imageViewer.setTitle("Display a List Of Images in The Main Directory");
//            ObservableList<Path> imageFiles = FXCollections.observableArrayList();

                    Button loadButton = new Button("Click Load");

            imageFiles.setAll(load(Paths.get(directoryField.getText())));
                    loadButton.setOnAction(loadHandler);


                    HBox controls = new HBox( );
                    controls.setAlignment(Pos.CENTER);
                    controls.setPadding(new Insets(5));

                    ListView<Path> imageFilesList = new ListView<>(imageFiles);
                    imageFilesList.setCellFactory(listView -> new ListCell<Path>() {
                        private final ImageView imageView = new ImageView();

                        @Override
                        public void updateItem(Path path, boolean empty) {

                            super.updateItem(path, empty);
                            if (empty) {
                                setText(null);
                                setGraphic(null);
                            } else {
                                setText(path.getFileName().toString());
                                imageView.setImage(new Image(path.toUri().toString(), 80, 160, true, true, true));
                                setGraphic(imageView);
                            }
                        }
                    });


                    ImageView imageView = new ImageView();
                    imageFilesList.getSelectionModel().selectedItemProperty().addListener((obs, oldFile, newFile) -> {
                        if (newFile == null) {
                            imageView.setImage(null);
                        } else {
                            imageView.setImage(new Image(newFile.toUri().toString(), true));
                        }
                    });
                    imageFilesList.setMinWidth(500);

                    StackPane imageHolder = new StackPane(imageView);
                    imageView.fitWidthProperty().bind(imageHolder.widthProperty());
                    imageView.fitHeightProperty().bind(imageHolder.heightProperty());
                    imageView.setPreserveRatio(true);

                    BorderPane root = new BorderPane(imageHolder, null, null, null, imageFilesList);

                    Scene scene = new Scene(root, 800, 600);
                    imageViewer.setScene(scene);
                    imageViewer.show();
                }
        );
    }

    static List<Path> load(Path directory) {
        List<Path> files = new ArrayList<>();
        try {
            Files.newDirectoryStream(directory, "*.{jpg,bmp,png,gif,JPG,BMP,GIF,PNG}").forEach(files::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }
}

