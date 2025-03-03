package com.overridetech.funpay_monitor.selenium.page;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Duration;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class FunpayPOM {

    private WebDriver webDriver;
    private Wait<WebDriver> wait;

    @Value(value = "${funpay.base-url}")
    private String BASE_URL;


    @Value(value = "${app.webDriver}")
    private String WEB_DRIVER_PATH;

    @Value(value = "${app.selenium.url}")
    private String SELENIUM_URL;


    private final By NAVBAR_WIDGET_BUTTON = By.xpath("//a[contains(@class, 'dropdown-toggle menu-item-currencies')]");

    private final By USD_RATE_LINK = By.xpath("//a[@data-cy=\"usd\"]");

    private final By FIRST_LOOT_PRICE = By.xpath("(//div[contains(@class, 'tc-price')])[2]");

    private final By CURRENCY_SIGN = By.xpath("(//span[@class='unit'])[1]");

    public FunpayPOM getBasePage() {
        initDriver();
        webDriver.get(BASE_URL);

        return this;
    }

    @SneakyThrows
    public void initDriver() {
//        System.setProperty("webdriver.chrome.driver", WEB_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-webrtc");

//        options.addArguments("--remote-debugging-port=9222"); // не работает в контейнере
        options.addArguments("--disable-software-rasterizer"); // Исправляет баг с графикой
        options.addArguments("--disable-setuid-sandbox"); // Избегает проблем с правами доступа
        options.addArguments("--disable-features=VizDisplayCompositor"); // Убирает проблемы с рендерингом

        options.addArguments("--window-size=1920,1080");
        options.setCapability(CapabilityType.BROWSER_NAME, "chrome");
        options.setCapability(CapabilityType.PLATFORM_NAME, "LINUX");
//        webDriver = new ChromeDriver(options);
        webDriver = new RemoteWebDriver(URI.create("http://selenium-chrome:4444/wd/hub").toURL(), options);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
        wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
    }

    public BigDecimal getPrice(String currencySign) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(CURRENCY_SIGN, currencySign));
        String price = webDriver.findElement(FIRST_LOOT_PRICE).getAttribute("data-s");
        return new BigDecimal(Objects.requireNonNull(price));
    }

    public FunpayPOM openNavBarCurrencyWidget() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(NAVBAR_WIDGET_BUTTON)).getFirst().click();
        return this;
    }

    public FunpayPOM switchCurrencyToUSD() {
        wait.until(ExpectedConditions.elementToBeClickable(USD_RATE_LINK)).click();
        return this;
    }

    public void closeWebDriver() {
        if (webDriver != null) {
            try {
                webDriver.quit();
                System.out.println("WebDriver завершён корректно.");
            } catch (Exception e) {
                System.err.println("Ошибка при закрытии WebDriver: " + e.getMessage());
            }
        }
    }
}
