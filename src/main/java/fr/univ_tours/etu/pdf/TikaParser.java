package fr.univ_tours.etu.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.FileFilter;
import java.io.FileReader;

//import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.util.Constants;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;

import org.xml.sax.SAXException;

public class TikaParser {
	
	private File toParse;
	private BodyContentHandler handler;
	private FileInputStream inputstream;
	private ParseContext pcontext;
	private PDFParser pdfparser;
	private Metadata metadata;
	


	
	private void parseFile() throws IOException,TikaException, SAXException 
	{
		this.handler=new BodyContentHandler();
		this.inputstream = new FileInputStream(this.toParse);
		this.pcontext = new ParseContext();
		this.pdfparser = new PDFParser(); 
		this.metadata = new Metadata();
		pdfparser.parse(this.inputstream, this.handler, this.metadata,this.pcontext);
	
	}
	
	public BodyContentHandler getHandler() {
		return handler;
	}

	public ParseContext getPcontext() {
		return pcontext;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public TikaParser(File toParse) throws IOException, TikaException, SAXException  {
		this.toParse = toParse;
		this.parseFile();	
	}
	
	public boolean isKeywords()
	{
		if (this.metadata.get("meta:keyword").length() > 0)
			return true;
		
		return false;
	}
	
	public Document getDocument() throws IOException{
		   Document document = new Document();

		   //index file contents
		   Field contentField = new Field(DocFields.CONTENTS, this.getHandler().toString(),Field.Store.NO,Field.Index.ANALYZED);
		   
		   //index title
		   Field titleField = new Field(DocFields.TITLE,this.getMetadata().get("dc:title"),Field.Store.YES,Field.Index.NOT_ANALYZED);
		   
		   
		   //index file path
		   Field filePathField = new Field(DocFields.FILE_PATH,this.toParse.getCanonicalPath(),Field.Store.YES,Field.Index.NOT_ANALYZED);

		   //index author
		   Field authorField= new Field(DocFields.AUTHOR,this.getMetadata().get("Author"),Field.Store.YES,Field.Index.NOT_ANALYZED);
		   
		 //index keywords
		   Field keywordsField= new Field(DocFields.KEYWORDS,this.getMetadata().get("meta:keyword"),Field.Store.NO,Field.Index.NOT_ANALYZED);
		   
		   document.add(contentField);
		   document.add(titleField);
		   document.add(filePathField);
		   document.add(authorField);
		   document.add(keywordsField);
		   

		   return document;
		} 
	
	
	

   public static void main(final String[] args) throws IOException,TikaException, SAXException {
	   
	   TikaParser tp= new TikaParser(new File("src/main/resources/test_docs/N13-1041.pdf"));
	   
		 //getting the content of the document
	     System.out.println("Contents of the PDF :" + tp.getHandler().toString());
	     
	     //getting metadata of the document
	      System.out.println("Metadata of the PDF:");
	      String[] metadataNames = tp.getMetadata().names();
	      for(String name : metadataNames) {
        System.out.println(name+ " : " + tp.getMetadata().get(name));
     }
	      /*
	      Document d=tp.getDocument();
	      for(IndexableField field: d.getFields())
	      {
	    	  System.out.println(d.get(field.name()));
	      }*/
	      

   }
   
   
}