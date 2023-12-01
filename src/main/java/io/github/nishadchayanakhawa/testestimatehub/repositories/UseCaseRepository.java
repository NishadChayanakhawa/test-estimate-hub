package io.github.nishadchayanakhawa.testestimatehub.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import io.github.nishadchayanakhawa.testestimatehub.model.UseCase;

public interface UseCaseRepository extends JpaRepository<UseCase,Long>{
	List<UseCase> findByRequirementId(Long requirementId);
}
