package fr.univ_tours.etu.searcher;

import fr.univ_tours.etu.pdf.*;
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
import fr.univ_tours.etu.pdf.DocFields;
import fr.univ_tours.etu.search.SearchQueriesRequest;
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
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by Katherine on 10.03.2016.
 */
public class ParserTest {

    public static void main(String[] args) {
        PDFParser parser;
        PDFTextStripper pdfStripper;
        PDDocument pdDoc;
        COSDocument cosDoc;

        String text;
        String filePath = "src/main/resources/test_docs/N13-1041.pdf";
        File file;

        pdfStripper = null;
        pdDoc = null;
        cosDoc = null;

        //file = new File(filePath);

        String mainQuery = "Association for Computational Linguistics";
        String titleQuery = null;
        String authorsQuery = null;
        String keywordsQuery = null;
        SearchQueriesRequest searchQueriesRequest = new SearchQueriesRequest();
        searchQueriesRequest.setUseQueryExpansion(true);

        if(mainQuery != null && !"".equals(mainQuery)){
            searchQueriesRequest.getQueriesDictionary().put(DocFields.CONTENTS, mainQuery);
        }
        if(titleQuery != null && !"".equals(titleQuery)){
            searchQueriesRequest.getQueriesDictionary().put(DocFields.TITLE, titleQuery);
        }
        if(authorsQuery != null && !"".equals(authorsQuery)){
            searchQueriesRequest.getQueriesDictionary().put(DocFields.AUTHOR, authorsQuery);
        }
        if(keywordsQuery != null && !"".equals(keywordsQuery)){
            searchQueriesRequest.getQueriesDictionary().put(DocFields.KEYWORDS, keywordsQuery);
        }

        List<ResultObject> results = new ArrayList<>();
        try{
            Searcher searcher = new Searcher();
            results = searcher.search(searchQueriesRequest);

        } catch (IOException e){
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
            /*parser = new PDFParser(new FileInputStream(file)); // update for PDFBox V 2.0
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            pdDoc.getNumberOfPages();
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(10);
            text = pdfStripper.getText(pdDoc);
            //testDictionary();
            testOpenNlp(text, file);
*/
        System.out.println();
    }

    public static void testOpenNlp(String text, File file) throws IOException, ParseException {

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
        NlpNeTokenizer nlpNeTokenizer = new CoreNlpTokenizer(props);
        nlpNeTokenizer.tokenize(text);
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
        PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(
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
        ScoreDoc[] hits = docs.scoreDocs;

        // 4. display results
        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("Title") + "\t" + d.get("Author") + "\n" + d.get("summary"));
            System.out.println(docs.totalHits + " found for query: " + query);
            //}

        }

    }
}
