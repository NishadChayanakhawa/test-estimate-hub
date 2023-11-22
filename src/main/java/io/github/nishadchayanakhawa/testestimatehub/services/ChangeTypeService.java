package io.github.nishadchayanakhawa.testestimatehub.services;

//import section
//logger
import org.slf4j.LoggerFactory;
//java utils
import java.util.List;
//model mapper
import org.modelmapper.ModelMapper;
//spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//dto and repository
import io.github.nishadchayanakhawa.testestimatehub.model.ChangeType;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeTypeDTO;
import io.github.nishadchayanakhawa.testestimatehub.repositories.ChangeTypeRepository;
//exceptions
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.DuplicateEntityException;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.EntityNotFoundException;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.TransactionException;

/**
 * <b>Class Name</b>: ChangeTypeService<br>
 * <b>Description</b>: Service for processing Change Type.<br>
 * 
 * @author nishad.chayanakhawa
 */
@Transactional
@Service
public class ChangeTypeService {
	// logger
	private static final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
			.getLogger(ChangeTypeService.class);

	// change type repository
	private ChangeTypeRepository changeTypeRepository;

	// model mapper
	private ModelMapper modelMapper;

	@Autowired
	public ChangeTypeService(ChangeTypeRepository changeTypeRepository, ModelMapper modelMapper) {
		this.changeTypeRepository = changeTypeRepository;
		this.modelMapper = modelMapper;
	}

	/**
	 * <b>Method Name</b>: save<br>
	 * <b>Description</b>: Save change type.<br>
	 * 
	 * @param changeTypeToSaveDTO change type to save as
	 *                            {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeTypeDTO
	 *                            ChangeTypeDTO}
	 * @return saved change type as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeTypeDTO
	 *         ChangeTypeDTO}
	 */
	public ChangeTypeDTO save(ChangeTypeDTO changeTypeToSaveDTO) {
		logger.debug("Saving Change Type: {}", changeTypeToSaveDTO);
		try {
			// save change type
			ChangeTypeDTO changeTypeSavedDTO = modelMapper.map(
					this.changeTypeRepository.saveAndFlush(modelMapper.map(changeTypeToSaveDTO, ChangeType.class)),
					ChangeTypeDTO.class);
			logger.debug("Saved Change Type: {}", changeTypeSavedDTO);
			// return saved instance
			return changeTypeSavedDTO;
		} catch (DataIntegrityViolationException e) {
			// throw exception when change type name is not unique
			throw (DuplicateEntityException) new DuplicateEntityException("Change Type", "name",
					changeTypeToSaveDTO.getName()).initCause(e);
		} catch (jakarta.validation.ConstraintViolationException e) {
			throw (TransactionException) new TransactionException(e).initCause(e);
		}
	}

	/**
	 * <b>Method Name</b>: getAll<br>
	 * <b>Description</b>: Get list of change types as - {@link java.util.List List}
	 * of
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeTypeDTO
	 * ChangeTypeDTO}
	 * 
	 * @return
	 */
	public List<ChangeTypeDTO> getAll() {
		// get list of saved change types
		logger.trace("Retreiving all change type records");
		List<ChangeTypeDTO> changeTypes = this.changeTypeRepository.findAll().stream()
				.map(changeType -> modelMapper.map(changeType, ChangeTypeDTO.class)).toList();
		logger.trace("Change types: {}", changeTypes);
		// return the list
		return changeTypes;
	}

	/**
	 * <b>Method Name</b>: get<br>
	 * <b>Description</b>: Get change type as
	 * {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeTypeDTO
	 * ChangeTypeDTO} based on id
	 * 
	 * @param id change type id as as {@link java.lang.Long Long}
	 * @return matching change type as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeTypeDTO
	 *         ChangeTypeDTO}
	 */
	public ChangeTypeDTO get(Long id) {
		// retreive change type based on id
		logger.trace("Retreiving change type for id {}", id);
		ChangeTypeDTO changeTypeDTO = modelMapper.map(this.changeTypeRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Change Type", id)), ChangeTypeDTO.class);
		logger.trace("Retreived change type: {}", changeTypeDTO);
		// return change type
		return changeTypeDTO;
	}

	/**
	 * <b>Method Name</b>: delete<br>
	 * <b>Description</b>: Delete change type.<br>
	 * 
	 * @param changeTypeToDeleteDTO change type to delete as
	 *                              {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeTypeDTO
	 *                              ChangeTypeDTO}. Only id is required, other
	 *                              properties are ignored and can be null.
	 */
	public void delete(Long id) {
		// delete change type record
		logger.debug("Deleting change type by id: {}", id);
		this.changeTypeRepository.deleteById(id);
		logger.debug("Deleted change type successfully.");
	}
}