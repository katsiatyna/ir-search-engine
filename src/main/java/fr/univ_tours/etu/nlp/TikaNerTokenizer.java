package fr.univ_tours.etu.nlp;

import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ner.NamedEntityParser;

import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by Katherine on 17.05.2016.
 */
public class TikaNerTokenizer implements NlpNeTokenizer {
    private Tika tika;
    private List<String> neList = new ArrayList<>();
    public static final String CONFIG_FILE = "tika-config.xml";
    private CoreNLPNERTika recognizer;

    public TikaNerTokenizer(Properties props){
        System.setProperty(NamedEntityParser.SYS_PROP_NER_IMPL,
                CoreNLPNERTika.class.getName());
        //System.getProperty(MODEL_PROP_NAME, DEFAULT_MODEL_PATH);
        try {
            //test config is added to resources directory
            //InputStreamReader reader = new FileReader(new File(CONFIG_FILE));
            TikaConfig config = new TikaConfig(getClass().getClassLoader()
                    .getResourceAsStream(CONFIG_FILE));
            tika = new Tika(config);
            recognizer = new CoreNLPNERTika();
        } catch (TikaException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void tokenize(String text, boolean isQuery) {
        Metadata md = new Metadata();
        //tika.parse(new ByteArrayInputStream(text.getBytes()), md);
        Map<String, List<String>> recognized = recognizer.recogniseAll(text);

        neList.clear();
        if(recognized.containsKey("PERSON")) {
            neList.addAll(recognized.get("PERSON"));
        }
        if(recognized.containsKey("LOCATION")) {
            neList.addAll(recognized.get("LOCATION"));
        }
        if(recognized.containsKey("ORGANIZATION")) {
            neList.addAll(recognized.get("ORGANIZATION"));
        }
        if(recognized.containsKey("DATE")) {
            neList.addAll(recognized.get("DATE"));
        }
        if(recognized.containsKey("TIME")) {
            neList.addAll(recognized.get("TIME"));
        }
        if(recognized.containsKey("PERCENT")) {
            neList.addAll(recognized.get("PERCENT"));
        }
        if(recognized.containsKey("MONEY")) {
            neList.addAll(recognized.get("MONEY"));
        }
        if(recognized.containsKey("MISCELLANEOUS")) {
            neList.addAll(recognized.get("MISCELLANEOUS"));
        }

    }

    @Override
    public List<String> getNeList() {
        return neList;
    }

    @Override
    public List<String> getLemmaList() {
        return null;
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
        return null;
    }

    @Override
    public Map<String, List<String>> getLemmaPosMap() {
        return null;
    }
}
