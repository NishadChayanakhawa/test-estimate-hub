package io.github.nishadchayanakhawa.testestimatehub.tests.apitests;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
		features = "src/test/resources/Features/ControllerTests.feature",
		glue = "io.nishadc.automationtestingframework.testinginterface.restapi.stepdefinitions",
		tags="not @disabled")
public class ControllerTests extends AbstractTestNGCucumberTests{
}