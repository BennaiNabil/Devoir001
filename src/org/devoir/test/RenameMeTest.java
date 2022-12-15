package org.devoir.test;


import org.devoir.main.RenameMe;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class RenameMeTest {

    private static RenameMe renameMe;
    private static String testFolder;

    private String prefix;
    private String suffix;

    @BeforeAll
    public void setUp() {
        testFolder = "testfolder";
        String propertiesFileName = System.getProperty("propfilename");
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(propertiesFileName));
            renameMe = new RenameMe(props);
            prefix = renameMe.getPrefix();
            suffix = renameMe.getSuffix();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(1)
    @DisplayName("Should fetch a list of all files in a folder")
    void getAllFilesInFolder() {
        List<File> allFiles = renameMe.getAllFilesInFolder(testFolder);
        assertEquals(allFiles.size(), 8);
    }

    @Test
    @Order(2)
    @DisplayName("Should fetch a file by its name")
    void getFileByPath() {
        String filename = "file1.txt";
        File file = renameMe.getFileByPath(testFolder, filename);
        assertEquals(file.getPath(), "%s/%s".formatted(testFolder, filename));
    }

    @Test
    @Order(3)
    @DisplayName("Should rename a single file")
    void renameFile() {
        String filename = "fileToRename.txt";
        String newFilename = "renamedFile.txt";
        File oldFile = renameMe.getFileByPath(testFolder, filename);
        renameMe.renameFile(oldFile, newFilename);
        File newFile = renameMe.getFileByPath(testFolder, newFilename);
        assertNotNull(newFile);
    }

    @Test
    @Order(4)
    @DisplayName("Should rename all files with predefinite prefixes and suffixes")
    void renameAll() {
        renameMe.renameAll(testFolder);
        String regex = "^pre.*suf\\.txt$";
        List<File> renamedFiles = renameMe.getAllFilesInFolder(testFolder);
        for (File file : renamedFiles) {
            assertTrue(file.getName().startsWith(this.prefix));
            assertTrue(file.getName().endsWith(this.suffix + ".txt"));
        }
    }

    @Test
    @Order(4)
    @DisplayName("Should undo the prefixing and suffixing")
    void undoRenameAll() {
        renameMe.undoRenameAll(testFolder);
        List<File> renamedFiles = renameMe.getAllFilesInFolder(testFolder);
        for (File file : renamedFiles) {
            assertFalse(file.getName().startsWith(this.prefix));
            assertFalse(file.getName().endsWith(this.suffix + ".txt"));
        }
    }

    @Test
    @DisplayName("Should rename a bunch of files with the same radix and a number")
    @Order(5)
    void batchRename() {
        renameMe.batchRename(testFolder, "same");
        List<File> renamedFiles = renameMe.getAllFilesInFolder(testFolder);
        for (File file : renamedFiles) {
            assertTrue(file.getName().startsWith("same"));
        }
    }
}