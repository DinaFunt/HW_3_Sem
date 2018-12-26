package org.sample.program;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyThreadPool implements Executor {

    private Thread[] threads;
    private MyConcurrentSkipList<Runnable> queue;
    private Lock lock;
    private boolean shutDown;

    public MyThreadPool(int numOfThread) {
        threads = new Thread[numOfThread];
        lock = new ReentrantLock();
        queue = new MyConcurrentSkipList<>();
        shutDown = false;

        for (int i = 0; i < numOfThread; i++) {
            threads[i] = new Thread(new PoolTask());
            threads[i].start();
        }
    }

    private final class PoolTask implements Runnable {

        @Override
        public void run() {

            while (!shutDown) {
              Runnable task = null;
                try {
                    lock.lock();
                    if (!queue.isEmpty()) {
                        task = queue.first();
                    }
                } finally {
                    lock.unlock();
                }

                if (task != null) {
                    task.run();
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void execute(Runnable task) {
        queue.add(task);
    }

    public void shutdown() {
        shutDown = true;
    }
}