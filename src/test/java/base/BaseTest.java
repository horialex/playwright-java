package base;

import com.microsoft.playwright.Page;

import api.ApiUtils;
import constants.ProjectConstants;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseTest {
    protected TestInfo testInfo;
    protected PlaywrightConfig playwrightConfig;
    protected Page page;
    protected ApiUtils apiUtils;

    protected String className;
    protected String methodName;
    protected String timestamp;

    @RegisterExtension
    TestWatcher watcher = new TestWatcher() {

        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            System.out.println("WATCHER: Test failed");
            if (playwrightConfig != null) {
                takeScreenshotAndAttachToReport("failure");
                playwrightConfig.setTestFailed(true);
                playwrightConfig.close();
            }
        }

        @Override
        public void testSuccessful(ExtensionContext context) {
            System.out.println("WATCHER: Test passed");
            if (playwrightConfig != null) {
                playwrightConfig.setTestFailed(false);
                playwrightConfig.close();
            }
        }
    };

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        this.testInfo = testInfo;
        playwrightConfig = new PlaywrightConfig();

        // Create unique trace file name per test
        className = testInfo.getTestClass().map(Class::getSimpleName).orElse("UnknownClass");
        methodName = testInfo.getTestMethod().map(Method::getName).orElse("unknownTest");
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));

        String traceFile = ProjectConstants.TRACE_PATH + "trace_" + className + "_" + methodName + "_" + timestamp
                + ".zip";

        playwrightConfig.setTraceFilePath(traceFile);

        playwrightConfig.launch();
        page = playwrightConfig.getPage();
        this.apiUtils = new ApiUtils(playwrightConfig.getApiRequest());
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
