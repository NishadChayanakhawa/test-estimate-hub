package io.github.nishadchayanakhawa.testestimatehub.configurations;

import java.io.IOException;
import java.util.List;
//logger
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import io.github.nishadchayanakhawa.testestimatehub.model.dto.UserDTO;
import io.github.nishadchayanakhawa.testestimatehub.services.UserService;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.TestEstimateHubExceptions;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Profile("qa")
public class CommandLineAppStartupRunner implements CommandLineRunner {
	private static final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
			.getLogger(CommandLineAppStartupRunner.class);

	private static ObjectMapper objectMapper = new ObjectMapper();

	@Value("${user.records}")
	private Resource userRecords;

	@Value("${applicationConfiguration.records}")
	private Resource applicationConfigurationRecords;
	
	@Value("${testType.records}")
	private Resource testTypeRecords;
	
	@Value("${changeType.records}")
	private Resource changeTypeRecords;

	private UserService userService;

	@Autowired
	public CommandLineAppStartupRunner(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void run(String... args) {
		try {
			loadDefaultUser();
			logger.info("Application started. Please navigate to http://localhost:8999/login");
		} catch (Exception e) {
			throw new TestEstimateHubExceptions(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
					"Unhandled exception");
		}
	}

	private void loadDefaultUser() throws IOException {
		if (userService.getAll().isEmpty()) {
			logger.warn("No users were found. Default user will be created.");
			UserDTO[] users = objectMapper.readValue(userRecords.getContentAsByteArray(), UserDTO[].class);
			List.of(users).stream().forEach(user -> {
				UserDTO savedUser = this.userService.save(user);
				logger.info("User Saved: {}", savedUser);
			});
		}
	}
}