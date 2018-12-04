package com.jenkov;

import java.util.concurrent.locks.Lock;

class CounterThread extends Thread {
    private Counter counter;
    private Lock p;

    public CounterThread(Counter counter, Lock p) {
        this.counter = counter;
        this.p = p;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000000; i++) {
            p.lock();
            try {
                counter.increaseCounter();
            }
            finally {
                p.unlock();
            }
        }
        System.out.println("Finished - " + Thread.currentThread().getId());
    }
}