package org.sample.program;

public class MyWebCrawlersManager implements IBaseCrawlerManager{

    private MyConcurrentSkipList<String> visited;

    private MyThreadPool threadPool;
    private MyConcurrentSkipList<Integer> workThreads;
    private static final int MAX_THREADS = 4;

    private String URL;
    private int depth;


    public MyWebCrawlersManager(int threadsCount) {
        visited = new MyConcurrentSkipList<>();
        threadPool = new MyThreadPool(threadsCount);
        workThreads = new MyConcurrentSkipList<>();

        this.depth = depth;
        this.URL = URL;
    }

    public void addNewThread(String link, int depth) {
        int id = workThreads.size() + 1;
        Runnable task = new CrawlerMy(link, this, depth, id);
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