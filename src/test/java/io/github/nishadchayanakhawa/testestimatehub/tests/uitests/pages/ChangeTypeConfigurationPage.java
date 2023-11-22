package io.github.nishadchayanakhawa.testestimatehub.tests.uitests.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeTypeDTO;

public class ChangeTypeConfigurationPage extends BasePage {
	@FindBy(xpath = "//input[@id='name']")
	WebElement name;

	@FindBy(xpath = "//input[@id='testCaseCountModifier']")
	WebElement testCaseCountModifier;

	@FindBy(xpath = "//input[@id='testPlanningEffortAllocationPercentage']")
	WebElement testPlanningEffortAllocationPercentage;

	@FindBy(xpath = "//input[@id='testPreparationEffortAllocationPercentage']")
	WebElement testPreparationEffortAllocationPercentage;
	
	@FindBy(xpath = "//input[@id='managementEffortAllocationPercentage']")
	WebElement managementEffortAllocationPercentage;

	public ChangeTypeConfigurationPage(WebDriver driver) {
		super(driver);
	}

	private ChangeTypeConfigurationPage saveChangeType(ChangeTypeDTO changeType) {
		this.clearValue(name);
		this.sendText(name, changeType.getName());
		this.clearValue(testCaseCountModifier);
		this.sendText(testCaseCountModifier, String.valueOf(changeType.getTestCaseCountModifier()));
		this.clearValue(testPlanningEffortAllocationPercentage);
		this.sendText(testPlanningEffortAllocationPercentage, String.valueOf(changeType.getTestPlanningEffortAllocationPercentage()));
		this.clearValue(testPreparationEffortAllocationPercentage);
		this.sendText(testPreparationEffortAllocationPercentage,
				String.valueOf(changeType.getTestPreparationEffortAllocationPercentage()));
		this.clearValue(managementEffortAllocationPercentage);
		this.sendText(managementEffortAllocationPercentage,
				String.valueOf(changeType.getManagementEffortAllocationPercentage()));
		this.clickElement(this.saveRecordButton);
		return this;
	}

	public ChangeTypeConfigurationPage addChangeType(ChangeTypeDTO changeType) {
		this.clickElement(this.addRecordButton);
		return this.saveChangeType(changeType);
	}
	
	public ChangeTypeConfigurationPage editChangeType(ChangeTypeDTO changeType) {
		this.clickEditButton(changeType.getName());
		return this.saveChangeType(changeType);
	}
	
	public ChangeTypeConfigurationPage deleteChangeType(ChangeTypeDTO changeType) {
		this.clickDeleteButton(changeType.getName());
		this.clickElement(this.confirmDeleteOperationButton);
		return this;
	}

}
