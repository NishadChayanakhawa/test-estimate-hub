package io.github.nishadchayanakhawa.testestimatehub.services;

//import section
//java-util
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

import io.github.nishadchayanakhawa.testestimatehub.model.Release;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.ReleaseDTO;
import io.github.nishadchayanakhawa.testestimatehub.repositories.ReleaseRepository;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.DuplicateEntityException;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.TransactionException;

/**
 * <b>Class Name</b>: ReleaseService<br>
 * <b>Description</b>: Service for release entity.<br>
 * 
 * @author nishad.chayanakhawa
 */
@Transactional
@Service
public class ReleaseService {
	// logger
	private static final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
			.getLogger(ReleaseService.class);

	// change type repository
	private ReleaseRepository releaseRepository;

	// model mapper
	private ModelMapper modelMapper;

	@Autowired
	public ReleaseService(ReleaseRepository releaseRepository, ModelMapper modelMapper) {
		this.releaseRepository = releaseRepository;
		this.modelMapper = modelMapper;
	}
	
	/**
	 * <b>Method Name</b>: save<br>
	 * <b>Description</b>: Save release record.<br>
	 * 
	 * @param releaseToSaveDTO release record to save as
	 *                         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ReleaseDTO
	 *                         ReleaseDTO}
	 * @return saved release instance as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ReleaseDTO
	 *         ReleaseDTO}
	 */
	public ReleaseDTO save(ReleaseDTO releaseToSaveDTO) {
		logger.debug("Release to save: {}", releaseToSaveDTO);
		if (releaseToSaveDTO.getEndDate().isBefore(releaseToSaveDTO.getStartDate())) {
			throw new TransactionException("endDate cannot be before Start Date");
		}
		try {
			// save release record
			ReleaseDTO savedReleaseDTO = modelMapper.map(
					this.releaseRepository.saveAndFlush(modelMapper.map(releaseToSaveDTO, Release.class)), ReleaseDTO.class);
			logger.debug("Saved release : {}", savedReleaseDTO);
			// return saved release record
			return savedReleaseDTO;
		} catch (DataIntegrityViolationException e) {
			// throw exception when release identifier is not unique
			throw (DuplicateEntityException) new DuplicateEntityException("Release", "identifier",
					releaseToSaveDTO.getIdentifier()).initCause(e);
		}
	}

	/**
	 * <b>Method Name</b>: getAll<br>
	 * <b>Description</b>: Get list of all saved releases.<br>
	 * 
	 * @return {@link java.util.List List} of
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ReleaseDTO
	 *         ReleaseDTO}
	 */
	public List<ReleaseDTO> getAll() {
		// get list of saved release records
		logger.trace("Retreiving all release records");
		List<ReleaseDTO> releases = this.releaseRepository.findAll().stream()
				.map(release -> modelMapper.map(release, ReleaseDTO.class)).toList();
		logger.trace("Releases: {}", releases);
		// return the list
		return releases;
	}

	/**
	 * <b>Method Name</b>: get<br>
	 * <b>Description</b>: Retreive release record based on id.<br>
	 * 
	 * @param id for release record as {@link java.lang.Long Long}
	 * @return matching release record as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ReleaseDTO
	 *         ReleaseDTO}
	 */
	public ReleaseDTO get(Long id) {
		// retreive release based on id
		logger.trace("Retreiving release for id {}", id);
		ReleaseDTO releaseDTO = modelMapper.map(this.releaseRepository.findById(id).orElseThrow(), ReleaseDTO.class);
		logger.trace("Retreived release: {}", releaseDTO);
		// return release
		return releaseDTO;
	}

	/**
	 * <b>Method Name</b>: delete<br>
	 * <b>Description</b>: Delete release record<br>
	 * 
	 * @param releaseToDeleteDTO release record to delete as
	 *                           {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ReleaseDTO
	 *                           ReleaseDTO}. Only id is required, other fields are
	 *                           ignored and hence can be set to null.
	 */
	public void delete(Long id) {
		// delete release record
		logger.debug("Deleting release for id: {}", id);
		this.releaseRepository.deleteById(id);
		logger.debug("Deleted release successfully.");
	}
}