package io.github.nishadchayanakhawa.testestimatehub.controllers.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.nishadchayanakhawa.testestimatehub.configurations.TestEstimateHubConstants;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.GeneralConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.services.GeneralConfigurationService;

@RestController
@RequestMapping(TestEstimateHubConstants.GENERAL_CONFIGURATION_API)
public class GeneralConfigurationApi {
	// logger
	private static final Logger logger = LoggerFactory.getLogger(GeneralConfigurationApi.class);

	// Test type service
	private GeneralConfigurationService generalConfigurationService;

	// autowired constructor to initialize service
	@Autowired
	public GeneralConfigurationApi(GeneralConfigurationService generalConfigurationService) {
		this.generalConfigurationService = generalConfigurationService;
	}

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GeneralConfigurationDTO> save(@RequestBody GeneralConfigurationDTO generalConfigurationDTO) {
		logger.debug(TestEstimateHubConstants.SERVING_REQUEST_DEBUG_MESSAGE, "PUT",
				TestEstimateHubConstants.GENERAL_CONFIGURATION_API);
		// save entity
		return new ResponseEntity<>(this.generalConfigurationService.save(generalConfigurationDTO), HttpStatus.OK);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<GeneralConfigurationDTO> getChangeType() {
		logger.debug(TestEstimateHubConstants.SERVING_REQUEST_DEBUG_MESSAGE, "GET",
				TestEstimateHubConstants.GENERAL_CONFIGURATION_API);
		// return test type record
		return new ResponseEntity<>(this.generalConfigurationService.get(), HttpStatus.OK);
	}
}
