package photo_viewer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import photo_manager.Photo;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class SlideshowButton {

    Button slideshow = new Button("Slideshow");

    SlideshowButton(int width, int height, ListView<Photo> listView,
                    ObservableList<Photo> list) {
        slideshow.setOnAction(
                (event -> {
                    if (listView.getSelectionModel().getSelectedItem() != null) {
                        Stage slideshowStage = new Stage();
                        slideshowStage.setTitle("Slideshow");
                        VBox slideshowMainPane = new VBox();

          /* Initializes the center image view of the slideshow. */
                        ImageView slideshowImage = new ImageView();
                        slideshowImage.setPreserveRatio(true);
                        slideshowImage.setFitHeight(250);
                        slideshowImage.setFitWidth(250);

          /* Initializes the left image view of the slideshow. */
                        ImageView slideshowLeftImage = new ImageView();
                        slideshowLeftImage.setPreserveRatio(true);
                        slideshowLeftImage.setFitHeight(125);
                        slideshowLeftImage.setFitWidth(125);

          /* Initializes the right image view of the slideshow. */
                        ImageView slideshowRightImage = new ImageView();
                        slideshowRightImage.setPreserveRatio(true);
                        slideshowRightImage.setFitHeight(125);
                        slideshowRightImage.setFitWidth(125);

                        Scene slideshowScene = new Scene(slideshowMainPane, width, height);
                        slideshowStage.setScene(slideshowScene);

          /* Creates pane to contain all 3 of the images. */
                        AnchorPane slideshowImageCycle = new AnchorPane();
                        slideshowImageCycle.setPrefSize(slideshowScene.getWidth(), slideshowScene.getHeight());
                        /* Anchor all of the images to their respective positives. */
                        slideshowImageCycle.setLeftAnchor(slideshowLeftImage, 50.0);
                        slideshowImageCycle.setTopAnchor(slideshowLeftImage, height * 1.0 / 4 + 125.0 / 2);
                        slideshowImageCycle.setRightAnchor(slideshowRightImage, 50.0);
                        slideshowImageCycle.setTopAnchor(slideshowRightImage, height * 1.0 / 4 + 125.0 / 2);
                        slideshowImageCycle.setLeftAnchor(slideshowImage, width * 1.0 / 3);
                        slideshowImageCycle.setRightAnchor(slideshowImage, width * 1.0 / 3);
                        slideshowImageCycle.setTopAnchor(slideshowImage, height * 1.0 / 8);
                        slideshowImageCycle.getChildren().addAll(slideshowLeftImage, slideshowRightImage, slideshowImage);
                        slideshowImageCycle.setMinSize(0, 0);


          /* Initializes the images. */
                        Image[] initialImages = getSlideShowImages(listView, list);
                        try {
                            slideshowLeftImage.setImage(initialImages[0]);
                            slideshowImage.setImage(initialImages[1]);
                            slideshowRightImage.setImage(initialImages[2]);


          /* Add the Move Left and Move Right buttons to the slideshow window. */
                            slideshowMainPane.getChildren().add(slideshowImageCycle);
                            HBox slideshowButtons = new HBox();
                            slideshowButtons.setSpacing(50);
                            Button moveLeft = new Button("Move Left");
                            Button moveRight = new Button("Move Right");
                            CheckBox auto = new CheckBox("Auto");
                            slideshowButtons.getChildren().add(moveLeft);
                            slideshowButtons.getChildren().add(auto);
                            slideshowButtons.getChildren().add(moveRight);

          /* Make Move Left button move the slideshow left. */
                            moveLeft.setOnAction(
                                    (event2 -> {
                                        if (listView.getSelectionModel().getSelectedIndex() > 0) {
                                            listView
                                                    .getSelectionModel()
                                                    .select(listView.getSelectionModel().getSelectedIndex() - 1);
                                            Image[] images = getSlideShowImages(listView, list);
                                            slideshowLeftImage.setImage(images[0]);
                                            slideshowImage.setImage(images[1]);
                                            slideshowRightImage.setImage(images[2]);
                                        }
                                    }));

          /* Make Move Right move the slideshow right. */
                            moveRight.setOnAction(
                                    (event2 -> {
                                        if (listView.getSelectionModel().getSelectedIndex() < list.size()) {
                                            listView
                                                    .getSelectionModel()
                                                    .select(listView.getSelectionModel().getSelectedIndex() + 1);
                                            Image[] images = getSlideShowImages(listView, list);
                                            slideshowLeftImage.setImage(images[0]);
                                            slideshowImage.setImage(images[1]);
                                            slideshowRightImage.setImage(images[2]);
                                        }
                                    }));
                            slideshowImageCycle.setBottomAnchor(slideshowButtons, height * 1.0 / 8);
                            slideshowImageCycle.setLeftAnchor(slideshowButtons, width * 1.0 / 4);
                            slideshowImageCycle.setRightAnchor(slideshowButtons, width * 1.0 / 4);
                            slideshowImageCycle.getChildren().add(slideshowButtons);
                            slideshowStage.show();

            /* Creates a Timeline that moves the slideshow to the right every three seconds if the Auto Checkbox is
             * checked. */
                            Timeline timer = new Timeline(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {

                                @Override
                                public void handle(ActionEvent event) {
                                    if (slideshowStage.isShowing() && auto.isSelected())
                                        moveRight.fire();
                                }
                            }));
                            timer.setCycleCount(Timeline.INDEFINITE);
                            timer.play();
                        } catch (NullPointerException ne) {
                            AlertBox.display("Loading the Image, please deselect and select the image again to speed up " +
                                    "the process. Thank you");
                        }
                    }
                    else{
                        AlertBox.display("Please select an image!");
                    }
                }));
        slideshow.setMinWidth(150);


    }

    private Image[] getSlideShowImages(ListView<Photo> i, ObservableList<Photo> photoList) {
        Image[] images = new Image[3];
        try {
            Image imageCenter =
                    new Image(new FileInputStream(i.getSelectionModel().getSelectedItem().getFile()));
            images[1] = imageCenter;
            Image imageLeft;
            if (i.getSelectionModel().getSelectedIndex() - 1 >= 0) {
                imageLeft =
                        new Image(
                                new FileInputStream(photoList.get(i.getSelectionModel().getSelectedIndex() - 1)));
                images[0] = imageLeft;
            }
            Image imageRight;
            if (i.getSelectionModel().getSelectedIndex() + 1 < photoList.size()) {
                imageRight =
                        new Image(
                                new FileInputStream(photoList.get(i.getSelectionModel().getSelectedIndex() + 1)));
                images[2] = imageRight;
            }
        } catch (FileNotFoundException e) {
            System.out.print("File not found! Cannot set Slideshow images!");
            return null;
        }
        return images;
    }
}
