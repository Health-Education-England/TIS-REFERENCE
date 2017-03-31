package com.transformuk.hee.tis.reference.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.domain.MedicalSchool;
import com.transformuk.hee.tis.reference.repository.MedicalSchoolRepository;
import com.transformuk.hee.tis.reference.service.dto.MedicalSchoolDTO;
import com.transformuk.hee.tis.reference.service.mapper.MedicalSchoolMapper;
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
 * REST controller for managing MedicalSchool.
 */
@RestController
@RequestMapping("/api")
public class MedicalSchoolResource {

	private static final String ENTITY_NAME = "medicalSchool";
	private final Logger log = LoggerFactory.getLogger(MedicalSchoolResource.class);
	private final MedicalSchoolRepository medicalSchoolRepository;

	private final MedicalSchoolMapper medicalSchoolMapper;

	public MedicalSchoolResource(MedicalSchoolRepository medicalSchoolRepository, MedicalSchoolMapper medicalSchoolMapper) {
		this.medicalSchoolRepository = medicalSchoolRepository;
		this.medicalSchoolMapper = medicalSchoolMapper;
	}

	/**
	 * POST  /medical-schools : Create a new medicalSchool.
	 *
	 * @param medicalSchoolDTO the medicalSchoolDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new medicalSchoolDTO, or with status 400 (Bad Request) if the medicalSchool has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/medical-schools")
	@Timed
	public ResponseEntity<MedicalSchoolDTO> createMedicalSchool(@Valid @RequestBody MedicalSchoolDTO medicalSchoolDTO) throws URISyntaxException {
		log.debug("REST request to save MedicalSchool : {}", medicalSchoolDTO);
		if (medicalSchoolDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new medicalSchool cannot already have an ID")).body(null);
		}
		MedicalSchool medicalSchool = medicalSchoolMapper.medicalSchoolDTOToMedicalSchool(medicalSchoolDTO);
		medicalSchool = medicalSchoolRepository.save(medicalSchool);
		MedicalSchoolDTO result = medicalSchoolMapper.medicalSchoolToMedicalSchoolDTO(medicalSchool);
		return ResponseEntity.created(new URI("/api/medical-schools/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /medical-schools : Updates an existing medicalSchool.
	 *
	 * @param medicalSchoolDTO the medicalSchoolDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated medicalSchoolDTO,
	 * or with status 400 (Bad Request) if the medicalSchoolDTO is not valid,
	 * or with status 500 (Internal Server Error) if the medicalSchoolDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/medical-schools")
	@Timed
	public ResponseEntity<MedicalSchoolDTO> updateMedicalSchool(@Valid @RequestBody MedicalSchoolDTO medicalSchoolDTO) throws URISyntaxException {
		log.debug("REST request to update MedicalSchool : {}", medicalSchoolDTO);
		if (medicalSchoolDTO.getId() == null) {
			return createMedicalSchool(medicalSchoolDTO);
		}
		MedicalSchool medicalSchool = medicalSchoolMapper.medicalSchoolDTOToMedicalSchool(medicalSchoolDTO);
		medicalSchool = medicalSchoolRepository.save(medicalSchool);
		MedicalSchoolDTO result = medicalSchoolMapper.medicalSchoolToMedicalSchoolDTO(medicalSchool);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, medicalSchoolDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /medical-schools : get all the medicalSchools.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of medicalSchools in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/medical-schools")
	@Timed
	public ResponseEntity<List<MedicalSchoolDTO>> getAllMedicalSchools(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of MedicalSchools");
		Page<MedicalSchool> page = medicalSchoolRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/medical-schools");
		return new ResponseEntity<>(medicalSchoolMapper.medicalSchoolsToMedicalSchoolDTOs(page.getContent()), headers, HttpStatus.OK);
	}

	/**
	 * GET  /medical-schools/:id : get the "id" medicalSchool.
	 *
	 * @param id the id of the medicalSchoolDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the medicalSchoolDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/medical-schools/{id}")
	@Timed
	public ResponseEntity<MedicalSchoolDTO> getMedicalSchool(@PathVariable Long id) {
		log.debug("REST request to get MedicalSchool : {}", id);
		MedicalSchool medicalSchool = medicalSchoolRepository.findOne(id);
		MedicalSchoolDTO medicalSchoolDTO = medicalSchoolMapper.medicalSchoolToMedicalSchoolDTO(medicalSchool);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(medicalSchoolDTO));
	}

	/**
	 * DELETE  /medical-schools/:id : delete the "id" medicalSchool.
	 *
	 * @param id the id of the medicalSchoolDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/medical-schools/{id}")
	@Timed
	public ResponseEntity<Void> deleteMedicalSchool(@PathVariable Long id) {
		log.debug("REST request to delete MedicalSchool : {}", id);
		medicalSchoolRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
