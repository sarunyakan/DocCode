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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private String pmc = "";

    public Article_image_SQL(Connection conn, String table_name, ArrayList<Path> imgList) throws SQLException {
        this.conn = conn;
        this.table_name = table_name;
        this.imgList = imgList;

        Insert_Article_Image(conn, Configuration.ARTICLE_IMAGE_TBL, imgList);
    }

    public void Insert_Article_Image(Connection conn, String table_name, ArrayList<Path> fileList) throws SQLException {
        stmt_article_image = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt_article_image.executeQuery(SelectAllStr(table_name));
        ResultSetMetaData meta = rs.getMetaData();
        String att_str = Attribute_string(table_name, meta);
        String val_str = "";
        for (Path img_name : fileList) {
            if (table_name.equalsIgnoreCase(Configuration.ARTICLE_IMAGE_TBL)) {
                val_str = ArticleImage_value_string(table_name, meta, img_name, stmt_article_image);
            }
            InsertQuery(table_name, meta, stmt_article_image, att_str, val_str);
        }

    }

    public String ArticleImage_value_string(String table_name, ResultSetMetaData meta, Path img_name, Statement stmt_article_image) throws SQLException {
        String img_path = appendQuote(img_name.toString());
        String img_filename_str = img_name.getFileName().toString();
        String img_filename = appendQuote(img_filename_str);

        String val_str = "";
        String fig_id_tmp = FigID_attribute(img_name);
        String caption_id_tmp = getCaptionIDfromImgFile(img_filename_str, fig_id_tmp);
        String fig_id = appendQuote(fig_id_tmp);
        String cluster_id = appendQuote(null);
        String caption_id = appendQuote(caption_id_tmp);
        int seq = getNextval(Configuration.ARTICLE_IMAGE_SEQ, stmt_article_image);
        String aticleImage_pk = appendQuote(Configuration.ARTICLE_IMAGE_PK + seq);
        String image_pmc_id_str = getPmc();
        if (image_pmc_id_str.length() == 0) {
            image_pmc_id_str = null;
        } else {
            image_pmc_id_str = appendQuote(image_pmc_id_str);
        }
        val_str = aticleImage_pk + "," + fig_id + "," + img_filename + "," + img_path + "," + cluster_id + "," + caption_id + "," + image_pmc_id_str;

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

    public String caption_id_attribute(String filepath) throws SQLException {
        String ID = CheckExisted_ID_Value(Configuration.IMAGE_CAPTION_TBL, "file_path_id", "file_path", filepath, stmt_article_image);
        if (ID.length() == 0) {
            ID = null;
        }
        return ID;
    }

    public String getCaptionIDfromImgFile(String img_filename_str, String fignum) throws SQLException {
        String pmc_num = getNumberPMC(img_filename_str);
        String caption_id = getIDfromJoin(pmc_num, fignum, stmt_article_image);
        if (caption_id.length() == 0) {
            caption_id = null;
        }
        return caption_id;
    }

    public String getNumberPMC(String img_filename_str) {
        String pattern_str = "PMC[\\d]+";
//        System.out.println(img_filename_str);
        Pattern pattern = Pattern.compile(pattern_str);
        Matcher matcher = pattern.matcher(img_filename_str.trim());
        pmc = "";
//        System.out.println(matcher.group(1));
        while (matcher.find()) {
            pmc = matcher.group(0).replace("PMC", "");
        }
        setPmc(pmc);
        return pmc;
    }

    /**
     * @return the pmc
     */
    public String getPmc() {
        return pmc;
    }

    /**
     * @param pmc the pmc to set
     */
    public void setPmc(String pmc) {
        this.pmc = pmc;
    }

}
