package example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class Tests {

    public static final String cookieBannerButton = ".cc-btn.cc_btn_accept_all";

    @BeforeMethod
    public void setup() throws Exception {
        WebDriver driver = DriverFactory.getInstance().getDriver();
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
        WebDriver driver = DriverFactory.getInstance().getDriver();
        driver.findElement(By.className("header-user-welcome")).click();
        driver.findElement(By.id("username")).sendKeys("agileTestingDays");
        driver.findElement(By.id("login_credentials_password")).sendKeys("atd!rocks");
        driver.findElement(By.id("login_submit")).click();
        driver.findElement(By.cssSelector("img[alt^=\"Icon_todo\"]"));
    }

    @AfterMethod
    public void closeDriver() {
        DriverFactory.getInstance().removeDriver();
    }

    private void removeCookieBanner() throws Exception {
        WebDriver driver = DriverFactory.getInstance().getDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.querySelector('" + cookieBannerButton + "').click();");
        waitUntilOverlayDisappears(cookieBannerButton);
    }

    private void waitUntilOverlayDisappears(String selector) throws Exception {
        WebDriver driver = DriverFactory.getInstance().getDriver();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(selector)));
    }
}
