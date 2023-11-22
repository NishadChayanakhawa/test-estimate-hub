package io.github.nishadchayanakhawa.testestimatehub.repositories;

//import section
//spring data library
import org.springframework.data.jpa.repository.JpaRepository;

import io.github.nishadchayanakhawa.testestimatehub.model.ChangeType;

/**
 * <b>Class Name</b>: ChangeTypeRepository<br>
 * <b>Description</b>: JPA repository for Change Type.<br>
 * @author nishad.chayanakhawa
 */
public interface ChangeTypeRepository extends JpaRepository<ChangeType,Long>{
	//standard data access methods are handled by jpa automatically. No need to declare and define.
}