package io.github.nishadchayanakhawa.testestimatehub.services;

import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.nishadchayanakhawa.testestimatehub.repositories.RequirementRepository;
import io.github.nishadchayanakhawa.testestimatehub.repositories.UseCaseRepository;
import io.github.nishadchayanakhawa.testestimatehub.services.exceptions.EntityNotFoundException;
import io.github.nishadchayanakhawa.testestimatehub.model.Requirement;
import io.github.nishadchayanakhawa.testestimatehub.model.UseCase;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.EstimationDetailDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.TestTypeDTO;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.UseCaseDTO;

@Transactional
@Service
public class UseCaseService {
	private static final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
			.getLogger(UseCaseService.class);
	private UseCaseRepository useCaseRepository;
	private RequirementRepository requirementRepository;

	/** The model mapper. */
	// model mapper
	private ModelMapper modelMapper;

	public UseCaseService(UseCaseRepository useCaseRepository, RequirementRepository requirementRepository,
			ModelMapper modelMapper) {
		this.useCaseRepository = useCaseRepository;
		this.requirementRepository = requirementRepository;
		this.modelMapper = modelMapper;
	}

	public UseCaseDTO save(UseCaseDTO useCaseToSave) {
		logger.debug("Saving use case: {}", useCaseToSave);
		if (useCaseToSave.getId() == null) {
			useCaseToSave.setEstimationDetails(new HashSet<>());
		} else {
			UseCase originalUseCase = this.useCaseRepository.findById(useCaseToSave.getId()).get();
			useCaseToSave.setEstimationDetails(originalUseCase.getEstimationDetails().stream()
					.map(estimationDetail -> this.modelMapper.map(estimationDetail, EstimationDetailDTO.class))
					.collect(Collectors.toSet()));
		}
		UseCaseDTO savedUseCase = this.modelMapper.map(
				this.useCaseRepository.saveAndFlush(this.modelMapper.map(useCaseToSave, UseCase.class)),
				UseCaseDTO.class);
		logger.debug("Saved use case: {}", savedUseCase);
		return savedUseCase;
	}

	public UseCaseDTO get(Long id) {
		UseCase useCase = this.useCaseRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Use Case", id));
		UseCaseDTO useCaseDTO = this.modelMapper.map(useCase, UseCaseDTO.class);
		useCaseDTO.setApplicableTestTypes(new HashSet<>());
		useCase.getApplicableTestTypes().stream().forEach(testType -> {
			TestTypeDTO testTypeDTO = this.modelMapper.map(testType, TestTypeDTO.class);
			useCaseDTO.getApplicableTestTypes().add(testTypeDTO);
		});
		return useCaseDTO;
	}

	public void delete(Long id) {
		logger.debug("Deleting use case with id: {}", id);
		this.useCaseRepository.deleteById(id);
	}

	public Set<UseCaseDTO> findAllByRequirementId(Long requirementId) {
		Requirement requirement = this.requirementRepository.findById(requirementId).get();
		return requirement.getUseCases().stream().map(useCase -> modelMapper.map(useCase, UseCaseDTO.class))
				.collect(Collectors.toSet());
	}
}
