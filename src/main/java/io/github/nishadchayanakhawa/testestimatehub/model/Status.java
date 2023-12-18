package io.github.nishadchayanakhawa.testestimatehub.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Status {
	PENDING_ESTIMATION("PENDING_ESTIMATION", "Pending Estimation"),
	PENDING_ESTIMATION_REVIEW("PENDING_ESTIMATION_REVIEW", "Pending Estimation Review"),
	PENDING_RESOURCE_ALLOCATION("PENDING_RESOURCE_ALLOCATION", "Pending Resource Allocation"),
	RESOURCES_ALLOCATED("RESOURCES_ALLOCATED", "Resources Allocated"),
	RESOURCES_LOCKED("RESOURCES_LOCKED", "Resources Locked");

	private String code;
	private String displayValue;

	Status(String code, String displayValue) {
		this.code = code;
		this.displayValue = displayValue;
	}
}
