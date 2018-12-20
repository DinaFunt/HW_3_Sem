import java.util.concurrent.*;

public class WebCrawlersManager {

    private MyConcurrentSkipList<String> visited;

    private ExecutorService threadPool;
    private MyConcurrentSkipList<Integer> workThreads;
    private static final int MAX_THREADS = 4;

     WebCrawlersManager() {
        visited = new MyConcurrentSkipList<>();
        threadPool = Executors.newFixedThreadPool(MAX_THREADS);
        workThreads = new MyConcurrentSkipList<>();
    }

    public void addNewThread(String link, int depth) {
        int id = workThreads.size() + 1;
        Runnable task = new Crawler(link, this, depth, id);
        visited.add(link);

        workThreads.add(id);
        threadPool.execute(task);
    }

    public boolean isVisited(String link) {
        return visited.contains(link);
    }

    public void endOfThread(Integer id) {
        workThreads.remove(id);

        if (workThreads.isEmpty()) {
            threadPool.shutdown();

            System.out.println("Pool of threads is shut down");
        }
    }
}