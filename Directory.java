package photo_manager;

import java.io.FilenameFilter;
import java.util.ArrayList;
import java.io.File;
import photo_viewer.Main;

/**
 * A Directory which takes in a path on the computer and assigns a Photo object to all image files present in that path,
 * and Directory also assigns a Directory object to all folders in that path. The Directory class responsible for being
 * able to store and return these objects.
 */
public class Directory implements Comparable<Directory> {

    /**
     * The list of all subdirectories in this directory.
     */
    private ArrayList<Directory> subdirectories;

    /**
     * The list of all photos in this directories (not including photos in subdirectories).
     */
    private ArrayList<Photo> photos;

    /**
     * Constructs a Directory Object. A Directory is responsible for creating an Photo ArrayList photos for all Files
     * that end with .jpg, .png, .gif, .bmp and their uppercase variants. The Directory also creates a Directory ArrayList
     * subdirectories for all sub-folders. The Directory is capable of retrieving all Photo Files present within the
     * Directory and subdirectories.
     *
     * @param path the path to the File the contains the Directory.
     */
    public Directory(String path) {
        this.photos = new ArrayList<>();
        this.subdirectories = new ArrayList<>();
        File directory = new File(path);

        /* Creates a list of Files that end with jpg. */
        File[] fileList = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String[] containsThisType = {".jpg", ".png", ".gif", ".bmp", ".JPG", ".PNG", ".GIF", ".BMP"};
                for (String type: containsThisType){
                    if (name.contains(type)){
                        return true;
                    }
                }
                return false;

            }
        });

        /* Creates an instance of Photo for each one of these Files. */
        if (fileList != null) {
            for (File f : fileList) {
                    if (Main.storedPhotos.contains(new Photo(f.getPath()))) {
                        Photo p = Main.storedPhotos.get(Main.storedPhotos.indexOf(new Photo(f.getPath())));
                        photos.add(p);
                    } else {
                        photos.add(new Photo(f.getPath()));
                    }


            }
        }
        /* Creates a list of all subdirectories. */
        File[] directoryList = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory();
            }
        });

        /* Adds all the subdirectories to the TreeSet. */
        if (directoryList != null) {
            for (File f : directoryList) {
                this.subdirectories.add(new Directory(f.getPath()));
            }
        }
    }

    /**
     * Returns a list of all photos in this Directory and subdirectories. Each Photo corresponds to a file on the
     * computer of type .jpg, .png, .gif, .bmp.
     *
     * @return the ArrayList of all Photo Files in this Directory and in subdirectories.
     */
    public ArrayList<Photo> getAllImages(){
        ArrayList<Photo> photos = this.photos;
        for (Directory d : this.subdirectories){
            photos.addAll(d.getAllImages());
        }

        return photos;
    }

    /**
     * Compares this Directory to a different Directory by comparing the number of Photo objects present within each
     * Directory.
     *
     * @param o the Directory to be compared to.
     * @return true if the Directory has the same number of Photo objects as this Directory, false otherwise.
     */
    @Override
    public int compareTo(Directory o) {
        return Integer.compare(this.getAllImages().size(), o.getAllImages().size());
    }
}