/*
 * This is a main class of the setting up process. 
 * It involved the process of managing directories and sub directories and database connection.
 * This code is a part of my doctoral thesis in Information Science and Engineering, Shibaura Institute of Technology. 
 */
package Project.data.preparation;

import com.itextpdf.text.DocumentException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.exceptions.CryptographyException;
import org.xml.sax.SAXException;
import Project.data.database.*;
import java.sql.SQLException;

/**
 * @author fang
 * @since Sun 07 Sept 2014 16:56
 */
public class MainClass {

    /**
     * @param args the command line arguments
     */
    static ArrayList<Path> filelist = new ArrayList<>();
    static ArrayList<Path> imgList = new ArrayList<>();
    static ReadPDF readpdf;

    public static void main(String[] args) throws IOException, DocumentException, ParserConfigurationException, SAXException, XPathExpressionException, FileNotFoundException, InterruptedException, CryptographyException, COSVisitorException, SQLException {

        //---------------[READ FILE : IMAGE]----------------------
//        filelist = new DirectoryAccess(Configuration.PATH_DIR).getFilelist();
//        ReadPDF readpdf = null;
//        for (Path filename : filelist) {
//            readpdf = new ReadPDF(filename);
//        }
        
        
        //---------------[READ FILE : XML]----------------------
        filelist = new DirectoryAccess(Configuration.XML_PATH).getFilelist();
        for (Path filename : filelist) {
            System.out.println(filename);
            new ReadPDF(filename, Configuration.SWITCH_XML_MODE);
        }
        
        
        //---------------[Add image description into database]----------------------
//        DatabaseConnect db = new DatabaseConnect();
//        SQL_query sql = null;
//
//        //------[FOR Article_image PATH]------------
//        DirectoryAccess dirImg = new DirectoryAccess();
//        dirImg.PathWalkImage(Configuration.PATH_DIR);
//        imgList = dirImg.getFilelist();
//
//        //------[INSERT DATA INTO DATABASE]---------
//        sql = new SQL_query(db.getConnection());            //Figure
//        sql = new SQL_query(db.getConnection(), imgList);   //Article_image
//
//        //-----------------[RESET SEQUENCE]-------------------------
//        if (Configuration.ARTICLE_IMAGE_RESET.equalsIgnoreCase("on")) {
//            sql = new SQL_query();
//            sql.resetSeq(db.getConnection(), Configuration.ARTICLE_IMAGE_SEQ);
//            sql.truncateTable(db.getConnection(), Configuration.ARTICLE_IMAGE_TBL);
//        }
    }
}
