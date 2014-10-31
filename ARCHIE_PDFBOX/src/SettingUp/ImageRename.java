/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SettingUp;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author fang
 */
public class ImageRename {

    private String ori_imageName = "";
    private String fig_name;
    private String new_figName = "";
    private String pathName = "";
    private String validString = "";

    public ImageRename() {

    }

    public ImageRename(String pathName, String ori_imageName, String fig_name) {
        this.ori_imageName = ori_imageName;
        this.fig_name = fig_name;
        this.pathName = pathName + "/IMG/";
        validString = ValidString();
        RenameFile();
    }

    public void RenameFile() {
        File file = null;
        File newFile = null;
        boolean renamed = false;

        file = new File(pathName + ori_imageName);
        newFile = new File(pathName + ori_imageName.replace("cover", validString.replace(".", "+")).replace(" ", ""));

        if (!newFile.exists()) {
            if (file.exists()) {
                renamed = file.renameTo(newFile);
            }
            if (renamed) {
            } else {
                System.out.println("Error renaming file " + file.getPath());
            }
        } else {
            if (file.delete()) {
                System.out.println("Delete >> " + file.toString());
            }
        }
    }

    public String ValidString() {
        String pattern_str = Configuration.REGEX_FIG;
        String line = "";
        Pattern pattern = Pattern.compile(pattern_str);
        Matcher matcher = pattern.matcher(fig_name);
        while (matcher.find()) {
            line = matcher.group(1);
        }

        return line;
    }

}
