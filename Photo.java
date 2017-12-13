package photo_manager;

import javafx.collections.ObservableList;
import photo_viewer.PhotoManager;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A Photo class extends File but can only store image Files of type .jpg, .png, .gif, .bmp. The Photo class is
 * responsible for storing an ArrayList of all names the image ever had as well as ArrayLists of TimeStamp and Tag. The
 * Photo class allows the user to add a Tag to the Photo (Which changes the name of the actual image to match), remove
 * any Tag from the Photo, move the image file to a different location, and retrieve all name/tag/timestamp related data.
 */
public class Photo extends File implements Serializable, Cloneable {

    private String path;

    /**
     * The current name of the image.
     */
    private String name;

    /**
     * An ArrayList of all Tags that the Photo currently has. Appended whenever a Tag is added to the Photo.
     */
    private ArrayList<Tag> tags;

    /**
     * An ArrayList of all names the Photo ever had. Appended whenever a Tag is added to the Photo.
     */
    private ArrayList<String> nameLog;

    /**
     * A HashMap that takes in the name of the Photo as key and stores all relevant information in a Storeinfo object.
     */
    private HashMap<String, StoreInfo> photoLog;

    /**
     * An ArrayList of all of the old Tags that the Photo no longer has. Appended whenever a Tag is removed.
     */
    private ArrayList<Tag> oldTags;

    /**
     * An ArrayList of all TimeStamps. Appended whenever a Tag is added to the Photo.
     */
    private ArrayList<TimeStamp> timeStamps;

    /**
     * Constructor for the Photo Object. The constructor is responsible for initializing all of the relevant information
     * in Photo. The constructor also updates nameLog and photoLog with the current information.
     *
     * @param pathName the path to the image file.
     */
    public Photo(String pathName) {
        super(pathName);
        this.path = pathName;
        this.tags = new ArrayList<>();
        this.oldTags = new ArrayList<>();
        this.name = getName();

        this.timeStamps = new ArrayList<>();
        nameLog = new ArrayList<>();
        photoLog = new HashMap<>();
        this.nameLog.add(this.name);
        this.photoLog.put(this.name, new StoreInfo(tags, oldTags, name, timeStamps, nameLog));
    }


    /**
     * Adds a Tag object to the ArrayList tags. The Tag is a string that is appended to the end of the Photo name. The
     * same Tag cannot be added multiple times and an empty Tag cannot be added either.
     *
     * @param tag the Tag being added.
     */
    public void addTag(Tag tag)  {
        Tag trimmedTag = new Tag(tag.getName().replaceAll("@", "").trim());
        if (!this.tags.contains(trimmedTag)) {
            this.tags.add(trimmedTag);
            String oldName = this.name;
            this.name = this.name.substring(0, this.name.indexOf(getPhotoType())) + trimmedTag.toString() + getPhotoType();

            TimeStamp newTimeStamp = new TimeStamp(oldName, this.name);
            this.timeStamps.add(newTimeStamp);
            this.nameLog.add(this.name);
            this.photoLog.put(this.name,new StoreInfo(tags, oldTags, name, timeStamps, nameLog) );
            this.path = this.getParent() + File.separator + name;
        }
    }

    public void addTag(ObservableList<Tag> tags){

        String oldName = this.name;
        int result = 0;
        for (Tag tag: tags){
            if (!this.tags.contains(tag)) {
                result++;
                this.tags.add(tag);
                this.name = this.name.substring(0, this.name.indexOf(getPhotoType())) + tag.toString() + getPhotoType();
                this.path = this.getParent() + File.separator + name;
            }
        }
        if (result > 0) {
            TimeStamp newTimeStamp = new TimeStamp(oldName, this.name);
            this.timeStamps.add(newTimeStamp);
            this.nameLog.add(this.name);
            this.photoLog.put(this.name, new StoreInfo(this.tags, oldTags, name, timeStamps, nameLog));
        }
    }

    /**
     * Sets the name of the Photo to a new name. Does not alter the actual image name or the file path.
     *
     * @param newName the new name of the Photo.
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Returns what type of image file this Photo is. Supported image files include .jpg, .png, .gif, .bmp and their
     * capitalized variants
     *
     * @return the type of image file.
     */
    public String getPhotoType() {
        String[] containsThisType = {".jpg", ".png", ".bmp", ".JPG", ".PNG", ".GIF", ".BMP"};

        for (String a : containsThisType) {
            if (this.getPhotoName().contains(a)) { // we can intentionally leave out a type to return
                return a;
            }
        }
        return ".gif";
    }

    /**
     * Returns a string representation of all of the tags. This method builds and returns a String based on the current
     * tags ArrayList. Each Tag is appended to the end of the String and they are seperated by @ symbols.
     *
     * @return string representation of all Tag objects in tags.
     */
    public String getAllTags() {
        StringBuilder result = new StringBuilder();
        if (this.getName().contains("@")) {
            result.append(this.getName().substring(this.getName().indexOf("@"), this.getName().indexOf(this.getPhotoType())));
        } else if (tags.size() == 0) {
            result.append(this.getName().substring(0, this.getName().indexOf(this.getPhotoType())));
        } else {

            for (Tag t : tags) {
                if (result.indexOf(t.getName()) < 0) {
                    result.append(t.getName());
                }
            }
        }

        return new String(result);
    }


    /**
     * Gets the ArrayList tags which contains Tag objects associated with this Photo.
     *
     * @return the ArrayList of Tag objects.
     */
    public ArrayList<Tag> getTags() {
        return tags;
    }

    /**
     * Gets all of the keys present within photoLog. The keys represent all of the names of the Photo.
     *
     * @return a String representation of all of the keys in photoLog.
     */
    public String getPhotoLogKeys(){
        StringBuilder result = new StringBuilder();
        for (String s: photoLog.keySet()){
            result.append(s);
            result.append("  ");
        }
        return new String(result);
    }

    /**
     * Gets the nameLog. the nameLog is an ArrayList of all the names that this Photo had.
     *
     * @return the ArrayList of names, nameLog.
     */
    public ArrayList<String> getNameLog() {
        return nameLog;
    }

    /**
     * Gets an ArrayList of string representations of timeStamps.
     *
     * @return ArrayList of string representations of timeStamps.
     */
    public ArrayList<String> getTimeStampsString() {
        ArrayList<String> result = new ArrayList<>();
        for (TimeStamp t : this.timeStamps) {
            result.add(t.toString());
        }
        return result;
    }

    /**
     * Removes a Tag from this Photo. the ArrayLists oldTags, nameLog, timeSTamps and photoLog are all updated as a
     * result. The name of the image is changed to correspond to the removal of the Tag. An empty Tag cannot be removed.
     *
     * @param tag the Tag to be removed from the Photo.
     */
    public void removeTag(Tag tag) {
        this.oldTags.add(tag);
        String oldName = this.name;
        tags.remove(tag);
    this.name  = this.name.substring(0, this.name.indexOf("@")) + getStringOfTags() + getPhotoType();
        TimeStamp newTimeStamp = new TimeStamp(oldName, this.name);
        this.timeStamps.add(newTimeStamp);
        this.nameLog.add(this.name);
        if (!photoLog.containsKey(this.name)){
            photoLog.put(this.name, new StoreInfo(tags, oldTags, name, timeStamps, nameLog));
        }
        this.path = this.getParent() + File.separator + name;
    }

    /**
     * Get String representation of this Photo.
     *
     * @return String representation of this Photo.
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Gets the File.
     *
     * @return the File.
     */
    public File getFile() {
        return this;
    }

    /**
     * Compares this Photo object to a different Photo object and returns if they are equal or not. The comparison is
     * made based the name and directory of each Photo.
     *
     * @param obj the obj that is being compared to this Photo.
     * @return true if this Photo shares the same name as the obj (if obj is of instance Photo).
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof  Photo && this.getParent().equalsIgnoreCase(((Photo) obj).getParent())){
            if (((Photo) obj).name.contains("@") && this.name.contains("@")){
                return ((Photo) obj).name.substring(0, ((Photo) obj).name.indexOf("@")).equals(this.name.substring(0, this.name.indexOf("@") ));
            } else if (((Photo) obj).name.contains("@")) {
                return ((Photo) obj).name.substring(0, ((Photo) obj).name.indexOf("@")).equals(this.name.substring(0, this.name.indexOf(getPhotoType())));
            } else if (this.name.contains("@")) {
                return this.name.substring(0, this.name.indexOf("@")).equals(((Photo) obj).name.substring(0, ((Photo) obj).name.indexOf(((Photo) obj).getPhotoType())));
            }else  {
                return ((Photo) obj).name.equals(this.name);
            }
        }
        return false;
    }


    /**
     * Gets the current name of the Photo.
     *
     * @return the name of the Photo.
     */
    public String getPhotoName() {
        return name;
    }

    /**
     * Gets a String representation of the tags ArrayList.
     *
     * @return a String representation of the tags ArrayList.
     */
    public String getStringOfTags(){
        StringBuilder result = new StringBuilder();
        for (Tag t: tags){
            result.append(t.getName());
        }
        return new String(result);
    }

    /**
     * Transfers the information from a different Photo to this Photo.
     *
     * @param photo the Photo whose information to use.
     */
    public void transferValues(Photo photo){
        photo.oldTags = this.oldTags;
        photo.tags = this.tags;
        photo.nameLog = this.nameLog;
        photo.timeStamps = this.timeStamps;
        photo.name = this.name;
        photo.photoLog = this.photoLog;

    }

    /**
     * Updates the information of a Photo to older information that is stored in photoLog.
     *
     * @param name the name of the Photo whose information is being accessed in photoLog.
     * @param photoManager the Photo manager corresponding to the Photo.
     */
    public void revertToThisStage(String name,  PhotoManager photoManager){

        String oldName =  this.name;
        StoreInfo result = photoLog.get(name);
        this.name = result.getName();
        this.nameLog.add(name);
        this.tags = result.getTags();
        this.oldTags = result.getOldTags();
        TimeStamp newTimeStamp = new TimeStamp(oldName, this.name);
        this.timeStamps.add(newTimeStamp);
        photoManager.changeNameOfPhoto(this);
        this.path = this.getParent() + File.separator + name;
    }

    /**
     * Getter for the path of the Photo instance.
     * @return String of the path
     */
    @Override
    public String getPath(){
        return this.path;
    }

    /**
     * Getter for the oldTags of the Photo instance.
     * @return ArrayList<Tag> of the oldTags
     */
    public ArrayList<Tag> getOldTags() {
        return oldTags;
    }

    /**
     * A setter for tags.
     * @param tags the new set of tags
     */

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }
}




