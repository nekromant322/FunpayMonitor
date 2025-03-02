package com.overridetech.funpay_monitor.selenium.page;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
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
    private String baseUrl;

    private final By navbarWidgetButton = By.xpath("//a[contains(@class, 'dropdown-toggle menu-item-currencies')]");

    private final By usdRateLink = By.xpath("//a[@data-cy=\"usd\"]");

    private final By firstLootPrice = By.xpath("(//div[contains(@class, 'tc-price')])[2]");

    private final By CurrencySign = By.xpath("(//span[@class='unit'])[1]");

    public FunpayPOM getBasePage() {
        webDriver = new ChromeDriver();
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
        webDriver.get(baseUrl);

        wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

        return this;
    }

    public BigDecimal getPrice(String currencySign) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(CurrencySign, currencySign));
        String price = webDriver.findElement(firstLootPrice).getAttribute("data-s");
        return new BigDecimal(Objects.requireNonNull(price));
    }

    public FunpayPOM openNavBarCurrencyWidget() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(navbarWidgetButton)).getFirst().click();
        return this;
    }

    public FunpayPOM switchCurrencyToUSD() {
        wait.until(ExpectedConditions.elementToBeClickable(usdRateLink)).click();
        return this;
    }

    public void closeWebDriver() {
        webDriver.close();
    }
}
