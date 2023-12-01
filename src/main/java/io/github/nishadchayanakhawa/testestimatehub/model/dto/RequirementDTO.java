package io.github.nishadchayanakhawa.testestimatehub.model.dto;

//import section
//jav util
//lombok
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.NonNull;
import java.util.Set;

/**
 * <b>Class Name</b>: RequirementDTO<br>
 * <b>Description</b>: Requirement entity DTO.<br>
 * 
 * @author nishad.chayanakhawa
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
public class RequirementDTO {
	// id
	private Long id;

	// change id
	@Setter(AccessLevel.NONE)
	private Long changeId;

	// requirement identifier
	@NonNull
	@Setter(AccessLevel.NONE)
	private String identifier;

	// requirement summary
	@NonNull
	@Setter(AccessLevel.NONE)
	private String summary;
	
	// use cases
	Set<UseCaseDTO> useCases;
}