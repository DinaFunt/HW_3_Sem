package org.sample.program;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SystemWebCrawlersManager implements IBaseCrawlerManager {

    private Set<String> visited;

    private ExecutorService threadPool;
    private Set<Integer> workThreads;
    private static final int MAX_THREADS = 4;

    private String URL;
    private int depth;

    public SystemWebCrawlersManager(int threadCount) {
        visited = new ConcurrentSkipListSet<>();
        threadPool = Executors.newFixedThreadPool(threadCount);
        workThreads = new ConcurrentSkipListSet<>();

        this.depth = depth;
        this.URL = URL;
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