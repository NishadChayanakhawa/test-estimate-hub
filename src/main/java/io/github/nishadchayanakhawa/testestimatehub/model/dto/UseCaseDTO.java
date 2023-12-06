package io.github.nishadchayanakhawa.testestimatehub.model.dto;

//import section
//java util
import java.util.Set;

//lombok
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <b>Class Name</b>: UseCaseDTO<br>
 * <b>Description</b>: Use case entity dto.<br>
 * @author nishad.chayanakhawa
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
public class UseCaseDTO {
	//requirement id
	private Long requirementId;
	//use case id
	private Long id;
	
	//summary
	private String summary;
	
	//business application, module and sub-module
	private Long businessFunctionalityId;
	
	//data variation count
	private Integer dataVariationCount;

	//test configuration complexity code and display value
	private String testConfigurationComplexityCode;
	private String testConfigurationComplexityDisplayValue;
	
	//test data setup complexity code and display value
	private String testDataSetupComplexityCode;
	private String testDataSetupComplexityDisplayValue;
	
	//test transaction complexity code and display value
	private String testTransactionComplexityCode;
	private String testTransactionComplexityDisplayValue;
	
	//test validation complexity code and display value
	private String testValidationComplexityCode;
	private String testValidationComplexityDisplayValue;
	
	//applicable test types
	private Set<TestTypeDTO> applicableTestTypes;
	
	private Set<EstimationDetailDTO> estimationDetails;
}