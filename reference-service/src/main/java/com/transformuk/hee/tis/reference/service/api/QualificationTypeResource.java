package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.QualificationTypeDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.QualificationType;
import com.transformuk.hee.tis.reference.service.repository.QualificationTypeRepository;
import com.transformuk.hee.tis.reference.service.service.impl.QualificationTypeServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.QualificationTypeMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import uk.nhs.tis.StringConverter;
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

/**
 * REST controller for managing QualificationType.
 */
@RestController
@RequestMapping("/api")
public class QualificationTypeResource {

  private static final String ENTITY_NAME = "qualificationType";
  private final Logger log = LoggerFactory.getLogger(QualificationTypeResource.class);

  private final QualificationTypeRepository qualificationTypeRepository;
  private final QualificationTypeMapper qualificationTypeMapper;
  private final QualificationTypeServiceImpl qualificationTypeService;

  public QualificationTypeResource(QualificationTypeRepository qualificationTypeRepository,
                                   QualificationTypeMapper qualificationTypeMapper,
                                   QualificationTypeServiceImpl qualificationTypeService) {
    this.qualificationTypeRepository = qualificationTypeRepository;
    this.qualificationTypeMapper = qualificationTypeMapper;
    this.qualificationTypeService = qualificationTypeService;
  }

  /**
   * POST  /qualification-types : Create a new qualificationType.
   *
   * @param qualificationTypeDTO the qualificationTypeDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new qualificationTypeDTO, or with status 400 (Bad Request) if the qualificationType has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/qualification-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<QualificationTypeDTO> createQualificationType(@Valid @RequestBody QualificationTypeDTO qualificationTypeDTO) throws URISyntaxException {
    log.debug("REST request to save QualificationType : {}", qualificationTypeDTO);
    if (qualificationTypeDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new qualificationType cannot already have an ID")).body(null);
    }
    QualificationType qualificationType = qualificationTypeMapper.qualificationTypeDTOToQualificationType(qualificationTypeDTO);
    qualificationType = qualificationTypeRepository.save(qualificationType);
    QualificationTypeDTO result = qualificationTypeMapper.qualificationTypeToQualificationTypeDTO(qualificationType);
    return ResponseEntity.created(new URI("/api/qualification-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
  }

  /**
   * PUT  /qualification-types : Updates an existing qualificationTypeDTO.
   *
   * @param qualificationTypeDTO the qualificationTypeDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated qualificationTypeDTO,
   * or with status 400 (Bad Request) if the qualificationTypeDTO is not valid,
   * or with status 500 (Internal Server Error) if the qualificationTypeDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/qualification-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<QualificationTypeDTO> updateQualificationType(@Valid @RequestBody QualificationTypeDTO qualificationTypeDTO) throws URISyntaxException {
    log.debug("REST request to update QualificationType : {}", qualificationTypeDTO);
    if (qualificationTypeDTO.getId() == null) {
      return createQualificationType(qualificationTypeDTO);
    }
    QualificationType qualificationType = qualificationTypeMapper.qualificationTypeDTOToQualificationType(qualificationTypeDTO);
    qualificationType = qualificationTypeRepository.save(qualificationType);
    QualificationTypeDTO result = qualificationTypeMapper.qualificationTypeToQualificationTypeDTO(qualificationType);
    return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, qualificationTypeDTO.getId().toString()))
            .body(result);
  }


  /**
   * GET  /qualification-types : get all qualificationType.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of qualification Type in body
   */
  @ApiOperation(value = "Lists qualification Type",
          notes = "Returns a list of qualification Type with support for pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Qualification Type list")})
  @GetMapping("/qualification-types")
  @Timed
  public ResponseEntity<List<QualificationTypeDTO>> getAllQualificationTypes(
          @ApiParam Pageable pageable,
          @ApiParam(value = "any wildcard string to be searched")
          @RequestParam(value = "searchQuery", required = false) String searchQuery,
          @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
          @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.info("REST request to get a page of Qualification Type begin");
    searchQuery = StringConverter.getConverter(searchQuery).decodeUrl().escapeForSql().toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    Page<QualificationType> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = qualificationTypeRepository.findAll(pageable);
    } else {
      page = qualificationTypeService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<QualificationTypeDTO> results = page.map(qualificationTypeMapper::qualificationTypeToQualificationTypeDTO);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/qualification-types");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /qualification-types/:id : get the "id" qualificationType.
   *
   * @param id the id of the qualificationTypeDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the qualificationTypeDTO, or with status 404 (Not Found)
   */
  @GetMapping("/qualification-types/{id}")
  @Timed
  public ResponseEntity<QualificationTypeDTO> getQualificationType(@PathVariable Long id) {
    log.debug("REST request to get qualificationType : {}", id);
    QualificationType qualificationType = qualificationTypeRepository.findOne(id);
    QualificationTypeDTO qualificationTypeDTO = qualificationTypeMapper.qualificationTypeToQualificationTypeDTO(qualificationType);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(qualificationTypeDTO));
  }

  /**
   * DELETE  /qualification-types/:id : delete the "id" qualificationType.
   *
   * @param id the id of the qualificationTypeDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/qualification-types/{id}")
  @Timed
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteQualificationType(@PathVariable Long id) {
    log.debug("REST request to delete qualificationType : {}", id);
    qualificationTypeRepository.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * EXISTS /qualification-types/exists/ : check is qualificationType exists
   *
   * @param code the code of the qualificationTypeDTO to check
   * @return boolean true if exists otherwise false
   */
  @PostMapping("/qualification-types/exists/")
  @Timed
  public ResponseEntity<Boolean> qualificationTypeExists(@RequestBody String code) {
    log.debug("REST request to check QualificationType exists : {}", code);
    QualificationType qualificationType = qualificationTypeRepository.findFirstByCode(code);
    if(qualificationType == null){
      return new ResponseEntity<>(false, HttpStatus.OK);
    }
    return new ResponseEntity<>(true, HttpStatus.OK);
  }

  /**
   * POST  /bulk-qualification-types : Bulk create a new qualification-types.
   *
   * @param qualificationTypeDTOs List of the qualificationTypeDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new qualificationTypeDTOS, or with status 400 (Bad Request) if the qualificationTypeDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-qualification-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<QualificationTypeDTO>> bulkCreateQualificationType(@Valid @RequestBody List<QualificationTypeDTO> qualificationTypeDTOs) throws URISyntaxException {
    log.debug("REST request to bulk save qualificationType : {}", qualificationTypeDTOs);
    if (!Collections.isEmpty(qualificationTypeDTOs)) {
      List<Long> entityIds = qualificationTypeDTOs.stream()
              .filter(qualificationTypeDTO -> qualificationTypeDTO.getId() != null)
              .map(qualificationTypeDTO -> qualificationTypeDTO.getId())
              .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new qualificationType cannot already have an ID")).body(null);
      }
    }
    List<QualificationType> qualificationTypes = qualificationTypeMapper.qualificationTypeDTOsToQualificationTypes(qualificationTypeDTOs);
    qualificationTypes = qualificationTypeRepository.save(qualificationTypes);
    List<QualificationTypeDTO> result = qualificationTypeMapper.qualificationTypesToQualificationTypeDTOs(qualificationTypes);
    return ResponseEntity.ok()
            .body(result);
  }

  /**
   * PUT  /bulk-qualification-types : Updates an existing qualification-types.
   *
   * @param qualificationTypeDTOS List of the qualificationTypeDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated qualificationTypeDTOS,
   * or with status 400 (Bad Request) if the qualificationTypeDTOS is not valid,
   * or with status 500 (Internal Server Error) if the qualificationTypeDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-qualification-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<QualificationTypeDTO>> bulkUpdateQualificationType(@Valid @RequestBody List<QualificationTypeDTO> qualificationTypeDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update qualificationType : {}", qualificationTypeDTOS);
    if (Collections.isEmpty(qualificationTypeDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
              "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(qualificationTypeDTOS)) {
      List<QualificationTypeDTO> entitiesWithNoId = qualificationTypeDTOS.stream().filter(qualificationTypeDTO -> qualificationTypeDTO.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
                "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<QualificationType> qualificationTypes = qualificationTypeMapper.qualificationTypeDTOsToQualificationTypes(qualificationTypeDTOS);
    qualificationTypes = qualificationTypeRepository.save(qualificationTypes);
    List<QualificationTypeDTO> results = qualificationTypeMapper.qualificationTypesToQualificationTypeDTOs(qualificationTypes);
    return ResponseEntity.ok()
            .body(results);
  }

}
