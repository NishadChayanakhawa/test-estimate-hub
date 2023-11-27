package io.github.nishadchayanakhawa.testestimatehub.model;

//import section
//java time
import java.time.LocalDate;

//jpa
import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
//jpa validations
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
//lombok
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//java utils
import java.util.Set;
import java.util.HashSet;

/**
 * <b>Class Name</b>: Change<br>
 * <b>Description</b>: Change entity.<br>
 * 
 * @author nishad.chayanakhawa
 */
@Entity
@Table(name = "TEH_CHANGE", uniqueConstraints = {
		@UniqueConstraint(name = "UNIQUE_TEH_CHANGE_IDENTIFIER", columnNames = { "IDENTIFIER" }) })
@Getter
@Setter
@NoArgsConstructor
public class Change {
	// id
	@Id
	@GeneratedValue
	private Long id;

	// release
	@ManyToOne
	@JoinColumn(name = "RELEASE_ID", nullable = false)
	@NotNull(message = "release {required.message}")
	private Release release;

	// identifier
	@Column(nullable = false)
	@NotBlank(message = "identifier {required.message}")
	private String identifier;

	// summary
	@Column(nullable = false)
	@NotBlank(message = "summary {required.message}")
	private String summary;

	// change type
	@ManyToOne
	@JoinColumn(name = "CHANGE_TYPE_ID", nullable = false)
	@NotNull(message = "changeType {required.message}")
	private ChangeType changeType;

	// start date
	@Column(nullable = false)
	@NotNull(message = "startDate {required.message}")
	private LocalDate startDate;

	// end date
	@Column(nullable = false)
	@NotNull(message = "endDate {required.message}")
	private LocalDate endDate;

	// requirements
	@OneToMany(mappedBy="change",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	Set<Requirement> requirements = new HashSet<>();

	// impacted applications, modules and sub-modules
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "TEH_CHANGE_IMPACT", 
		joinColumns = @JoinColumn(name = "CHANGE_ID"), 
		inverseJoinColumns = @JoinColumn(name = "APPLICATION_CONFIGURATION_ID"))
	@NotEmpty(message="impactedArea {non-empty.message}")
	Set<ApplicationConfiguration> impactedArea = new HashSet<>();
	
	public void addRequirement(Requirement requirement) {
		requirement.setChange(this);
		this.requirements.add(requirement);
	}
}