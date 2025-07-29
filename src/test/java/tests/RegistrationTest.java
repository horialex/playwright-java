package tests;
import java.nio.file.Paths;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

import base.BaseTest;
import constants.ProjectConstants;
import io.qameta.allure.Description;
import pages.HomePage.HomePage;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegistrationTest extends BaseTest {
    // Store credentials for reuse across tests
    private static String userEmail;
    private static final String USER_PASSWORD = "TestPassword123!";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    
    @Test
    @Description("Verify that a new user can successfully register with valid credentials")
    @Order(1)
    public void testUserRegistration() {
        HomePage homePage = new HomePage(page);
        homePage.navigateToHomePage();
        String timestamp = String.valueOf(System.currentTimeMillis());
        userEmail = "test" + timestamp + "@example.com";
        homePage.createAccount(
            FIRST_NAME,
            LAST_NAME,
            userEmail,
            USER_PASSWORD
        );
        homePage.validateAuthentication();
        page.screenshot(new com.microsoft.playwright.Page.ScreenshotOptions()
            .setPath(Paths.get(ProjectConstants.SCREENSHOT_PATH + "NewUserCreated.png")));
        homePage.signOut();
        homePage.signIn(userEmail, USER_PASSWORD);
        homePage.validateAuthentication();
        page.screenshot(new com.microsoft.playwright.Page.ScreenshotOptions()
            .setPath(Paths.get(ProjectConstants.SCREENSHOT_PATH + "ExistingUserReauthenticated.png")));
        // Save the authenticated state
        page.context().storageState(new BrowserContext.StorageStateOptions()
            .setPath(Paths.get("LoginState.json")));
        }    
    
    @Test
    @Description("Verify a previously saved context can be used for authentication.")
    @Order(2)
    public void UseExistingContext() { 
        // Create a new context with the saved storage state
        BrowserContext newContext = browser.newContext(
            new Browser.NewContextOptions().setStorageStatePath(Paths.get("LoginState.json")));
        
        // Create a new page in this context
        Page newPage = newContext.newPage();
        newPage.navigate("http://qa3magento.dev.evozon.com/");
        HomePage homePage = new HomePage(newPage);
        homePage.checkUserIsAuthenticated(FIRST_NAME, LAST_NAME);

    }
}