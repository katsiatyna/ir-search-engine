package fr.univ_tours.etu.pdf;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.*;
import org.apache.lucene.document.Document;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;


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

        file = new File(filePath);
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
            testDictionary();
            Document luceneDocument = LucenePDFDocument.getDocument(file);
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testDictionary() throws IOException{
        String wnhome = System.getenv("WNHOME");
        String path = wnhome + File.separator + "dict";
        URL url = new URL("file", null, path);

        IDictionary dictionary = new Dictionary(url);
        dictionary.open();

        IIndexWord indexWord = dictionary.getIndexWord("database", POS.NOUN);
        IWordID wordID = indexWord.getWordIDs().get(0);
        IWord word = dictionary.getWord(wordID);
        System .out . println ("Id = " + wordID );
        System .out . println (" Lemma = " + word . getLemma ());
        System .out . println (" Gloss = " + word . getSynset (). getGloss ());

        List<ISynsetID> hypernyms =
            word.getSynset() . getRelatedSynsets ( Pointer . HYPERNYM );
        // print out each h y p e r n y m s id and synonyms
        List <IWord > words ;
        for( ISynsetID sid : hypernyms ){
            words = dictionary . getSynset (sid). getWords ();
            System .out . print (sid + " {");
            for(Iterator<IWord > i = words . iterator (); i. hasNext () ;){
                System .out . print (i. next (). getLemma ());
                if(i. hasNext ())
                    System .out . print (", ");
                }
            System .out . println ("}");
            }
        }
    }


