package io.github.nishadchayanakhawa.testestimatehub.tests.uitests;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import io.github.nishadchayanakhawa.testestimatehub.model.dto.TestTypeDTO;
import io.github.nishadchayanakhawa.testestimatehub.tests.uitests.pages.LoginPage;
import io.github.nishadchayanakhawa.testestimatehub.tests.uitests.pages.TestTypeConfigurationPage;
import io.nishadc.automationtestingframework.testngcustomization.TestFactory;
import io.nishadc.automationtestingframework.testngcustomization.annotations.Retry;

public class TestTypeConfigurationTests {
	@Retry(3)
	@Test
	public void addTestType() {
		LoginPage loginPage = LoginPage.getLoginPage();

		TestTypeDTO testType=new TestTypeDTO();
		testType.setName("SIT");
		testType.setRelativeTestCaseCountPercentage(100d);
		testType.setReExecutionPercentage(20d);
		testType.setAdditionalCycleExecutionPercentage(15d);
		
		TestFactory.recordTest("Add Test type", loginPage.getDriver());
		TestTypeConfigurationPage testTypeConfigurationPage=loginPage.login("admin", "admin").navigateToTestTypeConfiguration().addTestType(testType);
		
		String actualToastMessage=testTypeConfigurationPage.getToastMessage();
		Assertions.assertThat(actualToastMessage).isEqualTo("Test type 'SIT' saved successfully");
		TestFactory.recordTestStep(actualToastMessage, true);
		testTypeConfigurationPage.logout();
	}
	
	@Retry(3)
	@Test(dependsOnMethods= {"addTestType"})
	public void addDuplicateTestType() {
		LoginPage loginPage = LoginPage.getLoginPage();

		TestTypeDTO testType=new TestTypeDTO();
		testType.setName("SIT");
		testType.setRelativeTestCaseCountPercentage(0d);
		testType.setReExecutionPercentage(0d);
		testType.setAdditionalCycleExecutionPercentage(0d);
		
		TestFactory.recordTest("Add Duplicate Test type", loginPage.getDriver());
		TestTypeConfigurationPage testTypeConfigurationPage=loginPage.login("admin", "admin").navigateToTestTypeConfiguration().addTestType(testType);
		
		String actualToastMessage=testTypeConfigurationPage.getToastMessage();
		Assertions.assertThat(actualToastMessage).isEqualTo("Name 'SIT' already exists for another Test Type");
		TestFactory.recordTestStep(actualToastMessage, true);
		testTypeConfigurationPage.logout();
	}
	
	@Retry(3)
	@Test
	public void addInvalidTestType() {
		LoginPage loginPage = LoginPage.getLoginPage();

		TestTypeDTO testType=new TestTypeDTO();
		testType.setName("Invalid");
		testType.setRelativeTestCaseCountPercentage(-1d);
		testType.setReExecutionPercentage(0d);
		testType.setAdditionalCycleExecutionPercentage(0d);
		
		TestFactory.recordTest("Add Invalid Test type", loginPage.getDriver());
		TestTypeConfigurationPage testTypeConfigurationPage=loginPage.login("admin", "admin").navigateToTestTypeConfiguration().addTestType(testType);
		
		String actualToastMessage=testTypeConfigurationPage.getToastMessage();
		Assertions.assertThat(actualToastMessage).isEqualTo("Relative Test Case Count Percentage cannot be lower than 0");
		TestFactory.recordTestStep(actualToastMessage, true);
		testTypeConfigurationPage.logout();
	}
	
	@Retry(3)
	@Test(dependsOnMethods= {"addTestType"})
	public void updateTestType() {
		LoginPage loginPage = LoginPage.getLoginPage();

		TestTypeDTO testType=new TestTypeDTO();
		testType.setName("SIT");
		testType.setRelativeTestCaseCountPercentage(1d);
		testType.setReExecutionPercentage(2d);
		testType.setAdditionalCycleExecutionPercentage(5d);
		
		TestFactory.recordTest("Update Test type", loginPage.getDriver());
		TestTypeConfigurationPage testTypeConfigurationPage=loginPage.login("admin", "admin").navigateToTestTypeConfiguration().editTestType(testType);
		
		String actualToastMessage=testTypeConfigurationPage.getToastMessage();
		Assertions.assertThat(actualToastMessage).isEqualTo("Test type 'SIT' saved successfully");
		TestFactory.recordTestStep(actualToastMessage, true);
		testTypeConfigurationPage.logout();
	}
	
	@Retry(3)
	@Test(dependsOnMethods= {"updateTestType"})
	public void deleteTestType() {
		LoginPage loginPage = LoginPage.getLoginPage();

		TestTypeDTO testType=new TestTypeDTO();
		testType.setName("SIT");
		
		TestFactory.recordTest("Delete Test type", loginPage.getDriver());
		TestTypeConfigurationPage testTypeConfigurationPage=loginPage.login("admin", "admin").navigateToTestTypeConfiguration().deleteTestType(testType);
		
		String actualToastMessage=testTypeConfigurationPage.getToastMessage();
		Assertions.assertThat(actualToastMessage).isEqualTo("Test type 'SIT' deleted successfully");
		TestFactory.recordTestStep(actualToastMessage, true);
		testTypeConfigurationPage.logout();
	}
}
