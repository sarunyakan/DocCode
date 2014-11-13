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
import java.nio.file.Path;
import java.util.HashMap;

/**
 *
 * @author fang
 */
public class SQL_query {

    private HashMap<String, String> map_value = null;
    private Connection conn = null;
    private Statement stmt = null;

    private ArrayList<String> attribute = new ArrayList<String>();
    private ArrayList<String> value = null;
    private ArrayList<Path> imgList = new ArrayList<Path>();

    public SQL_query() {
    }

    //สร้างข้อมูลที่รันเองได้เลย
    public SQL_query(Connection conn) throws SQLException {
        //Journal, File_path, Subject, Figure
        this.conn = conn;
        Figure_SQL fig_sql = new Figure_SQL(conn, Configuration.FIGURE_TBL);
    }

    public SQL_query(Connection conn, ArrayList<Path> imgList) throws SQLException {
        this.conn = conn;
        this.imgList = imgList;
        Article_image_SQL art_img_sql = new Article_image_SQL(conn, Configuration.ARTICLE_IMAGE_TBL, imgList);
        //Insert_Article_Image(conn, Configuration.ARTICLE_IMAGE_TBL, imgList);
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

    public String SelectStr(String table_name) {
        return "SELECT * FROM \"" + table_name + "\";";
    }

    public String InsertStr(String table_name, String att_val, String val_str) {
        return "INSERT INTO \"" + table_name + "\" (" + att_val + ") VALUES (" + val_str + ");";
    }

    public void InsertQuery(String table_name, ResultSetMetaData meta, Statement stmt, String att_val, String val_str) throws SQLException {
        String insert_query = InsertStr(table_name, att_val, val_str);
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