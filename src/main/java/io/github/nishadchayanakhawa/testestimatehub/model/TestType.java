package io.github.nishadchayanakhawa.testestimatehub.model;

import io.github.nishadchayanakhawa.testestimatehub.configurations.TestEstimateHubConstants;
//import section
//jpa
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
//lombok
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <b>Class Name</b>: TestType<br>
 * <b>Description</b>: Test type configuration record.<br>
 * @author nishad.chayanakhawa
 */
@Entity
@Table(name = "TEH_TEST_TYPE_CONFIGURATION",
	uniqueConstraints = { @UniqueConstraint(name = "UNIQUE_TEH_TEST_TYPE_NAME", columnNames = { "NAME" }) })
@Getter
@Setter
@NoArgsConstructor
public class TestType {
	// id
	@Id
	@GeneratedValue
	private Long id;

	//name
	@Column(nullable = false)
	@NotBlank(message = "name " + TestEstimateHubConstants.NON_BLANK_MESSAGE)
	private String name;

	//relative test case count percentage
	@Column(nullable = false)
	@Min(value = 0, message = "relativeTestCaseCountPercentage " + TestEstimateHubConstants.MINIMUM_VALUE_MESSAGE + " 0")
	@Max(value = 100, message = "relativeTestCaseCountPercentage " + TestEstimateHubConstants.MAXIMUM_VALUE_MESSAGE + " 100")
	private double relativeTestCaseCountPercentage;

	//Re-execution percentage
	@Column(nullable = false)
	@Min(value = 0, message = "reExecutionPercentage " + TestEstimateHubConstants.MINIMUM_VALUE_MESSAGE + " 0")
	@Max(value = 100, message = "reExecutionPercentage " + TestEstimateHubConstants.MAXIMUM_VALUE_MESSAGE + " 100")
	private double reExecutionPercentage;

	//additional cycle execution percentage
	@Column(nullable = false)
	@Min(value = 0, message = "additionalCycleExecutionPercentage " + TestEstimateHubConstants.MINIMUM_VALUE_MESSAGE + " 0")
	@Max(value = 100, message = "additionalCycleExecutionPercentage " + TestEstimateHubConstants.MAXIMUM_VALUE_MESSAGE + " 100")
	private double additionalCycleExecutionPercentage;
}