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
        //---------------[Add image description into database]----------------------
        DatabaseConnect db = new DatabaseConnect();
        SQL_operation sql = null;

        //-----------------[RESET SEQUENCE]-------------------------------------------------------------------------
        if (Configuration.RESET.equalsIgnoreCase("on")) {
            sql = new SQL_operation();
            sql.truncateTable(db.getConnection(), Configuration.FIGURE_TBL);

            sql.resetSeq(db.getConnection(), Configuration.ARTICLE_IMAGE_SEQ);
            sql.truncateTable(db.getConnection(), Configuration.ARTICLE_IMAGE_TBL);

            sql.resetSeq(db.getConnection(), Configuration.SUBJECT_SEQ);
            sql.truncateTable(db.getConnection(), Configuration.SUBJECT_TBL);

            sql.resetSeq(db.getConnection(), Configuration.JOURNAL_SEQ);
            sql.truncateTable(db.getConnection(), Configuration.JOURNAL_TBL);

            sql.resetSeq(db.getConnection(), Configuration.FILEPATH_SEQ);
            sql.truncateTable(db.getConnection(), Configuration.FILE_PATH_TBL);

            sql.resetSeq(db.getConnection(), Configuration.AUTHOR_SEQ);
            sql.truncateTable(db.getConnection(), Configuration.AUTHOR_TBL);

            sql.resetSeq(db.getConnection(), Configuration.ARTICLE_KEYWORD_SEQ);
            sql.truncateTable(db.getConnection(), Configuration.ARTICLE_KEYWORD_TBL);

            sql.truncateTable(db.getConnection(), Configuration.ARTICLE_TBL);

            sql.resetSeq(db.getConnection(), Configuration.IMAGE_CAPTION_SEQ);
            sql.truncateTable(db.getConnection(), Configuration.IMAGE_CAPTION_TBL);

            sql.resetSeq(db.getConnection(), Configuration.ARTICLE_PARAGRAPH_SEQ);
            sql.truncateTable(db.getConnection(), Configuration.ARTICLE_PARAGRAPH_TBL);
        }
        //----------------------------------------------------------------------------------------------------------

        //------[FOR Article_image PATH]------------
        DirectoryAccess dirImg = new DirectoryAccess();
        dirImg.PathWalkImage(Configuration.PATH_DIR);
        imgList = dirImg.getFilelist();

        //---------------[READ FILE : XML]----------------------
        filelist = new DirectoryAccess(Configuration.XML_PATH).getFilelist();
        for (Path filename : filelist) {
            System.out.println(filename);
            readpdf = new ReadPDF(filename, Configuration.SWITCH_XML_MODE);
            //------[INSERT DATA INTO DATABASE]---------
            sql = new SQL_operation(db.getConnection(), readpdf.getTextExtraction(), readpdf.getFilename());   //SUBJECT

        }

        //------[INSERT DATA INTO DATABASE]---------
        sql = new SQL_operation(db.getConnection());            //Figure
        sql = new SQL_operation(db.getConnection(), imgList);   //Article_image

    }
}
