package com.transformuk.hee.tis.reference.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.domain.Trust;
import com.transformuk.hee.tis.reference.repository.TrustRepository;
import com.transformuk.hee.tis.reference.service.dto.TrustDTO;
import com.transformuk.hee.tis.reference.service.mapper.TrustMapper;
import com.transformuk.hee.tis.reference.web.rest.util.HeaderUtil;
import com.transformuk.hee.tis.reference.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Trust.
 */
@RestController
@RequestMapping("/api")
public class TrustResource {

	private final Logger log = LoggerFactory.getLogger(TrustResource.class);

	private static final String ENTITY_NAME = "trust";

	private final TrustRepository trustRepository;

	private final TrustMapper trustMapper;

	public TrustResource(TrustRepository trustRepository, TrustMapper trustMapper) {
		this.trustRepository = trustRepository;
		this.trustMapper = trustMapper;
	}

	/**
	 * POST  /trusts : Create a new trust.
	 *
	 * @param trustDTO the trustDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new trustDTO, or with status 400 (Bad Request) if the trust has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/trusts")
	@Timed
	public ResponseEntity<TrustDTO> createTrust(@Valid @RequestBody TrustDTO trustDTO) throws URISyntaxException {
		log.debug("REST request to save Trust : {}", trustDTO);
		if (trustDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new trust cannot already have an ID")).body(null);
		}
		Trust trust = trustMapper.trustDTOToTrust(trustDTO);
		trust = trustRepository.save(trust);
		TrustDTO result = trustMapper.trustToTrustDTO(trust);
		return ResponseEntity.created(new URI("/api/trusts/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /trusts : Updates an existing trust.
	 *
	 * @param trustDTO the trustDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated trustDTO,
	 * or with status 400 (Bad Request) if the trustDTO is not valid,
	 * or with status 500 (Internal Server Error) if the trustDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/trusts")
	@Timed
	public ResponseEntity<TrustDTO> updateTrust(@Valid @RequestBody TrustDTO trustDTO) throws URISyntaxException {
		log.debug("REST request to update Trust : {}", trustDTO);
		if (trustDTO.getId() == null) {
			return createTrust(trustDTO);
		}
		Trust trust = trustMapper.trustDTOToTrust(trustDTO);
		trust = trustRepository.save(trust);
		TrustDTO result = trustMapper.trustToTrustDTO(trust);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, trustDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /trusts : get all the trusts.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of trusts in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/trusts")
	@Timed
	public ResponseEntity<List<TrustDTO>> getAllTrusts(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of Trusts");
		Page<Trust> page = trustRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/trusts");
		return new ResponseEntity<>(trustMapper.trustsToTrustDTOs(page.getContent()), headers, HttpStatus.OK);
	}

	/**
	 * GET  /trusts/:id : get the "id" trust.
	 *
	 * @param id the id of the trustDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the trustDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/trusts/{id}")
	@Timed
	public ResponseEntity<TrustDTO> getTrust(@PathVariable Long id) {
		log.debug("REST request to get Trust : {}", id);
		Trust trust = trustRepository.findOne(id);
		TrustDTO trustDTO = trustMapper.trustToTrustDTO(trust);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(trustDTO));
	}

	/**
	 * DELETE  /trusts/:id : delete the "id" trust.
	 *
	 * @param id the id of the trustDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/trusts/{id}")
	@Timed
	public ResponseEntity<Void> deleteTrust(@PathVariable Long id) {
		log.debug("REST request to delete Trust : {}", id);
		trustRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
