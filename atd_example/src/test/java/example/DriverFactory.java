package example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverFactory {
    private static DriverFactory instance = new DriverFactory();

    ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>() {
        @Override
        protected WebDriver initialValue() {
            setCapability();
            WebDriver driver = null;
            if (System.getenv("BROWSER").contains("firefox")) {
                driver = new FirefoxDriver();
            } else {
                driver = new ChromeDriver();
            }
            return driver;
        }
    };

    private DriverFactory() {

    }

    public static DriverFactory getInstance() {
        return instance;
    }

    private void setCapability() {
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

    public WebDriver getDriver() // call this method to get the driver object
    {
        return driver.get();
    }

    public void removeDriver() // Quits the driver and closes the browser
    {
        if (driver.get() != null) {
            try {
                driver.get().quit();
            } catch (Exception e) {}
            driver.remove();
        }
    }
}

