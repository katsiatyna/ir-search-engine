package fr.univ_tours.etu.pdf;


import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
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
      StandardAnalyzer analyzer = new StandardAnalyzer();
      writer = new IndexWriter(indexDirectory, new IndexWriterConfig(analyzer));
 
   }

   public void close() throws CorruptIndexException, IOException{
      writer.close();
   }


   private void indexFile(File file) throws IOException, TikaException, SAXException{
      System.out.println("Indexing "+file.getCanonicalPath());
      TikaParser tp= new TikaParser(file);
      Document document = tp.getDocument();
      writer.addDocument(document);
   }

   public int createIndex(String dataDirPath) 
      throws IOException, TikaException, SAXException{
      //get all files in the data directory
      File[] files = new File(dataDirPath).listFiles();

      for (File file : files) {
         if(!file.isDirectory()
            && !file.isHidden()
            && file.exists()
            && file.canRead()){
            indexFile(file);
         }
      }
      return writer.numDocs();
   }
   
   
   public static void main(String[] args) throws TikaException, SAXException {
	   
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