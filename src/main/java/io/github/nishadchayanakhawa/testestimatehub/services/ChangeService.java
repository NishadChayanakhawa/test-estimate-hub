package io.github.nishadchayanakhawa.testestimatehub.services;

//import section
//java-util
import java.util.List;
import java.util.TreeSet;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
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
import io.github.nishadchayanakhawa.testestimatehub.repositories.RequirementRepository;
import io.github.nishadchayanakhawa.testestimatehub.repositories.UseCaseRepository;
import io.github.nishadchayanakhawa.testestimatehub.repositories.EstimationDetailRepository;
import io.github.nishadchayanakhawa.testestimatehub.repositories.EstimationSummaryRepository;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.DuplicateEntityException;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.EntityNotFoundException;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.TransactionException;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.ApplicationConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.EstimationDetailDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.EstimationSummaryDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.GeneralConfigurationDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.ReleaseDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.RequirementDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.TestTypeDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.UseCaseDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.Change;
import io.github.nishadchayanakhawa.testestimatehub.model.ChangeType;
import io.github.nishadchayanakhawa.testestimatehub.model.Complexity;
import io.github.nishadchayanakhawa.testestimatehub.model.Requirement;
import io.github.nishadchayanakhawa.testestimatehub.model.TestType;
import io.github.nishadchayanakhawa.testestimatehub.model.UseCase;
import io.github.nishadchayanakhawa.testestimatehub.model.EstimationDetail;
import io.github.nishadchayanakhawa.testestimatehub.model.EstimationSummary;

/**
 * <b>Class Name</b>: ChangeService<br>
 * <b>Description</b>: Service for change entity.<br>
 * 
 * @author nishad.chayanakhawa
 */
@Transactional
@Service
public class ChangeService {
	private static final int WORKING_HOURS = 8;

	/** The Constant logger. */
	// logger
	private static final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
			.getLogger(ChangeService.class);

	/** The change repository. */
	// change type repository
	private ChangeRepository changeRepository;

	/** The requirement repository. */
	private RequirementRepository requirementRepository;

	/** The release service. */
	private ReleaseService releaseService;

	/** The use case repository. */
	private UseCaseRepository useCaseRepository;

	private EstimationDetailRepository estimationDetailRepository;

	private EstimationSummaryRepository estimationSummaryRepository;

	private GeneralConfigurationService generalConfigurationService;

	/** The model mapper. */
	// model mapper
	private ModelMapper modelMapper;

	/**
	 * Instantiates a new change service.
	 *
	 * @param changeRepository      the change repository
	 * @param requirementRepository the requirement repository
	 * @param releaseService        the release service
	 * @param useCaseRepository     the use case repository
	 * @param modelMapper           the model mapper
	 */
	@Autowired
	public ChangeService(ChangeRepository changeRepository, RequirementRepository requirementRepository,
			ReleaseService releaseService, UseCaseRepository useCaseRepository,
			EstimationDetailRepository estimationDetailRepository,
			EstimationSummaryRepository estimationSummaryRepository,
			GeneralConfigurationService generalConfigurationService, ModelMapper modelMapper) {
		this.changeRepository = changeRepository;
		this.requirementRepository = requirementRepository;
		this.releaseService = releaseService;
		this.useCaseRepository = useCaseRepository;
		this.estimationDetailRepository = estimationDetailRepository;
		this.estimationSummaryRepository = estimationSummaryRepository;
		this.generalConfigurationService = generalConfigurationService;
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
				if (requirementToSave.getId() == null) {
					requirementToSave.setUseCases(new HashSet<>());
				} else {
					requirementToSave.setUseCases(
							this.requirementRepository.findById(requirementToSave.getId()).get().getUseCases());
				}

				changeToSave.addRequirement(requirementToSave);
			});
			ChangeDTO savedChangeDTO = modelMapper.map(this.changeRepository.saveAndFlush(changeToSave),
					ChangeDTO.class);
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
	 * @param id    for change record as {@link java.lang.Long Long}
	 * @param depth the depth
	 * @return matching change record as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeDTO
	 *         ChangeDTO}
	 */
	public ChangeDTO get(Long id, int depth) {
		// retreive change based on id
		logger.debug("Retreiving change for id {}", id);
		Change change = this.changeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Change", id));
		logger.debug("Change debug {}", change);
		ChangeDTO changeDTO = modelMapper.map(change, ChangeDTO.class);

		if (depth > 0) {
			changeDTO.setRequirements(this.getRequirements(change, depth));
			changeDTO.setImpactedArea(new HashSet<>());
			changeDTO.setEstimationSummaryRecords(new HashSet<>());
			change.getImpactedArea().stream().forEach(impact -> changeDTO.getImpactedArea()
					.add(modelMapper.map(impact, ApplicationConfigurationDTO.class)));
			change.getEstimationSummaryRecords().stream()
					.forEach(estimationSummaryRecord -> changeDTO.getEstimationSummaryRecords()
							.add(modelMapper.map(estimationSummaryRecord, EstimationSummaryDTO.class)));
		}
		logger.debug("Retreived change: {}", changeDTO);
		// return change
		return changeDTO;
	}

	private Set<RequirementDTO> getRequirements(Change change, int depth) {
		Set<RequirementDTO> requirements = new TreeSet<>(Comparator.comparing(RequirementDTO::getIdentifier));

		change.getRequirements().stream().forEach(requirement -> {
			RequirementDTO requirementDTO = modelMapper.map(requirement, RequirementDTO.class);
			if (depth > 1) {
				requirementDTO.setUseCases(this.getUseCases(requirement, depth));
			}
			requirements.add(requirementDTO);
		});
		return requirements;
	}

	private Set<UseCaseDTO> getUseCases(Requirement requirement, int depth) {
		Set<UseCaseDTO> useCases = new HashSet<>();
		requirement.getUseCases().stream().forEach(useCase -> {
			UseCaseDTO useCaseDTO = modelMapper.map(useCase, UseCaseDTO.class);
			if (depth > 2) {
				useCaseDTO.setApplicableTestTypes(new HashSet<>());
				useCase.getApplicableTestTypes().stream().forEach(testType -> {
					TestTypeDTO testTypeDTO = modelMapper.map(testType, TestTypeDTO.class);
					useCaseDTO.getApplicableTestTypes().add(testTypeDTO);
					if (depth > 3) {
						useCaseDTO.setEstimationDetails(new HashSet<>());
						useCase.getEstimationDetails().stream().forEach(estimationDetail -> {
							EstimationDetailDTO estimationDetailDTO = this.modelMapper.map(estimationDetail,
									EstimationDetailDTO.class);
							useCaseDTO.getEstimationDetails().add(estimationDetailDTO);
						});
					}
				});
			}
			useCases.add(useCaseDTO);
		});
		return useCases;
	}

	/**
	 * <b>Method Name</b>: delete<br>
	 * <b>Description</b>: Delete change record<br>
	 * .
	 *
	 * @param id the id
	 */
	public void delete(Long id) {
		// delete change record
		logger.debug("Deleting change for id: {}", id);
		this.changeRepository.deleteById(id);
		logger.debug("Deleted change successfully.");
	}

	private static Complexity calculateEffectiveComplexity(UseCase useCase,
			GeneralConfigurationDTO generalConfiguration) {
		// calculate effective complexity as-
		// corresponding ordinal position +1(as ordinal starts with 0), multiply with
		// weightage from general configuration
		// round off to nearest integer
		// reduce 1 to normalize ordinal +1 increment applied earlier
		int effectiveComplexityOrdinal = ((int) Math.round(((useCase.getTestConfigurationComplexity().ordinal() + 1)
				* (generalConfiguration.getTestConfigurationComplexityPercentage() / 100))
				+ ((useCase.getTestDataSetupComplexity().ordinal() + 1)
						* (generalConfiguration.getTestDataComplexityPercentage() / 100))
				+ ((useCase.getTestTransactionComplexity().ordinal() + 1)
						* (generalConfiguration.getTestTransactionComplexityPercentage() / 100))
				+ ((useCase.getTestValidationComplexity().ordinal() + 1)
						* (generalConfiguration.getTestValidationComplexityPercentage() / 100)))
				- 1);
		// return value corresponding to calculated effective ordinal value
		return Complexity.values()[effectiveComplexityOrdinal];
	}

	private static int calulateTestCaseCount(UseCase useCase, TestType testType, ChangeType changeType) {
		return (int) Math
				.round(((useCase.getBusinessFunctionality().getBaseTestScriptCount() * useCase.getDataVariationCount())
						* (testType.getRelativeTestCaseCountPercentage() / 100))
						* changeType.getTestCaseCountModifier());
	}

	/**
	 * Calculate estimation.
	 *
	 * @param id the id
	 * @return the change DTO
	 */
	public ChangeDTO calculateEstimation(Long id) {
		Change change = this.changeRepository.findById(id).get();

		Map<String, Long> existingEstimationSummaryByTestTypeNameMap = new HashMap<>();
		Map<String, EstimationSummary> estimationSummaryByTestTypeNameMap = new HashMap<>();

		change.getEstimationSummaryRecords().stream()
				.forEach(estimationSummary -> existingEstimationSummaryByTestTypeNameMap
						.put(estimationSummary.getTestType().getName(), estimationSummary.getId()));
		logger.warn("Existing summary records: {}", existingEstimationSummaryByTestTypeNameMap);
		Set<Long> updatedEstimationSummaryRecords = new HashSet<>();

		change.getRequirements().stream().forEach(requirement -> {
			requirement.getUseCases().forEach(useCase -> {
				Map<String, Long> estimationByTestTypeNameMap = new HashMap<>();
				useCase.getEstimationDetails().stream().forEach(estimation -> estimationByTestTypeNameMap
						.put(estimation.getTestType().getName(), estimation.getId()));

				Set<EstimationDetail> estimationDetailRecords = new HashSet<>();
				Set<Long> updatedEstimationDetails = new HashSet<>();
				GeneralConfigurationDTO generalConfiguration = generalConfigurationService.get();

				useCase.getApplicableTestTypes().stream().forEach(testType -> {
					EstimationDetail estimationDetail = new EstimationDetail();
					estimationDetail.setTestType(testType);
					estimationDetail.setId(estimationByTestTypeNameMap.containsKey(testType.getName())
							? estimationByTestTypeNameMap.get(testType.getName())
							: null);

					estimationDetail.setTestCaseCount(
							ChangeService.calulateTestCaseCount(useCase, testType, change.getChangeType()));
					estimationDetail.setReExecutionCount((int) Math
							.round((estimationDetail.getTestCaseCount() * testType.getReExecutionPercentage()) / 100));
					estimationDetail.setAdditionalCycleExecutionCount((int) Math.round(
							(estimationDetail.getTestCaseCount() * testType.getAdditionalCycleExecutionPercentage())
									/ 100));
					estimationDetail.setTotalExecutionCount(
							estimationDetail.getTestCaseCount() + estimationDetail.getReExecutionCount()
									+ estimationDetail.getAdditionalCycleExecutionCount());

					Complexity effectiveComplexity = ChangeService.calculateEffectiveComplexity(useCase,
							generalConfiguration);

					estimationDetail.setDesignEfforts(ChangeService.round((estimationDetail.getTestCaseCount()
							/ generalConfiguration.getTestDesignProductivity().get(effectiveComplexity))
							* ChangeService.WORKING_HOURS));

					estimationDetail.setExecutionEfforts(ChangeService.round((estimationDetail.getTotalExecutionCount()
							/ generalConfiguration.getTestExecutionProductivity().get(effectiveComplexity))
							* ChangeService.WORKING_HOURS));

					estimationDetail.setTotalEfforts(ChangeService
							.round(estimationDetail.getDesignEfforts() + estimationDetail.getExecutionEfforts()));

					estimationDetailRecords.add(estimationDetail);
					updatedEstimationDetails.add(estimationDetail.getId());

					logger.warn("Generating summary for test type {}", testType.getName());
					if (!estimationSummaryByTestTypeNameMap.containsKey(testType.getName())) {
						logger.warn("Record not found creating new");
						EstimationSummary estimationSummary = new EstimationSummary();
						estimationSummary
								.setId(existingEstimationSummaryByTestTypeNameMap.containsKey(testType.getName())
										? existingEstimationSummaryByTestTypeNameMap.get(testType.getName())
										: null);
						logger.warn("id set to {}", estimationSummary.getId());
						estimationSummary.setTestType(testType);
						estimationSummaryByTestTypeNameMap.put(testType.getName(), estimationSummary);
					}
					estimationSummaryByTestTypeNameMap.get(testType.getName()).addEstimationDetail(estimationDetail);
					updatedEstimationSummaryRecords
							.add(estimationSummaryByTestTypeNameMap.get(testType.getName()).getId());
					logger.warn("Updated summary records set {}", updatedEstimationSummaryRecords);
				});

				useCase.getEstimationDetails().stream().forEach(estimation -> {
					if (!updatedEstimationDetails.contains(estimation.getId())) {
						this.estimationDetailRepository.deleteById(estimation.getId());
					}
				});

				useCase.setEstimationDetails(new HashSet<>());
				estimationDetailRecords.stream().forEach(estimation -> useCase.addEstimationDetail(estimation));

				this.useCaseRepository.saveAndFlush(useCase);

			});
		});

		change.getEstimationSummaryRecords().stream().forEach(estimationSummary -> {
			if (!updatedEstimationSummaryRecords.contains(estimationSummary.getId())) {
				logger.warn("Deleting record with id {}", estimationSummary.getId());
				logger.warn("and test type {}", estimationSummary.getTestType().getName());
				this.estimationSummaryRepository.deleteById(estimationSummary.getId());
			}
		});

		change.setEstimationSummaryRecords(new HashSet<>());
		logger.warn("Final summary records: {}", estimationSummaryByTestTypeNameMap);
		estimationSummaryByTestTypeNameMap.values().stream()
				.forEach(estimationSummaryRecord -> change.addEstimationSummaryRecord(estimationSummaryRecord));

		change.setDesignEfforts(ChangeService.round(
				change.getEstimationSummaryRecords().stream().mapToDouble(EstimationSummary::getDesignEfforts).sum()));
		change.setExecutionEfforts(ChangeService.round(change.getEstimationSummaryRecords().stream()
				.mapToDouble(EstimationSummary::getExecutionEfforts).sum()));
		double totalEfforts = ChangeService.round(change.getDesignEfforts() + change.getExecutionEfforts());
		change.setPlanningEfforts(ChangeService
				.round(((totalEfforts * change.getChangeType().getTestPlanningEffortAllocationPercentage()) / 100)));
		change.setPreparationEfforts(ChangeService
				.round(((totalEfforts * change.getChangeType().getTestPreparationEffortAllocationPercentage()) / 100)));
		change.setManagementEfforts(ChangeService
				.round(((totalEfforts * change.getChangeType().getManagementEffortAllocationPercentage()) / 100)));
		change.setTotalEfforts(ChangeService.round(totalEfforts + change.getPlanningEfforts()
				+ change.getPreparationEfforts() + change.getPlanningEfforts()));

		this.changeRepository.saveAndFlush(change);

		return this.get(id, 4);
	}

	public static double round(double value) {
		BigDecimal bd = new BigDecimal(Double.toString(value));
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
}