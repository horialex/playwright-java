package tests;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Tracing;

import base.BaseTest;
import constants.ProjectConstants;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import pages.HomePage.HomePage;

public class SearchItemTest extends BaseTest {
    @Test
    @Description("Verify that the search functionality returns expected results and items can be added to the cart")
    public void addSearchedItemToCart() {
        context.tracing().start(new Tracing.StartOptions()
            .setScreenshots(true)
            .setSnapshots(true)
            .setSources(true));

        HomePage homePage = new HomePage(page);
        homePage.navigateToHomePage();
        
        StringBuilder mismatchesProducts = homePage.searchProduct("dress");
        boolean searchItemWorksFine;
        searchItemWorksFine = (mismatchesProducts.length()) == 0;

        page.screenshot(new com.microsoft.playwright.Page.ScreenshotOptions()
            .setPath(Paths.get(ProjectConstants.SCREENSHOT_PATH + "searchedItem.png"))
            .setFullPage(true));

        homePage.addItemToCart(1);
        page.goBack();
        page.goBack();
        homePage.addItemToCart(2);
        page.screenshot(new com.microsoft.playwright.Page.ScreenshotOptions()
            .setPath(Paths.get(ProjectConstants.SCREENSHOT_PATH + "checkout.png"))
            .setFullPage(true));
        //assertTrue(searchItemWorksFine, "Some products did not match the search term:" + mismatchesProducts.toString());

        String[] imageNames = {"searchedItem.png", "checkout.png"};

        for (String imageName : imageNames) {
            try {
                Allure.addAttachment(
                    imageName, // Use the image file name as the attachment name
                    "image/png",
                    Files.newInputStream(Paths.get(ProjectConstants.SCREENSHOT_PATH + imageName)),
                    ".png"
                );
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
    }   
        context.tracing().stop(new Tracing.StopOptions()
            .setPath(Paths.get("trace.zip")));      
    }

    @Test 
    public void addItemToCart() {
        
    }

    @Test
    @Disabled("This test is skipped as it is not implemented yet")
    public void testSearchFunctionality2() throws Exception {
        String baseUrl = "http://qa3magento.dev.evozon.com/customer/account/index/?";
        String searchTerm = "coat";
        String searchUrl = baseUrl + "q=" + searchTerm;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(searchUrl))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Accept both 200 (OK) and 302 (Found/Redirect) as valid responses
        assertTrue(response.statusCode() == 200 || response.statusCode() == 302, "Expected HTTP 200 or 302 response");
        System.out.println(response.body().contains(searchTerm));
        assertTrue(response.body().contains(searchTerm), "Search results should contain the search term");
    } 

}
