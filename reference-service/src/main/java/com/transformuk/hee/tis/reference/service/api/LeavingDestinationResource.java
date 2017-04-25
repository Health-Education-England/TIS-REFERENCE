package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.service.model.LeavingDestination;
import com.transformuk.hee.tis.reference.service.repository.LeavingDestinationRepository;
import com.transformuk.hee.tis.reference.api.dto.LeavingDestinationDTO;
import com.transformuk.hee.tis.reference.service.service.mapper.LeavingDestinationMapper;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing LeavingDestination.
 */
@RestController
@RequestMapping("/api")
public class LeavingDestinationResource {

	private static final String ENTITY_NAME = "leavingDestination";
	private final Logger log = LoggerFactory.getLogger(LeavingDestinationResource.class);
	private final LeavingDestinationRepository leavingDestinationRepository;

	private final LeavingDestinationMapper leavingDestinationMapper;

	public LeavingDestinationResource(LeavingDestinationRepository leavingDestinationRepository, LeavingDestinationMapper leavingDestinationMapper) {
		this.leavingDestinationRepository = leavingDestinationRepository;
		this.leavingDestinationMapper = leavingDestinationMapper;
	}

	/**
	 * POST  /leaving-destinations : Create a new leavingDestination.
	 *
	 * @param leavingDestinationDTO the leavingDestinationDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new leavingDestinationDTO, or with status 400 (Bad Request) if the leavingDestination has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/leaving-destinations")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<LeavingDestinationDTO> createLeavingDestination(@Valid @RequestBody LeavingDestinationDTO leavingDestinationDTO) throws URISyntaxException {
		log.debug("REST request to save LeavingDestination : {}", leavingDestinationDTO);
		if (leavingDestinationDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new leavingDestination cannot already have an ID")).body(null);
		}
		LeavingDestination leavingDestination = leavingDestinationMapper.leavingDestinationDTOToLeavingDestination(leavingDestinationDTO);
		leavingDestination = leavingDestinationRepository.save(leavingDestination);
		LeavingDestinationDTO result = leavingDestinationMapper.leavingDestinationToLeavingDestinationDTO(leavingDestination);
		return ResponseEntity.created(new URI("/api/leaving-destinations/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /leaving-destinations : Updates an existing leavingDestination.
	 *
	 * @param leavingDestinationDTO the leavingDestinationDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated leavingDestinationDTO,
	 * or with status 400 (Bad Request) if the leavingDestinationDTO is not valid,
	 * or with status 500 (Internal Server Error) if the leavingDestinationDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/leaving-destinations")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<LeavingDestinationDTO> updateLeavingDestination(@Valid @RequestBody LeavingDestinationDTO leavingDestinationDTO) throws URISyntaxException {
		log.debug("REST request to update LeavingDestination : {}", leavingDestinationDTO);
		if (leavingDestinationDTO.getId() == null) {
			return createLeavingDestination(leavingDestinationDTO);
		}
		LeavingDestination leavingDestination = leavingDestinationMapper.leavingDestinationDTOToLeavingDestination(leavingDestinationDTO);
		leavingDestination = leavingDestinationRepository.save(leavingDestination);
		LeavingDestinationDTO result = leavingDestinationMapper.leavingDestinationToLeavingDestinationDTO(leavingDestination);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, leavingDestinationDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /leaving-destinations : get all the leavingDestinations.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of leavingDestinations in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/leaving-destinations")
	@Timed
	public ResponseEntity<List<LeavingDestinationDTO>> getAllLeavingDestinations(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of LeavingDestinations");
		Page<LeavingDestination> page = leavingDestinationRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/leaving-destinations");
		return new ResponseEntity<>(leavingDestinationMapper.leavingDestinationsToLeavingDestinationDTOs(page.getContent()), headers, HttpStatus.OK);
	}

	/**
	 * GET  /leaving-destinations/:id : get the "id" leavingDestination.
	 *
	 * @param id the id of the leavingDestinationDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the leavingDestinationDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/leaving-destinations/{id}")
	@Timed
	public ResponseEntity<LeavingDestinationDTO> getLeavingDestination(@PathVariable Long id) {
		log.debug("REST request to get LeavingDestination : {}", id);
		LeavingDestination leavingDestination = leavingDestinationRepository.findOne(id);
		LeavingDestinationDTO leavingDestinationDTO = leavingDestinationMapper.leavingDestinationToLeavingDestinationDTO(leavingDestination);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(leavingDestinationDTO));
	}

	/**
	 * DELETE  /leaving-destinations/:id : delete the "id" leavingDestination.
	 *
	 * @param id the id of the leavingDestinationDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/leaving-destinations/{id}")
	@Timed
	@PreAuthorize("hasAuthority('reference:delete:entities')")
	public ResponseEntity<Void> deleteLeavingDestination(@PathVariable Long id) {
		log.debug("REST request to delete LeavingDestination : {}", id);
		leavingDestinationRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}