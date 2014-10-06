/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SettingUp;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author FANG
 */
public class ImageExtraction {

    private Path filename;
    private String imgpath;

    public ImageExtraction() {
        this.filename = null;
        this.imgpath = "";
    }

    public ImageExtraction(Path filename, String imgpath) throws IOException, DocumentException {
        this.filename = filename;
        this.imgpath = imgpath;
        extractImages();
    }

    public void extractImages()
            throws IOException, DocumentException {
        PdfReader reader = new PdfReader(filename.toString());
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        ImageRenderListener listener = new ImageRenderListener(imgpath);
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            parser.processContent(i, listener);
        }
        reader.close();
    }
}
