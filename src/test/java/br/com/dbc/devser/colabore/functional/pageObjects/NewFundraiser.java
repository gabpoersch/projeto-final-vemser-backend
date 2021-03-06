package br.com.dbc.devser.colabore.functional.pageObjects;

import org.openqa.selenium.By;

public class NewFundraiser extends HomeColabore {

    public static By inputTitle = By.id("title");
    public static By inputGoal = By.id("goal");
    public static By checkAutomaticClose = By.id("automaticClose");
    public static By inputDate = By.id("endingDate");
    public static By btnCoverPhoto = By.id("coverPhoto");
    public static By inputCategories = By.xpath("//*[@id=\"categories\"]/div/div[1]/div[2]");
    public static By textArea = By.id("description");
    public static By buttonBackFundraiser = By.xpath("//*[@id=\"root\"]/div/div[1]/button");
    public static By buttonSaveFundraiser = By.cssSelector("button[type='submit']");


}
