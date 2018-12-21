package org.sample.program;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class CrawlerMy implements Runnable {

    private String URL;
    private MyWebCrawlersManager holder;
    private int id;
    private int depth;

    CrawlerMy(String URL, MyWebCrawlersManager holder, int depth, int id) {
        this.URL = URL;
        this.holder = holder;
        this.depth = depth;
        this.id = id;
    }

    @Override
    public void run() {
        Document document;
        try {
            document = Jsoup.connect(URL).get();
            if(depth > 0) {
                Elements linksOnPage = document.select("a[href]");

                for (Element page : linksOnPage) {
                    String link = page.attr("abs:href");
                    if (!holder.isVisited(link)){
//                        System.out.println(link);
                        holder.addNewThread(link, depth - 1);
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

