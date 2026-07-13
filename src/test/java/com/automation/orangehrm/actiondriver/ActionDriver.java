package com.automation.orangehrm.actiondriver;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.automation.orangehrm.base.BaseTest.getProp;


/* Consider this as BASEPAGE CLASS WHICH HOLDS WHAT ALL SELENIUM WEBDRIVER CAN PERFORM ON A PAGE */

public class ActionDriver {

    private WebDriver driver;
    private WebDriverWait wait;

    public ActionDriver(WebDriver driver) {
        this.driver = driver;
        int explicitWait = Integer.parseInt(getProp().getProperty("explicitWait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
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
            WebElement element = driver.findElement(by);
            element.clear();
            element.sendKeys(text);
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


    public boolean compareText(By by, String expectedText) {
        try {
            waitForElementToBeVisible(by);
            String actualText = driver.findElement(by).getText();
            if (expectedText.equals(actualText)) {
                System.out.println("Text match confirmed: [" + actualText + "]");
                return true;
            }
            System.out.println("Text mismatch — Expected: [" + expectedText + "] | Actual: [" + actualText + "]");
            return false;
        } catch (Exception e) {
            System.out.println("Unable to compare text: " + e.getMessage());
            return false;
        }
    }

    public boolean isElementDisplayed(By by) {
        try {
            waitForElementToBeVisible(by);
            return driver.findElement(by).isDisplayed();
        } catch (Exception e) {
            System.out.println("Unable to check if element is displayed: " + e.getMessage());
            return false;
        }
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

    //scroll to element
    public void scrollToElement(By by) {
        try {
            WebElement element = driver.findElement(by);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        } catch (Exception e) {
            System.out.println("Failed to scroll to element: " + e.getMessage());
        }
    }

}
