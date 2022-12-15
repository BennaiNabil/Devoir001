package org.devoir.main;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class RenameMe {

    /**
     * String receiving the prefix fetched from the Properties object
     */
    private String prefix;

    /**
     * String receiving the suffix fetched from the Properties object
     */
    private String suffix;
    /**
     * String encoding the default prefix if none is available from the Properties object
     */
    private static final String DEFAULT_PREFIX = "xxx_";
    /**
     * String encoding the default suffix if none is available from the Properties object
     */
    private static final String DEFAULT_SUFFIX = "_yyy";


    /**
     * Constructor taking a Properties object containing a prefix and a suffix
     *
     * @param properties The Properties object containing a prefix and a suffix
     */
    public RenameMe(Properties properties) {
        this.prefix = properties.getProperty("prefix") == null ? DEFAULT_PREFIX : properties.getProperty("prefix");
        this.suffix = properties.getProperty("suffix") == null ? DEFAULT_SUFFIX : properties.getProperty("suffix");
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }


    /**
     * Returns a list of all the files recursively in a folder passed as an argument
     *
     * @param folderPathName the relative path of the file we want to get the files of
     * @return a list of File objects
     */
    public List<File> getAllFilesInFolder(String folderPathName) {
        // New File object encoding the folder located in folderPathName
        File dir = new File(folderPathName);
        // Returns a list of all the files in dir
        return (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
    }

    /**
     * Renames a file
     *
     * @param file    Original File object to rename
     * @param newName new name as a String
     */
    public void renameFile(File file, String newName) {
        try {
            Path source = Paths.get(file.getPath());
            File fileToCheck = new File(newName);
            if (fileToCheck.exists()) {
                System.out.printf("> The file %s already exists %n", newName);
            } else {
                Files.move(source, source.resolveSibling(newName));
                System.out.printf("> The file %s has been renamed to %s%n", file.getName(), newName);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Takes a File object as a parameter and returns a prefixed and suffixed String
     *
     * @param file The file we want to generate a prefixed and suffixed version of its name
     * @return a String as follows: [PREFIX]baseName[Suffix]
     */
    private String prefixAndSuffix(File file) {
        String fileExtension = FilenameUtils.getExtension(file.getName());
        String coreFileName = FilenameUtils.getBaseName(file.getName());
        return "%s%s%s.%s".formatted(this.prefix, coreFileName, this.suffix, fileExtension);
    }

    private String undoPrefixAndSuffix(File file) {
        String fileExtension = FilenameUtils.getExtension(file.getName());
        String coreFileName = FilenameUtils.getBaseName(file.getName()).replaceAll(this.prefix, "").replaceAll(this.suffix, "");
        return "%s.%s".formatted(coreFileName, fileExtension);
    }

    /**
     * Renames all the files within a folder by adding a prefix and a suffix
     *
     * @param folderPathName folder's name as a String
     */
    public void renameAll(String folderPathName) {
        getAllFilesInFolder(folderPathName).forEach(file -> renameFile(file, prefixAndSuffix(file)));
    }

    /**
     * Undoes the prefixing and the suffixing within a folder
     *
     * @param folderPathName folder's name as a String
     */
    public void undoRenameAll(String folderPathName) {
        getAllFilesInFolder(folderPathName).forEach(file -> renameFile(file, undoPrefixAndSuffix(file)));
    }

    /**
     * Renames all the files in a folder, with the same name suffixed by a number
     *
     * @param folderPathName the folder within which you wan to batch rename
     * @param baseName       the common radix
     */
    public void batchRename(String folderPathName, String baseName) {
        List<File> files = getAllFilesInFolder(folderPathName);
        String coreFileName;
        String fileExtension;
        int nbFiles = files.size();
        for (int i = 0; i < files.size(); i++) {
            int nbLeadingZeros = (int) (Math.ceil(Math.log10(nbFiles)));
            File file = files.get(i);
            String newName = getNewNameWithNumber(baseName, i, nbLeadingZeros, file);
            renameFile(file, newName);
        }
    }

    private static String getNewNameWithNumber(String baseName, int i, int nbLeadingZeros, File file) {
        String coreFileName;
        String fileExtension;
        coreFileName = baseName + "0".repeat(nbLeadingZeros) + String.valueOf(i + 1);
        fileExtension = FilenameUtils.getExtension(file.getName());
        return "%s.%s".formatted(coreFileName, fileExtension);
    }

    /**
     * Searches for a file within a folder
     *
     * @param folderPathName the folder's name
     * @param filename       the searched file's name
     * @return a file object
     */
    public File getFileByPath(String folderPathName, String filename) {
        File dir = new File("%s/%s".formatted(folderPathName, filename));
        return FileUtils.getFile(dir);
    }
}
