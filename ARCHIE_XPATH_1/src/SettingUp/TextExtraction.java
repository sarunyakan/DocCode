/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SettingUp;

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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TextExtraction {

    private Path filename;
    //private String expression = "";
    private String RESULT = "";
    private ArrayList<String> textContent = new ArrayList<String>();
    private FileInputStream file;
    private DocumentBuilderFactory builderFactory;
    private DocumentBuilder builder;
    private Document xmlDocument;
    private XPath xPath;

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

        ExtractArticleXML("JOURNAL-TITLE", Configuration.XML_JOURNAL_TITLE);
        ExtractArticleXML("JOURNAL-ID", Configuration.XML_JOURNAL_ID);

        ExtractArticleXML("ARTICLE-TITLE", Configuration.XML_ARTICLE_TITLE);
        ExtractArticleXML("PMID", Configuration.XML_ARTICLE_PMID);
        ExtractArticleXML("PMC", Configuration.XML_ARTICLE_PMC);
        ExtractArticleXML("KEYWORDS", Configuration.XML_ARTICLE_KW);
        ExtractArticleXML("SUBJECT", Configuration.XML_ARTICLE_SUB);
        ExtractArticleXML("ABSTRACT", Configuration.XML_ARTICLE_ABS);
        System.out.println("FIGURE---------------------------------------------");
        ExtractArticleXML("FIGURE", Configuration.XML_ARTICLE_BODY_FIG);
        System.out.println("");
    }

    public void remove1stLine(File inputFile) throws FileNotFoundException, IOException, InterruptedException {
        String str = "";
        BufferedReader fi = new BufferedReader(new FileReader(inputFile));
        String line;

        while ((line = fi.readLine()) != null) {
            if (line.contains("<!DOCTYPE")) {
                ;
            } else {
                //System.out.println(line);
                //String sentence = ReplaceRegEX(line);
                //str = str + " " + sentence;
                str = str + " " + line;
            }

        }
        BufferedWriter fo = new BufferedWriter(new FileWriter(inputFile));

        fo.write(str);
        fo.flush();
        fo.close();
        fi.close();

    }

    public String ReplaceRegEX(String line) {
        String sentence = "";
        String[] pattern_str = {
            //"<supplementary-material.*</supplementary-material>",
            //"<media [\\w|-|:-]+=\"[\\w|-]+\"( |[\\w|-|:-]+=\"[\\w|-|.]+\")+/>",
            //"<media [\\w|-|:-]+=\"[\\w|-]+\"[ |[\\w|-|:-]+={1}\"[\\w|-|.]+\"]*/>",
            "<sc>", "</sc>",
            "<inline-formula><inline-graphic [\\w|-|:-]+=\"[\\w|-]+.(gif|png|jpg|tif)\"/></inline-formula>",
            
            "<xref [\\w|-]+=\"([\\w|-]+| )+\" [[\\w|-]+=\"[\\w|-| ]+\"]*>", "</xref>",
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

        sentence = line;
        return sentence;
    }

    public TextExtraction(Path filename, String RESULT) throws IOException, DocumentException {

        this.filename = filename;
        this.RESULT = RESULT;

    }

    public void ExtractArticleXML(String title, String[] xml_ele) throws XPathExpressionException {
        String[] expression = xml_ele;
        for (String ele : expression) {
            NodeList nodeList = (NodeList) xPath.compile(ele).evaluate(xmlDocument, XPathConstants.NODESET);
            if (nodeList.getLength() > 0) {
                PrintNode(title, nodeList); // print out all node
            }
        }
    }

    public void PrintNode(String title, NodeList nodeList) throws XPathExpressionException {
        System.out.print(title + " :\t");

        for (int i = 0; i < nodeList.getLength(); i++) {
            // System.out.println(nodeList.item(i));
            String text = nodeList.item(i).getFirstChild().getNodeValue();
            if (text != null) {
                System.out.println("\t" + text);
            }

        }

    }

    public void PrintNodeNoNewLine(String title, NodeList nodeList) throws XPathExpressionException {
        System.out.print(title + " :\t");
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                System.out.print("\t" + nodeList.item(i).getFirstChild().getNodeValue());
            }
        }

    }
}
