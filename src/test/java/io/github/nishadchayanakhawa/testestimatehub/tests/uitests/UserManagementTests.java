package io.github.nishadchayanakhawa.testestimatehub.tests.uitests;

import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.testng.annotations.Test;

import io.github.nishadchayanakhawa.testestimatehub.tests.uitests.pages.LoginPage;
import io.nishadc.automationtestingframework.testngcustomization.TestFactory;
import io.nishadc.automationtestingframework.filehandling.JsonFileHandling;
import io.nishadc.automationtestingframework.filehandling.exceptions.FlatFileHandlingException;
import io.nishadc.automationtestingframework.filehandling.exceptions.JsonFileHandlingException;
import io.nishadc.automationtestingframework.testngcustomization.annotations.Retry;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.UserDTO;
import io.github.nishadchayanakhawa.testestimatehub.tests.uitests.pages.UserManagementPage;

public class UserManagementTests {
	@Retry(3)
	@Test
	public void addUser() throws JSONException, FlatFileHandlingException, JsonFileHandlingException {
		LoginPage loginPage = LoginPage.getLoginPage();
		System.out.println(JsonFileHandling.getJsonFileContent("src/test/resources/RequestBodyTemplates/User/addUser.json").toString(1));
		UserDTO user = new UserDTO();
		user.setUsername("johnd");
		user.setPassword("johnd");
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setEmail("john.d@company.com");

		TestFactory.recordTest("Add user", loginPage.getDriver());
		UserManagementPage userManagementPage = loginPage.login("admin", "admin").navigateToUserManagement()
				.addUser(user);

		String actualToastMessage = userManagementPage.getToastMessage();
		Assertions.assertThat(actualToastMessage).isEqualTo("User 'johnd' saved successfully");
		TestFactory.recordTestStep(actualToastMessage, true);
		userManagementPage.logout();
	}

	@Retry(3)
	@Test(dependsOnMethods = { "addUser" })
	public void addUser_duplicateUsername() {
		LoginPage loginPage = LoginPage.getLoginPage();

		UserDTO user = new UserDTO();
		user.setUsername("johnd");
		user.setPassword("johnd");
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setEmail("john.d1@company.com");

		TestFactory.recordTest("Add user with duplicate username", loginPage.getDriver());
		UserManagementPage userManagementPage = loginPage.login("admin", "admin").navigateToUserManagement()
				.addUser(user);

		String actualToastMessage = userManagementPage.getToastMessage();
		Assertions.assertThat(actualToastMessage).isEqualTo("Username 'johnd' already exists for another User");
		TestFactory.recordTestStep(actualToastMessage, true);
		userManagementPage.logout();
	}

	@Retry(3)
	@Test(dependsOnMethods = { "addUser" })
	public void addUser_duplicateEmail() {
		LoginPage loginPage = LoginPage.getLoginPage();

		UserDTO user = new UserDTO();
		user.setUsername("johnd1");
		user.setPassword("johnd");
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setEmail("john.d@company.com");

		TestFactory.recordTest("Add user with duplicate email", loginPage.getDriver());
		UserManagementPage userManagementPage = loginPage.login("admin", "admin").navigateToUserManagement()
				.addUser(user);

		String actualToastMessage = userManagementPage.getToastMessage();
		Assertions.assertThat(actualToastMessage)
				.isEqualTo("Email 'john.d@company.com' already exists for another User");
		TestFactory.recordTestStep(actualToastMessage, true);
		userManagementPage.logout();
	}

	@Retry(3)
	@Test(dependsOnMethods = { "addUser" })
	public void editUser_withPassword() {
		LoginPage loginPage = LoginPage.getLoginPage();

		UserDTO user = new UserDTO();
		user.setUsername("johnd");
		user.setPassword("new-password");
		user.setFirstName("Mr. John");
		user.setLastName("Doe");
		user.setEmail("john.d@company.com");

		TestFactory.recordTest("Edit user with password change", loginPage.getDriver());
		UserManagementPage userManagementPage = loginPage.login("admin", "admin").navigateToUserManagement()
				.editUser(user);

		String actualToastMessage = userManagementPage.getToastMessage();
		Assertions.assertThat(actualToastMessage).isEqualTo("User 'johnd' saved successfully");
		TestFactory.recordTestStep(actualToastMessage, true);
		userManagementPage.logout();
	}

	@Retry(3)
	@Test(dependsOnMethods = { "editUser_withPassword" })
	public void editUser_withoutPassword() {
		LoginPage loginPage = LoginPage.getLoginPage();

		UserDTO user = new UserDTO();
		user.setUsername("johnd");
		user.setPassword("");
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setEmail("john.d@company.com");

		TestFactory.recordTest("Edit user but no password change", loginPage.getDriver());
		UserManagementPage userManagementPage = loginPage.login("admin", "admin").navigateToUserManagement()
				.editUser(user);

		String actualToastMessage = userManagementPage.getToastMessage();
		Assertions.assertThat(actualToastMessage).isEqualTo("User 'johnd' saved successfully");
		TestFactory.recordTestStep(actualToastMessage, true);
		userManagementPage.logout();
	}

	@Retry(3)
	@Test(dependsOnMethods = { "addUser_duplicateUsername", "addUser_duplicateEmail", "editUser_withoutPassword" })
	public void deleteUser() {
		LoginPage loginPage = LoginPage.getLoginPage();

		TestFactory.recordTest("Delete user", loginPage.getDriver());
		UserManagementPage userManagementPage = loginPage.login("admin", "admin").navigateToUserManagement()
				.deleteUser("johnd");

		String actualToastMessage = userManagementPage.getToastMessage();
		Assertions.assertThat(actualToastMessage).isEqualTo("User 'johnd' deleted successfully");
		TestFactory.recordTestStep(actualToastMessage, true);
		userManagementPage.logout();
	}
}