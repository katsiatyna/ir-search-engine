package fr.univ_tours.etu.pdf;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.*;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import fr.univ_tours.etu.nlp.CoreNlpTokenizer;
import fr.univ_tours.etu.nlp.NlpNeTokenizer;
import fr.univ_tours.etu.nlp.SemicolonAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.QueryBuilder;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;


/**
 * Created by Katherine on 10.03.2016.
 */
public class ParserTest {
    static String filePath = "c:\\Users\\Katherine\\Documents\\2nd sem\\IR\\project\\ir-search-engine\\src\\main\\resources\\test_docs\\J07-4010.pdf";

    public static void main(String[] args) throws ParseException {
        /*PDFParser parser;
        PDFTextStripper pdfStripper;
        PDDocument pdDoc;
        COSDocument cosDoc;
*/
        String text;

        File file;

  /*      pdfStripper = null;
        pdDoc = null;
        cosDoc = null;
  */    String parsed =   parseWithTika();
        file = new File(filePath);
        try {
    /*        parser = new PDFParser(new FileInputStream(file)); // update for PDFBox V 2.0
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            pdDoc.getNumberOfPages();
    */        //pdfStripper.setStartPage(1);
            //pdfStripper.setEndPage(10);
      /*      text = pdfStripper.getText(pdDoc);
            String resultString = text.replaceAll("\\p{C}|\\p{Sm}|\\p{Sk}|\\p{So}", " ");
            */

            //testDictionary();
            testOpenNlp(parsed);
            testOpenNlp("anas al bassit");
            testOpenNlp("Anas Al Bassit");
            testOpenNlp("barack obama");
            testOpenNlp("Barack Obama");


            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        catch (ParseException e) {
//            e.printStackTrace();
//        }
    }


    public static void testOpenNlp(String text) throws IOException, ParseException {

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
        //props.put("pos.model", "edu/stanford/nlp/models/pos-tagger/english-caseless-left3words-distsim.tagger");
        //props.put("parse.model", "edu/stanford/nlp/models/lexparser/englishPCFG.caseless.ser.gz");
        //props.put("ner.model", "edu/stanford/nlp/models/ner/english.muc.7class.caseless.distsim.crf.ser.gz");
        //props.put("ner.model", "edu/stanford/nlp/models/ner/english.all.3class.caseless.distsim.crf.ser.gz edu/stanford/nlp/models/ner/english.muc.7class.caseless.distsim.crf.ser.gz edu/stanford/nlp/models/ner/english.conll.4class.caseless.distsim.crf.ser.gz");
        NlpNeTokenizer nlpNeTokenizer = new CoreNlpTokenizer(props);
        nlpNeTokenizer.tokenize(text, false);
        List<String> neList = nlpNeTokenizer.getNeList();
        String neString = nlpNeTokenizer.getNeString(";", false);
        List<String> lemmaList = nlpNeTokenizer.getLemmaList();
        String lemmaString = nlpNeTokenizer.getLemmaString();
        System.out.println();

        //creating index
        // map field-name to analyzer
        Map<String, Analyzer> analyzerPerField = new HashMap<String, Analyzer>();
        analyzerPerField.put("namedEntities", new SemicolonAnalyzer());
        //analyzerPerField.put("someotherfield", new SomeOtherAnalyzer());

        // create a per-field analyzer wrapper using the StandardAnalyzer as .. standard analyzer ;)
        /*PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(
                new StandardAnalyzer(), analyzerPerField);
        Document luceneDocument = LucenePDFDocument.getDocument(file);
        Directory index = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(analyzer)
                .setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter writer = new IndexWriter(index, config);
        luceneDocument.add(new TextField("namedEntities", neString, Field.Store.YES));
        writer.addDocument(luceneDocument);
        writer.commit();
        writer.close();

        //trying to read
        IndexReader reader = DirectoryReader.open(index);

        Set<String> termStrings = new TreeSet<>();
        for (LeafReaderContext atomicReaderContext : reader.leaves()) {
            LeafReader atomicReader = atomicReaderContext.reader();
            TermsEnum iterator = atomicReader.terms("namedEntities").iterator();
            BytesRef next = iterator.next();
            while (next != null) {
                termStrings.add(iterator.term().utf8ToString());
                next = iterator.next();
            }
        }

        String queryString = "Jing Jiang USA";
        NlpNeTokenizer queryTokenizer = new CoreNlpTokenizer(props);
        queryTokenizer.tokenize(queryString);
        String neQuery = queryTokenizer.getNeString(";", true);
        //QueryBuilder builder = new QueryBuilder(new SemicolonAnalyzer());
        //Query query = builder.createPhraseQuery("namedEntities",neQuery);
        Query query = new QueryParser("namedEntities", new SemicolonAnalyzer()).parse(neQuery);

        //for(String ne: queryTokenizer.getNeList()) {
            //Query query = new TermQuery(new Term("namedEntities", ne));
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs docs = searcher.search(query, 1);

            System.out.println(docs.totalHits + " found for query: " + query);
        //}


*/
    }

    public static String parseWithTika(){
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream inputstream = null;
        try {
            inputstream = new FileInputStream(new File(filePath));

        ParseContext pcontext = new ParseContext();

        //parsing the document using PDF parser
        PDFParser pdfparser = new PDFParser();
        pdfparser.parse(inputstream,  handler, metadata, pcontext);

        //getting the content of the document
        System.out.println("Contents of the PDF :" + handler.toString());

        //getting metadata of the document
        System.out.println("Metadata of the PDF:");
        String[] metadataNames = metadata.names();

        for(String name : metadataNames) {
            System.out.println(name+ " : " + metadata.get(name));
        }
            return handler.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void testDictionary() throws IOException {
        //String wnhome = System.getenv("WNHOME");
        String wnhome="c:\\Users\\Katherine\\Documents\\2nd sem\\IR\\project\\WordNet_3.1";
        String path = wnhome + File.separator + "dict";


        URL url = new URL("file", null, path);

        IDictionary dictionary = new Dictionary(url);
        dictionary.open();
        // /Users/Ward/Downloads
        IIndexWord indexWord = dictionary.getIndexWord("use", POS.VERB);

        for (IWordID wordID : indexWord.getWordIDs()) {

            IWord word = dictionary.getWord(wordID);
            for (IWord word1 : word.getSynset().getWords()) {
                System.out.println(word1.getLemma());
            }
//            System.out.println("Id = " + wordID);
//            System.out.println(" Lemma = " + word.getLemma());
//            System.out.println(" Gloss = " + word.getSynset().getGloss());

            List<ISynsetID> hypernyms =
                    word.getSynset().getRelatedSynsets(Pointer.HYPERNYM);
            // print out each h y p e r n y m s id and synonyms
            List<IWord> words;
            for (ISynsetID sid : hypernyms) {
                words = dictionary.getSynset(sid).getWords();
                System.out.print(sid + " {");
                for (Iterator<IWord> i = words.iterator(); i.hasNext(); ) {
                    System.out.print(i.next().getLemma());
                    if (i.hasNext())
                        System.out.print(", ");
                }
                System.out.println("}");
            }
        }
    }
}


