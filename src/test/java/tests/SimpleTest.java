package tests;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import com.microsoft.playwright.junit.UsePlaywright;
import base.BaseTest;
import io.qameta.allure.Description;
import pages.HomePage.HomePage;

public class SimpleTest extends BaseTest {

    @Test
    @Description("Basic Simple test 1")
    public void simpleTest1() {
        HomePage homePage = new HomePage(page);
        homePage.navigateToHomePage();
        takeScreenshotAndAttachToReport("wishlist1");
        // page.pause();
    }

    @Test
    @Description("Basic Simple test 2")
    public void simpleTest2() {
        HomePage homePage = new HomePage(page);
        homePage.navigateToHomePage();
        takeScreenshotAndAttachToReport("wishlist2");
    }

    // @Test
    // @Disabled
    // @Description("Basic Simple test 3")
    // public void simpleTest3() {
    // HomePage homePage = new HomePage(page);
    // homePage.navigateToHomePage();
    // takeScreenshotAndAttachToReport("wishlist3");
    // }

}
