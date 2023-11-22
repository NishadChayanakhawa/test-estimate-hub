package io.github.nishadchayanakhawa.testestimatehub.tests.uitests;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeTypeDTO;
import io.github.nishadchayanakhawa.testestimatehub.tests.uitests.pages.LoginPage;
import io.github.nishadchayanakhawa.testestimatehub.tests.uitests.pages.ChangeTypeConfigurationPage;
import io.nishadc.automationtestingframework.testngcustomization.TestFactory;
import io.nishadc.automationtestingframework.testngcustomization.annotations.Retry;

public class ChangeTypeConfigurationTests {
	@Retry(3)
	@Test
	public void addChangeType() {
		LoginPage loginPage = LoginPage.getLoginPage();

		ChangeTypeDTO changeType=new ChangeTypeDTO();
		changeType.setName("Significant");
		changeType.setTestCaseCountModifier(1.2d);
		changeType.setTestPlanningEffortAllocationPercentage(20d);
		changeType.setTestPreparationEffortAllocationPercentage(15d);
		changeType.setManagementEffortAllocationPercentage(10d);
		
		TestFactory.recordTest("Add Change type", loginPage.getDriver());
		ChangeTypeConfigurationPage changeTypeConfigurationPage=loginPage.login("admin", "admin").navigateToChangeTypeConfiguration().addChangeType(changeType);
		
		String actualToastMessage=changeTypeConfigurationPage.getToastMessage();
		Assertions.assertThat(actualToastMessage).isEqualTo("Change type 'Significant' saved successfully");
		TestFactory.recordTestStep(actualToastMessage, true);
		changeTypeConfigurationPage.logout();
	}
	
	@Retry(3)
	@Test(dependsOnMethods= {"addChangeType"})
	public void addDuplicateChangeType() {
		LoginPage loginPage = LoginPage.getLoginPage();

		ChangeTypeDTO changeType=new ChangeTypeDTO();
		changeType.setName("Significant");
		changeType.setTestCaseCountModifier(1.3d);
		changeType.setTestPlanningEffortAllocationPercentage(21d);
		changeType.setTestPreparationEffortAllocationPercentage(16d);
		changeType.setManagementEffortAllocationPercentage(11d);
		
		TestFactory.recordTest("Add Duplicate Change type", loginPage.getDriver());
		ChangeTypeConfigurationPage changeTypeConfigurationPage=loginPage.login("admin", "admin").navigateToChangeTypeConfiguration().addChangeType(changeType);
		
		String actualToastMessage=changeTypeConfigurationPage.getToastMessage();
		Assertions.assertThat(actualToastMessage).isEqualTo("Name 'Significant' already exists for another Change Type");
		TestFactory.recordTestStep(actualToastMessage, true);
		changeTypeConfigurationPage.logout();
	}
	
	@Retry(3)
	@Test
	public void addInvalidChangeType() {
		LoginPage loginPage = LoginPage.getLoginPage();

		ChangeTypeDTO changeType=new ChangeTypeDTO();
		changeType.setName("Invalid");
		changeType.setTestCaseCountModifier(-1.3d);
		changeType.setTestPlanningEffortAllocationPercentage(100d);
		changeType.setTestPreparationEffortAllocationPercentage(16d);
		changeType.setManagementEffortAllocationPercentage(11d);
		
		TestFactory.recordTest("Add Invalid Change type", loginPage.getDriver());
		ChangeTypeConfigurationPage changeTypeConfigurationPage=loginPage.login("admin", "admin").navigateToChangeTypeConfiguration().addChangeType(changeType);
		
		String actualToastMessage=changeTypeConfigurationPage.getToastMessage();
		Assertions.assertThat(actualToastMessage).isEqualTo("Test Case Count Modifier cannot be lower than 0");
		TestFactory.recordTestStep(actualToastMessage, true);
		changeTypeConfigurationPage.logout();
	}
	
	@Retry(3)
	@Test(dependsOnMethods= {"addChangeType"})
	public void updateChangeType() {
		LoginPage loginPage = LoginPage.getLoginPage();

		ChangeTypeDTO changeType=new ChangeTypeDTO();
		changeType.setName("Significant");
		changeType.setTestCaseCountModifier(1.3d);
		changeType.setTestPlanningEffortAllocationPercentage(100d);
		changeType.setTestPreparationEffortAllocationPercentage(16d);
		changeType.setManagementEffortAllocationPercentage(11d);
		
		TestFactory.recordTest("Update Change type", loginPage.getDriver());
		ChangeTypeConfigurationPage changeTypeConfigurationPage=loginPage.login("admin", "admin").navigateToChangeTypeConfiguration().editChangeType(changeType);
		
		String actualToastMessage=changeTypeConfigurationPage.getToastMessage();
		Assertions.assertThat(actualToastMessage).isEqualTo("Change type 'Significant' saved successfully");
		TestFactory.recordTestStep(actualToastMessage, true);
		changeTypeConfigurationPage.logout();
	}
	
	@Retry(3)
	@Test(dependsOnMethods= {"updateChangeType"})
	public void deleteChangeType() {
		LoginPage loginPage = LoginPage.getLoginPage();

		ChangeTypeDTO changeType=new ChangeTypeDTO();
		changeType.setName("Significant");
		
		TestFactory.recordTest("Delete Change type", loginPage.getDriver());
		ChangeTypeConfigurationPage changeTypeConfigurationPage=loginPage.login("admin", "admin").navigateToChangeTypeConfiguration().deleteChangeType(changeType);
		
		String actualToastMessage=changeTypeConfigurationPage.getToastMessage();
		Assertions.assertThat(actualToastMessage).isEqualTo("Change type 'Significant' deleted successfully");
		TestFactory.recordTestStep(actualToastMessage, true);
		changeTypeConfigurationPage.logout();
	}
}
