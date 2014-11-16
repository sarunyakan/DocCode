/*
 * This page is a configuration file. All constants will be placed here.
 */
package Project.data.preparation;

/**
 * @author fang
 */
public class Configuration {

    //-------SOURCE PATH---------------------
    public static final String PATH_DIR = "E:/Doctoral_Research/Source/PDF/";

    //-------DATABASE CONFIGURATION---------------------
    public static final String DANAME = "ARCHIE";
    public static final String USERNAME = "fang";
    public static final String PASSWORD = "fang";

    //-------IMAGE EXTRACTION---------------------
    public static final double[] PAGE_SIZE_A4 = {595.0, 795.0};
    public static final String REGEX_FIG = "(^(F|f)igures?\\.? {0,1}\\d*\\.?|^(F|f)igs?\\.? {0,1}\\d*\\.?)";
    public static final String REGEX_FIG2 = "((^|\n*)(F|f)igures?\\.? {0,1}\\d*\\.?|(^|\n*)(F|f)igs?\\.? {0,1}\\d*\\.?)";
    public static final String REGEX_FIG3 = "(^?(F|f)igures?\\.? {0,1}\\d*\\.?|^?(F|f)igs?\\.? {0,1}\\d*\\.?)";
    public static final String REGEX_FIG4 = "((^|\n)?(F|f)igures?\\.? {0,1}\\d*\\.?|(^|\n)?(F|f)igs?\\.? {0,1}\\d*\\.?)";
    public static final String REGEX_FIG_PARA = "((F|f)igures?\\.? {0,1}\\d+\\.?|(F|f)igs?\\.? {0,1}\\d+\\.?)";
    public static final int CROPPED_AREA_WIDTH = 20;

    //-------MAP<KEY,VALUE>------------------
    public static final String CHARATER_KEY = "alphabet";
    public static final String POSITION_X_KEY = "position_x";
    public static final String POSITION_Y_KEY = "position_y";
    public static final String FONT_BASE_KEY = "font_base";
    public static final String ITALIC_KEY = "italic";
    public static final String BOLD_KEY = "bold";
    public static final String BOLD_AND_ITALIC_KEY = "bold_and_italic";

    //-------XML EXTRACTION---------------
    public static final int SWITCH_XML_MODE = 1;
    public static final String XML_PATH = "E:/Doctoral_Research/Source/XML/";
    public static final String[] XML_JOURNAL_TITLE = {"/article/front/journal-meta/journal-title", "/article/front/journal-meta/journal-title-group/journal-title"};
    public static final String[] XML_JOURNAL_ID = {"/article/front/journal-meta/journal-id[@journal-id-type='nlm-ta']"};
    public static final String[] XML_ARTICLE_TITLE = {"/article/front/article-meta/title-group/article-title[text()]"};
    public static final String[] XML_ARTICLE_KW = {"/article/front/article-meta/kwd-group/kwd[text()]"};
    public static final String[] XML_ARTICLE_PMID = {"article/front/article-meta/article-id[@pub-id-type='pmid'][text()]"};
    public static final String[] XML_ARTICLE_SUB = {"article/front/article-meta/article-categories/subj-group/subject[text()]"};
    public static final String[] XML_ARTICLE_PMC = {"article/front/article-meta/article-id[@pub-id-type='pmc'][text()]"};
    public static final String[] XML_ARTICLE_ABS = {"article/front/article-meta/abstract[not(@*)]//descendant::*[text()]"};
    public static final String[] XML_ARTICLE_ABS2 = {"article/front/article-meta/abstract//descendant::*[text()]"};
//    public static final String[] XML_ARTICLE_BODY_FIG = {"article/body//sec//fig//descendant::*[text()] | article/body//sec//sec//fig//descendant::*[text()] | article/floats-group//fig//descendant::*[text()] | article/body//fig//descendant::*[text()]"};
    public static final String[] XML_ARTICLE_BODY_FIG = {"article/body//sec//fig//label[text()] | article/body//sec//sec//fig//label[text()] | article/floats-group//fig//label[text()] | article/body//fig//label[text()]"};
    public static final String[] XML_ARTICLE_BODY_FIG_CAPTION_TITLE = {"article/body//sec//fig//caption//title[text()] | article/body//sec//sec//fig//caption//title[text()] | article/floats-group//fig//caption//title[text()] | article/body//fig//caption//title[text()]"};
    public static final String[] XML_ARTICLE_BODY_FIG_CAPTION_PARA = {"article/body//sec//fig//caption//p[text()] | article/body//sec//sec//fig//caption//p[text()] | article/floats-group//fig//caption//p[text()] | article/body//fig//caption//p[text()]"};
    public static final String[] XML_ARTICLE_PARAGRAPH = {"article/body//sec//descendant-or-self::p[text()][not(parent::caption)]"};
//    public static final String[] XML_AUTHOR = {"article/front/article-meta/contrib-group//contrib[@contrib-type='author']/name/descendant-or-self::*[text()]"};
    public static final String[] XML_AUTHOR = {"article/front/article-meta/contrib-group//contrib[@contrib-type='author']/name/given-names[text()]"};
    public static final String[] XML_AUTHOR2 = {"article/front/article-meta/contrib-group//contrib[@contrib-type='author']/name/surname[text()]"};
    //-------TABLE NAME---------------------
    public static final String FIGURE_TBL = "Figure";
    public static final String ARTICLE_IMAGE_TBL = "Article_image";
    public static final String CLUSTER_IMAGE_TBL = "Cluster_image";
    public static final String KEYWORD_OF_CLUSTER_TBL = "Keyword_of_cluster";
    public static final String CLUSTER_KEYWORD_TBL = "Cluster_keyword";
    public static final String IMAGE_OF_PARAGRAPH_TBL = "Image_of_paragraph";
    public static final String ARTICLE_PARAGRAPH_TBL = "Article_paragraph";
    public static final String ARTICLE_TBL = "Article";
    public static final String SUBJECT_TBL = "Subject";
    public static final String JOURNAL_TBL = "Journal";
    public static final String IMAGE_CAPTION_TBL = "Image_caption";
    public static final String FILE_PATH_TBL = "File_path";
    public static final String AUTHOR_OF_ARTICLE_TBL = "Author_of_article";
    public static final String KEYWORD_OF_ARTICLE_TBL = "Keyword_of_article";
    public static final String ARTICLE_KEYWORD_TBL = "Article_keyword";
    public static final String AUTHOR_TBL = "Author";

    //-------TABLE OBJECT---------------------
    public static final String RESET = "ON";

    public static final String ARTICLE_IMAGE_SEQ = "article_image_id_seq";
    public static final String ARTICLE_IMAGE_PK = "AI";

    public static final String FIGURE_IMAGE_PK = "figure";

    public static final String ARTICLE_KEYWORD_SEQ = "article_keyword_id_seq";
    public static final String ARTICLE_KEYWORD_PK = "AK";

    public static final String SUBJECT_PK = "S";
    public static final String SUBJECT_SEQ = "subject_id_seq";

    public static final String JOURNAL_PK = "J";
    public static final String JOURNAL_SEQ = "journal_id_seq";

    public static final String FILEPATH_PK = "FP";
    public static final String FILEPATH_SEQ = "file_path_id_seq";

    public static final String AUTHOR_PK = "A";
    public static final String AUTHOR_SEQ = "author_id_seq";

    public static final String IMAGE_CAPTION_PK = "IC";
    public static final String IMAGE_CAPTION_SEQ = "image_caption_id_seq";

    public static final String ARTICLE_PARAGRAPH_PK = "AP";
    public static final String ARTICLE_PARAGRAPH_SEQ = "article_paragraph_id_seq";
}
