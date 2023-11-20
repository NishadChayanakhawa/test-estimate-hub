package io.github.nishadchayanakhawa.testestimatehub.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.github.nishadchayanakhawa.testestimatehub.configurations.TestEstimateHubConstants;

/**
 * <b>Class Name</b>: DuplicateEntityException<br>
 * <b>Description</b>: Thrown when unique constraint is violated for an entity.
 * Associated with status code 409.<br>
 * 
 * @author nishad.chayanakhawa
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Entity with unique values already exists")
public class DuplicateEntityException extends TestEstimateHubExceptions {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DuplicateEntityException(String message) {
		super(message,HttpStatus.CONFLICT,
				"Entity with unique values already exists");
	}

	public DuplicateEntityException(String entity, String field, String value) {
		this(TestEstimateHubConstants.getDuplicateEntityExceptionErrorMessage(entity, field, value));
	}
}