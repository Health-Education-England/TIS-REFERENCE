package com.transformuk.hee.tis.reference.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.domain.Trust;
import com.transformuk.hee.tis.reference.repository.TrustRepository;
import com.transformuk.hee.tis.reference.service.dto.LimitedListResponse;
import com.transformuk.hee.tis.reference.service.dto.TrustDTO;
import com.transformuk.hee.tis.reference.service.impl.SitesTrustsService;
import com.transformuk.hee.tis.reference.service.mapper.TrustMapper;
import com.transformuk.hee.tis.reference.web.rest.util.HeaderUtil;
import com.transformuk.hee.tis.reference.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import static org.springframework.web.bind.annotation.RequestMethod.GET;

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
	private final SitesTrustsService sitesTrustsService;

	private final int limit;

	public TrustResource(TrustRepository trustRepository, TrustMapper trustMapper, SitesTrustsService sitesTrustsService,
						 @Value("${search.result.limit:100}") int limit) {
		this.trustRepository = trustRepository;
		this.trustMapper = trustMapper;
		this.sitesTrustsService = sitesTrustsService;
		this.limit = limit;
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

	@ApiOperation(value = "searchTrusts()",
			notes = "Returns a list of trusts matching given search string",
			response = List.class, responseContainer = "Trusts List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Trusts list", response = LimitedListResponse.class)})
	@RequestMapping(method = GET, value = "/trusts/search")
	public LimitedListResponse<TrustDTO> searchTrusts(@RequestParam(value = "searchString") String searchString)
			throws Exception {
		List<TrustDTO> ret = trustMapper.trustsToTrustDTOs(sitesTrustsService.searchTrusts(searchString));
		return new LimitedListResponse<> (ret, limit);
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
	 * GET  /trusts/code/:code : get the "code" trust.
	 *
	 * @param code the code of the trustDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the trustDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/trusts/code/{code}")
	@Timed
	public ResponseEntity<TrustDTO> getTrust(@PathVariable String code) {
		log.debug("REST request to get Trust by code: {}", code);
		Trust trust = trustRepository.findByCode(code);
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
