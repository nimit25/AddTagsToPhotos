package photo_viewer;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import photo_manager.Directory;
import photo_manager.Photo;
import photo_manager.Tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * The Main class responsible for handling most of the front-end. Will both initialize all relevant
 * databases and display the program to the user through the use of javafx.
 */
public class Main extends Application {

  /** The List of Photos that is displayed in the ListView on the left side of the screen. */
  private static ObservableList<Photo> photoList;

  /** The javafx stage for the Main Window. */
  private Stage window;

  /** The List of Photos used for serialization. */
  public ArrayList<Photo> photos = new ArrayList<>();

  /** The PhotoManager which handles serialization and deserialization. */
  private PhotoManager photoManager;

  /** The path to the file containing all of the serialized Objects */
  private String path = "./serializedobjects.ser";

  /** The path to the file containing all of the serialized Tags */
  private String pathForTagManager = "./serializedtags.ser";

  /** The TagManager that handles serialization and deserialization of Tags. */
  private TagManager tagManager;

  /** All Photos that have been read from the serialized file. */
  public static ArrayList<Photo> storedPhotos;

  /** The current opened Directory. All Image files in this Directory will be displayed. */
  private static Directory mainDirectory;

  /** The width of the Slideshow window. */
  private static final int SLIDESHOW_WIDTH = 720;

  /** The height of the Slideshow window. */
  private static final int SLIDESHOW_HEIGHT = 480;

  /**
   * Main method that is responsible for launching the javafx.
   *
   * @param args the supplied command line arguments.
   */
  public static void main(String[] args) {
    photoList = FXCollections.observableArrayList();
    launch(args);
  }

  /**
   * The front-end of the program. This method is responsible for initializing and displaying all of
   * the relevant javafx Objects as well as managing all of the important commands that the user can
   * perform.
   *
   * @param primaryStage the Stage for the Main window.
   * @throws Exception whenever something does not go according to plan.
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    ObservableList<Path> imageFiles = FXCollections.observableArrayList();
    TextField directoryField = new TextField();
    window = primaryStage;
    primaryStage.setTitle("Photo Viewer 2017 Premium");
    tagManager = new TagManager(pathForTagManager);
    ArrayList<Tag> storedTags = tagManager.readFromFile(pathForTagManager);
    photoManager = new PhotoManager(path);
    storedPhotos = photoManager.readFromFile(path);
    window = primaryStage;
    primaryStage.setTitle("Photo Viewer 2017 Premium");
    EventHandler<ActionEvent> loadHandler =
        event ->
            imageFiles.setAll(DisplayListOfImagesButton.load(Paths.get(directoryField.getText())));
    directoryField.setOnAction(loadHandler);

    /* Sets up the layout of the program using BorderPane */
    BorderPane mainPane = new BorderPane();
    Scene mainScene = new Scene(mainPane, 850, 430);
    primaryStage.setScene(mainScene);

    /* Creates the top pane of the program (Enter Directory button with text area) */
    HBox topPane = new HBox();
    Button enterDirectory = new Button("Choose Directory");
    enterDirectory.setMinWidth(110);
    enterDirectory.setOnAction(
        (event -> {
          DirectoryChooser directoryChooser = new DirectoryChooser();
          directoryChooser.setTitle("Select Directory");
          File result = directoryChooser.showDialog(primaryStage);
          if (result != null) {
            mainDirectory = new Directory(result.getPath());
            photoList.clear();
            photoList.addAll(mainDirectory.getAllImages());
            directoryField.setText(result.toString());
            imageFiles.setAll(DisplayListOfImagesButton.load(result.toPath()));
          }
        }));

    /* Align the Enter Directory button and Enter Directory text area. */
    enterDirectory.setMaxHeight(Double.MAX_VALUE);
    enterDirectory.setMinWidth(150);

    /* Add the Enter Directory button/text area to the top pane. */
    topPane.getChildren().add(enterDirectory);
    mainPane.setTop(topPane);

    /* Add a tagTextArea to show the tags as a String. */
    TextArea tagTextArea = new TextArea();
    tagTextArea.setEditable(false);
    tagTextArea.setPrefRowCount(1);
    tagTextArea.setMinHeight(60);
    tagTextArea.setStyle("-fx-text-fill: black;");

    /* Add tagPathArea to show the full path of the file as a String. */
    TextArea tagPathArea = new TextArea();
    tagPathArea.setEditable(false);
    tagPathArea.setPrefRowCount(1);
    tagPathArea.setMinHeight(30);
    tagPathArea.setStyle("-fx-text-fill: black;");

    /* Create a picturePane, the area where the picture will show up. */
    StackPane picturePane = new StackPane();
    picturePane.setMinHeight(300);

    /* Create center pane that displays the image and add elements to it. */
    VBox centerPane = new VBox();
    centerPane.getChildren().add(tagTextArea);
    centerPane.getChildren().add(picturePane);
    centerPane.getChildren().add(tagPathArea);
    mainPane.setCenter(centerPane);

    /* Add the left pane containing the list of images. */
    VBox leftPane = new VBox();
    ListView<Photo> imageListView = new ListView<>();
    imageListView.setItems(photoList);
    leftPane.getChildren().add(imageListView);
    leftPane.setMaxWidth(150);
    imageListView
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              picturePane.getChildren().clear();
              ImageView imgView = new ImageView();
              imgView.setPreserveRatio(true);
              imgView.setFitHeight(250);
              imgView.setFitWidth(250);
              try {
                if (imageListView.getSelectionModel().getSelectedItem() == null)
                  imgView.setImage(null);
                else {
                  Image img =
                      new Image(
                          new FileInputStream(
                              imageListView.getSelectionModel().getSelectedItem().getFile()));
                  imgView.setImage(img);
                  for (Photo p : imageListView.getItems()) {
                    for (Photo i : storedPhotos) {
                      if (p.equals(i)) {
                        p = i;
                        tagManager.addTagsFromPhoto(p);
                      }
                    }
                  }
                }
              } catch (FileNotFoundException e) {
                AlertBox.display("File not found!");
              }
              picturePane.getChildren().add(imgView);
              Photo photo3 = imageListView.getSelectionModel().getSelectedItem();
              if (photo3 != null) {
                for (Photo p : storedPhotos) {
                  if (photo3.equals(p)) {
                    photo3 = p;
                  }
                }
                tagTextArea.setText(photo3.getStringOfTags());
                tagPathArea.setText(photo3.getAbsolutePath());
              }
            }));
    mainPane.setLeft(leftPane);

    /* Creates a button to view tags. */
    ViewTagsButton viewTagsButton =
        new ViewTagsButton(
            tagTextArea, tagPathArea, imageListView, photoList, storedPhotos, photoManager);

    /* Creates a button to view all past names. */
    ViewNamesButton viewNamesButton =
        new ViewNamesButton(
            tagTextArea, tagPathArea, imageListView, photoList, storedPhotos, photoManager);

    /* Creates a button to view rename history. */
    ViewRenameButton viewRenameButton = new ViewRenameButton(imageListView);

    /* Creates a button to move photos. */
    MovePhotoButton movePhotoButton =
        new MovePhotoButton(
            primaryStage,
            picturePane,
            tagTextArea,
            imageListView,
            photoList,
            storedPhotos,
            photoManager);

    /* Creates a button to view default tags. */
    DefaultTagsButton defaultTagsButton =
        new DefaultTagsButton(
            tagTextArea, tagPathArea, imageListView, photoList, storedPhotos, storedTags, photoManager, tagManager);

    /* Creates a button to view images as a slideshow. */
    SlideshowButton slideshowButton =
        new SlideshowButton(SLIDESHOW_WIDTH, SLIDESHOW_HEIGHT, imageListView, photoList);

    DisplayListOfImagesButton displayListOfImagesButton =
        new DisplayListOfImagesButton(loadHandler, imageFiles, directoryField);

    /* Add all the buttons to the main window of the program. */
    topPane.getChildren().add(viewTagsButton.viewTags);
    topPane.getChildren().add(viewNamesButton.viewNames);
    topPane.getChildren().add(viewRenameButton.viewRename);
    topPane.getChildren().add(movePhotoButton.movePhoto);
    topPane.getChildren().add(defaultTagsButton.defaultTags);
    topPane.getChildren().add(displayListOfImagesButton.displayList);
    leftPane.getChildren().add(slideshowButton.slideshow);

    window.setOnCloseRequest(
        e -> {
          photos.addAll(imageListView.getItems());
          e.consume();
          closeProgram();
        });

    primaryStage.show();
  }

  /** Manages the closing of the Main Window. */
  private void closeProgram() {
    try {
      photoManager.saveToFile(path);
      tagManager.saveToFile(pathForTagManager);
    } catch (IOException e) {
      e.printStackTrace();
    }
    window.close();
  }
}
