import java.util.Set;
import java.util.concurrent.*;

public class WebCrawlersManager {

    private Set<String> visited;

    private ExecutorService threadPool;
    private Set<Integer> workThreads;
    private static final int MAX_THREADS = 4;

     WebCrawlersManager() {
        visited = new ConcurrentSkipListSet<>();
        threadPool = Executors.newFixedThreadPool(MAX_THREADS);
        workThreads = new ConcurrentSkipListSet<>();
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