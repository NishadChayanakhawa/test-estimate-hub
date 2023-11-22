package io.github.nishadchayanakhawa.testestimatehub.tests.uitests.pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import io.nishadc.automationtestingframework.testinginterface.webui.ApplicationActions;

public class BasePage extends ApplicationActions {	
	@FindBy(xpath="//a[@id=\"userLink\"]")
	WebElement userLink;
	
	@FindBy(xpath="//button[@id=\"logoutButton\"]")
	WebElement logoutButton;
	
	@FindBy(xpath="//*[@id=\"configurationsLink\"]")
	WebElement configurationLink;
	
	@FindBy(xpath="//*[@id=\"estimationLink\"]")
	WebElement estimationLink;
	
	@FindBy(xpath="//*[@id=\"userManagementOption\"]")
	WebElement userManagementOption;
	
	@FindBy(xpath="//*[@id=\"applicationConfigurationOption\"]")
	WebElement applicationConfigurationOption;
	
	@FindBy(xpath="//*[@id=\"releaseRecordOption\"]")
	WebElement releaseRecordOption;
	
	@FindBy(xpath="//*[@id=\"testTypeConfigurationOption\"]")
	WebElement testTypeConfigurationOption;
	
	@FindBy(xpath="//*[@id=\"changeTypeConfigurationOption\"]")
	WebElement changeTypeConfigurationOption;
	
	@FindBy(xpath="//*[@id=\"generalConfigurationOption\"]")
	WebElement generalConfigurationOption;
	
	@FindBy(xpath="//button[@class='navbar-toggler']")
	WebElement navbarToggler;
	
	@FindBy(xpath="//*[@id=\"toast-container\"]/div/div[2]")
	WebElement visibleToastMessage;
	
	@FindBy(xpath="//*[@id=\"toast-container\"]/div/button")
	WebElement closeToastMessage;
	
	@FindBy(xpath="//button[@id='dismissModal']")
	WebElement dismissModalButton;
	
	@FindBy(xpath="//button[@id='addRecordButton']")
	WebElement addRecordButton;
	
	@FindBy(xpath="//button[@id='saveRecordButton']")
	WebElement saveRecordButton;
	
	@FindBy(xpath="//button[@id='confirmDeleteOperation']")
	WebElement confirmDeleteOperationButton;
	
	private static final String EDIT_BUTTON_XPATH_TEMPLATE=
			"//td[text()='<RECORD_NAME>']/parent::tr//button[starts-with(@id,'editRecordButton_')]";
	
	private static final String DELETE_BUTTON_XPATH_TEMPLATE=
			"//td[text()='<RECORD_NAME>']/parent::tr//button[starts-with(@id,'deleteRecordButton_')]";
	
	private static final String RECORD_NAME_PLACEHOLDER="<RECORD_NAME>";
	
	public void clickEditButton(String recordName) {
		this.clickElement(BasePage.EDIT_BUTTON_XPATH_TEMPLATE.replace(BasePage.RECORD_NAME_PLACEHOLDER, recordName));
	}
	
	public void clickDeleteButton(String recordName) {
		this.clickElement(BasePage.DELETE_BUTTON_XPATH_TEMPLATE.replace(BasePage.RECORD_NAME_PLACEHOLDER, recordName));
	}

	public BasePage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public WebDriver getDriver() {
		return this.driver;
	}
	
	public void logout() {
		this.clickNavbarTogglerIfAvailable();
		this.clickElement(userLink);
		this.clickElement(logoutButton);
		this.driver.quit();
	}
	
	private void clickNavbarTogglerIfAvailable() {
		if(this.isDisplayed(navbarToggler)) {
			this.clickElement(navbarToggler);
		}
	}
	
	public UserManagementPage navigateToUserManagement() {
		this.clickNavbarTogglerIfAvailable();
		this.clickElement(configurationLink);
		this.clickElement(userManagementOption);
		return new UserManagementPage(this.driver);
	}
	
	public ApplicationConfigurationPage navigateToApplicationConfiguration() {
		this.clickNavbarTogglerIfAvailable();
		this.clickElement(configurationLink);
		this.clickElement(applicationConfigurationOption);
		return new ApplicationConfigurationPage(this.driver);
	}
	
	public ChangeTypeConfigurationPage navigateToChangeTypeConfiguration() {
		this.clickNavbarTogglerIfAvailable();
		this.clickElement(configurationLink);
		this.clickElement(this.changeTypeConfigurationOption);
		return new ChangeTypeConfigurationPage(this.driver);
	}
	
	public TestTypeConfigurationPage navigateToTestTypeConfiguration() {
		this.clickNavbarTogglerIfAvailable();
		this.clickElement(configurationLink);
		this.clickElement(this.testTypeConfigurationOption);
		return new TestTypeConfigurationPage(this.driver);
	}
	
	public GeneralConfigurationPage navigateToGeneralConfiguration() {
		this.clickNavbarTogglerIfAvailable();
		this.clickElement(configurationLink);
		this.clickElement(this.generalConfigurationOption);
		return new GeneralConfigurationPage(this.driver);
	}
	
	public String getToastMessage() {
		String toastMessage=this.getInnerText(visibleToastMessage);
		this.clickElement(closeToastMessage);
		try {
		if(this.isDisplayed(dismissModalButton)) {
			this.clickElement(dismissModalButton);
		}
		}catch(NoSuchElementException e) {
			//do nothing
		}
		return toastMessage;
	}
}