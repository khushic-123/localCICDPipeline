package org.example;


import org.junit.jupiter.api.Test;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocalHtmlSeleniumTest {

    @Test
    public void testLocalHtml() throws Exception {
        // Credentials from environment
        String username = System.getenv("LT_USERNAME");
        String accessKey = System.getenv("LT_ACCESS_KEY");
        if (username == null || accessKey == null) {
            throw new IllegalStateException("Please set LT_USERNAME and LT_ACCESS_KEY in environment variables");
        }

        // Local HTML page served on CircleCI container
        String testUrl = "http://localhost:8080/index.html";

        // LambdaTest hub URL
        String hubUrl = "https://" + username + ":" + accessKey + "@hub.lambdatest.com/wd/hub";

        // Chrome browser options
        ChromeOptions options = new ChromeOptions();
        options.setCapability("browserName", "Chrome");
        options.setCapability("browserVersion", "latest");

        // LambdaTest options (W3C)
        Map<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("platformName", "Windows 11");
        ltOptions.put("tunnel", true);
        ltOptions.put("tunnelName", "myLocalTunnel");
        ltOptions.put("build", "CircleCI - LambdaTest Local HTML");
        ltOptions.put("name", "Local HTML Test");
        ltOptions.put("w3c", true);

        // Attach LT options
        options.setCapability("LT:Options", ltOptions);

        WebDriver driver = new RemoteWebDriver(new URL(hubUrl), options);

        try {
            driver.get(testUrl);
            String title = driver.getTitle();
            System.out.println("Page title: " + title);
            assertTrue(title != null && title.length() > 0);
        } finally {
            driver.quit();
        }
    }
}
