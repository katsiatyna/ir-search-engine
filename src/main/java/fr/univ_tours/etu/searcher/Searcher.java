/*
 *  This work is licensed for the ULB Group13 BKT for the DBSA course.
 */
package fr.univ_tours.etu.searcher;

import fr.univ_tours.etu.nlp.CoreNlpTokenizer;
import fr.univ_tours.etu.nlp.NlpNeTokenizer;
import fr.univ_tours.etu.nlp.SemicolonAnalyzer;
import fr.univ_tours.etu.pdf.DocFields;
import fr.univ_tours.etu.search.SearchQueriesRequest;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author Anas Alba
 */
public class Searcher {

    private static final Properties propsCaseless;
    private static final Properties propsRegular;
    private IndexReader reader;
    private final PerFieldAnalyzerWrapper analyzer;
    private final int numRetrievedDocs;
    static NlpNeTokenizer caselessTokenizer;
    static NlpNeTokenizer regularTokenizer;
    private String indexDir;
    static {
        propsRegular = new Properties();
        propsRegular.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
        regularTokenizer = new CoreNlpTokenizer(propsRegular);
        propsCaseless = new Properties();
        propsCaseless.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
        propsCaseless.put("pos.model", "edu/stanford/nlp/models/pos-tagger/english-caseless-left3words-distsim.tagger");
        propsCaseless.put("parse.model", "edu/stanford/nlp/models/lexparser/englishPCFG.caseless.ser.gz");
        propsCaseless.put("ner.model", "edu/stanford/nlp/models/ner/english.muc.7class.caseless.distsim.crf.ser.gz");
        caselessTokenizer = new CoreNlpTokenizer(propsCaseless);

    }

    public Searcher() throws IOException {
        this.indexDir = DocFields.INDEX_DIR;
        this.reader = DirectoryReader.open(FSDirectory.open(new File(this.indexDir).toPath()));
        Map<String, Analyzer> analyzerPerField = new HashMap<>();
        analyzerPerField.put(DocFields.NAMED_ENTITIES, new SemicolonAnalyzer());
        this.analyzer = new PerFieldAnalyzerWrapper(
                new StandardAnalyzer(), analyzerPerField);
        this.numRetrievedDocs = Integer.MAX_VALUE;

    }

    public Searcher(int numRetrievedDocs) throws IOException {

        this.indexDir = DocFields.INDEX_DIR;
        this.reader = DirectoryReader.open(FSDirectory.open(new File(this.indexDir).toPath()));
        Map<String, Analyzer> analyzerPerField = new HashMap<>();
        analyzerPerField.put(DocFields.NAMED_ENTITIES, new SemicolonAnalyzer());
        this.analyzer = new PerFieldAnalyzerWrapper(
                new StandardAnalyzer(), analyzerPerField);
        this.numRetrievedDocs = numRetrievedDocs;

    }

    public Searcher(int numRetrievedDocs, String indexDir) throws IOException {
        this.indexDir = indexDir;
        this.reader = DirectoryReader.open(FSDirectory.open(new File(this.indexDir).toPath()));
        Map<String, Analyzer> analyzerPerField = new HashMap<>();
        analyzerPerField.put(DocFields.NAMED_ENTITIES, new SemicolonAnalyzer());
        this.analyzer = new PerFieldAnalyzerWrapper(
                new StandardAnalyzer(), analyzerPerField);
        this.numRetrievedDocs = numRetrievedDocs;

    }

    public List<ResultObject> search(SearchQueriesRequest query) throws IOException, ParseException {

        Map<String, String> queriesDictionary = query.getQueriesDictionary();
        boolean useQueryExpansion = query.isUseQueryExpansion();
        List<Integer> docsToExpand = (useQueryExpansion) ? new ArrayList<>() : null;

        List<String> fsa = new ArrayList<>();
        List<String> qsa = new ArrayList<>();
        String contentLemmas = "";
        if (queriesDictionary.containsKey(DocFields.CONTENTS)) {
            regularTokenizer.tokenize(queriesDictionary.get(DocFields.CONTENTS), true);
            caselessTokenizer.tokenize(queriesDictionary.get(DocFields.CONTENTS), true);
            contentLemmas = caselessTokenizer.getLemmaString();
            System.out.println("Lemmas: " + caselessTokenizer.getLemmaList());
            String neString = "";
            if (caselessTokenizer.getNeList() != null && caselessTokenizer.getNeList().size() != 0) {
                neString = caselessTokenizer.getNeString(";", true);
                System.out.println("NE caseless: " + neString);
            }
            if (regularTokenizer.getNeList() != null && regularTokenizer.getNeList().size() != 0) {
                neString += ";" + regularTokenizer.getNeString(";", true);
                System.out.println("NE all: " + neString);
            }
            if(!"".equals(neString)){
                fsa.add(DocFields.NAMED_ENTITIES);
                qsa.add(neString);
            }

        }

        for (Map.Entry<String, String> entry : queriesDictionary.entrySet()) {
            fsa.add(entry.getKey());
            if(entry.getKey().equals(DocFields.CONTENTS)
                    || entry.getKey().equals(DocFields.SYNONYMS)){
                qsa.add(contentLemmas);
            }else {
                qsa.add(entry.getValue());
            }
        }

        Query q = MultiFieldQueryParser.parse(qsa.toArray(new String[qsa.size()]), fsa.toArray(new String[fsa.size()]), analyzer);

        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, this.numRetrievedDocs);
        ScoreDoc[] hits = docs.scoreDocs;

        List<ResultObject> resultObjects = new ArrayList<>();

        String result = "";
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            if (useQueryExpansion) {
                docsToExpand.add(docId);
            }
            Document d = searcher.doc(docId);
            resultObjects.add(new ResultObject(docId, i,
                    d.get(DocFields.TITLE),
                    d.get(DocFields.AUTHOR),
                    d.get(DocFields.FILE_PATH),
                    d.get(DocFields.SUMMARY),
                    d.get(DocFields.FILE_NAME)));
            result = d.get(DocFields.SUMMARY);
        }

        if (useQueryExpansion) {
            reader.close();

            this.reader = DirectoryReader.open(FSDirectory.open(new File(this.indexDir).toPath()));
            searcher = new IndexSearcher(reader);
            MoreLikeThis mlt = new MoreLikeThis(reader);
            mlt.setMinTermFreq(0);
            mlt.setMinDocFreq(0);
            mlt.setAnalyzer(analyzer);
            for (int i = 0; i < Math.min(docsToExpand.size(), 5); i++) {

                Reader r = new StringReader(resultObjects.get(i).getSummary());
                Query expandedQuery = mlt.like(DocFields.CONTENTS, r);

                TopDocs topDocs = searcher.search(expandedQuery, 5);

                for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                    if (!docsToExpand.contains(scoreDoc.doc)) {
                        docsToExpand.add(scoreDoc.doc);
                        Document aSimilar = searcher.doc(scoreDoc.doc);

                        resultObjects.add(new ResultObject(1, resultObjects.size(),
                                aSimilar.get(DocFields.TITLE),
                                aSimilar.get(DocFields.AUTHOR),
                                aSimilar.get(DocFields.FILE_PATH),
                                aSimilar.get(DocFields.SUMMARY),
                                aSimilar.get(DocFields.FILE_NAME)));
                    } else {
                    }

                }
            }
        }

        return resultObjects;
    }

}
