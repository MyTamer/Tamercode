package org.ikasan.connector.basefiletransfer.outbound.command.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import junit.framework.TestCase;

/**
 * Test class for UnzippingFileProvider
 * 
 * @author Ikasan Development Team
 */
public class UnzippingFileProviderTest extends TestCase {

    /**
     * File name for the zip entry
     */
    private static final String ZIP_ENTRY_FILE_NAME = "file.ext";

    /**
     * Underlying zip archive for these tests
     */
    byte[] zipArchive = null;

    /**
     * Constructor
     */
    public UnzippingFileProviderTest() {
        try {
            zipArchive = zipBytes(new byte[1000], ZIP_ENTRY_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Answer a byte array compressed in the Zip format from bytes.
     * 
     * @param bytes a byte array
     * @param aName a String the represents a file name
     * @return byte[] compressed bytes
     * @throws IOException
     */
    public static byte[] zipBytes(byte[] bytes, String aName) throws IOException {
        ByteArrayOutputStream tempOStream = null;
        BufferedOutputStream tempBOStream = null;
        ZipOutputStream tempZStream = null;
        ZipEntry tempEntry = null;
        byte[] tempBytes = null;
        CRC32 tempCRC = null;
        tempOStream = new ByteArrayOutputStream(bytes.length);
        tempBOStream = new BufferedOutputStream(tempOStream);
        tempZStream = new ZipOutputStream(tempBOStream);
        tempCRC = new CRC32();
        tempCRC.update(bytes, 0, bytes.length);
        tempEntry = new ZipEntry(aName);
        tempEntry.setMethod(ZipEntry.STORED);
        tempEntry.setSize(bytes.length);
        tempEntry.setCrc(tempCRC.getValue());
        tempZStream.putNextEntry(tempEntry);
        tempZStream.write(bytes, 0, bytes.length);
        tempZStream.flush();
        tempBytes = tempOStream.toByteArray();
        tempZStream.close();
        return tempBytes;
    }

    /**
     * Simply tests the contructor
     * @throws UnzipNotSupportedException 
     */
    public void testUnzippingFileProvider() throws UnzipNotSupportedException {
        createUnzippingFileProvider();
    }

    /**
     * Creates an instance of the class to test
     * @return new instance of UnzippingFileProvider backed by a zip archive
     * @throws UnzipNotSupportedException 
     */
    private UnzippingFileProvider createUnzippingFileProvider() throws UnzipNotSupportedException {
        InputStream zippedInputStream = new ByteArrayInputStream(zipArchive);
        UnzippingFileProvider unzippingFileProvider = new UnzippingFileProvider(zippedInputStream);
        return unzippingFileProvider;
    }

    /**
     * Tests the hasNext method
     * @throws UnzipNotSupportedException 
     */
    public void testHasNext() throws UnzipNotSupportedException {
        UnzippingFileProvider fileProvider = createUnzippingFileProvider();
        assertTrue("fileProvider should have a next entry", fileProvider.hasNext());
    }

    /**
     * Tests the next method
     * @throws UnzipNotSupportedException 
     */
    public void testNext() throws UnzipNotSupportedException {
        UnzippingFileProvider fileProvider = createUnzippingFileProvider();
        FileHandle next = fileProvider.next();
        assertNotNull("object returned from next method should not be null, when there is a legitimate next object", next);
        assertEquals("path for FileHandle should match the name of the zip entry", ZIP_ENTRY_FILE_NAME, next.getPath());
        assertFalse("fileProvider should not have a next entry, when the only entry has already been retrieved", fileProvider.hasNext());
    }

    /**
     * Test the illegal Input Stream
     */
    public void testIlleagalInputStream() {
        byte[] bytes = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
        try {
            new UnzippingFileProvider(new ByteArrayInputStream(bytes));
            fail("shoud have thrown exception");
        } catch (UnzipNotSupportedException e) {
        }
    }
}
