package fr.univ_tours.etu.index;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class ContentsSynoner implements Synoner{

    protected IDictionary dictionary;
    protected String content;
	protected String synContent;
	
	public ContentsSynoner(String content) throws MalformedURLException {
		this.content=content;
		this.synContent="";
		String wnhome="c:\\Users\\Katherine\\Documents\\2nd sem\\IR\\project\\WordNet_3.1";
		String path = wnhome + File.separator + "dict";

		URL url = new URL("file", null, path);
		dictionary = new Dictionary(url);
	}
	
	
	
	
	public void tokenize() throws IOException
	{
        dictionary.open();
		StringTokenizer st = new StringTokenizer(this.content);
		String tkn="";
		while(st.hasMoreTokens())
		{
			String term= modified(st.nextToken());
			//call the function to get all synonyms for this term
			//System.out.print(term+ " | ");
			POS[] parray={POS.ADJECTIVE,POS.ADVERB,POS.NOUN,POS.VERB};
			for(POS p: parray)
			{
				//System.out.println(p.toString());
				getSynonyms(term,p);
			}
			//tkn+= " ";
		}
		//System.out.println(tkn);
        dictionary.close();
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
        }	}
	
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
