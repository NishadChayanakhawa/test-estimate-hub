package io.github.nishadchayanakhawa.testestimatehub.tests.uitests.pages;

import java.util.logging.Level;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginPage extends BasePage {

	@FindBy(xpath="//*[@id='username']")
	WebElement username;
	
	@FindBy(xpath="//*[@id='password']")
	WebElement password;
	
	@FindBy(xpath="//button[text()='Sign in']")
	WebElement loginButton;
	
	public LoginPage(WebDriver driver) {
		super(driver);
	}
	
	public static LoginPage getLoginPage() {
		java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless=new");
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("start-maximized");
		options.addArguments("--window-size=1920,1080");
		WebDriver driver=WebDriverManager.chromedriver().capabilities(options).create();
		driver.get("http://localhost:8999/login");
		return new LoginPage(driver);
	}
	
	public HomePage login(String username,String password) {
		this.sendText(this.username, username);
		this.sendText(this.password, password);
		this.clickElement(this.loginButton);
		return new HomePage(this.driver);
	}
}