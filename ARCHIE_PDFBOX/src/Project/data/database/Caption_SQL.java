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
public class Caption_SQL extends SQL_operation {

    private Statement stmt_cap = null;
    private Connection conn = null;
    private ArrayList<String> caption_id = new ArrayList<String>();
    private ArrayList<String> caption_title = new ArrayList<String>();
    private ArrayList<String> caption_para = new ArrayList<String>();

    private ArrayList<String> caption_fignum = new ArrayList<String>();
    private Path caption_file;

    private String table_name = "";
    private Path filename;

    public Caption_SQL(Connection conn, String table_name, ArrayList<String> caption_title, ArrayList<String> caption_para, ArrayList<String> caption_fignum, Path caption_file) throws SQLException {
        this.conn = conn;
        this.table_name = table_name;
        this.caption_title = caption_title;
        this.caption_para = caption_para;
        this.caption_fignum = caption_fignum;
        this.caption_file = caption_file;
        Insert_Image_caption(conn, table_name, caption_title, caption_para, caption_fignum, caption_file);
    }

    public void Insert_Image_caption(Connection conn, String table_name, ArrayList<String> caption_title, ArrayList<String> caption_para, ArrayList<String> caption_fignum, Path caption_file) throws SQLException {
        stmt_cap = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt_cap.executeQuery(SelectAllStr(table_name));
        ResultSetMetaData meta = rs.getMetaData();
        String att_str = Attribute_string(table_name, meta);
        String val_str = "";
        String str_title = "", str_para = "";
        for (int i = 0; i < caption_fignum.size(); i++) {
            if (table_name.equalsIgnoreCase(Configuration.IMAGE_CAPTION_TBL)) {

                if (caption_title.size() == 0) {
                    str_title = null;
                } else {
                    str_title = caption_title.get(i);
                }

                if (caption_para.size() == 0) {
                    str_para = null;
                } else {
                    str_para = caption_para.get(i);
                }
                val_str = ImageCaption_value_string(table_name, meta, str_title, str_para, caption_fignum.get(i), caption_file, stmt_cap);
            }
            InsertQuery(table_name, meta, stmt_cap, att_str, val_str);
        }

    }

    public String ImageCaption_value_string(String table_name, ResultSetMetaData meta, String caption_title, String caption_para, String caption_fignum, Path caption_file, Statement stmt_cap) throws SQLException {
        String val_str = "";
        String caption_title_str = "";
        String caption_para_str = "";

        if (caption_para == null) {
            caption_para_str = null;
        } else {
            caption_para_str = appendQuote(caption_para.replace("'", "''"));
        }

        if (caption_title == null) {
            caption_title_str = null;
        } else {
            caption_title_str = appendQuote(caption_title.replace("'", "''"));
        }
        if (caption_para == null) {
            caption_para_str = null;
        } else {
            caption_para_str = appendQuote(caption_para.replace("'", "''"));
        }
        caption_fignum = StringChange(caption_fignum);
        String caption_fignum_str = appendQuote(caption_fignum);
        String caption_file_str = appendQuote(caption_file.toString());

        int seq = getNextval(Configuration.IMAGE_CAPTION_SEQ, stmt_cap);
        String imgCap_pk = appendQuote(Configuration.IMAGE_CAPTION_PK + seq);

        val_str = imgCap_pk + "," + caption_para_str + "," + caption_fignum_str + "," + caption_file_str + "," + caption_title_str;
        return val_str;
    }

    public String StringChange(String caption_fignum) {
        String tmp_str = caption_fignum.replace(".", "").replace(":", "").replace("s", "").replace(" ", "").toLowerCase();

        if (tmp_str.length() < 6) {
            tmp_str = tmp_str.replace("fig", "figure");
        } else {
            tmp_str = tmp_str;
        }
        return tmp_str;
    }
}
