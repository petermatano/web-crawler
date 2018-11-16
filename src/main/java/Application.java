public class Application {

    public static void main(String[] args) throws Exception {
        if (args == null || args.length != 1) {
            System.out.println("Please provide site to crawl");
            System.exit(1);
        }

        String site = args[0];
        SimpleWebCrawler simpleWebCrawler = new SimpleWebCrawler(site);
        simpleWebCrawler.crawlSite();
        simpleWebCrawler.printSummary();
        simpleWebCrawler.exportResultsToFile();
    }
}
