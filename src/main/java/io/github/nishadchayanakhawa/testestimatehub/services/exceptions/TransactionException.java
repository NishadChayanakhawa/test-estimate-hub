package io.github.nishadchayanakhawa.testestimatehub.services.exceptions;

import java.util.stream.Collectors;
//spring libraries
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import jakarta.validation.ConstraintViolation;

/**
 * <b>Class Name</b>: EntityNotFoundException<br>
 * <b>Description</b>: Thrown in case of transactional error with entity.
 * Associated with status code 400.<br>
 * 
 * @author nishad.chayanakhawa
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Request data is incorrect")
public class TransactionException extends TestEstimateHubExceptions {
	private static final long serialVersionUID = 1L;

	public TransactionException(String message) {
		super(message, HttpStatus.BAD_REQUEST, "Request data is incorrect");
	}

	public TransactionException(jakarta.validation.ConstraintViolationException e) {
		this(TransactionException.extractMessages(e));
	}

	private static String extractMessages(jakarta.validation.ConstraintViolationException e) {
		return e.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
				.collect(Collectors.joining(";"));
	}
}