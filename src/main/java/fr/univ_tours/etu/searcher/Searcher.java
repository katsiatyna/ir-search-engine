/*
 *  This work is licensed for the ULB Group13 BKT for the DBSA course.
 */
package fr.univ_tours.etu.searcher;

import fr.univ_tours.etu.nlp.CoreNlpTokenizer;
import fr.univ_tours.etu.nlp.NlpNeTokenizer;
import fr.univ_tours.etu.nlp.SemicolonAnalyzer;
import fr.univ_tours.etu.search.SearchQueriesRequest;
import java.io.File;
import java.io.IOException;
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

    private final Properties props;
    private final IndexReader reader;
    private final PerFieldAnalyzerWrapper analyzer;
    private final int numRetrievedDocs;

    public Searcher() throws IOException {
        this.props = new Properties();
        this.props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");

        this.reader = DirectoryReader.open(FSDirectory.open(new File(DocFields.INDEX_PATH).toPath()));

        Map<String, Analyzer> analyzerPerField = new HashMap<>();
        analyzerPerField.put(DocFields.NAMED_ENTITIES, new SemicolonAnalyzer());
        this.analyzer = new PerFieldAnalyzerWrapper(
                new StandardAnalyzer(), analyzerPerField);
        this.numRetrievedDocs = Integer.MAX_VALUE;
    }

    public Searcher(int numRetrievedDocs) throws IOException {
        this.props = new Properties();
        this.props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");

        this.reader = DirectoryReader.open(FSDirectory.open(new File(DocFields.INDEX_PATH).toPath()));

        Map<String, Analyzer> analyzerPerField = new HashMap<>();
        analyzerPerField.put(DocFields.NAMED_ENTITIES, new SemicolonAnalyzer());
        this.analyzer = new PerFieldAnalyzerWrapper(
                new StandardAnalyzer(), analyzerPerField);
        this.numRetrievedDocs = numRetrievedDocs;
    }

    public List<ResultObject> search(SearchQueriesRequest query) throws IOException, ParseException {

        Map<String, String> queriesDictionary = query.getQueriesDictionary();
        boolean useQueryExpansion = query.isUseQueryExpansion();

        int isThereContents = 0;
        if (queriesDictionary.containsKey(DocFields.CONTENTS)) {
            isThereContents++;
        }

        String[] fs = new String[queriesDictionary.size() + isThereContents];
        String[] qs = new String[queriesDictionary.size() + isThereContents];

        int counter = 0;
        for (Map.Entry<String, String> entry : queriesDictionary.entrySet()) {
            fs[counter] = entry.getKey();
            qs[counter++] = entry.getValue();
        }

        if (queriesDictionary.containsKey(DocFields.CONTENTS)) {
            NlpNeTokenizer queryTokenizer = new CoreNlpTokenizer(props);
            queryTokenizer.tokenize(queriesDictionary.get(DocFields.CONTENTS));
            fs[counter] = DocFields.NAMED_ENTITIES;
            qs[counter] = queryTokenizer.getNeString(";", true);
        }

        Query q = MultiFieldQueryParser.parse(qs, fs, analyzer);

        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, 2);
        ScoreDoc[] hits = docs.scoreDocs;

        List<ResultObject> resultObjects = new ArrayList<>();

        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            resultObjects.add(new ResultObject(i,
                    d.get(DocFields.TITLE),
                    d.get(DocFields.AUTHOR),
                    d.get(DocFields.DOC_PATH),
                    d.get(DocFields.SUMMARY)));
        }
        return resultObjects;
    }

}
