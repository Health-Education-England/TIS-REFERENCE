package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.RecordTypeDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.RecordType;
import com.transformuk.hee.tis.reference.service.repository.RecordTypeRepository;
import com.transformuk.hee.tis.reference.service.service.impl.RecordTypeServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.RecordTypeMapper;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing RecordType.
 */
@RestController
@RequestMapping("/api")
public class RecordTypeResource {

  private static final String ENTITY_NAME = "recordType";
  private final Logger log = LoggerFactory.getLogger(RecordTypeResource.class);

  private final RecordTypeRepository recordTypeRepository;
  private final RecordTypeMapper recordTypeMapper;
  private final RecordTypeServiceImpl recordTypeService;

  public RecordTypeResource(RecordTypeRepository recordTypeRepository, RecordTypeMapper recordTypeMapper,
                            RecordTypeServiceImpl recordTypeService) {
    this.recordTypeRepository = recordTypeRepository;
    this.recordTypeMapper = recordTypeMapper;
    this.recordTypeService = recordTypeService;
  }

  /**
   * POST  /record-types : Create a new recordType.
   *
   * @param recordTypeDTO the recordTypeDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new recordTypeDTO, or with status 400 (Bad Request) if the recordType has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/record-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<RecordTypeDTO> createRecordType(@Valid @RequestBody RecordTypeDTO recordTypeDTO) throws URISyntaxException {
    log.debug("REST request to save RecordType : {}", recordTypeDTO);
    if (recordTypeDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new recordType cannot already have an ID")).body(null);
    }
    RecordType recordType = recordTypeMapper.recordTypeDTOToRecordType(recordTypeDTO);
    recordType = recordTypeRepository.save(recordType);
    RecordTypeDTO result = recordTypeMapper.recordTypeToRecordTypeDTO(recordType);
    return ResponseEntity.created(new URI("/api/record-types/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /record-types : Updates an existing recordType.
   *
   * @param recordTypeDTO the recordTypeDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated recordTypeDTO,
   * or with status 400 (Bad Request) if the recordTypeDTO is not valid,
   * or with status 500 (Internal Server Error) if the recordTypeDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/record-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<RecordTypeDTO> updateRecordType(@Valid @RequestBody RecordTypeDTO recordTypeDTO) throws URISyntaxException {
    log.debug("REST request to update RecordType : {}", recordTypeDTO);
    if (recordTypeDTO.getId() == null) {
      return createRecordType(recordTypeDTO);
    }
    RecordType recordType = recordTypeMapper.recordTypeDTOToRecordType(recordTypeDTO);
    recordType = recordTypeRepository.save(recordType);
    RecordTypeDTO result = recordTypeMapper.recordTypeToRecordTypeDTO(recordType);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, recordTypeDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /record-types : get all record types.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of record types in body
   */
  @ApiOperation(value = "Lists record types",
      notes = "Returns a list of record types with support for pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "record types list")})
  @GetMapping("/record-types")
  @Timed
  public ResponseEntity<List<RecordTypeDTO>> getAllRecordTypes(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.info("REST request to get a page of record types begin");
    searchQuery = StringConverter.getConverter(searchQuery).fromJson().decodeUrl().escapeForSql().toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    Page<RecordType> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = recordTypeRepository.findAll(pageable);
    } else {
      page = recordTypeService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<RecordTypeDTO> results = page.map(recordTypeMapper::recordTypeToRecordTypeDTO);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/record-types");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /record-types/:id : get the "id" recordType.
   *
   * @param id the id of the recordTypeDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the recordTypeDTO, or with status 404 (Not Found)
   */
  @GetMapping("/record-types/{id}")
  @Timed
  public ResponseEntity<RecordTypeDTO> getRecordType(@PathVariable Long id) {
    log.debug("REST request to get RecordType : {}", id);
    RecordType recordType = recordTypeRepository.findOne(id);
    RecordTypeDTO recordTypeDTO = recordTypeMapper.recordTypeToRecordTypeDTO(recordType);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(recordTypeDTO));
  }

  /**
   * DELETE  /record-types/:id : delete the "id" recordType.
   *
   * @param id the id of the recordTypeDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/record-types/{id}")
  @Timed
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteRecordType(@PathVariable Long id) {
    log.debug("REST request to delete RecordType : {}", id);
    recordTypeRepository.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }


  /**
   * POST  /bulk-record-types : Bulk create a new record-types.
   *
   * @param recordTypeDTOS List of the recordTypeDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new recordTypeDTOS, or with status 400 (Bad Request) if the RecordTypeDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-record-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<RecordTypeDTO>> bulkCreateRecordType(@Valid @RequestBody List<RecordTypeDTO> recordTypeDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save RecordTypeDtos : {}", recordTypeDTOS);
    if (!Collections.isEmpty(recordTypeDTOS)) {
      List<Long> entityIds = recordTypeDTOS.stream()
          .filter(rt -> rt.getId() != null)
          .map(rt -> rt.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new recordTypes cannot already have an ID")).body(null);
      }
    }
    List<RecordType> recordTypes = recordTypeMapper.recordTypeDTOsToRecordTypes(recordTypeDTOS);
    recordTypes = recordTypeRepository.save(recordTypes);
    List<RecordTypeDTO> result = recordTypeMapper.recordTypesToRecordTypeDTOs(recordTypes);
    return ResponseEntity.ok()
        .body(result);
  }

  /**
   * PUT  /bulk-record-types : Updates an existing record-types.
   *
   * @param recordTypeDTOS List of the recordTypeDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated recordTypeDTOS,
   * or with status 400 (Bad Request) if the recordTypeDTOS is not valid,
   * or with status 500 (Internal Server Error) if the recordTypeDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-record-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<RecordTypeDTO>> bulkUpdateRecordType(@Valid @RequestBody List<RecordTypeDTO> recordTypeDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update RecordTypeDtos : {}", recordTypeDTOS);
    if (Collections.isEmpty(recordTypeDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(recordTypeDTOS)) {
      List<RecordTypeDTO> entitiesWithNoId = recordTypeDTOS.stream().filter(rt -> rt.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<RecordType> recordTypes = recordTypeMapper.recordTypeDTOsToRecordTypes(recordTypeDTOS);
    recordTypes = recordTypeRepository.save(recordTypes);
    List<RecordTypeDTO> results = recordTypeMapper.recordTypesToRecordTypeDTOs(recordTypes);
    return ResponseEntity.ok()
        .body(results);
  }

}
