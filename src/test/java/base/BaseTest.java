package base;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import constants.ProjectConstants;
import io.qameta.allure.Allure;

public class BaseTest {
    protected TestInfo testInfo;
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;
    protected String storageStatePath = null;

    protected String className;
    protected String methodName;
    protected String timestamp;

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        this.testInfo = testInfo;
        playwright = Playwright.create();

        className = testInfo.getTestClass().map(Class::getSimpleName).orElse("UnknownClass");
        methodName = testInfo.getTestMethod().map(Method::getName).orElse("unknownTest");
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));

        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(200));

        if (storageStatePath != null) {
            context = browser.newContext(
                    new Browser.NewContextOptions().setViewportSize(1920, 1080)
                            .setStorageStatePath(Paths.get(storageStatePath)));
        } else {
            context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1920, 1080));
        }
        page = context.newPage();
    }

    @AfterEach
    public void tearDown() {
        if (page != null)
            page.close();
        if (context != null)
            context.close();
        if (browser != null)
            browser.close();
        if (playwright != null)
            playwright.close();
    }

    protected void takeScreenshotAndAttachToReport(String screenshotName) {
        String uniqueName = className + "_" + methodName + "_" + screenshotName + "_" + timestamp;
        String fullPath = ProjectConstants.SCREENSHOT_PATH + uniqueName + ".png";
        try {
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get(fullPath))
                    .setFullPage(true));

            Allure.addAttachment(
                    uniqueName,
                    "image/png",
                    Files.newInputStream(Paths.get(fullPath)),
                    ".png");

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to take or attach screenshot", e);
        }
    }

}