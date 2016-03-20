package fr.univ_tours.etu.nlp;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by Katherine on 19.03.2016.
 */
public class SemicolonTokenizer extends CharTokenizer {


    @Override
    protected boolean isTokenChar(int i) {
        return (i != ';');
    }
}
