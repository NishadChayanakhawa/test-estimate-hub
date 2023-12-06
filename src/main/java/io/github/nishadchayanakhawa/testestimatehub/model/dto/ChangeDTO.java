package io.github.nishadchayanakhawa.testestimatehub.model.dto;

//import section
//java time
import java.time.LocalDate;
//lombok
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
//java util
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <b>Class Name</b>: ChangeDTO<br>
 * <b>Description</b>: Change entity DTO.<br>
 * 
 * @author nishad.chayanakhawa
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
public class ChangeDTO {
	// id
	private Long id;
	// release id
	private Long releaseId;
	// release identifier
	private String releaseIdentifier;
	private String releaseName;
	// change identifier
	private String identifier;
	// change summary
	private String summary;
	// change type id
	private Long changeTypeId;
	// change type name
	private String changeTypeName;
	// start date
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	// end date
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
	// requirements
	private Set<RequirementDTO> requirements;
	// impacted areas
	private Set<ApplicationConfigurationDTO> impactedArea;
	private Set<EstimationSummaryDTO> estimationSummaryRecords;
	
	private double designEfforts;
	private double executionEfforts;
	private double planningEfforts;
	private double preparationEfforts;
	private double managementEfforts;
	private double totalEfforts;
}