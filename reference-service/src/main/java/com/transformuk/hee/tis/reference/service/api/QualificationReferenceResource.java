package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.QualificationReferenceDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.QualificationReference;
import com.transformuk.hee.tis.reference.service.repository.QualificationReferenceRepository;
import com.transformuk.hee.tis.reference.service.service.impl.QualificationReferenceServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.QualificationReferenceMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.transformuk.hee.tis.reference.service.api.util.StringUtil.sanitize;

/**
 * REST controller for managing QualificationReference.
 */
@RestController
@RequestMapping("/api")
public class QualificationReferenceResource {

  private static final String ENTITY_NAME = "qualificationReference";
  private final Logger log = LoggerFactory.getLogger(QualificationReferenceResource.class);

  private final QualificationReferenceRepository qualificationReferenceRepository;
  private final QualificationReferenceMapper qualificationReferenceMapper;
  private final QualificationReferenceServiceImpl qualificationReferenceService;

  public QualificationReferenceResource(QualificationReferenceRepository qualificationReferenceRepository,
                                        QualificationReferenceMapper qualificationReferenceMapper,
                                        QualificationReferenceServiceImpl qualificationReferenceService) {
    this.qualificationReferenceRepository = qualificationReferenceRepository;
    this.qualificationReferenceMapper = qualificationReferenceMapper;
    this.qualificationReferenceService = qualificationReferenceService;
  }

  /**
   * POST  /qualification-reference : Create a new qualificationReference.
   *
   * @param qualificationReferenceDTO the qualificationReferenceDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new qualificationReferenceDTO, or with status 400 (Bad Request) if the qualificationReference has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/qualification-reference")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<QualificationReferenceDTO> createQualificationReference(@Valid @RequestBody QualificationReferenceDTO qualificationReferenceDTO) throws URISyntaxException {
    log.debug("REST request to save QualificationReference : {}", qualificationReferenceDTO);
    if (qualificationReferenceDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new qualificationReference cannot already have an ID")).body(null);
    }
    QualificationReference qualificationReference = qualificationReferenceMapper.qualificationReferenceDTOToQualificationReference(qualificationReferenceDTO);
    qualificationReference = qualificationReferenceRepository.save(qualificationReference);
    QualificationReferenceDTO result = qualificationReferenceMapper.qualificationReferenceToQualificationReferenceDTO(qualificationReference);
    return ResponseEntity.created(new URI("/api/qualification-reference/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
  }

  /**
   * PUT  /qualification-reference : Updates an existing qualificationReferenceDTO.
   *
   * @param qualificationReferenceDTO the qualificationReferenceDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated qualificationReferenceDTO,
   * or with status 400 (Bad Request) if the qualificationReferenceDTO is not valid,
   * or with status 500 (Internal Server Error) if the qualificationReferenceDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/qualification-reference")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<QualificationReferenceDTO> updateQualificationReference(@Valid @RequestBody QualificationReferenceDTO qualificationReferenceDTO) throws URISyntaxException {
    log.debug("REST request to update QualificationReference : {}", qualificationReferenceDTO);
    if (qualificationReferenceDTO.getId() == null) {
      return createQualificationReference(qualificationReferenceDTO);
    }
    QualificationReference qualificationReference = qualificationReferenceMapper.qualificationReferenceDTOToQualificationReference(qualificationReferenceDTO);
    qualificationReference = qualificationReferenceRepository.save(qualificationReference);
    QualificationReferenceDTO result = qualificationReferenceMapper.qualificationReferenceToQualificationReferenceDTO(qualificationReference);
    return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, qualificationReferenceDTO.getId().toString()))
            .body(result);
  }


  /**
   * GET  /qualification-reference : get all qualificationReference.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of qualification reference in body
   */
  @ApiOperation(value = "Lists qualification reference",
          notes = "Returns a list of qualification reference with support for pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Qualification Reference list")})
  @GetMapping("/qualification-reference")
  @Timed
  public ResponseEntity<List<QualificationReferenceDTO>> getAllQualificationReferences(
          @ApiParam Pageable pageable,
          @ApiParam(value = "any wildcard string to be searched")
          @RequestParam(value = "searchQuery", required = false) String searchQuery,
          @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
          @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.info("REST request to get a page of Qualification Reference begin");
    searchQuery = sanitize(searchQuery);
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    Page<QualificationReference> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = qualificationReferenceRepository.findAll(pageable);
    } else {
      page = qualificationReferenceService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<QualificationReferenceDTO> results = page.map(qualificationReferenceMapper::qualificationReferenceToQualificationReferenceDTO);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/qualification-reference");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /qualification-reference/:id : get the "id" qualificationReference.
   *
   * @param id the id of the qualificationReferenceDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the qualificationReferenceDTO, or with status 404 (Not Found)
   */
  @GetMapping("/qualification-reference/{id}")
  @Timed
  public ResponseEntity<QualificationReferenceDTO> getQualificationReference(@PathVariable Long id) {
    log.debug("REST request to get qualificationReference : {}", id);
    QualificationReference qualificationReference = qualificationReferenceRepository.findOne(id);
    QualificationReferenceDTO qualificationReferenceDTO = qualificationReferenceMapper.qualificationReferenceToQualificationReferenceDTO(qualificationReference);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(qualificationReferenceDTO));
  }

  /**
   * DELETE  /qualification-reference/:id : delete the "id" qualificationReference.
   *
   * @param id the id of the qualificationReferenceDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/qualification-reference/{id}")
  @Timed
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteQualificationReference(@PathVariable Long id) {
    log.debug("REST request to delete qualificationReference : {}", id);
    qualificationReferenceRepository.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * POST  /bulk-qualification-reference : Bulk create a new qualification-reference.
   *
   * @param qualificationReferenceDTOs List of the qualificationReferenceDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new qualificationReferenceDTOS, or with status 400 (Bad Request) if the qualificationReferenceDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-qualification-reference")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<QualificationReferenceDTO>> bulkCreateQualificationReference(@Valid @RequestBody List<QualificationReferenceDTO> qualificationReferenceDTOs) throws URISyntaxException {
    log.debug("REST request to bulk save qualificationReference : {}", qualificationReferenceDTOs);
    if (!Collections.isEmpty(qualificationReferenceDTOs)) {
      List<Long> entityIds = qualificationReferenceDTOs.stream()
              .filter(qualificationReferenceDTO -> qualificationReferenceDTO.getId() != null)
              .map(qualificationReferenceDTO -> qualificationReferenceDTO.getId())
              .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new qualificationReference cannot already have an ID")).body(null);
      }
    }
    List<QualificationReference> qualificationReferences = qualificationReferenceMapper.qualificationReferenceDTOsToQualificationReferences(qualificationReferenceDTOs);
    qualificationReferences = qualificationReferenceRepository.save(qualificationReferences);
    List<QualificationReferenceDTO> result = qualificationReferenceMapper.qualificationReferencesToQualificationReferenceDTOs(qualificationReferences);
    return ResponseEntity.ok()
            .body(result);
  }

  /**
   * PUT  /bulk-qualification-reference : Updates an existing qualification-reference.
   *
   * @param qualificationReferenceDTOS List of the qualificationReferenceDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated qualificationReferenceDTOS,
   * or with status 400 (Bad Request) if the qualificationReferenceDTOS is not valid,
   * or with status 500 (Internal Server Error) if the qualificationReferenceDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-qualification-reference")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<QualificationReferenceDTO>> bulkUpdateQualificationReference(@Valid @RequestBody List<QualificationReferenceDTO> qualificationReferenceDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update qualificationReference : {}", qualificationReferenceDTOS);
    if (Collections.isEmpty(qualificationReferenceDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
              "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(qualificationReferenceDTOS)) {
      List<QualificationReferenceDTO> entitiesWithNoId = qualificationReferenceDTOS.stream().filter(qualificationReferenceDTO -> qualificationReferenceDTO.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
                "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<QualificationReference> qualificationReferences = qualificationReferenceMapper.qualificationReferenceDTOsToQualificationReferences(qualificationReferenceDTOS);
    qualificationReferences = qualificationReferenceRepository.save(qualificationReferences);
    List<QualificationReferenceDTO> results = qualificationReferenceMapper.qualificationReferencesToQualificationReferenceDTOs(qualificationReferences);
    return ResponseEntity.ok()
            .body(results);
  }

}
