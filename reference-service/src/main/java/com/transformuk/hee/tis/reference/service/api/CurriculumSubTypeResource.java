package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.service.model.CurriculumSubType;
import com.transformuk.hee.tis.reference.service.repository.CurriculumSubTypeRepository;
import com.transformuk.hee.tis.reference.api.dto.CurriculumSubTypeDTO;
import com.transformuk.hee.tis.reference.service.service.mapper.CurriculumSubTypeMapper;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
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
 * REST controller for managing CurriculumSubType.
 */
@RestController
@RequestMapping("/api")
public class CurriculumSubTypeResource {

	private static final String ENTITY_NAME = "curriculumSubType";
	private final Logger log = LoggerFactory.getLogger(CurriculumSubTypeResource.class);
	private final CurriculumSubTypeRepository curriculumSubTypeRepository;

	private final CurriculumSubTypeMapper curriculumSubTypeMapper;

	public CurriculumSubTypeResource(CurriculumSubTypeRepository curriculumSubTypeRepository, CurriculumSubTypeMapper curriculumSubTypeMapper) {
		this.curriculumSubTypeRepository = curriculumSubTypeRepository;
		this.curriculumSubTypeMapper = curriculumSubTypeMapper;
	}

	/**
	 * POST  /curriculum-sub-types : Create a new curriculumSubType.
	 *
	 * @param curriculumSubTypeDTO the curriculumSubTypeDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new curriculumSubTypeDTO, or with status 400 (Bad Request) if the curriculumSubType has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/curriculum-sub-types")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<CurriculumSubTypeDTO> createCurriculumSubType(@Valid @RequestBody CurriculumSubTypeDTO curriculumSubTypeDTO) throws URISyntaxException {
		log.debug("REST request to save CurriculumSubType : {}", curriculumSubTypeDTO);
		if (curriculumSubTypeDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new curriculumSubType cannot already have an ID")).body(null);
		}
		CurriculumSubType curriculumSubType = curriculumSubTypeMapper.curriculumSubTypeDTOToCurriculumSubType(curriculumSubTypeDTO);
		curriculumSubType = curriculumSubTypeRepository.save(curriculumSubType);
		CurriculumSubTypeDTO result = curriculumSubTypeMapper.curriculumSubTypeToCurriculumSubTypeDTO(curriculumSubType);
		return ResponseEntity.created(new URI("/api/curriculum-sub-types/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /curriculum-sub-types : Updates an existing curriculumSubType.
	 *
	 * @param curriculumSubTypeDTO the curriculumSubTypeDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated curriculumSubTypeDTO,
	 * or with status 400 (Bad Request) if the curriculumSubTypeDTO is not valid,
	 * or with status 500 (Internal Server Error) if the curriculumSubTypeDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/curriculum-sub-types")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<CurriculumSubTypeDTO> updateCurriculumSubType(@Valid @RequestBody CurriculumSubTypeDTO curriculumSubTypeDTO) throws URISyntaxException {
		log.debug("REST request to update CurriculumSubType : {}", curriculumSubTypeDTO);
		if (curriculumSubTypeDTO.getId() == null) {
			return createCurriculumSubType(curriculumSubTypeDTO);
		}
		CurriculumSubType curriculumSubType = curriculumSubTypeMapper.curriculumSubTypeDTOToCurriculumSubType(curriculumSubTypeDTO);
		curriculumSubType = curriculumSubTypeRepository.save(curriculumSubType);
		CurriculumSubTypeDTO result = curriculumSubTypeMapper.curriculumSubTypeToCurriculumSubTypeDTO(curriculumSubType);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, curriculumSubTypeDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /curriculum-sub-types : get all the curriculumSubTypes.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of curriculumSubTypes in body
	 */
	@GetMapping("/curriculum-sub-types")
	@Timed
	public List<CurriculumSubTypeDTO> getAllCurriculumSubTypes() {
		log.debug("REST request to get all CurriculumSubTypes");
		List<CurriculumSubType> curriculumSubTypes = curriculumSubTypeRepository.findAll();
		return curriculumSubTypeMapper.curriculumSubTypesToCurriculumSubTypeDTOs(curriculumSubTypes);
	}

	/**
	 * GET  /curriculum-sub-types/:id : get the "id" curriculumSubType.
	 *
	 * @param id the id of the curriculumSubTypeDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the curriculumSubTypeDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/curriculum-sub-types/{id}")
	@Timed
	public ResponseEntity<CurriculumSubTypeDTO> getCurriculumSubType(@PathVariable Long id) {
		log.debug("REST request to get CurriculumSubType : {}", id);
		CurriculumSubType curriculumSubType = curriculumSubTypeRepository.findOne(id);
		CurriculumSubTypeDTO curriculumSubTypeDTO = curriculumSubTypeMapper.curriculumSubTypeToCurriculumSubTypeDTO(curriculumSubType);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(curriculumSubTypeDTO));
	}

	/**
	 * DELETE  /curriculum-sub-types/:id : delete the "id" curriculumSubType.
	 *
	 * @param id the id of the curriculumSubTypeDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/curriculum-sub-types/{id}")
	@Timed
	@PreAuthorize("hasAuthority('reference:delete:entities')")
	public ResponseEntity<Void> deleteCurriculumSubType(@PathVariable Long id) {
		log.debug("REST request to delete CurriculumSubType : {}", id);
		curriculumSubTypeRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}


	/**
	 * POST  /bulk-curriculum-sub-types : Bulk create a new curriculumSubTypes.
	 *
	 * @param curriculumSubTypeDTOs List of the curriculumSubTypeDTOs to create
	 * @return the ResponseEntity with status 200 (Created) and with body the new curriculumSubTypeDTOs, or with status 400 (Bad Request) if the curriculumSubType has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/bulk-curriculum-sub-types")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<List<CurriculumSubTypeDTO>> bulkCreateCurriculumSubType(@Valid @RequestBody List<CurriculumSubTypeDTO> curriculumSubTypeDTOs) throws URISyntaxException {
		log.debug("REST request to bulk save CurriculumSubType : {}", curriculumSubTypeDTOs);
		if (!Collections.isEmpty(curriculumSubTypeDTOs)) {
			List<Long> entityIds = curriculumSubTypeDTOs.stream()
					.filter(cst -> cst.getId() != null)
					.map(cst -> cst.getId())
					.collect(Collectors.toList());
			if(!Collections.isEmpty(entityIds)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new curriculumSubTypes cannot already have an ID")).body(null);
			}
		}
		List<CurriculumSubType> curriculumSubTypes = curriculumSubTypeMapper.curriculumSubTypeDTOsToCurriculumSubTypes(curriculumSubTypeDTOs);
		curriculumSubTypes = curriculumSubTypeRepository.save(curriculumSubTypes);
		List<CurriculumSubTypeDTO> result = curriculumSubTypeMapper.curriculumSubTypesToCurriculumSubTypeDTOs(curriculumSubTypes);
		List<Long> ids = result.stream().map(cst -> cst.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(result);
	}

	/**
	 * PUT  /bulk-curriculum-sub-types : Updates an existing curriculumSubType.
	 *
	 * @param curriculumSubTypeDTOs List of the curriculumSubTypeDTOs to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated curriculumSubTypeDTOs,
	 * or with status 400 (Bad Request) if the curriculumSubTypeDTOs is not valid,
	 * or with status 500 (Internal Server Error) if the curriculumSubTypeDTOs couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/bulk-curriculum-sub-types")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<List<CurriculumSubTypeDTO>> bulkUpdateCurriculumSubType(@Valid @RequestBody List<CurriculumSubTypeDTO> curriculumSubTypeDTOs) throws URISyntaxException {
		log.debug("REST request to bulk update CurriculumSubType : {}", curriculumSubTypeDTOs);
		if(Collections.isEmpty(curriculumSubTypeDTOs)){
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
					"The request body for this end point cannot be empty")).body(null);
		} else if (!Collections.isEmpty(curriculumSubTypeDTOs)) {
			List<CurriculumSubTypeDTO> entitiesWithNoId = curriculumSubTypeDTOs.stream().filter(cst -> cst.getId() == null).collect(Collectors.toList());
			if (!Collections.isEmpty(entitiesWithNoId)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
						"bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
			}
		}
		List<CurriculumSubType> curriculumSubTypes = curriculumSubTypeMapper.curriculumSubTypeDTOsToCurriculumSubTypes(curriculumSubTypeDTOs);
		curriculumSubTypes = curriculumSubTypeRepository.save(curriculumSubTypes);
		List<CurriculumSubTypeDTO> results = curriculumSubTypeMapper.curriculumSubTypesToCurriculumSubTypeDTOs(curriculumSubTypes);
		List<Long> ids = results.stream().map(cst -> cst.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(results);
	}
}
