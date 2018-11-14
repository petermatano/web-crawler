import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;

public class WebCrawlerDriver {

    private String domain;
    private HashSet<String> visitedUrls = new HashSet<>();
    private HashSet<String> discoveredUrls = new HashSet<>();

    public WebCrawlerDriver(String domain) {
        if (!isLinkValid(domain)) {
            throw new IllegalArgumentException("Invalid domain");
        }
        this.domain = domain;
    }

    public void crawlDomain(){
        retrievePageLinks(this.domain);
        discoveredUrls.forEach(System.out::println);
    }

    private void retrievePageLinks(String url) {
        if (!visitedUrls.contains(url)) {
            try {
                visitedUrls.add(url);
                Document document = Jsoup.connect(url).get();
                Elements links = document.select("a[href]");
                links.forEach(link -> {
                    String linkUrl = link.attr("abs:href");
                    discoveredUrls.add(linkUrl);
                    if (shouldFollowLink(linkUrl)) {
                        retrievePageLinks(url);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean shouldFollowLink(String url) {
        return isLinkValid(url) && !isExternalLink(url);
    }

    private boolean isLinkValid(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isExternalLink(String url) {
        return !url.startsWith(this.domain);
    }


    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please enter valid input parameter");
            System.exit(1);
        }
        String domain = args[0];
        WebCrawlerDriver webCrawlerDriver = new WebCrawlerDriver(domain);
        webCrawlerDriver.crawlDomain();
    }
}
