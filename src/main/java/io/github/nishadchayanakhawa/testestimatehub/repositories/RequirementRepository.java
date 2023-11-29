package io.github.nishadchayanakhawa.testestimatehub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.nishadchayanakhawa.testestimatehub.model.Requirement;

public interface RequirementRepository extends JpaRepository<Requirement,Long>{

}
