package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.service.model.GmcStatus;
import com.transformuk.hee.tis.reference.service.repository.GmcStatusRepository;
import com.transformuk.hee.tis.reference.service.api.dto.GmcStatusDTO;
import com.transformuk.hee.tis.reference.service.service.mapper.GmcStatusMapper;
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
 * REST controller for managing GmcStatus.
 */
@RestController
@RequestMapping("/api")
public class GmcStatusResource {

	private static final String ENTITY_NAME = "gmcStatus";
	private final Logger log = LoggerFactory.getLogger(GmcStatusResource.class);
	private final GmcStatusRepository gmcStatusRepository;

	private final GmcStatusMapper gmcStatusMapper;

	public GmcStatusResource(GmcStatusRepository gmcStatusRepository, GmcStatusMapper gmcStatusMapper) {
		this.gmcStatusRepository = gmcStatusRepository;
		this.gmcStatusMapper = gmcStatusMapper;
	}

	/**
	 * POST  /gmc-statuses : Create a new gmcStatus.
	 *
	 * @param gmcStatusDTO the gmcStatusDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new gmcStatusDTO, or with status 400 (Bad Request) if the gmcStatus has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/gmc-statuses")
	@Timed
	public ResponseEntity<GmcStatusDTO> createGmcStatus(@Valid @RequestBody GmcStatusDTO gmcStatusDTO) throws URISyntaxException {
		log.debug("REST request to save GmcStatus : {}", gmcStatusDTO);
		if (gmcStatusDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new gmcStatus cannot already have an ID")).body(null);
		}
		GmcStatus gmcStatus = gmcStatusMapper.gmcStatusDTOToGmcStatus(gmcStatusDTO);
		gmcStatus = gmcStatusRepository.save(gmcStatus);
		GmcStatusDTO result = gmcStatusMapper.gmcStatusToGmcStatusDTO(gmcStatus);
		return ResponseEntity.created(new URI("/api/gmc-statuses/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /gmc-statuses : Updates an existing gmcStatus.
	 *
	 * @param gmcStatusDTO the gmcStatusDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated gmcStatusDTO,
	 * or with status 400 (Bad Request) if the gmcStatusDTO is not valid,
	 * or with status 500 (Internal Server Error) if the gmcStatusDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/gmc-statuses")
	@Timed
	public ResponseEntity<GmcStatusDTO> updateGmcStatus(@Valid @RequestBody GmcStatusDTO gmcStatusDTO) throws URISyntaxException {
		log.debug("REST request to update GmcStatus : {}", gmcStatusDTO);
		if (gmcStatusDTO.getId() == null) {
			return createGmcStatus(gmcStatusDTO);
		}
		GmcStatus gmcStatus = gmcStatusMapper.gmcStatusDTOToGmcStatus(gmcStatusDTO);
		gmcStatus = gmcStatusRepository.save(gmcStatus);
		GmcStatusDTO result = gmcStatusMapper.gmcStatusToGmcStatusDTO(gmcStatus);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gmcStatusDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /gmc-statuses : get all the gmcStatuses.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of gmcStatuses in body
	 */
	@GetMapping("/gmc-statuses")
	@Timed
	public List<GmcStatusDTO> getAllGmcStatuses() {
		log.debug("REST request to get all GmcStatuses");
		List<GmcStatus> gmcStatuses = gmcStatusRepository.findAll();
		return gmcStatusMapper.gmcStatusesToGmcStatusDTOs(gmcStatuses);
	}

	/**
	 * GET  /gmc-statuses/:id : get the "id" gmcStatus.
	 *
	 * @param id the id of the gmcStatusDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the gmcStatusDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/gmc-statuses/{id}")
	@Timed
	public ResponseEntity<GmcStatusDTO> getGmcStatus(@PathVariable Long id) {
		log.debug("REST request to get GmcStatus : {}", id);
		GmcStatus gmcStatus = gmcStatusRepository.findOne(id);
		GmcStatusDTO gmcStatusDTO = gmcStatusMapper.gmcStatusToGmcStatusDTO(gmcStatus);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gmcStatusDTO));
	}

	/**
	 * DELETE  /gmc-statuses/:id : delete the "id" gmcStatus.
	 *
	 * @param id the id of the gmcStatusDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/gmc-statuses/{id}")
	@Timed
	public ResponseEntity<Void> deleteGmcStatus(@PathVariable Long id) {
		log.debug("REST request to delete GmcStatus : {}", id);
		gmcStatusRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
