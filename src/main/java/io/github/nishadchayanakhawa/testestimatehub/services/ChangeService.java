package io.github.nishadchayanakhawa.testestimatehub.services;

//import section
//java-util
import java.util.List;
import java.util.HashSet;
//model mapper
import org.modelmapper.ModelMapper;
//logger
import org.slf4j.LoggerFactory;
//spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.nishadchayanakhawa.testestimatehub.repositories.ChangeRepository;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.DuplicateEntityException;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.EntityNotFoundException;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.TransactionException;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.ApplicationConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.ReleaseDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.RequirementDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.Change;
import io.github.nishadchayanakhawa.testestimatehub.model.Requirement;

/**
 * <b>Class Name</b>: ChangeService<br>
 * <b>Description</b>: Service for change entity.<br>
 * 
 * @author nishad.chayanakhawa
 */
@Transactional
@Service
public class ChangeService {
	// logger
	private static final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
			.getLogger(ChangeService.class);

	// change type repository
	private ChangeRepository changeRepository;

	private ReleaseService releaseService;

	// model mapper
	private ModelMapper modelMapper;

	@Autowired
	public ChangeService(ChangeRepository changeRepository, ReleaseService releaseService, ModelMapper modelMapper) {
		this.changeRepository = changeRepository;
		this.releaseService = releaseService;
		this.modelMapper = modelMapper;
	}

	/**
	 * <b>Method Name</b>: save<br>
	 * <b>Description</b>: Save change record.<br>
	 * 
	 * @param changeToSaveDTO change record to save as
	 *                        {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeDTO
	 *                        ChangeDTO}
	 * @return saved change instance as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeDTO
	 *         ChangeDTO}
	 */
	public ChangeDTO save(ChangeDTO changeToSaveDTO) {
		logger.debug("Change to save: {}", changeToSaveDTO);
		// check change dates against release dates
		ReleaseDTO release = this.releaseService.get(changeToSaveDTO.getReleaseId());
		if (changeToSaveDTO.getStartDate().isBefore(release.getStartDate())
				|| changeToSaveDTO.getEndDate().isAfter(release.getEndDate())) {
			throw new TransactionException("startDate-endDate do not align with selected release start and end dates");
		}
		if (changeToSaveDTO.getEndDate().isBefore(changeToSaveDTO.getStartDate())) {
			throw new TransactionException("startDate-endDate are incorrect. Start date cannot be before end date");
		}
		try {
			Change changeToSave = modelMapper.map(changeToSaveDTO, Change.class);
			changeToSave.setRequirements(new HashSet<>());
			changeToSaveDTO.getRequirements().stream().forEach(requirementDTO -> {
				Requirement requirementToSave = modelMapper.map(requirementDTO, Requirement.class);
				changeToSave.addRequirement(requirementToSave);
			});
			ChangeDTO savedChangeDTO = modelMapper.map(this.changeRepository.saveAndFlush(changeToSave), ChangeDTO.class);
			logger.debug("Saved change : {}", savedChangeDTO);
			// return saved change record
			return savedChangeDTO;
		} catch (DataIntegrityViolationException e) {
			if (e.getMessage().contains("TEH_UNIQUE_REQUIREMENT_PER_CHANGE")) {
				// throw exception when requirement identifier is not unique
				throw (DuplicateEntityException) new DuplicateEntityException(
						"Please remove duplicate entries from requirement id").initCause(e);
			} else if (e.getMessage().contains("PUBLIC.PRIMARY_KEY_6E ON PUBLIC.TEH_CHANGE_IMPACT")) {
				// throw exception when app configuration identifier is not unique
				throw (DuplicateEntityException) new DuplicateEntityException(
						"Please remove duplicate entries from impacted area").initCause(e);
			} else {
				// throw exception when change identifier is not unique
				logger.error("[][]: {}", e.getMessage());
				throw (DuplicateEntityException) new DuplicateEntityException("Change", "identifier",
						changeToSaveDTO.getIdentifier()).initCause(e);
			}
		}
	}

	/**
	 * <b>Method Name</b>: getAll<br>
	 * <b>Description</b>: Get list of all saved changes.<br>
	 * 
	 * @return {@link java.util.List List} of
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeDTO
	 *         ChangeDTO}
	 */
	public List<ChangeDTO> getAll() {
		// get list of saved change records
		logger.debug("Retreiving all change records");
		List<ChangeDTO> changes = this.changeRepository.findAll().stream()
				.map(change -> modelMapper.map(change, ChangeDTO.class)).toList();
		logger.debug("Changes: {}", changes);
		// return the list
		return changes;
	}

	/**
	 * <b>Method Name</b>: get<br>
	 * <b>Description</b>: Retreive change record based on id.<br>
	 * 
	 * @param id for change record as {@link java.lang.Long Long}
	 * @return matching change record as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeDTO
	 *         ChangeDTO}
	 */
	public ChangeDTO get(Long id) {
		// retreive change based on id
		logger.debug("Retreiving change for id {}", id);
		Change change = this.changeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Change", id));
		logger.debug("Change debug {}", change);
		ChangeDTO changeDTO = modelMapper.map(change, ChangeDTO.class);
		changeDTO.setRequirements(new HashSet<>());
		changeDTO.setImpactedArea(new HashSet<>());
		change.getRequirements().stream().forEach(
				requirement -> changeDTO.getRequirements().add(modelMapper.map(requirement, RequirementDTO.class)));
		change.getImpactedArea().stream().forEach(
				impact -> changeDTO.getImpactedArea().add(modelMapper.map(impact, ApplicationConfigurationDTO.class)));
		logger.debug("Retreived change: {}", changeDTO);
		// return change
		return changeDTO;
	}

	/**
	 * <b>Method Name</b>: delete<br>
	 * <b>Description</b>: Delete change record<br>
	 * 
	 * @param changeToDeleteDTO change record to delete as
	 *                          {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeDTO
	 *                          ChangeDTO}. Only id is required, other fields are
	 *                          ignored and hence can be set to null.
	 */
	public void delete(Long id) {
		// delete change record
		logger.debug("Deleting change for id: {}", id);
		this.changeRepository.deleteById(id);
		logger.debug("Deleted change successfully.");
	}
}