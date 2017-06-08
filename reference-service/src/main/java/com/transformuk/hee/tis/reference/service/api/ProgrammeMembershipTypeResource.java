package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.api.dto.ProgrammeMembershipTypeDTO;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.model.ProgrammeMembershipType;
import com.transformuk.hee.tis.reference.service.repository.ProgrammeMembershipTypeRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.ProgrammeMembershipTypeMapper;
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
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
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
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
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
	@PreAuthorize("hasAuthority('reference:delete:entities')")
	public ResponseEntity<Void> deleteProgrammeMembershipType(@PathVariable Long id) {
		log.debug("REST request to delete ProgrammeMembershipType : {}", id);
		programmeMembershipTypeRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}


	/**
	 * POST  /bulk-programme-membership-types : Bulk create a new programme-membership-types.
	 *
	 * @param programmeMembershipTypeDTOS List of the programmeMembershipTypeDTOS to create
	 * @return the ResponseEntity with status 200 (Created) and with body the new programmeMembershipTypeDTOS, or with status 400 (Bad Request) if the ProgrammeMembershipTypeDTO has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/bulk-programme-membership-types")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<List<ProgrammeMembershipTypeDTO>> bulkCreateProgrammeMembershipType(@Valid @RequestBody List<ProgrammeMembershipTypeDTO> programmeMembershipTypeDTOS) throws URISyntaxException {
		log.debug("REST request to bulk save ProgrammeMembershipTypeDtos : {}", programmeMembershipTypeDTOS);
		if (!Collections.isEmpty(programmeMembershipTypeDTOS)) {
			List<Long> entityIds = programmeMembershipTypeDTOS.stream()
					.filter(pmt -> pmt.getId() != null)
					.map(pmt -> pmt.getId())
					.collect(Collectors.toList());
			if (!Collections.isEmpty(entityIds)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new programmeMembershipTypes cannot already have an ID")).body(null);
			}
		}
		List<ProgrammeMembershipType> programmeMembershipTypes = programmeMembershipTypeMapper.programmeMembershipTypeDTOsToProgrammeMembershipTypes(programmeMembershipTypeDTOS);
		programmeMembershipTypes = programmeMembershipTypeRepository.save(programmeMembershipTypes);
		List<ProgrammeMembershipTypeDTO> result = programmeMembershipTypeMapper.programmeMembershipTypesToProgrammeMembershipTypeDTOs(programmeMembershipTypes);
		List<Long> ids = result.stream().map(pmt -> pmt.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(result);
	}

	/**
	 * PUT  /bulk-programme-membership-types : Updates an existing programme-membership-types.
	 *
	 * @param programmeMembershipTypeDTOS List of the programmeMembershipTypeDTOS to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated programmeMembershipTypeDTOS,
	 * or with status 400 (Bad Request) if the programmeMembershipTypeDTOS is not valid,
	 * or with status 500 (Internal Server Error) if the programmeMembershipTypeDTOS couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/bulk-programme-membership-types")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<List<ProgrammeMembershipTypeDTO>> bulkUpdateProgrammeMembershipType(@Valid @RequestBody List<ProgrammeMembershipTypeDTO> programmeMembershipTypeDTOS) throws URISyntaxException {
		log.debug("REST request to bulk update ProgrammeMembershipTypeDtos : {}", programmeMembershipTypeDTOS);
		if (Collections.isEmpty(programmeMembershipTypeDTOS)) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
					"The request body for this end point cannot be empty")).body(null);
		} else if (!Collections.isEmpty(programmeMembershipTypeDTOS)) {
			List<ProgrammeMembershipTypeDTO> entitiesWithNoId = programmeMembershipTypeDTOS.stream().filter(pmt -> pmt.getId() == null).collect(Collectors.toList());
			if (!Collections.isEmpty(entitiesWithNoId)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
						"bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
			}
		}
		List<ProgrammeMembershipType> programmeMembershipTypes = programmeMembershipTypeMapper.programmeMembershipTypeDTOsToProgrammeMembershipTypes(programmeMembershipTypeDTOS);
		programmeMembershipTypes = programmeMembershipTypeRepository.save(programmeMembershipTypes);
		List<ProgrammeMembershipTypeDTO> results = programmeMembershipTypeMapper.programmeMembershipTypesToProgrammeMembershipTypeDTOs(programmeMembershipTypes);
		List<Long> ids = results.stream().map(pmt -> pmt.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(results);
	}

}
