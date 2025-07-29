package base;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import constants.ProjectConstants;
import io.qameta.allure.Allure;

public class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeEach
    public void setUp() {
        playwright = Playwright.create();
        /*browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
            .setHeadless(false)
            .setSlowMo(600)); */
        browser = playwright.chromium().launch();
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    public void tearDown() {
        if (page != null) page.close();
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    protected void takeScreenshotAndAttachToReport(String screenshotName) {
        page.screenshot(new com.microsoft.playwright.Page.ScreenshotOptions()
            .setPath(Paths.get(ProjectConstants.SCREENSHOT_PATH + screenshotName))
            .setFullPage(true));
        try {
            Allure.addAttachment(
                screenshotName, 
                "image/png",
                Files.newInputStream(Paths.get(ProjectConstants.SCREENSHOT_PATH + screenshotName)),
                ".png"
            );
        } catch (java.io.IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to attach screenshot", e);
        }
    }
}