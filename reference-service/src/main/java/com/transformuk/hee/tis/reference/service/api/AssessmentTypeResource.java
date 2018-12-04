package com.transformuk.hee.tis.reference.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.validation.Create;
import com.transformuk.hee.tis.reference.api.dto.validation.Update;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.AssessmentType;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.repository.AssessmentTypeRepository;
import com.transformuk.hee.tis.reference.service.service.impl.AssessmentTypeServiceImpl;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static com.transformuk.hee.tis.reference.service.api.util.StringUtil.sanitize;

/**
 * REST controller for managing Assessment Types.
 */
@RestController
@RequestMapping("/api")

public class AssessmentTypeResource {

  private static final String ENTITY_NAME = "AssessmentType";
  private final Logger log = LoggerFactory.getLogger(AssessmentTypeResource.class);

  private final AssessmentTypeRepository assessmentTypeRepository;
  private final AssessmentTypeServiceImpl assessmentTypeService;

  public AssessmentTypeResource(AssessmentTypeRepository assessmentTypeRepository,
                                AssessmentTypeServiceImpl assessmentTypeService) {
    this.assessmentTypeRepository = assessmentTypeRepository;
    this.assessmentTypeService = assessmentTypeService;
  }

  /**
   * POST  /assessment-types : Create a new Assessment type.
   *
   * @param assessmentType the Assessment type to create
   * @return the ResponseEntity with status 201 (Created) and with body the new AssessmentType, or with status 400 (Bad Request) if the AssessmentType has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/assessment-types")
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
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<AssessmentType> updateAssessmentType(@Validated(Update.class) @RequestBody AssessmentType assessmentType) throws URISyntaxException {
    log.debug("REST request to update AssessmentType : {}", assessmentType);

      if (!assessmentTypeRepository.findById(assessmentType.getCode()).isPresent()) {
      return createAssessmentType(assessmentType);
    }

    AssessmentType result = assessmentTypeRepository.save(assessmentType);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, assessmentType.getCode()))
        .body(result);
  }

  /**
   * GET  /assessment-types : get all assessment types.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of assessment types in body
   */
  @GetMapping("/assessment-types")
  public ResponseEntity<List<AssessmentType>> getAllAssessmentTypes(
          Pageable pageable,
          @RequestParam(value = "searchQuery", required = false) String searchQuery,
          @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.info("REST request to get a page of assessment types begin");
    searchQuery = sanitize(searchQuery);
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    Page<AssessmentType> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = assessmentTypeRepository.findAll(pageable);
    } else {
      page = assessmentTypeService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/assessment-type");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /assessment-types/:code : get the Assessment Type based on the code.
   *
   * @param code the code of the AssessmentType to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the AssessmentType, or with status 404 (Not Found)
   */
  @GetMapping("/assessment-types/{code}")
  public ResponseEntity<AssessmentType> getAssessmentTypeByCode(@PathVariable String code) {
    log.debug("REST request to get Assessment Type : {}", code);
      AssessmentType result = assessmentTypeRepository.findById(code).orElse(null);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
  }


}
