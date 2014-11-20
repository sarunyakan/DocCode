/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project.data.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import Project.data.preparation.Configuration;
import Project.data.preparation.TextExtraction;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.util.HashMap;

/**
 *
 * @author fang
 */
public class SQL_operation {

    private HashMap<String, String> map_value = null;
    private Connection conn = null;
    private Statement stmt = null;

    private ArrayList<String> attribute = new ArrayList<String>();
    private ArrayList<String> value = null;
    private ArrayList<Path> imgList = new ArrayList<Path>();
    private TextExtraction txtEx = null;
    private Path path = null;

    public SQL_operation() {

    }

    //------------[Constructor - FIGURE]--------------------
    public SQL_operation(Connection conn) throws SQLException {
        //Journal, File_path, Subject, Figure
        this.conn = conn;
        Figure_SQL fig_sql = new Figure_SQL(conn, Configuration.FIGURE_TBL);
    }

    //------------[Constructor - ARTICLE_IMAGE]--------------------
    public SQL_operation(Connection conn, ArrayList<Path> imgList) throws SQLException {
        this.conn = conn;
        this.imgList = imgList;
        Article_image_SQL art_img_sql = new Article_image_SQL(conn, Configuration.ARTICLE_IMAGE_TBL, imgList);
        //Insert_Article_Image(conn, Configuration.ARTICLE_IMAGE_TBL, imgList);
    }

    public SQL_operation(Connection conn, TextExtraction txtEx) throws SQLException {
        this.conn = conn;
        this.txtEx = txtEx;

        Image_of_paragraph_SQL img_of_para_sql = new Image_of_paragraph_SQL(conn, Configuration.IMAGE_OF_PARAGRAPH_TBL, txtEx.getPmc_id_value(), txtEx.getParagraph_value());
    }

    //------------[Constructor - TEXT]--------------------
    public SQL_operation(Connection conn, TextExtraction txtEx, Path path) throws SQLException {
        this.conn = conn;
        this.txtEx = txtEx;
        this.path = path;
        Subject_SQL sub_sql = new Subject_SQL(conn, Configuration.SUBJECT_TBL, txtEx.getSubject_value());
        Journal_SQL journal_sql = new Journal_SQL(conn, Configuration.JOURNAL_TBL, txtEx.getJournal_id_value(), txtEx.getJournal_title_value());
        File_path_SQL filepath_sql = new File_path_SQL(conn, Configuration.FILE_PATH_TBL, txtEx.getFilename());
        Author_SQL author_sql = new Author_SQL(conn, Configuration.AUTHOR_TBL, txtEx.getAuthor_name_value(), txtEx.getAuthor_surname_value());
        Article_keyword_SQL art_kw_sql = new Article_keyword_SQL(conn, Configuration.ARTICLE_KEYWORD_TBL, txtEx.getKeywords_value());
        Caption_SQL cap_sql = new Caption_SQL(conn, Configuration.IMAGE_CAPTION_TBL, txtEx.getCaption_title_value(), txtEx.getCaption_para_value(), txtEx.getFigurenum_value(), txtEx.getFilename());
        Article_SQL art_sql = new Article_SQL(conn, Configuration.ARTICLE_TBL, txtEx.getPmc_id_value(), txtEx.getArticle_title_value(), txtEx.getPmid_value(), txtEx.getFilename(), txtEx.getSubject_value(), txtEx.getJournal_id_value());
        Article_paragraph_SQL art_para_sql = new Article_paragraph_SQL(conn, Configuration.ARTICLE_PARAGRAPH_TBL, txtEx.getPmc_id_value(), txtEx.getParagraph_value());
        Author_of_article_SQL author_of_art_sql = new Author_of_article_SQL(conn, Configuration.AUTHOR_OF_ARTICLE_TBL, txtEx.getAuthor_name_value(), txtEx.getAuthor_surname_value(), txtEx.getPmc_id_value());
        Keyword_of_article_SQL kw_of_art_sql = new Keyword_of_article_SQL(conn, Configuration.KEYWORD_OF_ARTICLE_TBL, txtEx.getKeywords_value(), txtEx.getPmc_id_value());
//        Image_of_paragraph_SQL img_of_para_sql = new Image_of_paragraph_SQL(conn, Configuration.IMAGE_OF_PARAGRAPH_TBL, txtEx.getPmc_id_value(), txtEx.getParagraph_value());
    }

    public String Attribute_string(String table_name, ResultSetMetaData meta) throws SQLException {
        String att_str = "";
        int col = meta.getColumnCount();
        for (int k = 1; k <= col; k++) {
            if (k == col) {
                att_str = att_str + meta.getColumnName(k);
            } else {
                att_str = att_str + meta.getColumnName(k) + ",";
            }
        }

        return att_str;
    }

    public String getIDfromJoin(String pmc_num, String fignum, Statement stmt) throws SQLException {
        //For Image_caption, File_path and article only --> to fill caption_id at Article_image table
        String query = SelectIDJoinStr(pmc_num, fignum);
//        System.out.println(query);
        ResultSet rs = stmt.executeQuery(query);
        String ID = "";
        while (rs.next()) {
            ID = rs.getString("caption_id");
        }

        return ID;

    }

    public String SelectIDJoinStr(String pmc_num, String fignum) {
        return "SELECT \"Image_caption\".caption_id FROM \"Image_caption\" WHERE \"Image_caption\".caption_fignum = '" + fignum + "' AND \"Image_caption\".caption_file = (SELECT \"File_path\".file_path FROM \"Article\", \"File_path\" WHERE \"Article\".file_path_id = \"File_path\".file_path_id AND \"Article\".pmc_id ='" + pmc_num + "');";
    }

    public String appendQuote(String str) {

        if (str == null) {
            str = null;
        } else {
            str = "'" + str + "'";
        }

        return str;
    }

    public int getNextval(String seq_name, Statement stmt) throws SQLException {

        String query = "select nextval('" + seq_name + "')";
        ResultSet rs = stmt.executeQuery(query);
        int serialNum = 0;
        if (rs.next()) {
            serialNum = rs.getInt(1);
        }

        return serialNum;
    }

    public String SelectLikeStr(String table_name, String attribute, String str_part) {
        return "SELECT * FROM \"" + table_name + "\" WHERE " + attribute + " LIKE '" + str_part + "';";
    }

    public String SelectLikeClauseStr(String table_name, String str_part) {
        return "SELECT * FROM \"" + table_name + "\" WHERE " + str_part + ";";
    }

    public String SelectIDStr(String table_name, String targetAtt, String clauseAtt, String str_part) {
        return "SELECT " + targetAtt + " FROM \"" + table_name + "\" WHERE " + clauseAtt + " = '" + str_part + "';";
    }

    public String SelectIDClausesStr(String table_name, String targetAtt, String clause) {
        return "SELECT " + targetAtt + " FROM \"" + table_name + "\" WHERE " + clause + ";";
    }

    public boolean CheckExistedValue(String table_name, String attribute, String str_part, Statement stmt) throws SQLException {

        String query = SelectLikeStr(table_name, attribute, str_part);
//        System.out.println(query);
        ResultSet rs = stmt.executeQuery(query);
        boolean hasRows = false;
        while (rs.next()) {
            hasRows = true;
        }
        return hasRows;
    }

    public boolean CheckExisted2Value(String table_name, String str_part, Statement stmt) throws SQLException {

        String query = SelectLikeClauseStr(table_name, str_part);
//        System.out.println(query);
        ResultSet rs = stmt.executeQuery(query);
        boolean hasRows = false;
        while (rs.next()) {
            hasRows = true;
        }
        return hasRows;
    }

    public String CheckExisted_ID_Value(String table_name, String targetAtt, String clauseAtt, String str_part, Statement stmt) throws SQLException {

        String query = SelectIDStr(table_name, targetAtt, clauseAtt, str_part);
//        System.out.println(query);
        ResultSet rs = stmt.executeQuery(query);
        String ID = "";
        while (rs.next()) {
            ID = rs.getString(targetAtt);
        }
        return ID;
    }

    public String CheckExisted_ID_Clauses_Value(String table_name, String targetAtt, String clause, Statement stmt) throws SQLException {

        String query = SelectIDClausesStr(table_name, targetAtt, clause);
//        System.out.println(query);
        String ID = "";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            ID = rs.getString(targetAtt);
        }
        return ID;
    }

    public String SelectAllStr(String table_name) {
        return "SELECT * FROM \"" + table_name + "\";";
    }

    public String InsertStr(String table_name, String att_val, String val_str) {
        return "INSERT INTO \"" + table_name + "\" (" + att_val + ") VALUES (" + val_str + ");";
    }

    public void InsertQuery(String table_name, ResultSetMetaData meta, Statement stmt, String att_val, String val_str) throws SQLException {
        String insert_query = InsertStr(table_name, att_val, val_str);
//        System.out.println(insert_query);
        ExecuteInsertSQL(stmt, insert_query);
    }

    public void ExecuteInsertSQL(Statement stmt, String query_str) throws SQLException {
        stmt.executeUpdate(query_str);

    }

    public void ExecuteAlterSQL(Statement stmt, String query_str) throws SQLException {
        stmt.executeUpdate(query_str);

    }

    public void resetSeq(Connection conn, String seq_name) throws SQLException {
        Statement stmt_seq = conn.createStatement();
        String query = "ALTER SEQUENCE " + seq_name + " RESTART WITH 1;";
        ExecuteAlterSQL(stmt_seq, query);
        stmt_seq.close();
    }

    public void truncateTable(Connection conn, String table_name) throws SQLException {
        Statement stmt_trunc = conn.createStatement();
        String query = "TRUNCATE \"" + table_name + "\" CASCADE;";
//        System.out.println(query);
        stmt_trunc.executeUpdate(query);
        stmt_trunc.close();
    }

    public boolean checkPaperYear(Connection conn, ArrayList<String> pmc_id_value) throws SQLException {
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        boolean isFound = false;
        for (String pmc_id_value1 : pmc_id_value) {
            String query = SelectIDStr(Configuration.ARTICLE__PUBLISH_YEAR_TBL, "pmc_id", "year", pmc_id_value1);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                return true;
            }
        }
        return false;
    }

    public boolean checkExistedPaper(Connection conn, ArrayList<String> pmc_id_value) throws SQLException {
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        boolean isFound = false;
        for (String pmc_id_value1 : pmc_id_value) {
            String query = SelectIDStr(Configuration.ARTICLE_TBL, "pmc_id", "pmc_id", pmc_id_value1);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                return true;
            }
        }
        return false;
    }

}
