package io.github.nishadchayanakhawa.testestimatehub.model.dto;

//import section
//lombok
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Class Name</b>: ChangeType<br>
 * <b>Description</b>: Change type configuration record.<br>
 * 
 * @author nishad.chayanakhawa
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
public class ChangeTypeDTO {
	// id
	private Long id;

	// name
	private String name;

	// test case count modifier
	private double testCaseCountModifier;

	// test planning effort allocation percentage
	private double testPlanningEffortAllocationPercentage;

	// test preparation effort allocation percentgae
	private double testPreparationEffortAllocationPercentage;

	// management effort allocation percentgae
	private double managementEffortAllocationPercentage;
}