package org.example;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocalHtmlSeleniumTest {

    @Test
    public void testLocalHtml() throws Exception {
        // 1️⃣ LambdaTest credentials from environment
        String username = System.getenv("LT_USERNAME");
        String accessKey = System.getenv("LT_ACCESS_KEY");
        if (username == null || accessKey == null) {
            throw new IllegalStateException("Please set LT_USERNAME and LT_ACCESS_KEY in environment variables");
        }

        // 2️⃣ Local HTML page
        String testUrl = "http://localhost:5500/index.html";

        // 3️⃣ LambdaTest hub URL
        String hubUrl = "https://" + username + ":" + accessKey + "@hub.lambdatest.com/wd/hub";

        // 4️⃣ Chrome options (no deprecated keys at top-level)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");


        // 5️⃣ LambdaTest options (all custom keys go here)
        Map<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("platformName", "Windows 11");      // Correct W3C key
        ltOptions.put("browserVersion", "latest");       // Correct W3C key
        ltOptions.put("tunnel", true);                   // Enable tunnel
        ltOptions.put("tunnelName", "myLocalTunnel");    // Match your CircleCI tunnel
        ltOptions.put("build", "CircleCI - Local HTML"); // Optional build name
        ltOptions.put("name", "Local HTML Test");        // Optional test name
        ltOptions.put("w3c", true);                      // Enforce W3C compliance

        // Attach LambdaTest options
        options.setCapability("LT:Options", ltOptions);

        // 6️⃣ Create RemoteWebDriver
        WebDriver driver = new RemoteWebDriver(new URL(hubUrl), options);


        try {
            System.out.println("⏳ Waiting for page to load via tunnel: " + testUrl);

            // 7️⃣ Wait until page title is available
            new WebDriverWait(driver, Duration.ofSeconds(60))
                    .until((ExpectedCondition<Boolean>) d -> d.getTitle() != null && !d.getTitle().isEmpty());

            // 8️⃣ Open local HTML page
            driver.get(testUrl);

            // 9️⃣ Validate page title
            String title = driver.getTitle();
            System.out.println("✅ Page title: " + title);
            assertTrue(title != null && title.length() > 0, "Title should not be empty");

        } finally {
            driver.quit();
            System.out.println("Driver session ended.");
        }
    }
}
