package com.transformuk.hee.tis.reference.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.domain.FundingIssue;
import com.transformuk.hee.tis.reference.repository.FundingIssueRepository;
import com.transformuk.hee.tis.reference.service.dto.FundingIssueDTO;
import com.transformuk.hee.tis.reference.service.mapper.FundingIssueMapper;
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
 * REST controller for managing FundingIssue.
 */
@RestController
@RequestMapping("/api")
public class FundingIssueResource {

	private static final String ENTITY_NAME = "fundingIssue";
	private final Logger log = LoggerFactory.getLogger(FundingIssueResource.class);
	private final FundingIssueRepository fundingIssueRepository;

	private final FundingIssueMapper fundingIssueMapper;

	public FundingIssueResource(FundingIssueRepository fundingIssueRepository, FundingIssueMapper fundingIssueMapper) {
		this.fundingIssueRepository = fundingIssueRepository;
		this.fundingIssueMapper = fundingIssueMapper;
	}

	/**
	 * POST  /funding-issues : Create a new fundingIssue.
	 *
	 * @param fundingIssueDTO the fundingIssueDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new fundingIssueDTO, or with status 400 (Bad Request) if the fundingIssue has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/funding-issues")
	@Timed
	public ResponseEntity<FundingIssueDTO> createFundingIssue(@Valid @RequestBody FundingIssueDTO fundingIssueDTO) throws URISyntaxException {
		log.debug("REST request to save FundingIssue : {}", fundingIssueDTO);
		if (fundingIssueDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new fundingIssue cannot already have an ID")).body(null);
		}
		FundingIssue fundingIssue = fundingIssueMapper.fundingIssueDTOToFundingIssue(fundingIssueDTO);
		fundingIssue = fundingIssueRepository.save(fundingIssue);
		FundingIssueDTO result = fundingIssueMapper.fundingIssueToFundingIssueDTO(fundingIssue);
		return ResponseEntity.created(new URI("/api/funding-issues/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /funding-issues : Updates an existing fundingIssue.
	 *
	 * @param fundingIssueDTO the fundingIssueDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated fundingIssueDTO,
	 * or with status 400 (Bad Request) if the fundingIssueDTO is not valid,
	 * or with status 500 (Internal Server Error) if the fundingIssueDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/funding-issues")
	@Timed
	public ResponseEntity<FundingIssueDTO> updateFundingIssue(@Valid @RequestBody FundingIssueDTO fundingIssueDTO) throws URISyntaxException {
		log.debug("REST request to update FundingIssue : {}", fundingIssueDTO);
		if (fundingIssueDTO.getId() == null) {
			return createFundingIssue(fundingIssueDTO);
		}
		FundingIssue fundingIssue = fundingIssueMapper.fundingIssueDTOToFundingIssue(fundingIssueDTO);
		fundingIssue = fundingIssueRepository.save(fundingIssue);
		FundingIssueDTO result = fundingIssueMapper.fundingIssueToFundingIssueDTO(fundingIssue);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fundingIssueDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /funding-issues : get all the fundingIssues.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of fundingIssues in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/funding-issues")
	@Timed
	public ResponseEntity<List<FundingIssueDTO>> getAllFundingIssues(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of FundingIssues");
		Page<FundingIssue> page = fundingIssueRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/funding-issues");
		return new ResponseEntity<>(fundingIssueMapper.fundingIssuesToFundingIssueDTOs(page.getContent()), headers, HttpStatus.OK);
	}

	/**
	 * GET  /funding-issues/:id : get the "id" fundingIssue.
	 *
	 * @param id the id of the fundingIssueDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the fundingIssueDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/funding-issues/{id}")
	@Timed
	public ResponseEntity<FundingIssueDTO> getFundingIssue(@PathVariable Long id) {
		log.debug("REST request to get FundingIssue : {}", id);
		FundingIssue fundingIssue = fundingIssueRepository.findOne(id);
		FundingIssueDTO fundingIssueDTO = fundingIssueMapper.fundingIssueToFundingIssueDTO(fundingIssue);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fundingIssueDTO));
	}

	/**
	 * DELETE  /funding-issues/:id : delete the "id" fundingIssue.
	 *
	 * @param id the id of the fundingIssueDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/funding-issues/{id}")
	@Timed
	public ResponseEntity<Void> deleteFundingIssue(@PathVariable Long id) {
		log.debug("REST request to delete FundingIssue : {}", id);
		fundingIssueRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
