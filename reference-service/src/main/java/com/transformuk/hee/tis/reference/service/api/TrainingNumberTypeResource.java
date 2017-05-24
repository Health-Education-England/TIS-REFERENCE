package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.api.dto.TrainingNumberTypeDTO;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.model.TrainingNumberType;
import com.transformuk.hee.tis.reference.service.repository.TrainingNumberTypeRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.TrainingNumberTypeMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
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
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
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
	@PreAuthorize("hasAuthority('reference:delete:entities')")
	public ResponseEntity<Void> deleteTrainingNumberType(@PathVariable Long id) {
		log.debug("REST request to delete TrainingNumberType : {}", id);
		trainingNumberTypeRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}


	/**
	 * POST  /bulk-training-number-types : Bulk create a new training-number-types.
	 *
	 * @param trainingNumberTypeDTOS List of the trainingNumberTypeDTOS to create
	 * @return the ResponseEntity with status 200 (Created) and with body the new trainingNumberTypeDTOS, or with status 400 (Bad Request) if the TrainingNumberTypeDto has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/bulk-training-number-types")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<List<TrainingNumberTypeDTO>> bulkCreateTrainingNumberType(@Valid @RequestBody List<TrainingNumberTypeDTO> trainingNumberTypeDTOS) throws URISyntaxException {
		log.debug("REST request to bulk save TrainingNumberTypeDtos : {}", trainingNumberTypeDTOS);
		if (!Collections.isEmpty(trainingNumberTypeDTOS)) {
			List<Long> entityIds = trainingNumberTypeDTOS.stream()
					.filter(tnt -> tnt.getId() != null)
					.map(tnt -> tnt.getId())
					.collect(Collectors.toList());
			if (!Collections.isEmpty(entityIds)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new trainingNumberTypes cannot already have an ID")).body(null);
			}
		}
		List<TrainingNumberType> trainingNumberTypes = trainingNumberTypeMapper.trainingNumberTypeDTOsToTrainingNumberTypes(trainingNumberTypeDTOS);
		trainingNumberTypes = trainingNumberTypeRepository.save(trainingNumberTypes);
		List<TrainingNumberTypeDTO> result = trainingNumberTypeMapper.trainingNumberTypesToTrainingNumberTypeDTOs(trainingNumberTypes);
		List<Long> ids = result.stream().map(tnt -> tnt.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(result);
	}

	/**
	 * PUT  /bulk-training-number-types : Updates an existing training-number-types.
	 *
	 * @param trainingNumberTypeDTOS List of the trainingNumberTypeDTOS to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated trainingNumberTypeDTOS,
	 * or with status 400 (Bad Request) if the trainingNumberTypeDTOS is not valid,
	 * or with status 500 (Internal Server Error) if the trainingNumberTypeDTOS couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/bulk-training-number-types")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<List<TrainingNumberTypeDTO>> bulkUpdateTrainingNumberType(@Valid @RequestBody List<TrainingNumberTypeDTO> trainingNumberTypeDTOS) throws URISyntaxException {
		log.debug("REST request to bulk update TrainingNumberTypeDtos : {}", trainingNumberTypeDTOS);
		if (Collections.isEmpty(trainingNumberTypeDTOS)) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
					"The request body for this end point cannot be empty")).body(null);
		} else if (!Collections.isEmpty(trainingNumberTypeDTOS)) {
			List<TrainingNumberTypeDTO> entitiesWithNoId = trainingNumberTypeDTOS.stream().filter(tnt -> tnt.getId() == null).collect(Collectors.toList());
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
					"bulk.update.failed.noId", "The request body for this end point cannot be empty")).body(null);
		}
		List<TrainingNumberType> trainingNumberTypes = trainingNumberTypeMapper.trainingNumberTypeDTOsToTrainingNumberTypes(trainingNumberTypeDTOS);
		trainingNumberTypes = trainingNumberTypeRepository.save(trainingNumberTypes);
		List<TrainingNumberTypeDTO> results = trainingNumberTypeMapper.trainingNumberTypesToTrainingNumberTypeDTOs(trainingNumberTypes);
		List<Long> ids = results.stream().map(tnt -> tnt.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(results);
	}
}
