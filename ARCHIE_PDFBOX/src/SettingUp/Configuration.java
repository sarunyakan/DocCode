/*
 * This page is a configuration file. All constants will be placed here.
 */
package SettingUp;

/**
 * @author fang
 */
public class Configuration {

    public static final String PATH_DIR = "E:/Doctoral_Research/Source/PDF/02/00/";
    public static final String DANAME = "ARCHIE";
    public static final String USERNAME = "fang";
    public static final String PASSWORD = "fang";
    public static final int SWITCH_XML_MODE = 1;
    public static final double[] PAGE_SIZE_A4 = {595.0, 795.0};
    public static final String REGEX_FIG = "($(F|f)igs?\\.?.{0,1}[\\d+| ]\\.?|(F|f)igures?\\.?.{0,1}[\\d+| ]\\.?)";

    //Map Key
    public static final String CHARATER_KEY = "alphabet";
    public static final String POSITION_X_KEY = "position_x";
    public static final String POSITION_Y_KEY = "position_y";
    public static final String FONT_BASE_KEY = "font_base";
    public static final String ITALIC_KEY = "italic";
    public static final String BOLD_KEY = "bold";
    public static final String BOLD_AND_ITALIC_KEY = "bold_and_italic";

    //XML Extraction constants
    public static final String XML_PATH = "E:/Doctoral_Research/Source/XML/";
    public static final String[] XML_JOURNAL_TITLE = {"/article/front/journal-meta/journal-title", "/article/front/journal-meta/journal-title-group/journal-title"};
    public static final String[] XML_JOURNAL_ID = {"/article/front/journal-meta/journal-id[@journal-id-type='nlm-ta']"};

    public static final String[] XML_ARTICLE_TITLE = {"/article/front/article-meta/title-group/article-title[text()]"};
    public static final String[] XML_ARTICLE_KW = {"/article/front/article-meta/kwd-group/kwd[text()]"};
    public static final String[] XML_ARTICLE_PMID = {"article/front/article-meta/article-id[@pub-id-type='pmid'][text()]"};
    public static final String[] XML_ARTICLE_SUB = {"article/front/article-meta/article-categories/subj-group/subject[text()]"};
    public static final String[] XML_ARTICLE_PMC = {"article/front/article-meta/article-id[@pub-id-type='pmc'][text()]"};
    public static final String[] XML_ARTICLE_ABS = {"article/front/article-meta/abstract[not(@*)]//descendant::*[text()]"};
    public static final String[] XML_ARTICLE_BODY_FIG = {"article/body//sec//fig//descendant::*[text()] | article/body//sec//sec//fig//descendant::*[text()] | article/floats-group//fig//descendant::*[text()] | article/body//fig//descendant::*[text()]"};
    public static final String[] XML_ARTICLE_PARAGRAPH = {"article/body//sec//descendant-or-self::p[text()][not(parent::caption)]"};
    public static final String[] XML_AUTHOR = {"article/front/article-meta/contrib-group//contrib[@contrib-type='author']/name/descendant-or-self::*[text()]"};

}
