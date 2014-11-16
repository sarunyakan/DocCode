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
public class Image_of_paragraph_SQL extends SQL_operation {

    private Statement stmt_img_para = null;
    private Connection conn = null;
    private ArrayList<String> pmc_id_value = new ArrayList<String>();
    private ArrayList<String> paragraph_value = new ArrayList<String>();
    private String table_name = "";

    public Image_of_paragraph_SQL(Connection conn, String table_name, ArrayList<String> pmc_id_value, ArrayList<String> paragraph_value) throws SQLException {
        this.conn = conn;
        this.table_name = table_name;
        this.pmc_id_value = pmc_id_value;
        this.paragraph_value = paragraph_value;
        Insert_Image_of_paragraph(conn, table_name, pmc_id_value, paragraph_value);
    }

    public void Insert_Image_of_paragraph(Connection conn, String table_name, ArrayList<String> pmc_id_value, ArrayList<String> paragraph_value) throws SQLException {
        stmt_img_para = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt_img_para.executeQuery(SelectAllStr(table_name));
        ResultSetMetaData meta = rs.getMetaData();
        String att_str = Attribute_string(table_name, meta);

        String val_str = "";

        for (int i = 0; i < paragraph_value.size(); i++) {
            if (table_name.equalsIgnoreCase(Configuration.IMAGE_OF_PARAGRAPH_TBL)) {
                val_str = Image_of_paragraph_value_string(table_name, pmc_id_value.get(0), paragraph_value.get(i), stmt_img_para);
            }
            if (val_str.length() > 0 && !val_str.contains("null")) {
                InsertQuery(table_name, meta, stmt_img_para, att_str, val_str);
            }
        }
    }

    public String Image_of_paragraph_value_string(String table_name, String pmc_id, String para, Statement stmt_img_para) throws SQLException {
        String val_str = "";

        String para_id_str = appendQuote(art_para_attribute(para, pmc_id, "paragraph_id"));
        String para_fig = art_para_attribute(para, pmc_id, "paragraph_fignum");
        String img_id_str = "";

        String[] fig = para_fig.split("\\+");

        for (String fig1 : fig) {
            img_id_str = art_img_attribute(fig1, pmc_id, "img_id");
            img_id_str = appendQuote(img_id_str);
            val_str = img_id_str + "," + para_id_str;
        }

        return val_str;

    }

    public String art_para_attribute(String para, String pmc_id, String targetAtt) throws SQLException {
        String clause = "paragraph_text = '" + para.replace("'", "''") + "' AND pmc_id = '" + pmc_id + "'";
        String ID = CheckExisted_ID_Clauses_Value(Configuration.ARTICLE_PARAGRAPH_TBL, targetAtt, clause, stmt_img_para);
        if (ID.length() == 0) {
            ID = null;
        }

        return ID;
    }

    public String art_img_attribute(String fig, String pmc_id, String targetAtt) throws SQLException {
        String clause = "fig_id = '" + fig + "' AND image_pmc_id = '" + pmc_id + "'";
        String ID = CheckExisted_ID_Clauses_Value(Configuration.ARTICLE_IMAGE_TBL, targetAtt, clause, stmt_img_para);

        if (ID.length() == 0) {
            ID = null;
        }

        return ID;
    }
}
