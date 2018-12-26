public class Main {

    public static void main(String[] args) {
        String URL = "http://www.mkyong.com/";
        int depth = 1;

        WebCrawlersManager crawler = new WebCrawlersManager();
        crawler.addNewThread(URL, depth);
    }
}