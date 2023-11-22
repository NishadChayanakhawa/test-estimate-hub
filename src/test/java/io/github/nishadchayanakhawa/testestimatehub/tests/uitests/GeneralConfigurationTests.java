package io.github.nishadchayanakhawa.testestimatehub.tests.uitests;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import io.github.nishadchayanakhawa.testestimatehub.tests.uitests.pages.LoginPage;
import io.nishadc.automationtestingframework.testngcustomization.TestFactory;
import io.nishadc.automationtestingframework.testngcustomization.annotations.Retry;
import io.github.nishadchayanakhawa.testestimatehub.tests.uitests.pages.GeneralConfigurationPage;

public class GeneralConfigurationTests {
	@Retry(3)
	@Test
	public void saveGeneralConfiguration() {
		LoginPage loginPage = LoginPage.getLoginPage();
		
		TestFactory.recordTest("Save general configuration", loginPage.getDriver());
		GeneralConfigurationPage generalConfigurationPage=loginPage.login("admin", "admin").navigateToGeneralConfiguration().save(33d,10d,20d);
		
		String actualToastMessage=generalConfigurationPage.getToastMessage();
		Assertions.assertThat(actualToastMessage).isEqualTo("General setting saved successfully");
		TestFactory.recordTestStep(actualToastMessage, true);
		generalConfigurationPage.logout();
	}
	
	@Retry(3)
	@Test
	public void negativeGeneralConfiguration() {
		LoginPage loginPage = LoginPage.getLoginPage();
		
		TestFactory.recordTest("Save invalid general configuration", loginPage.getDriver());
		GeneralConfigurationPage generalConfigurationPage=loginPage.login("admin", "admin").navigateToGeneralConfiguration().save(33d,11d,20d);
		
		String actualToastMessage=generalConfigurationPage.getToastMessage();
		Assertions.assertThat(actualToastMessage).isEqualTo("Test complexity weightage percentages must add up to 100.");
		TestFactory.recordTestStep(actualToastMessage, true);
		generalConfigurationPage.logout();
	}
	
	@Retry(3)
	@Test
	public void inalidDistributionGeneralConfiguration() {
		LoginPage loginPage = LoginPage.getLoginPage();
		
		TestFactory.recordTest("Save invalid general weightage distribution", loginPage.getDriver());
		GeneralConfigurationPage generalConfigurationPage=loginPage.login("admin", "admin").navigateToGeneralConfiguration().save(33d,-10d,40d);
		
		String actualToastMessage=generalConfigurationPage.getToastMessage();
		Assertions.assertThat(actualToastMessage).isEqualTo("testConfigurationComplexityPercentage cannot be lower than 0");
		TestFactory.recordTestStep(actualToastMessage, true);
		generalConfigurationPage.logout();
	}
}
