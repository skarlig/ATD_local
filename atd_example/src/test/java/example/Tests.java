package example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class Tests {

    public WebDriver driver;

    public static final String cookieBannerButton = ".cc-btn.cc_btn_accept_all";

    @BeforeMethod
    public void setup() throws Exception {
        createBrowserDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().fullscreen();
        driver.get("https://en.dawanda.com");

        //remove cookie banner
        if (driver.findElements(By.cssSelector(cookieBannerButton)).size() != 0) {
            removeCookieBanner();
        }
    }

    @Test
    public void loginDaWanda() {
        driver.findElement(By.className("header-user-welcome")).click();
        driver.findElement(By.id("username")).sendKeys("agileTestingDays");
        driver.findElement(By.id("login_credentials_password")).sendKeys("atd!rocks");
        driver.findElement(By.id("login_submit")).click();
        driver.findElement(By.cssSelector("img[alt^=\"Icon_todo\"]"));
    }

    @AfterMethod
    public void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }


    private void createBrowserDriver() {
        String os = detectOS();

        String[] location = Tests.class.getProtectionDomain().getCodeSource().getLocation().toString().
                split("build");

        String driverPath = location[0].replace("file:", "").concat("/drivers/");

        switch (os) {
            case "windows":
                System.setProperty("webdriver.gecko.driver", driverPath + "geckodriver_windows.exe");
                System.setProperty("webdriver.chrome.driver", driverPath + "chromedriver_windows.exe");
                break;

            case "linux":
                System.setProperty("webdriver.gecko.driver", driverPath + "geckodriver_linux");
                System.setProperty("webdriver.chrome.driver", driverPath + "chromedriver_linux");
                break;

            case "mac":
                System.setProperty("webdriver.gecko.driver", driverPath + "geckodriver_mac");
                System.setProperty("webdriver.chrome.driver", driverPath + "chromedriver_mac");
                break;
        }
        if (System.getenv("BROWSER").contains("firefox")) {
            driver = new FirefoxDriver();
        } else {
            driver = new ChromeDriver();
        }
    }

    private String detectOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "windows";
        } else if (os.contains("nux") || os.contains("nix")) {
            return "linux";
        } else if (os.contains("mac")) {
            return "mac";
        } else {
            System.out.println("Not supported OS");
            return "other";
        }
    }

    private void removeCookieBanner() throws Exception {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.querySelector('" + cookieBannerButton + "').click();");
        waitUntilOverlayDisappears(cookieBannerButton);
    }

    private void waitUntilOverlayDisappears(String selector) throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(selector)));
    }
}