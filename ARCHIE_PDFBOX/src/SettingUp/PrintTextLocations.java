/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SettingUp;

/**
 *
 * @author fang
 */
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PrintTextLocations extends PDFTextStripper {

    private HashMap<String, String> TextDesc = null;
    private HashMap<String, String> WordAppend = null;
    private PDPage page = null;
    private ArrayList<HashMap> TextDescArr = new ArrayList<HashMap>();
    private ArrayList<HashMap> WordAppendArr = new ArrayList<HashMap>();

    public PrintTextLocations() throws IOException {
        super.setSortByPosition(true);
        this.page = null;
    }

    public PrintTextLocations(PDPage page) throws IOException {
        super.setSortByPosition(true);
        this.page = page;
        TextStyleProcess();
    }

    public void TextStyleProcess() throws IOException {
        PDStream contents = page.getContents();
        if (contents != null) {
            //RUN WHOLE PAGE IN ONE TIME

            processStream(page, page.findResources(), page.getContents().getStream());
            //++++++++++++++++++++++++++++++++++++
            //เอา char ที่มีลักษณะเหมือนกันมาต่อกันเป็นคำเดียว แล้วเอา position ของตัวแรกมาเก็บไว้ เมื่อทำเสดในใส่ลงไปใน TextDescArr
            appendWordsSameStyle();
            //++++++++++++++++++++++++++++++++++++
            //setTextDescArr(TextDescArr);
        }

    }

    public void appendWordsSameStyle() {
//        System.out.println(TextDescArr.size());
        String font_base_tmp = TextDescArr.get(0).get(Configuration.FONT_BASE_KEY).toString();

        String words = "";
        String tmp = "";
        int num = 0;
        String pox = "";
        String poy = "";
        String isBold = "";
        String isItalic = "";
        String isBoth = "";
        String prev_pos_y = "";
        for (Map characters : TextDescArr) {
            WordAppend = new HashMap<String, String>();
            String ch = characters.get(Configuration.CHARATER_KEY).toString();
            String fontBase = characters.get(Configuration.FONT_BASE_KEY).toString();

            if (num == 0) {
                //For only first round
                pox = characters.get(Configuration.POSITION_X_KEY).toString();
                poy = characters.get(Configuration.POSITION_Y_KEY).toString();
                num = 1;
                prev_pos_y = poy;
            }
            String cur_pos_y = characters.get(Configuration.POSITION_Y_KEY).toString();
            if (fontBase.equalsIgnoreCase(font_base_tmp) && prev_pos_y.equalsIgnoreCase(cur_pos_y)) {
                words = words + tmp + ch;
                tmp = "";
                isBold = characters.get(Configuration.BOLD_KEY).toString();
                isItalic = characters.get(Configuration.ITALIC_KEY).toString();
                isBoth = characters.get(Configuration.BOLD_AND_ITALIC_KEY).toString();

            } else {
                prev_pos_y = characters.get(Configuration.POSITION_Y_KEY).toString();
                WordAppend.put(Configuration.CHARATER_KEY, words.toString());
                WordAppend.put(Configuration.POSITION_X_KEY, pox);
                WordAppend.put(Configuration.POSITION_Y_KEY, poy);
                WordAppend.put(Configuration.FONT_BASE_KEY, font_base_tmp);
                WordAppend.put(Configuration.BOLD_KEY, isBold);
                WordAppend.put(Configuration.ITALIC_KEY, isItalic);
                WordAppend.put(Configuration.BOLD_AND_ITALIC_KEY, isBoth);

                pox = characters.get(Configuration.POSITION_X_KEY).toString();
                poy = characters.get(Configuration.POSITION_Y_KEY).toString();
                font_base_tmp = fontBase;
                tmp = ch;

            }

            if (WordAppend.size() > 0) {
                getWordAppendArr().add(WordAppend);
            }

            if (!WordAppend.isEmpty()) {
                WordAppend = null;
                words = "";
            }
        }
        setWordAppendArr(WordAppendArr);
//        printOut_MapArr(getWordAppendArr());
        
    }

    public void processTextPosition(TextPosition text) {
        TextDesc = new HashMap<String, String>();

        TextDesc.put(Configuration.CHARATER_KEY, text.getCharacter() + "");
        TextDesc.put(Configuration.POSITION_X_KEY, text.getXDirAdj() + "");
        TextDesc.put(Configuration.POSITION_Y_KEY, Configuration.PAGE_SIZE_A4[1]-text.getYDirAdj() + "");
        TextDesc.put(Configuration.FONT_BASE_KEY, text.getFont().getBaseFont());
        TextDesc.put(Configuration.ITALIC_KEY, text.getFont().getBaseFont().toLowerCase().toString().contains("-it") + "");
        TextDesc.put(Configuration.BOLD_KEY, text.getFont().getBaseFont().toLowerCase().contains("bold") + "");
        TextDesc.put(Configuration.BOLD_AND_ITALIC_KEY, text.getFont().getBaseFont().toLowerCase().contains("boldIt") + "");
        TextDescArr.add(TextDesc);

//        System.out.println("String[" + text.getXDirAdj() + ","
//                + text.getYDirAdj() + " fs=" + text.getFontSize() + " xscale="
//                + text.getXScale() + " height=" + text.getHeightDir() + " space="
//                + text.getWidthOfSpace() + " width="
//                + text.getWidthDirAdj() + "]" + text.getCharacter());
//        System.out.append(text.getCharacter() + " <--------------------------------");
//        System.out.println(text.getFont().getBaseFont());
//        System.out.println(" Italic=" + text.getFont().getFontDescriptor().isItalic());
//        System.out.println(" Bold=" + (text.getFont().getBaseFont().contains("bold") || text.getFont().getBaseFont().contains("Bold")));
//        System.out.println(" ItalicAngle=" + text.getFont().getFontDescriptor().getItalicAngle());
//        System.out.println(" Bold And Italic=" + text.getFont().getBaseFont().contains("boldIt"));
//        System.out.println(" xxxx=" + text.getFont().getFontDescriptor().isFixedPitch());
    }

    public void printOut_MapArr(ArrayList<HashMap> MapArr) {
        for (Map ele : MapArr) {

            Iterator<String> keySetIterator = ele.keySet().iterator();

            while (keySetIterator.hasNext()) {
                String key = keySetIterator.next();
                System.out.println("\nkey: " + key + " value: " + ele.get(key));
            }
            System.out.println("----------------");
        }

    }

    /**
     * @return the TextDescArr
     */
    public ArrayList<HashMap> getTextDescArr() {
        return TextDescArr;
    }

    /**
     * @param TextDescArr the TextDescArr to set
     */
    public void setTextDescArr(ArrayList<HashMap> TextDescArr) {
        this.TextDescArr = TextDescArr;
    }

    /**
     * @return the WordAppendArr
     */
    public ArrayList<HashMap> getWordAppendArr() {
        return WordAppendArr;
    }

    /**
     * @param WordAppendArr the WordAppendArr to set
     */
    public void setWordAppendArr(ArrayList<HashMap> WordAppendArr) {
        this.WordAppendArr = WordAppendArr;
    }

    public boolean isWordInSameLineY(Map characters) {
        double y_position = (double) characters.get(Configuration.POSITION_Y_KEY);

        return false;
    }

}
