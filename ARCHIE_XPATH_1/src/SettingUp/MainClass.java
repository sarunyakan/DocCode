/*
 * This is a main class of the setting up process. 
 * It involved the process of managing directories and sub directories and database connection.
 * This code is a part of my doctoral thesis in Information Science and Engineering, Shibaura Institute of Technology. 
 */
package SettingUp;

import com.itextpdf.text.DocumentException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;

/**
 * @author fang
 * @since Sun 07 Sept 2014 16:56
 */
public class MainClass {

    /**
     * @param args the command line arguments
     */
    static ArrayList<Path> filelist = new ArrayList<>();

    public static void main(String[] args) throws IOException, DocumentException, ParserConfigurationException, SAXException, XPathExpressionException, FileNotFoundException, InterruptedException {

        //new DatabaseConnect(); // Make a connection to database
//        filelist = new DirectoryAccess(Configuration.PATH_DIR).getFilelist();
//
//        for (Path filename : filelist) {
//            new ReadPDF(filename);
//        }
        
        
        /*
        Read paths pf XML files
        */
        filelist = new DirectoryAccess(Configuration.XML_PATH).getFilelist();
        for (Path filename : filelist) {
            System.out.println(filename);
            new ReadPDF(filename, 0);
        }
    }

}
