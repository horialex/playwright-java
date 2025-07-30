package base;

import com.microsoft.playwright.Page;
import constants.ProjectConstants;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseTest {
    protected TestInfo testInfo;
    protected BrowserConfig browserConfig;
    protected Page page;

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        this.testInfo = testInfo;
        browserConfig = new BrowserConfig();
        browserConfig.launch();
        page = browserConfig.getPage();
    }

    @AfterEach
    public void tearDown() {
        if (browserConfig != null) {
            browserConfig.close();
        }
    }

    protected void takeScreenshotAndAttachToReport(String screenshotName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));

        String className = testInfo.getTestClass()
                .map(Class::getSimpleName)
                .orElse("UnknownClass");

        String methodName = testInfo.getTestMethod()
                .map(Method::getName)
                .orElse("unknownTest");

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
