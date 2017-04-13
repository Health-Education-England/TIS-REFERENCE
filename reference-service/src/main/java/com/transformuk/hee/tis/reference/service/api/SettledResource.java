package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.service.domain.Settled;
import com.transformuk.hee.tis.reference.service.repository.SettledRepository;
import com.transformuk.hee.tis.reference.service.api.dto.SettledDTO;
import com.transformuk.hee.tis.reference.service.service.mapper.SettledMapper;
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
 * REST controller for managing Settled.
 */
@RestController
@RequestMapping("/api")
public class SettledResource {

	private static final String ENTITY_NAME = "settled";
	private final Logger log = LoggerFactory.getLogger(SettledResource.class);
	private final SettledRepository settledRepository;

	private final SettledMapper settledMapper;

	public SettledResource(SettledRepository settledRepository, SettledMapper settledMapper) {
		this.settledRepository = settledRepository;
		this.settledMapper = settledMapper;
	}

	/**
	 * POST  /settleds : Create a new settled.
	 *
	 * @param settledDTO the settledDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new settledDTO, or with status 400 (Bad Request) if the settled has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/settleds")
	@Timed
	public ResponseEntity<SettledDTO> createSettled(@Valid @RequestBody SettledDTO settledDTO) throws URISyntaxException {
		log.debug("REST request to save Settled : {}", settledDTO);
		if (settledDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new settled cannot already have an ID")).body(null);
		}
		Settled settled = settledMapper.settledDTOToSettled(settledDTO);
		settled = settledRepository.save(settled);
		SettledDTO result = settledMapper.settledToSettledDTO(settled);
		return ResponseEntity.created(new URI("/api/settleds/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /settleds : Updates an existing settled.
	 *
	 * @param settledDTO the settledDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated settledDTO,
	 * or with status 400 (Bad Request) if the settledDTO is not valid,
	 * or with status 500 (Internal Server Error) if the settledDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/settleds")
	@Timed
	public ResponseEntity<SettledDTO> updateSettled(@Valid @RequestBody SettledDTO settledDTO) throws URISyntaxException {
		log.debug("REST request to update Settled : {}", settledDTO);
		if (settledDTO.getId() == null) {
			return createSettled(settledDTO);
		}
		Settled settled = settledMapper.settledDTOToSettled(settledDTO);
		settled = settledRepository.save(settled);
		SettledDTO result = settledMapper.settledToSettledDTO(settled);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, settledDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /settleds : get all the settleds.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of settleds in body
	 */
	@GetMapping("/settleds")
	@Timed
	public List<SettledDTO> getAllSettleds() {
		log.debug("REST request to get all Settleds");
		List<Settled> settleds = settledRepository.findAll();
		return settledMapper.settledsToSettledDTOs(settleds);
	}

	/**
	 * GET  /settleds/:id : get the "id" settled.
	 *
	 * @param id the id of the settledDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the settledDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/settleds/{id}")
	@Timed
	public ResponseEntity<SettledDTO> getSettled(@PathVariable Long id) {
		log.debug("REST request to get Settled : {}", id);
		Settled settled = settledRepository.findOne(id);
		SettledDTO settledDTO = settledMapper.settledToSettledDTO(settled);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(settledDTO));
	}

	/**
	 * DELETE  /settleds/:id : delete the "id" settled.
	 *
	 * @param id the id of the settledDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/settleds/{id}")
	@Timed
	public ResponseEntity<Void> deleteSettled(@PathVariable Long id) {
		log.debug("REST request to delete Settled : {}", id);
		settledRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
