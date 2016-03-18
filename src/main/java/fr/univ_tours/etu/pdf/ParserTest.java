package fr.univ_tours.etu.pdf;

import org.apache.lucene.document.Document;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Katherine on 10.03.2016.
 */
public class ParserTest {

    public static void main(String[] args) {
        PDFParser parser;
        PDFTextStripper pdfStripper;
        PDDocument pdDoc ;
        COSDocument cosDoc ;

        String Text ;
        String filePath = "src/main/resources/test_docs/N13-1041.pdf";
        File file;

        pdfStripper = null;
        pdDoc = null;
        cosDoc = null;

        file = new File(filePath);//tests
        
        try {
            /*parser = new PDFParser(new FileInputStream(file)); // update for PDFBox V 2.0
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            pdDoc.getNumberOfPages();
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(10);
            Text = pdfStripper.getText(pdDoc);*/
            Document luceneDocument = LucenePDFDocument.getDocument(file);
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

}
