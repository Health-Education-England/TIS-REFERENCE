package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.api.dto.CurriculumSubTypeDTO;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.model.Grade;
import com.transformuk.hee.tis.reference.service.repository.GradeRepository;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.service.service.mapper.GradeMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Grade.
 */
@RestController
@RequestMapping("/api")
public class GradeResource {

	private static final String ENTITY_NAME = "grade";
	private final Logger log = LoggerFactory.getLogger(GradeResource.class);
	private final GradeRepository gradeRepository;

	private final GradeMapper gradeMapper;

	public GradeResource(GradeRepository gradeRepository, GradeMapper gradeMapper) {
		this.gradeRepository = gradeRepository;
		this.gradeMapper = gradeMapper;
	}

	/**
	 * POST  /grades : Create a new grade.
	 *
	 * @param gradeDTO the gradeDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new gradeDTO, or with status 400 (Bad Request) if the grade has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/grades")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<GradeDTO> createGrade(@Valid @RequestBody GradeDTO gradeDTO) throws URISyntaxException {
		log.debug("REST request to save Grade : {}", gradeDTO);
		if (gradeDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new grade cannot already have an ID")).body(null);
		}
		Grade grade = gradeMapper.gradeDTOToGrade(gradeDTO);
		grade = gradeRepository.save(grade);
		GradeDTO result = gradeMapper.gradeToGradeDTO(grade);
		return ResponseEntity.created(new URI("/api/grades/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /grades : Updates an existing grade.
	 *
	 * @param gradeDTO the gradeDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated gradeDTO,
	 * or with status 400 (Bad Request) if the gradeDTO is not valid,
	 * or with status 500 (Internal Server Error) if the gradeDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/grades")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<GradeDTO> updateGrade(@Valid @RequestBody GradeDTO gradeDTO) throws URISyntaxException {
		log.debug("REST request to update Grade : {}", gradeDTO);
		if (gradeDTO.getId() == null) {
			return createGrade(gradeDTO);
		}
		Grade grade = gradeMapper.gradeDTOToGrade(gradeDTO);
		grade = gradeRepository.save(grade);
		GradeDTO result = gradeMapper.gradeToGradeDTO(grade);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gradeDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /grades : get all the grades.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of grades in body
	 */
	@GetMapping("/grades")
	@Timed
	public List<GradeDTO> getAllGrades() {
		log.debug("REST request to get all Grades");
		List<Grade> grades = gradeRepository.findAll();
		return gradeMapper.gradesToGradeDTOs(grades);
	}

	/**
	 * GET  /grades/:id : get the "id" grade.
	 *
	 * @param id the id of the gradeDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the gradeDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/grades/{id}")
	@Timed
	public ResponseEntity<GradeDTO> getGrade(@PathVariable Long id) {
		log.debug("REST request to get Grade : {}", id);
		Grade grade = gradeRepository.findOne(id);
		GradeDTO gradeDTO = gradeMapper.gradeToGradeDTO(grade);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gradeDTO));
	}

	/**
	 * GET  /grades/code/:code : get the "code" grade.
	 *
	 * @param code the code of the gradeDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the gradeDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/grades/code/{code}")
	@Timed
	public ResponseEntity<GradeDTO> getGradeByCode(@PathVariable String code) {
		log.debug("REST request to get Grade by code: {}", code);
		Grade grade = gradeRepository.findByAbbreviation(code);
		GradeDTO gradeDTO = gradeMapper.gradeToGradeDTO(grade);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gradeDTO));
	}

	/**
	 * DELETE  /grades/:id : delete the "id" grade.
	 *
	 * @param id the id of the gradeDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/grades/{id}")
	@Timed
	@PreAuthorize("hasAuthority('reference:delete:entities')")
	public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
		log.debug("REST request to delete Grade : {}", id);
		gradeRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}


	/**
	 * POST  /bulk-grades : Bulk create a new grade.
	 *
	 * @param gradeDTOs the gradeDTOs to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new gradeDTOs, or with status 400 (Bad Request) if the grade has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/bulk-grades")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<List<GradeDTO>> bulkCreateGrade(@Valid @RequestBody List<GradeDTO> gradeDTOs) throws URISyntaxException {
		log.debug("REST request to bulk save Grade : {}", gradeDTOs);
		if (!Collections.isEmpty(gradeDTOs)) {
			List<Long> entityIds = gradeDTOs.stream().map(grade -> grade.getId()).collect(Collectors.toList());
			if(!Collections.isEmpty(entityIds)) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new grades cannot already have an ID")).body(null);
			}
		}
		List<Grade> grades = gradeMapper.gradeDTOsToGrades(gradeDTOs);
		grades = gradeRepository.save(grades);
		List<GradeDTO> results = gradeMapper.gradesToGradeDTOs(grades);
		List<Long> ids = results.stream().map(grade -> grade.getId()).collect(Collectors.toList());

		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(results);
	}

	/**
	 * PUT  /grades : Bulk updates an existing grade.
	 *
	 * @param gradeDTOs the gradeDTOs to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated gradeDTOs,
	 * or with status 400 (Bad Request) if the gradeDTOs is not valid,
	 * or with status 500 (Internal Server Error) if the gradeDTOs couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/bulk-grades")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<List<GradeDTO>> bulkUpdateGrade(@Valid @RequestBody List<GradeDTO> gradeDTOs) throws URISyntaxException {
		log.debug("REST request to bulk update Grade : {}", gradeDTOs);
		if(Collections.isEmpty(gradeDTOs)){
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
					"The request body for this end point cannot be empty")).body(null);
		} else if (!Collections.isEmpty(gradeDTOs)) {
			List<GradeDTO> entitiesWithNoId = gradeDTOs.stream().filter(grades -> grades.getId() == null).collect(Collectors.toList());
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
					"bulk.update.failed.noId","The request body for this end point cannot be empty")).body(null);
		}


		List<Grade> grades = gradeMapper.gradeDTOsToGrades(gradeDTOs);
		grades = gradeRepository.save(grades);
		List<GradeDTO> results = gradeMapper.gradesToGradeDTOs(grades);
		List<Long> ids = results.stream().map(grade -> grade.getId()).collect(Collectors.toList());

		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
				.body(results);
	}

}
