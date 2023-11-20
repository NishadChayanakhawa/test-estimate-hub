package io.github.nishadchayanakhawa.testestimatehub.tests.apitests;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
		features = "src/test/resources/Features/01_UserManagementApiTests.feature",
		glue = "io.nishadc.automationtestingframework.testinginterface.restapi.stepdefinitions",
		tags="not @disabled")
public class UserManagementApiTests extends AbstractTestNGCucumberTests{
}