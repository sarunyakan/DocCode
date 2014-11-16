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
public class Journal_SQL extends SQL_operation {

    private Statement stmt_journal = null;
    private Connection conn = null;
    private ArrayList<String> journal_id = new ArrayList<String>();
    private ArrayList<String> journal_title = new ArrayList<String>();
    private String table_name = "";

    public Journal_SQL(Connection conn, String table_name, ArrayList<String> journal_id, ArrayList<String> journal_title) throws SQLException {
        this.conn = conn;
        this.table_name = table_name;
        this.journal_id = journal_id;
        this.journal_title = journal_title;
        Insert_Journal(conn, table_name, journal_id, journal_title);
    }

    public void Insert_Journal(Connection conn, String table_name, ArrayList<String> journal_id, ArrayList<String> journal_title) throws SQLException {
        stmt_journal = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt_journal.executeQuery(SelectAllStr(table_name));
        ResultSetMetaData meta = rs.getMetaData();
        String att_str = Attribute_string(table_name, meta);
        String val_str = "";
        if (journal_id.size() == journal_title.size()) {
            for (int i = 0; i < journal_id.size(); i++) {

                if (table_name.equalsIgnoreCase(Configuration.JOURNAL_TBL)) {
                    val_str = journal_value_string(table_name, meta, journal_id.get(i), journal_title.get(i), stmt_journal);
                }

                if (val_str.length() > 0) {
                    InsertQuery(table_name, meta, stmt_journal, att_str, val_str);
                }
            }
        }

    }

    public String journal_value_string(String table_name, ResultSetMetaData meta, String jid, String jtitle, Statement stmt_journal) throws SQLException {

        String val_str = "";

        String journal_id_str = jid;
        String journal_title_str = jtitle;
//        boolean chk = CheckExistedValue(table_name, meta.getColumnName(2), jtitle, stmt_journal);
//        boolean chk2 = CheckExistedValue(table_name, meta.getColumnName(3), jid, stmt_journal);
//
//        if (!(chk && chk2)) {

        String query_clause = meta.getColumnName(3) + " = '" + jid + "' AND " + meta.getColumnName(2) + " = '" + jtitle + "'";
        boolean chk = CheckExisted2Value(table_name, query_clause, stmt_journal);
//        boolean chk2 = CheckExistedValue(table_name, meta.getColumnName(3), jid, stmt_journal);

        if (!(chk)) {
            int seq = getNextval(Configuration.JOURNAL_SEQ, stmt_journal);
            String journal_pk = appendQuote(Configuration.JOURNAL_PK + seq);
            journal_id_str = appendQuote(journal_id_str);
            journal_title_str = appendQuote(journal_title_str);
            val_str = journal_pk + "," + journal_title_str + "," + journal_id_str;
        }

        return val_str;
    }

}
