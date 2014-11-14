/*
 * Read PDF files and extract to plain text by itext libraries
 */
package Project.data.preparation;

import com.itextpdf.text.DocumentException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.exceptions.CryptographyException;
import org.xml.sax.SAXException;

/**
 *
 * @author fang
 */
public class ReadPDF {

    private Path filename = null;
private TextExtraction textExtraction = null;
    //-------------[READPDF - IMAGE EXTRACTION]-----------------------------

    @SuppressWarnings("empty-statement")
    public ReadPDF(Path filename) throws IOException, DocumentException, CryptographyException, COSVisitorException {
        this.filename = filename;
        String RESULT = this.filename.getParent() + "\\RESULT\\" + this.filename.getFileName() + ".txt";
        if (CheckPathAccess(this.filename, "RESULT") || CheckPathAccess(this.filename, "IMG")) {
            ; // if there is RESULT folder in specific path --> do not do anything
        } else {
            String imgPath = CreateEmptyFolder("IMG");
            String imgPathFull = imgPath + "/";
            ImageExtraction imageExtraction = new ImageExtraction(filename, imgPathFull);
            
        }

    }

    //-------------[READPDF - XML EXTRACTION]-----------------------------
    public ReadPDF(Path filename, int i) throws IOException, DocumentException, ParserConfigurationException, SAXException, XPathExpressionException, FileNotFoundException, InterruptedException {
        if (filename.getFileName().toString().endsWith("nxml")) {
            textExtraction = new TextExtraction(filename);
//            setFilename(filename);
        }
    }

    //-------------[CREATE AN NEW DIRECTORY]-----------------------------
    private String CreateEmptyFolder(String FolderName) {
        String filenamePath = this.getFilename().getParent() + "\\" + FolderName;
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

    //-------------[cHECK IF THE PATH HAS ALREADY EXISTED]-----------------------------
    private boolean CheckPathAccess(Path path, String compareStr) {
        return path.getParent().endsWith(compareStr);
    }

    /**
     * @return the textExtraction
     */
    public TextExtraction getTextExtraction() {
        return textExtraction;
    }

    /**
     * @param textExtraction the textExtraction to set
     */
    public void setTextExtraction(TextExtraction textExtraction) {
        this.textExtraction = textExtraction;
    }

    /**
     * @return the filename
     */
    public Path getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(Path filename) {
        this.filename = filename;
    }

    /**
     * @return the newName
     */
}
