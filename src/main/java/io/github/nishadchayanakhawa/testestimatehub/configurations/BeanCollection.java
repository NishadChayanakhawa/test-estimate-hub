package io.github.nishadchayanakhawa.testestimatehub.configurations;

import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * <b>Class Name</b>: BeanCollection<br>
 * <b>Description</b>: Configuration class holding a collection of bean
 * configurations used by the application.<br>
 * It includes configurations for ModelMapper and the password encoder
 * (BCryptPasswordEncoder).<br>
 * <p>
 * <b>Author:</b> nishad.chayanakhawa
 * </p>
 */
@Component
public class BeanCollection {

	/**
	 * <b>Method Name</b>: modelMapper<br>
	 * <b>Description</b>: Bean configuration for ModelMapper.<br>
	 * Configures ModelMapper with field matching enabled and sets field access
	 * level to private. Additionally, it excludes Hibernate PersistentCollections
	 * from mapping.<br>
	 * 
	 * @return An instance of {@link ModelMapper} configured for the application.
	 */
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setFieldMatchingEnabled(true)
				.setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
		modelMapper.getConfiguration()
				.setPropertyCondition(context -> !(context.getSource() instanceof PersistentCollection));
		return modelMapper;
	}

	/**
	 * <b>Method Name</b>: passwordEncoder<br>
	 * <b>Description</b>: Bean configuration for PasswordEncoder.<br>
	 * Configures and provides an instance of {@link BCryptPasswordEncoder} for
	 * encoding passwords.<br>
	 * 
	 * @return An instance of {@link PasswordEncoder}.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
