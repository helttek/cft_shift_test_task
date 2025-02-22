package cftShift.fileFilter;

import java.util.concurrent.LinkedBlockingQueue;

public class Config {
    private static final String DEFAULT_PATH = ".";
    private static final String DEFAULT_PREFIX = "";
    private static final int DEFAULT_QUEUE_CAPACITY = 10;

    private static String path;
    private static String prefix;
    private static boolean prependToExisting;
    private static boolean shortStatistics;
    private static boolean fullStatistics;
    private static LinkedBlockingQueue<String> inputFiles;

    public Config() {
        path = null;
        prefix = null;
        prependToExisting = false;
        shortStatistics = false;
        fullStatistics = false;
        inputFiles = new LinkedBlockingQueue<String>(DEFAULT_QUEUE_CAPACITY);
    }

    public String getPath() {
        return path;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean prependToExisting() {
        return prependToExisting;
    }

    public boolean showShortStatistics() {
        return shortStatistics;
    }

    public boolean showFullStatistics() {
        return fullStatistics;
    }

    public void setPath(String str) {
        path = str;
    }

    public void setPrefix(String str) {
        prefix = str;
    }

    public void setPrependToExisting(boolean status) {
        prependToExisting = status;
    }

    public void setShortStatistics(boolean status) {
        shortStatistics = status;
    }

    public void setFullStatistics(boolean status) {
        fullStatistics = status;
    }

    public void addInputFile(String file) {
        try {
            inputFiles.put(file);
        } catch (InterruptedException e) {
            System.out.println("Error adding file: " + file + ": " + e.getMessage());
        }
    }

    public void setPathToDefault() {
        path = DEFAULT_PATH;
    }

    public void setPrefixToDefault() {
        prefix = DEFAULT_PREFIX;
    }

    public String getInputFile() throws InterruptedException {
        try {
            return inputFiles.take();
        } catch (InterruptedException e) {
            return null;
        }
    }

    public boolean noInputFiles() {
        return inputFiles.isEmpty();
    }
}
