package com.automation.orangehrm.tests;

import com.automation.orangehrm.base.BaseTest;
import com.automation.orangehrm.pages.HomePage;
import com.automation.orangehrm.pages.LoginPage;
import org.testng.annotations.BeforeMethod;

public class LoginPageTest extends BaseTest {
   private LoginPage loginPage;
   private HomePage homePage;

    @BeforeMethod
    public void setupPages() {
        loginPage = new LoginPage(getDriver());
        homePage  = new HomePage(getDriver());
    }
}
