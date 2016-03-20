package fr.univ_tours.etu.nlp;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.miscellaneous.TrimFilter;
import org.apache.lucene.analysis.util.CharTokenizer;

import java.io.Reader;

public class SemicolonAnalyzer extends Analyzer {

    /* This is the only function that we need to override for our analyzer.
     * It takes in a java.io.Reader object and saves the tokenizer and list
     * of token filters that operate on it.
     */
    @Override
    protected TokenStreamComponents createComponents(String field) {
        Tokenizer tokenizer = new SemicolonTokenizer();
        TokenStream filter = new LowerCaseFilter(tokenizer);
        return new TokenStreamComponents(tokenizer, filter);
    }


}
