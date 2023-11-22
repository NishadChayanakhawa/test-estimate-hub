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
//constants, models and services
import io.github.nishadchayanakhawa.testestimatehub.configurations.TestEstimateHubConstants;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.TestTypeDTO;
import io.github.nishadchayanakhawa.testestimatehub.services.TestTypeService;

/**
 * <b>Class Name</b>: TestTypeConfigurationApi<br>
 * <b>Description</b>: Serves test type configuration api<br>
 * 
 * @author nishad.chayanakhawa
 */
@RestController
@RequestMapping(TestEstimateHubConstants.TEST_TYPE_CONFIGURATION_API)
public class TestTypeConfigurationApi {
	// logger
	private static final Logger logger = LoggerFactory.getLogger(TestTypeConfigurationApi.class);

	// Test type service
	private TestTypeService testTypeService;

	// autowired constructor to initialize service
	@Autowired
	public TestTypeConfigurationApi(TestTypeService testTypeService) {
		this.testTypeService = testTypeService;
	}

	/**
	 * <b>Method Name</b>: save<br>
	 * <b>Description</b>: Saves Test type configuration record<br>
	 * 
	 * @param testTypeToSave test type record to save as
	 *                       {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.TestTypeDTO
	 *                       TestTypeDTO}
	 * @return saved test type record as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.TestTypeDTO
	 *         TestTypeDTO}. If record was added, status is 201, and for update,
	 *         status is 200.
	 */
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TestTypeDTO> save(@RequestBody TestTypeDTO testTypeToSave) {
		logger.debug(TestEstimateHubConstants.SERVING_REQUEST_DEBUG_MESSAGE, "PUT",
				TestEstimateHubConstants.TEST_TYPE_CONFIGURATION_API);
		// in case record is new, set response code to 201, 200 otherwise
		HttpStatus status = testTypeToSave.getId() == null ? HttpStatus.CREATED : HttpStatus.OK;
		// save entity
		return new ResponseEntity<>(this.testTypeService.save(testTypeToSave), status);
	}

	/**
	 * <b>Method Name</b>: getTestType<br>
	 * <b>Description</b>: Get test type record.<br>
	 * 
	 * @param id id to lookup record
	 * @return test type record as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.TestTypeDTO
	 *         TestTypeDTO}
	 */
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<TestTypeDTO> getTestType(@PathVariable Long id) {
		logger.debug(TestEstimateHubConstants.SERVING_GET_REQUEST_DEBUG_MESSAGE, "GET",
				TestEstimateHubConstants.TEST_TYPE_CONFIGURATION_API, id);
		// return test type record
		return new ResponseEntity<>(this.testTypeService.get(id), HttpStatus.OK);
	}

	/**
	 * <b>Method Name</b>: getAll<br>
	 * <b>Description</b>: Get all saved test types.<br>
	 * 
	 * @return {@link java.util.List List} of
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.TestTypeDTO
	 *         TestTypeDTO}
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<List<TestTypeDTO>> getAll() {
		logger.debug(TestEstimateHubConstants.SERVING_REQUEST_DEBUG_MESSAGE, "GET",
				TestEstimateHubConstants.TEST_TYPE_CONFIGURATION_API);
		// return list of test type records
		return new ResponseEntity<>(this.testTypeService.getAll(), HttpStatus.OK);
	}

	/**
	 * <b>Method Name</b>: deleteUser<br>
	 * <b>Description</b>: Delete test type record.<br>
	 * 
	 * @param testTypeToDelete test type record to delete as
	 *                         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.TestTypeDTO
	 *                         TestTypeDTO} only id property is required
	 * @return status 200 once record is deleted
	 */
	@DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> deleteUser(@RequestBody TestTypeDTO testTypeToDelete) {
		logger.debug(TestEstimateHubConstants.SERVING_REQUEST_DEBUG_MESSAGE, "DELETE",
				TestEstimateHubConstants.TEST_TYPE_CONFIGURATION_API);
		// delete record
		this.testTypeService.delete(testTypeToDelete.getId());
		// return status code 200
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
