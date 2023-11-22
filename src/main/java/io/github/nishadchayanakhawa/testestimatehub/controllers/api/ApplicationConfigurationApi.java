package io.github.nishadchayanakhawa.testestimatehub.controllers.api;

//import section
//java utils
import java.util.List;
//logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//entities and services
import io.github.nishadchayanakhawa.testestimatehub.configurations.TestEstimateHubConstants;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.ApplicationConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.services.ApplicationConfigurationService;

/**
 * <b>Class Name</b>: ApplicationConfigurationApi<br>
 * <b>Description</b>: Rest Controller for Application Configuration entity.<br>
 * 
 * @author nishad.chayanakhawa
 */
@RestController
@RequestMapping(TestEstimateHubConstants.APPLICATION_CONFIGURATION_API)
public class ApplicationConfigurationApi {
	// logger
	private static final Logger logger = LoggerFactory.getLogger(ApplicationConfigurationApi.class);

	// application configuration service
	private ApplicationConfigurationService applicationConfigurationService;
	
	@Autowired
	public ApplicationConfigurationApi(ApplicationConfigurationService applicationConfigurationService) {
		this.applicationConfigurationService=applicationConfigurationService;
	}

	/**
	 * <b>Method Name</b>: save<br>
	 * <b>Description</b>: Save entity. Responds with 201 if entity is created, 200
	 * otherwise.<br>
	 * 
	 * @param applicationConfiguration Application configuration to save as
	 *                                 {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ApplicationConfigurationDTO
	 *                                 ApplicationConfigurationDTO}
	 * @return saved application configuration sa
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ApplicationConfigurationDTO
	 *         ApplicationConfigurationDTO}
	 */
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ApplicationConfigurationDTO> save(
			@RequestBody ApplicationConfigurationDTO applicationConfiguration) {
		ApplicationConfigurationApi.logger.debug(TestEstimateHubConstants.SERVING_REQUEST_DEBUG_MESSAGE, "PUT",
				TestEstimateHubConstants.APPLICATION_CONFIGURATION_API);
		// set status to 201 if id is null as record will be created
		// otherwise, status is set to 200
		HttpStatus status = applicationConfiguration.getId() == null ? HttpStatus.CREATED : HttpStatus.OK;
		// return saved entity and status code
		return new ResponseEntity<>(this.applicationConfigurationService.save(applicationConfiguration), status);
	}

	/**
	 * <b>Method Name</b>: get<br>
	 * <b>Description</b>: Retrieve application configuration record.<br>
	 * 
	 * @param id id for record as {@link java.lang.Long Long}
	 * @return application configuration record as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ApplicationConfigurationDTO
	 *         ApplicationConfigurationDTO}
	 */
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ApplicationConfigurationDTO> get(@PathVariable Long id) {
		ApplicationConfigurationApi.logger.debug(TestEstimateHubConstants.SERVING_GET_REQUEST_DEBUG_MESSAGE, "GET",
				TestEstimateHubConstants.APPLICATION_CONFIGURATION_API, id);
		// retrieve record and return the same
		return new ResponseEntity<>(this.applicationConfigurationService.get(id), HttpStatus.OK);
	}

	/**
	 * <b>Method Name</b>: getAll<br>
	 * <b>Description</b>: Get all records for application configuration.<br>
	 * 
	 * @return {@link java.util.List List} of
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ApplicationConfigurationDTO
	 *         ApplicationConfigurationDTO}
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<List<ApplicationConfigurationDTO>> getAll() {
		ApplicationConfigurationApi.logger.debug(TestEstimateHubConstants.SERVING_REQUEST_DEBUG_MESSAGE, "GET",
				TestEstimateHubConstants.APPLICATION_CONFIGURATION_API);
		// return list of saved application configuration records.
		return new ResponseEntity<>(this.applicationConfigurationService.getAll(), HttpStatus.OK);
	}

	/**
	 * <b>Method Name</b>: delete<br>
	 * <b>Description</b>: Delete application configuration record.<br>
	 * 
	 * @param applicationConfiguration
	 * @return
	 */
	@DeleteMapping
	ResponseEntity<String> delete(@RequestBody ApplicationConfigurationDTO applicationConfiguration) {
		ApplicationConfigurationApi.logger.debug(TestEstimateHubConstants.SERVING_REQUEST_DEBUG_MESSAGE, "DELETE",
				TestEstimateHubConstants.APPLICATION_CONFIGURATION_API);
		this.applicationConfigurationService.delete(applicationConfiguration.getId());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}