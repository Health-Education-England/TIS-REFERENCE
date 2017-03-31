package com.transformuk.hee.tis.reference.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.domain.MaritalStatus;
import com.transformuk.hee.tis.reference.repository.MaritalStatusRepository;
import com.transformuk.hee.tis.reference.service.dto.MaritalStatusDTO;
import com.transformuk.hee.tis.reference.service.mapper.MaritalStatusMapper;
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
 * REST controller for managing MaritalStatus.
 */
@RestController
@RequestMapping("/api")
public class MaritalStatusResource {

	private static final String ENTITY_NAME = "maritalStatus";
	private final Logger log = LoggerFactory.getLogger(MaritalStatusResource.class);
	private final MaritalStatusRepository maritalStatusRepository;

	private final MaritalStatusMapper maritalStatusMapper;

	public MaritalStatusResource(MaritalStatusRepository maritalStatusRepository, MaritalStatusMapper maritalStatusMapper) {
		this.maritalStatusRepository = maritalStatusRepository;
		this.maritalStatusMapper = maritalStatusMapper;
	}

	/**
	 * POST  /marital-statuses : Create a new maritalStatus.
	 *
	 * @param maritalStatusDTO the maritalStatusDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new maritalStatusDTO, or with status 400 (Bad Request) if the maritalStatus has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/marital-statuses")
	@Timed
	public ResponseEntity<MaritalStatusDTO> createMaritalStatus(@Valid @RequestBody MaritalStatusDTO maritalStatusDTO) throws URISyntaxException {
		log.debug("REST request to save MaritalStatus : {}", maritalStatusDTO);
		if (maritalStatusDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new maritalStatus cannot already have an ID")).body(null);
		}
		MaritalStatus maritalStatus = maritalStatusMapper.maritalStatusDTOToMaritalStatus(maritalStatusDTO);
		maritalStatus = maritalStatusRepository.save(maritalStatus);
		MaritalStatusDTO result = maritalStatusMapper.maritalStatusToMaritalStatusDTO(maritalStatus);
		return ResponseEntity.created(new URI("/api/marital-statuses/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /marital-statuses : Updates an existing maritalStatus.
	 *
	 * @param maritalStatusDTO the maritalStatusDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated maritalStatusDTO,
	 * or with status 400 (Bad Request) if the maritalStatusDTO is not valid,
	 * or with status 500 (Internal Server Error) if the maritalStatusDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/marital-statuses")
	@Timed
	public ResponseEntity<MaritalStatusDTO> updateMaritalStatus(@Valid @RequestBody MaritalStatusDTO maritalStatusDTO) throws URISyntaxException {
		log.debug("REST request to update MaritalStatus : {}", maritalStatusDTO);
		if (maritalStatusDTO.getId() == null) {
			return createMaritalStatus(maritalStatusDTO);
		}
		MaritalStatus maritalStatus = maritalStatusMapper.maritalStatusDTOToMaritalStatus(maritalStatusDTO);
		maritalStatus = maritalStatusRepository.save(maritalStatus);
		MaritalStatusDTO result = maritalStatusMapper.maritalStatusToMaritalStatusDTO(maritalStatus);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, maritalStatusDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /marital-statuses : get all the maritalStatuses.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of maritalStatuses in body
	 */
	@GetMapping("/marital-statuses")
	@Timed
	public List<MaritalStatusDTO> getAllMaritalStatuses() {
		log.debug("REST request to get all MaritalStatuses");
		List<MaritalStatus> maritalStatuses = maritalStatusRepository.findAll();
		return maritalStatusMapper.maritalStatusesToMaritalStatusDTOs(maritalStatuses);
	}

	/**
	 * GET  /marital-statuses/:id : get the "id" maritalStatus.
	 *
	 * @param id the id of the maritalStatusDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the maritalStatusDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/marital-statuses/{id}")
	@Timed
	public ResponseEntity<MaritalStatusDTO> getMaritalStatus(@PathVariable Long id) {
		log.debug("REST request to get MaritalStatus : {}", id);
		MaritalStatus maritalStatus = maritalStatusRepository.findOne(id);
		MaritalStatusDTO maritalStatusDTO = maritalStatusMapper.maritalStatusToMaritalStatusDTO(maritalStatus);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(maritalStatusDTO));
	}

	/**
	 * DELETE  /marital-statuses/:id : delete the "id" maritalStatus.
	 *
	 * @param id the id of the maritalStatusDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/marital-statuses/{id}")
	@Timed
	public ResponseEntity<Void> deleteMaritalStatus(@PathVariable Long id) {
		log.debug("REST request to delete MaritalStatus : {}", id);
		maritalStatusRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
