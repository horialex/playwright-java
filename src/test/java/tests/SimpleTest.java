package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import base.BaseTest;
import io.qameta.allure.Description;
import pages.HomePage.HomePage;

public class SimpleTest extends BaseTest {

    @Test
    @DisplayName("Test Name 1")
    @Description("Basic Simple test 1")
    public void simpleTest1() {
        HomePage homePage = new HomePage(page);
        homePage.navigateToHomePage();
        takeScreenshotAndAttachToReport("wishlist1");
    }

    @Test
    @DisplayName("Test Name 2")
    @Description("Basic Simple test 2")
    public void simpleTest2() {
        HomePage homePage = new HomePage(page);
        homePage.navigateToHomePage();
        takeScreenshotAndAttachToReport("wishlist1");
    }
}
