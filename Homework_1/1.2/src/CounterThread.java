class CounterThread extends Thread {
    private Counter counter;
    private Peterson p;
    private int id;

    public CounterThread(Counter counter, Peterson p, int id) {
        this.counter = counter;
        this.p = p;
        this.id = id;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000000; i++) {
            p.lock(id);
            try {
                counter.increaseCounter();
            }
            finally {
                p.unlock(id);
            }
        }
        System.out.println("Finished - " + Thread.currentThread().getId());
    }
}