/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SettingUp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.FilteredTextRenderListener;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;
import com.itextpdf.text.pdf.parser.RenderFilter;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class ExtractPageContentArea {

    private String pdf;
    private int pageNum = 0;
    private int upper_x, lower_x = 0;
    private int upper_y, lower_y = 0;
    private int width = 0;
    private int height = 0;
    private String TextCropped = "";
    private Rectangle rect;

    public ExtractPageContentArea() {
    }

    public ExtractPageContentArea(String pdf, int pageNum, int upper_x, int upper_y, int lower_x, int lower_y) throws IOException {
        this.pageNum = pageNum;
        this.pdf = pdf;
        this.upper_x = upper_x;
        this.upper_y = upper_y;
        this.lower_x = lower_x;
        this.lower_y = lower_y;
        this.width = lower_x - upper_x;
        this.height = upper_y - lower_y;
        parsePdf(pdf, pageNum, upper_x, upper_y, lower_x, lower_y);
        setHeight(height);
        setWidth(width);
    }

    public void parsePdf(String pdf, int pageNum, int upper_x, int upper_y, int lower_x, int lower_y) throws IOException {
        PdfReader reader = new PdfReader(pdf);
        System.out.println("(" + upper_x + " , " + upper_y + ") to ( " + lower_x + " , " + lower_y + ")");
        rect = new Rectangle(upper_x, upper_y, lower_x, lower_y);
        RenderFilter filter = new RegionTextRenderFilter(getRect());
        TextExtractionStrategy strategy;
        strategy = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), filter);
        TextCropped = PdfTextExtractor.getTextFromPage(reader, pageNum, strategy);
        setTextCropped(TextCropped);
        reader.close();
    }

    /**
     * @return the TextCropped
     */
    public String getTextCropped() {
        return TextCropped;
    }

    /**
     * @param TextCropped the TextCropped to set
     */
    public void setTextCropped(String TextCropped) {
        this.TextCropped = TextCropped;
    }

    /**
     * @return the rect
     */
    public Rectangle getRect() {
        return rect;
    }

    /**
     * @param rect the rect to set
     */
    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    /**
     * @return the upper_x
     */
    public int getUpper_x() {
        return upper_x;
    }

    /**
     * @param upper_x the upper_x to set
     */
    public void setUpper_x(int upper_x) {
        this.upper_x = upper_x;
    }

    /**
     * @return the lower_x
     */
    public int getLower_x() {
        return lower_x;
    }

    /**
     * @param lower_x the lower_x to set
     */
    public void setLower_x(int lower_x) {
        this.lower_x = lower_x;
    }

    /**
     * @return the upper_y
     */
    public int getUpper_y() {
        return upper_y;
    }

    /**
     * @param upper_y the upper_y to set
     */
    public void setUpper_y(int upper_y) {
        this.upper_y = upper_y;
    }

    /**
     * @return the lower_y
     */
    public int getLower_y() {
        return lower_y;
    }

    /**
     * @param lower_y the lower_y to set
     */
    public void setLower_y(int lower_y) {
        this.lower_y = lower_y;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the x
     */
}
