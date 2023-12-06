package io.github.nishadchayanakhawa.testestimatehub.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EstimationDTO {
	private Long id;
	
	private String testTypeId;
	private String testTypeName;
	
	private int testCaseCount;
	
	private int reExecutionCount;
	private int additionalCycleExecutionCount;
	private int totalExecutionCount;
	
	private double designEfforts;
	private double executionEfforts;
	private double totalEfforts;
}