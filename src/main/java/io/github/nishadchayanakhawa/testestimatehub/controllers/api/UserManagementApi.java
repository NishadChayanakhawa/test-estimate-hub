package io.github.nishadchayanakhawa.testestimatehub.controllers.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import section
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
import io.github.nishadchayanakhawa.testestimatehub.configurations.TestEstimateHubConstants;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.UserDTO;
import io.github.nishadchayanakhawa.testestimatehub.services.UserService;

/**
 * <b>Class Name</b>: UserApi<br>
 * <b>Description</b>: REST Controller for User service.<br>
 * 
 * @author nishad.chayanakhawa
 */
@RestController
@RequestMapping(TestEstimateHubConstants.USER_MANAGEMENT_API)
public class UserManagementApi {
	// logger
	private static final Logger logger = LoggerFactory.getLogger(UserManagementApi.class);

	// user service
	private UserService userService;
	
	@Autowired
	public UserManagementApi(UserService userService) {
		this.userService=userService;
	}

	/**
	 * <b>Method Name</b>: addUser<br>
	 * <b>Description</b>: Serves PUT request to add/update user<br>
	 * 
	 * @param userToAdd user to add as
	 *                  {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.UserDTO
	 *                  UserDTO}
	 * @return added user as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.UserDTO
	 *         UserDTO} wrapped in {@link org.springframework.http.ResponseEntity
	 *         ResponseEntity}. With status code as 201, when user is added and 200
	 *         when updated.
	 */
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userToAdd) {
		logger.debug(TestEstimateHubConstants.SERVING_REQUEST_DEBUG_MESSAGE, "PUT",
				TestEstimateHubConstants.USER_MANAGEMENT_API);
		// if user id is null, user will be added, hence status is set to 201
		// else, status is 200
		HttpStatus status = userToAdd.getId() == null ? HttpStatus.CREATED : HttpStatus.OK;
		// add user
		UserDTO savedUser = this.userService.save(userToAdd);
		// return user with status code
		return new ResponseEntity<>(savedUser, status);
	}

	/**
	 * <b>Method Name</b>: getUser<br>
	 * <b>Description</b>: Serves GET request with id in path parameter to look up
	 * user record.<br>
	 * 
	 * @param id user id as {@link java.lang.Long Long}
	 * @return user located as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.UserDTO
	 *         UserDTO} wrapped in {@link org.springframework.http.ResponseEntity
	 *         ResponseEntity}. With status code as 200.
	 */
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
		// search for user and return the same with status code 200
		logger.debug(TestEstimateHubConstants.SERVING_GET_REQUEST_DEBUG_MESSAGE, "GET",
				TestEstimateHubConstants.USER_MANAGEMENT_API, id);
		return new ResponseEntity<>(this.userService.get(id), HttpStatus.OK);
	}

	/**
	 * <b>Method Name</b>: getAll<br>
	 * <b>Description</b>: Get list of all users.<br>
	 * 
	 * @return {@link java.util.List List} of
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.UserDTO
	 *         UserDTO} wrapped in {@link org.springframework.http.ResponseEntity
	 *         ResponseEntity}, with status code as 200.
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<List<UserDTO>> getAll() {
		// return all users
		logger.debug(TestEstimateHubConstants.SERVING_REQUEST_DEBUG_MESSAGE, "GET",
				TestEstimateHubConstants.USER_MANAGEMENT_API);
		return new ResponseEntity<>(this.userService.getAll(), HttpStatus.OK);
	}

	/**
	 * <b>Method Name</b>: deleteUser<br>
	 * <b>Description</b>: Delete user<br>
	 * 
	 * @param userToDelete user to delete as
	 *                     {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.UserDTO
	 *                     UserDTO}
	 * @return {@link org.springframework.http.ResponseEntity ResponseEntity}, with
	 *         status code as 200.
	 */
	@DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> deleteUser(@RequestBody UserDTO userToDelete) {
		logger.debug(TestEstimateHubConstants.SERVING_REQUEST_DEBUG_MESSAGE, "DELETE",
				TestEstimateHubConstants.USER_MANAGEMENT_API);
		// delete user
		this.userService.delete(userToDelete.getId());
		// send status code 200
		return new ResponseEntity<>(HttpStatus.OK);
	}
}