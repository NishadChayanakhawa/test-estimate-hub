package io.github.nishadchayanakhawa.testestimatehub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TEH_ESTIMATION")
@Inheritance(strategy=InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
public class Estimation {
	@Id
	@GeneratedValue
	private Long id;

	// change type
	@ManyToOne
	@JoinColumn(name = "TEST_TYPE_ID", nullable = false)
	private TestType testType;

	private int testCaseCount;

	private int reExecutionCount;
	private int additionalCycleExecutionCount;
	private int totalExecutionCount;

	private double designEfforts;
	private double executionEfforts;
	private double totalEfforts;
}
