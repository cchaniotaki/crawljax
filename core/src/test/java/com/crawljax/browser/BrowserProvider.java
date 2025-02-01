package com.crawljax.browser;

import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.google.common.base.Strings;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.rules.ExternalResource;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowserProvider extends ExternalResource {

    private static final Logger LOG = LoggerFactory.getLogger(BrowserProvider.class);
    private List<RemoteWebDriver> usedBrowsers;

    public static EmbeddedBrowser.BrowserType getBrowserType() {
        // typically read from the profile in pom.xml
        String browser = System.getProperty("test.browser");
        if (!Strings.isNullOrEmpty(browser)) {
            return EmbeddedBrowser.BrowserType.valueOf(browser);
        } else {
            return BrowserType.CHROME_HEADLESS;
        }
    }

    @Override
    protected void before() {
        usedBrowsers = new LinkedList<>();
    }

    public EmbeddedBrowser newEmbeddedBrowser() {
        return WebDriverBackedEmbeddedBrowser.withDriver(newBrowser());
    }

    /**
     * Return the browser.
     */
    public RemoteWebDriver newBrowser() {
        RemoteWebDriver driver;
        FirefoxOptions foptions = new FirefoxOptions();
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            System.setProperty(
                    "webdriver.edge.driver", "C:\\Users\\nikit\\Desktop\\Krawler\\drivers\\msedgedriver.exe");
            System.setProperty(
                    "webdriver.gecko.driver", "C:\\Users\\nikit\\Desktop\\Krawler\\drivers\\geckodriver.exe");
            foptions.setBinary("C:\\Users\\nikit\\AppData\\Local\\Mozilla Firefox\\firefox.exe");
            System.setProperty(
                    "webdriver.chrome.driver", "C:\\Users\\nikit\\Desktop\\Krawler\\drivers\\chromedriver.exe");

        } else if (os.contains("mac")) {
            System.setProperty(
                    "webdriver.edge.driver",
                    "/Users/christinechaniotaki/Documents/Krawler-study/krawler-paper/drivers/mac/msedgedriver");
            System.setProperty(
                    "webdriver.gecko.driver",
                    "/Users/christinechaniotaki/Documents/Krawler-study/krawler-paper/drivers/mac/geckodriver");
            System.setProperty(
                    "webdriver.chrome.driver",
                    "/Users/christinechaniotaki/Documents/Krawler-study/krawler-paper/drivers/mac/chromedriver");

        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            System.setProperty("webdriver.edge.driver", "/home/cchaniot/Desktop/Krawler-Study/drivers/msedgedriver");
            System.setProperty("webdriver.gecko.driver", "/home/cchaniot/Desktop/Krawler-Study/drivers/geckodriver");
            System.setProperty("webdriver.chrome.driver", "/home/cchaniot/Desktop/Krawler-Study/drivers/chromedriver");
        }
        // TODO: add your own path
        switch (getBrowserType()) {
            case CHROME:
                driver = new ChromeDriver(createChromeOptions());
                break;
            case CHROME_HEADLESS:
                ChromeOptions optionsChrome = createChromeOptions();
                optionsChrome.addArguments("--headless");
                optionsChrome.addArguments("--disable-web-security");
                optionsChrome.addArguments("--allow-file-access-from-files");
                driver = new ChromeDriver(optionsChrome);
                break;
            case FIREFOX:
                driver = new FirefoxDriver(foptions);
                break;
            case FIREFOX_HEADLESS:
                foptions.setCapability("marionette", true);
                foptions.addArguments("--headless"); // Enable headless mode
                driver = new FirefoxDriver(foptions);
                break;
            default:
                throw new IllegalStateException("Unsupported browser type " + getBrowserType());
        }

        /* Store the browser as a used browser so we can clean it up later. */
        usedBrowsers.add(driver);

        driver.manage()
                .timeouts()
                .implicitlyWait(5, TimeUnit.SECONDS)
                .pageLoadTimeout(30, TimeUnit.SECONDS)
                .setScriptTimeout(30, TimeUnit.SECONDS);

        driver.manage().deleteAllCookies();

        return driver;
    }

    private static ChromeOptions createChromeOptions() {
        ChromeOptions optionsChrome = new ChromeOptions();
        optionsChrome.addArguments("--remote-allow-origins=*");
        optionsChrome.addArguments("--disable-web-security");
        optionsChrome.addArguments("--allow-running-insecure-content");
        optionsChrome.addArguments("--disable-gpu");
        optionsChrome.addArguments("--ignore-certificate-errors");
        optionsChrome.addArguments("--disable-extensions");
        optionsChrome.addArguments("--no-sandbox");
        optionsChrome.addArguments("--disable-dev-shm-usage");
        optionsChrome.addArguments("--allow-file-access-from-files");
        optionsChrome.addArguments("--disable-web-security");
        return optionsChrome;
    }

    @Override
    protected void after() {
        for (RemoteWebDriver browser : usedBrowsers) {
            try {
                /* Make sure we clean up properly. */
                if (!browser.toString().contains("(null)")) {
                    browser.quit();
                }

            } catch (RuntimeException e) {
                LOG.warn("Could not close the browser: {}", e.getMessage());
            }
        }
    }
}
