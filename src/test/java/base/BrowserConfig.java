package base;

import java.nio.file.Paths;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class BrowserConfig {
    protected String browserName;
    protected boolean headless;
    protected int slowMo;
    protected String storageStatePath;

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    public BrowserConfig() {
        this.browserName = System.getProperty("browser", "chromium").toLowerCase();
        this.headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        this.slowMo = Integer.parseInt(System.getProperty("slowMo", "200"));
        this.storageStatePath = System.getProperty("storageStatePath"); // optional
    }

    public void launch() {
        playwright = Playwright.create();

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setSlowMo(slowMo);

        switch (browserName.toLowerCase()) {
            case "firefox":
                browser = playwright.firefox().launch(options);
                break;
            case "webkit":
                browser = playwright.webkit().launch(options);
                break;
            case "chromium":
            default:
                browser = playwright.chromium().launch(options);
                break;
        }

        if (storageStatePath != null) {
            context = browser.newContext(
                    new Browser.NewContextOptions().setStorageStatePath(Paths.get(storageStatePath)));
        } else {
            context = browser.newContext();
        }

        page = context.newPage();
    }

    public void close() {
        if (page != null)
            page.close();
        if (context != null)
            context.close();
        if (browser != null)
            browser.close();
        if (playwright != null)
            playwright.close();
    }

    public Page getPage() {
        return page;
    }

    public BrowserContext getContext() {
        return context;
    }

    public Browser getBrowser() {
        return browser;
    }

    public Playwright getPlaywright() {
        return playwright;
    }

    public String getStorageStatePath() {
        return storageStatePath;
    }

    public void setStorageStatePath(String storageStatePath) {
        this.storageStatePath = storageStatePath;
    }
}
