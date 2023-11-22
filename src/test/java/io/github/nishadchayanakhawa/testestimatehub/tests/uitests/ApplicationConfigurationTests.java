package io.github.nishadchayanakhawa.testestimatehub.tests.uitests;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.ApplicationConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.tests.uitests.pages.LoginPage;
import io.nishadc.automationtestingframework.testngcustomization.TestFactory;
import io.nishadc.automationtestingframework.testngcustomization.annotations.Retry;
import io.github.nishadchayanakhawa.testestimatehub.tests.uitests.pages.ApplicationConfigurationPage;

public class ApplicationConfigurationTests {
	@Retry(3)
	@Test
	public void add() {
		LoginPage loginPage = LoginPage.getLoginPage();

		ApplicationConfigurationDTO applicationConfiguration = new ApplicationConfigurationDTO();
		applicationConfiguration.setApplication("TEH");
		applicationConfiguration.setModule("Configuration");
		applicationConfiguration.setSubModule("User Management");
		applicationConfiguration.setBaseTestScriptCount(9);

		TestFactory.recordTest("Add application configuration", loginPage.getDriver());
		ApplicationConfigurationPage applicationConfigurationPage = loginPage.login("admin", "admin")
				.navigateToApplicationConfiguration().add(applicationConfiguration);

		String actualToastMessage = applicationConfigurationPage.getToastMessage();
		Assertions.assertThat(actualToastMessage)
				.isEqualTo("Application configuration 'TEH-Configuration-User Management' saved successfully");
		TestFactory.recordTestStep(actualToastMessage, true);
		applicationConfigurationPage.logout();
	}
	
	@Retry(3)
	@Test
	public void addInvalid() {
		LoginPage loginPage = LoginPage.getLoginPage();

		ApplicationConfigurationDTO applicationConfiguration = new ApplicationConfigurationDTO();
		applicationConfiguration.setApplication("TEH");
		applicationConfiguration.setModule("Configuration");
		applicationConfiguration.setSubModule("Test Type Management");
		applicationConfiguration.setBaseTestScriptCount(0);

		TestFactory.recordTest("Add invalid application configuration", loginPage.getDriver());
		ApplicationConfigurationPage applicationConfigurationPage = loginPage.login("admin", "admin")
				.navigateToApplicationConfiguration().add(applicationConfiguration);

		String actualToastMessage = applicationConfigurationPage.getToastMessage();
		Assertions.assertThat(actualToastMessage)
				.isEqualTo("Base Test Script Count cannot be lower than 1");
		TestFactory.recordTestStep(actualToastMessage, true);
		applicationConfigurationPage.logout();
	}
	
	@Retry(3)
	@Test(dependsOnMethods= {"add"})
	public void addDuplicate() {
		LoginPage loginPage = LoginPage.getLoginPage();

		ApplicationConfigurationDTO applicationConfiguration = new ApplicationConfigurationDTO();
		applicationConfiguration.setApplication("TEH");
		applicationConfiguration.setModule("Configuration");
		applicationConfiguration.setSubModule("User Management");
		applicationConfiguration.setBaseTestScriptCount(9);

		TestFactory.recordTest("Add duplicate application configuration", loginPage.getDriver());
		ApplicationConfigurationPage applicationConfigurationPage = loginPage.login("admin", "admin")
				.navigateToApplicationConfiguration().add(applicationConfiguration);

		String actualToastMessage = applicationConfigurationPage.getToastMessage();
		Assertions.assertThat(actualToastMessage)
				.isEqualTo("Application-module-sub Module 'TEH-Configuration-User Management' already exists for another Application Configuration");
		TestFactory.recordTestStep(actualToastMessage, true);
		applicationConfigurationPage.logout();
	}
	
	@Retry(3)
	@Test(dependsOnMethods= {"addDuplicate"})
	public void update() {
		LoginPage loginPage = LoginPage.getLoginPage();

		ApplicationConfigurationDTO applicationConfiguration = new ApplicationConfigurationDTO();
		applicationConfiguration.setApplication("TEH");
		applicationConfiguration.setModule("Configuration");
		applicationConfiguration.setSubModule("Test Type Management");
		applicationConfiguration.setBaseTestScriptCount(3);

		TestFactory.recordTest("Update application configuration", loginPage.getDriver());
		ApplicationConfigurationPage applicationConfigurationPage = loginPage.login("admin", "admin")
				.navigateToApplicationConfiguration().edit(applicationConfiguration);

		String actualToastMessage = applicationConfigurationPage.getToastMessage();
		Assertions.assertThat(actualToastMessage)
				.isEqualTo("Application configuration 'TEH-Configuration-Test Type Management' saved successfully");
		TestFactory.recordTestStep(actualToastMessage, true);
		applicationConfigurationPage.logout();
	}
	
	@Retry(3)
	@Test(dependsOnMethods= {"update"})
	public void delete() {
		LoginPage loginPage = LoginPage.getLoginPage();

		ApplicationConfigurationDTO applicationConfiguration = new ApplicationConfigurationDTO();
		applicationConfiguration.setApplication("TEH");

		TestFactory.recordTest("Delete application configuration", loginPage.getDriver());
		ApplicationConfigurationPage applicationConfigurationPage = loginPage.login("admin", "admin")
				.navigateToApplicationConfiguration().delete(applicationConfiguration);

		String actualToastMessage = applicationConfigurationPage.getToastMessage();
		Assertions.assertThat(actualToastMessage)
				.isEqualTo("Application configuration 'TEH - Configuration - Test Type Management' deleted successfully");
		TestFactory.recordTestStep(actualToastMessage, true);
		applicationConfigurationPage.logout();
	}
}