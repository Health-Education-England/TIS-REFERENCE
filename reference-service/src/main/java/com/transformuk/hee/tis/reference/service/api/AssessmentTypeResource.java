package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.api.dto.AssessmentTypeDTO;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.model.AssessmentType;
import com.transformuk.hee.tis.reference.service.repository.AssessmentTypeRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.AssessmentTypeMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing AssessmentType.
 */
@RestController
@RequestMapping("/api")
public class AssessmentTypeResource {

  private static final String ENTITY_NAME = "assessmentType";
  private final Logger log = LoggerFactory.getLogger(AssessmentTypeResource.class);
  private final AssessmentTypeRepository assessmentTypeRepository;

  private final AssessmentTypeMapper assessmentTypeMapper;

  public AssessmentTypeResource(AssessmentTypeRepository assessmentTypeRepository, AssessmentTypeMapper assessmentTypeMapper) {
    this.assessmentTypeRepository = assessmentTypeRepository;
    this.assessmentTypeMapper = assessmentTypeMapper;
  }

  /**
   * POST  /assessment-types : Create a new assessmentType.
   *
   * @param assessmentTypeDTO the assessmentTypeDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new assessmentTypeDTO, or with status 400 (Bad Request) if the assessmentType has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/assessment-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<AssessmentTypeDTO> createAssessmentType(@Valid @RequestBody AssessmentTypeDTO assessmentTypeDTO) throws URISyntaxException {
    log.debug("REST request to save AssessmentType : {}", assessmentTypeDTO);
    if (assessmentTypeDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new assessmentType cannot already have an ID")).body(null);
    }
    AssessmentType assessmentType = assessmentTypeMapper.assessmentTypeDTOToAssessmentType(assessmentTypeDTO);
    assessmentType = assessmentTypeRepository.save(assessmentType);
    AssessmentTypeDTO result = assessmentTypeMapper.assessmentTypeToAssessmentTypeDTO(assessmentType);
    return ResponseEntity.created(new URI("/api/assessment-types/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /assessment-types : Updates an existing assessmentType.
   *
   * @param assessmentTypeDTO the assessmentTypeDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated assessmentTypeDTO,
   * or with status 400 (Bad Request) if the assessmentTypeDTO is not valid,
   * or with status 500 (Internal Server Error) if the assessmentTypeDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/assessment-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<AssessmentTypeDTO> updateAssessmentType(@Valid @RequestBody AssessmentTypeDTO assessmentTypeDTO) throws URISyntaxException {
    log.debug("REST request to update AssessmentType : {}", assessmentTypeDTO);
    if (assessmentTypeDTO.getId() == null) {
      return createAssessmentType(assessmentTypeDTO);
    }
    AssessmentType assessmentType = assessmentTypeMapper.assessmentTypeDTOToAssessmentType(assessmentTypeDTO);
    assessmentType = assessmentTypeRepository.save(assessmentType);
    AssessmentTypeDTO result = assessmentTypeMapper.assessmentTypeToAssessmentTypeDTO(assessmentType);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, assessmentTypeDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /assessment-types : get all the assessmentTypes.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of assessmentTypes in body
   */
  @GetMapping("/assessment-types")
  @Timed
  public List<AssessmentTypeDTO> getAllAssessmentTypes() {
    log.debug("REST request to get all AssessmentTypes");
    List<AssessmentType> assessmentTypes = assessmentTypeRepository.findAll();
    return assessmentTypeMapper.assessmentTypesToAssessmentTypeDTOs(assessmentTypes);
  }

  /**
   * GET  /assessment-types/:id : get the "id" assessmentType.
   *
   * @param id the id of the assessmentTypeDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the assessmentTypeDTO, or with status 404 (Not Found)
   */
  @GetMapping("/assessment-types/{id}")
  @Timed
  public ResponseEntity<AssessmentTypeDTO> getAssessmentType(@PathVariable Long id) {
    log.debug("REST request to get AssessmentType : {}", id);
    AssessmentType assessmentType = assessmentTypeRepository.findOne(id);
    AssessmentTypeDTO assessmentTypeDTO = assessmentTypeMapper.assessmentTypeToAssessmentTypeDTO(assessmentType);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(assessmentTypeDTO));
  }

  /**
   * DELETE  /assessment-types/:id : delete the "id" assessmentType.
   *
   * @param id the id of the assessmentTypeDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/assessment-types/{id}")
  @Timed
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteAssessmentType(@PathVariable Long id) {
    log.debug("REST request to delete AssessmentType : {}", id);
    assessmentTypeRepository.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }


  /**
   * POST  /bulk-assessment-types : Bulk create a new assessment-types.
   *
   * @param assessmentTypeDTOS List of the assessmentTypeDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new assessmentTypeDTOS, or with status 400 (Bad Request) if the AssessmentTypeDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-assessment-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<AssessmentTypeDTO>> bulkCreateAssessmentType(@Valid @RequestBody List<AssessmentTypeDTO> assessmentTypeDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save AssessmentTypes : {}", assessmentTypeDTOS);
    if (!Collections.isEmpty(assessmentTypeDTOS)) {
      List<Long> entityIds = assessmentTypeDTOS.stream()
          .filter(assessmentTypeDTO -> assessmentTypeDTO.getId() != null)
          .map(assessmentTypeDTO -> assessmentTypeDTO.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new assessmentTypes cannot already have an ID")).body(null);
      }
    }
    List<AssessmentType> assessmentTypes = assessmentTypeMapper.assessmentTypeDTOsToAssessmentTypes(assessmentTypeDTOS);
    assessmentTypes = assessmentTypeRepository.save(assessmentTypes);
    List<AssessmentTypeDTO> result = assessmentTypeMapper.assessmentTypesToAssessmentTypeDTOs(assessmentTypes);
    List<Long> ids = result.stream().map(at -> at.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }

  /**
   * PUT  /bulk-assessment-types : Updates an existing AssessmentType.
   *
   * @param assessmentTypeDTOS List of the assessmentTypeDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated assessmentTypeDTOS,
   * or with status 400 (Bad Request) if the assessmentTypeDTOS is not valid,
   * or with status 500 (Internal Server Error) if the assessmentTypeDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-assessment-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<AssessmentTypeDTO>> bulkUpdateAssessmentType(@Valid @RequestBody List<AssessmentTypeDTO> assessmentTypeDTOS) throws URISyntaxException {
    log.info("REST request to bulk update AssessmentTypesDTO : [{}]", assessmentTypeDTOS);
    if (Collections.isEmpty(assessmentTypeDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(assessmentTypeDTOS)) {
      List<AssessmentTypeDTO> entitiesWithNoId = assessmentTypeDTOS.stream().filter(at -> at.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<AssessmentType> assessmentTypes = assessmentTypeMapper.assessmentTypeDTOsToAssessmentTypes(assessmentTypeDTOS);
    assessmentTypes = assessmentTypeRepository.save(assessmentTypes);
    List<AssessmentTypeDTO> results = assessmentTypeMapper.assessmentTypesToAssessmentTypeDTOs(assessmentTypes);
    List<Long> ids = results.stream().map(at -> at.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }
}
