/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project.data.database;

import Project.data.preparation.Configuration;
import java.nio.file.Path;
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
public class Author_of_article_SQL extends SQL_operation {

    private Statement stmt_auth_art = null;
    private Connection conn = null;
    private ArrayList<String> author_name_value = new ArrayList<String>();
    private ArrayList<String> author_surname_value = new ArrayList<String>();
    private ArrayList<String> pmc_id_value = new ArrayList<String>();
    private String table_name = "";

    public Author_of_article_SQL(Connection conn, String table_name, ArrayList<String> author_name_value, ArrayList<String> author_surname_value, ArrayList<String> pmc_id_value) throws SQLException {
        this.conn = conn;
        this.table_name = table_name;
        this.author_name_value = author_name_value;
        this.author_surname_value = author_surname_value;
        this.pmc_id_value = pmc_id_value;
        Insert_Author_of_article(conn, table_name, author_name_value, author_name_value, author_surname_value, pmc_id_value);
    }

    public void Insert_Author_of_article(Connection conn, String table_name, ArrayList<String> author_name_value, ArrayList<String> article_title_value, ArrayList<String> author_surname_value, ArrayList<String> pmc_id_value) throws SQLException {
        stmt_auth_art = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt_auth_art.executeQuery(SelectAllStr(table_name));
        ResultSetMetaData meta = rs.getMetaData();
        String att_str = Attribute_string(table_name, meta);

        String val_str = "";

        for (int i = 0; i < author_name_value.size(); i++) {
            if (table_name.equalsIgnoreCase(Configuration.AUTHOR_OF_ARTICLE_TBL)) {
                val_str = Author_of_article_value_string(table_name, author_name_value.get(i), author_surname_value.get(i), pmc_id_value.get(0), stmt_auth_art);
            }

            if (val_str.length() > 0) {
                InsertQuery(table_name, meta, stmt_auth_art, att_str, val_str);
            }
        }
    }

    public String Author_of_article_value_string(String table_name, String author_name, String author_surname, String pmc_id, Statement stmt_auth_art) throws SQLException {
        String val_str = "";
        String pmc_id_str = appendQuote(pmc_id);
        String author_id = appendQuote(Author_id_attribute(author_name, author_surname));

        val_str = author_id + "," + pmc_id;
        return val_str;
    }

    public String Author_id_attribute(String name, String surname) throws SQLException {
        String clause = "author_name = '" + name.replace("'", "''") + "' AND author_surname = '" + surname.replace("'", "''") + "'";
        String ID = CheckExisted_ID_Clauses_Value(Configuration.AUTHOR_TBL, "author_id", clause, stmt_auth_art);
        if (ID.length() == 0) {
            ID = null;
        }
        return ID;
    }
}
