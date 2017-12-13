package photo_viewer;

import photo_manager.Photo;
import photo_manager.Tag;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The TagManager class is responsible for serialization-deserialization of the
 * Tag objects that are created. The class is able to both read from the serialized file and write to it. Tag objects
 * can also be added and removed.
 */
public class TagManager {

    /**
     * A Logger for TagManager class name.
     */
    private static final Logger logger =
            Logger.getLogger(TagManager.class.getName());

    /**
     * ---------
     */
    private static final Handler consoleHandler = new ConsoleHandler();

    /**
     * An ArrayList of all Tag objects that are stored by this TagManager.
     */
    private ArrayList<Tag> tags;

    /**
     * Constructor for a TagManager class. The constructor reads from the the serialized file at the given file path and
     * writes the tags found within that file to the ArrayList tags. If there is no file at that path, then the
     * constructor creates a new file.
     *
     * @param filePath the path to the serialized file.
     * @throws ClassNotFoundException thrown if ------
     * @throws IOException thrown if the serialized file is not found, a replacement file will be created.
     */
    public TagManager(String filePath) throws ClassNotFoundException, IOException {
        tags = new ArrayList<>();

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
     * Writes all of the Tag objects stored in the ArrayList tags. All the serializable information will be transferred
     * to the File at the given filePath.
     *
     * @param filePath the path to the serialized file.
     * @throws IOException thrown if the file is not found.
     */
    void saveToFile(String filePath) throws IOException {

        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        // serialize the Map
        output.writeObject(tags);
        output.close();
    }

    /**
     * Adds a Tag to the ArrayList tags, the new Tag will be written to the serialized file on program close.
     *
     * @param tag the Tag that is added to the ArrayList tags.
     */
    void addTags(Tag tag) {

        if (!tags.contains(tag)) {
            this.tags.add(tag);
        }

    }

    /**
     * Reads all of the Tag objects from the serialized file. The Tag objects are stored in the ArrayList tags.
     *
     * @param path the path of the serialized file.
     * @return the new ArrayList of Tag objects.
     * @throws ClassNotFoundException thrown if ------
     */
    @SuppressWarnings("unchecked")
    ArrayList<Tag> readFromFile(String path) throws ClassNotFoundException {

        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            //deserialize the Map
            tags = (ArrayList<Tag>) input.readObject();
            input.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Cannot read from input.", ex);
        }
        return tags;
    }

    /**
     * Gets the ArrayList of Tag objects.
     *
     * @return the ArrayList of Tag objects.
     */
    public ArrayList<Tag> getTags() {
        return tags;
    }

    /**
     * Returns a string representation of all of the Tag objects stored in tags.
     *
     * @return a string representation of all of the Tag objects stored in tags.
     */
    @Override
    public String toString(){
        return tags.toString();
    }

//    public void addTagsToPhoto(Photo photo){ // method you call in the beginning
//        for (Tag t: tags){
//            if (!photo.getTags().contains(t) && !photo.getOldTags().contains(t) ){
//                photo.addTag(t);
//            }
//        }
//    }

    /**
     * Removed a Tag from the ArrayList of Tag objects, the Tag will no longer be written to the serialized file when
     * the program closes.
     *
     * @param tag the Tag that is removed.
     */
    void removeTag(Tag tag){
        tags.remove(tag);
    }

    /**
     * Adds all Tag objects from a Photo to the ArrayList tags unless the Tag objects are already present within the
     * list. Only the Tag objects that are missing from the list are added.
     *
     * @param photo the Photo whose Tag objects to add.
     */
    void addTagsFromPhoto(Photo photo){
        for (Tag t: photo.getTags()){
            this.addTags(t);
        }
    }
}
