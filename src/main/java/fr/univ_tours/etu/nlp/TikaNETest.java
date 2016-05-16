package fr.univ_tours.etu.nlp;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ner.NamedEntityParser;
import org.apache.tika.parser.ner.corenlp.CoreNLPNERecogniser;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Katherine on 14.05.2016.
 */
public class TikaNETest {

    public static final String CONFIG_FILE = "tika-config.xml";
    public static void main(String[] args){
        try {
            PDFParser parser;
            PDFTextStripper pdfStripper;
            PDDocument pdDoc;
            COSDocument cosDoc;

            String text;
            String filePath = "c:\\Users\\Katherine\\Documents\\2nd sem\\IR\\project\\IR CORPUS DOCS\\1201.0409.pdf";
            File file;

            pdfStripper = null;
            pdDoc = null;
            cosDoc = null;

            file = new File(filePath);
            try {
                parser = new PDFParser(new FileInputStream(file)); // update for PDFBox V 2.0
                parser.parse();
                cosDoc = parser.getDocument();
                pdfStripper = new PDFTextStripper();
                pdDoc = new PDDocument(cosDoc);
                pdDoc.getNumberOfPages();
                //pdfStripper.setStartPage(1);
                //pdfStripper.setEndPage(10);
                text = pdfStripper.getText(pdDoc);
                String resultString = text.replaceAll("\\p{C}|\\p{Sm}|\\p{Sk}|\\p{So}", " ");

                System.out.println();
                new TikaNETest().test(text);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (TikaException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
    public void test(String text) throws TikaException, IOException, SAXException {
        System.setProperty(NamedEntityParser.SYS_PROP_NER_IMPL,
                CoreNLPNERecogniser.class.getName());


        //test config is added to resources directory
        //InputStreamReader reader = new FileReader(new File(CONFIG_FILE));
        TikaConfig config = new TikaConfig(getClass().getClassLoader()
                .getResourceAsStream(CONFIG_FILE));
        Tika tika = new Tika(config);

        //String text = "I am student at University of Southern California(USC)," +
        //        " located in Los Angeles, California. USC's football team is called by name Trojans." +
         //       " Mr. John McKay was the head coach of the team from 1960 - 1975.";
        Metadata md = new Metadata();
        tika.parse(new ByteArrayInputStream(text.getBytes()), md);

        HashSet<String> set = new HashSet<String>();
        set.addAll(Arrays.asList(md.getValues("X-Parsed-By")));


        set.clear();
        set.addAll(Arrays.asList(md.getValues("NER_PERSON")));


        set.clear();
        set.addAll(Arrays.asList(md.getValues("NER_LOCATION")));


        set.clear();
        set.addAll(Arrays.asList(md.getValues("NER_ORGANIZATION")));


        set.clear();
        set.addAll(Arrays.asList(md.getValues("NER_DATE")));

        set.clear();
        set.addAll(Arrays.asList(md.getValues("NER_TIME")));

        set.clear();
        set.addAll(Arrays.asList(md.getValues("NER_PERCENT")));

        set.clear();
        set.addAll(Arrays.asList(md.getValues("NER_MONEY")));

        set.clear();
        set.addAll(Arrays.asList(md.getValues("NER_MISCELLANEOUS")));

    }
}
