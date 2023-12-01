package io.github.nishadchayanakhawa.testestimatehub.controllers.api;

import java.util.Set;

//import section
//java utils

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
//constants, models and services
import io.github.nishadchayanakhawa.testestimatehub.configurations.TestEstimateHubConstants;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.UseCaseDTO;
import io.github.nishadchayanakhawa.testestimatehub.services.UseCaseService;

/**
 * <b>Class Name</b>: ChangeConfigurationApi<br>
 * <b>Description</b>: Serves test type configuration api<br>
 * 
 * @author nishad.chayanakhawa
 */
@RestController
@RequestMapping(TestEstimateHubConstants.USE_CASE_MANAGEMENT_API)
public class UseCaseManagementApi {
	// logger
	private static final Logger logger = LoggerFactory.getLogger(UseCaseManagementApi.class);

	// Test type service
	private UseCaseService useCaseService;

	// autowired constructor to initialize service
	@Autowired
	public UseCaseManagementApi(UseCaseService useCaseService) {
		this.useCaseService = useCaseService;
	}

	@GetMapping(path = "/byRequirement/{requirementId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<UseCaseDTO>> getAllByRequirementId(@PathVariable Long requirementId) {
		return new ResponseEntity<>(this.useCaseService.findAllByRequirementId(requirementId), HttpStatus.OK);
	}

	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UseCaseDTO> get(@PathVariable Long id) {
		return new ResponseEntity<>(this.useCaseService.get(id), HttpStatus.OK);
	}

	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UseCaseDTO> save(@RequestBody UseCaseDTO useCaseToSave) {
		logger.debug(TestEstimateHubConstants.SERVING_REQUEST_DEBUG_MESSAGE, "PUT",
				TestEstimateHubConstants.USE_CASE_MANAGEMENT_API);
		// in case record is new, set response code to 201, 200 otherwise
		HttpStatus status = useCaseToSave.getId() == null ? HttpStatus.CREATED : HttpStatus.OK;
		// save entity
		return new ResponseEntity<>(this.useCaseService.save(useCaseToSave), status);
	}

	@DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> delete(@RequestBody UseCaseDTO useCaseToDelete) {
		logger.debug(TestEstimateHubConstants.SERVING_REQUEST_DEBUG_MESSAGE, "DELETE",
				TestEstimateHubConstants.USE_CASE_MANAGEMENT_API);
		this.useCaseService.delete(useCaseToDelete.getId());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}