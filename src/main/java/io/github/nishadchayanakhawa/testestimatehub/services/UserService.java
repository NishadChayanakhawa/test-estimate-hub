package io.github.nishadchayanakhawa.testestimatehub.services;

//import section
//java utils
import java.util.List;
//model mapper
import org.modelmapper.ModelMapper;
//logger
import org.slf4j.LoggerFactory;
//spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//spring security
import org.springframework.security.crypto.password.PasswordEncoder;

import io.github.nishadchayanakhawa.testestimatehub.model.User;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.UserDTO;
import io.github.nishadchayanakhawa.testestimatehub.repositories.UserRepository;
//application exceptions
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.DuplicateEntityException;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.EntityNotFoundException;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.TransactionException;

//jpa exceptions
import org.hibernate.exception.ConstraintViolationException;

/**
 * <b>Class Name</b>: UserService<br>
 * <b>Description</b>: Service for User entity.<br>
 * 
 * @author nishad.chayanakhawa
 */
@Transactional
@Service
public class UserService {
	// logger
	private static final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
			.getLogger(UserService.class);

	// user repository
	private UserRepository userRepository;

	// model mapper
	private ModelMapper modelMapper;

	// password encoder
	private PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * <b>Method Name</b>: save<br>
	 * <b>Description</b>: Save User entity.<br>
	 * 
	 * @param user to save as
	 *             {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.UserDTO
	 *             UserDTO}
	 * @return saved user as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.UserDTO
	 *         UserDTO}
	 */
	public UserDTO save(UserDTO user) {
		if (user.getPassword().trim().isEmpty()) {
			// if password is null, retrieve old password and set again
			user.setPassword(this.userRepository.findById(user.getId()).orElseThrow().getPassword());
		} else {
			// else, encode updated password and set to user
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		try {
			// save user
			UserDTO savedUser = modelMapper.map(this.userRepository.saveAndFlush(modelMapper.map(user, User.class)),
					UserDTO.class);
			// remove password from dto object to avoid logging
			savedUser.setPassword(null);
			logger.debug("Saved user: {}", savedUser);
			// return saved user
			return savedUser;
		} catch (DataIntegrityViolationException e) {
			// in case unique constraint is violated, get constraint name
			String constraintName = ((ConstraintViolationException) e.getCause()).getConstraintName();
			if (constraintName.contains("TEH_USER_UNQ_USERNAME")) {
				// for unique username violation, throw DuplicateEntityException
				throw (DuplicateEntityException) new DuplicateEntityException("User", "username", user.getUsername())
						.initCause(e);
			} else {
				// else, assume constraint for email is violated, as only two unique constraints
				// are present
				// and throw DuplicateEntityException
				throw (DuplicateEntityException) new DuplicateEntityException("User", "email", user.getEmail())
						.initCause(e);
			}
		} catch (jakarta.validation.ConstraintViolationException e) {
			throw (TransactionException) new TransactionException(e).initCause(e);
		}
	}

	/**
	 * <b>Method Name</b>: getAll<br>
	 * <b>Description</b>: Fetch all user records.<br>
	 * 
	 * @return {@link java.util.List List} of saved user as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.UserDTO
	 *         UserDTO}
	 */
	public List<UserDTO> getAll() {
		// get list of all users from db
		List<UserDTO> users = this.userRepository.findAll().stream().map(user -> modelMapper.map(user, UserDTO.class))
				.toList();
		// set password to null in dto object
		users.stream().forEach(userDTO -> userDTO.setPassword(null));
		logger.trace("User list: {}", users);
		// return list
		return users;
	}

	/**
	 * <b>Method Name</b>: get<br>
	 * <b>Description</b>: Fetch User record by id.<br>
	 * 
	 * @param id as {@link java.lang.Long Long}
	 * @return user record found as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.UserDTO
	 *         UserDTO}
	 */
	public UserDTO get(Long id) {
		// get user by id
		// if user is not found, throw EntityNotFoundException
		UserDTO user = modelMapper.map(
				this.userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User", id)),
				UserDTO.class);
		// set password to null in dto object
		user.setPassword(null);
		logger.trace("User for id {} is: {}", id, user);
		// return user
		return user;
	}

	/**
	 * <b>Method Name</b>: delete<br>
	 * <b>Description</b>: Delete user record.<br>
	 * 
	 * @param id as {@link java.lang.Long Long}
	 */
	public void delete(Long id) {
		logger.debug("Deleting User with id {}", id);
		// delete user
		this.userRepository.deleteById(id);
	}
}