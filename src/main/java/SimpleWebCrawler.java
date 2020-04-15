import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleWebCrawler {

    private URL site;
    private Set<String> visitedLinks = Collections.newSetFromMap(new ConcurrentHashMap<>()); //new HashSet<>();
    private Set<String> externalLinks = Collections.newSetFromMap(new ConcurrentHashMap<>()); //new HashSet<>();
    private Set<String> staticContentLinks = Collections.newSetFromMap(new ConcurrentHashMap<>()); //new HashSet<>();
    private Set<String> unprocessedLinks = Collections.newSetFromMap(new ConcurrentHashMap<>()); //new HashSet<>();

    public SimpleWebCrawler(String site) throws MalformedURLException {
        this.site = new URL(site);
    }

    public void crawlSite() {
        System.out.println("Crawling the following site: " + this.site.toString());
        retrieveLinks(this.site);
    }

    private void retrieveLinks(URL link) {
        if (visitedLinks.add(getLinkHash(link))) {
            try {
                Elements elements = Jsoup.parse(link, 30000).select("[href], [src]");
                elements.stream()
                        .parallel()
                        .forEach(element -> processElement(link, element));
            } catch (Exception e) {
                unprocessedLinks.add(link.toExternalForm());
            }
        }
    }

    private String getLinkHash(URL link) {
        return (link.getProtocol() + "://" + link.getHost() + link.getPath()).replaceAll("/$", "");
    }

    private boolean isSameProtocol(URL link) {
        return link.getProtocol().equals(this.site.getProtocol());
    }

    private boolean isExternalLink(URL link) {
        return !this.site.getHost().equals(link.getHost());
    }

    private boolean isHTMLContent(URL link) {
        try {
            String contentType = Jsoup.connect(link.toExternalForm()).timeout(30000).execute().contentType();
            return contentType.contains("html");
        } catch (IOException e) {
            return false;
        }
    }

    private boolean isValidLink(String link) {
        try {
            new URL(link);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private void processElement(URL parentLink, Element element) {
        try {
            String attributeKey = element.is("[href]") ? "href" : "src";
            String attributeValue = element.absUrl(attributeKey);
            if (!isValidLink(attributeValue)) {
                return;
            }
            URL elementLink = new URL(attributeValue);
            if (visitedLinks.contains(getLinkHash(elementLink))) {
                return;
            }
            if (!isSameProtocol(elementLink)) {
                return;
            }
            if (isExternalLink(elementLink)) {
                externalLinks.add(elementLink.toExternalForm());
                return;
            }
            if (!isHTMLContent(elementLink)) {
                staticContentLinks.add(elementLink.toExternalForm());
                return;
            }
            System.out.println("Crawling next link: " + elementLink.toString());
            retrieveLinks(elementLink);
        } catch (Exception e) {
            unprocessedLinks.add(parentLink.toExternalForm());
        }
    }

    public void printSummary() {
        System.out.println("Links Visited Total: " + visitedLinks.size());
        System.out.println("External Links Total: " + externalLinks.size());
        System.out.println("Static Content Links Total: " + staticContentLinks.size());
        System.out.println("Unprocessed Links Total: " + unprocessedLinks.size());
    }

    public void exportResultsToFile() {
        Path path = Paths.get("crawler-results.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"))) {
            writer.write("****** Links Visited Total: " + visitedLinks.size() + " ******");
            writer.newLine();
            for (String s : visitedLinks) {
                writer.write(s);
                writer.newLine();
            }
            writer.newLine();
            writer.write("****** External Links Total: " + externalLinks.size() + " ******");
            writer.newLine();
            for (String s : externalLinks) {
                writer.write(s);
                writer.newLine();
            }
            writer.newLine();
            writer.write("****** Static Content Link Total: " + staticContentLinks.size() + " ******");
            writer.newLine();
            for (String s : staticContentLinks) {
                writer.write(s);
                writer.newLine();
            }
            writer.newLine();
            writer.write("****** Unprocessed Links Total: " + unprocessedLinks.size() + " ******");
            writer.newLine();
            for (String s : unprocessedLinks) {
                writer.write(s);
                writer.newLine();
            }
            System.out.println("Results: " + path.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Unable to export results");
        }
    }

}
