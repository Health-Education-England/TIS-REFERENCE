package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.api.dto.CollegeDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.College;
import com.transformuk.hee.tis.reference.service.repository.CollegeRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.CollegeMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
   * GET  /current/colleges : get all current colleges.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of colleges in body
   * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
   */
  @GetMapping("/current/olleges")
  @Timed
  public ResponseEntity<List<CollegeDTO>> getAllCurrentColleges(@ApiParam Pageable pageable) {
    log.debug("REST request to get a page of current Colleges");
    College college = new College();
    college.setStatus(Status.CURRENT);
    Page<College> page = collegeRepository.findAll(Example.of(college), pageable);
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

  /**
   * POST  /bulk-colleges : Bulk create a new colleges.
   *
   * @param collegeDTOS List of the collegeDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new collegeDTOS, or with status 400 (Bad Request) if the CollegeDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-colleges")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<CollegeDTO>> bulkCreateCollege(@Valid @RequestBody List<CollegeDTO> collegeDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save CollegeDTOs : {}", collegeDTOS);
    if (!Collections.isEmpty(collegeDTOS)) {
      List<Long> entityIds = collegeDTOS.stream()
          .filter(collegeDTO -> collegeDTO.getId() != null)
          .map(collegeDTO -> collegeDTO.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new colleges cannot already have an ID")).body(null);
      }
    }
    List<College> colleges = collegeMapper.collegeDTOsToColleges(collegeDTOS);
    colleges = collegeRepository.save(colleges);
    List<CollegeDTO> result = collegeMapper.collegesToCollegeDTOs(colleges);
    List<Long> ids = result.stream().map(c -> c.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }

  /**
   * PUT  /bulk-colleges : Updates an existing curriculumSubType.
   *
   * @param collegeDTOS List of the collegeDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated collegeDTOS,
   * or with status 400 (Bad Request) if the collegeDTOS is not valid,
   * or with status 500 (Internal Server Error) if the collegeDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-colleges")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<CollegeDTO>> bulkUpdateCollege(@Valid @RequestBody List<CollegeDTO> collegeDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update CollegeDTO : {}", collegeDTOS);
    if (Collections.isEmpty(collegeDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(collegeDTOS)) {
      List<CollegeDTO> entitiesWithNoId = collegeDTOS.stream().filter(c -> c.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<College> colleges = collegeMapper.collegeDTOsToColleges(collegeDTOS);
    colleges = collegeRepository.save(colleges);
    List<CollegeDTO> results = collegeMapper.collegesToCollegeDTOs(colleges);
    List<Long> ids = results.stream().map(c -> c.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }

}
