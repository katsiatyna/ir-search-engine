package fr.univ_tours.etu.index;


import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fr.univ_tours.etu.nlp.SemicolonAnalyzer;
import fr.univ_tours.etu.pdf.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;
import org.apache.lucene.index.IndexWriterConfig;

public class Indexer {

   private IndexWriter writer;
   //private IndexWriter synWriter;

   /**
    * Gets the path to the PDF files directory
    * @param indexDirectoryPath a path for the directory containing pdf files
    * @throws IOException
    */
   public Indexer(String indexDirectoryPath) throws IOException{
      //this directory will contain the indexes
      Directory indexDirectory = FSDirectory.open((new File(indexDirectoryPath)).toPath());
    		  //new RAMDirectory();

         //FSDirectory.open((new File(indexDirectoryPath)).toPath());
      
      Map<String, Analyzer> analyzerPerField = new HashMap<String, Analyzer>();
      analyzerPerField.put(DocFields.NAMED_ENTITIES, new SemicolonAnalyzer());
      analyzerPerField.put(DocFields.KEYWORDS, new KeywordAnalyzer());
      analyzerPerField.put(DocFields.CREATOR, new WhitespaceAnalyzer());
      
      PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(
              new StandardAnalyzer(), analyzerPerField);
 
      IndexWriterConfig config = new IndexWriterConfig(analyzer)
              .setOpenMode(IndexWriterConfig.OpenMode.CREATE);
       writer = new IndexWriter(indexDirectory, config);
      
       Directory synDirectory = FSDirectory.open((new File(DocFields.SYN_DIR)).toPath());
       WhitespaceAnalyzer synAnalyzer = new WhitespaceAnalyzer();
       //synWriter = new IndexWriter(synDirectory, new IndexWriterConfig(synAnalyzer));
 
   }
   
   public File getIndexDirectory()
   {
	   return new File(DocFields.INDEX_DIR);
   }
   
   public File getSynonymsDirectory()
   {
	   return new File(DocFields.SYN_DIR);
   }
   
   public File getDataDirectory()
   {
	   return new File(DocFields.DATA_DIR);
   }

   public void close() throws CorruptIndexException, IOException{
      writer.close();
   }


   private void indexFile(File file, int i) throws IOException{
      System.out.println(i + ". Indexing "+file.getCanonicalPath());
//      TikaParser tp= new TikaParser(file);
//      Document document = tp.getDocument();
      LucenePDFDocument lpd= new LucenePDFDocument();
      
      Document document= lpd.convertDocument(file);
      writer.addDocument(document);
      //this.synWriter.addDocument(lpd.getSynDoc());
      
   }
   


   /**
    * Create full index for all documents available to be indexed and returns the number of total indexed
    * documents or PDF files
    * @param dataDirPath the location where the files are
    * @return The number of indexed files 
    * @throws IOException
    * @throws TikaException
    * @throws SAXException
    */
   public int createIndex(String dataDirPath, int numDocs)
      throws IOException{
      //get all files in the data directory
      File[] files = new File(dataDirPath).listFiles();
       int i = 0;
      while(i < numDocs) {
          File file = files[i];
         if(!file.isDirectory()
            && !file.isHidden()
            && file.exists()
            && file.canRead()){
        	// System.out.println("NOW FILE: " + file.getName());
            indexFile(file, i);
            //indexSynonyms(file);
             i++;
         }
      }
      return writer.numDocs();
   }
   
   
   public static void main(String[] args) {
	   
	   String indexDir = "src/main/resources/indexdir";
	   //String dataDir = "src/main/resources/test_docs";
       String dataDir = "c:\\Users\\Katherine\\Documents\\2nd sem\\IR\\project\\IR CORPUS DOCS\\";
	   try {
          int numDocs = 100;
		Indexer indexer =new Indexer(indexDir);
		int numIndexed;
	      long startTime = System.currentTimeMillis();	
	      numIndexed = indexer.createIndex(dataDir, numDocs);
	      long endTime = System.currentTimeMillis();
	      indexer.close();
	      System.out.println(numIndexed+" File indexed, time taken: "
	         +(endTime-startTime)+" ms");
	      
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
	   
	   
	   
   }
   
}