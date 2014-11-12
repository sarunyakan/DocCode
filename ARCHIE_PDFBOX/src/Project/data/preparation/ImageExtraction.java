/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project.data.preparation;

import java.io.IOException;
import java.nio.file.Path;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

/**
 *
 * @author FANG
 */
public class ImageExtraction {

    private File oldFile;
    private Path filename;
    private String imgpath;
    private ArrayList<double[]> location_xy;
    private ArrayList<double[]> size_xy_ordered;
    private ArrayList<double[]> size_xy_tmp;
    private ArrayList<double[]> location_ordered;
    private ArrayList<String> original_imgName;

    public ImageExtraction() {
        this.filename = null;
        this.imgpath = "";
    }

    public ImageExtraction(Path filename, String imgpath) throws IOException, CryptographyException, COSVisitorException {
        this.filename = filename;
        this.imgpath = imgpath;

        oldFile = new File(filename.toString());
        extractImages(filename.toString(), imgpath.toString());
    }

    //-------------[EXTRACT IMAGES AND THEIR LOCATION AND SIZE]-----------------------------
    public void extractImages(String sourceDir, String destinationDir) throws IOException, CryptographyException, COSVisitorException {
        PDDocument document = null;
        double[] size;
        if (oldFile.exists()) {
            document = PDDocument.load(sourceDir);
            if (document.isEncrypted()) {
                document.decrypt("");
            }
            PrintImageLocation printer; // Get image location
            List<PDPage> list = document.getDocumentCatalog().getAllPages();

            String fileName_img = oldFile.getName().replace(".pdf", "_cover");
            int pageNum = 0;
            int totalImages = 1;
            System.out.println("\n" + filename);

            for (PDPage page : list) {

                original_imgName = new ArrayList<String>();
                location_xy = new ArrayList<double[]>();
                size_xy_ordered = new ArrayList<double[]>();
                size_xy_tmp = new ArrayList<double[]>();
                PDResources pdResources = page.getResources();
                Map pageImages = pdResources.getXObjects();
                pageNum++;
                if (pageImages != null && pageImages.size() > 0) {

                    Iterator imageIter = pageImages.keySet().iterator();
                    while (imageIter.hasNext()) {

                        String key = (String) imageIter.next();
                        PDXObjectImage pdxObjectImage = (PDXObjectImage) pageImages.get(key);
                        String imgName = fileName_img + "_" + totalImages;
                        System.out.println("Page Number : " + pageNum + "\t" + imgName);
                        pdxObjectImage.write2file(destinationDir + imgName);

                        original_imgName.add(imgName + "." + pdxObjectImage.getSuffix());
                        size = new double[]{pdxObjectImage.getWidth(), pdxObjectImage.getHeight()};
                        size_xy_ordered.add(size);
                        totalImages++;
                    }
                    //Start for detect figure name for image renaming
                    printer = new PrintImageLocation(page);
                    location_xy = printer.getLocation_xy();
                    size_xy_tmp = printer.getSize_xy();
                    RearrangeImageOrder(location_xy, size_xy_tmp, size_xy_ordered);
                    //PrinttoString();
                    DetectFigureName detectFig = new DetectFigureName(original_imgName, filename, pageNum, page, location_ordered, size_xy_ordered);
                }
            }
        } else {
            System.err.println("File not exists");
        }

        if (document != null) {
            document.close();
        }

    }

    public void RearrangeImageOrder(ArrayList<double[]> location, ArrayList<double[]> size_tmp, ArrayList<double[]> size) {
        location_ordered = new ArrayList<double[]>();
        if (size_tmp.size() == size.size()) {
            for (int i = 0; i < size_tmp.size(); i++) {
                if ((size_tmp.get(i)[0] != size.get(i)[0]) || (size_tmp.get(i)[1] != size.get(i)[1])) {
                    int index = FindMatchItem(size_tmp, size.get(i));
                    location_ordered.add(location.get(index));
                } else {
                    location_ordered.add(location.get(i));
                }
            }
        } else {
            System.err.println("Size of size_tmp.size() != size.size() >> CHECK!!!!");
        }
//        System.out.println(location_ordered);
//        System.out.println(size_xy_ordered);
    }

    public int FindMatchItem(ArrayList<double[]> size_tmp, double[] size) {
        int index = 0;
        double sizeX = size[0];
        double sizeY = size[1];
        for (int i = 0; i < size_tmp.size(); i++) {
            if (size_tmp.get(i)[0] == sizeX && size_tmp.get(i)[1] == sizeY) {
                index = i;
            }
        }
        return index;
    }

    public void PrinttoString() {
        for (double[] lo : location_ordered) {
            System.out.println("Location : " + lo[0] + " " + lo[1]);
        }
        for (double[] lo : size_xy_ordered) {
            System.out.println("Size : " + lo[0] + " " + lo[1]);
        }

        for (double[] lo : size_xy_tmp) {
            System.out.println("Size_tmp : " + lo[0] + " " + lo[1]);
        }
        System.out.println("");
    }

}
