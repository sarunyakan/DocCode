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
public class Article_image_SQL extends SQL_operation {

    private Statement stmt_article_image = null;
    private Connection conn = null;
    private ArrayList<String> attribute = new ArrayList<String>();
    private ArrayList<String> value = null;
    private ArrayList<Path> imgList = new ArrayList<Path>();
    private String table_name = "";

    public Article_image_SQL(Connection conn, String table_name, ArrayList<Path> imgList) throws SQLException {
        this.conn = conn;
        this.table_name = table_name;
        this.imgList = imgList;

        Insert_Article_Image(conn, Configuration.ARTICLE_IMAGE_TBL, imgList);
    }

    public void Insert_Article_Image(Connection conn, String table_name, ArrayList<Path> fileList) throws SQLException {
        stmt_article_image = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt_article_image.executeQuery(SelectStr(table_name));
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

    public String AticleImage_value_string(String table_name, ResultSetMetaData meta, Path img_name, Statement stmt_article_image) throws SQLException {
        String img_path = appendQuote(img_name.getParent().toString());
        String img_filename = appendQuote(img_name.getFileName().toString());
        String val_str = "";
        String fig_id_tmp = FigID_attribute(img_name);
        String fig_id = appendQuote(fig_id_tmp);
        String cluster_id = appendQuote(null);
        String caption_id = appendQuote(null);
        int seq = getNextval(Configuration.ARTICLE_IMAGE_SEQ, stmt_article_image);
        String aticleImage_pk = appendQuote(Configuration.ARTICLE_IMAGE_PK + seq);

        val_str = aticleImage_pk + "," + fig_id + "," + img_filename + "," + img_path + "," + cluster_id + "," + caption_id;

        return val_str;
    }

    public String FigID_attribute(Path img_name) {
        String[] str_tmp = img_name.getFileName().toString().split("_");
        String filename = "";
        for (String str_tmp1 : str_tmp) {
            if (str_tmp1.contains("figure")) {
                filename = str_tmp1;
                break;
            }
        }
        if (filename.length() == 0) {
            filename = "figure0";
        }
        return filename;
    }
}
