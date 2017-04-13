package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.service.model.PlacementType;
import com.transformuk.hee.tis.reference.service.repository.PlacementTypeRepository;
import com.transformuk.hee.tis.reference.api.dto.PlacementTypeDTO;
import com.transformuk.hee.tis.reference.service.service.mapper.PlacementTypeMapper;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
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
 * REST controller for managing PlacementType.
 */
@RestController
@RequestMapping("/api")
public class PlacementTypeResource {

	private static final String ENTITY_NAME = "placementType";
	private final Logger log = LoggerFactory.getLogger(PlacementTypeResource.class);
	private final PlacementTypeRepository placementTypeRepository;

	private final PlacementTypeMapper placementTypeMapper;

	public PlacementTypeResource(PlacementTypeRepository placementTypeRepository, PlacementTypeMapper placementTypeMapper) {
		this.placementTypeRepository = placementTypeRepository;
		this.placementTypeMapper = placementTypeMapper;
	}

	/**
	 * POST  /placement-types : Create a new placementType.
	 *
	 * @param placementTypeDTO the placementTypeDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new placementTypeDTO, or with status 400 (Bad Request) if the placementType has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/placement-types")
	@Timed
	public ResponseEntity<PlacementTypeDTO> createPlacementType(@Valid @RequestBody PlacementTypeDTO placementTypeDTO) throws URISyntaxException {
		log.debug("REST request to save PlacementType : {}", placementTypeDTO);
		if (placementTypeDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new placementType cannot already have an ID")).body(null);
		}
		PlacementType placementType = placementTypeMapper.placementTypeDTOToPlacementType(placementTypeDTO);
		placementType = placementTypeRepository.save(placementType);
		PlacementTypeDTO result = placementTypeMapper.placementTypeToPlacementTypeDTO(placementType);
		return ResponseEntity.created(new URI("/api/placement-types/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /placement-types : Updates an existing placementType.
	 *
	 * @param placementTypeDTO the placementTypeDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated placementTypeDTO,
	 * or with status 400 (Bad Request) if the placementTypeDTO is not valid,
	 * or with status 500 (Internal Server Error) if the placementTypeDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/placement-types")
	@Timed
	public ResponseEntity<PlacementTypeDTO> updatePlacementType(@Valid @RequestBody PlacementTypeDTO placementTypeDTO) throws URISyntaxException {
		log.debug("REST request to update PlacementType : {}", placementTypeDTO);
		if (placementTypeDTO.getId() == null) {
			return createPlacementType(placementTypeDTO);
		}
		PlacementType placementType = placementTypeMapper.placementTypeDTOToPlacementType(placementTypeDTO);
		placementType = placementTypeRepository.save(placementType);
		PlacementTypeDTO result = placementTypeMapper.placementTypeToPlacementTypeDTO(placementType);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, placementTypeDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /placement-types : get all the placementTypes.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of placementTypes in body
	 */
	@GetMapping("/placement-types")
	@Timed
	public List<PlacementTypeDTO> getAllPlacementTypes() {
		log.debug("REST request to get all PlacementTypes");
		List<PlacementType> placementTypes = placementTypeRepository.findAll();
		return placementTypeMapper.placementTypesToPlacementTypeDTOs(placementTypes);
	}

	/**
	 * GET  /placement-types/:id : get the "id" placementType.
	 *
	 * @param id the id of the placementTypeDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the placementTypeDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/placement-types/{id}")
	@Timed
	public ResponseEntity<PlacementTypeDTO> getPlacementType(@PathVariable Long id) {
		log.debug("REST request to get PlacementType : {}", id);
		PlacementType placementType = placementTypeRepository.findOne(id);
		PlacementTypeDTO placementTypeDTO = placementTypeMapper.placementTypeToPlacementTypeDTO(placementType);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(placementTypeDTO));
	}

	/**
	 * DELETE  /placement-types/:id : delete the "id" placementType.
	 *
	 * @param id the id of the placementTypeDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/placement-types/{id}")
	@Timed
	public ResponseEntity<Void> deletePlacementType(@PathVariable Long id) {
		log.debug("REST request to delete PlacementType : {}", id);
		placementTypeRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
