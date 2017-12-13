package photo_viewer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;
import photo_manager.*;

/**
 * The PhotoManager class handles serialization and deserialization of Photos, so that after the program is closed and
 * reopened, the program will retain information about all Photos. PhotoManager has an ArrayList photos that stores all
 * Photos.
 */
public class PhotoManager   {

    /**
     *
     */
    private static final Logger logger =
            Logger.getLogger(PhotoManager.class.getName());

    /**
     *
     */
    private static final Handler consoleHandler = new ConsoleHandler();

    /**
     * The list of all the Photos that PhotoManager stores.
     */
    private ArrayList<Photo> photos;

    /**
     * Constructs a PhotoManager. PhotoManager is responsible for handling serialization and deserialization of Photos.
     * This class is able to read from the serialization file and write to it.
     *
     * @param filePath serialization file that PhotoManger reads from.
     * @throws ClassNotFoundException thrown if ---------------
     * @throws IOException thrown if the serialized file is not found; a replacement file will be created.
     */
    public PhotoManager(String filePath) throws ClassNotFoundException, IOException {
        photos = new ArrayList<>();

        // Associate the handler with the logger.
        logger.setLevel(Level.ALL);
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);

        // Reads serializable objects from file.
        // Populates the record list using stored data, if it exists.
        File file = new File(filePath);
        if (file.exists()) {
            readFromFile(filePath);
        } else {
            boolean newFile = file.createNewFile();
        }
    }

    /**
     * Writes the Photos to the serialization file specified by filePath.
     *
     * @param filePath the serialization file to write the Photos to.
     * @throws IOException thrown if the file is not found.
     */
    void saveToFile(String filePath) throws IOException {

        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        // serialize the Map
        output.writeObject(photos);
        output.close();
    }

    /**
     * Adds the specified Photo to the list of Photos.
     *
     * @param photo Photo to add to the list of Photos.
     */
    void addPhoto(Photo photo){

        if (!photos.contains(photo)) {
            this.photos.add(photo);
        } else {
            for (int i = 0; i < photos.size(); i ++){
                if (photos.get(i).equals(photo)){
                    photos.set(i, photo);
                }
            }
        }

    }

    /**
     * Reads from the given serialization file.
     *
     * @param path the path for the serialization file.
     * @return ArrayList<Photo> the list of photos stored by PhotoManager.
     * @throws ClassNotFoundException thrown if --------------
     */
    @SuppressWarnings("unchecked")
    ArrayList<Photo> readFromFile(String path) throws ClassNotFoundException {
        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            //deserialize the Map
            photos = (ArrayList<Photo>) input.readObject();
            input.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Cannot read from input.", ex);
        }
        return photos;
    }

    /**
     * Returns a string representation of PhotoManager. This string representation consists of the list of tags of each
     * Photo in the list of Photos.
     *
     * @return a string representation of PhotoManger.
     */
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        for (Photo p: photos){
            result.append(p.getTags());
            result.append(",");
        }
        return new String(result);
    }

    /**
     * Renames the name of the file represented by the given Photo in the user's OS.
     *
     * @param photo the Photo that represents the file that needs to be renamed.
     */
    public void changeNameOfPhoto(Photo photo){
                String newName = photo.getPhotoName();
                if (this.checkContains(newName)){ // can make a method for checking this
                    Photo newFile = new Photo(photo.getParent() + File.separator + newName);
                    photo.renameTo(newFile);
                    photo.transferValues(newFile);
                    photo = newFile; // changes the path
                } else {
                    Photo newFile = new Photo(photo.getParent() + File.separator + newName + photo.getPhotoType());
                    photo.renameTo(newFile);
                    photo.transferValues(newFile);
                    photo = newFile;  // changes the path

                }

                this.addPhoto(photo); // tried to add the statement here
    }

    /**
     * Moves the file represented by the given Photo in the user's OS.
     *
     * @param newName the new name for the file represented by the given Photo.
     * @param newPath the new path that the file represented by the given Photo will be moved to.
     * @param photo the Photo that represents the file that needs to be moved.
     */
    void movePhoto(String newName, String newPath, Photo photo) {
        if (this.checkContains(photo.getPhotoName())) {
            Photo newFile = new Photo(newPath + File.separator + newName);
            photo.renameTo(newFile);
            photo.transferValues(newFile);
            photo = newFile;


        } else {
            Photo newFile = new Photo(newPath + File.separator + newName + photo.getPhotoType());
            photo.renameTo(newFile);
            photo.transferValues(newFile);
            photo = newFile;


        }
        this.addPhoto(photo);
    // need to update the file after changing it
    }

    /**
     * Returns whether a file is an instance of any of the image types.
     *
     * @param name the name of the file to check for.
     * @return whether the file is an instance of any of the image types.
     */
    private boolean checkContains(String name){
         String[] containsThisType = {".jpg", ".png", ".gif", ".bmp", ".JPG", ".PNG", ".GIF", ".BMP"};
         for (String type: containsThisType){
             if (name.contains(type)){
                 return true;
             }
         }
         return false;
    }

    /**
     * Returns the list of Photos that PhotoManager stores.
     *
     * @return the list of Photos that PhotoManager stores.
     */
    ArrayList<Photo> getPhotos() {
        return photos;
    }

}
