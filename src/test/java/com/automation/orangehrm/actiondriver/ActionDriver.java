package com.automation.orangehrm.actiondriver;

import com.automation.orangehrm.base.BaseTest;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.automation.orangehrm.base.BaseTest.getProp;

// Wraps raw Selenium calls (click, type, wait, etc.) so page objects never call
// driver.findElement(...) directly. One instance is created per thread by BaseTest.setup()
// and handed out via BaseTest.getActionDriver().
public class ActionDriver {

    private WebDriver driver;
    private WebDriverWait wait;
    // Reuses BaseTest's logger instead of creating a separate one, so ActionDriver
    // and test logs interleave under the same class-based logger config.
    public static final Logger logger = BaseTest.logger;

    public ActionDriver(WebDriver driver) {
        this.driver = driver;
        int explicitWait = Integer.parseInt(getProp().getProperty("explicitWait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
    }

    // method to click an element
    public void click(By by) {
        try {
            waitForElementToBeClickable(by);
            String description = getElementDescription(by);
            driver.findElement(by).click();
            logger.info("Clicked {}", description);
        } catch (Exception e) {
            logger.error("Failed to click element {}: {}", by, e.getMessage());
        }
    }

    // method to enter text to input field
    public void enterText(By by, String text) {
        try {
            waitForElementToBeVisible(by);
            WebElement element = driver.findElement(by);
            element.clear();
            element.sendKeys(text);
        } catch (Exception e) {
            logger.error("Failed to enter text into {}: {}", by, e.getMessage());
        }
    }

    // method to get text from input field
    public String getText(By by) {
        try {
            waitForElementToBeVisible(by);
            return driver.findElement(by).getText();
        } catch (Exception e) {
            logger.error("Failed to get text from {}: {}", by, e.getMessage());
            return "";
        }
    }

    public boolean compareText(By by, String expectedText) {
        try {
            waitForElementToBeVisible(by);
            String actualText = driver.findElement(by).getText();
            if (expectedText.equals(actualText)) {
                return true;
            }
            logger.warn("Text mismatch on {} — Expected: [{}] | Actual: [{}]", by, expectedText, actualText);
            return false;
        } catch (Exception e) {
            logger.error("Unable to compare text on {}: {}", by, e.getMessage());
            return false;
        }
    }

    public boolean isElementDisplayed(By by) {
        try {
            waitForElementToBeVisible(by);
            boolean displayed = driver.findElement(by).isDisplayed();
            logger.info("{} is displayed: {}", getElementDescription(by), displayed);
            return displayed;
        } catch (Exception e) {
            logger.error("Unable to check if element {} is displayed: {}", by, e.getMessage());
            return false;
        }
    }

    // wait for element to be clickable
    public void waitForElementToBeClickable(By by) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            logger.error("Element {} not clickable: {}", by, e.getMessage());
        }
    }

    // wait for element to be visible
    public void waitForElementToBeVisible(By by) {      
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            logger.error("Element {} not visible: {}", by, e.getMessage());
        }
    }

    // scroll to element
    public void scrollToElement(By by) {
        try {
            WebElement element = driver.findElement(by);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        } catch (Exception e) {
            logger.error("Failed to scroll to element {}: {}", by, e.getMessage());
        }
    }

    // Method to get the description of an element using By locator
    public String getElementDescription(By locator) {
        // Check for null driver or locator to avoid NullPointerException
        if (driver == null) {
            return "Driver is not initialized.";
        }
        if (locator == null) {
            return "Locator is null.";
        }

        try {
            // Find the element using the locator
            WebElement element = driver.findElement(locator);

            // Get element attributes
            String name = element.getDomProperty("name");
            String id = element.getDomProperty("id");
            String text = element.getText();
            String className = element.getDomProperty("class");
            String placeholder = element.getDomProperty("placeholder");

            // Return a description based on available attributes
            if (isNotEmpty(name)) {
                return "Element with name: " + name;
            } else if (isNotEmpty(id)) {
                return "Element with ID: " + id;
            } else if (isNotEmpty(text)) {
                return "Element with text: " + truncate(text, 50);
            } else if (isNotEmpty(className)) {
                return "Element with class: " + className;
            } else if (isNotEmpty(placeholder)) {
                return "Element with placeholder: " + placeholder;
            } else {
                return "Element located using: " + locator.toString();
            }
        } catch (Exception e) {
            // Log exception for debugging
            e.printStackTrace(); // Replace with a logger in a real-world scenario
            return "Unable to describe element due to error: " + e.getMessage();
        }
    }

    // Utility method to check if a string is not null or empty
    private boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    // Utility method to truncate long strings
    private String truncate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength) + "...";
    }

}
