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
      
      
//      StandardAnalyzer analyzer = new StandardAnalyzer();
//     writer = new IndexWriter(indexDirectory, new IndexWriterConfig(analyzer));
 
   }
   
   public File getIndexDirectory()
   {
	   return new File(DocFields.INDEX_DIR);
   }
   
   public File getDataDirectory()
   {
	   return new File(DocFields.DATA_DIR);
   }

   public void close() throws CorruptIndexException, IOException{
      writer.close();
   }


   private void indexFile(File file) throws IOException{
      System.out.println("Indexing "+file.getCanonicalPath());
//      TikaParser tp= new TikaParser(file);
//      Document document = tp.getDocument();
      LucenePDFDocument lpd= new LucenePDFDocument();
      
      Document document= lpd.convertDocument(file);
      writer.addDocument(document);
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
   public int createIndex(String dataDirPath) 
      throws IOException{
      //get all files in the data directory
      File[] files = new File(dataDirPath).listFiles();

      for (File file : files) {
         if(!file.isDirectory()
            && !file.isHidden()
            && file.exists()
            && file.canRead()){
        	// System.out.println("NOW FILE: " + file.getName());
            indexFile(file);
         }
      }
      return writer.numDocs();
   }
   
   
   public static void main(String[] args) {
	   
	   String indexDir = "src/main/resources/indexdir";
	   String dataDir = "src/main/resources/test_docs";
	   try {
		Indexer indexer =new Indexer(indexDir);
		int numIndexed;
	      long startTime = System.currentTimeMillis();	
	      numIndexed = indexer.createIndex(dataDir);
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