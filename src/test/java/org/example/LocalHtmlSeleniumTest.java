package org.example;


import org.junit.jupiter.api.Test;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class LocalHtmlSeleniumTest {

    @Test
    public void testLocalHtml() throws Exception {


        String username = System.getenv("LT_USERNAME");
        String accessKey = System.getenv("LT_ACCESS_KEY");

        // General browser capabilities
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("browserVersion", "latest");

        // LambdaTest-specific options
        MutableCapabilities ltOptions = new MutableCapabilities();
        ltOptions.setCapability("username", username);
        ltOptions.setCapability("accessKey", accessKey);
        ltOptions.setCapability("tunnel", true);
        ltOptions.setCapability("tunnelName", "myLocalTunnel");
        ltOptions.setCapability("w3c", true);
        ltOptions.setCapability("build", "Local HTML Build");
        ltOptions.setCapability("name", "Local HTML Test");

        // Attach LambdaTest options
        capabilities.setCapability("LT:Options", ltOptions);

        // Create RemoteWebDriver instance
        RemoteWebDriver driver = new RemoteWebDriver(
                new URL("https://hub.lambdatest.com/wd/hub"), capabilities
        );

        // Open your local page via tunnel
        driver.get("http://localhost:8080/index.html");
        System.out.println("Page Title: " + driver.getTitle());

        driver.quit();
    }
}
