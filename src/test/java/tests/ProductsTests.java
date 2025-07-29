package tests;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import pages.HomePage.HomePage;

public class ProductsTests extends BaseTest {
    @Override
    @BeforeEach
    public void setUp() {
        this.storageStatePath = "LoginState.json";
        super.setUp();
    }
    @Test
    @Description("Verify that the search functionality returns expected results")
    public void SearchItem() {
        /*context.tracing().start(new Tracing.StartOptions()
            .setScreenshots(true)
            .setSnapshots(true)
            .setSources(true));*/

        HomePage homePage = new HomePage(page);
        homePage.navigateToHomePage();
        
        StringBuilder foundProducts = null;
        foundProducts = homePage.searchProduct("dress");
        takeScreenshotAndAttachToReport("searchedItem.png");

        if (foundProducts.length() > 0) {
            Allure.addAttachment("All mismatched products", foundProducts.toString());
            throw new AssertionError("Some products did not match the search term:\n" + foundProducts);
        } 
        /*context.tracing().stop(new Tracing.StopOptions()
            .setPath(Paths.get("trace.zip")));     */
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

    @Test
    @Description("Verify that items can be added to cart after searching")
    public void addItemToCart() {
        HomePage homePage = new HomePage(page);
        homePage.navigateToHomePage();
        homePage.searchProduct("dress");
        homePage.addItemToCart(1);
        page.goBack();
        page.goBack();
        homePage.addItemToCart(2);
        takeScreenshotAndAttachToReport("checkout.png");
    }
    
    @Test
    @Description("Verify that items can be added to wishlist after searching")
    public void addItemToWishlist() {
        HomePage homePage = new HomePage(page);
        homePage.navigateToHomePage();
        homePage.searchProduct("coat");
        homePage.addItemToWishlistWhileLoggedIn(0);
        takeScreenshotAndAttachToReport("wishlist.png");
    }
}
