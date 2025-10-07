package org.example;


import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class LocalHtmlSeleniumTest {

    @Test
    public void testLocalHtml() throws Exception {

        String username = System.getenv("LT_USERNAME");
        String accessKey = System.getenv("LT_ACCESS_KEY");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("version", "latest");
        capabilities.setCapability("platform", "Windows 11");
        capabilities.setCapability("tunnel", true);
        capabilities.setCapability("tunnelName", "myLocalTunnel");

        String gridUrl = "https://" + username + ":" + accessKey + "@hub.lambdatest.com/wd/hub";

        WebDriver driver = new RemoteWebDriver(new URL(gridUrl), capabilities);

        driver.get("http://localhost:8080/index.html"); // Access local HTML page via tunnel
        System.out.println("Page Title: " + driver.getTitle());

        driver.quit();
    }
}
