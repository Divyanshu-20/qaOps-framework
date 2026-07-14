package com.automation.orangehrm.tests;

import com.automation.orangehrm.base.BaseTest;
import com.automation.orangehrm.pages.HomePage;
import com.automation.orangehrm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginPageTest extends BaseTest {
   private LoginPage loginPage;
   private HomePage homePage;

    @BeforeMethod
    public void setupPages() {
        loginPage = new LoginPage(getDriver());
        homePage  = new HomePage(getDriver());
    }

    @Test
    public void verifyValidLoginTest() {
        loginPage.login("Admin", "admin123");
        Assert.assertTrue(homePage.isAdminTabVisible(),"Admin tab should be visible after successful login ");
        homePage.logout();
        staticWait(2);
    }

    @Test
    public void verifyInvalidValidLoginTest() {
        loginPage.login("Admin1", "admin123");
        String expectedErrorMessage = "Invalid credentials";
        Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage));
    }
}
