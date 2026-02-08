package TestBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

public class BaseClass {

    public static WebDriver driver;
    public Logger logger;
    public Properties p;

    @BeforeClass(groups = {"Sanity", "Regression", "Master"})
    @Parameters({"os", "browser"})
    public void setup(String os, String br) throws IOException {

        FileReader file = new FileReader("./src/test/resources/config.properties");
        p = new Properties();
        p.load(file);

        logger = LogManager.getLogger(this.getClass());

        if (p.getProperty("execution").equalsIgnoreCase("remote")) {

            DesiredCapabilities capabilities = new DesiredCapabilities();

            // OS optional for CI
            if (os != null) {
                if (os.equalsIgnoreCase("windows")) {
                    capabilities.setPlatform(Platform.WIN11);
                } else if (os.equalsIgnoreCase("mac")) {
                    capabilities.setPlatform(Platform.MAC);
                }
            }

            if (br.equalsIgnoreCase("chrome")) {

                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless=new");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--window-size=1920,1080");

                options.merge(capabilities);

                String hubUrl = System.getenv("SELENIUM_REMOTE_URL");
                if (hubUrl == null || hubUrl.isEmpty()) {
                    hubUrl = System.getProperty("selenium.remote.url", "http://selenium:4444/wd/hub");
                }

                driver = new RemoteWebDriver(new URL(hubUrl), options);

            } else if (br.equalsIgnoreCase("edge")) {

                EdgeOptions options = new EdgeOptions();
                options.addArguments("--headless=new");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--window-size=1920,1080");

                options.merge(capabilities);

                String hubUrl = System.getenv("SELENIUM_REMOTE_URL");
                if (hubUrl == null || hubUrl.isEmpty()) {
                    hubUrl = System.getProperty("selenium.remote.url", "http://selenium:4444/wd/hub");
                }

                driver = new RemoteWebDriver(new URL(hubUrl), options);

            } else {
                throw new RuntimeException("No Matching Browser");
            }
        }

        else if (p.getProperty("execution").equalsIgnoreCase("local")) {

            if (br.equalsIgnoreCase("chrome")) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless=new");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--window-size=1920,1080");
                driver = new ChromeDriver(options);
            }
            else if (br.equalsIgnoreCase("edge")) {
                EdgeOptions options = new EdgeOptions();
                options.addArguments("--headless=new");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--window-size=1920,1080");
                driver = new EdgeDriver(options);
            }
            else {
                throw new RuntimeException("Invalid browser");
            }
        }

        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get(p.getProperty("appURL1"));
    }

    @AfterClass(groups = {"Sanity", "Regression", "Master"})
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Optional helpers
    public String randomString() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public String randomNumber() {
        return RandomStringUtils.randomNumeric(10);
    }

    public String randomAlphaNumeric() {
        return RandomStringUtils.randomAlphabetic(3) + "@" + RandomStringUtils.randomNumeric(3);
    }

    public String captureScreen(String tname) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);

        String targetPath = System.getProperty("user.dir") + "/screenshots/" + tname + "_" + timeStamp + ".png";
        File target = new File(targetPath);
        src.renameTo(target);

        return tar
