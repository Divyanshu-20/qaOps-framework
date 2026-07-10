package com.automation.orangehrm.pages;

import com.automation.orangehrm.actiondriver.ActionDriver;
import com.automation.orangehrm.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private ActionDriver actionDriver;


    public LoginPage(WebDriver driver) {
        this.actionDriver = new ActionDriver(driver);
    }

    //Define Locators
    private By userNameField = By.name("username");
    private By passworField = By.cssSelector("input[type='password']");
    private By loginButton = By.xpath("//button[text()=' Login ']");
    private By errorMessage = By.xpath("//p[text()='Invalid credentials']");

    //Method to perform login
    public void login(String userName, String password) {
        actionDriver.enterText(userNameField, userName);
        actionDriver.enterText(passworField, password);
        actionDriver.click(loginButton);
    }

    //Method to check if error message is displayed
    public boolean isErrorMessageDisplayed() {
        return actionDriver.isElementDisplayed(errorMessage);
    }

    //Method to get the text from Error message
    public String getErrorMessageText() {
        return actionDriver.getText(errorMessage);
    }

    //Verify if error is correct or not
    public boolean verifyErrorMessage(String expectedError) {
        return actionDriver.compareText(errorMessage, expectedError);
    }

}
