package com.overridetech.funpay_monitor.selenium.page;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class FunpayPOM {

    private WebDriver webDriver;
    private Wait<WebDriver> wait;

    @Value(value = "${funpay.base-url}")
    private String BASE_URL;

    private final By NAVBAR_WIDGET_BUTTON = By.xpath("//a[contains(@class, 'dropdown-toggle menu-item-currencies')]");

    private final By USD_RATE_LINK = By.xpath("//a[@data-cy=\"usd\"]");

    private final By FIRST_LOOT_PRICE = By.xpath("(//div[contains(@class, 'tc-price')])[2]");

    private final By CURRENCY_SIGN = By.xpath("(//span[@class='unit'])[1]");

    public FunpayPOM getBasePage() {
        initDriver();
        webDriver.get(BASE_URL);

        return this;
    }

    public void initDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        webDriver = new ChromeDriver(options);
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
        webDriver.close();
    }
}
