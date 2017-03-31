package com.transformuk.hee.tis.reference.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.domain.EthnicOrigin;
import com.transformuk.hee.tis.reference.repository.EthnicOriginRepository;
import com.transformuk.hee.tis.reference.service.dto.EthnicOriginDTO;
import com.transformuk.hee.tis.reference.service.mapper.EthnicOriginMapper;
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
 * REST controller for managing EthnicOrigin.
 */
@RestController
@RequestMapping("/api")
public class EthnicOriginResource {

	private static final String ENTITY_NAME = "ethnicOrigin";
	private final Logger log = LoggerFactory.getLogger(EthnicOriginResource.class);
	private final EthnicOriginRepository ethnicOriginRepository;

	private final EthnicOriginMapper ethnicOriginMapper;

	public EthnicOriginResource(EthnicOriginRepository ethnicOriginRepository, EthnicOriginMapper ethnicOriginMapper) {
		this.ethnicOriginRepository = ethnicOriginRepository;
		this.ethnicOriginMapper = ethnicOriginMapper;
	}

	/**
	 * POST  /ethnic-origins : Create a new ethnicOrigin.
	 *
	 * @param ethnicOriginDTO the ethnicOriginDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new ethnicOriginDTO, or with status 400 (Bad Request) if the ethnicOrigin has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/ethnic-origins")
	@Timed
	public ResponseEntity<EthnicOriginDTO> createEthnicOrigin(@Valid @RequestBody EthnicOriginDTO ethnicOriginDTO) throws URISyntaxException {
		log.debug("REST request to save EthnicOrigin : {}", ethnicOriginDTO);
		if (ethnicOriginDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ethnicOrigin cannot already have an ID")).body(null);
		}
		EthnicOrigin ethnicOrigin = ethnicOriginMapper.ethnicOriginDTOToEthnicOrigin(ethnicOriginDTO);
		ethnicOrigin = ethnicOriginRepository.save(ethnicOrigin);
		EthnicOriginDTO result = ethnicOriginMapper.ethnicOriginToEthnicOriginDTO(ethnicOrigin);
		return ResponseEntity.created(new URI("/api/ethnic-origins/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /ethnic-origins : Updates an existing ethnicOrigin.
	 *
	 * @param ethnicOriginDTO the ethnicOriginDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated ethnicOriginDTO,
	 * or with status 400 (Bad Request) if the ethnicOriginDTO is not valid,
	 * or with status 500 (Internal Server Error) if the ethnicOriginDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/ethnic-origins")
	@Timed
	public ResponseEntity<EthnicOriginDTO> updateEthnicOrigin(@Valid @RequestBody EthnicOriginDTO ethnicOriginDTO) throws URISyntaxException {
		log.debug("REST request to update EthnicOrigin : {}", ethnicOriginDTO);
		if (ethnicOriginDTO.getId() == null) {
			return createEthnicOrigin(ethnicOriginDTO);
		}
		EthnicOrigin ethnicOrigin = ethnicOriginMapper.ethnicOriginDTOToEthnicOrigin(ethnicOriginDTO);
		ethnicOrigin = ethnicOriginRepository.save(ethnicOrigin);
		EthnicOriginDTO result = ethnicOriginMapper.ethnicOriginToEthnicOriginDTO(ethnicOrigin);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ethnicOriginDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /ethnic-origins : get all the ethnicOrigins.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of ethnicOrigins in body
	 */
	@GetMapping("/ethnic-origins")
	@Timed
	public List<EthnicOriginDTO> getAllEthnicOrigins() {
		log.debug("REST request to get all EthnicOrigins");
		List<EthnicOrigin> ethnicOrigins = ethnicOriginRepository.findAll();
		return ethnicOriginMapper.ethnicOriginsToEthnicOriginDTOs(ethnicOrigins);
	}

	/**
	 * GET  /ethnic-origins/:id : get the "id" ethnicOrigin.
	 *
	 * @param id the id of the ethnicOriginDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the ethnicOriginDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/ethnic-origins/{id}")
	@Timed
	public ResponseEntity<EthnicOriginDTO> getEthnicOrigin(@PathVariable Long id) {
		log.debug("REST request to get EthnicOrigin : {}", id);
		EthnicOrigin ethnicOrigin = ethnicOriginRepository.findOne(id);
		EthnicOriginDTO ethnicOriginDTO = ethnicOriginMapper.ethnicOriginToEthnicOriginDTO(ethnicOrigin);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ethnicOriginDTO));
	}

	/**
	 * DELETE  /ethnic-origins/:id : delete the "id" ethnicOrigin.
	 *
	 * @param id the id of the ethnicOriginDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/ethnic-origins/{id}")
	@Timed
	public ResponseEntity<Void> deleteEthnicOrigin(@PathVariable Long id) {
		log.debug("REST request to delete EthnicOrigin : {}", id);
		ethnicOriginRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
