package io.github.nishadchayanakhawa.testestimatehub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import io.github.nishadchayanakhawa.testestimatehub.services.ApplicationConfigurationService;
import io.github.nishadchayanakhawa.testestimatehub.services.ChangeTypeService;
import io.github.nishadchayanakhawa.testestimatehub.services.ReleaseService;
@Controller
public class TestEstimateHubApplicationControllers {
	
	private ApplicationConfigurationService applicationConfigurationService;
	private ChangeTypeService changeTypeService;
	private ReleaseService releaseService;
	
	@Autowired
	public TestEstimateHubApplicationControllers(ApplicationConfigurationService applicationConfigurationService,
			ChangeTypeService changeTypeService,
			ReleaseService releaseService) {
		this.applicationConfigurationService=applicationConfigurationService;
		this.changeTypeService=changeTypeService;
		this.releaseService=releaseService;
	}

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
	
	@GetMapping("/change")
	public String getChangeRecordManagementPage(Model model) {
		model.addAttribute("releases", this.releaseService.getAll());
		model.addAttribute("changeTypes", this.changeTypeService.getAll());
		model.addAttribute("applicationConfigurationRecords", this.applicationConfigurationService.getAll());
		return "record/change";
	}
}