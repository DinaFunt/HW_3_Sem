public class Main
{
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        Peterson p = new Peterson();

        for(int i = 0; i < 2; i++) {
            CounterThread ct = new CounterThread(counter, p, i);
            ct.start();
        }

        Thread.sleep(1000);

        System.out.println("Counter:" + counter.getCounter());
    }
}
