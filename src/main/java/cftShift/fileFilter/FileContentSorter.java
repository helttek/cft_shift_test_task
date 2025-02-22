package cftShift.fileFilter;

import java.io.FileNotFoundException;

public class FileContentSorter {
    private Config config;
    private Sorter sorter;

    public FileContentSorter(String[] args) {
        ArgsValidator ArgsValidator = new ArgsValidator();
        config = ArgsValidator.validate(args);
        if (config == null) {
            throw new RuntimeException("Failed to create config file.");
        }
        sorter = new Sorter(config);
    }

    /*
     * TODO:
     * - add keyboard interrupt hadlers
     * - add descriptions to all functions
     */

    /**
     * @param args
     * @return void
     * @throws smth
     */

    public void run() {
        Statistics stats = null;
        try {
            stats = sorter.sort();
        } catch (Exception e) {
            System.out.println("ERROR: Failed to sort the files (" + e.getMessage() + ").");
        }
        if (stats == null) {
            System.out.println("ERROR: Failed to acquire statistics.");
            return;
        }
        if (config.showFullStatistics()) {
            stats.printFull();
        }
        if (config.showShortStatistics()) {
            stats.printShort();
        }
    }
}
