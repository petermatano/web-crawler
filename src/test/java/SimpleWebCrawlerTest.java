import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimpleWebCrawlerTest {

    @Test(expected = MalformedURLException.class)
    public void invalidSite() throws Exception {
        new SimpleWebCrawler("invalid-url");
    }

    @Test
    public void isExternalLink() throws MalformedURLException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        SimpleWebCrawler simpleWebCrawler = new SimpleWebCrawler("https://google.com");
        Method method = SimpleWebCrawler.class.getDeclaredMethod("isExternalLink", URL.class);
        method.setAccessible(true);
        boolean isExternal = (boolean) method.invoke(simpleWebCrawler, new URL("https://some.other.site.com"));
        assertTrue(isExternal);
    }

    @Test
    public void isNotExternalLink() throws MalformedURLException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        SimpleWebCrawler simpleWebCrawler = new SimpleWebCrawler("https://google.com");
        Method method = SimpleWebCrawler.class.getDeclaredMethod("isExternalLink", URL.class);
        method.setAccessible(true);
        boolean isExternal = (boolean) method.invoke(simpleWebCrawler, new URL("https://google.com"));
        assertFalse(isExternal);
    }

    @Test
    public void isValidLink() throws MalformedURLException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        SimpleWebCrawler simpleWebCrawler = new SimpleWebCrawler("https://google.com");
        Method method = SimpleWebCrawler.class.getDeclaredMethod("isValidLink", String.class);
        method.setAccessible(true);
        boolean isValid = (boolean) method.invoke(simpleWebCrawler, "https://some.other.site.com");
        assertTrue(isValid);
    }

    @Test
    public void isNotValidLink() throws MalformedURLException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        SimpleWebCrawler simpleWebCrawler = new SimpleWebCrawler("https://google.com");
        Method method = SimpleWebCrawler.class.getDeclaredMethod("isValidLink", String.class);
        method.setAccessible(true);
        boolean isValid = (boolean) method.invoke(simpleWebCrawler, "invalid-site");
        assertFalse(isValid);
    }

    //TODO: Write additional tests to mock Jsoup
}
