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
public class Subject_SQL extends SQL_operation {

    private Statement stmt_subject = null;
    private Connection conn = null;
    private ArrayList<String> subject = new ArrayList<String>();
    private String table_name = "";

    public Subject_SQL(Connection conn, String table_name, ArrayList<String> subject) throws SQLException {
        this.conn = conn;
        this.table_name = table_name;
        this.subject = subject;
        Insert_Subject(conn, table_name, subject);
    }

    public void Insert_Subject(Connection conn, String table_name, ArrayList<String> subject) throws SQLException {
        stmt_subject = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt_subject.executeQuery(SelectAllStr(table_name));
        ResultSetMetaData meta = rs.getMetaData();
        String att_str = Attribute_string(table_name, meta);
        String val_str = "";
        for (String subject_ele : subject) {
            if (table_name.equalsIgnoreCase(Configuration.SUBJECT_TBL)) {
                val_str = Subject_value_string(table_name, meta, subject_ele, stmt_subject);
            }

            if (val_str.length() > 0) {
                InsertQuery(table_name, meta, stmt_subject, att_str, val_str);
            }
        }
    }

    public String Subject_value_string(String table_name, ResultSetMetaData meta, String subject_ele, Statement stmt_subject) throws SQLException {

        String val_str = "";

        String subject = subject_ele;
        boolean chk = CheckExistedValue(table_name, meta.getColumnName(2), subject, stmt_subject);
        if (!chk) {
            int seq = getNextval(Configuration.SUBJECT_SEQ, stmt_subject);
            String sub_pk = appendQuote(Configuration.SUBJECT_PK + seq);
            subject = appendQuote(subject);
            val_str = sub_pk + "," + subject;
        }

        return val_str;
    }

}
