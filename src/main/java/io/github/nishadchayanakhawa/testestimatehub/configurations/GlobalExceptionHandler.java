package io.github.nishadchayanakhawa.testestimatehub.configurations;

import java.util.List;
import java.util.Arrays;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.github.nishadchayanakhawa.testestimatehub.model.dto.ErrorResponseDTO;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.TestEstimateHubExceptions;

@ControllerAdvice
public class GlobalExceptionHandler {
	 @ExceptionHandler({TestEstimateHubExceptions.class})
	 public ResponseEntity<ErrorResponseDTO> handleExceptions(TestEstimateHubExceptions e) {
		 int status=e.getStatus();
		 String error=e.getError();
		 String reason=e.getReason();
		 List<String> details=Arrays.asList(e.getMessage().split(";"));
		 ErrorResponseDTO errorDetail=new ErrorResponseDTO(status,error,reason,details);
		 return new ResponseEntity<>(errorDetail,e.getHttpStatus());
	 }
}