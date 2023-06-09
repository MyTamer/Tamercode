package com.limegroup.gnutella.gui.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import junit.framework.Test;
import org.limewire.util.FileUtils;
import com.google.inject.Injector;
import com.limegroup.gnutella.LimeTestUtils;
import com.limegroup.gnutella.RemoteFileDesc;
import com.limegroup.gnutella.URN;
import com.limegroup.gnutella.downloader.RemoteFileDescFactory;
import com.limegroup.gnutella.settings.SharingSettings;
import com.limegroup.gnutella.util.LimeTestCase;

public class StoreDownloaderFactoryTest extends LimeTestCase {

    private RemoteFileDescFactory remoteFileDescFactory;

    public StoreDownloaderFactoryTest(String name) {
        super(name);
    }

    public static Test suite() {
        return buildTestSuite(StoreDownloaderFactoryTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    protected void setUp() {
        Injector injector = LimeTestUtils.createInjector();
        remoteFileDescFactory = injector.getInstance(RemoteFileDescFactory.class);
    }

    public void testInvalidStoreDownloaderFactoryArgs() {
        try {
            new StoreDownloaderFactory(null, null);
            fail("StoreDescriptor cannot be null");
        } catch (NullPointerException e) {
        }
        ;
    }

    @SuppressWarnings("deprecation")
    public void testStoreDownloaderFactorySetup() throws Exception {
        File file = createNewNamedTestFile(5, "test", _baseDir);
        file.deleteOnExit();
        URN urn = URN.createSHA1Urn(file);
        String fileName = "fileName.txt";
        long size = 100;
        RemoteFileDesc rfd = remoteFileDescFactory.createUrlRemoteFileDesc(file.toURL(), fileName, urn, size);
        assertNotNull(rfd);
        StoreDownloaderFactory store = new StoreDownloaderFactory(rfd, fileName);
        long sizeReturn = store.getFileSize();
        assertEquals(size, sizeReturn);
        URN urnReturn = store.getURN();
        assertEquals(urnReturn, urn);
        File fileReturn = store.getSaveFile();
        fileReturn.deleteOnExit();
        assertEquals(new File(SharingSettings.getSaveLWSDirectory(), fileName), fileReturn);
        File newFile = new File(SharingSettings.getSaveLWSDirectory(), "test.txt");
        store.setSaveFile(newFile);
        fileReturn = store.getSaveFile();
        assertEquals(newFile, fileReturn);
        store.setSaveFile(null);
        assertEquals(newFile, store.getSaveFile());
        store = new StoreDownloaderFactory(rfd, null);
        fileReturn = store.getSaveFile();
        assertEquals(new File(SharingSettings.getSaveLWSDirectory(), fileName), fileReturn);
    }

    /**
     * Helper function to create a new temporary file of the given size,
     * with the given name, in the given directory.
     */
    protected File createNewNamedTestFile(int size, String name, File directory) throws Exception {
        File file = File.createTempFile(name, "." + "XYZ", directory);
        file.deleteOnExit();
        OutputStream out = new FileOutputStream(file);
        out.write(new byte[size]);
        out.flush();
        out.close();
        return FileUtils.getCanonicalFile(file);
    }
}
