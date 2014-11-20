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
public class Article_SQL extends SQL_operation {

    private Statement stmt_art = null;
    private Connection conn = null;
    private ArrayList<String> pmc_id_value = new ArrayList<String>();
    private ArrayList<String> article_title_value = new ArrayList<String>();
    private ArrayList<String> pmid_value = new ArrayList<String>();
    private ArrayList<String> subject_value = new ArrayList<String>();
    private ArrayList<String> journal_id_value = new ArrayList<String>();
    private String table_name = "";
    private Path filename;

    public Article_SQL(Connection conn, String table_name, ArrayList<String> pmc_id_value, ArrayList<String> article_title_value, ArrayList<String> pmid_value, Path filename, ArrayList<String> subject_value, ArrayList<String> journal_id_value) throws SQLException {
        this.conn = conn;
        this.table_name = table_name;
        this.pmc_id_value = pmc_id_value;
        this.article_title_value = article_title_value;
        this.pmid_value = pmid_value;
        this.subject_value = subject_value;
        this.filename = filename;
        this.journal_id_value = journal_id_value;
        Insert_Article(conn, table_name, pmc_id_value, article_title_value, pmid_value, filename, subject_value, journal_id_value);

    }

    public void Insert_Article(Connection conn, String table_name, ArrayList<String> pmc_id_value, ArrayList<String> article_title_value, ArrayList<String> pmid_value, Path filename, ArrayList<String> subject_value, ArrayList<String> journal_id_value) throws SQLException {
        stmt_art = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt_art.executeQuery(SelectAllStr(table_name));
        ResultSetMetaData meta = rs.getMetaData();
        String att_str = Attribute_string(table_name, meta);
        String val_str = "";
        String pmid_arr = "";
        for (int i = 0; i < pmc_id_value.size(); i++) {
            if (table_name.equalsIgnoreCase(Configuration.ARTICLE_TBL)) {
                if (pmid_value.size() == 0) {
                    pmid_arr = "00000000";
                } else {
                    pmid_arr = pmid_value.get(i);
                }

                val_str = Article_value_string(table_name, pmc_id_value.get(i), article_title_value.get(i), pmid_arr, filename, subject_value.get(i), journal_id_value.get(i), stmt_art);
            }

            if (val_str.length() > 0) {
                InsertQuery(table_name, meta, stmt_art, att_str, val_str);
            }
        }
    }

    public String Article_value_string(String table_name, String pmc_id_value, String article_title_value, String pmid_value, Path filename, String subject_value, String journal_id_value, Statement stmt_art) throws SQLException {
        String val_str = "";
        String pmc_id = appendQuote(pmc_id_value);
        String article_title = appendQuote(article_title_value.replace("'", "''"));
        String pmid = appendQuote(pmid_value);

        String pathname = appendQuote(File_path_id_attribute(filename.toString()));
        String subject = appendQuote(Subject_id_attribute(subject_value.replace("'","''")));
        String journal = appendQuote(Journal_id_attribute(journal_id_value));

        val_str = pmc_id + "," + article_title + "," + pmid + "," + pathname + "," + subject + "," + journal;
        return val_str;
    }

    public String File_path_id_attribute(String filepath) throws SQLException {
        String ID = CheckExisted_ID_Value(Configuration.FILE_PATH_TBL, "file_path_id", "file_path", filepath, stmt_art);
        if (ID.length() == 0) {
            ID = null;
        }
        return ID;
    }

    public String Subject_id_attribute(String subject_value) throws SQLException {
        String ID = CheckExisted_ID_Value(Configuration.SUBJECT_TBL, "sub_id", "subject", subject_value, stmt_art);
        if (ID.length() == 0) {
            ID = null;
        }
        return ID;
    }

    public String Journal_id_attribute(String journal_id_value) throws SQLException {
        String ID = CheckExisted_ID_Value(Configuration.JOURNAL_TBL, "journal_id", "journal_key", journal_id_value, stmt_art);
        if (ID.length() == 0) {
            ID = null;
        }
        return ID;
    }

}
