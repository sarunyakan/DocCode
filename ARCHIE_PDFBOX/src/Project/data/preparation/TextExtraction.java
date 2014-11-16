/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project.data.preparation;

import com.itextpdf.text.DocumentException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TextExtraction {

    private Path filename;
    //private String expression = "";
    private String RESULT = "";
    private ArrayList<String> textContent = new ArrayList<String>();
    private ArrayList<String> node_value = null;
    private ArrayList<String> para_node_value = null;
    private FileInputStream file;
    private DocumentBuilderFactory builderFactory;
    private DocumentBuilder builder;
    private Document xmlDocument;
    private XPath xPath;
    private ArrayList<String> journal_title_value = new ArrayList<String>();
    private ArrayList<String> journal_id_value = new ArrayList<String>();
    private ArrayList<String> article_title_value = new ArrayList<String>();
    private ArrayList<String> author_name_value = new ArrayList<String>();
    private ArrayList<String> author_surname_value = new ArrayList<String>();
    private ArrayList<String> pmid_value = new ArrayList<String>();
    private ArrayList<String> pmc_id_value = new ArrayList<String>();
    private ArrayList<String> keywords_value = new ArrayList<String>();
    private ArrayList<String> subject_value = new ArrayList<String>();
    private ArrayList<String> abstract_value = new ArrayList<String>();
    private ArrayList<String> figurenum_value = new ArrayList<String>();
    private ArrayList<String> caption_title_value = new ArrayList<String>();
    private ArrayList<String> caption_para_value = new ArrayList<String>();
    private ArrayList<String> figure_detail_value = new ArrayList<String>();
    private ArrayList<String> paragraph_value = new ArrayList<String>();

    public TextExtraction(Path filename) throws IOException, DocumentException, ParserConfigurationException, SAXException, XPathExpressionException, FileNotFoundException, InterruptedException {
        this.filename = filename;

        File inputFile = new File(filename.toString());

        file = new FileInputStream(inputFile);
        //Delete first line of file:DTD not found! Caused error!
        remove1stLine(inputFile);
        //=====================================================
        builderFactory = DocumentBuilderFactory.newInstance();
        builder = builderFactory.newDocumentBuilder();
        xmlDocument = builder.parse(file);
        xPath = XPathFactory.newInstance().newXPath();

        journal_title_value = ExtractArticleXML("JOURNAL-TITLE", Configuration.XML_JOURNAL_TITLE);
        journal_id_value = ExtractArticleXML("JOURNAL-ID", Configuration.XML_JOURNAL_ID);
        article_title_value = ExtractArticleXML("ARTICLE-TITLE", Configuration.XML_ARTICLE_TITLE);
        author_name_value = ExtractArticleXML("AUTHOR", Configuration.XML_AUTHOR);
        author_surname_value = ExtractArticleXML("AUTHOR", Configuration.XML_AUTHOR2);
        pmid_value = ExtractArticleXML("PMID", Configuration.XML_ARTICLE_PMID);
        pmc_id_value = ExtractArticleXML("PMC", Configuration.XML_ARTICLE_PMC);
        keywords_value = ExtractArticleXML("KEYWORDS", Configuration.XML_ARTICLE_KW);
        subject_value = ExtractArticleXML("SUBJECT", Configuration.XML_ARTICLE_SUB);
        abstract_value = ExtractArticleXML("ABSTRACT", Configuration.XML_ARTICLE_ABS);
        if (abstract_value.size() == 0) {
            abstract_value = ExtractArticleXML("ABSTRACT", Configuration.XML_ARTICLE_ABS2);
        }
//        System.out.println("FIGURE---------------------------------------------");

        figurenum_value = ExtractArticleXML("FIGURE", Configuration.XML_ARTICLE_BODY_FIG);
        caption_title_value = ExtractArticleXML("FIGURE_TITLE", Configuration.XML_ARTICLE_BODY_FIG_CAPTION_TITLE);
        caption_para_value = ExtractArticleXML("FIGURE_CAPTION", Configuration.XML_ARTICLE_BODY_FIG_CAPTION_PARA);
        paragraph_value = DetectParagraph("FIGURE_DETAIL", Configuration.XML_ARTICLE_PARAGRAPH);
//        System.out.println("");
    }

    public ArrayList<String> DetectParagraph(String title, String[] xml_ele) throws XPathExpressionException {
        para_node_value = new ArrayList<String>();
        String[] expression = xml_ele;
        boolean isFind = false;
        for (String ele : expression) {
            NodeList nodeList = (NodeList) xPath.compile(ele).evaluate(xmlDocument, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                if (isFoundWord(nodeList.item(i))) {
                    //System.out.println("DETAIL: " + nodeList.item(i).getFirstChild().getNodeValue() + "\n");
                    para_node_value.add(nodeList.item(i).getFirstChild().getNodeValue());
                }

            }

        }
        return para_node_value;
    }

    public boolean isFoundWord(Node item) {
        boolean isFound = false;

        String str = item.getFirstChild().getNodeValue();
        if (str != null) {
            String pattern = Configuration.REGEX_FIG_PARA;
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(str);
            if (m.find()) {
                return true;
            }
        }
        return isFound;
    }

    public String ReplaceRegEX(String line) {
        String sentence = "";
        String[] pattern_str = {
            //"<supplementary-material.*</supplementary-material>",
            //"<media [\\w|-|:-]+=\"[\\w|-]+\"( |[\\w|-|:-]+=\"[\\w|-|.]+\")+/>",
            //"<media [\\w|-|:-]+=\"[\\w|-]+\"[ |[\\w|-|:-]+={1}\"[\\w|-|.]+\"]*/>",
            "<sc>", "</sc>",
            //            "<inline-formula><inline-graphic [\\w|-|:-]+=\"[\\w|-]+.(gif|png|jpg|tif)\"/></inline-formula>",
            //"<ext-link [\\w|-]+=\"([\\w|-]+| )+\" [[\\w|-|:-]+=\"[\\w|-| |:-|/|.|?-|#|&|;|=]+\"]*>"
            "<ext-link [([\\w|-|:-]| )+=\"[\\w~|:|/|.|-|+|&|#|%|@|?-|=|;-]+\"]*>", "</ext-link>",
            //"<xref [\\w|-]+=\"([\\w|-]+| )+\" [[\\w|-]+=\"[\\w|-| ]+\"]*>", 
            "<xref [([\\w|-]| )+=\"[\\w|-| ]+\"]*/?>", "</xref>",
            "<graphic xmlns:xlink=.+ xlink:href=\"[\\w|-]+\"]/>",
            "<inline-graphic xlink:href=\"[\\w|-]+.(gif|png|jpg|tif)\"( [\\w|-]+=\"[\\w|-]+\")*/>",
            "<graphic xlink:href=\"[\\w|-]+\"( [\\w|-]+=\"[\\w|-]+\")*/>",
            "<graphic xlink:href=.*</graphic>",
            "<sup>", "</sup>", "<sub>", "</sub>", "<bold>", "</bold>", "<italic>", "</italic>", "<styled-content style=\"fixed-case\">", "</styled-content>"};

        for (String pat : pattern_str) {
            Pattern pattern = Pattern.compile(pat);
            Matcher matcher = pattern.matcher(line);
            line = matcher.replaceAll("");

        }

        Pattern pattern = Pattern.compile("&#x000a0;");
        Matcher matcher = pattern.matcher(line);
        line = matcher.replaceAll("");

        sentence = line;
        return sentence;
    }

    public void remove1stLine(File inputFile) throws FileNotFoundException, IOException, InterruptedException {
        String str = "";
        BufferedReader fi = new BufferedReader(new FileReader(inputFile));
        String line;

        while ((line = fi.readLine()) != null) {
            if (line.contains("<!DOCTYPE")) {
                ;
            } else {

                String sentence = ReplaceRegEX(line);
                str = str + " " + sentence;
            }

        }
        BufferedWriter fo = new BufferedWriter(new FileWriter(inputFile));

        fo.write(str);
        fo.flush();
        fo.close();
        fi.close();

    }

    public TextExtraction(Path filename, String RESULT) throws IOException, DocumentException {

        this.filename = filename;
        this.RESULT = RESULT;

    }

    public ArrayList<String> ExtractArticleXML(String title, String[] xml_ele) throws XPathExpressionException {
        String[] expression = xml_ele;
        node_value = new ArrayList<String>();
        for (String ele : expression) {
            NodeList nodeList = (NodeList) xPath.compile(ele).evaluate(xmlDocument, XPathConstants.NODESET);
            if (nodeList.getLength() > 0) {
//                PrintNode(title, nodeList); // print out all node
                for (int i = 0; i < nodeList.getLength(); i++) {
                    String text = nodeList.item(i).getFirstChild().getNodeValue();
                    if (text != null) {
                        //System.out.println("\t" + text);
                        node_value.add(text);
                    }
                }
            }
        }
        return node_value;
    }

    public void PrintNode(String title, NodeList nodeList) throws XPathExpressionException {
        System.out.print(title + " :\t");

        for (int i = 0; i < nodeList.getLength(); i++) {
            String text = nodeList.item(i).getFirstChild().getNodeValue();
            if (text != null) {
                System.out.println("\t>>" + text);
            }
            System.out.println("");
        }

    }

    public void PrintNodeNoNewLine(String title, NodeList nodeList) throws XPathExpressionException {
        System.out.print(title + " :\t");
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {

                System.out.print(i + "\t" + nodeList.item(i).getFirstChild().getNodeValue() + "\n");
            }
        }
    }

    /**
     * @return the journal_title_value
     */
    public ArrayList<String> getJournal_title_value() {
        return journal_title_value;
    }

    /**
     * @param journal_title_value the journal_title_value to set
     */
    public void setJournal_title_value(ArrayList<String> journal_title_value) {
        this.journal_title_value = journal_title_value;
    }

    /**
     * @return the journal_id_value
     */
    public ArrayList<String> getJournal_id_value() {
        return journal_id_value;
    }

    /**
     * @param journal_id_value the journal_id_value to set
     */
    public void setJournal_id_value(ArrayList<String> journal_id_value) {
        this.journal_id_value = journal_id_value;
    }

    /**
     * @return the article_title_value
     */
    public ArrayList<String> getArticle_title_value() {
        return article_title_value;
    }

    /**
     * @param article_title_value the article_title_value to set
     */
    public void setArticle_title_value(ArrayList<String> article_title_value) {
        this.article_title_value = article_title_value;
    }

    /**
     * @return the pmid_value
     */
    public ArrayList<String> getPmid_value() {
        return pmid_value;
    }

    /**
     * @param pmid_value the pmid_value to set
     */
    public void setPmid_value(ArrayList<String> pmid_value) {
        this.pmid_value = pmid_value;
    }

    /**
     * @return the pmc_id_value
     */
    public ArrayList<String> getPmc_id_value() {
        return pmc_id_value;
    }

    /**
     * @param pmc_id_value the pmc_id_value to set
     */
    public void setPmc_id_value(ArrayList<String> pmc_id_value) {
        this.pmc_id_value = pmc_id_value;
    }

    /**
     * @return the keywords_value
     */
    public ArrayList<String> getKeywords_value() {
        return keywords_value;
    }

    /**
     * @param keywords_value the keywords_value to set
     */
    public void setKeywords_value(ArrayList<String> keywords_value) {
        this.keywords_value = keywords_value;
    }

    /**
     * @return the subject_value
     */
    public ArrayList<String> getSubject_value() {
        return subject_value;
    }

    /**
     * @param subject_value the subject_value to set
     */
    public void setSubject_value(ArrayList<String> subject_value) {
        this.subject_value = subject_value;
    }

    /**
     * @return the abstract_value
     */
    public ArrayList<String> getAbstract_value() {
        return abstract_value;
    }

    /**
     * @param abstract_value the abstract_value to set
     */
    public void setAbstract_value(ArrayList<String> abstract_value) {
        this.abstract_value = abstract_value;
    }

    /**
     * @return the figure_detail_value
     */
    public ArrayList<String> getFigure_detail_value() {
        return figure_detail_value;
    }

    /**
     * @param figure_detail_value the figure_detail_value to set
     */
    public void setFigure_detail_value(ArrayList<String> figure_detail_value) {
        this.figure_detail_value = figure_detail_value;
    }

    /**
     * @return the filename
     */
    public Path getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(Path filename) {
        this.filename = filename;
    }

    /**
     * @return the author_name_value
     */
    public ArrayList<String> getAuthor_name_value() {
        return author_name_value;
    }

    /**
     * @param author_name_value the author_name_value to set
     */
    public void setAuthor_name_value(ArrayList<String> author_name_value) {
        this.author_name_value = author_name_value;
    }

    /**
     * @return the author_surname_value
     */
    public ArrayList<String> getAuthor_surname_value() {
        return author_surname_value;
    }

    /**
     * @param author_surname_value the author_surname_value to set
     */
    public void setAuthor_surname_value(ArrayList<String> author_surname_value) {
        this.author_surname_value = author_surname_value;
    }

    /**
     * @return the figurenum_value
     */
    public ArrayList<String> getFigurenum_value() {
        return figurenum_value;
    }

    /**
     * @param figurenum_value the figurenum_value to set
     */
    public void setFigurenum_value(ArrayList<String> figurenum_value) {
        this.figurenum_value = figurenum_value;
    }

    /**
     * @return the caption_title_value
     */
    public ArrayList<String> getCaption_title_value() {
        return caption_title_value;
    }

    /**
     * @param caption_title_value the caption_title_value to set
     */
    public void setCaption_title_value(ArrayList<String> caption_title_value) {
        this.caption_title_value = caption_title_value;
    }

    /**
     * @return the caption_para_value
     */
    public ArrayList<String> getCaption_para_value() {
        return caption_para_value;
    }

    /**
     * @param caption_para_value the caption_para_value to set
     */
    public void setCaption_para_value(ArrayList<String> caption_para_value) {
        this.caption_para_value = caption_para_value;
    }

    /**
     * @return the paragraph_value
     */
    public ArrayList<String> getParagraph_value() {
        return paragraph_value;
    }

    /**
     * @param paragraph_value the paragraph_value to set
     */
    public void setParagraph_value(ArrayList<String> paragraph_value) {
        this.paragraph_value = paragraph_value;
    }

}
