package br.com.dbc.devser.colabore.functional.actions;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static br.com.dbc.devser.colabore.functional.pageObjects.HomeColabore.*;

@RequiredArgsConstructor
public class HomeAction {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public HomeAction fundraiserHome() {
        wait.until(ExpectedConditions
                .elementToBeClickable(driver.findElement(exploreButton))).click();
        return this;
    }

    public HomeAction writeFundraiserToBeFound(String text) {
        CommomActions.write(driver, wait, text, searchBar);
        return this;
    }

    public HomeAction createFundraiser() {
        WebElement ele = driver.findElement(createButton);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].click()", ele);
        return this;
    }

    public HomeAction logout() {
        WebElement ele = driver.findElement(logoutButton);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].click()", ele);
        return this;
    }

}