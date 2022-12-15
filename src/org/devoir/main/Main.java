package org.devoir.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        if (args.length == 2) {
            String runningMode = args[0];
            String folder = args[1];
            Properties props = new Properties();
            String propertiesFileName = System.getProperty("propfilename");
            try {
                FileInputStream fis = new FileInputStream(propertiesFileName);
                props.load(fis);
                RenameMe renameMe = new RenameMe(props);
                switch (runningMode) {
                    case "numeric" -> renameMe.batchRename(folder, "base");
                    case "presuf" -> renameMe.renameAll(folder);
                    default ->
                            throw new IllegalArgumentException("Running mode unknown, choose between numeric and presuf");
                }
            } catch (IOException e) {
                System.out.printf("Erreur : Le fichier %s n'existe pas%n", propertiesFileName);
            }
        }
    }
}