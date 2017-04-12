package com.transformuk.hee.tis.reference.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.domain.GdcStatus;
import com.transformuk.hee.tis.reference.repository.GdcStatusRepository;
import com.transformuk.hee.tis.reference.api.dto.GdcStatusDTO;
import com.transformuk.hee.tis.reference.service.mapper.GdcStatusMapper;
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
 * REST controller for managing GdcStatus.
 */
@RestController
@RequestMapping("/api")
public class GdcStatusResource {

	private static final String ENTITY_NAME = "gdcStatus";
	private final Logger log = LoggerFactory.getLogger(GdcStatusResource.class);
	private final GdcStatusRepository gdcStatusRepository;

	private final GdcStatusMapper gdcStatusMapper;

	public GdcStatusResource(GdcStatusRepository gdcStatusRepository, GdcStatusMapper gdcStatusMapper) {
		this.gdcStatusRepository = gdcStatusRepository;
		this.gdcStatusMapper = gdcStatusMapper;
	}

	/**
	 * POST  /gdc-statuses : Create a new gdcStatus.
	 *
	 * @param gdcStatusDTO the gdcStatusDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new gdcStatusDTO, or with status 400 (Bad Request) if the gdcStatus has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/gdc-statuses")
	@Timed
	public ResponseEntity<GdcStatusDTO> createGdcStatus(@Valid @RequestBody GdcStatusDTO gdcStatusDTO) throws URISyntaxException {
		log.debug("REST request to save GdcStatus : {}", gdcStatusDTO);
		if (gdcStatusDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new gdcStatus cannot already have an ID")).body(null);
		}
		GdcStatus gdcStatus = gdcStatusMapper.gdcStatusDTOToGdcStatus(gdcStatusDTO);
		gdcStatus = gdcStatusRepository.save(gdcStatus);
		GdcStatusDTO result = gdcStatusMapper.gdcStatusToGdcStatusDTO(gdcStatus);
		return ResponseEntity.created(new URI("/api/gdc-statuses/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /gdc-statuses : Updates an existing gdcStatus.
	 *
	 * @param gdcStatusDTO the gdcStatusDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated gdcStatusDTO,
	 * or with status 400 (Bad Request) if the gdcStatusDTO is not valid,
	 * or with status 500 (Internal Server Error) if the gdcStatusDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/gdc-statuses")
	@Timed
	public ResponseEntity<GdcStatusDTO> updateGdcStatus(@Valid @RequestBody GdcStatusDTO gdcStatusDTO) throws URISyntaxException {
		log.debug("REST request to update GdcStatus : {}", gdcStatusDTO);
		if (gdcStatusDTO.getId() == null) {
			return createGdcStatus(gdcStatusDTO);
		}
		GdcStatus gdcStatus = gdcStatusMapper.gdcStatusDTOToGdcStatus(gdcStatusDTO);
		gdcStatus = gdcStatusRepository.save(gdcStatus);
		GdcStatusDTO result = gdcStatusMapper.gdcStatusToGdcStatusDTO(gdcStatus);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gdcStatusDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /gdc-statuses : get all the gdcStatuses.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of gdcStatuses in body
	 */
	@GetMapping("/gdc-statuses")
	@Timed
	public List<GdcStatusDTO> getAllGdcStatuses() {
		log.debug("REST request to get all GdcStatuses");
		List<GdcStatus> gdcStatuses = gdcStatusRepository.findAll();
		return gdcStatusMapper.gdcStatusesToGdcStatusDTOs(gdcStatuses);
	}

	/**
	 * GET  /gdc-statuses/:id : get the "id" gdcStatus.
	 *
	 * @param id the id of the gdcStatusDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the gdcStatusDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/gdc-statuses/{id}")
	@Timed
	public ResponseEntity<GdcStatusDTO> getGdcStatus(@PathVariable Long id) {
		log.debug("REST request to get GdcStatus : {}", id);
		GdcStatus gdcStatus = gdcStatusRepository.findOne(id);
		GdcStatusDTO gdcStatusDTO = gdcStatusMapper.gdcStatusToGdcStatusDTO(gdcStatus);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gdcStatusDTO));
	}

	/**
	 * DELETE  /gdc-statuses/:id : delete the "id" gdcStatus.
	 *
	 * @param id the id of the gdcStatusDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/gdc-statuses/{id}")
	@Timed
	public ResponseEntity<Void> deleteGdcStatus(@PathVariable Long id) {
		log.debug("REST request to delete GdcStatus : {}", id);
		gdcStatusRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
