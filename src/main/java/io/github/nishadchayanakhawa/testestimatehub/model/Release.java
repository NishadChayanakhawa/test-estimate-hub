package io.github.nishadchayanakhawa.testestimatehub.model;

//import section
//java time
import java.time.LocalDate;

import io.github.nishadchayanakhawa.testestimatehub.configurations.TestEstimateHubConstants;
//jpa
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
//validation
import jakarta.validation.constraints.NotBlank;
//lombok
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <b>Class Name</b>: Release<br>
 * <b>Description</b>: Release record entity.<br>
 * @author nishad.chayanakhawa
 */
@Entity
@Table(name = "TEH_RELEASE", uniqueConstraints = {
		@UniqueConstraint(name = "UNIQUE_TEH_RELEASE_IDENTIFIER", columnNames = { "IDENTIFIER" }) })
@Getter
@Setter
@NoArgsConstructor
public class Release {
	// id
	@Id
	@GeneratedValue
	private Long id;

	// identifier
	@Column(nullable = false)
	@NotBlank(message = "identifier " + TestEstimateHubConstants.NON_BLANK_MESSAGE)
	private String identifier;

	// name
	@Column(nullable = false)
	@NotBlank(message = "name " + TestEstimateHubConstants.NON_BLANK_MESSAGE)
	private String name;
	
	//start date
	@Column(nullable = false)
	private LocalDate startDate;
	
	//end date
	@Column(nullable = false)
	private LocalDate endDate;
}