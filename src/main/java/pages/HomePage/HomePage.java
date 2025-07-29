package pages.HomePage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import pages.BasePage;

public class HomePage extends BasePage {
    // Header Locators
    public Locator searchBox;
    public Locator searchButton;
    public Locator accountButton;
    public Locator registerMenuButton;
    public Locator firstNameInput;
    public Locator lastNameInput;
    public Locator passwordInput;
    public Locator confirmPasswordInput;
    public Locator registerButton;
    public Locator emailInput;
    public Locator signOutMenuButton;
    public Locator loginMenuButton;
    public Locator emailLoginInput;
    public Locator passwordLoginInput;
    public Locator loginButton;
    public Locator welcomeMessage;

    //buttons available after searching for a product
    public Locator productNames;
    public Locator colourSwatch;
    public Locator productSizeS;
    public Locator addToCartSingleButton;
    // get all product containers
    public Locator product;
    public Locator sizeSwatches;
    public Locator addToWishlistButton;

    // Constructor
    public HomePage(Page page) {
        super(page);
        
        // Initialize Header Locators
        //this.searchBox = page.locator("#search");
        this.searchBox = page.getByPlaceholder("Search entire store here...");
        //this.searchButton = page.locator("button[title='Search']");
        this.searchButton = page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search"));
        //this.accountButton = page.getByText("Account");
        this.accountButton = page.locator("a.skip-account span.label");
        this.registerMenuButton = page.getByText("Register");
        this.firstNameInput = page.locator("input[name='firstname']");
        this.lastNameInput = page.locator("input[name='lastname']");
        this.emailInput = page.locator("#email_address");
        this.passwordInput = page.locator("input[name='password']");
        this.confirmPasswordInput = page.locator("input[name='confirmation']");
        this.registerButton = page.locator("button[title='Register']");
        
        // Initialize Login Locators
        this.signOutMenuButton= page.getByTitle("Log Out");
        this.loginMenuButton = page.getByTitle("Log In");
        this.emailLoginInput = page.getByTitle("Email Address");
        this.passwordLoginInput = page.getByTitle("Password");
        this.loginButton = page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login"));
        this.welcomeMessage = page.locator(".welcome-msg");

        this.productNames = page.locator("h2.product-name > a");
        // Locate the first swatch-link inside the color swatch list and click it
        this.colourSwatch = page.locator("#configurable_swatch_color a.swatch-link");
        this.sizeSwatches = page.locator("#configurable_swatch_size a.swatch-link");
        //this.productSizeS = page.getByRole(com.microsoft.playwright.options.AriaRole.LINK, new Page.GetByRoleOptions().setName("S"));
        this.addToCartSingleButton = page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to Cart"));
        this.product = page.locator("ul.products-grid > li.item");
        this.addToWishlistButton = page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to Wishlist"));
    }
    
    public void navigateToHomePage() {
        navigateToUrl("http://qa3magento.dev.evozon.com/");
    }
    
    public void createAccount(String firstName, String lastName, String email, String password) {
        accountButton.click();
        registerMenuButton.click();
        firstNameInput.fill(firstName);
        lastNameInput.fill(lastName);
        emailInput.fill(email);
        passwordInput.fill(password);
        confirmPasswordInput.fill(password);
        registerButton.scrollIntoViewIfNeeded();
        registerButton.click();
    }

    public void validateAuthentication() {
        String pageTitle = page.title();
        String pageUrl = page.url();

        System.out.println("Page title: " + pageTitle);
        System.out.println("Page URL: " + pageUrl);

        assertTrue(pageTitle.contains("My Account"), "Expected title to contain 'My Account' but was: " + pageTitle);
        assertTrue(pageUrl.contains("/customer/account/"), "Expected URL to contain '/customer/account/' but was: " + pageUrl);
    }

    public void checkUserIsAuthenticated(String UserFirstName, String UserLastName){
        Locator displayText = page.locator(".welcome-msg");
        String actualText = displayText.innerText().trim();
            assertEquals("WELCOME, " + UserFirstName.toUpperCase() + " " + UserLastName.toUpperCase() + "!", actualText, "The welcome message should match exactly.");
    }

    public void signOut() {
        accountButton.click();
        signOutMenuButton.click();
        // Wait for the logout to complete
        page.waitForURL("**/logoutSuccess/");
        String pageTitle = page.title();
        String pageUrl = page.url();
        assertTrue(pageTitle.contains("Magento Commerce"), "Expected title to contain 'Magento Commerce' but was: " + pageTitle);
        assertTrue(pageUrl.contains("/customer/account/logoutSuccess/"), "Expected URL to contain '/customer/account/logoutSuccess/' but was: " + pageUrl);
    }

    public void signIn(String email, String password) {
        accountButton.click();
        loginMenuButton.click();
        page.waitForURL("**/customer/account/login/");
        emailLoginInput.fill(email);
        passwordLoginInput.fill(password);
        loginButton.click();
        page.waitForURL("**/customer/account/");
        
        // Verify welcome message is visible
        //assertTrue(welcomeMessage.isVisible(), "Welcome message should be visible after successful login");
    }

    public StringBuilder searchProduct(String searchTerm) {
        searchBox.fill(searchTerm);
        searchButton.click();
        int count = productNames.count(); 
        StringBuilder mismatchedProducts = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String name = productNames.nth(i).innerText();
            //assertTrue(name.toLowerCase().contains(searchTerm.toLowerCase()),
                        //"Product name does not contain '" + searchTerm + "': " + name);
            if (!name.toLowerCase().contains(searchTerm.toLowerCase())) {
                mismatchedProducts.append("\nProduct name does not contain '")
                         .append(searchTerm)
                         .append("': ") 
                         .append(name);
            }
        }
        return mismatchedProducts;
    
    }

    public void addItemToCart(int index) {
        Locator addToCartButton = product.nth(index).locator("button.btn-cart");
        Locator viewDetailsButton = product.nth(index).locator("a.button:has-text('View Details')");
        if (addToCartButton.first().isVisible()) {
            addToCartButton.click();
            page.waitForSelector(".success-msg");
        }
        else {
            viewDetailsButton.click();
            //choose required options
            colourSwatch.first().click();
            sizeSwatches.first().click();
            addToCartSingleButton.click();
            page.waitForSelector(".success-msg");
        }
    }

    public void addItemToWishlistWhileLoggedIn(int productIndex) {
        Locator wishlistButton = product.nth(productIndex).locator("text=Add to Wishlist");
        wishlistButton.click();
        Locator successMsg = page.locator(".success-msg span");
        assertTrue(successMsg.isVisible(), "Success message should be visible");
    }   
}
