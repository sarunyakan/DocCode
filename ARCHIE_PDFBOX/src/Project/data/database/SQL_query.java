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
    private Statement stmt_figure = null;
    private Statement stmt_article_image = null;
    private ArrayList<String> attribute = new ArrayList<String>();
    private ArrayList<String> value = null;
    private ArrayList<Path> imgList = new ArrayList<Path>();

    public SQL_query() {
    }

    //สร้างข้อมูลที่รันเองได้เลย

    public SQL_query(Connection conn) throws SQLException {
        //Journal, File_path, Subject, Figure
        this.conn = conn;
        Insert_Figure(conn, Configuration.FIGURE_TBL);

    }

    public SQL_query(Connection conn, ArrayList<Path> imgList) throws SQLException {
        this.conn = conn;
        this.imgList = imgList;
        Insert_Article_Image(conn, Configuration.ARTICLE_IMAGE_TBL, imgList);
    }

    public void Insert_Article_Image(Connection conn, String table_name, ArrayList<Path> fileList) throws SQLException {
        stmt_article_image = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt_article_image.executeQuery("SELECT * FROM \"" + table_name + "\";");
        ResultSetMetaData meta = rs.getMetaData();
        String att_str = Attribute_string(table_name, meta);
        String val_str = "";
        for (Path img_name : fileList) {
            if (table_name.equalsIgnoreCase(Configuration.ARTICLE_IMAGE_TBL)) {
                val_str = AticleImage_value_string(table_name, meta, img_name, stmt_article_image);
            }
            InsertQuery(table_name, meta, stmt_article_image, att_str, val_str);
        }

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

    public String AticleImage_value_string(String table_name, ResultSetMetaData meta, Path img_name, Statement stmt_article_image) throws SQLException {
        String img_path = appendQuote(img_name.getParent().toString());
        String img_filename = appendQuote(img_name.getFileName().toString());
        String val_str = "";
        String fig_id = appendQuote(null);
        String cluster_id = appendQuote(null);

        int seq = getNextval(Configuration.ARTICLE_IMAGE_SEQ, stmt_article_image);
        String aticleImage_pk = appendQuote(Configuration.ARTICLE_IMAGE_PK + seq);

        val_str = aticleImage_pk + "," + fig_id + "," + img_filename + "," + img_path + "," + cluster_id + "";

        return val_str;
    }

    public String appendQuote(String str) {
        String string = "";

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

    public void InsertQuery(String table_name, ResultSetMetaData meta, Statement stmt, String att_val, String val_str) throws SQLException {
        String insert_query = "INSERT INTO \"" + table_name + "\" (" + att_val + ") VALUES (" + val_str + ");";
        ExecuteInsertSQL(stmt, insert_query);
    }

    //----------------------------------------------------------
    public void Insert_Figure(Connection conn, String table_name) throws SQLException {

        stmt_figure = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        ResultSet rs = stmt_figure.executeQuery("SELECT * FROM \"" + table_name + "\";");
        ResultSetMetaData meta = rs.getMetaData();
        rs.last();
        int count = rs.getRow();
        rs.beforeFirst();
        if (count == 0) {
            int col = meta.getColumnCount();
            if (table_name.equalsIgnoreCase(Configuration.FIGURE_TBL)) {
                Insert_figure_value(meta);
                InsertFigureQuery(table_name, meta, value, stmt);
            }

        } else {
            ;
        }
        stmt_figure.close();
    }

    public void InsertFigureQuery(String tableName, ResultSetMetaData meta, ArrayList<String> value, Statement stmt) throws SQLException {

        for (int i = 0; i < value.size(); i++) {
            String val_str = "";
            String insert_query = "INSERT INTO \"" + tableName + "\" (";
            String att_str = "";
            int col = meta.getColumnCount();
            for (int k = 1; k <= col; k++) {
                if (k == col) {
                    att_str = att_str + meta.getColumnName(k) + ") VALUES (";
                } else {
                    att_str = att_str + meta.getColumnName(k) + ",";
                }
            }

            val_str = val_str + "'" + value.get(i) + "')";

            String query_str = insert_query + att_str + val_str;
            ExecuteInsertSQL(stmt, query_str);
        }
    }

    public void Insert_figure_value(ResultSetMetaData meta) throws SQLException {

        value = new ArrayList<String>();
        for (int i = 0; i < 31; i++) {

            value.add(Configuration.FIGURE_IMAGE_PK + i);
        }
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
