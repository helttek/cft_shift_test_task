package cftShift.fileFilter;

import java.util.concurrent.ArrayBlockingQueue;

public class Consumer implements Runnable {
    private Config cfg;
    private ArrayBlockingQueue<Object> q;
    private static Statistics statistics;

    public Consumer(ArrayBlockingQueue<Object> queue, Config config, Statistics stats) {
        q = queue;
        cfg = config;
        statistics = stats;
    }

    @Override
    public void run() {
        System.out.println("Consumer started");
        while (!Thread.interrupted() && !q.isEmpty()) {
        }
    }
}
