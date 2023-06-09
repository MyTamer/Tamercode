package net.sourceforge.cobertura.util;

import java.io.File;
import java.io.IOException;
import junit.framework.*;

public class FileFinderTest extends TestCase {

    private FileFinder fileFinder;

    private FileFixture fileFixture;

    public void testGetSourceDirectoryList() {
        assertEquals(4, fileFinder.getSourceDirectoryList().size());
        assertTrue(new FileFinder().getSourceDirectoryList().isEmpty());
        FileFinder ff = new FileFinder();
        ff.addSourceDirectory(FileFixture.SOURCE_DIRECTORY_IDENTIFIER[0]);
        assertEquals(1, ff.getSourceDirectoryList().size());
        ff.addSourceDirectory(FileFixture.SOURCE_DIRECTORY_IDENTIFIER[3]);
        assertEquals(2, ff.getSourceDirectoryList().size());
        ff = new FileFinder();
        ff.addSourceFile(FileFixture.SOURCE_DIRECTORY_IDENTIFIER[0], "com/example/Sample1.java");
        ff.addSourceFile(FileFixture.SOURCE_DIRECTORY_IDENTIFIER[0], "com/example/Sample2.java");
        assertEquals(1, ff.getSourceDirectoryList().size());
        ff.addSourceFile(FileFixture.SOURCE_DIRECTORY_IDENTIFIER[1], "com/example/Sample3.java");
        assertEquals(2, ff.getSourceDirectoryList().size());
        ff.addSourceDirectory(FileFixture.SOURCE_DIRECTORY_IDENTIFIER[3]);
        assertEquals(3, ff.getSourceDirectoryList().size());
    }

    private void checkFile(String fileName, String baseName, int sourceNum) throws IOException {
        File file = fileFinder.getFileForSource(fileName);
        assertTrue(file.getAbsolutePath(), file.getAbsolutePath().indexOf(FileFixture.SOURCE_DIRECTORY_IDENTIFIER[sourceNum]) != -1);
        assertTrue(baseName.equals(file.getName()));
        assertTrue(file.exists());
        assertTrue(file.isFile());
    }

    public void testFindFile() throws IOException {
        checkFile("com/example/Sample1.java", "Sample1.java", 0);
        checkFile("com\\example\\Sample2.java", "Sample2.java", 0);
        checkFile("com/example\\Sample3.java", "Sample3.java", 1);
        checkFile("com/example/Sample4.java", "Sample4.java", 1);
        checkFile("com/example/Sample5.java", "Sample5.java", 2);
        checkFile("com/example/Sample6.java", "Sample6.java", 2);
        checkFile("com\\example/Sample7.java", "Sample7.java", 3);
    }

    public void testFindFile_NotFound() {
        try {
            fileFinder.getFileForSource("com/example/Sample19.java");
            fail("IOException expected");
        } catch (IOException ex) {
        }
        try {
            fileFinder.getFileForSource("com/example/Sample1.jav");
            fail("IOException expected");
        } catch (IOException ex) {
        }
        try {
            fileFinder.getFileForSource("com/example/Sample7.java2");
            fail("IOException expected");
        } catch (IOException ex) {
        }
        try {
            fileFinder.getFileForSource("Sample3.java");
            fail("IOException expected");
        } catch (IOException ex) {
        }
        try {
            fileFinder.getFileForSource("com/example/Sample8.java");
            fail("IOException expected");
        } catch (IOException ex) {
        }
    }

    public void testFindFile_null() throws IOException {
        try {
            fileFinder.getFileForSource(null);
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
        }
    }

    public void testAddSourceDirectory_null() {
        try {
            fileFinder.addSourceDirectory(null);
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
        }
    }

    public void testAddSourceFile_null() {
        try {
            fileFinder.addSourceFile(null, "com/example/Sample1.java");
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
        }
        try {
            fileFinder.addSourceFile(FileFixture.SOURCE_DIRECTORY_IDENTIFIER[0], null);
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
        }
    }

    protected void setUp() throws Exception {
        super.setUp();
        fileFixture = new FileFixture();
        fileFixture.setUp();
        fileFinder = new FileFinder();
        fileFinder.addSourceDirectory(fileFixture.sourceDirectory(FileFixture.SOURCE_DIRECTORY_IDENTIFIER[0]).toString());
        fileFinder.addSourceDirectory(fileFixture.sourceDirectory(FileFixture.SOURCE_DIRECTORY_IDENTIFIER[1]).toString());
        fileFinder.addSourceFile(fileFixture.sourceDirectory(FileFixture.SOURCE_DIRECTORY_IDENTIFIER[2]).toString(), "com/example\\Sample5.java");
        fileFinder.addSourceFile(fileFixture.sourceDirectory(FileFixture.SOURCE_DIRECTORY_IDENTIFIER[2]).toString(), "com/example/Sample6.java");
        fileFinder.addSourceFile(fileFixture.sourceDirectory(FileFixture.SOURCE_DIRECTORY_IDENTIFIER[3]).toString(), "com/example/Sample7.java");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        fileFixture.tearDown();
    }
}
