package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import base.BaseTest;
import io.qameta.allure.Description;
import pages.HomePage.HomePage;

public class SimpleTest extends BaseTest {

    @Test
    @DisplayName("Test Name")
    @Description("Basic Simple test 1")
    public void simpleTest1() {
        HomePage homePage = new HomePage(page);
        homePage.navigateToHomePage();
        takeScreenshotAndAttachToReport("wishlist1");
    }

    // @Test
    // @Description("Basic Simple test 2")
    // public void simpleTest2() {
    // HomePage homePage = new HomePage(page);
    // homePage.navigateToHomePage();
    // takeScreenshotAndAttachToReport("wishlist2");
    // }

    // @Test
    // @Description("Basic Simple test 2")
    // public void simpleTest2() {
    // HomePage homePage = new HomePage(page);
    // homePage.navigateToHomePage();
    // takeScreenshotAndAttachToReport("wishlist2");
    // }

    // @Test
    // @Description("Login via API")
    // public void login_api() {
    // String username = EnvConfig.get("EMAIL");
    // String password = EnvConfig.get("PASSWORD");
    // String domain = EnvConfig.get("DOMAIN");

    // // Login Via API
    // LoginApiHelper loginHelper = new LoginApiHelper(apiUtils);
    // APIResponse loginResponse = loginHelper.login(username, password);
    // assertEquals(200, loginResponse.status());

    // // Inject cookies into browser context
    // List<String> setCookieHeaders = loginResponse.headersArray().stream()
    // .filter(header -> header.name.equalsIgnoreCase("Set-Cookie"))
    // .map(header -> header.value)
    // .toList();

    // System.out.println("Set-Cookie headers:");
    // setCookieHeaders.forEach(System.out::println);
    // List<Cookie> cookies =
    // CookieParserUtil.parseSetCookieHeaders(setCookieHeaders, domain);

    // // ✅ Inject cookies into the browser context
    // browserConfig.getContext().addCookies(cookies);

    // // ✅ Open your homepage as a logged-in user
    // HomePage homePage = new HomePage(page);
    // homePage.navigateToHomePage();

    // browserConfig.getContext().cookies().forEach(c -> {
    // System.out.println("Injected cookie: " + c.name + " = " + c.value + " for " +
    // c.domain);
    // });

    // page.pause();

    // }
}
