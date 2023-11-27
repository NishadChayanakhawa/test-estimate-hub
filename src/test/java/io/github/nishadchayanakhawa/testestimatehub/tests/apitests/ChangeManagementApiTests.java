package io.github.nishadchayanakhawa.testestimatehub.tests.apitests;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
		features = "src/test/resources/Features/ChangeManagementApiTests.feature",
		glue = "io.nishadc.automationtestingframework.testinginterface.restapi.stepdefinitions")
public class ChangeManagementApiTests extends AbstractTestNGCucumberTests{
}
