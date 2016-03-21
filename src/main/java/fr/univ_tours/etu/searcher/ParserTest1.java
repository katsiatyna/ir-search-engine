package fr.univ_tours.etu.searcher;

import fr.univ_tours.etu.pdf.*;
import org.apache.lucene.document.Document;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

/**
 * Created by Katherine on 10.03.2016.
 */
public class ParserTest1 {

    public static void main(String[] args) throws ParseException {
        PDFParser parser;
        PDFTextStripper pdfStripper;
        PDDocument pdDoc;
        COSDocument cosDoc;

        String Text;
        String filePath1 = "src/main/resources/test_docs/N13-1041.pdf";
        String filePath2 = "src/main/resources/test_docs/N13-1040.pdf";
        File file;

        pdfStripper = null;
        pdDoc = null;
        cosDoc = null;

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
            Document luceneDocument1 = LucenePDFDocument.getDocument(new File(filePath1));
            Document luceneDocument2 = LucenePDFDocument.getDocument(new File(filePath2));
//            String asd = ((TextField) luceneDocument.getField("contents")).readerValue().toString();
//            System.out.println();
            StandardAnalyzer analyzer = new StandardAnalyzer();
            Directory index = new RAMDirectory();

            IndexWriterConfig config = new IndexWriterConfig(analyzer);

            IndexWriter w = new IndexWriter(index, config);
            w.addDocument(luceneDocument1);
            w.addDocument(luceneDocument2);
            w.close();

            String querystr = "abstract";
            Query q = new QueryParser("contents", analyzer).parse(querystr);

            int hitsPerPage = 10;
            IndexReader reader = DirectoryReader.open(index);
            reader.getTermVector(0, "contents");
//            DisjunctionMaxQuery
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs docs = searcher.search(q, hitsPerPage);
            ScoreDoc[] hits = docs.scoreDocs;

            try (DirectoryReader r = DirectoryReader.open(index)) {
                Set<String> termStrings = new TreeSet<>();
                for (LeafReaderContext atomicReaderContext : r.leaves()) {
                    LeafReader atomicReader = atomicReaderContext.reader();
                    Terms terms = atomicReader.terms("contents");
                    TermsEnum iterator = atomicReader.terms("contents").iterator();
                    BytesRef next = iterator.next();
                    while (next != null) {
                        termStrings.add(iterator.term().utf8ToString());
                        next = iterator.next();
                    }
                }
            }
            
            
            
            
            int s = 123;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
