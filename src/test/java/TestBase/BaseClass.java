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
	public Logger logger;//log4j
	public Properties p;

	@BeforeClass(groups= {"Sanity","Regression","Master"})
	@Parameters({"os","browser"})
	public void setup(String os,String br) throws IOException {
		
		FileReader file=new FileReader("./src//test//resources//config.properties");
		p=new Properties();
		p.load(file);
		
		logger=LogManager.getLogger(this.getClass());
		
		if(p.getProperty("execution").equalsIgnoreCase("remote")) {

		    DesiredCapabilities capabilities = new DesiredCapabilities();

		    // OS
		    if(os.equalsIgnoreCase("windows")) {
		        capabilities.setPlatform(Platform.WIN11);
		    }
		    else if(os.equalsIgnoreCase("mac")) {
		        capabilities.setPlatform(Platform.MAC);
		    }
		    else {
		        System.out.println("No Matching os");
		        return;
		    }

		    // Browser + Headless Options
		    if (br.equalsIgnoreCase("chrome")) {

		        ChromeOptions options = new ChromeOptions();
		        options.addArguments("--headless=new");          // Headless mode
		        options.addArguments("--no-sandbox");            // Required in Docker
		        options.addArguments("--disable-dev-shm-usage"); // CI stability

		        options.merge(capabilities);

		        String seleniumUrl = System.getenv("SELENIUM_REMOTE_URL");
		        if (seleniumUrl == null || seleniumUrl.isEmpty()) {
		            seleniumUrl = "http://localhost:4444/wd/hub"; // fallback for local grid
		        }

		        driver = new RemoteWebDriver(new URL(seleniumUrl), options);

		    } else if (br.equalsIgnoreCase("edge")) {

		        EdgeOptions options = new EdgeOptions();
		        options.addArguments("--headless=new");
		        options.addArguments("--no-sandbox");
		        options.addArguments("--disable-dev-shm-usage");

		        options.merge(capabilities);

		        String seleniumUrl = System.getenv("SELENIUM_REMOTE_URL");
		        if (seleniumUrl == null || seleniumUrl.isEmpty()) {
		            seleniumUrl = "http://localhost:4444/wd/hub";
		        }

		        driver = new RemoteWebDriver(new URL(seleniumUrl), options);

		    } else {
		        System.out.println("No Matching Browser");
		        return;
		    }
		}

		else if(p.getProperty("execution").equalsIgnoreCase("local")) {
		
		switch(br.toLowerCase()) {
		case "chrome": driver=new ChromeDriver();break;
		case "edge": driver=new EdgeDriver();break;
		default:System.out.println("Invalid Parameter");return;
		}
		}
		
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		driver.get(p.getProperty("appURL1"));
		driver.manage().window().maximize();
	}
	@AfterClass(groups= {"Sanity","Regression","Master"})
	public void teardown() {
		driver.quit();
		
	}
	
	public String randomeString() {
	    String generatedstring = RandomStringUtils.randomAlphabetic(5);
	    return generatedstring;
	}

	public String randomeNumber() {
	    String generatednumber = RandomStringUtils.randomNumeric(10);
	    return generatednumber;
	}

	public String randomeAlphaNumeric() {
	    String generatedstring = RandomStringUtils.randomAlphabetic(3);
	    String generatednumber = RandomStringUtils.randomNumeric(3);
	    return (generatedstring + "@" + generatednumber);
	}
	public String captureScreen(String tname) throws IOException {
	    String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

	    TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
	    File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

	    String targetFilePath = System.getProperty("user.dir") + "\\screenshots\\" + tname + "_" + timeStamp + ".png";
	    File targetFile = new File(targetFilePath);

	    sourceFile.renameTo(targetFile);

	    return targetFilePath;
	}

}
