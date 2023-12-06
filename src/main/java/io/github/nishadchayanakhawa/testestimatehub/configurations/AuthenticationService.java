package io.github.nishadchayanakhawa.testestimatehub.configurations;

//import section
//spring libraries
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//spring-security libraries
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//model and repository
import io.github.nishadchayanakhawa.testestimatehub.model.User;
import io.github.nishadchayanakhawa.testestimatehub.repositories.UserRepository;

/**
 * <b>Class Name</b>: AuthenticationService<br>
 * <b>Description</b>: Implementation of the Spring Security
 * {@link UserDetailsService} interface for authenticating users.<br>
 * This service retrieves user details from the {@link UserRepository} and is
 * used by Spring Security for authentication purposes.<br>
 * <p>
 * <b>Author:</b> nishad.chayanakhawa
 * </p>
 */
@Service
public class AuthenticationService implements UserDetailsService {

	//User repository
	private final UserRepository userRepository;

	/**
	 * <b>Constructor</b>: AuthenticationService<br>
	 * <b>Description</b>: Constructs an instance of {@code AuthenticationService}
	 * with the necessary dependencies.<br>
	 * 
	 * @param userRepository The repository for accessing user data.
	 */
	@Autowired
	public AuthenticationService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * <b>Method Name</b>: loadUserByUsername<br>
	 * <b>Description</b>: Implementation of
	 * {@link UserDetailsService#loadUserByUsername(String)}. Retrieves user details
	 * from the repository based on the provided username.<br>
	 * 
	 * @param username The username for which user details are requested.
	 * @return The {@link UserDetails} for the given username.
	 * @throws UsernameNotFoundException If the user with the given username is not
	 *                                   found.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		} else {
			return user;
		}
	}
}
