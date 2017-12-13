package photo_manager;

import java.io.Serializable;

/**
 * A Tag for a Photo. A Tag contains a name.
 */
public class Tag implements Serializable {

    /**
     * The name of this Tag.
     */
    private String name;

    /**
     * Constructs a Tag with the given name.
     *
     * @param name the name of this Tag.
     * @throws NullPointerException if the specified tag name is null.
     */
    public Tag(String name) throws NullPointerException{
        if (!name.equals("")) {
            this.name = "@" + name;
        } else
            throw new NullPointerException("Need to have something");
    }

    /**
     * Returns the name of this Tag.
     *
     * @return the name of this Tag.
     */
    public String getName(){
        return this.name;
    }

    /**
     * Returns a string representation of this Tag. The string representation is the name of this Tag.
     *
     * @return a string representation of this Tag.
     */
    @Override
    public String toString(){
        return this.name;
    }

    /**
     * Compares this Tag to the specified object. The result is true if and only if the argument is an instance of Tag
     * and the argument's name is the same as this Tag's name.
     *
     * @param obj The object to compare this Tag against.
     * @return true if the given object represents a Tag equivalent to this Tag, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Tag) && (this.name.equalsIgnoreCase(((Tag) obj).name)) ;
    }
}
