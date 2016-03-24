package fr.univ_tours.etu.index;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by Katherine on 24.03.2016.
 */
public class NlpSynoner implements Synoner {

    protected IDictionary dictionary;
    protected String synContent;
    protected Map<String, List<POS>> lemmaPosMap;
    public static Map<String, List<POS>> posMap;
    protected List<String> lemmas;

    static {
        posMap = new HashMap<>();
        posMap.put("CD", new ArrayList<>());
        posMap.get("CD").add(POS.NOUN); //numeral, cardinal

        posMap.put("DT", new ArrayList<>());
        posMap.get("DT").add(POS.NOUN);
        posMap.get("DT").add(POS.ADJECTIVE);
        posMap.get("DT").add(POS.ADVERB);

        posMap.put("EX", new ArrayList<>());
        posMap.get("EX").add(POS.NOUN);
        posMap.get("EX").add(POS.ADVERB);

        posMap.put("FW", new ArrayList<>());
        posMap.get("FW").add(POS.NOUN);
        posMap.get("FW").add(POS.ADJECTIVE);
        posMap.get("FW").add(POS.ADVERB);
        posMap.get("FW").add(POS.VERB);

        posMap.put("JJ", new ArrayList<>());
        posMap.get("JJ").add(POS.ADJECTIVE);

        posMap.put("JJR", new ArrayList<>());
        posMap.get("JJR").add(POS.ADJECTIVE);

        posMap.put("JJS", new ArrayList<>());
        posMap.get("JJS").add(POS.ADJECTIVE);

        posMap.put("MD", new ArrayList<>());
        posMap.get("MD").add(POS.VERB);

        posMap.put("NN", new ArrayList<>());
        posMap.get("NN").add(POS.NOUN);

        posMap.put("NNS", new ArrayList<>());
        posMap.get("NNS").add(POS.NOUN);

        posMap.put("NNP", new ArrayList<>());
        posMap.get("NNP").add(POS.NOUN);

        posMap.put("NNPS", new ArrayList<>());
        posMap.get("NNPS").add(POS.NOUN);

        posMap.put("PDT", new ArrayList<>());
        posMap.get("PDT").add(POS.NOUN);
        posMap.get("PDT").add(POS.ADVERB);
        posMap.get("PDT").add(POS.ADJECTIVE);

        posMap.put("RB", new ArrayList<>());
        posMap.get("RB").add(POS.ADVERB);

        posMap.put("RBR", new ArrayList<>());
        posMap.get("RBR").add(POS.ADVERB);
        posMap.get("RBR").add(POS.ADJECTIVE);

        posMap.put("RBS", new ArrayList<>());
        posMap.get("RBS").add(POS.ADVERB);
        posMap.get("RBS").add(POS.ADJECTIVE);

        posMap.put("RP", new ArrayList<>());
        posMap.get("RP").add(POS.ADVERB);

        posMap.put("UH", new ArrayList<>());
        posMap.get("UH").add(POS.NOUN);

        posMap.put("VB", new ArrayList<>());
        posMap.get("VB").add(POS.VERB);

        posMap.put("VBD", new ArrayList<>());
        posMap.get("VBD").add(POS.VERB);

        posMap.put("VBG", new ArrayList<>());
        posMap.get("VBG").add(POS.VERB);

        posMap.put("VBN", new ArrayList<>());
        posMap.get("VBN").add(POS.VERB);

        posMap.put("VBP", new ArrayList<>());
        posMap.get("VBP").add(POS.VERB);

        posMap.put("VBZ", new ArrayList<>());
        posMap.get("VBZ").add(POS.VERB);

        posMap.put("WRB", new ArrayList<>());
        posMap.get("WRB").add(POS.ADVERB);
    }

    public NlpSynoner(Map<String, List<String>> lemmaPosStringMap, List<String> lemmas) throws MalformedURLException {
        fillPosMap(lemmaPosStringMap);
        this.synContent="";
        String wnhome="c:\\Users\\Katherine\\Documents\\2nd sem\\IR\\project\\WordNet_3.1";
        String path = wnhome + File.separator + "dict";
        this.lemmas = lemmas;
        URL url = new URL("file", null, path);
        dictionary = new Dictionary(url);

    }

    private void fillPosMap(Map<String, List<String>> lemmaPosStringMap) {
        lemmaPosMap = new HashMap<>();
        for(String lemma: lemmaPosStringMap.keySet()){
            lemmaPosMap.put(lemma, new ArrayList<>());
            for(String posString: lemmaPosStringMap.get(lemma)){
                List<POS> posList = posMap.get(posString);
                if(posList != null) {
                    for (POS pos : posList) {
                        if (!lemmaPosMap.get(lemma).contains(pos)) {
                            lemmaPosMap.get(lemma).add(pos);
                        }
                    }
                }
            }
        }
    }


    public void tokenize() throws IOException
    {
        dictionary.open();
        for(String lemma: lemmas)
        {
            //call the function to get all synonyms for this term
            //System.out.print(term+ " | ");
            for(POS p: lemmaPosMap.get(lemma))
            {
                //System.out.println(p.toString());
                getSynonyms(lemma,p);
            }
            //tkn+= " ";
        }
        //System.out.println(tkn);
        dictionary.close();
        System.out.println(synContent);
    }

    private String modified(final String input){
        final StringBuilder builder = new StringBuilder();
        for(final char c : input.toCharArray())
            if(Character.isLetterOrDigit(c) || c==' ')
                builder.append(Character.isLowerCase(c) ? c : Character.toLowerCase(c));
        return builder.toString();
    }

    private void getSynonyms(String token,POS p) throws IOException
    {
        List<String> synonyms = new ArrayList<>();
        if(token!=null && token.length()>1 )
            try
            {
                IIndexWord indexWord = dictionary.getIndexWord(token, p);
                if(indexWord!=null)
                {
                    for(IWordID wordID :indexWord.getWordIDs()) {
                        IWord word = dictionary.getWord(wordID);
                        String wordString = word.getLemma();
                        for (IWord synonym : word.getSynset().getWords()) {
                            String synonymString = synonym.getLemma();
                            if(!synonyms.contains(synonymString) && !synonymString.equals(wordString)) {
                                synonyms.add(synonym.getLemma());
                            }
                        }
                        List<ISynsetID> hypernyms =
                                word.getSynset().getRelatedSynsets(Pointer.HYPERNYM);
                        // print out each h y p e r n y m s id and synonyms
                        List<IWord> words;
                        for (ISynsetID sid : hypernyms) {
                            words = dictionary.getSynset(sid).getWords();
                            //System.out.print(sid + " {");
                            for (IWord word1 : words) {
                                String synonymString = word1.getLemma();
                                if(!synonyms.contains(synonymString)  && !synonymString.equals(wordString)){
                                    synonyms.add(word1.getLemma());
                                }

//               if (i.hasNext())
//                    System.out.print(", ");
                            }
                            //System.out.println("}");
                        }
                    }
                }
                else
                {
                    //System.out.println("{   }");
                }
            }catch (Exception e){
                System.out.println("E R R O R :" + token);
            }
        for(String synonym: synonyms){
            String tmp = synonym + " ";
            //System.out.print(tmp);

            this.synContent += tmp;
        }
    }

    public String getSynText()
    {
        return this.synContent;
    }


    public static void main(String[] args) throws IOException
    {
//		String str="School of Information Systems, Singapore"
//				+ " Management University, Singapore School of Software and Microelectronics, Peking University, China"
//				+"{minghui.qiu.2010,jingjiang}@smu.edu.sg, yang.liu@pku.edu.cn";

        String str="Errata In the Squib by Yves Bestgen (“Improving Text Segmentation Using Latent Semantic"
                + "Analysis: A Reanalysis of Choi, Wiemer-Hastings, and Moore” (2001) in Issue 32:1"
                + "[March 2006]), the author refers to two papers by Choi et al. as appearing in NAACL"
                + "2001. However, only one is listed. The Choi, Wiemer-Hastings, and Moore (2001)"
                + "citation should be as follows:"
                + "Choi, Freddy Y. Y., Peter Wiemer-Hastings, and Johanna Moore. 2001. Latent semantic analysis"
                + "for text segmentation. In Proceedings of the Conference on Empirical Methods in Natural Language"
                + "Processing, Carnegie Mellon University, Pittsburgh, PA, pages 109–117."
                + "© 2006 Association for Computational Linguistics";



        ContentsSynoner sn= new ContentsSynoner(str);
        sn.tokenize();
        //System.out.println(sn.getSynText());

    }

}
