package photo_manager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A StoreInfo contains information about a Photo's tags, old tags, name, list of TimeStamps, and log of past names.
 *
 * Whenever a Photo's name is changed, a new StoreInfo object is created and stored into the Photo's photoLog. Because
 * this StoreInfo contains all the information about the Photo at this point, this allows the program to revert the
 * Photo back to any given name as well as change the tags to reflect this change.
 */
public class StoreInfo implements Serializable {

    /**
     * The list of tags of the Photo that this StoreInfo object will store.
     */
    private ArrayList<Tag> tags;

    /**
     * The list of old tags of the Photo that this StoreInfo object will store.
     */
    private ArrayList<Tag> oldTags;

    /**
     * The name of the Photo that this StoreInfo object will store.
     */
    private  String name;

    /**
     * The list of TimeStamps of the Photo that this StoreInfo object will store.
     */
    private ArrayList<TimeStamp> timeStamps;

    /**
     * The list of past names of the Photo that this StoreInfo object will store.
     */
    private ArrayList<String> nameLog;

    /**
     * Constructs a StoreInfo class with the given lists of tags and old tags, the given name, the given list of
     * TimeStamps, and the given list of past names.
     *
     * @param tags the list of tags of the Photo that this StoreInfo object will store.
     * @param oldTags the list of old tags of the Photo that this StoreInfo object will store.
     * @param name the name of the Photo that this StoreInfo object will store.
     * @param timeStamps the list of TimeStamps of the Photo that this StoreInfo object will store.
     * @param nameLog the list of past names of the Photo that this StoreInfo object will store.
     */
    @SuppressWarnings("unchecked")
    public StoreInfo(ArrayList<Tag> tags, ArrayList<Tag> oldTags, String name, ArrayList<TimeStamp> timeStamps, ArrayList<String> nameLog){
        this.tags = (ArrayList<Tag>) tags.clone();
        this.oldTags = (ArrayList<Tag>) oldTags.clone();
        this.name = name;
        this.timeStamps = (ArrayList<TimeStamp>) timeStamps.clone();
        this.nameLog = (ArrayList<String>) nameLog.clone();
    }

    /**
     * Returns the log of names that this StoreInfo object stores.
     *
     * @return the log of names that this StoreInfo object stores.
     */
    ArrayList<String> getNameLog() {
        return nameLog;
    }

    /**
     * Returns the list of old tags that this StoreInfo object stores.
     *
     * @return the list of old tags that this StoreInfo object stores.
     */
    @SuppressWarnings("unchecked")
    ArrayList<Tag> getOldTags() {
        return (ArrayList<Tag>) oldTags.clone();
    }

    /**
     * Returns the list of tags that this StoreInfo object stores.
     *
     * @return the list of tags that this StoreInfo object stores.
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Tag> getTags() {
        return (ArrayList<Tag>) tags.clone();
    }

    /**
     * Returns the list of TimeStamps that this StoreInfo object stores.
     *
     * @return the list of TimeStamps that this StoreInfo object stores.
     */
    ArrayList<TimeStamp> getTimeStamps() {
        return timeStamps;
    }

    /**
     * Returns the name that this StoreInfo object stores.
     *
     * @return the name that this StoreInfo object stores.
     */
    public String getName() {
        return name;
    }
}
