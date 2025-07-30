package base;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import com.microsoft.playwright.Page;

import constants.ProjectConstants;
import io.qameta.allure.Allure;

public class BaseTest_old {
    protected BrowserConfig browserConfig;
    protected Page page;

    @BeforeEach
    public void setUp() {
        browserConfig = new BrowserConfig();
        browserConfig.launch();
        page = browserConfig.getPage();
    }

    @AfterEach
    public void tearDown() {
        browserConfig.close();
    }

    protected void takeScreenshotAndAttachToReport(String screenshotName) {
        String fullPath = ProjectConstants.SCREENSHOT_PATH + screenshotName + ".png";

        try {
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get(fullPath))
                    .setFullPage(true));

            Allure.addAttachment(
                    screenshotName,
                    "image/png",
                    Files.newInputStream(Paths.get(fullPath)),
                    ".png");

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to take or attach screenshot", e);
        }
    }
}