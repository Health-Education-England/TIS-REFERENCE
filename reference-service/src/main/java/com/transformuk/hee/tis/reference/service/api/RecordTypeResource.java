package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.api.dto.RecordTypeDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.model.RecordType;
import com.transformuk.hee.tis.reference.service.repository.RecordTypeRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.RecordTypeMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
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
 * REST controller for managing RecordType.
 */
@RestController
@RequestMapping("/api")
public class RecordTypeResource {

  private static final String ENTITY_NAME = "recordType";
  private final Logger log = LoggerFactory.getLogger(RecordTypeResource.class);
  private final RecordTypeRepository recordTypeRepository;

  private final RecordTypeMapper recordTypeMapper;

  public RecordTypeResource(RecordTypeRepository recordTypeRepository, RecordTypeMapper recordTypeMapper) {
    this.recordTypeRepository = recordTypeRepository;
    this.recordTypeMapper = recordTypeMapper;
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
   * GET  /record-types : get all the recordTypes.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of recordTypes in body
   */
  @GetMapping("/record-types")
  @Timed
  public List<RecordTypeDTO> getAllRecordTypes() {
    log.debug("REST request to get all RecordTypes");
    List<RecordType> recordTypes = recordTypeRepository.findAll();
    return recordTypeMapper.recordTypesToRecordTypeDTOs(recordTypes);
  }

  /**
   * GET  /current/record-types : get all the current recordTypes.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of recordTypes in body
   */
  @GetMapping("/current/record-types")
  @Timed
  public List<RecordTypeDTO> getAllCurrentRecordTypes() {
    log.debug("REST request to get all RecordTypes");
    RecordType recordType = new RecordType();
    recordType.setStatus(Status.CURRENT);
    List<RecordType> recordTypes = recordTypeRepository.findAll(Example.of(recordType));
    return recordTypeMapper.recordTypesToRecordTypeDTOs(recordTypes);
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
    List<Long> ids = result.stream().map(rt -> rt.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
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
    List<Long> ids = results.stream().map(at -> at.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }

}
