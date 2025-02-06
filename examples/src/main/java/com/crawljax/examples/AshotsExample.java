package com.crawljax.examples;

import com.crawljax.util.FSUtils;
import com.crawljax.util.UrlUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import ru.yandex.qatools.ashot.shooting.ShootingStrategy;
import ru.yandex.qatools.ashot.shooting.SimpleShootingStrategy;
import ru.yandex.qatools.ashot.shooting.ViewportPastingDecorator;

public class AshotsExample {

    private static void saveImage(BufferedImage image, String name) {
        try {

            String folderName = "out" + "/ashotsscreenshots/";
            FSUtils.directoryCheck(folderName);
            ImageIO.write(image, "PNG", new File(folderName + name + ".png"));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        String os = System.getProperty("os.name").toLowerCase();
        FirefoxOptions options = new FirefoxOptions();

        System.setProperty("webdriver.gecko.driver", UrlUtils.gecko_driver);
        options.setBinary(UrlUtils.firefox_binary);
        FirefoxDriver driver = new FirefoxDriver(options);

        driver.navigate().to("http://testcue.com/crawljax-demo-full/");
        // ((JavascriptExecutor) driver).executeScript("window.resizeTo(1024, 768);");

        saveImage(takeshot(driver), "myhomepage8");

        driver.navigate().to("http://phptravels.com/demo/");

        saveImage(takeshot(driver), "phptravel8");

        driver.close();
    }

    private static BufferedImage takeshot(FirefoxDriver driver) {
        ShootingStrategy pasting = new ViewportPastingDecorator(new SimpleShootingStrategy()).withScrollTimeout(500);
        BufferedImage image = pasting.getScreenshot(driver);

        // BufferedImage image = new
        // AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000))
        // .takeScreenshot(driver).getImage();

        // Thread.sleep(500);
        // File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(src, new File("out/ashotsscreenshots/error.png"));

        // BufferedImage image = pasting.getScreenshot(driver);
        // BufferedImage image = new AShot().takeScreenshot(driver).getImage();

        // image = new AShot().takeScreenshot(driver).getImage();

        // image = new
        // AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver).getImage();

        return image;
    }
}
