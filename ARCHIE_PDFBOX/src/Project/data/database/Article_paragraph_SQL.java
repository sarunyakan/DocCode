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
import org.w3c.dom.Node;

/**
 *
 * @author fang
 */
public class Article_paragraph_SQL extends SQL_operation {

    private Statement stmt_article_para = null;
    private Connection conn = null;
    private ArrayList<String> paragraph_txt = null;
    private ArrayList<String> paragraph_fig = null;
    private String table_name = "";
    private ArrayList<String> pmc_id = null;

    public Article_paragraph_SQL(Connection conn, String table_name, ArrayList<String> pmc_id, ArrayList<String> paragraph_txt) throws SQLException {
        this.conn = conn;
        this.table_name = table_name;
        this.pmc_id = pmc_id;
        this.paragraph_txt = paragraph_txt;
        Insert_Article_Image(conn, table_name, pmc_id, paragraph_txt);
    }

    public void Insert_Article_Image(Connection conn, String table_name, ArrayList<String> pmc_id, ArrayList<String> paragraph_txt) throws SQLException {
        stmt_article_para = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmt_article_para.executeQuery(SelectAllStr(table_name));
        ResultSetMetaData meta = rs.getMetaData();
        String att_str = Attribute_string(table_name, meta);
        String val_str = "";
        for (String txt : paragraph_txt) {
            if (table_name.equalsIgnoreCase(Configuration.ARTICLE_PARAGRAPH_TBL)) {
                val_str = ArticleParagraph_value_string(table_name, meta, pmc_id.get(0), txt, stmt_article_para);
            }
            InsertQuery(table_name, meta, stmt_article_para, att_str, val_str);
        }

    }

    public String ArticleParagraph_value_string(String table_name, ResultSetMetaData meta, String pmc_id, String paragraph, Statement stmt_article_para) throws SQLException {
        String fignum = "";
        String val_str = "";
        int seq = getNextval(Configuration.ARTICLE_PARAGRAPH_SEQ, stmt_article_para);
        String articlePara_pk = appendQuote(Configuration.ARTICLE_PARAGRAPH_PK + seq);
        String pmc_id_str = appendQuote(pmc_id);
        String para_str = appendQuote(paragraph.replace("'", "''"));
        //Extract fig
        fignum = appendQuote(FignumWord(paragraph));
        //----------
        val_str = articlePara_pk + "," + pmc_id_str + "," + para_str + "," + fignum;
        return val_str;
    }

    public String FignumWord(String para_txt) {
        ArrayList<String> fignum = new ArrayList<String>();
        String str = "";
        String str2 = "";

        if (para_txt != null) {
            String pattern = Configuration.REGEX_FIG_PARA + "( {0,1}(and|,) {0,1}\\d+)*";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(para_txt);
            if (m.find()) {
                String[] word = m.group(0).split(" ");
                for (int i = 0; i < word.length; i++) {
                    if (checkIfDFigureNum(word[i])) {
                        str = word[i];
                    } else if (checkIfDigit(word[i])) {
                        str = "figure" + word[i];
                    } else {
                        continue;
                    }
                    String line = StringChange(str);

                    if (str2.length() == 0) {
                        str2 = str2 + line;
                    } else {
                        str2 = str2 + "+" + line;
                    }
                }

            }

        }
        return str2;
    }

    public String StringChange(String fignum) {
        String tmp_str = fignum.replace(".", "").replace(":", "").replace("s", "").replace(" ", "").replace(",", "").toLowerCase();

        if (tmp_str.length() < 6) {
            tmp_str = tmp_str.replace("fig", "figure");
        } else {
            tmp_str = tmp_str;
        }
        return tmp_str;
    }

    public boolean checkIfDigit(String word) {
        String pattern_str = "[\\d]+";

        Pattern pattern = Pattern.compile(pattern_str);
        Matcher matcher = pattern.matcher(word);
        while (matcher.find()) {
            return true;
        }

        return false;
    }

    public boolean checkIfDFigureNum(String word) {
        String pattern_str = Configuration.REGEX_FIG_PARA;
        Pattern pattern = Pattern.compile(pattern_str);
        Matcher matcher = pattern.matcher(word);
        while (matcher.find()) {
            return true;
        }

        return false;
    }
}
