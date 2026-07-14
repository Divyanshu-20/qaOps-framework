package com.automation.orangehrm.base;

import com.automation.orangehrm.actiondriver.ActionDriver;
import com.automation.orangehrm.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class BaseTest {

    protected static Properties prop;
    private static WebDriver driver;
    private static ActionDriver actionDriver;
    public static final Logger logger = LoggerManager.getLogger(BaseTest.class); //you are not creating new Logger or something, just storing (Logger) what you got from another class

    /*
    public static WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        BaseTest.driver = driver;
    }
     */

    public static Properties getProp() {
        return prop;
    }

    @BeforeMethod
    public void setup() throws IOException {
        logger.info("Setting up WebDriver for test: {}", this.getClass().getSimpleName());
        launchBrowser();
        configureBrowser();
        staticWait(2);

        //Initialize ActionDriver (SINGLETON PATTERN)
        if(actionDriver == null) {
            actionDriver = new ActionDriver(driver);
            logger.info("ActionDriver instance created");
        }
    }

    @BeforeSuite
    public void loadConfig() throws IOException {
        //Load config file
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        prop.load(fis);
    }

    private void launchBrowser(){
        //WebDriver Initialization
        String browser = prop.getProperty("browser");
        logger.debug("Launching browser: {}", browser);

        switch (browser.toLowerCase()) {
            case "chrome":
                driver = new ChromeDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            default:
                logger.error("Browser not supported: {}", browser);
                throw new IllegalArgumentException("Browser Not Supported " + browser);
        }
    }


    //Configure Browser settings
    public void configureBrowser() {
        //Implicit wait
        int wait = Integer.parseInt(prop.getProperty("implicitWait"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(wait));
        logger.debug("Implicit wait set to {} seconds", wait);

        //Maximize Driver
        driver.manage().window().maximize();

        //Navigate to URL
        try {
            driver.get(prop.getProperty("url"));
            logger.info("Navigated to URL: {}", prop.getProperty("url"));
        } catch (Exception e) {
            logger.error("Failed to navigate to the URL: {}", e.getMessage());
        }
    }

    @AfterMethod
    public void tearDown() {
        try {
            if (driver != null) {
                driver.quit();
                logger.info("WebDriver session closed");
            }
        }
        catch (Exception e) {
            logger.error("Failed to quit driver: {}", e.getMessage());
        }
        driver=null;
        actionDriver=null;
    }

    //If browser loads in 0.5 seconds, we essentially waste 1.5 seconds if passed 2. Recheck.
    public void staticWait(int seconds) {
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }

    // Getter Method for WebDriver
    public static WebDriver getDriver() {

        if (driver == null) {
            logger.error("WebDriver is not initialized");
            throw new IllegalStateException("WebDriver is not initialized");
        }
        return driver;

    }

    // Getter Method for ActionDriver
    public static ActionDriver getActionDriver() {

        if (actionDriver == null) {
            logger.error("ActionDriver is not initialized");
            throw new IllegalStateException("ActionDriver is not initialized");
        }
        return actionDriver;

    }

}
