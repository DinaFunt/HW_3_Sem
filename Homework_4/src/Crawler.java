import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Crawler implements Runnable {

    private String URL;
    private WebCrawlersManager holder;
    private int id;
    private int depth;
    private Lock lock;

    Crawler(String URL, WebCrawlersManager holder, int depth, int id) {
        this.URL = URL;
        this.holder = holder;
        this.depth = depth;
        this.id = id;
        lock = new ReentrantLock();
    }

    @Override
    public void run() {
        Document document;
        try {
            document = Jsoup.connect(URL).get();
            if (depth > 0) {
                Elements linksOnPage = document.select("a[href]");

                for (Element page : linksOnPage) {
                    String link = page.attr("abs:href");
                    try {
                        lock.lock();
                        if (!holder.isVisited(link)) {
//                        System.out.println(link);
                            holder.addNewThread(link, depth - 1);
                        }
                    } finally {
                        lock.unlock();
                    }
                }
            }
            downloadPage(document);

            holder.endOfThread(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadPage(Document document) throws Exception{
        File f = new File("pages/page - "+ id + ".html");
        BufferedWriter writer = new BufferedWriter(new FileWriter(f));
        writer.write(document.outerHtml());
    }
}

