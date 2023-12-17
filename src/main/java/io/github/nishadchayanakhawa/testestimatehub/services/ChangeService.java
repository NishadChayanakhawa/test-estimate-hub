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
import io.github.nishadchayanakhawa.testestimatehub.model.Status;
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
	 * Constructor for ChangeService.
	 * 
	 * This constructor initializes the ChangeService with required repositories and
	 * services to manage changes, requirements, releases, use cases, and
	 * estimations in the application.
	 * 
	 * @param changeRepository            The repository for managing change
	 *                                    entities.
	 * @param requirementRepository       The repository for managing requirement
	 *                                    entities.
	 * @param releaseService              The service for managing release cycles
	 *                                    and details.
	 * @param useCaseRepository           The repository for managing use case
	 *                                    entities.
	 * @param estimationDetailRepository  The repository for managing estimation
	 *                                    details.
	 * @param estimationSummaryRepository The repository for managing estimation
	 *                                    summaries.
	 * @param generalConfigurationService The service for managing general
	 *                                    configurations of the application.
	 * @param modelMapper                 The utility for object mapping and
	 *                                    transformation in the service layer.
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
			changeToSave.setStatus(Status.PENDING_ESTIMATION);
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

	private Change get(Long id) {
		return this.changeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Change", id));
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
		Change change = this.get(id);
		logger.debug("Change debug {}", change);
		ChangeDTO changeDTO = modelMapper.map(change, ChangeDTO.class);
		// if requested depth is >0,
		if (depth > 0) {
			// attach collection of requirement DTO to change
			changeDTO.setRequirements(this.getRequirements(change, depth));

			changeDTO.setImpactedArea(new HashSet<>());
			changeDTO.setEstimationSummaryRecords(new HashSet<>());

			// for each impacted area record
			// convert application configuration to DTO and add to impactedArea collection
			change.getImpactedArea().stream().forEach(impact -> changeDTO.getImpactedArea()
					.add(modelMapper.map(impact, ApplicationConfigurationDTO.class)));
			// for each estimation summary record,
			// convert estimation summary to dto and add to estimation summary collection
			change.getEstimationSummaryRecords().stream()
					.forEach(estimationSummaryRecord -> changeDTO.getEstimationSummaryRecords()
							.add(modelMapper.map(estimationSummaryRecord, EstimationSummaryDTO.class)));
		}
		logger.debug("Retreived change: {}", changeDTO);
		// return change
		return changeDTO;
	}

	/**
	 * <b>Method Name</b>: getRequirements<br>
	 * <b>Description</b>: Returns collection of requirements for given Change.<br>
	 * 
	 * @param change change as
	 *               {@link io.github.nishadchayanakhawa.testestimatehub.model.Change
	 *               Change}
	 * @param depth  intended information depth as integer
	 * @return {@link java.util.Set Set} of
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.RequirementDTO
	 *         RequirementDTO}
	 */
	private Set<RequirementDTO> getRequirements(Change change, int depth) {
		Set<RequirementDTO> requirements = new TreeSet<>(Comparator.comparing(RequirementDTO::getIdentifier));
		// for each requirement in change record,
		change.getRequirements().stream().forEach(requirement -> {
			// map requirement to DTO
			RequirementDTO requirementDTO = modelMapper.map(requirement, RequirementDTO.class);
			// if requested depth is >1,
			if (depth > 1) {
				// attach use case collection to requirement
				requirementDTO.setUseCases(this.getUseCases(requirement, depth));
			}
			// add requirement DTO to collection
			requirements.add(requirementDTO);
		});
		return requirements;
	}

	/**
	 * <b>Method Name</b>: getUseCases<br>
	 * <b>Description</b>: Retreives Use cases for given requirement<br>
	 * 
	 * @param requirement requirement as
	 *                    {@link io.github.nishadchayanakhawa.testestimatehub.model.Requirement
	 *                    Requirement}
	 * @param depth       intended information depth as integer
	 * @return {@link java.util.Set Set} of
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.UseCaseDTO
	 *         UseCaseDTO}
	 */
	private Set<UseCaseDTO> getUseCases(Requirement requirement, int depth) {
		Set<UseCaseDTO> useCases = new HashSet<>();
		// for each use case in requirement,
		requirement.getUseCases().stream().forEach(useCase -> {
			// map use case to DTO
			UseCaseDTO useCaseDTO = modelMapper.map(useCase, UseCaseDTO.class);
			// id, depth requested is >2,
			if (depth > 2) {
				useCaseDTO.setApplicableTestTypes(new HashSet<>());
				// for each applicable test type in use case,
				useCase.getApplicableTestTypes().stream().forEach(testType -> {
					// map test type to DTO and add the same to useCaseDTO
					TestTypeDTO testTypeDTO = modelMapper.map(testType, TestTypeDTO.class);
					useCaseDTO.getApplicableTestTypes().add(testTypeDTO);
					// if depth requested is >3,
					if (depth > 3) {
						useCaseDTO.setEstimationDetails(new HashSet<>());
						// for each estimation detail in use case,
						useCase.getEstimationDetails().stream().forEach(estimationDetail -> {
							// map estimation detail to DTO and add the same to use case DTO
							EstimationDetailDTO estimationDetailDTO = this.modelMapper.map(estimationDetail,
									EstimationDetailDTO.class);
							useCaseDTO.getEstimationDetails().add(estimationDetailDTO);
						});
					}
				});
			}
			// add use case DTO to set collection
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

	/**
	 * <b>Method Name</b>: calculateEffectiveComplexity<br>
	 * <b>Description</b>: Calculate effective complexity.<br>
	 * 
	 * @param useCase              use case as
	 *                             {@link io.github.nishadchayanakhawa.testestimatehub.model.UseCase
	 *                             UseCase}
	 * @param generalConfiguration general configuration as
	 *                             {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.GeneralConfigurationDTO
	 *                             GeneralConfigurationDTO}
	 * @return calculated effective complexity as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.Complexity
	 *         Complexity}
	 */
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

	/**
	 * <b>Method Name</b>: calulateTestCaseCount<br>
	 * <b>Description</b>: Calculate base test case count<br>
	 * 
	 * @param useCase    use case as
	 *                   {@link io.github.nishadchayanakhawa.testestimatehub.model.UseCase
	 *                   UseCase}
	 * @param testType   test type as
	 *                   {@link io.github.nishadchayanakhawa.testestimatehub.model.TestType
	 *                   TestType}
	 * @param changeType change type as
	 *                   {@link io.github.nishadchayanakhawa.testestimatehub.model.ChangeType
	 *                   ChangeType}
	 * @return
	 */
	private static int calulateTestCaseCount(UseCase useCase, TestType testType, ChangeType changeType) {
		// test case count is=
		// base test case count for given business functionality * data variation count
		// *
		// (relative test case count percentage based on test type/100) *
		// test case count modifier based on change type
		return (int) Math
				.round(((useCase.getBusinessFunctionality().getBaseTestScriptCount() * useCase.getDataVariationCount())
						* (testType.getRelativeTestCaseCountPercentage() / 100))
						* changeType.getTestCaseCountModifier());
	}

	/**
	 * <b>Method Name</b>: getEstimationID<br>
	 * <b>Description</b>: Get estimation record id<br>
	 * 
	 * @param existingEstimationMap estimation record map
	 * @param testTypeName          name of test type
	 * @return if found, matching id, null otherwise
	 */
	private Long getEstimationID(Map<String, Long> existingEstimationMap, String testTypeName) {
		// if test type name is found in map, return corresponding id,
		// else return null
		return existingEstimationMap.containsKey(testTypeName) ? existingEstimationMap.get(testTypeName) : null;
	}

	/**
	 * <b>Method Name</b>: calculateEstimation<br>
	 * <b>Description</b>: Calculate estimates.<br>
	 * 
	 * @param id change id
	 * @return change record with calculated estimates as
	 *         {@link io.github.nishadchayanakhawa.testestimatehub.model.dto.ChangeDTO
	 *         ChangeDTO}
	 */
	public ChangeDTO calculateEstimation(Long id) {
		// get change record
		Change change = this.get(id);

		// create map of test type -> existing estimation summary record id
		Map<String, Long> existingEstimationSummaryByTestTypeNameMap = new HashMap<>();
		// map of test type -> estimation summary record
		Map<String, EstimationSummary> estimationSummaryByTestTypeNameMap = new HashMap<>();

		// for each existing estimation summary records,
		// add test type name -> estimation summary id to
		// existingEstimationSummaryByTestTypeNameMap
		change.getEstimationSummaryRecords().stream()
				.forEach(estimationSummary -> existingEstimationSummaryByTestTypeNameMap
						.put(estimationSummary.getTestType().getName(), estimationSummary.getId()));
		logger.debug("Existing summary records: {}", existingEstimationSummaryByTestTypeNameMap);
		// initialize collection to keep record of updated estimation summary records
		Set<Long> updatedEstimationSummaryRecords = new HashSet<>();

		// for each requirement in change,
		// for each use case within requirement
		change.getRequirements().stream().forEach(requirement -> requirement.getUseCases().forEach(useCase -> {
			// initialize map to keep records of existing estimation detail records
			Map<String, Long> estimationByTestTypeNameMap = new HashMap<>();
			// for each existing estimation detail record, add test type name -> estimation
			// detail id to estimationByTestTypeNameMap
			useCase.getEstimationDetails().stream().forEach(estimation -> estimationByTestTypeNameMap
					.put(estimation.getTestType().getName(), estimation.getId()));

			// initialize empty collection of estimation detail records
			Set<EstimationDetail> estimationDetailRecords = new HashSet<>();
			// initialize collection to keep updated estimation detail id
			Set<Long> updatedEstimationDetails = new HashSet<>();

			// get general configuration for calculations
			GeneralConfigurationDTO generalConfiguration = generalConfigurationService.get();

			// for each applicable test type within use case
			useCase.getApplicableTestTypes().stream().forEach(testType -> {
				// initialize estimation detail object
				EstimationDetail estimationDetail = new EstimationDetail();
				// set test type
				estimationDetail.setTestType(testType);
				// set id if record exists, null otherwise
				estimationDetail.setId(this.getEstimationID(estimationByTestTypeNameMap, testType.getName()));

				// calculate and set test case count
				estimationDetail.setTestCaseCount(
						ChangeService.calulateTestCaseCount(useCase, testType, change.getChangeType()));
				// re-execution count = test count * re-execution percentage/100
				estimationDetail.setReExecutionCount((int) Math
						.round((estimationDetail.getTestCaseCount() * testType.getReExecutionPercentage()) / 100));
				// additional cycle execution count=test cound * additional cycle execution
				// percentage/100
				estimationDetail.setAdditionalCycleExecutionCount((int) Math
						.round((estimationDetail.getTestCaseCount() * testType.getAdditionalCycleExecutionPercentage())
								/ 100));
				// total executions=test case count(cycle-0) + re-excution count + additional
				// cycle count
				estimationDetail.setTotalExecutionCount(estimationDetail.getTestCaseCount()
						+ estimationDetail.getReExecutionCount() + estimationDetail.getAdditionalCycleExecutionCount());

				// calculate effective complexity based on use case test config, data,
				// transaction and validation complexity
				Complexity effectiveComplexity = ChangeService.calculateEffectiveComplexity(useCase,
						generalConfiguration);

				// calculate design efforts=
				// (test case count / design productivity for complexity) * Working hours
				estimationDetail.setDesignEfforts(ChangeService.round((estimationDetail.getTestCaseCount()
						/ generalConfiguration.getTestDesignProductivity().get(effectiveComplexity))
						* ChangeService.WORKING_HOURS));

				// calculate execution efforts=
				// (test case count / execution productivity for complexity) * Working hours
				estimationDetail.setExecutionEfforts(ChangeService.round((estimationDetail.getTotalExecutionCount()
						/ generalConfiguration.getTestExecutionProductivity().get(effectiveComplexity))
						* ChangeService.WORKING_HOURS));

				// total efforts = design + execution efforts
				estimationDetail.setTotalEfforts(ChangeService
						.round(estimationDetail.getDesignEfforts() + estimationDetail.getExecutionEfforts()));

				// add estimation detail record to collection
				estimationDetailRecords.add(estimationDetail);
				// add estimation detail record id to collection
				updatedEstimationDetails.add(estimationDetail.getId());

				// Add detail record to summary record
				logger.debug("Generating summary for test type {}", testType.getName());
				// if summary record for given test type name isn't present,
				if (!estimationSummaryByTestTypeNameMap.containsKey(testType.getName())) {
					// create new estimation summary object
					EstimationSummary estimationSummary = new EstimationSummary();
					// if record exists, set id, else set to null
					estimationSummary.setId(existingEstimationSummaryByTestTypeNameMap.containsKey(testType.getName())
							? existingEstimationSummaryByTestTypeNameMap.get(testType.getName())
							: null);
					// set test type
					estimationSummary.setTestType(testType);
					// add estimation summary record to map against test type name
					estimationSummaryByTestTypeNameMap.put(testType.getName(), estimationSummary);
				}
				// for estimation summary record against given test type, add estimation detail
				// record to update summary record
				estimationSummaryByTestTypeNameMap.get(testType.getName()).addEstimationDetail(estimationDetail);
				// update summary record id to updated estimation summary id collection
				updatedEstimationSummaryRecords.add(estimationSummaryByTestTypeNameMap.get(testType.getName()).getId());
			});

			// for each estimation detail record in use case,
			useCase.getEstimationDetails().stream().forEach(estimation -> {
				// if id doesn't exist in collection of updated id,
				if (!updatedEstimationDetails.contains(estimation.getId())) {
					// delete corresponding record as corresponding test type is not applicable
					this.estimationDetailRepository.deleteById(estimation.getId());
				}
			});

			// add estimation detail record to use case
			useCase.setEstimationDetails(new HashSet<>());
			estimationDetailRecords.stream().forEach(estimation -> useCase.addEstimationDetail(estimation));

			// save use case along with collection of estimation detail records
			this.useCaseRepository.saveAndFlush(useCase);
		}));

		// for each estimation summary record in change,
		change.getEstimationSummaryRecords().stream().forEach(estimationSummary -> {
			// if record id is not in collection of updated id
			if (!updatedEstimationSummaryRecords.contains(estimationSummary.getId())) {
				// delete the summary record as corresponding test type is not applicable
				// anymore
				this.estimationSummaryRepository.deleteById(estimationSummary.getId());
			}
		});

		// add estimation summary records to change
		change.setEstimationSummaryRecords(new HashSet<>());
		estimationSummaryByTestTypeNameMap.values().stream().forEach(change::addEstimationSummaryRecord);

		// set total design efforts as some of all individual design efforts in
		// estimation detail record
		change.setDesignEfforts(ChangeService.round(
				change.getEstimationSummaryRecords().stream().mapToDouble(EstimationSummary::getDesignEfforts).sum()));
		// set total execution efforts as some of all individual execution efforts in
		// estimation detail record
		change.setExecutionEfforts(ChangeService.round(change.getEstimationSummaryRecords().stream()
				.mapToDouble(EstimationSummary::getExecutionEfforts).sum()));
		// total effort=design + execution efforts
		double totalEfforts = ChangeService.round(change.getDesignEfforts() + change.getExecutionEfforts());
		// planning effort = total effort * (planning effort percentage for given change
		// type/100)
		change.setPlanningEfforts(ChangeService
				.round(((totalEfforts * change.getChangeType().getTestPlanningEffortAllocationPercentage()) / 100)));
		// preparation effort = total effort * (preparation effort percentage for given
		// change type/100)
		change.setPreparationEfforts(ChangeService
				.round(((totalEfforts * change.getChangeType().getTestPreparationEffortAllocationPercentage()) / 100)));
		// management effort = total effort * (management effort percentage for given
		// change type/100)
		change.setManagementEfforts(ChangeService
				.round(((totalEfforts * change.getChangeType().getManagementEffortAllocationPercentage()) / 100)));
		// total effort = design + execution + planning + preparation + management
		change.setTotalEfforts(ChangeService.round(totalEfforts + change.getPlanningEfforts()
				+ change.getPreparationEfforts() + change.getPlanningEfforts()));

		change.setStatus(Status.PENDING_ESTIMATION);

		// save change along with collection of estimation summary
		this.changeRepository.saveAndFlush(change);

		// return change record with depth=4, to include estimation detail and summary
		return this.get(id, 4);
	}

	public void submitEstimationsForReview(Long id) {
		// get change record
		Change change = this.get(id);
		// if change record isn't estimated,
		if (change.getTotalEfforts()==0d) {
			// throw exception
			throw new TransactionException(
					"Please calculate estimates before submitting for review");
		}
		// else, change status to Pending Estimation Review
		change.setStatus(Status.PENDING_ESTIMATION_REVIEW);
		// save record
		this.changeRepository.saveAndFlush(change);
	}

	public void approveEstimations(Long id) {
		// get change record
		Change change = this.get(id);
		// if status is not pending for review,
		if (change.getStatus() != Status.PENDING_ESTIMATION_REVIEW) {
			// throw exception
			throw new TransactionException(
					String.format("Current status is '%s'.Please submit estimations for review first.",
							change.getStatus().getDisplayValue()));
		}
		// else change status to pending resource allocation
		change.setStatus(Status.PENDING_RESOURCE_ALLOCATION);
		// save record
		this.changeRepository.saveAndFlush(change);
	}

	/**
	 * <b>Method Name</b>: round<br>
	 * <b>Description</b>: round value to two precision<br>
	 * 
	 * @param value as double
	 * @return rounded value as double
	 */
	public static double round(double value) {
		BigDecimal bd = new BigDecimal(Double.toString(value));
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
}