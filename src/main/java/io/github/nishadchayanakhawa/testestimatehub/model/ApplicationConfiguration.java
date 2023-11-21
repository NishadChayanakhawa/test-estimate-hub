package io.github.nishadchayanakhawa.testestimatehub.model;

//import section
import io.github.nishadchayanakhawa.testestimatehub.configurations.TestEstimateHubConstants;
//jpa libraries
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
//validation libraries
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
//lombok libraries
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <b>Class Name</b>: ApplicationConfiguration<br>
 * <b>Description</b>: Application configuration entity.<br>
 * 
 * @author nishad.chayanakhawa
 */
@Entity
@Table(name = "TEH_APPLICATION_CONFIGURATION", uniqueConstraints = {
		@UniqueConstraint(name = "TEH_APPLICATION_CONFIGURATION_UNQ", columnNames = "configurationKey") })
@Getter
@Setter
@NoArgsConstructor
public class ApplicationConfiguration {

	// id
	@Id
	@GeneratedValue
	private Long id;

	// dummy configuration key to validate case-insensitive uniqueness of
	// application, module and sub-module
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private String configurationKey;

	// application name)
	@NotBlank(message = "application " + TestEstimateHubConstants.NON_BLANK_MESSAGE)
	private String application;

	// module name
	@NotBlank(message = "module " + TestEstimateHubConstants.NON_BLANK_MESSAGE)
	private String module;

	// sub-module name
	@NotBlank(message = "subModule " + TestEstimateHubConstants.NON_BLANK_MESSAGE)
	private String subModule;

	// base test script count
	@Min(value = 1, message = "baseTestScriptCount " + TestEstimateHubConstants.MINIMUM_VALUE_MESSAGE + " 1")
	private int baseTestScriptCount;

	/**
	 * <b>Method Name</b>: prepare<br>
	 * <b>Description</b>: before inserting or updating record, populate
	 * configurationKey. configurationKey is dummy key to enforce case-insensitive
	 * unique constraing on application + module + subModule combination. Spaces in
	 * individual properties are also removed.<br>
	 */
	@PrePersist
	@PreUpdate
	private void prepare() {
		// remove spaces from application, module and sub-module and concatenate them,
		// separated by ':'
		// assign concatenated value to configurationKey
		try {
			this.configurationKey = String.format("%s:%s:%s", this.application.toLowerCase().replace(" ", ""),
					this.module.toLowerCase().replace(" ", ""), this.subModule.toLowerCase().replace(" ", ""));
		} catch (NullPointerException e) {
			this.configurationKey = null;
		}
	}
}