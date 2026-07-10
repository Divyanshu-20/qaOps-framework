package com.automation.orangehrm.base;

import com.automation.orangehrm.actiondriver.ActionDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class BaseClass {

    protected static Properties prop; //Static is necessary to share common copy of prop across tests
    private static WebDriver driver;
    private static ActionDriver actionDriver;

    public static WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        BaseClass.driver = driver;
    }

    public static Properties getProp() {
        return prop;
    }

    @BeforeMethod
    public void setup() throws IOException {
        System.out.println("Setting WebDriver for: " + this.getClass().getSimpleName());
        launchBrowser();
        configureBrowser();
        staticWait(2);
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

        switch (browser.toLowerCase()) {
            case "chrome":
                driver = new ChromeDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            default:
                throw new IllegalArgumentException("Browser Not Supported " + browser);
        }
    }


    //Configure Browser settings
    public void configureBrowser() {
        //Implicit wait
        int wait = Integer.parseInt(prop.getProperty("implicitWait"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(wait));

        //Maximize Driver
        driver.manage().window().maximize();

        //Navigate to URL
        try {
            driver.get(prop.getProperty("url"));
        } catch (Exception e) {
            System.out.println("Failed to Navigate to the URL: " + e.getMessage());
        }
    }

    @AfterMethod
    public void tearDown() {
        try {
            if (driver != null) {
                driver.quit();
            }
        }
        catch (Exception e) {
            System.out.println("Failed to quit driver: " + e.getMessage());
        }
    }

    public void staticWait(int seconds) {
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }


}
