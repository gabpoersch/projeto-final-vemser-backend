package br.com.dbc.devser.colabore.functional.actions;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static br.com.dbc.devser.colabore.functional.pageObjects.NewFundraiser.*;

@RequiredArgsConstructor
public class NewFundraiserAction {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public NewFundraiserAction writeInputTitle(String text) {
        CommonActions.write(driver, wait, text, inputTitle);
        return this;
    }

    public NewFundraiserAction writeInputGoal(String text) {
        CommonActions.write(driver, wait, text, inputGoal);
        return this;
    }

    public NewFundraiserAction clickAutomaticClose() {
        driver.findElement(checkAutomaticClose).click();
        return this;
    }

    public NewFundraiserAction writeInputDate(String text) {
        CommonActions.write(driver, wait, text, inputDate);
        return this;
    }

    public NewFundraiserAction clickBtnCoverPhoto() {
        wait.until(ExpectedConditions
                        .elementToBeClickable(driver.findElement(btnCoverPhoto)))
                .sendKeys(System.getProperty("user.dir") + "/src/main/resources/img/doacao-foto.png");
        return this;
    }

    public NewFundraiserAction writeInputCategories(List<String> categories) {
        WebElement inputCreateCategories = wait.until(ExpectedConditions
                .visibilityOf(driver.findElement(inputCategories)));
        Actions act = new Actions(driver);
        categories.forEach(c -> {
            act.sendKeys(inputCreateCategories, c).build().perform();
            act.sendKeys(Keys.ENTER).build().perform();
        });
        return this;
    }

    public NewFundraiserAction writeDescription(String text) {
        CommonActions.write(driver, wait, text, textArea);
        return this;
    }

    public NewFundraiserAction clickBtnBack() {
        driver.findElement(buttonBackFundraiser).click();
        return this;
    }

    public NewFundraiserAction clickBtnSave() {
        driver.findElement(buttonSaveFundraiser).click();
        return this;
    }

}
