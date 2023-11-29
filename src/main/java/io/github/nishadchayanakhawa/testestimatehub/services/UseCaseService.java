package io.github.nishadchayanakhawa.testestimatehub.services;

import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.nishadchayanakhawa.testestimatehub.repositories.UseCaseRepository;
import io.github.nishadchayanakhawa.testestimatehub.model.UseCase;
import io.github.nishadchayanakhawa.testestimatehub.model.dto.UseCaseDTO;

@Transactional
@Service
public class UseCaseService {
	private static final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
			.getLogger(UseCaseService.class);
	private UseCaseRepository useCaseRepository;

	/** The model mapper. */
	// model mapper
	private ModelMapper modelMapper;

	public UseCaseService(UseCaseRepository useCaseRepository, ModelMapper modelMapper) {
		this.useCaseRepository = useCaseRepository;
		this.modelMapper = modelMapper;
	}

	public UseCaseDTO save(UseCaseDTO useCaseToSave) {
		logger.debug("Saving use case: {}", useCaseToSave);
		UseCaseDTO savedUseCase = this.modelMapper.map(
				this.useCaseRepository.saveAndFlush(this.modelMapper.map(useCaseToSave, UseCase.class)),
				UseCaseDTO.class);
		logger.debug("Saved use case: {}", savedUseCase);
		return savedUseCase;
	}
	
	public void delete(Long id) {
		logger.debug("Deleting use case with id: {}", id);
		this.useCaseRepository.deleteById(id);
	}
}
