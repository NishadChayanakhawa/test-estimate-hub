package io.github.nishadchayanakhawa.testestimatehub.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDTO {
	int status;
	String error;
	String message;
	List<String> details;
}