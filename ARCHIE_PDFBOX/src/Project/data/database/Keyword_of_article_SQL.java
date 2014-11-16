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
public class Keyword_of_article_SQL extends SQL_operation {

    private Statement stmt_kw_art = null;
    private Connection conn = null;
    private ArrayList<String> keywords_value = new ArrayList<String>();
    private ArrayList<String> pmc_id_value = new ArrayList<String>();
    private String table_name = "";

    public Keyword_of_article_SQL(Connection conn, String table_name, ArrayList<String> keywords_value, ArrayList<String> pmc_id_value) throws SQLException {
        this.conn = conn;
        this.table_name = table_name;
        this.keywords_value = keywords_value;
        this.pmc_id_value = pmc_id_value;
        Insert_Keyword_of_article(conn, table_name, keywords_value, pmc_id_value);
    }

    public void Insert_Keyword_of_article(Connection conn, String table_name, ArrayList<String> keywords_value, ArrayList<String> pmc_id_value) throws SQLException {
        stmt_kw_art = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt_kw_art.executeQuery(SelectAllStr(table_name));
        ResultSetMetaData meta = rs.getMetaData();
        String att_str = Attribute_string(table_name, meta);

        String val_str = "";

        for (int i = 0; i < keywords_value.size(); i++) {
            if (table_name.equalsIgnoreCase(Configuration.KEYWORD_OF_ARTICLE_TBL)) {
                val_str = Keyword_of_article_value_string(table_name, keywords_value.get(i), pmc_id_value.get(0), stmt_kw_art);
            }

            if (val_str.length() > 0) {
                InsertQuery(table_name, meta, stmt_kw_art, att_str, val_str);
            }
        }
    }

    public String Keyword_of_article_value_string(String table_name, String keywords_value, String pmc_id_value, Statement stmt_kw_art) throws SQLException {
        String val_str = "";
        String pmc_id_str = appendQuote(pmc_id_value);
        String keywords_value_str = appendQuote(Keyword_id_attribute(keywords_value));

        val_str = keywords_value_str + "," + pmc_id_str;
        return val_str;
    }

    public String Keyword_id_attribute(String keywords_value_str) throws SQLException {
        String ID = CheckExisted_ID_Value(Configuration.ARTICLE_KEYWORD_TBL, "kw_id", "keyword", keywords_value_str, stmt_kw_art);
        if (ID.length() == 0) {
            ID = null;
        }
        return ID;
    }
}
