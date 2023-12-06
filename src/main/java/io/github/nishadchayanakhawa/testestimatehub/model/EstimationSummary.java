package io.github.nishadchayanakhawa.testestimatehub.model;

import io.github.nishadchayanakhawa.testestimatehub.services.ChangeService;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TEH_ESTIMATION_SUMMARY")
@Getter
@Setter
@NoArgsConstructor
public class EstimationSummary extends Estimation {
	@ManyToOne
	@JoinColumn(name = "CHANGE_ID")
	private Change change;

	public void addEstimationDetail(EstimationDetail estimationDetail) {
		this.setTestCaseCount(this.getTestCaseCount() + estimationDetail.getTestCaseCount());
		this.setReExecutionCount(this.getReExecutionCount() + estimationDetail.getReExecutionCount());
		this.setAdditionalCycleExecutionCount(
				this.getAdditionalCycleExecutionCount() + estimationDetail.getAdditionalCycleExecutionCount());
		this.setTotalExecutionCount(this.getTotalExecutionCount() + estimationDetail.getTotalExecutionCount());
		this.setDesignEfforts(ChangeService.round(this.getDesignEfforts() + estimationDetail.getDesignEfforts()));
		this.setExecutionEfforts(
				ChangeService.round(this.getExecutionEfforts() + estimationDetail.getExecutionEfforts()));
		this.setTotalEfforts(ChangeService.round(this.getTotalEfforts() + estimationDetail.getTotalEfforts()));
	}
}