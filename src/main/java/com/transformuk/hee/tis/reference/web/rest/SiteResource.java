package com.transformuk.hee.tis.reference.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.domain.Site;
import com.transformuk.hee.tis.reference.repository.SiteRepository;
import com.transformuk.hee.tis.reference.service.dto.SiteDTO;
import com.transformuk.hee.tis.reference.service.mapper.SiteMapper;
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
 * REST controller for managing Site.
 */
@RestController
@RequestMapping("/api")
public class SiteResource {

	private static final String ENTITY_NAME = "site";
	private final Logger log = LoggerFactory.getLogger(SiteResource.class);
	private final SiteRepository siteRepository;

	private final SiteMapper siteMapper;

	public SiteResource(SiteRepository siteRepository, SiteMapper siteMapper) {
		this.siteRepository = siteRepository;
		this.siteMapper = siteMapper;
	}

	/**
	 * POST  /sites : Create a new site.
	 *
	 * @param siteDTO the siteDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new siteDTO, or with status 400 (Bad Request) if the site has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/sites")
	@Timed
	public ResponseEntity<SiteDTO> createSite(@Valid @RequestBody SiteDTO siteDTO) throws URISyntaxException {
		log.debug("REST request to save Site : {}", siteDTO);
		if (siteDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new site cannot already have an ID")).body(null);
		}
		Site site = siteMapper.siteDTOToSite(siteDTO);
		site = siteRepository.save(site);
		SiteDTO result = siteMapper.siteToSiteDTO(site);
		return ResponseEntity.created(new URI("/api/sites/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /sites : Updates an existing site.
	 *
	 * @param siteDTO the siteDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated siteDTO,
	 * or with status 400 (Bad Request) if the siteDTO is not valid,
	 * or with status 500 (Internal Server Error) if the siteDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/sites")
	@Timed
	public ResponseEntity<SiteDTO> updateSite(@Valid @RequestBody SiteDTO siteDTO) throws URISyntaxException {
		log.debug("REST request to update Site : {}", siteDTO);
		if (siteDTO.getId() == null) {
			return createSite(siteDTO);
		}
		Site site = siteMapper.siteDTOToSite(siteDTO);
		site = siteRepository.save(site);
		SiteDTO result = siteMapper.siteToSiteDTO(site);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, siteDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /sites : get all the sites.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of sites in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/sites")
	@Timed
	public ResponseEntity<List<SiteDTO>> getAllSites(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of Sites");
		Page<Site> page = siteRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sites");
		return new ResponseEntity<>(siteMapper.sitesToSiteDTOs(page.getContent()), headers, HttpStatus.OK);
	}

	/**
	 * GET  /sites/:id : get the "id" site.
	 *
	 * @param id the id of the siteDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the siteDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/sites/{id}")
	@Timed
	public ResponseEntity<SiteDTO> getSite(@PathVariable Long id) {
		log.debug("REST request to get Site : {}", id);
		Site site = siteRepository.findOne(id);
		SiteDTO siteDTO = siteMapper.siteToSiteDTO(site);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(siteDTO));
	}

	/**
	 * DELETE  /sites/:id : delete the "id" site.
	 *
	 * @param id the id of the siteDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/sites/{id}")
	@Timed
	public ResponseEntity<Void> deleteSite(@PathVariable Long id) {
		log.debug("REST request to delete Site : {}", id);
		siteRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
