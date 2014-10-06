/*
 * Access to directories anf sub directories
 */
package SettingUp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 *
 * @author fang
 */
public class DirectoryAccess {

    private ArrayList<Path> filelist = new ArrayList<Path>();

    public DirectoryAccess(String path) throws IOException {
        PathWalk(path);
    }
    /*
     * Read path and filename in directories and subdirectories
     */

    public void PathWalk(String path) throws IOException {
        Files.walk(Paths.get(path)).forEach(filePath -> {
            if (Files.isRegularFile(filePath)) {
                getFilelist().add(filePath);
            }
        });
        setFilelist(filelist);
    }

    //+++++++++++++++++++ Encupsulation ++++++++++++++++++++++++++++++++++//
    /**
     * @return the filelist
     */
    public ArrayList<Path> getFilelist() {
        return filelist;
    }

    /**
     * @param filelist the filelist to set
     */
    public void setFilelist(ArrayList<Path> filelist) {
        this.filelist = filelist;
    }

}
