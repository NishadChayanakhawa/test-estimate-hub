package io.github.nishadchayanakhawa.testestimatehub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TEH_ESTIMATION_DETAIL")
@Getter
@Setter
@NoArgsConstructor
public class EstimationDetail extends Estimation {
	@ManyToOne
	@JoinColumn(name = "USE_CASE_ID")
	private UseCase useCase;
}