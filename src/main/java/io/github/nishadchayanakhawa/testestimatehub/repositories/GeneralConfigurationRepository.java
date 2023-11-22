package io.github.nishadchayanakhawa.testestimatehub.repositories;

//import section
//jpa
import org.springframework.data.jpa.repository.JpaRepository;

import io.github.nishadchayanakhawa.testestimatehub.model.GeneralConfiguration;

/**
 * <b>Class Name</b>: GeneralConfigurationRepository<br>
 * <b>Description</b>: General configuration jpa repository interface<br>
 * @author nishad.chayanakhawa
 */
public interface GeneralConfigurationRepository extends JpaRepository<GeneralConfiguration,Long>{
	//no need to declare functions as JPA auto-generates.
}