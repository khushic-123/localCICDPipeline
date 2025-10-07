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
            throw new IllegalStateException("Please set LT_USERNAME and LT_ACCESS_KEY in CircleCI environment variables");
        }

        // 2️⃣ Local HTML page URL (***CRITICAL FIX: Use the LambdaTest local hostname***)
        // This is necessary for the LambdaTest grid to route the request through the tunnel.
        String testUrl = "http://localhost.lambdatest.com:5500/index.html";

        // 3️⃣ LambdaTest hub URL
        String hubUrl = "https://" + username + ":" + accessKey + "@hub.lambdatest.com/wd/hub";

        // 4️⃣ Standard Chrome options (these are fine)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");


        // 5️⃣ LambdaTest options (LT:Options block)
        Map<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("browserName","chrome");
        ltOptions.put("platformName", "Windows 11");
        ltOptions.put("browserVersion", "latest");
        ltOptions.put("tunnel", true);                   // Enable tunnel
        ltOptions.put("tunnelName", "myLocalTunnel");    // Match name in config.yml
        ltOptions.put("build", "CircleCI - Local HTML");
        ltOptions.put("name", "Local HTML Test");
        ltOptions.put("w3c", true);

        // Attach LambdaTest options
        options.setCapability("LT:Options", ltOptions);

        // 6️⃣ Create RemoteWebDriver
        WebDriver driver = new RemoteWebDriver(new URL(hubUrl), options);


        try {
            System.out.println("⏳ Waiting for page to load via tunnel: " + testUrl);

            // 7️⃣ Open local HTML page (The connection reset was happening here before)
            driver.get(testUrl);

            // 8️⃣ Wait until page title is available (Requires a non-empty <title> tag in index.html)
            new WebDriverWait(driver, Duration.ofSeconds(60))
                    .until((ExpectedCondition<Boolean>) d -> d.getTitle() != null && !d.getTitle().isEmpty());

            // 9️⃣ Validate page title
            String title = driver.getTitle();
            System.out.println("✅ Page title: " + title);
            assertTrue(title != null && title.length() > 0, "Page title validation failed.");

        } finally {
            if (driver != null) {
                driver.quit();
                System.out.println("Driver session ended.");
            }
        }
    }
}
