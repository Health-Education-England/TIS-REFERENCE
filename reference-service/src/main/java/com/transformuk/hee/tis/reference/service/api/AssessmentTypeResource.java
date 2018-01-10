package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.api.dto.validation.Create;
import com.transformuk.hee.tis.reference.api.dto.validation.Update;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.AssessmentType;
import com.transformuk.hee.tis.reference.service.repository.AssessmentTypeRepository;
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
import org.springframework.validation.annotation.Validated;
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

/**
 * REST controller for managing Assessment Types.
 */
@RestController
@RequestMapping("/api")

public class AssessmentTypeResource {

  private static final String ENTITY_NAME = "AssessmentType";
  private final Logger log = LoggerFactory.getLogger(AssessmentTypeResource.class);

  private final AssessmentTypeRepository assessmentTypeRepository;

  public AssessmentTypeResource(AssessmentTypeRepository assessmentTypeRepository) {
    this.assessmentTypeRepository = assessmentTypeRepository;
  }

  /**
   * POST  /assessment-types : Create a new Assessment type.
   *
   * @param assessmentType the Assessment type to create
   * @return the ResponseEntity with status 201 (Created) and with body the new AssessmentType, or with status 400 (Bad Request) if the AssessmentType has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/assessment-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<AssessmentType> createAssessmentType(@Validated(Create.class) @RequestBody AssessmentType assessmentType) throws URISyntaxException {
    log.debug("REST request to save AssessmentType : {}", assessmentType);
    if (assessmentType.getCode() == null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "Assessment type must have an ID")).body(null);
    }
    AssessmentType newAssessmentType = assessmentTypeRepository.save(assessmentType);
    return ResponseEntity.created(new URI("/api/assessment-types/" + newAssessmentType.getCode()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, newAssessmentType.getCode()))
        .body(newAssessmentType);
  }

  /**
   * PUT  /assessment-types : Updates an existing Assessment Type.
   *
   * @param assessmentType the assessmentType to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated assessmentType,
   * or with status 400 (Bad Request) if the assessmentType is not valid,
   * or with status 500 (Internal Server Error) if the assessmentType couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/assessment-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<AssessmentType> updateAssessmentType(@Validated(Update.class) @RequestBody AssessmentType assessmentType) throws URISyntaxException {
    log.debug("REST request to update AssessmentType : {}", assessmentType);

    if(assessmentTypeRepository.findOne(assessmentType.getCode()) == null){
      return createAssessmentType(assessmentType);
    }

    AssessmentType result = assessmentTypeRepository.save(assessmentType);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, assessmentType.getCode()))
        .body(result);
  }

  /**
   * GET  /assessment-types : get all the Assessment Types.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of AssessmentType in body
   * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
   */
  @GetMapping("/assessment-types")
  @Timed
  public ResponseEntity<List<AssessmentType>> getAllAssessmentTypes(@ApiParam Pageable pageable) {
    log.debug("REST request to get a page of Assessment Type");
    Page<AssessmentType> page = assessmentTypeRepository.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/assessment-types");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /assessment-types/:code : get the Assessment Type based on the code.
   *
   * @param code the code of the AssessmentType to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the AssessmentType, or with status 404 (Not Found)
   */
  @GetMapping("/assessment-types/{code}")
  @Timed
  public ResponseEntity<AssessmentType> getAssessmentTypeByCode(@PathVariable String code) {
    log.debug("REST request to get Assessment Type : {}", code);
    AssessmentType result = assessmentTypeRepository.findOne(code);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
  }


}
