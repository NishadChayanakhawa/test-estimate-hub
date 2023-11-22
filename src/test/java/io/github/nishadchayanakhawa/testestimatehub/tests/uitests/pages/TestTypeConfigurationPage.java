package io.github.nishadchayanakhawa.testestimatehub.tests.uitests.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import io.github.nishadchayanakhawa.testestimatehub.model.dto.TestTypeDTO;

public class TestTypeConfigurationPage extends BasePage {
	@FindBy(xpath = "//input[@id='name']")
	WebElement name;

	@FindBy(xpath = "//input[@id='relativeTestCaseCountPercentage']")
	WebElement relativeTestCaseCountPercentage;

	@FindBy(xpath = "//input[@id='reExecutionPercentage']")
	WebElement reExecutionPercentage;

	@FindBy(xpath = "//input[@id='additionalCycleExecutionPercentage']")
	WebElement additionalCycleExecutionPercentage;
	
	public TestTypeConfigurationPage(WebDriver driver) {
		super(driver);
	}

	private TestTypeConfigurationPage saveTestType(TestTypeDTO testType) {
		this.clearValue(name);
		this.sendText(name, testType.getName());
		this.clearValue(relativeTestCaseCountPercentage);
		this.sendText(relativeTestCaseCountPercentage, String.valueOf(testType.getRelativeTestCaseCountPercentage()));
		this.clearValue(reExecutionPercentage);
		this.sendText(reExecutionPercentage, String.valueOf(testType.getReExecutionPercentage()));
		this.clearValue(additionalCycleExecutionPercentage);
		this.sendText(additionalCycleExecutionPercentage,
				String.valueOf(testType.getAdditionalCycleExecutionPercentage()));
		this.clickElement(this.saveRecordButton);
		return this;
	}

	public TestTypeConfigurationPage addTestType(TestTypeDTO testType) {
		this.clickElement(this.addRecordButton);
		return this.saveTestType(testType);
	}
	
	public TestTypeConfigurationPage editTestType(TestTypeDTO testType) {
		this.clickEditButton(testType.getName());
		return this.saveTestType(testType);
	}
	
	public TestTypeConfigurationPage deleteTestType(TestTypeDTO testType) {
		this.clickDeleteButton(testType.getName());
		this.clickElement(this.confirmDeleteOperationButton);
		return this;
	}
}
