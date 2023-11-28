package io.github.nishadchayanakhawa.testestimatehub.services;

//import section
//java utils
//model mapper
import org.modelmapper.ModelMapper;
//logger
import org.slf4j.LoggerFactory;
//spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.nishadchayanakhawa.testestimatehub.model.GeneralConfiguration;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.GeneralConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.repositories.GeneralConfigurationRepository;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.EntityNotFoundException;
//exceptions
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.TransactionException;

/**
 * <b>Class Name</b>: GeneralConfigurationService<br>
 * <b>Description</b>: General configuration entity service.<br>
 * 
 * @author nishad.chayanakhawa
 */
@Transactional
@Service
public class GeneralConfigurationService {
	// logger
	private static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
			.getLogger(GeneralConfigurationService.class);

	// autowired application configuration repository
	private GeneralConfigurationRepository generalConfigurationRepository;

	// autowired model mapper
	private ModelMapper modelMapper;

	@Autowired
	public GeneralConfigurationService(GeneralConfigurationRepository generalConfigurationRepository,
			ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
		this.generalConfigurationRepository = generalConfigurationRepository;
	}

	/**
	 * <b>Method Name</b>: save<br>
	 * <b>Description</b>: Save general configuration.<br>
	 * 
	 * @param generalConfiguration configuration to save as
	 *                             {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.GeneralConfigurationDTO
	 *                             GeneralConfigurationDTO}
	 * @return saved configuration as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.GeneralConfigurationDTO
	 *         GeneralConfigurationDTO}
	 */
	public GeneralConfigurationDTO save(GeneralConfigurationDTO generalConfiguration) {
		logger.info("Saving general configuraion: {}", generalConfiguration);
		// validate if weightage sums up to exactly 100
		if ((generalConfiguration.getTestConfigurationComplexityPercentage()
				+ generalConfiguration.getTestDataComplexityPercentage()
				+ generalConfiguration.getTestTransactionComplexityPercentage()
				+ generalConfiguration.getTestValidationComplexityPercentage()) != 100) {
			// if validation fails, throw an exception
			throw new TransactionException("Test complexity weightage percentages must add up to 100.");
		}
		// override id to 1, as only single record will exist throught lifecycle.
		generalConfiguration.setId(1L);
		try {
			// persist general configuration
			GeneralConfigurationDTO savedGeneralConfiguration = modelMapper.map(
					this.generalConfigurationRepository
							.saveAndFlush(modelMapper.map(generalConfiguration, GeneralConfiguration.class)),
					GeneralConfigurationDTO.class);
			logger.info("Saved general configuraion: {}", savedGeneralConfiguration);
			return savedGeneralConfiguration;
		} catch (jakarta.validation.ConstraintViolationException e) {
			throw (TransactionException) new TransactionException(e).initCause(e);
		}
	}

	/**
	 * <b>Method Name</b>: get<br>
	 * <b>Description</b>: Retreive generalconfiguration record. ID looked up is
	 * '1'.<br>
	 * 
	 * @return general configuration record as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.GeneralConfigurationDTO
	 *         GeneralConfigurationDTO}
	 */
	public GeneralConfigurationDTO get() {
		GeneralConfiguration generalConfiguration = this.generalConfigurationRepository.findById(1L)
				.orElseThrow(() -> new EntityNotFoundException("General Configuration", 1L));
		GeneralConfigurationDTO generalConfigurationDTO = modelMapper.map(generalConfiguration,
				GeneralConfigurationDTO.class);
		generalConfigurationDTO.setTestDesignProductivity(generalConfiguration.getTestDesignProductivity());
		generalConfigurationDTO.setTestExecutionProductivity(generalConfiguration.getTestExecutionProductivity());
		logger.trace("General configuraion found: {}", generalConfigurationDTO);
		return generalConfigurationDTO;
	}
}