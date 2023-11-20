package io.github.nishadchayanakhawa.testestimatehub.services.exceptions;

//spring libraries
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <b>Class Name</b>: EntityNotFoundException<br>
 * <b>Description</b>: Thrown in case entity is not found in db. Associated with
 * status code 410<br>
 * 
 * @author nishad.chayanakhawa
 */

@ResponseStatus(value = HttpStatus.GONE, reason = "Requested entity doesn't exist")
public class EntityNotFoundException extends TestEstimateHubExceptions {
	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(String name, Long id) {
		super(String.format("%s with id %d doesn't exist.", name, id), HttpStatus.GONE,
				"Requested entity doesn't exist");
	}
}