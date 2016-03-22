package fr.univ_tours.etu.nlp;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Katherine on 19.03.2016.
 */
public class CoreNlpTokenizer implements NlpNeTokenizer {

    protected StanfordCoreNLP pipeline;
    protected Annotation document;
    protected List<String> neList;
    protected List<String> lemmaList;

    public CoreNlpTokenizer(Properties props) {
        pipeline = new StanfordCoreNLP(props);
    }

    @Override
    public void tokenize(String text) {
        document = new Annotation(text);
        // run all Annotators on this text
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        StringBuilder sb = new StringBuilder();
        List<String> tokens = new ArrayList<>();
        lemmaList = new ArrayList<>();
        neList = new ArrayList<>();
        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            // traversing the words in the current sentence, "O" is a sensible default to initialise
            // tokens to since we're not interested in unclassified / unknown things..
            String prevNeToken = "O";
            String currNeToken = "O";
            boolean newToken = true;
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                //System.out.println("Word: " + word);
                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
               // System.out.println("Lemma: " + lemma);
                lemmaList.add(lemma);
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                // System.out.println("Lemma: " + lemma);
                // this is the NER label of the token
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
//                System.out.println("NE: " + ne);
//                System.out.println();
                currNeToken = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                // Strip out "O"s completely, makes code below easier to understand
                if (currNeToken.equals("O")) {
                    // LOG.debug("Skipping '{}' classified as {}", word, currNeToken);
                    if (!prevNeToken.equals("O") && (sb.length() > 0)) {
                        handleEntity(prevNeToken, sb, tokens);
                        newToken = true;
                    }
                    continue;
                }

                if (newToken) {
                    prevNeToken = currNeToken;
                    newToken = false;
                    sb.append(word);
                    continue;
                }

                if (currNeToken.equals(prevNeToken)) {
                    sb.append(" " + word);
                } else {
                    // We're done with the current entity - print it out and reset
                    // TODO save this token into an appropriate ADT to return for useful processing..
                    handleEntity(prevNeToken, sb, tokens);
                    newToken = true;
                }
                prevNeToken = currNeToken;
            }
            if (!prevNeToken.equals("O") && (sb.length() > 0)) {
                handleEntity(prevNeToken, sb, tokens);
            }
        }
    }

    private void handleEntity(String inKey, StringBuilder inSb, List inTokens) {
        inTokens.add(new EmbeddedToken(inKey, inSb.toString()));
        if(!inKey.equals("NUMBER")) {
            neList.add(inSb.toString());
        }
        inSb.setLength(0);
    }

    static class EmbeddedToken {

        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public EmbeddedToken(String name, String value) {
            super();
            this.name = name;
            this.value = value;
        }
    }

    @Override
    public List<String> getNeList() {
        return neList;
    }

    @Override
    public List<String> getLemmaList() {
        return lemmaList;
    }

    @Override
    public String getNeString(String delimiter, boolean useQuotes) {
        StringBuilder sb = new StringBuilder();
        for (String ne: neList){
            if(useQuotes){
                sb.append("\"");
            }
            sb.append(ne);
            if(useQuotes){
                sb.append("\"");
            }
            sb.append(delimiter);
        }
        String neString = sb.toString();
        return (neString.length() > 1) ? neString.substring(0, neString.length() - 1) : null;
    }

    @Override
    public String getLemmaString() {
        StringBuilder sb = new StringBuilder();
        for (String lemma: lemmaList){
            sb.append(lemma);
            sb.append(" ");
        }
        String lemmaString = sb.toString();
        return lemmaString.substring(0, lemmaString.length() - 1);
    }
}
