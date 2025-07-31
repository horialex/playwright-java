package pages;

import com.microsoft.playwright.Page;

public class BasePage {
    protected final Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    public void navigateToUrl(String url) {
        page.navigate(url);
    }

    protected void waitForPageLoad() {
        page.waitForLoadState();
    }
}
