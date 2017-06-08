package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.api.dto.LocalOfficeDTO;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.LocalOffice;
import com.transformuk.hee.tis.reference.service.repository.LocalOfficeRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.LocalOfficeMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Collectors;

/**
 * REST controller for managing LocalOffice.
 */
@RestController
@RequestMapping("/api")
public class LocalOfficeResource {

	private static final String ENTITY_NAME = "localOffice";
	private final Logger log = LoggerFactory.getLogger(LocalOfficeResource.class);
	private final LocalOfficeRepository localOfficeRepository;

	private final LocalOfficeMapper localOfficeMapper;

	public LocalOfficeResource(LocalOfficeRepository localOfficeRepository, LocalOfficeMapper localOfficeMapper) {
		this.localOfficeRepository = localOfficeRepository;
		this.localOfficeMapper = localOfficeMapper;
	}

	/**
	 * POST  /local-offices : Create a new localOffice.
	 *
	 * @param localOfficeDTO the localOfficeDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new localOfficeDTO, or with status 400 (Bad Request) if the localOffice has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/local-offices")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<LocalOfficeDTO> createLocalOffice(@Valid @RequestBody LocalOfficeDTO localOfficeDTO) throws URISyntaxException {
		log.debug("REST request to save LocalOffice : {}", localOfficeDTO);
		if (localOfficeDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new localOffice cannot already have an ID")).body(null);
		}
		LocalOffice localOffice = localOfficeMapper.localOfficeDTOToLocalOffice(localOfficeDTO);
		localOffice = localOfficeRepository.save(localOffice);
		LocalOfficeDTO result = localOfficeMapper.localOfficeToLocalOfficeDTO(localOffice);
		return ResponseEntity.created(new URI("/api/local-offices/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /local-offices : Updates an existing localOffice.
	 *
	 * @param localOfficeDTO the localOfficeDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated localOfficeDTO,
	 * or with status 400 (Bad Request) if the localOfficeDTO is not valid,
	 * or with status 500 (Internal Server Error) if the localOfficeDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/local-offices")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<LocalOfficeDTO> updateLocalOffice(@Valid @RequestBody LocalOfficeDTO localOfficeDTO) throws URISyntaxException {
		log.debug("REST request to update LocalOffice : {}", localOfficeDTO);
		if (localOfficeDTO.getId() == null) {
			return createLocalOffice(localOfficeDTO);
		}
		LocalOffice localOffice = localOfficeMapper.localOfficeDTOToLocalOffice(localOfficeDTO);
		localOffice = localOfficeRepository.save(localOffice);
		LocalOfficeDTO result = localOfficeMapper.localOfficeToLocalOfficeDTO(localOffice);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, localOfficeDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /local-offices : get all the localOffices.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of localOffices in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/local-offices")
	@Timed
	public ResponseEntity<List<LocalOfficeDTO>> getAllLocalOffices(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of LocalOffices");
		Page<LocalOffice> page = localOfficeRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/local-offices");
		return new ResponseEntity<>(localOfficeMapper.localOfficesToLocalOfficeDTOs(page.getContent()), headers, HttpStatus.OK);
	}

	/**
	 * GET  /local-offices/:id : get the "id" localOffice.
	 *
	 * @param id the id of the localOfficeDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the localOfficeDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/local-offices/{id}")
	@Timed
	public ResponseEntity<LocalOfficeDTO> getLocalOffice(@PathVariable Long id) {
		log.debug("REST request to get LocalOffice : {}", id);
		LocalOffice localOffice = localOfficeRepository.findOne(id);
		LocalOfficeDTO localOfficeDTO = localOfficeMapper.localOfficeToLocalOfficeDTO(localOffice);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(localOfficeDTO));
	}

	/**
	 * DELETE  /local-offices/:id : delete the "id" localOffice.
	 *
	 * @param id the id of the localOfficeDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/local-offices/{id}")
	@Timed
	@PreAuthorize("hasAuthority('reference:delete:entities')")
	public ResponseEntity<Void> deleteLocalOffice(@PathVariable Long id) {
		log.debug("REST request to delete LocalOffice : {}", id);
		localOfficeRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * POST  /bulk-local-offices : Bulk create a new local-offices.
	 *
	 * @param localOfficeDTOS List of the localOfficeDTOS to create
	 * @return the ResponseEntity with status 200 (Created) and with body the new localOfficeDTOS, or with status 400 (Bad Request) if the LocalOfficeDTO has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/bulk-local-offices")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<List<LocalOfficeDTO>> bulkCreateLocalOffice(@Valid @RequestBody List<LocalOfficeDTO> localOfficeDTOS) throws URISyntaxException {
		log.debug("REST request to bulk save LocalOfficeDtos : {}", localOfficeDTOS);
		if (!Collections.isEmpty(localOfficeDTOS)) {
			List<Long> entityIds = localOfficeDTOS.stream()
					.filter(localOfficeDTO -> localOfficeDTO.getId() != null)
					.map(localOfficeDTO -> localOfficeDTO.getId())
					.collect(Collectors.toList());
			if (!Collections.isEmpty(entityIds)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new localOffices cannot already have an ID")).body(null);
			}
		}
		List<LocalOffice> localOffices = localOfficeMapper.localOfficeDTOsToLocalOffices(localOfficeDTOS);
		localOffices = localOfficeRepository.save(localOffices);
		List<LocalOfficeDTO> result = localOfficeMapper.localOfficesToLocalOfficeDTOs(localOffices);
		List<Long> ids = result.stream().map(localOfficeDTO -> localOfficeDTO.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(result);
	}

	/**
	 * PUT  /bulk-local-offices : Updates an existing local-offices.
	 *
	 * @param localOfficeDTOS List of the localOfficeDTOS to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated localOfficeDTOS,
	 * or with status 400 (Bad Request) if the localOfficeDTOS is not valid,
	 * or with status 500 (Internal Server Error) if the localOfficeDTOS couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/bulk-local-offices")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<List<LocalOfficeDTO>> bulkUpdateLocalOffice(@Valid @RequestBody List<LocalOfficeDTO> localOfficeDTOS) throws URISyntaxException {
		log.debug("REST request to bulk update LocalOfficeDtos : {}", localOfficeDTOS);
		if (Collections.isEmpty(localOfficeDTOS)) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
					"The request body for this end point cannot be empty")).body(null);
		} else if (!Collections.isEmpty(localOfficeDTOS)) {
			List<LocalOfficeDTO> entitiesWithNoId = localOfficeDTOS.stream().filter(at -> at.getId() == null).collect(Collectors.toList());
			if (!Collections.isEmpty(entitiesWithNoId)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
						"bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
			}
		}
		List<LocalOffice> localOffices = localOfficeMapper.localOfficeDTOsToLocalOffices(localOfficeDTOS);
		localOffices = localOfficeRepository.save(localOffices);
		List<LocalOfficeDTO> results = localOfficeMapper.localOfficesToLocalOfficeDTOs(localOffices);
		List<Long> ids = results.stream().map(localOfficeDTO -> localOfficeDTO.getId()).collect(Collectors.toList());
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(results);
	}
}
