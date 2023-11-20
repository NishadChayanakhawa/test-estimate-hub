package io.github.nishadchayanakhawa.testestimatehub.services.exceptions;

import org.springframework.http.HttpStatus;

/**
 * <b>Class Name</b>: TestEstimateHubExceptions<br>
 * <b>Description</b>: Parent exception class for all exceptions under application.<br>
 * @author nishad.chayanakhawa
 */
public class TestEstimateHubExceptions extends RuntimeException {
	private final HttpStatus status;
	private final String reason;
	
	private static final long serialVersionUID = 1L;
	
	public TestEstimateHubExceptions(String message,HttpStatus status,String reason) {
		super(message);
		this.status=status;
		this.reason=reason;
	}
	
	public int getStatus() {
		return this.status.value();
	}
	
	public String getError() {
		return this.status.getReasonPhrase();
	}
	
	public String getReason() {
		return this.reason;
	}
	
	public HttpStatus getHttpStatus() {
		return this.status;
	}
}