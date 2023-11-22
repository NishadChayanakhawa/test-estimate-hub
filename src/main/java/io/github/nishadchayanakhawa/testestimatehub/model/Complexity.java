package io.github.nishadchayanakhawa.testestimatehub.model;

//import section
//json formatting
import com.fasterxml.jackson.annotation.JsonFormat;
//lombok
import lombok.Getter;

/**
 * <b>Enum Name</b>: Complexity<br>
 * <b>Description</b>: Holds complexity values. Complexity is persisted in JPA as Ordinal, hence 
 * **DO NOT DISTURB THE ORDER**. Additional can be added at bottom, if required.<br>
 * @author nishad.chayanakhawa
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Complexity {
	VERY_LOW("VERY_LOW","Very Low"),
	LOW("LOW","Low"),
	MEDIUM("MEDIUM","Medium"),
	HIGH("HIGH","High"),
	VERY_HIGH("VERY_HIGH","Very High");
	
	private String code;
	private String displayValue;
	
	Complexity(String code,String displayValue) {
		this.code=code;
		this.displayValue=displayValue;
	}
}