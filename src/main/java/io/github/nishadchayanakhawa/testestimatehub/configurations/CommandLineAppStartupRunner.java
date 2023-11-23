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
import io.github.nishadchayanakhawa.testestimatehub.model.dto.ApplicationConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeTypeDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.TestTypeDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.GeneralConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.services.UserService;
import io.github.nishadchayanakhawa.testestimatehub.services.ApplicationConfigurationService;
import io.github.nishadchayanakhawa.testestimatehub.services.ChangeTypeService;
import io.github.nishadchayanakhawa.testestimatehub.services.TestTypeService;
import io.github.nishadchayanakhawa.testestimatehub.services.GeneralConfigurationService;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.EntityNotFoundException;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.TestEstimateHubExceptions;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Profile("!dev")
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
	
	@Value("${generalConfiguration.record}")
	private Resource generalConfigurationRecord;

	private UserService userService;
	private ApplicationConfigurationService applicationConfigurationService;
	private ChangeTypeService changeTypeService;
	private TestTypeService testTypeService;
	private GeneralConfigurationService generalConfigurationService;
	
	private static boolean areDefaultUsersLoaded=false;

	@Autowired
	public CommandLineAppStartupRunner(UserService userService,
			ApplicationConfigurationService applicationConfigurationService,
			ChangeTypeService changeTypeService,
			TestTypeService testTypeService,
			GeneralConfigurationService generalConfigurationService) {
		this.userService = userService;
		this.applicationConfigurationService = applicationConfigurationService;
		this.changeTypeService=changeTypeService;
		this.testTypeService=testTypeService;
		this.generalConfigurationService=generalConfigurationService;
	}

	@Override
	public void run(String... args) {
		try {
			loadDefaultUser();
			loadDefaultApplicationConfiguration();
			loadDefaultChangeTypes();
			loadDefaultTestTypes();
			loadDefaultGeneralConfiguration();
			logger.info("Application started. Please navigate to http://localhost:8999/login");
			if(CommandLineAppStartupRunner.areDefaultUsersLoaded) {
				logger.info("Please login with username and password as 'admin'.");
			}
		} catch (Exception e) {
			throw new TestEstimateHubExceptions(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
					"Unhandled exception");
		}
	}

	private void loadDefaultUser() throws IOException {
		if (userService.getAll().isEmpty()) {
			logger.warn("No users were found. Default users will be created.");
			UserDTO[] users = objectMapper.readValue(userRecords.getContentAsByteArray(), UserDTO[].class);
			List.of(users).stream().forEach(user -> {
				UserDTO savedUser = this.userService.save(user);
				logger.info("User Saved: {}", savedUser);
			});
			CommandLineAppStartupRunner.areDefaultUsersLoaded=true;
		}
	}

	private void loadDefaultApplicationConfiguration() throws IOException {
		if (applicationConfigurationService.getAll().isEmpty()) {
			logger.warn("No application configuration records found. Default records will be created.");
			ApplicationConfigurationDTO[] applicationConfigurations = objectMapper
					.readValue(applicationConfigurationRecords.getContentAsByteArray(), ApplicationConfigurationDTO[].class);
			List.of(applicationConfigurations).stream().forEach(applicationConfiguration -> {
				ApplicationConfigurationDTO savedApplicationConfiguration = this.applicationConfigurationService
						.save(applicationConfiguration);
				logger.info("Application Configuration Saved: {}", savedApplicationConfiguration);
			});
		}
	}
	
	private void loadDefaultChangeTypes() throws IOException {
		if (changeTypeService.getAll().isEmpty()) {
			logger.warn("No change type records found. Default records will be created.");
			ChangeTypeDTO[] changeTypes = objectMapper
					.readValue(changeTypeRecords.getContentAsByteArray(), ChangeTypeDTO[].class);
			List.of(changeTypes).stream().forEach(changeType -> {
				ChangeTypeDTO savedChangeType = this.changeTypeService
						.save(changeType);
				logger.info("Change Type Saved: {}", savedChangeType);
			});
		}
	}
	
	private void loadDefaultTestTypes() throws IOException {
		if (testTypeService.getAll().isEmpty()) {
			logger.warn("No test type records found. Default records will be created.");
			TestTypeDTO[] testTypes = objectMapper
					.readValue(testTypeRecords.getContentAsByteArray(), TestTypeDTO[].class);
			List.of(testTypes).stream().forEach(testType -> {
				TestTypeDTO savedTestType = this.testTypeService
						.save(testType);
				logger.info("Test Type Saved: {}", savedTestType);
			});
		}
	}
	
	private void loadDefaultGeneralConfiguration() throws IOException {
		try {
			this.generalConfigurationService.get();
		} catch(EntityNotFoundException e) {
			logger.warn("No general setting record found. Default record will be created.");
			GeneralConfigurationDTO generalConfiguration=objectMapper
					.readValue(generalConfigurationRecord.getContentAsByteArray(), GeneralConfigurationDTO.class);
			GeneralConfigurationDTO savedGeneralConfiguration=
					this.generalConfigurationService.save(generalConfiguration);
			logger.info("General configuration Saved: {}", savedGeneralConfiguration);
		}
	}
}