package com.automation.orangehrm.tests;

import com.automation.orangehrm.base.BaseTest;
import com.automation.orangehrm.pages.HomePage;
import com.automation.orangehrm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageTest extends BaseTest {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPages() {
        loginPage = new LoginPage(getDriver());
        homePage  = new HomePage(getDriver());
    }

    @Test
    public void verifyOrangeHRMLogo() {
        loginPage.login("Admin", "admin123");
        Assert.assertTrue(homePage.verifyOrangeHRMlogo(),"Logo is not visible");
        homePage.logout();
    }

}
