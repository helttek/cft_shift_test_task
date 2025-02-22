package cftShift.fileFilter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

public class Producer implements Runnable {
    private ArrayBlockingQueue<Object> q;
    private Config cfg;

    public Producer(ArrayBlockingQueue<Object> queue, Config config) {
        q = queue;
        cfg = config;
    }

    @Override
    public void run() {

        try {
            while (!Thread.interrupted() && !cfg.noInputFiles()) {
                String inFile = cfg.getInputFile();
                BufferedReader bufferedReader = new BufferedReader(new FileReader(inFile));
                System.out.println("Producer opened file" + inFile);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    processLine(line);
                }
            }
            Thread.sleep(1000);
        } catch (InterruptedException | IOException e) {
            System.out.println("Producer interrupted");
        }
    }

    private void processLine(String line) {
        System.out.println(line);
        try {
            try {
                q.put(Double.parseDouble(line));
                return;
            } catch (NumberFormatException ignored) {
            }
            try {
                q.put(Integer.parseInt(line));
                return;
            } catch (NumberFormatException ignored) {
            }
            q.put(line);
        } catch (InterruptedException e) {
            System.out.println("ERROR: Failed to process line [" + line + "] - " + e.getMessage());
        }
    }
}
