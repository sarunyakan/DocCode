/*
 * This page is a configuration file. All constants will be placed here.
 */
package SettingUp;

/**
 * @author fang
 */
public class Configuration {

    public static final String PATH_DIR = "E:/Doctoral_Research/Source/02/00/";
    public static final String IMGNAME = "Img%s.%s";
    public static final String DANAME = "ARCHIE";
    public static final String USERNAME = "fang";
    public static final String PASSWORD = "fang";
    public static final String PATTERN_ABS = "abstract";
    public static final int DECIMAL_CONS = 10;

    //XML Extraction constants
    public static final String XML_PATH = "E:/Doctoral_Research/Source/XML/";

    /**
     * Only for SUBJECT = 'ARTICLE'
     */
    public static final String[] XML_JOURNAL_TITLE = {"/article/front/journal-meta/journal-title", "/article/front/journal-meta/journal-title-group/journal-title"};
    public static final String[] XML_JOURNAL_ID = {"/article/front/journal-meta/journal-id[@journal-id-type='publisher-id']"};

    public static final String[] XML_ARTICLE_TITLE = {"/article/front/article-meta/title-group/article-title[text()]"};
    public static final String[] XML_ARTICLE_KW = {"/article/front/article-meta/kwd-group/kwd[text()]"};
    public static final String[] XML_ARTICLE_PMID = {"article/front/article-meta/article-id[@pub-id-type='pmid'][text()]"};
    public static final String[] XML_ARTICLE_SUB = {"article/front/article-meta/article-categories/subj-group/subject[text()]"};
    public static final String[] XML_ARTICLE_PMC = {"article/front/article-meta/article-id[@pub-id-type='pmc'][text()]"};
    public static final String[] XML_ARTICLE_ABS = {"article/front/article-meta/abstract[1]//*[text()]"};
    public static final String[] XML_ARTICLE_BODY_FIG = {"article/body//sec//fig//*[text()] | article/body//sec//sec//fig//*[text()] | article/floats-group//fig//*[text()] | article/body//fig//*[text()]"};
}
