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

    //------------[Constructor - TEXT]--------------------
    public SQL_operation(Connection conn, TextExtraction txtEx, Path path) throws SQLException {
        this.conn = conn;
        this.txtEx = txtEx;
        this.path = path;
        Subject_SQL sub_sql = new Subject_SQL(conn, Configuration.SUBJECT_TBL, txtEx.getSubject_value());
        Journal_SQL journal_sql = new Journal_SQL(conn, Configuration.JOURNAL_TBL, txtEx.getJournal_id_value(), txtEx.getJournal_title_value());
        File_path_SQL filepath_sql = new File_path_SQL(conn, Configuration.FILE_PATH_TBL, txtEx.getFilename());
        Author_SQL author_sql = new Author_SQL(conn,Configuration.AUTHOR_TBL, txtEx.getAuthor_name_value(),txtEx.getAuthor_surname_value());
        Article_keyword_SQL art_kw_sql = new Article_keyword_SQL(conn, Configuration.ARTICLE_KEYWORD_TBL, txtEx.getKeywords_value());
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

    public boolean CheckExistedValue(String table_name, String attribute, String str_part, Statement stmt) throws SQLException {

        String query = SelectLikeStr(table_name, attribute, str_part);
        System.out.println(query);
        ResultSet rs = stmt.executeQuery(query);
        boolean hasRows = false;
        while (rs.next()) {
            hasRows = true;
        }
        return hasRows;
    }

    public String SelectAllStr(String table_name) {
        return "SELECT * FROM \"" + table_name + "\";";
    }

    public String InsertStr(String table_name, String att_val, String val_str) {
        return "INSERT INTO \"" + table_name + "\" (" + att_val + ") VALUES (" + val_str + ");";
    }

    public void InsertQuery(String table_name, ResultSetMetaData meta, Statement stmt, String att_val, String val_str) throws SQLException {
        String insert_query = InsertStr(table_name, att_val, val_str);
        System.out.println(insert_query);
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
        System.out.println(query);
        stmt_trunc.executeUpdate(query);
        stmt_trunc.close();
    }

}
