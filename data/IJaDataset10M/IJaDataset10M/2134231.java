package org.archive.crawler.extractor;

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PRIndirectReference;
import com.lowagie.text.pdf.PdfArray;
import java.io.*;
import java.util.*;

public class PDFParser {

    ArrayList<String> foundURIs;

    ArrayList<ArrayList<Integer>> encounteredReferences;

    PdfReader documentReader;

    byte[] document;

    PdfDictionary catalog;

    public PDFParser(String doc) throws IOException {
        resetState();
        getInFromFile(doc);
        initialize();
    }

    public PDFParser(byte[] doc) throws IOException {
        resetState();
        document = doc;
        initialize();
    }

    /** Reinitialize the object as though a new one were created.
     */
    protected void resetState() {
        foundURIs = new ArrayList<String>();
        encounteredReferences = new ArrayList<ArrayList<Integer>>();
        documentReader = null;
        document = null;
        catalog = null;
        for (int i = 0; i < encounteredReferences.size(); i++) {
            encounteredReferences.add(new ArrayList<Integer>());
        }
    }

    /**
     * Reset the object and initialize it with a new byte array (the document).
     * @param doc
     * @throws IOException
     */
    public void resetState(byte[] doc) throws IOException {
        resetState();
        document = doc;
        initialize();
    }

    /** Reinitialize the object as though a new one were created, complete
     * with a valid pointer to a document that can be read
     * @param doc
     * @throws IOException
     */
    public void resetState(String doc) throws IOException {
        resetState();
        getInFromFile(doc);
        initialize();
    }

    /**
     * Read a file named 'doc' and store its' bytes for later processing.
     * @param doc
     * @throws IOException
     */
    protected void getInFromFile(String doc) throws IOException {
        File documentOnDisk = new File(doc);
        long length = documentOnDisk.length();
        document = new byte[(int) length];
        FileInputStream inStream = new FileInputStream(documentOnDisk);
        inStream.read(document);
    }

    /**
     * Indicates, based on a PDFObject's generation/id pair whether
     * the parser has already encountered this object (or a reference to it)
     * so we don't infinitely loop on circuits within the PDF.
     * @param generation
     * @param id
     * @return True if already seen.
     */
    protected boolean haveSeen(int generation, int id) {
        if (generation >= encounteredReferences.size()) {
            for (int i = encounteredReferences.size(); i <= generation; i++) {
                encounteredReferences.add(new ArrayList<Integer>());
            }
            return false;
        }
        ArrayList<Integer> generationList = encounteredReferences.get(generation);
        for (int i : generationList) {
            if (i == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Note that an object (id/generation pair) has been seen by this parser
     * so that it can be handled differently when it is encountered again.
     * @param generation
     * @param id
     */
    protected void markAsSeen(int generation, int id) {
        ArrayList<Integer> objectIds = encounteredReferences.get(generation);
        objectIds.add(id);
    }

    /**
     * Get a list of URIs retrieved from the Pdf during the
     * extractURIs operation.
     * @return A list of URIs retrieved from the Pdf during the
     * extractURIs operation.
     */
    public ArrayList getURIs() {
        return foundURIs;
    }

    /**
     * Initialize opens the document for reading.  This is done implicitly
     * by the constuctor.  This should only need to be called directly following
     * a reset.
     * @throws IOException
     */
    protected void initialize() throws IOException {
        if (document != null) {
            documentReader = new PdfReader(document);
        }
        catalog = documentReader.getCatalog();
    }

    /**
     * Extract URIs from all objects found in a Pdf document's catalog.
     * Returns an array list representing all URIs found in the document catalog tree.
     * @return URIs from all objects found in a Pdf document's catalog.
     */
    public ArrayList extractURIs() {
        extractURIs(catalog);
        return getURIs();
    }

    /**
     * Parse a PdfDictionary, looking for URIs recursively and adding
     * them to foundURIs
     * @param entity
     */
    protected void extractURIs(PdfObject entity) {
        if (entity.isDictionary()) {
            PdfDictionary dictionary = (PdfDictionary) entity;
            @SuppressWarnings("unchecked") Set<PdfName> allkeys = dictionary.getKeys();
            for (PdfName key : allkeys) {
                PdfObject value = dictionary.get(key);
                if (key.toString().equals("/URI") || key.toString().equals("/URL")) {
                    foundURIs.add(value.toString());
                } else {
                    this.extractURIs(value);
                }
            }
        } else if (entity.isArray()) {
            PdfArray array = (PdfArray) entity;
            ArrayList arrayObjects = array.getArrayList();
            Iterator objectList = arrayObjects.iterator();
            while (objectList.hasNext()) {
                this.extractURIs((PdfObject) objectList.next());
            }
        } else if (entity.getClass() == PRIndirectReference.class) {
            PRIndirectReference indirect = (PRIndirectReference) entity;
            if (haveSeen(indirect.getGeneration(), indirect.getNumber())) {
                return;
            } else {
                markAsSeen(indirect.getGeneration(), indirect.getNumber());
            }
            indirect.getReader();
            PdfObject direct = PdfReader.getPdfObject(indirect);
            this.extractURIs(direct);
        }
    }

    public static void main(String[] argv) {
        try {
            PDFParser parser = new PDFParser("/home/parkert/files/pdfspec.pdf");
            ArrayList uris = parser.extractURIs();
            Iterator i = uris.iterator();
            while (i.hasNext()) {
                String uri = (String) i.next();
                System.out.println("got uri: " + uri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
