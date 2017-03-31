package com.transformuk.hee.tis.reference.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.domain.ProgrammeMembershipType;
import com.transformuk.hee.tis.reference.repository.ProgrammeMembershipTypeRepository;
import com.transformuk.hee.tis.reference.service.dto.ProgrammeMembershipTypeDTO;
import com.transformuk.hee.tis.reference.service.mapper.ProgrammeMembershipTypeMapper;
import com.transformuk.hee.tis.reference.web.rest.util.HeaderUtil;
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
 * REST controller for managing ProgrammeMembershipType.
 */
@RestController
@RequestMapping("/api")
public class ProgrammeMembershipTypeResource {

	private static final String ENTITY_NAME = "programmeMembershipType";
	private final Logger log = LoggerFactory.getLogger(ProgrammeMembershipTypeResource.class);
	private final ProgrammeMembershipTypeRepository programmeMembershipTypeRepository;

	private final ProgrammeMembershipTypeMapper programmeMembershipTypeMapper;

	public ProgrammeMembershipTypeResource(ProgrammeMembershipTypeRepository programmeMembershipTypeRepository, ProgrammeMembershipTypeMapper programmeMembershipTypeMapper) {
		this.programmeMembershipTypeRepository = programmeMembershipTypeRepository;
		this.programmeMembershipTypeMapper = programmeMembershipTypeMapper;
	}

	/**
	 * POST  /programme-membership-types : Create a new programmeMembershipType.
	 *
	 * @param programmeMembershipTypeDTO the programmeMembershipTypeDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new programmeMembershipTypeDTO, or with status 400 (Bad Request) if the programmeMembershipType has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/programme-membership-types")
	@Timed
	public ResponseEntity<ProgrammeMembershipTypeDTO> createProgrammeMembershipType(@Valid @RequestBody ProgrammeMembershipTypeDTO programmeMembershipTypeDTO) throws URISyntaxException {
		log.debug("REST request to save ProgrammeMembershipType : {}", programmeMembershipTypeDTO);
		if (programmeMembershipTypeDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new programmeMembershipType cannot already have an ID")).body(null);
		}
		ProgrammeMembershipType programmeMembershipType = programmeMembershipTypeMapper.programmeMembershipTypeDTOToProgrammeMembershipType(programmeMembershipTypeDTO);
		programmeMembershipType = programmeMembershipTypeRepository.save(programmeMembershipType);
		ProgrammeMembershipTypeDTO result = programmeMembershipTypeMapper.programmeMembershipTypeToProgrammeMembershipTypeDTO(programmeMembershipType);
		return ResponseEntity.created(new URI("/api/programme-membership-types/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /programme-membership-types : Updates an existing programmeMembershipType.
	 *
	 * @param programmeMembershipTypeDTO the programmeMembershipTypeDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated programmeMembershipTypeDTO,
	 * or with status 400 (Bad Request) if the programmeMembershipTypeDTO is not valid,
	 * or with status 500 (Internal Server Error) if the programmeMembershipTypeDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/programme-membership-types")
	@Timed
	public ResponseEntity<ProgrammeMembershipTypeDTO> updateProgrammeMembershipType(@Valid @RequestBody ProgrammeMembershipTypeDTO programmeMembershipTypeDTO) throws URISyntaxException {
		log.debug("REST request to update ProgrammeMembershipType : {}", programmeMembershipTypeDTO);
		if (programmeMembershipTypeDTO.getId() == null) {
			return createProgrammeMembershipType(programmeMembershipTypeDTO);
		}
		ProgrammeMembershipType programmeMembershipType = programmeMembershipTypeMapper.programmeMembershipTypeDTOToProgrammeMembershipType(programmeMembershipTypeDTO);
		programmeMembershipType = programmeMembershipTypeRepository.save(programmeMembershipType);
		ProgrammeMembershipTypeDTO result = programmeMembershipTypeMapper.programmeMembershipTypeToProgrammeMembershipTypeDTO(programmeMembershipType);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, programmeMembershipTypeDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /programme-membership-types : get all the programmeMembershipTypes.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of programmeMembershipTypes in body
	 */
	@GetMapping("/programme-membership-types")
	@Timed
	public List<ProgrammeMembershipTypeDTO> getAllProgrammeMembershipTypes() {
		log.debug("REST request to get all ProgrammeMembershipTypes");
		List<ProgrammeMembershipType> programmeMembershipTypes = programmeMembershipTypeRepository.findAll();
		return programmeMembershipTypeMapper.programmeMembershipTypesToProgrammeMembershipTypeDTOs(programmeMembershipTypes);
	}

	/**
	 * GET  /programme-membership-types/:id : get the "id" programmeMembershipType.
	 *
	 * @param id the id of the programmeMembershipTypeDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the programmeMembershipTypeDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/programme-membership-types/{id}")
	@Timed
	public ResponseEntity<ProgrammeMembershipTypeDTO> getProgrammeMembershipType(@PathVariable Long id) {
		log.debug("REST request to get ProgrammeMembershipType : {}", id);
		ProgrammeMembershipType programmeMembershipType = programmeMembershipTypeRepository.findOne(id);
		ProgrammeMembershipTypeDTO programmeMembershipTypeDTO = programmeMembershipTypeMapper.programmeMembershipTypeToProgrammeMembershipTypeDTO(programmeMembershipType);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(programmeMembershipTypeDTO));
	}

	/**
	 * DELETE  /programme-membership-types/:id : delete the "id" programmeMembershipType.
	 *
	 * @param id the id of the programmeMembershipTypeDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/programme-membership-types/{id}")
	@Timed
	public ResponseEntity<Void> deleteProgrammeMembershipType(@PathVariable Long id) {
		log.debug("REST request to delete ProgrammeMembershipType : {}", id);
		programmeMembershipTypeRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
