package com.automation.orangehrm.actiondriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ActionDriver {

    private WebDriver driver;
    private WebDriverWait wait;

    public ActionDriver(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    //method to click an element
    public void click(By by) {
        try {
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
        } catch (Exception e) {
            System.out.println("Failed to click element: " + e.getMessage());
        }
    }

    //method to enter text to input field
    public void enterText(By by, String text) {
        try {
            waitForElementToBeVisible(by);
            driver.findElement(by).clear();
            driver.findElement(by).sendKeys(text);
        } catch (Exception e) {
            System.out.println("Failed to enter text: " + e.getMessage());
        }
    }

    //method to get text from input field
    public String getText(By by) {
        try {
            waitForElementToBeVisible(by);
            return driver.findElement(by).getText();
        } catch (Exception e) {
            System.out.println("Failed to get text: " + e.getMessage());
            return "";
        }
    }


    //method to compare text from element with expected text
    public void compareText(By by, String expectedText) {
        try {
            waitForElementToBeVisible(by);
            String actualText = driver.findElement(by).getText();
            if (expectedText.equals(actualText)) {
                System.out.println("Text are Matching");
            } else {
                System.out.println("Text are NOT Matching. Expected: " + expectedText + " | Actual: " + actualText);
            }
        } catch (Exception e) {
            System.out.println("Unable to compare: " + e.getMessage());
        }
    }

    //method to check if an element is displayed
    public boolean isElementDisplayed(By by) {
        try {
            waitForElementToBeVisible(by);
            boolean isDisplayed = driver.findElement(by).isDisplayed();
            if (isDisplayed) {
                System.out.println("Element is Displayed");
            } else {
                System.out.println("Element is not Displayed");
            }
            return isDisplayed;
        } catch (Exception e) {
            System.out.println("Unable to check if element is displayed: " + e.getMessage());
        }
        return false;
    }


    //wait for element to be clickable
    public void waitForElementToBeClickable(By by) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            System.out.println("Element not clickable: " + e.getMessage());
        }
    }

    //wait for element to be visible
    public void waitForElementToBeVisible(By by) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            System.out.println("Element not visible: " + e.getMessage());
        }
    }

}
