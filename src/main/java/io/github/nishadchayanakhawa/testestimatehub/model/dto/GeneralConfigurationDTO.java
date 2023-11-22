package io.github.nishadchayanakhawa.testestimatehub.model.dto;

import java.util.Map;

import io.github.nishadchayanakhawa.testestimatehub.model.Complexity;
//import section
//lombok
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Class Name</b>: GeneralConfigurationDTO<br>
 * <b>Description</b>: DTO representation for general configuration entity.<br>
 * 
 * @author nishad.chayanakhawa
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
public class GeneralConfigurationDTO {
	// id
	private Long id;

	// test design productivity
	private Map<Complexity, Double> testDesignProductivity;

	// test execution productivity
	private Map<Complexity, Double> testExecutionProductivity;

	// test configuration complexity weightage
	private double testConfigurationComplexityPercentage;
	// test data complexity weightage
	private double testDataComplexityPercentage;
	// test transaction complexity weightage
	private double testTransactionComplexityPercentage;
	// test validation complexity weightage
	private double testValidationComplexityPercentage;
}