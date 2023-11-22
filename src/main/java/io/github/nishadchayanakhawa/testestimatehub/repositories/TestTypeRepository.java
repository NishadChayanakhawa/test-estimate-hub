package io.github.nishadchayanakhawa.testestimatehub.repositories;

//import section
//spring data library
import org.springframework.data.jpa.repository.JpaRepository;

import io.github.nishadchayanakhawa.testestimatehub.model.TestType;

/**
 * <b>Class Name</b>: TestTypeRepository<br>
 * <b>Description</b>: JPA repository for Test Type.<br>
 * @author nishad.chayanakhawa
 */
public interface TestTypeRepository extends JpaRepository<TestType,Long>{
	//standard data access methods are handled by jpa automatically. No need to declare and define.
}