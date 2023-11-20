package io.github.nishadchayanakhawa.testestimatehub.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestEstimateHubApplicationControllers {

	@GetMapping("/login")
	public String getLoginPage() {
		return "login";
	}

	@GetMapping("/home")
	public String getHomePage() {
		return "home";
	}

	@GetMapping("/configuration/userManagement")
	public String getUserManagementPage() {
		return "configuration/userManagement";
	}

	@GetMapping("/configuration/testType")
	public String getTestTypeConfigurationPage() {
		return "configuration/testType";
	}

	@GetMapping("/configuration/changeType")
	public String getChangeTypeConfigurationPage() {
		return "configuration/changeType";
	}

	@GetMapping("/configuration/general")
	public String getGeneralConfigurationPage() {
		return "configuration/general";
	}

	@GetMapping("/configuration/application")
	public String getApplicationConfigurationPage() {
		return "configuration/application";
	}

	@GetMapping("/release")
	public String getReleaseRecordManagementPage() {
		return "record/release";
	}
}