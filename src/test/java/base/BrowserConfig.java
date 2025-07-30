package base;

import java.nio.file.Paths;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import utils.EnvConfig;

public class BrowserConfig {
    protected String browserName;
    protected boolean headless;
    protected int slowMo;
    protected String storageStatePath;

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;
    protected String device;

    protected int defaultTimeout;
    protected int navigationTimeout;

    public BrowserConfig() {
        this.browserName = EnvConfig.get("BROWSER").toLowerCase();
        this.headless = Boolean.parseBoolean(EnvConfig.get("HEADLESS"));
        this.slowMo = Integer.parseInt(EnvConfig.get("SLOW_MO"));

        this.storageStatePath = System.getProperty("storageStatePath"); // optional
        this.device = EnvConfig.get("DEVICE");
        this.defaultTimeout = Integer.parseInt(EnvConfig.get("DEFAULT_TIMEOUT"));
        this.navigationTimeout = Integer.parseInt(EnvConfig.get("NAVIGATION_TIMEOUT"));
    }

    private Browser.NewContextOptions getDeviceContextOptions() {
        switch (device.toLowerCase()) {
            case "mobile":
                return new Browser.NewContextOptions()
                        .setViewportSize(375, 667)
                        .setIsMobile(true)
                        .setHasTouch(true)
                        .setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 13_2 like Mac OS X)");
            case "tablet":
                return new Browser.NewContextOptions()
                        .setViewportSize(768, 1024)
                        .setUserAgent("Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X)");
            case "web":
            default:
                return new Browser.NewContextOptions()
                        .setViewportSize(1920, 1080);
        }
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

        Browser.NewContextOptions contextOptions = getDeviceContextOptions();

        if (storageStatePath != null) {
            contextOptions.setStorageStatePath(Paths.get(storageStatePath));
        }

        context = browser.newContext(contextOptions);
        context.setDefaultTimeout(defaultTimeout);
        context.setDefaultNavigationTimeout(navigationTimeout);

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
