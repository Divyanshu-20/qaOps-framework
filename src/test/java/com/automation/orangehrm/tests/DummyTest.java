package com.automation.orangehrm.tests;

import com.automation.orangehrm.base.BaseClass;
import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class DummyTest extends BaseClass {     // class name should be PascalCase

    By imgLocator = By.xpath("//div[@class='orangehrm-login-branding']/img");

    @Test
    public void verifyImageIsPresent() {
        boolean isImagePresent = driver.findElement(imgLocator).isEnabled();
        System.out.println(isImagePresent);
        System.out.println(driver.getTitle());
    }

}
