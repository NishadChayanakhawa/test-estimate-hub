package io.github.nishadchayanakhawa.testestimatehub.tests.uitests.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GeneralConfigurationPage extends BasePage {
	
	@FindBy(xpath="//input[@id='testExecutionProductivity-VERY_LOW']")
	WebElement testExecutionProductivityVeryLow;
	
	@FindBy(xpath="//input[@id='testConfigurationComplexityPercentage']")
	WebElement testConfigurationComplexityPercentage;
	
	@FindBy(xpath="//input[@id='testDataComplexityPercentage']")
	WebElement testDataComplexityPercentage;
	
	@FindBy(xpath="//button[@id='saveGeneralConfigurationButton']")
	WebElement saveGeneralConfigurationButton;

	public GeneralConfigurationPage(WebDriver driver) {
		super(driver);
	}
	
	public GeneralConfigurationPage save(Double testExecutionProductivityVeryLow,Double testConfigurationComplexityPercentage,Double testDataComplexityPercentage) {
		this.clearValue(this.testExecutionProductivityVeryLow);
		this.sendText(this.testExecutionProductivityVeryLow, String.valueOf(testExecutionProductivityVeryLow));
		this.clearValue(this.testConfigurationComplexityPercentage);
		this.sendText(this.testConfigurationComplexityPercentage, String.valueOf(testConfigurationComplexityPercentage));
		this.clearValue(this.testDataComplexityPercentage);
		this.sendText(this.testDataComplexityPercentage, String.valueOf(testDataComplexityPercentage));
		this.clickElement(this.saveGeneralConfigurationButton);
		return this;
	}

}
