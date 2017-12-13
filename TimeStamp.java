package photo_manager;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A TimeStamp is used to keep track of an instance of when a Photo is renamed. It contains the current name of the
 * Photo at the time of the renaming, the new name to rename the Photo to, and the time at which the renaming occured.
 */
public class TimeStamp  implements Serializable{

    /**
     * The current name of the Photo at the time of the renaming.
     */
    private String oldName;

    /**
     * The new name to rename the Photo to.
     */
    private String newName;

    /**
     * Constructs a TimeStamp with the given old name and new name.
     *
     * @param oldName the current name of the photo.
     * @param newName the new name of the photo.
     */
    public TimeStamp(String oldName, String newName) {
        this.oldName = oldName;
        this.newName = newName;
    }

    /**
     * Returns the old name of this TimeStamp.
     *
     * @return the old name of this TimeStamp.
     */
    public String getOldName() {
        return this.oldName;
    }

    /**
     * Returns the new name of this TimeStamp.
     *
     * @return the new name of this TimeStamp.
     */
    public String getNewName() {
        return this.newName;
    }

    /**
     * Returns a string representation of this TimeStamp.
     *
     * @return a string representation of this TimeStamp.
     */
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm MM/dd/yy");
        Date date = new Date();
        return "New Name: " + this.getNewName() + ", " + "Old Name: " + this.getOldName() + ", " + dateFormat.format(date);
    }
}
