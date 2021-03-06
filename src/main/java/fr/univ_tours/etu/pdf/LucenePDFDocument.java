/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.univ_tours.etu.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import fr.univ_tours.etu.index.NlpSynoner;
import fr.univ_tours.etu.index.Synoner;
import fr.univ_tours.etu.nlp.*;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.util.PDFTextStripper;

import fr.univ_tours.etu.nlp.NlpNeTokenizer;

/**
 * This class is used to create a document for the lucene search engine. This should easily plug into the IndexPDFFiles
 * that comes with the lucene project. This class will populate the following fields.
 * <table>
 * <tr>
 * <th>Lucene Field Name</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>path</td>
 * <td>File system path if loaded from a file</td>
 * </tr>
 * <tr>
 * <td>url</td>
 * <td>URL to PDF document</td>
 * </tr>
 * <tr>
 * <td>contents</td>
 * <td>Entire contents of PDF document, indexed but not stored</td>
 * </tr>
 * <tr>
 * <td>summary</td>
 * <td>First 500 characters of content</td>
 * </tr>
 * <tr>
 * <td>modified</td>
 * <td>The modified date/time according to the url or path</td>
 * </tr>
 * <tr>
 * <td>uid</td>
 * <td>A unique identifier for the Lucene document.</td>
 * </tr>
 * <tr>
 * <td>CreationDate</td>
 * <td>From PDF meta-data if available</td>
 * </tr>
 * <tr>
 * <td>Creator</td>
 * <td>From PDF meta-data if available</td>
 * </tr>
 * <tr>
 * <td>Keywords</td>
 * <td>From PDF meta-data if available</td>
 * </tr>
 * <tr>
 * <td>ModificationDate</td>
 * <td>From PDF meta-data if available</td>
 * </tr>
 * <tr>
 * <td>Producer</td>
 * <td>From PDF meta-data if available</td>
 * </tr>
 * <tr>
 * <td>Subject</td>
 * <td>From PDF meta-data if available</td>
 * </tr>
 * <tr>
 * <td>Trapped</td>
 * <td>From PDF meta-data if available</td>
 * </tr>
 * </table>
 *
 * @author Ben Litchfield
 *
 */
public class LucenePDFDocument
{
    private static final char FILE_SEPARATOR = System.getProperty("file.separator").charAt(0);

    // given caveat of increased search times when using
    // MICROSECOND, only use SECOND by default
    private static final DateTools.Resolution DATE_TIME_RES = DateTools.Resolution.SECOND;

    private PDFTextStripper stripper = null;
	private Document readySynDoc;

    private NlpNeTokenizer nlpNeTokenizer;
    private Map<String, List<String>> lemmaPosMap;

    /** not Indexed, tokenized, stored. */
    public static final FieldType TYPE_STORED_NOT_INDEXED = new FieldType();

    static
    {
        TYPE_STORED_NOT_INDEXED.setIndexOptions(IndexOptions.NONE);
        TYPE_STORED_NOT_INDEXED.setStored(true);
        TYPE_STORED_NOT_INDEXED.setTokenized(true);
        TYPE_STORED_NOT_INDEXED.freeze();
    }

    private List<String> lemmas;

    /**
     * Constructor.
     */
    public LucenePDFDocument()
    {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
        nlpNeTokenizer = new CoreNlpTokenizer(props);
    }

    /**
     * Set the text stripper that will be used during extraction.
     *
     * @param aStripper The new pdf text stripper.
     */
    public void setTextStripper(PDFTextStripper aStripper)
    {
        stripper = aStripper;
    }

    private static String timeToString(long time)
    {
        return DateTools.timeToString(time, DATE_TIME_RES);
    }

    private void addKeywordField(Document document, String name, String value)
    {
        if (value != null)
        {
            document.add(new StringField(name, value, Field.Store.YES));
        }
    }

    private void addTextField(Document document, String name, Reader value)
    {
        if (value != null)
        {
            document.add(new TextField(name, value));
        }
    }

    private void addTextField(Document document, String name, Reader value, float boost)
    {
        if (value != null)
        {
            TextField textField = new TextField(name, value);
            textField.setBoost(boost);
            document.add(textField);
        }
    }

    private void addTextField(Document document, String name, String value)
    {
        if (value != null)
        {
            document.add(new TextField(name, value, Field.Store.YES));
        }
    }

    private void addTextField(Document document, String name, Date value)
    {
        if (value != null)
        {
            addTextField(document, name, DateTools.dateToString(value, DATE_TIME_RES));
        }
    }
    
    private void addUnstoredDate(Document document, String name, Date calendar)
    {
        if (calendar != null)
        {
        	document.add(new TextField(name, DateTools.dateToString(calendar, DATE_TIME_RES), Field.Store.NO));
           // addTextField(document, name, DateTools.dateToString(value, DATE_TIME_RES));
        }
    }

    private void addTextField(Document document, String name, Calendar value)
    {
        if (value != null)
        {
            addTextField(document, name, value.getTime());
        }
    }

    private static void addUnindexedField(Document document, String name, String value)
    {
        if (value != null)
        {
            document.add(new Field(name, value, TYPE_STORED_NOT_INDEXED));
        }
    }

    private void addUnstoredKeywordField(Document document, String name, String value)
    {
        if (value != null)
        {
            document.add(new Field(name, value, TextField.TYPE_NOT_STORED));
        }
    }

    /**
     * Convert the PDF stream to a lucene document.
     *
     * @param is The input stream.
     * @return The input stream converted to a lucene document.
     * @throws IOException If there is an error converting the PDF.
     */
    public Document convertDocument(InputStream is) throws IOException
    {
        Document document = new Document();
        addContent(document, is, "<inputstream>");
        return document;

    }

    /**
     * This will take a reference to a PDF document and create a lucene document.
     *
     * @param file A reference to a PDF document.
     * @return The converted lucene document.
     *
     * @throws IOException If there is an exception while converting the document.
     */
    public Document convertDocument(File file) throws IOException
    {
        Document document = new Document();
        //Document synDoc = new Document();

        // Add the url as a field named "url". Use an UnIndexed field, so
        // that the url is just stored with the document, but is not searchable.
        addUnindexedField(document, DocFields.FILE_PATH, file.getPath());
        addUnindexedField(document, DocFields.FILE_NAME, file.getName());
        
        //addUnindexedField(synDoc, DocFields.FILE_PATH, file.getPath());
        //addUnindexedField(synDoc, DocFields.FILE_NAME, file.getName());
      
       // addKeywordField(document, "modified", timeToString(file.lastModified()));

        String uid = createUID(file);

        addUnstoredKeywordField(document, DocFields.UID, uid);
        //addUnstoredKeywordField(synDoc, DocFields.UID, uid);

        FileInputStream input = null;
        try
        {
            input = new FileInputStream(file);
            addContent(document, input, file.getPath());
            //this.addSynonyms(synDoc);
            this.addSynonyms(document);
            //this.readySynDoc=synDoc;
        } catch (Exception e){
            e.printStackTrace();
        }
        finally
        {
            if (input != null)
            {
                input.close();
            }
        }

        // return the document

        return document;
    }
    
    public Document getSynDoc()
    {
    	return this.readySynDoc;
    }

    /**
     * Convert the document from a PDF to a lucene document.
     *
     * @param url A url to a PDF document.
     * @return The PDF converted to a lucene document.
     * @throws IOException If there is an error while converting the document.
     */
    public Document convertDocument(URL url) throws IOException
    {
        Document document = new Document();
        URLConnection connection = url.openConnection();
        connection.connect();
        // Add the url as a field named "url". Use an UnIndexed field, so
        // that the url is just stored with the document, but is not searchable.
        addUnindexedField(document, "url", url.toExternalForm());

        // Add the last modified date of the file a field named "modified". Use a
        // Keyword field, so that it's searchable, but so that no attempt is made
        // to tokenize the field into words.
        addKeywordField(document, "modified", timeToString(connection.getLastModified()));

        String uid = createUID(url, connection.getLastModified());

        // Add the uid as a field, so that index can be incrementally maintained.
        // This field is not stored with document, it is indexed, but it is not
        // tokenized prior to indexing.
        addUnstoredKeywordField(document, "uid", uid);

        InputStream input = null;
        try
        {
            input = connection.getInputStream();
            addContent(document, input, url.toExternalForm());
        }
        finally
        {
            if (input != null)
            {
                input.close();
            }
        }

        // return the document
        return document;
    }
    
    

    /**
     * This will get a lucene document from a PDF file.
     *
     * @param is The stream to read the PDF from.
     *
     * @return The lucene document.
     *
     * @throws IOException If there is an error parsing or indexing the document.
     */
    public static Document getDocument(InputStream is) throws IOException
    {
        LucenePDFDocument converter = new LucenePDFDocument();
        return converter.convertDocument(is);
    }

    /**
     * This will get a lucene document from a PDF file.
     *
     * @param file The file to get the document for.
     *
     * @return The lucene document.
     *
     * @throws IOException If there is an error parsing or indexing the document.
     */
    public static Document getDocument(File file) throws IOException
    {
        LucenePDFDocument converter = new LucenePDFDocument();
        return converter.convertDocument(file);
    }

    /**
     * This will get a lucene document from a PDF file.
     *
     * @param url The file to get the document for.
     *
     * @return The lucene document.
     *
     * @throws IOException If there is an error parsing or indexing the document.
     */
    public static Document getDocument(URL url) throws IOException
    {
        LucenePDFDocument converter = new LucenePDFDocument();
        return converter.convertDocument(url);
    }
    
    private void addSynonyms(Document doc) throws IOException
    {
    	Synoner sn= new NlpSynoner(lemmaPosMap, lemmas);
		sn.tokenize();
		String readySyn = sn.getSynText();
		//System.out.println(readySyn);
		StringReader reader = new StringReader(readySyn);
        addTextField(doc, DocFields.SYNONYMS, reader, 0.5f);
    }

    /**
     * This will add the contents to the lucene document.
     *
     * @param document The document to add the contents to.
     * @param is The stream to get the contents from.
     * @param documentLocation The location of the document, used just for debug messages.
     *
     * @throws IOException If there is an error parsing the document.
     */
    private void addContent(Document document, InputStream is, String documentLocation) throws IOException
    {
        PDDocument pdfDocument = null;
        try
        {
            pdfDocument = PDDocument.load(is);

            // create a writer where to append the text content.
            StringWriter writer = new StringWriter();
            if (stripper == null)
            {
                stripper = new PDFTextStripper();
            }
            stripper.writeText(pdfDocument, writer);

            String contentsDirty = writer.getBuffer().toString();
            //System.out.println(contentsDirty.substring(0,100));
            String contents = contentsDirty.replaceAll("\\p{Sm}|\\p{Sk}|\\p{So}", " ");
            //System.out.println(contents);

            // addTextField(document, DocFields.CONTENTS, reader);
            TextField ne= this.getNamedEntities(contents);

            String lemmas = nlpNeTokenizer.getLemmaString();

            //StringReader reader = new StringReader(contents);
            StringReader reader = new StringReader(lemmas);

            // Add the tag-stripped contents as a Reader-valued Text field so it will
            // get tokenized and indexed.

            FieldType type = new FieldType();
            type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
            type.setStored(false);
            type.setTokenized(true);
            document.add(new Field(DocFields.CONTENTS, reader, type));

            PDDocumentInformation info = pdfDocument.getDocumentInformation();
            if (info != null)
            {
            	document.add(ne);//adding named entities
                addTextField(document, DocFields.AUTHOR, info.getAuthor());
                
                try{//to avoid issues with CreationDate
                addUnstoredDate(document, DocFields.CREATION_DATE, info.getCreationDate().getTime());
                }catch(Exception e)
                {
                	System.out.println("Warning: some issue with CreationDate attribute!");
                }
                
                addTextField(document, DocFields.CREATOR, info.getCreator());
                addTextField(document, DocFields.KEYWORDS, info.getKeywords());
                
            
                addTextField(document, DocFields.SUBJECT, info.getSubject());
                addTextField(document, DocFields.TITLE, info.getTitle());
                
                //addTextField(document, "Title", info.getTitle());
              //addTextField(document, "ModificationDate", info.getModificationDate());
                //addTextField(document, "Producer", info.getProducer());
                //addTextField(document, "Trapped", info.getTrapped());
                
            }
            
            
            int summarySize = Math.min(contents.length(), 1500);
            String summary = contents.substring(0, summarySize);
            // Add the summary as an UnIndexed field, so that it is stored and returned
            // with hit documents for display.
            addUnindexedField(document, DocFields.SUMMARY, summary);
        } finally
        {
            if (pdfDocument != null)
            {
                pdfDocument.close();
            }
        }
    }

    /**
     * Create an UID for the given file using the given time.
     *
     * @param url the file we have to create an UID for
     * @param time the time to used to the UID
     *
     * @return the created UID
     */
    public static String createUID(URL url, long time)
    {
        return url.toExternalForm().replace(FILE_SEPARATOR, '\u0000') + "\u0000" + timeToString(time);
    }

    /**
     * Create an UID for the given file.
     *
     * @param file the file we have to create an UID for
     *
     * @return the created UID
     */
    public static String createUID(File file)
    {
        return file.getPath().replace(FILE_SEPARATOR, '\u0000') + "\u0000" + timeToString(file.lastModified());
    }
    
    /**
     * Gets all named-entities as TextField
     * @param text
     * @return
     * @throws IOException
     */
    public TextField getNamedEntities(String text) throws IOException {
        nlpNeTokenizer.tokenize(text, false);
        String neString = nlpNeTokenizer.getNeString(";", false);
        System.out.println(neString);
        lemmaPosMap = nlpNeTokenizer.getLemmaPosMap();
        lemmas = nlpNeTokenizer.getLemmaList();
        return new TextField(DocFields.NAMED_ENTITIES, neString, Field.Store.NO);

    }
}