/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project.data.preparation;

import java.io.IOException;
import org.apache.pdfbox.util.ResourceLoader;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.exceptions.InvalidPasswordException;
import org.apache.pdfbox.exceptions.WrappedIOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.PDFOperator;
import org.apache.pdfbox.util.PDFStreamEngine;
import org.apache.pdfbox.util.ResourceLoader;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

/**
 *
 * @author fang
 */
public class PrintImageLocation extends PDFStreamEngine {

    private ArrayList<double[]> location_xy = new ArrayList<double[]>();
    private ArrayList<double[]> size_xy = new ArrayList<double[]>();
    private PDPage page;

    public PrintImageLocation() throws IOException {
        super(ResourceLoader.loadProperties("C:\\Users\\fang\\workspace\\PDFbox\\Resources\\PDFTextStripper.properties", true));
    }

    public PrintImageLocation(PDPage page) throws IOException {
        super(ResourceLoader.loadProperties("C:\\Users\\fang\\workspace\\PDFbox\\Resources\\PDFTextStripper.properties", true));
        this.page = page;
        imageProcess();
    }

    public void imageProcess() throws IOException {
        if (page != null) {
            processStream(page, page.findResources(), page.getContents().getStream());
        }
    }

    public void processOperator(PDFOperator operator, List arguments) throws IOException {
        
        String operation = operator.getOperation();
        if (operation.equals("Do")) {
            COSName objectName = (COSName) arguments.get(0);
            Map xobjects = getResources().getXObjects();
            PDXObject xobject = (PDXObject) xobjects.get(objectName.getName());
            if (xobject instanceof PDXObjectImage) {
                try {
                    PDXObjectImage image = (PDXObjectImage) xobject;
                    PDPage page = getCurrentPage();
                    Matrix ctm = getGraphicsState().getCurrentTransformationMatrix();
                    double rotationInRadians = (page.findRotation() * Math.PI) / 180;

                    AffineTransform rotation = new AffineTransform();
                    rotation.setToRotation(rotationInRadians);
                    AffineTransform rotationInverse = rotation.createInverse();
                    Matrix rotationInverseMatrix = new Matrix();
                    rotationInverseMatrix.setFromAffineTransform(rotationInverse);
                    Matrix rotationMatrix = new Matrix();
                    rotationMatrix.setFromAffineTransform(rotation);

                    Matrix unrotatedCTM = ctm.multiply(rotationInverseMatrix);
                    float xScale = unrotatedCTM.getXScale();
                    float yScale = unrotatedCTM.getYScale();
                    double[] location = {unrotatedCTM.getXPosition(), unrotatedCTM.getYPosition()};
                   double[] size = {image.getWidth(), image.getHeight()};
                    location_xy.add(location);
                    size_xy.add(size);
                    setLocation_xy(location_xy);
                    setSize_xy(size_xy);
//                    System.out.println("Found image[" + objectName.getName() + "] "
//                            + "at " + unrotatedCTM.getXPosition() + "," + unrotatedCTM.getYPosition()
//                           // + " \t\tsize=" + (xScale / 100f * image.getWidth()) + "," + (yScale / 100f * image.getHeight())
//                            + " \t\tactual size=" + image.getWidth() + "," + image.getHeight());
                } catch (NoninvertibleTransformException e) {
                    throw new WrappedIOException(e);
                }
            }
        } else {
            super.processOperator(operator, arguments);
        }
    }

    /**
     * @return the location_xy
     */
    public ArrayList<double[]> getLocation_xy() {
        return location_xy;
    }

    /**
     * @param location_xy the location_xy to set
     */
    public void setLocation_xy(ArrayList<double[]> location_xy) {
        this.location_xy = location_xy;
    }

    /**
     * @return the size_xy
     */
    public ArrayList<double[]> getSize_xy() {
        return size_xy;
    }

    /**
     * @param size_xy the size_xy to set
     */
    public void setSize_xy(ArrayList<double[]> size_xy) {
        this.size_xy = size_xy;
    }

}
