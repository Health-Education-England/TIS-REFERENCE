package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.service.model.Status;
import com.transformuk.hee.tis.reference.service.repository.StatusRepository;
import com.transformuk.hee.tis.reference.service.api.dto.StatusDTO;
import com.transformuk.hee.tis.reference.service.service.mapper.StatusMapper;
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
 * REST controller for managing Status.
 */
@RestController
@RequestMapping("/api")
public class StatusResource {

	private static final String ENTITY_NAME = "status";
	private final Logger log = LoggerFactory.getLogger(StatusResource.class);
	private final StatusRepository statusRepository;

	private final StatusMapper statusMapper;

	public StatusResource(StatusRepository statusRepository, StatusMapper statusMapper) {
		this.statusRepository = statusRepository;
		this.statusMapper = statusMapper;
	}

	/**
	 * POST  /statuses : Create a new status.
	 *
	 * @param statusDTO the statusDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new statusDTO, or with status 400 (Bad Request) if the status has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/statuses")
	@Timed
	public ResponseEntity<StatusDTO> createStatus(@Valid @RequestBody StatusDTO statusDTO) throws URISyntaxException {
		log.debug("REST request to save Status : {}", statusDTO);
		if (statusDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new status cannot already have an ID")).body(null);
		}
		Status status = statusMapper.statusDTOToStatus(statusDTO);
		status = statusRepository.save(status);
		StatusDTO result = statusMapper.statusToStatusDTO(status);
		return ResponseEntity.created(new URI("/api/statuses/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /statuses : Updates an existing status.
	 *
	 * @param statusDTO the statusDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated statusDTO,
	 * or with status 400 (Bad Request) if the statusDTO is not valid,
	 * or with status 500 (Internal Server Error) if the statusDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/statuses")
	@Timed
	public ResponseEntity<StatusDTO> updateStatus(@Valid @RequestBody StatusDTO statusDTO) throws URISyntaxException {
		log.debug("REST request to update Status : {}", statusDTO);
		if (statusDTO.getId() == null) {
			return createStatus(statusDTO);
		}
		Status status = statusMapper.statusDTOToStatus(statusDTO);
		status = statusRepository.save(status);
		StatusDTO result = statusMapper.statusToStatusDTO(status);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, statusDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /statuses : get all the statuses.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of statuses in body
	 */
	@GetMapping("/statuses")
	@Timed
	public List<StatusDTO> getAllStatuses() {
		log.debug("REST request to get all Statuses");
		List<Status> statuses = statusRepository.findAll();
		return statusMapper.statusesToStatusDTOs(statuses);
	}

	/**
	 * GET  /statuses/:id : get the "id" status.
	 *
	 * @param id the id of the statusDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the statusDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/statuses/{id}")
	@Timed
	public ResponseEntity<StatusDTO> getStatus(@PathVariable Long id) {
		log.debug("REST request to get Status : {}", id);
		Status status = statusRepository.findOne(id);
		StatusDTO statusDTO = statusMapper.statusToStatusDTO(status);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(statusDTO));
	}

	/**
	 * DELETE  /statuses/:id : delete the "id" status.
	 *
	 * @param id the id of the statusDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/statuses/{id}")
	@Timed
	public ResponseEntity<Void> deleteStatus(@PathVariable Long id) {
		log.debug("REST request to delete Status : {}", id);
		statusRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
