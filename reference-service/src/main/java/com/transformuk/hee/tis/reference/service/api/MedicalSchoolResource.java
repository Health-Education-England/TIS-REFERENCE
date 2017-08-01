package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.api.dto.MedicalSchoolDTO;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.MedicalSchool;
import com.transformuk.hee.tis.reference.service.repository.MedicalSchoolRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.MedicalSchoolMapper;
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
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
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
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
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
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteMedicalSchool(@PathVariable Long id) {
    log.debug("REST request to delete MedicalSchool : {}", id);
    medicalSchoolRepository.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * POST  /bulk-medical-schools : Bulk create a new medical-schools.
   *
   * @param medicalSchoolDTOS List of the medicalSchoolDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new medicalSchoolDTOS, or with status 400 (Bad Request) if the MedicalSchoolDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-medical-schools")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<MedicalSchoolDTO>> bulkCreateMedicalSchool(@Valid @RequestBody List<MedicalSchoolDTO> medicalSchoolDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save MedicalSchools : {}", medicalSchoolDTOS);
    if (!Collections.isEmpty(medicalSchoolDTOS)) {
      List<Long> entityIds = medicalSchoolDTOS.stream()
          .filter(medicalSchoolDTO -> medicalSchoolDTO.getId() != null)
          .map(medicalSchoolDTO -> medicalSchoolDTO.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new medicalSchools cannot already have an ID")).body(null);
      }
    }
    List<MedicalSchool> medicalSchools = medicalSchoolMapper.medicalSchoolDTOsToMedicalSchools(medicalSchoolDTOS);
    medicalSchools = medicalSchoolRepository.save(medicalSchools);
    List<MedicalSchoolDTO> result = medicalSchoolMapper.medicalSchoolsToMedicalSchoolDTOs(medicalSchools);
    List<Long> ids = result.stream().map(medicalSchoolDTO -> medicalSchoolDTO.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }

  /**
   * PUT  /bulk-medical-schools : Updates an existing medical-schools.
   *
   * @param medicalSchoolDTOS List of the medicalSchoolDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated medicalSchoolDTOS,
   * or with status 400 (Bad Request) if the medicalSchoolDTOS is not valid,
   * or with status 500 (Internal Server Error) if the medicalSchoolDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-medical-schools")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<MedicalSchoolDTO>> bulkUpdateMedicalSchool(@Valid @RequestBody List<MedicalSchoolDTO> medicalSchoolDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update MedicalSchools : {}", medicalSchoolDTOS);
    if (Collections.isEmpty(medicalSchoolDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(medicalSchoolDTOS)) {
      List<MedicalSchoolDTO> entitiesWithNoId = medicalSchoolDTOS.stream().filter(medicalSchoolDTO -> medicalSchoolDTO.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<MedicalSchool> medicalSchools = medicalSchoolMapper.medicalSchoolDTOsToMedicalSchools(medicalSchoolDTOS);
    medicalSchools = medicalSchoolRepository.save(medicalSchools);
    List<MedicalSchoolDTO> results = medicalSchoolMapper.medicalSchoolsToMedicalSchoolDTOs(medicalSchools);
    List<Long> ids = results.stream().map(medicalSchoolDTO -> medicalSchoolDTO.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }
}
