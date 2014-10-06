/*
 * Read PDF files and extract to plain text by itext libraries
 */
package SettingUp;

import com.itextpdf.text.DocumentException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;

/**
 *
 * @author fang
 */
public class ReadPDF {

    private Path filename = null;

    public ReadPDF(Path filename) throws IOException, DocumentException {
        this.filename = filename;
        String RESULT = this.filename.getParent() + "\\RESULT\\" + this.filename.getFileName() + ".txt";
        if (CheckPathAccess(this.filename, "RESULT") || CheckPathAccess(this.filename, "IMG")) {
            ; // if there is RESULT folder in specific path --> do not do anything
        } else {

            String pathString = CreateEmptyFolder("RESULT");
            String imgPath = CreateEmptyFolder("IMG");
            String pathParent = this.filename.getParent().toString();
            String imgPathFull = imgPath + "\\" + filename.getFileName() + "_" + Configuration.IMGNAME;

            //int countINresult = new File(pathString).listFiles().length;
            //int countOUTresult = new File(pathParent).listFiles().length - 2;
            //if (pathString.endsWith("RESULT") && countINresult < countOUTresult) {
            //parseTextStream(this.filename.toString(), RESULT);
            // }
            //extractImages(this.filename.toString(), imgPath + "\\" + Configuration.IMGNAME);
//            new TextExtraction(filename, RESULT);
            new ImageExtraction(filename, imgPathFull);
        }

    }

    //Read XML

    public ReadPDF(Path filename, int i) throws IOException, DocumentException, ParserConfigurationException, SAXException, XPathExpressionException, FileNotFoundException, InterruptedException {
        if(filename.getFileName().toString().endsWith("nxml")){
        new TextExtraction(filename);
        }
    }

    public String CreateEmptyFolder(String FolderName) {
        String filenamePath = this.filename.getParent() + "\\" + FolderName;
        File theDir = new File(filenamePath);

        // if the directory does not exist, create it
        if (!theDir.exists()) {
            try {
                theDir.mkdir();
                return filenamePath;
            } catch (SecurityException se) {
                //handle it
            }
        } else {
            return filenamePath;
        }
        return "Path not found";
    }

    public boolean CheckPathAccess(Path path, String compareStr) {
        if (path.getParent().endsWith(compareStr)) {
            return true;
        }
        return false;
    }
}
