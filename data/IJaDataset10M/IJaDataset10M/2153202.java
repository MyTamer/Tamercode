package org.streams.test.collector.write.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionInputStream;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.log4j.Logger;
import org.streams.collector.main.Bootstrap;
import org.streams.collector.write.LogFileWriter;
import org.streams.collector.write.impl.LocalLogFileWriter;
import org.streams.commons.cli.CommandLineProcessorFactory;
import org.streams.commons.file.CoordinationException;
import org.streams.commons.file.FileStatus;
import org.streams.commons.file.PostWriteAction;

/**
 * Tests the LocalLogFileWriter features
 * 
 */
public class TestLocalFileWriter extends TestCase {

    private static final Logger LOG = Logger.getLogger(TestLocalFileWriter.class);

    File baseDir;

    /**
	 * Test that the LocalLogFileWriter does rollback a compressed file as
	 * expected.
	 * 
	 * @throws Exception
	 */
    public void testFileWriteRollBack() throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.loadProfiles(CommandLineProcessorFactory.PROFILE.DB, CommandLineProcessorFactory.PROFILE.REST_CLIENT, CommandLineProcessorFactory.PROFILE.COLLECTOR);
        final LocalLogFileWriter writer = (LocalLogFileWriter) bootstrap.getBean(LogFileWriter.class);
        GzipCodec codec = new GzipCodec();
        writer.init();
        writer.setCompressionCodec(codec);
        File fileInput = new File(baseDir, "testFileWriteRollBack/input");
        fileInput.mkdirs();
        File fileOutput = new File(baseDir, "testFileWriteRollBack/output");
        fileOutput.mkdirs();
        writer.setBaseDir(fileOutput);
        int fileCount = 10;
        int lineCount = 100;
        final int rollbackLimit = 90;
        File[] inputFiles = createInput(fileInput, fileCount, lineCount);
        final CountDownLatch latch = new CountDownLatch(inputFiles.length);
        ExecutorService exec = Executors.newFixedThreadPool(fileCount);
        for (final File file : inputFiles) {
            exec.submit(new Callable<Boolean>() {

                public Boolean call() throws Exception {
                    FileStatus.FileTrackingStatus status = FileStatus.FileTrackingStatus.newBuilder().setFileDate(System.currentTimeMillis()).setDate(System.currentTimeMillis()).setAgentName("agent1").setFileName(file.getName()).setFileSize(file.length()).setLogType("type1").build();
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    try {
                        String line = null;
                        int lineCount = 0;
                        final AtomicBoolean doRollback = new AtomicBoolean(false);
                        while ((line = reader.readLine()) != null) {
                            if (lineCount++ == rollbackLimit) {
                                doRollback.set(true);
                            }
                            writer.write(status, new ByteArrayInputStream((line + "\n").getBytes()), new PostWriteAction() {

                                @Override
                                public void run(int bytesWritten) throws Exception {
                                    if (doRollback.get()) {
                                        throw new CoordinationException("Induced error");
                                    }
                                }
                            });
                        }
                    } finally {
                        IOUtils.closeQuietly(reader);
                        latch.countDown();
                    }
                    return true;
                }
            });
        }
        latch.await();
        writer.close();
        File[] files = fileOutput.listFiles();
        assertNotNull(files);
        assertEquals(1, files.length);
        CompressionInputStream cin = codec.createInputStream(new FileInputStream(files[0]));
        BufferedReader reader = new BufferedReader(new InputStreamReader(cin));
        int inputLineCount = 0;
        try {
            while (reader.readLine() != null) {
                inputLineCount++;
            }
        } finally {
            cin.close();
            reader.close();
        }
        assertEquals(fileCount * rollbackLimit, inputLineCount);
    }

    public void testWriteThreadsNoCompression() throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.loadProfiles(CommandLineProcessorFactory.PROFILE.DB, CommandLineProcessorFactory.PROFILE.REST_CLIENT, CommandLineProcessorFactory.PROFILE.COLLECTOR);
        final LocalLogFileWriter writer = (LocalLogFileWriter) bootstrap.getBean(LogFileWriter.class);
        writer.init();
        writer.setCompressionCodec(null);
        File fileInput = new File(baseDir, "testWriteOneFile/input");
        fileInput.mkdirs();
        File fileOutput = new File(baseDir, "testWriteOneFile/output");
        fileOutput.mkdirs();
        writer.setBaseDir(fileOutput);
        int fileCount = 100;
        int lineCount = 100;
        File[] inputFiles = createInput(fileInput, fileCount, lineCount);
        ExecutorService exec = Executors.newFixedThreadPool(fileCount);
        final CountDownLatch latch = new CountDownLatch(fileCount);
        for (int i = 0; i < fileCount; i++) {
            final File file = inputFiles[i];
            final int count = i;
            exec.submit(new Callable<Boolean>() {

                @Override
                public Boolean call() throws Exception {
                    FileStatus.FileTrackingStatus status = FileStatus.FileTrackingStatus.newBuilder().setFileDate(System.currentTimeMillis()).setDate(System.currentTimeMillis()).setAgentName("agent1").setFileName(file.getName()).setFileSize(file.length()).setLogType("type1").build();
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    try {
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            writer.write(status, new ByteArrayInputStream((line + "\n").getBytes()));
                        }
                    } finally {
                        IOUtils.closeQuietly(reader);
                    }
                    LOG.info("Thread[" + count + "] completed ");
                    latch.countDown();
                    return true;
                }
            });
        }
        latch.await();
        exec.shutdown();
        LOG.info("Shutdown thread service");
        writer.close();
        File[] outputFiles = fileOutput.listFiles();
        assertNotNull(outputFiles);
        File testCombinedInput = new File(baseDir, "combinedInfile.txt");
        testCombinedInput.createNewFile();
        FileOutputStream testCombinedInputOutStream = new FileOutputStream(testCombinedInput);
        try {
            for (File file : inputFiles) {
                FileInputStream f1In = new FileInputStream(file);
                IOUtils.copy(f1In, testCombinedInputOutStream);
            }
        } finally {
            testCombinedInputOutStream.close();
        }
        File testCombinedOutput = new File(baseDir, "combinedOutfile.txt");
        testCombinedOutput.createNewFile();
        FileOutputStream testCombinedOutOutStream = new FileOutputStream(testCombinedOutput);
        try {
            System.out.println("----------------- " + testCombinedOutput.getAbsolutePath());
            for (File file : outputFiles) {
                FileInputStream f1In = new FileInputStream(file);
                IOUtils.copy(f1In, testCombinedOutOutStream);
            }
        } finally {
            testCombinedOutOutStream.close();
        }
        FileUtils.contentEquals(testCombinedInput, testCombinedOutput);
    }

    private File[] createInput(File fileInput, int fileCount, int lineCount) throws IOException {
        final File[] inputFiles = new File[fileCount];
        for (int i = 0; i < fileCount; i++) {
            File file = new File(fileInput, "test_" + i);
            FileWriter fileWriter = new FileWriter(file);
            try {
                for (int a = 0; a < lineCount; a++) {
                    fileWriter.append("A_" + a + "\tB_" + a + "\n");
                }
            } finally {
                fileWriter.close();
            }
            inputFiles[i] = file;
        }
        return inputFiles;
    }

    /**
	 * Tests writing one file. This test makes use of the CollectorDI
	 * 
	 * @throws Exception
	 */
    public void testWriteOneFileNoCompression() throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.loadProfiles(CommandLineProcessorFactory.PROFILE.DB, CommandLineProcessorFactory.PROFILE.REST_CLIENT, CommandLineProcessorFactory.PROFILE.COLLECTOR);
        LocalLogFileWriter writer = (LocalLogFileWriter) bootstrap.getBean(LogFileWriter.class);
        writer.setCompressionCodec(null);
        File fileInput = new File(baseDir, "testWriteOneFile/input");
        fileInput.mkdirs();
        File fileOutput = new File(baseDir, "testWriteOneFile/output");
        fileOutput.mkdirs();
        writer.setBaseDir(fileOutput);
        writer.init();
        File testFile = new File(fileInput, "testFile1.2010-01-01");
        testFile.createNewFile();
        FileWriter testFileWriter = new FileWriter(testFile);
        int lineCount = 100;
        try {
            for (int i = 0; i < lineCount; i++) {
                testFileWriter.write("A_" + i + "\tB_" + i + "\n");
            }
        } finally {
            testFileWriter.close();
        }
        BufferedReader reader = new BufferedReader(new FileReader(testFile));
        String line = null;
        FileStatus.FileTrackingStatus fileStatus = FileStatus.FileTrackingStatus.newBuilder().setFileDate(System.currentTimeMillis()).setDate(System.currentTimeMillis()).setAgentName("agent1").setFileName(testFile.getAbsolutePath()).setFileSize(testFile.length()).setLogType("type1").build();
        try {
            writer.init();
            int counter = 0;
            while ((line = reader.readLine()) != null) {
                byte[] bytes = (line + "\n").getBytes();
                System.out.println("Writing bytes: " + counter++);
                writer.write(fileStatus, new ByteArrayInputStream(bytes));
            }
        } finally {
            reader.close();
            writer.close();
        }
        File[] outFiles = fileOutput.listFiles();
        assertNotNull(outFiles);
        assertEquals(1, outFiles.length);
        boolean isEqual = FileUtils.contentEquals(testFile, outFiles[0]);
        assertTrue(isEqual);
    }

    @Override
    protected void setUp() throws Exception {
        baseDir = new File("target", "testLocalFileWRiter");
        if (baseDir.exists()) {
            FileUtils.deleteDirectory(baseDir);
        }
        baseDir.mkdirs();
    }

    @Override
    protected void tearDown() throws Exception {
        FileUtils.deleteDirectory(baseDir);
    }
}
