package cftShift.fileFilter;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ArrayBlockingQueue;

public class Sorter {
    private static final int QUEUE_CAPACITY = 1024;
    private static final int CORE_POOL_SIZE = 2;
    private static final int THREAD_POOL_TIMEOUT_SECONDS = 60;
    private static final int DEFAULT_CONSUMERS_NUMBER = 3;
    private static final int DEFAULT_PRODUCERS_NUMBER = 5;

    private static Config cfg;
    private int numberOfProducers;
    private int numberOfConsumers;
    private ArrayBlockingQueue<Object> buffer;//this buffer will store objects (String/int/double) that the producers have extracted
    private ThreadPoolExecutor executor;
    private Statistics stats;

    public Sorter(Config config) {
        this(config, DEFAULT_CONSUMERS_NUMBER, DEFAULT_PRODUCERS_NUMBER);
    }

    public Sorter(Config config, int consumersNum, int producersNum) {
        //check if number of producers and consumers is valid
        if (consumersNum > 0 && producersNum > 0) {
            numberOfConsumers = consumersNum;
            numberOfProducers = producersNum;
        } else {
            numberOfConsumers = DEFAULT_CONSUMERS_NUMBER;
            numberOfProducers = DEFAULT_PRODUCERS_NUMBER;
        }
        cfg = config;
        buffer = new ArrayBlockingQueue<Object>(QUEUE_CAPACITY);
        executor = new ThreadPoolExecutor(CORE_POOL_SIZE, numberOfProducers + numberOfConsumers, THREAD_POOL_TIMEOUT_SECONDS, TimeUnit.SECONDS,
                new SynchronousQueue<>());
        stats = new Statistics();
    }

    /*
    TODO:
    - use static to share variables across threads (rewrite at least the statistics part)
    */

    public Statistics sort() {
        //add consumer and producer tasks in one loop
        if (numberOfProducers > numberOfConsumers) {
            for (int i = 0; i < numberOfProducers; i++) {
                executor.submit(new Producer(buffer, cfg));
                if (i < numberOfConsumers) {
                    executor.submit(new Consumer(buffer, cfg, stats));
                }
            }
        } else {
            for (int i = 0; i < numberOfConsumers; i++) {
                executor.submit(new Consumer(buffer, cfg, stats));
                if (i < numberOfProducers) {
                    executor.submit(new Producer(buffer, cfg));
                }
            }
        }
        executor.shutdown();
        try {
            if (!executor.awaitTermination(THREAD_POOL_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                System.err.println("Threads were tasked to shutdown, but the timeout has lapsed?/");
            }
            return stats;
        } catch (InterruptedException e) {
            System.err.println("interrupted exception handler invoked");
            return null;
        }
    }
}
