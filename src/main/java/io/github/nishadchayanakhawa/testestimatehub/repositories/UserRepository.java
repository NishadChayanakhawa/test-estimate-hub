package io.github.nishadchayanakhawa.testestimatehub.repositories;

//import section
//jpa
import org.springframework.data.jpa.repository.JpaRepository;

import io.github.nishadchayanakhawa.testestimatehub.model.User;

/**
 * <b>Class Name</b>: UserRepository<br>
 * <b>Description</b>: Jpa repository for User entity.<br>
 * 
 * @author nishad.chayanakhawa
 */
public interface UserRepository extends JpaRepository<User, Long> {
	/**
	 * <b>Method Name</b>: findByUsername<br>
	 * <b>Description</b>: Find user by username. Used in authentication
	 * service.<br>
	 * 
	 * @param username as {@link java.lang.String String}
	 * @return User as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.User
	 *         User}
	 */
	public User findByUsername(String username);

	// remaining methods need not be written as jpa will auto-generate them
}