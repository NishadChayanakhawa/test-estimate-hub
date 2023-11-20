package io.github.nishadchayanakhawa.testestimatehub.tests.uitests.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import io.github.nishadchayanakhawa.testestimatehub.model.dto.UserDTO;

public class UserManagementPage extends BasePage{
	
	@FindBy(xpath="//*[@id='username']")
	WebElement username;
	
	@FindBy(xpath="//*[@id='password']")
	WebElement password;
	
	@FindBy(xpath="//*[@id='firstName']")
	WebElement firstName;
	
	@FindBy(xpath="//*[@id='lastName']")
	WebElement lastName;
	
	@FindBy(xpath="//*[@id='email']")
	WebElement email;

	public UserManagementPage(WebDriver driver) {
		super(driver);
	}
	
	private UserManagementPage saveUser(UserDTO user) {
		this.clearValue(username);
		this.sendText(username, user.getUsername());
		this.clearValue(password);
		this.sendText(password, user.getPassword());
		this.clearValue(firstName);
		this.sendText(firstName, user.getFirstName());
		this.clearValue(lastName);
		this.sendText(lastName, user.getLastName());
		this.clearValue(email);
		this.sendText(email, user.getEmail());
		this.clickElement(this.saveRecordButton);
		return this;
	}
	
	public UserManagementPage addUser(UserDTO user) {
		this.clickElement(this.addRecordButton);
		return saveUser(user);
	}
	
	public UserManagementPage editUser(UserDTO user) {
		this.clickEditButton(user.getUsername());
		return saveUser(user);
	}
	
	public UserManagementPage deleteUser(String username) {
		this.clickDeleteButton(username);
		this.clickElement(this.confirmDeleteOperationButton);
		return this;
	}

}