package base;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;

import io.qameta.allure.Allure;
import utils.EnvConfig;

public class PlaywrightConfig {
    protected String browserName;
    protected String device;
    protected boolean headless;
    protected int slowMo;
    protected String storageStatePath;

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;
    protected APIRequestContext apiRequest;

    protected int defaultTimeout;
    protected int navigationTimeout;
    protected String traceMode;
    protected boolean isTracingEnabled;
    private boolean testFailed = false; // for retain-on-failure
    private String traceFilePath; // 🔹 Unique path per test

    public PlaywrightConfig() {
        this.browserName = EnvConfig.get("BROWSER").toLowerCase();
        this.headless = Boolean.parseBoolean(EnvConfig.get("HEADLESS"));
        this.slowMo = Integer.parseInt(EnvConfig.get("SLOW_MO"));

        this.storageStatePath = System.getProperty("storageStatePath"); // optional
        this.device = EnvConfig.get("DEVICE");

        this.defaultTimeout = Integer.parseInt(EnvConfig.get("DEFAULT_TIMEOUT"));
        this.navigationTimeout = Integer.parseInt(EnvConfig.get("NAVIGATION_TIMEOUT"));

        this.traceMode = EnvConfig.get("TRACE_MODE").toLowerCase(); // e.g. "retain-on-failure"
        this.isTracingEnabled = traceMode.equals("retain-on-failure");
    }

    private Browser.NewContextOptions getDeviceContextOptions() {
        switch (device.toLowerCase()) {
            case "mobile":
                return new Browser.NewContextOptions()
                        .setViewportSize(375, 667)
                        .setIsMobile(true)
                        .setHasTouch(true);
            case "tablet":
                return new Browser.NewContextOptions()
                        .setViewportSize(768, 1024)
                        .setHasTouch(true);
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

        if (isTracingEnabled) {
            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true));
        }

        page = context.newPage();

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");

        // String token = EnvConfig.get("API_TOKEN");
        // if (token != null && !token.isBlank()) {
        // headers.put("Authorization", "Bearer " + token);
        // }

        apiRequest = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL(EnvConfig.get("APP_HOST"))
                        .setExtraHTTPHeaders(headers));
    }

    public void close() {
        try {
            if (isTracingEnabled && context != null) {
                if (testFailed && traceFilePath != null) {
                    Path tracePath = Paths.get(traceFilePath);
                    Files.createDirectories(tracePath.getParent());
                    context.tracing().stop(new Tracing.StopOptions().setPath(tracePath));

                    // ✅ Attach trace.zip to Allure
                    Allure.addAttachment("Playwright Trace", "application/zip",
                            new FileInputStream(tracePath.toFile()), ".zip");

                } else {
                    context.tracing().stop();
                }
            }
        } catch (Exception e) {
            System.err.println("Error stopping tracing: " + e.getMessage());
            e.printStackTrace();
        }

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

    public void setTestFailed(boolean testFailed) {
        this.testFailed = testFailed;
    }

    public void setTraceFilePath(String traceFilePath) {
        this.traceFilePath = traceFilePath;
    }

    public APIRequestContext getApiRequest() {
        return apiRequest;
    }
}
