package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.model.TrainingNumberType;
import com.transformuk.hee.tis.reference.service.repository.TrainingNumberTypeRepository;
import com.transformuk.hee.tis.reference.api.dto.TrainingNumberTypeDTO;
import com.transformuk.hee.tis.reference.service.service.mapper.TrainingNumberTypeMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TrainingNumberType.
 */
@RestController
@RequestMapping("/api")
public class TrainingNumberTypeResource {

	private static final String ENTITY_NAME = "trainingNumberType";
	private final Logger log = LoggerFactory.getLogger(TrainingNumberTypeResource.class);
	private final TrainingNumberTypeRepository trainingNumberTypeRepository;

	private final TrainingNumberTypeMapper trainingNumberTypeMapper;

	public TrainingNumberTypeResource(TrainingNumberTypeRepository trainingNumberTypeRepository, TrainingNumberTypeMapper trainingNumberTypeMapper) {
		this.trainingNumberTypeRepository = trainingNumberTypeRepository;
		this.trainingNumberTypeMapper = trainingNumberTypeMapper;
	}

	/**
	 * POST  /training-number-types : Create a new trainingNumberType.
	 *
	 * @param trainingNumberTypeDTO the trainingNumberTypeDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new trainingNumberTypeDTO, or with status 400 (Bad Request) if the trainingNumberType has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/training-number-types")
	@Timed
	public ResponseEntity<TrainingNumberTypeDTO> createTrainingNumberType(@Valid @RequestBody TrainingNumberTypeDTO trainingNumberTypeDTO) throws URISyntaxException {
		log.debug("REST request to save TrainingNumberType : {}", trainingNumberTypeDTO);
		if (trainingNumberTypeDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new trainingNumberType cannot already have an ID")).body(null);
		}
		TrainingNumberType trainingNumberType = trainingNumberTypeMapper.trainingNumberTypeDTOToTrainingNumberType(trainingNumberTypeDTO);
		trainingNumberType = trainingNumberTypeRepository.save(trainingNumberType);
		TrainingNumberTypeDTO result = trainingNumberTypeMapper.trainingNumberTypeToTrainingNumberTypeDTO(trainingNumberType);
		return ResponseEntity.created(new URI("/api/training-number-types/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /training-number-types : Updates an existing trainingNumberType.
	 *
	 * @param trainingNumberTypeDTO the trainingNumberTypeDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated trainingNumberTypeDTO,
	 * or with status 400 (Bad Request) if the trainingNumberTypeDTO is not valid,
	 * or with status 500 (Internal Server Error) if the trainingNumberTypeDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/training-number-types")
	@Timed
	public ResponseEntity<TrainingNumberTypeDTO> updateTrainingNumberType(@Valid @RequestBody TrainingNumberTypeDTO trainingNumberTypeDTO) throws URISyntaxException {
		log.debug("REST request to update TrainingNumberType : {}", trainingNumberTypeDTO);
		if (trainingNumberTypeDTO.getId() == null) {
			return createTrainingNumberType(trainingNumberTypeDTO);
		}
		TrainingNumberType trainingNumberType = trainingNumberTypeMapper.trainingNumberTypeDTOToTrainingNumberType(trainingNumberTypeDTO);
		trainingNumberType = trainingNumberTypeRepository.save(trainingNumberType);
		TrainingNumberTypeDTO result = trainingNumberTypeMapper.trainingNumberTypeToTrainingNumberTypeDTO(trainingNumberType);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, trainingNumberTypeDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /training-number-types : get all the trainingNumberTypes.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of trainingNumberTypes in body
	 */
	@GetMapping("/training-number-types")
	@Timed
	public List<TrainingNumberTypeDTO> getAllTrainingNumberTypes() {
		log.debug("REST request to get all TrainingNumberTypes");
		List<TrainingNumberType> trainingNumberTypes = trainingNumberTypeRepository.findAll();
		return trainingNumberTypeMapper.trainingNumberTypesToTrainingNumberTypeDTOs(trainingNumberTypes);
	}

	/**
	 * GET  /training-number-types/:id : get the "id" trainingNumberType.
	 *
	 * @param id the id of the trainingNumberTypeDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the trainingNumberTypeDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/training-number-types/{id}")
	@Timed
	public ResponseEntity<TrainingNumberTypeDTO> getTrainingNumberType(@PathVariable Long id) {
		log.debug("REST request to get TrainingNumberType : {}", id);
		TrainingNumberType trainingNumberType = trainingNumberTypeRepository.findOne(id);
		TrainingNumberTypeDTO trainingNumberTypeDTO = trainingNumberTypeMapper.trainingNumberTypeToTrainingNumberTypeDTO(trainingNumberType);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(trainingNumberTypeDTO));
	}

	/**
	 * DELETE  /training-number-types/:id : delete the "id" trainingNumberType.
	 *
	 * @param id the id of the trainingNumberTypeDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/training-number-types/{id}")
	@Timed
	public ResponseEntity<Void> deleteTrainingNumberType(@PathVariable Long id) {
		log.debug("REST request to delete TrainingNumberType : {}", id);
		trainingNumberTypeRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
