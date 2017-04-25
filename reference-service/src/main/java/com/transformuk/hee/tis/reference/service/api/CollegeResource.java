package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.service.model.College;
import com.transformuk.hee.tis.reference.service.repository.CollegeRepository;
import com.transformuk.hee.tis.reference.api.dto.CollegeDTO;
import com.transformuk.hee.tis.reference.service.service.mapper.CollegeMapper;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
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

/**
 * REST controller for managing College.
 */
@RestController
@RequestMapping("/api")
public class CollegeResource {

	private static final String ENTITY_NAME = "college";
	private final Logger log = LoggerFactory.getLogger(CollegeResource.class);
	private final CollegeRepository collegeRepository;

	private final CollegeMapper collegeMapper;

	public CollegeResource(CollegeRepository collegeRepository, CollegeMapper collegeMapper) {
		this.collegeRepository = collegeRepository;
		this.collegeMapper = collegeMapper;
	}

	/**
	 * POST  /colleges : Create a new college.
	 *
	 * @param collegeDTO the collegeDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new collegeDTO, or with status 400 (Bad Request) if the college has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/colleges")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<CollegeDTO> createCollege(@Valid @RequestBody CollegeDTO collegeDTO) throws URISyntaxException {
		log.debug("REST request to save College : {}", collegeDTO);
		if (collegeDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new college cannot already have an ID")).body(null);
		}
		College college = collegeMapper.collegeDTOToCollege(collegeDTO);
		college = collegeRepository.save(college);
		CollegeDTO result = collegeMapper.collegeToCollegeDTO(college);
		return ResponseEntity.created(new URI("/api/colleges/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /colleges : Updates an existing college.
	 *
	 * @param collegeDTO the collegeDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated collegeDTO,
	 * or with status 400 (Bad Request) if the collegeDTO is not valid,
	 * or with status 500 (Internal Server Error) if the collegeDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/colleges")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<CollegeDTO> updateCollege(@Valid @RequestBody CollegeDTO collegeDTO) throws URISyntaxException {
		log.debug("REST request to update College : {}", collegeDTO);
		if (collegeDTO.getId() == null) {
			return createCollege(collegeDTO);
		}
		College college = collegeMapper.collegeDTOToCollege(collegeDTO);
		college = collegeRepository.save(college);
		CollegeDTO result = collegeMapper.collegeToCollegeDTO(college);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, collegeDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /colleges : get all the colleges.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of colleges in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/colleges")
	@Timed
	public ResponseEntity<List<CollegeDTO>> getAllColleges(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of Colleges");
		Page<College> page = collegeRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/colleges");
		return new ResponseEntity<>(collegeMapper.collegesToCollegeDTOs(page.getContent()), headers, HttpStatus.OK);
	}

	/**
	 * GET  /colleges/:id : get the "id" college.
	 *
	 * @param id the id of the collegeDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the collegeDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/colleges/{id}")
	@Timed
	public ResponseEntity<CollegeDTO> getCollege(@PathVariable Long id) {
		log.debug("REST request to get College : {}", id);
		College college = collegeRepository.findOne(id);
		CollegeDTO collegeDTO = collegeMapper.collegeToCollegeDTO(college);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(collegeDTO));
	}

	/**
	 * DELETE  /colleges/:id : delete the "id" college.
	 *
	 * @param id the id of the collegeDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/colleges/{id}")
	@Timed
	@PreAuthorize("hasAuthority('reference:delete:entities')")
	public ResponseEntity<Void> deleteCollege(@PathVariable Long id) {
		log.debug("REST request to delete College : {}", id);
		collegeRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
