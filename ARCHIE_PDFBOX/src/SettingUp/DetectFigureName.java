/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SettingUp;

import java.awt.Rectangle;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFTextStripperByArea;

public class DetectFigureName {

    private ArrayList<String> original_imgName;
    private PDPage page;
    private ArrayList<double[]> location_xy = new ArrayList<double[]>();
    private ArrayList<String> fontBase_Fig = null;
    private HashSet fontBase_Fig_set = null;
    private ArrayList<double[]> size_xy = new ArrayList<double[]>();
    private double center_line = Math.round(Configuration.PAGE_SIZE_A4[0] / 2);
    private double page_height = Configuration.PAGE_SIZE_A4[1];
    private double img_size_x = 0;
    private double img_size_y = 0;
    private PrintTextLocations printerTxt = null;
    private Rectangle rect = null;
    private boolean IsPageStyleRun = false;
    private ArrayList<HashMap> WordAppendArr = null;
    private ArrayList<HashMap> WordMatch = null;
    private Path filename;
    private int page_num = 0;

    public DetectFigureName() {
    }

    public DetectFigureName(ArrayList<String> original_imgName, Path filename, int page_num, PDPage page, ArrayList<double[]> location_xy, ArrayList<double[]> size_xy) throws IOException, COSVisitorException {
        this.filename = filename;

        this.page = page;
        this.location_xy = location_xy;
        this.size_xy = size_xy;
        this.page_num = page_num;
        this.original_imgName = original_imgName;
        RunPageStyle();
        PositionAreaByImg();

    }

    public void RunPageStyle() throws IOException {
        WordAppendArr = new ArrayList<HashMap>();
        if (!IsPageStyleRun) {
            printerTxt = new PrintTextLocations(page);
            WordAppendArr = printerTxt.getWordAppendArr();
//            printerTxt.printOut_MapArr(WordAppendArr);
            IsPageStyleRun = true;
        }

    }

    public void PositionAreaByImg() throws IOException, COSVisitorException {
        int index = 0;

        for (double[] pos : location_xy) {

            double pos_x = pos[0];
            double pos_y = pos[1];
            boolean isLeftSide = CheckImgSide(pos_x);

            if (!isLeftSide) {
                ExtractStringFromRightSide(pos_x, pos_y, pos, index);
            } else {
                ExtractStringFromLeftSide(pos_x, pos_y, pos, index);
            }
            index++;
        }
    }

    public void ExtractStringFromLeftSide(double pos_x, double pos_y, double[] pos, int index) throws IOException {
        String fig_number = "";
        double new_pos_y = pos_y;
        double new_pos_x = 0;
        String croppedString = "";

        //left + bottom
        do {
            ExtractPageContentArea textArea = new ExtractPageContentArea(filename.toString(), page_num, (int) new_pos_x, (int) new_pos_y, (int) Configuration.PAGE_SIZE_A4[0] / 2, (int) new_pos_y - Configuration.CROPPED_AREA_WIDTH);
            croppedString = textArea.getTextCropped();
            rect = new Rectangle(textArea.getUpper_x(), textArea.getUpper_y(), (int) textArea.getWidth(), textArea.getHeight());

            if (croppedString.length() > 0 && isFoundWord(Configuration.REGEX_FIG, croppedString)) {
                fig_number = checkFontStyle(Configuration.REGEX_FIG, croppedString, rect, WordAppendArr, pos, 0);
                System.out.println("Fig_number : " + fig_number);
                ImageRename ir = new ImageRename(Configuration.REGEX_FIG, filename.getParent().toString(), original_imgName.get(index), fig_number);
            } else if (croppedString.length() > 0 && !isFoundWord(Configuration.REGEX_FIG, croppedString)) {
                break;
            }
            new_pos_y = new_pos_y - Configuration.CROPPED_AREA_WIDTH;
        } while (new_pos_y >= 0 && fig_number.length() == 0);

        new_pos_y = pos_y;
        new_pos_x = 0;
        croppedString = "";

        //Upper
        if (fig_number.length() == 0) {
            do {
                ExtractPageContentArea textArea = new ExtractPageContentArea(filename.toString(), page_num, (int) new_pos_x, (int) new_pos_y + Configuration.CROPPED_AREA_WIDTH, (int) Configuration.PAGE_SIZE_A4[0] / 2, (int) new_pos_y);
                croppedString = textArea.getTextCropped().trim();
//                System.out.println("2 && " + textArea.getUpper_x() + "," + textArea.getUpper_y() + "," + textArea.getLower_x() + "," + textArea.getLower_y() + "," + (int) textArea.getWidth() + "," + textArea.getHeight());
                rect = new Rectangle(textArea.getUpper_x(), textArea.getUpper_y(), (int) textArea.getWidth(), textArea.getHeight());

                if (croppedString.length() > 0 && isFoundWord(Configuration.REGEX_FIG2, croppedString)) {
                    fig_number = checkFontStyle(Configuration.REGEX_FIG2, croppedString, rect, WordAppendArr, pos, 1);
                    System.out.println("Fig_number : " + fig_number);
                    ImageRename ir = new ImageRename(Configuration.REGEX_FIG2, filename.getParent().toString(), original_imgName.get(index), fig_number);
                }
                new_pos_y = new_pos_y + Configuration.CROPPED_AREA_WIDTH;
            } while (new_pos_y <= Configuration.PAGE_SIZE_A4[1] && fig_number.length() == 0);

            new_pos_y = pos_y;
            new_pos_x = 0;
            croppedString = "";

            //Right
            if (fig_number.length() == 0) {
                do {
                    ExtractPageContentArea textArea = new ExtractPageContentArea(filename.toString(), page_num, (int) new_pos_x, (int) new_pos_y + Configuration.CROPPED_AREA_WIDTH, (int) Configuration.PAGE_SIZE_A4[0], (int) new_pos_y);
                    croppedString = textArea.getTextCropped().trim();
//                    System.out.println("2 && " + textArea.getUpper_x() + "," + textArea.getUpper_y() + "," + textArea.getLower_x() + "," + textArea.getLower_y() + "," + (int) textArea.getWidth() + "," + textArea.getHeight());
                    rect = new Rectangle(textArea.getUpper_x(), textArea.getUpper_y(), (int) textArea.getWidth(), textArea.getHeight());

                    if (croppedString.length() > 0 && isFoundWord(Configuration.REGEX_FIG2, croppedString)) {
                        fig_number = checkFontStyle(Configuration.REGEX_FIG2, croppedString, rect, WordAppendArr, pos, 1);
                        System.out.println("Fig_number : " + fig_number);
                        ImageRename ir = new ImageRename(Configuration.REGEX_FIG2, filename.getParent().toString(), original_imgName.get(index), fig_number);
                    }
                    new_pos_y = new_pos_y + Configuration.CROPPED_AREA_WIDTH;
                } while (new_pos_y <= Configuration.PAGE_SIZE_A4[1] && fig_number.length() == 0);

            }
        }
    }

    public void ExtractStringFromRightSide(double pos_x, double pos_y, double[] pos, int index) throws IOException, COSVisitorException {
//        System.out.println("\n!!!!!! IMAGE DIMENSION :(x,y) : (" + pos_x + "," + pos_y + ") !!!!!!!!!!");
        String fig_number = "";
        double new_pos_y = pos_y;
        double new_pos_x = 0;
        String croppedString = "";
//====================================================================================================================
        //Run from the first of image position down to end of page
        do {
            new_pos_x = center_line;
            ExtractPageContentArea textArea = new ExtractPageContentArea(filename.toString(), page_num, (int) new_pos_x, (int) new_pos_y, (int) Configuration.PAGE_SIZE_A4[0], (int) new_pos_y - Configuration.CROPPED_AREA_WIDTH);
            croppedString = textArea.getTextCropped();
            rect = new Rectangle(textArea.getUpper_x(), textArea.getUpper_y(), (int) textArea.getWidth(), textArea.getHeight());
//            System.out.println("1 && " + textArea.getUpper_x() + "," + textArea.getUpper_y() + "," + textArea.getLower_x() + "," + textArea.getLower_y() + "," + (int) textArea.getWidth() + "," + textArea.getHeight());
//            System.out.println(croppedString + "\n###################################\n");

            if (croppedString.length() > 0 && isFoundWord(Configuration.REGEX_FIG, croppedString)) {
                fig_number = checkFontStyle(Configuration.REGEX_FIG, croppedString, rect, WordAppendArr, pos, 0);
                System.out.println("Fig_number : " + fig_number);
                ImageRename ir = new ImageRename(Configuration.REGEX_FIG, filename.getParent().toString(), original_imgName.get(index), fig_number);
            } else if (croppedString.length() > 0 && !isFoundWord(Configuration.REGEX_FIG, croppedString)) {
                break;
            }

            new_pos_y = new_pos_y - Configuration.CROPPED_AREA_WIDTH;
        } while (new_pos_y >= 0 && fig_number.length() == 0);

        //=================================================================================
        new_pos_y = pos_y;
        new_pos_x = 0;
        croppedString = "";
        //Run from the first of image position up to above of page
        if (fig_number.length() == 0) {
            do {

                new_pos_x = center_line;
                ExtractPageContentArea textArea = new ExtractPageContentArea(filename.toString(), page_num, (int) new_pos_x, (int) new_pos_y + Configuration.CROPPED_AREA_WIDTH, (int) Configuration.PAGE_SIZE_A4[0], (int) new_pos_y);
                croppedString = textArea.getTextCropped().trim();
//                System.out.println("2 && " + textArea.getUpper_x() + "," + textArea.getUpper_y() + "," + textArea.getLower_x() + "," + textArea.getLower_y() + "," + (int) textArea.getWidth() + "," + textArea.getHeight());
                rect = new Rectangle(textArea.getUpper_x(), textArea.getUpper_y(), (int) textArea.getWidth(), textArea.getHeight());

                if (croppedString.length() > 0 && isFoundWord(Configuration.REGEX_FIG2, croppedString)) {
                    fig_number = checkFontStyle(Configuration.REGEX_FIG2, croppedString, rect, WordAppendArr, pos, 1);
                    System.out.println("Fig_number : " + fig_number);
                    ImageRename ir = new ImageRename(Configuration.REGEX_FIG2, filename.getParent().toString(), original_imgName.get(index), fig_number);
                }
                new_pos_y = new_pos_y + Configuration.CROPPED_AREA_WIDTH;
            } while (new_pos_y <= Configuration.PAGE_SIZE_A4[1] && fig_number.length() == 0);
        }
    }

    public void printOut_MapArr(ArrayList<HashMap> MapArr) {
        for (Map ele : MapArr) {
            Iterator<String> keySetIterator = ele.keySet().iterator();
            while (keySetIterator.hasNext()) {
                String key = keySetIterator.next();
                System.out.println("\nkey: " + key + " value: " + ele.get(key));
            }
        }
    }

    //เอาค่า rect ที่จับคำที่ต้องการได้ มาใช้ 
    public String checkFontStyle(String reg, String croppedString, Rectangle rect, ArrayList<HashMap> WordAppendArr, double[] pos, int num) throws IOException {
        String words_ele = "";
        //printOut_MapArr(WordAppendArr);
        WordMatch = new ArrayList<HashMap>();
        fontBase_Fig = new ArrayList<String>();
        fontBase_Fig_set = new HashSet();
        WordAppendArr.removeAll(Arrays.asList(null, ""));
        int chk_con = 0;
        ArrayList<Integer> index_arr = new ArrayList<Integer>();
        for (HashMap wordApp : WordAppendArr) {
            double word_posX = Double.parseDouble(wordApp.get(Configuration.POSITION_X_KEY).toString());
            double word_posY = Double.parseDouble(wordApp.get(Configuration.POSITION_Y_KEY).toString());
            String isBold = wordApp.get(Configuration.BOLD_KEY).toString();
            words_ele = wordApp.get(Configuration.CHARATER_KEY).toString();
            words_ele = words_ele.trim();
            String isItalic = wordApp.get(Configuration.ITALIC_KEY).toString();
            String isBoth = wordApp.get(Configuration.BOLD_AND_ITALIC_KEY).toString();
            //================= Comment later=======================================

//            if (isFoundWord(reg, words_ele)) {
//                System.out.println(rect + " : X : " + word_posX + " Y :" + word_posY + " rect.x :" + rect.x + " rect.y : " + rect.y);
//                System.out.println(words_ele + " = " + (word_posX >= rect.x && word_posY <= rect.y));
//            }
            //================= Comment later=======================================
            if (word_posX >= rect.x && word_posY <= rect.y && isFoundWord(reg, words_ele)
                    && (isBold.equalsIgnoreCase("true") || isItalic.equalsIgnoreCase("true") || isBoth.equalsIgnoreCase("true"))) {
                WordMatch.add(wordApp);
                fontBase_Fig.add(Configuration.FONT_BASE_KEY);
//                System.out.println("Case1");
                chk_con = 1;
                index_arr.add(WordAppendArr.indexOf(wordApp));
                if (num == 0) {
                    break;
                }
            } else if (word_posX >= rect.x && word_posY <= rect.y && isFoundWord(reg, words_ele)) {
                WordMatch.add(wordApp);
                fontBase_Fig.add(Configuration.FONT_BASE_KEY);
//                System.out.println("Case2");
                chk_con = 2;
                index_arr.add(WordAppendArr.indexOf(wordApp));
                if (num == 0) {
                    break;
                }
            }
        }

        removeUsedEle(WordAppendArr, index_arr);

        Collections.sort(fontBase_Fig);
        fontBase_Fig_set.addAll(fontBase_Fig);

        //เอาแค่ตัวเดียวคือตัวแรก
        if (WordMatch.size() > 1) {
            words_ele = WordMatch.get(0).get(Configuration.CHARATER_KEY).toString();
        } else if (WordMatch.size() == 1) {
            words_ele = WordMatch.get(0).get(Configuration.CHARATER_KEY).toString();
        } else {
            words_ele = "Figure0";
            System.err.println("NOT FOUND MATCHED WORDS : FIG_NUMBER");
        }

        return words_ele;
    }

    public void removeUsedEle(ArrayList<HashMap> WordAppendArr, ArrayList<Integer> index_arr) {
        //System.out.println("BEFORE DEL >> WordAppendArr : " + WordAppendArr.size());
        for (Integer index_arr1 : index_arr) {
//            System.out.println("DEL index_arr1 :" + index_arr1);
            WordAppendArr.remove(WordAppendArr.get(index_arr1));
        }
        //System.out.println("AFTER DEL >> WordAppendArr : " + WordAppendArr.size());

    }

    public boolean isFoundWord(String reg, String str) {
        boolean isFound = false;
        if (str != null) {
            String pattern = reg;
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(str);

            if (m.find()) {
                return true;
            }
        }
        return isFound;
    }

    public String CropTextArea(PDPage page, int x, int y, int width, int height) throws IOException, COSVisitorException {
        String croppedStr = "";
        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);
        rect = new Rectangle(x, y, width, height);
        stripper.addRegion("class1", rect);
        PDPage firstPage = page;
        stripper.extractRegions(firstPage);

        System.out.println("Text in the area:" + rect);
        System.out.println(stripper.getTextForRegion("class1"));
        croppedStr = stripper.getTextForRegion("class1");
        return croppedStr;
    }

    public boolean CheckImgSide(double pos_x) {
        boolean isLeftSide = ((pos_x < center_line) ? true : false);
        return isLeftSide;
    }

}
