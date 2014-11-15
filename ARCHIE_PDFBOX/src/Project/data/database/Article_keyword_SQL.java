/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project.data.database;

import Project.data.preparation.Configuration;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author fang
 */
public class Article_keyword_SQL extends SQL_operation {

    private Statement stmt_artkw = null;
    private Connection conn = null;
    private ArrayList<String> keywords_value = new ArrayList<String>();
    private String table_name = "";

    public Article_keyword_SQL(Connection conn, String table_name, ArrayList<String> keywords_value) throws SQLException {
        this.conn = conn;
        this.table_name = table_name;
        this.keywords_value = keywords_value;
        Insert_Article_keyword(conn, table_name, keywords_value);
    }

    public void Insert_Article_keyword(Connection conn, String table_name, ArrayList<String> art_kw) throws SQLException {
        stmt_artkw = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt_artkw.executeQuery(SelectAllStr(table_name));
        ResultSetMetaData meta = rs.getMetaData();
        String att_str = Attribute_string(table_name, meta);
        String val_str = "";
        for (String art_kw_ele : art_kw) {
            if (table_name.equalsIgnoreCase(Configuration.ARTICLE_KEYWORD_TBL)) {
                val_str = Article_keyword_value_string(table_name, meta, art_kw_ele, stmt_artkw);
            }

            if (val_str.length() > 0) {
                InsertQuery(table_name, meta, stmt_artkw, att_str, val_str);
            }
        }
    }

    public String Article_keyword_value_string(String table_name, ResultSetMetaData meta, String art_kw_ele, Statement stmt_artkw) throws SQLException {

        String val_str = "";

        String art_kw = art_kw_ele;
        boolean chk = CheckExistedValue(table_name, meta.getColumnName(2), art_kw, stmt_artkw);
        if (!chk) {
            int seq = getNextval(Configuration.ARTICLE_KEYWORD_SEQ, stmt_artkw);
            String artkw_pk = appendQuote(Configuration.ARTICLE_KEYWORD_PK + seq);
            art_kw = appendQuote(art_kw);
            val_str = artkw_pk + "," + art_kw;
        }

        return val_str;
    }
}
