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
public class Author_SQL extends SQL_operation {

    private Statement stmt_author = null;
    private Connection conn = null;
    private ArrayList<String> author_name = new ArrayList<String>();
    private ArrayList<String> author_surname = new ArrayList<String>();
    private String table_name = "";

    public Author_SQL(Connection conn, String table_name, ArrayList<String> author_name, ArrayList<String> author_surname) throws SQLException {
        this.conn = conn;
        this.table_name = table_name;
        this.author_name = author_name;
        this.author_surname = author_surname;
        Insert_author(conn, table_name, author_name, author_surname);
    }

    public void Insert_author(Connection conn, String table_name, ArrayList<String> author_name, ArrayList<String> author_surname) throws SQLException {
        stmt_author = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt_author.executeQuery(SelectAllStr(table_name));
        ResultSetMetaData meta = rs.getMetaData();
        String att_str = Attribute_string(table_name, meta);
        String val_str = "";
        if (author_name.size() == author_surname.size()) {
            for (int i = 0; i < author_name.size(); i++) {

                if (table_name.equalsIgnoreCase(Configuration.AUTHOR_TBL)) {
                    val_str = Author_value_string(table_name, meta, author_name.get(i), author_surname.get(i), stmt_author);
                }

                if (val_str.length() > 0) {
                    InsertQuery(table_name, meta, stmt_author, att_str, val_str);
                }
            }

        }
    }

    public String Author_value_string(String table_name, ResultSetMetaData meta, String author_name_ele, String author_surname_ele, Statement stmt_author) throws SQLException {

        String val_str = "";

        String author_name = author_name_ele.replace("'", "");
        String author_surname = author_surname_ele.replace("'", "");
        boolean chk = CheckExistedValue(table_name, meta.getColumnName(2), author_name, stmt_author);
        boolean chk2 = CheckExistedValue(table_name, meta.getColumnName(3), author_surname, stmt_author);
        if (!(chk && chk2)) {
            int seq = getNextval(Configuration.AUTHOR_SEQ, stmt_author);
            String author_pk = appendQuote(Configuration.AUTHOR_PK + seq);
            author_name = appendQuote(author_name);
            author_surname = appendQuote(author_surname);
            val_str = author_pk + "," + author_name + "," + author_surname;
        }

        return val_str;
    }
}
