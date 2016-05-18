package fr.univ_tours.etu.nlp;

import java.util.List;
import java.util.Map;

/**
 * Created by Katherine on 19.03.2016.
 */
public interface NlpNeTokenizer {
    public void tokenize(String text, boolean isQuery);

    public List<String> getNeList();

    public List<String> getLemmaList();

    public String getNeString(String delimiter, boolean useQuotes);

    public String getLemmaString();

    public Map<String, List<String>> getLemmaPosMap();
}
