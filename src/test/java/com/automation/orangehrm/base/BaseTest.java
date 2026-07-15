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

// Parent class every test class extends. Owns the WebDriver/ActionDriver lifecycle
// so individual test classes never touch Selenium setup directly.
public class BaseTest {

    protected static Properties prop;

    // One shared Logger per test class, fetched from LoggerManager (not "new"-ed here)
    // so every class logs through the same log4j2 config.
    public static final Logger logger = LoggerManager.getLogger(BaseTest.class);

    // ThreadLocal, not a plain field: if tests run in parallel (TestNG parallel="methods"),
    // each thread needs its own WebDriver/ActionDriver instance instead of sharing one.
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();

    public static Properties getProp() {
        return prop;
    }

    // Runs before every @Test method: brings up a fresh browser + ActionDriver
    // so each test starts from a clean, isolated state.
    @BeforeMethod
    public void setup() throws IOException {
        logger.info("Setting up WebDriver for test: {}", this.getClass().getSimpleName());
        launchBrowser();
        configureBrowser();
        staticWait(2);

        // ActionDriver wraps this thread's WebDriver; page objects fetch it via getActionDriver()
        // instead of constructing their own, so all Selenium calls share one wait config.
        actionDriver.set(new ActionDriver(getDriver()));
        logger.info("ActionDriver initlialized for thread: " + Thread.currentThread().getId());
        }

    // Runs once before the whole suite: config.properties is shared, read-only data,
    // so it only needs to load a single time rather than per test.
    @BeforeSuite
    public void loadConfig() throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        prop.load(fis);
    }

    // Reads the "browser" key from config.properties and stores a new driver instance
    // for the current thread. Add a new case here to support another browser.
    private void launchBrowser(){
        String browser = prop.getProperty("browser");
        logger.debug("Launching browser: {}", browser);

        switch (browser.toLowerCase()) {
            case "chrome":
                driver.set(new ChromeDriver());
                break;
            case "edge":
                driver.set(new EdgeDriver());
                break;
            default:
                logger.error("Browser not supported: {}", browser);
                throw new IllegalArgumentException("Browser Not Supported " + browser);
        }
    }


    // Applies implicit wait, maximizes the window, and navigates to the AUT URL —
    // all read from config.properties so environment changes don't need code changes.
    public void configureBrowser() {
        int wait = Integer.parseInt(prop.getProperty("implicitWait"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(wait));
        logger.debug("Implicit wait set to {} seconds", wait);

        //Maximize Driver
        getDriver().manage().window().maximize();

        //Navigate to URL
        try {
            getDriver().get(prop.getProperty("url"));
            logger.info("Navigated to URL: {}", prop.getProperty("url"));
        } catch (Exception e) {
            logger.error("Failed to navigate to the URL: {}", e.getMessage());
        }
    }

    // Runs after every @Test method: closes the browser and clears this thread's
    // ThreadLocal slots so no stale WebDriver/ActionDriver leaks into the next test.
    @AfterMethod
    public void tearDown() {
        try {
            if (getDriver() != null) {
                getDriver().quit();
                logger.info("WebDriver session closed");
            }
        }
        catch (Exception e) {
            logger.error("Failed to quit driver: {}", e.getMessage());
        }
        driver.remove();
        actionDriver.remove();
    }

    // Fixed delay, mainly used right after browser launch. Not ideal (wastes time when the
    // page is already ready) but simple; prefer ActionDriver's explicit waits inside tests.
    public void staticWait(int seconds) {
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }

    // Returns the current thread's WebDriver. Page objects and tests call this instead of
    // holding their own driver reference, so there's a single source of truth per thread.
    public static WebDriver getDriver() {

        if (driver.get() == null) {
            logger.error("WebDriver is not initialized");
            throw new IllegalStateException("WebDriver is not initialized");
        }
        return driver.get();

    }

    // Returns the current thread's ActionDriver. Page objects (e.g. LoginPage, HomePage)
    // call this in their constructors instead of "new ActionDriver(...)".
    public static ActionDriver getActionDriver() {

        if (actionDriver.get() == null) {
            logger.error("ActionDriver is not initialized");
            throw new IllegalStateException("ActionDriver is not initialized");
        }
        return actionDriver.get();

    }
}
