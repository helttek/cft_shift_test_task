package cftShift.fileFilter;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        FileContentSorter fcs;
        try {
            fcs = new FileContentSorter(args);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return;
        }
        fcs.run();
    }
}
