package io.github.nishadchayanakhawa.testestimatehub.model.dto;

//import section
//lombok
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <b>Class Name</b>: ApplicationConfigurationDTO<br>
 * <b>Description</b>: DTO representation of Application Configuration.<br>
 * 
 * @author nishad.chayanakhawa
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
public class ApplicationConfigurationDTO {
	// id
	private Long id;
	// application name
	private String application;
	// module name
	private String module;
	// sub-module name
	private String subModule;
	// base test script count
	private int baseTestScriptCount;
}