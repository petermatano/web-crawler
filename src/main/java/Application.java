import java.time.Duration;
import java.time.Instant;

public class Application {

    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 2) {
            System.out.println("Please provide site to crawl");
            System.exit(1);
        }

        String site = args[0];
        String externalLinkFilter = null;
        if (args.length == 2) {
            externalLinkFilter = args[1];
        }
        SimpleWebCrawler simpleWebCrawler = new SimpleWebCrawler(site, externalLinkFilter);
        Instant start = Instant.now();
        simpleWebCrawler.crawlSite();
        long timeElapsedInMillis = Duration.between(start, Instant.now()).toMillis();
        simpleWebCrawler.printSummary();
        simpleWebCrawler.exportResultsToFile();
        System.out.println("Time to Crawl: " + formatElapsedTime(timeElapsedInMillis));
    }

    public static String formatElapsedTime(long milliseconds) {
        String format = String.format("%%0%dd", 2);
        long elapsedTime = milliseconds / 1000;
        String seconds = String.format(format, elapsedTime % 60);
        String minutes = String.format(format, (elapsedTime % 3600) / 60);
        String hours = String.format(format, elapsedTime / 3600);
        String time = hours + ":" + minutes + ":" + seconds;
        return time;
    }
}
