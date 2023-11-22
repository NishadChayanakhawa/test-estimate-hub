package io.github.nishadchayanakhawa.testestimatehub.services;

//import section
//logger
import org.slf4j.LoggerFactory;
//java utils
import java.util.List;
//model mapper
import org.modelmapper.ModelMapper;
//spring libraries
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//spring data validation
import org.springframework.dao.DataIntegrityViolationException;
//models and repositories
import io.github.nishadchayanakhawa.testestimatehub.model.ApplicationConfiguration;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.ApplicationConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.repositories.ApplicationConfigurationRepository;
//exceptions
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.DuplicateEntityException;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.EntityNotFoundException;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.TransactionException;

/**
 * <b>Class Name</b>: ApplicationConfigurationService<br>
 * <b>Description</b>: Service for processing Application Configuration.<br>
 * 
 * @author nishad.chayanakhawa
 */
@Transactional
@Service
public class ApplicationConfigurationService {
	// logger
	private static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
			.getLogger(ApplicationConfigurationService.class);

	// autowired application configuration repository
	private ApplicationConfigurationRepository applicationConfigurationRepository;

	// autowired model mapper
	private ModelMapper modelMapper;

	@Autowired
	public ApplicationConfigurationService(ApplicationConfigurationRepository applicationConfigurationRepository,
			ModelMapper modelMapper) {
		this.applicationConfigurationRepository = applicationConfigurationRepository;
		this.modelMapper = modelMapper;
	}

	/**
	 * <b>Method Name</b>: save<br>
	 * <b>Description</b>: Save application configuration.<br>
	 * 
	 * @param applicationConfigurationToSaveDTO
	 * @return
	 */
	public ApplicationConfigurationDTO save(ApplicationConfigurationDTO applicationConfigurationToSaveDTO) {
		logger.debug("Saving: {}", applicationConfigurationToSaveDTO);
		try {
			// persist application configuration
			ApplicationConfigurationDTO applicationConfigurationSavedDTO = modelMapper.map(
					this.applicationConfigurationRepository.saveAndFlush(
							modelMapper.map(applicationConfigurationToSaveDTO, ApplicationConfiguration.class)),
					ApplicationConfigurationDTO.class);
			logger.debug("Saved: {}", applicationConfigurationToSaveDTO);
			// return persisted application configuration
			return applicationConfigurationSavedDTO;
		} catch (DataIntegrityViolationException e) {
			// throw exception when app-module-subModule combination is not unique
			throw (DuplicateEntityException) new DuplicateEntityException("Application Configuration",
					"application-module-subModule",
					String.format("%s-%s-%s", applicationConfigurationToSaveDTO.getApplication(),
							applicationConfigurationToSaveDTO.getModule(),
							applicationConfigurationToSaveDTO.getSubModule()))
					.initCause(e);
		} catch (jakarta.validation.ConstraintViolationException e) {
			throw (TransactionException) new TransactionException(e).initCause(e);
		}
	}

	public List<ApplicationConfigurationDTO> getAll() {
		List<ApplicationConfigurationDTO> applicationConfigurations = this.applicationConfigurationRepository.findAll()
				.stream().map(applicationConfiguration -> modelMapper.map(applicationConfiguration,
						ApplicationConfigurationDTO.class))
				.toList();
		logger.debug("Application configurations found: {}", applicationConfigurations);
		return applicationConfigurations;
	}

	public ApplicationConfigurationDTO get(Long id) {
		logger.debug("Looking up application configuration for id: {}", id);
		ApplicationConfigurationDTO applicationConfiguration = modelMapper.map(
				this.applicationConfigurationRepository.findById(id)
						.orElseThrow(() -> new EntityNotFoundException("Application Configuration", id)),
				ApplicationConfigurationDTO.class);
		logger.debug("Application configuration found: {}", applicationConfiguration);
		return applicationConfiguration;
	}

	public void delete(Long id) {
		logger.debug("Deleting application configuration id: {}", id);
		this.applicationConfigurationRepository.deleteById(id);
	}
}