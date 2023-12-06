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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//constants, models and services
import io.github.nishadchayanakhawa.testestimatehub.configurations.TestEstimateHubConstants;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeTypeDTO;
import io.github.nishadchayanakhawa.testestimatehub.services.ChangeService;

/**
 * <b>Class Name</b>: ChangeConfigurationApi<br>
 * <b>Description</b>: Serves test type configuration api<br>
 * 
 * @author nishad.chayanakhawa
 */
@RestController
@RequestMapping(TestEstimateHubConstants.CHANGE_MANAGEMENT_API)
public class ChangeManagementApi {
	// logger
	private static final Logger logger = LoggerFactory.getLogger(ChangeManagementApi.class);

	// Test type service
	private ChangeService changeService;

	// autowired constructor to initialize service
	@Autowired
	public ChangeManagementApi(ChangeService changeService) {
		this.changeService = changeService;
	}

	/**
	 * <b>Method Name</b>: save<br>
	 * <b>Description</b>: Saves Test type configuration record<br>
	 * 
	 * @param changeToSave test type record to save as
	 *                       {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeDTO
	 *                       ChangeDTO}
	 * @return saved test type record as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeDTO
	 *         ChangeDTO}. If record was added, status is 201, and for update,
	 *         status is 200.
	 */
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ChangeDTO> save(@RequestBody ChangeDTO changeToSave) {
		logger.debug(TestEstimateHubConstants.SERVING_REQUEST_DEBUG_MESSAGE, "PUT",
				TestEstimateHubConstants.CHANGE_MANAGEMENT_API);
		// in case record is new, set response code to 201, 200 otherwise
		HttpStatus status = changeToSave.getId() == null ? HttpStatus.CREATED : HttpStatus.OK;
		// save entity
		return new ResponseEntity<>(this.changeService.save(changeToSave), status);
	}

	/**
	 * <b>Method Name</b>: getChange<br>
	 * <b>Description</b>: Get test type record.<br>
	 * 
	 * @param id id to lookup record
	 * @return test type record as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeDTO
	 *         ChangeDTO}
	 */
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ChangeDTO> getChange(@PathVariable Long id,@RequestParam(name="depth",defaultValue="0") int depth) {
		logger.debug(TestEstimateHubConstants.SERVING_GET_REQUEST_DEBUG_MESSAGE, "GET",
				TestEstimateHubConstants.CHANGE_MANAGEMENT_API, id);
		// return test type record
		return new ResponseEntity<>(this.changeService.get(id,depth), HttpStatus.OK);
	}

	/**
	 * <b>Method Name</b>: getAll<br>
	 * <b>Description</b>: Get all saved test types.<br>
	 * 
	 * @return {@link java.util.List List} of
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeDTO
	 *         ChangeDTO}
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<List<ChangeDTO>> getAll() {
		logger.debug(TestEstimateHubConstants.SERVING_REQUEST_DEBUG_MESSAGE, "GET",
				TestEstimateHubConstants.CHANGE_MANAGEMENT_API);
		// return list of test type records
		return new ResponseEntity<>(this.changeService.getAll(), HttpStatus.OK);
	}

	/**
	 * <b>Method Name</b>: delete<br>
	 * <b>Description</b>: Delete test type record.<br>
	 * 
	 * @param changeToDelete test type record to delete as
	 *                         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeDTO
	 *                         ChangeDTO} only id property is required
	 * @return status 200 once record is deleted
	 */
	@DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> delete(@RequestBody ChangeDTO changeToDelete) {
		logger.debug(TestEstimateHubConstants.SERVING_REQUEST_DEBUG_MESSAGE, "DELETE",
				TestEstimateHubConstants.CHANGE_MANAGEMENT_API);
		// delete record
		this.changeService.delete(changeToDelete.getId());
		// return status code 200
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping(path = "/estimate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ChangeDTO> estimate(@RequestBody ChangeTypeDTO changeToEstimate) {
		return new ResponseEntity<>(this.changeService.calculateEstimation(changeToEstimate.getId()),HttpStatus.OK);
	}
}