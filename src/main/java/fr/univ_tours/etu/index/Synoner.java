package fr.univ_tours.etu.index;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.*;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import fr.univ_tours.etu.nlp.CoreNlpTokenizer;
import fr.univ_tours.etu.nlp.NlpNeTokenizer;
import fr.univ_tours.etu.nlp.SemicolonAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.QueryBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Synoner {

	
	private String content;
	private String synContent;
	
	public Synoner(String content)
	{
		this.content=content;
		this.synContent="";
	}
	
	
	
	
	public void Tokenize() throws IOException
	{
		
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
		String wnhome="/Users/Ward/Downloads";
        String path = wnhome + File.separator + "dict";
        
        URL url = new URL("file", null, path);
        IDictionary dictionary = new Dictionary(url);
        dictionary.open();

        if(token!=null && token.length()>1 )
        try
        {
        IIndexWord indexWord = dictionary.getIndexWord(token, p);
        
        if(indexWord!=null)
        {
        IWordID wordID = indexWord.getWordIDs().get(0);
        IWord word = dictionary.getWord(wordID);
        
        List<ISynsetID> hypernyms =
                word.getSynset().getRelatedSynsets(Pointer.HYPERNYM);
        // print out each h y p e r n y m s id and synonyms
        List<IWord> words;
        for (ISynsetID sid : hypernyms) {
            words = dictionary.getSynset(sid).getWords();
            //System.out.print(sid + " {");
            for (Iterator<IWord> i = words.iterator(); i.hasNext(); ) {
                String tmp=i.next().getLemma()+" ";
            	//System.out.print(tmp);
            	
                this.synContent+=tmp;
//               if (i.hasNext())
//                    System.out.print(", ");
            }
            //System.out.println("}");
        }
        }
        else
        {
        	//System.out.println("{   }");
        }
        }catch (Exception e){ System.out.println("E R R O R :" + token);}
        dictionary.close();
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

		
		
		Synoner sn= new Synoner(str);
		sn.Tokenize();
		//System.out.println(sn.getSynText());
		
	}
	
}
