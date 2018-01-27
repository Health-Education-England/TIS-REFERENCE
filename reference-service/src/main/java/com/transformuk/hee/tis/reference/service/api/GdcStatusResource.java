package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.api.dto.GdcStatusDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.model.GdcStatus;
import com.transformuk.hee.tis.reference.service.repository.GdcStatusRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.GdcStatusMapper;
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
 * REST controller for managing GdcStatus.
 */
@RestController
@RequestMapping("/api")
public class GdcStatusResource {

  private static final String ENTITY_NAME = "gdcStatus";
  private final Logger log = LoggerFactory.getLogger(GdcStatusResource.class);
  private final GdcStatusRepository gdcStatusRepository;

  private final GdcStatusMapper gdcStatusMapper;

  public GdcStatusResource(GdcStatusRepository gdcStatusRepository, GdcStatusMapper gdcStatusMapper) {
    this.gdcStatusRepository = gdcStatusRepository;
    this.gdcStatusMapper = gdcStatusMapper;
  }

  /**
   * POST  /gdc-statuses : Create a new gdcStatus.
   *
   * @param gdcStatusDTO the gdcStatusDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new gdcStatusDTO, or with status 400 (Bad Request) if the gdcStatus has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/gdc-statuses")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<GdcStatusDTO> createGdcStatus(@Valid @RequestBody GdcStatusDTO gdcStatusDTO) throws URISyntaxException {
    log.debug("REST request to save GdcStatus : {}", gdcStatusDTO);
    if (gdcStatusDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new gdcStatus cannot already have an ID")).body(null);
    }
    GdcStatus gdcStatus = gdcStatusMapper.gdcStatusDTOToGdcStatus(gdcStatusDTO);
    gdcStatus = gdcStatusRepository.save(gdcStatus);
    GdcStatusDTO result = gdcStatusMapper.gdcStatusToGdcStatusDTO(gdcStatus);
    return ResponseEntity.created(new URI("/api/gdc-statuses/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /gdc-statuses : Updates an existing gdcStatus.
   *
   * @param gdcStatusDTO the gdcStatusDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated gdcStatusDTO,
   * or with status 400 (Bad Request) if the gdcStatusDTO is not valid,
   * or with status 500 (Internal Server Error) if the gdcStatusDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/gdc-statuses")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<GdcStatusDTO> updateGdcStatus(@Valid @RequestBody GdcStatusDTO gdcStatusDTO) throws URISyntaxException {
    log.debug("REST request to update GdcStatus : {}", gdcStatusDTO);
    if (gdcStatusDTO.getId() == null) {
      return createGdcStatus(gdcStatusDTO);
    }
    GdcStatus gdcStatus = gdcStatusMapper.gdcStatusDTOToGdcStatus(gdcStatusDTO);
    gdcStatus = gdcStatusRepository.save(gdcStatus);
    GdcStatusDTO result = gdcStatusMapper.gdcStatusToGdcStatusDTO(gdcStatus);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gdcStatusDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /gdc-statuses : get all the gdcStatuses.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of gdcStatuses in body
   */
  @GetMapping("/gdc-statuses")
  @Timed
  public List<GdcStatusDTO> getAllGdcStatuses() {
    log.debug("REST request to get all GdcStatuses");
    List<GdcStatus> gdcStatuses = gdcStatusRepository.findAll();
    return gdcStatusMapper.gdcStatusesToGdcStatusDTOs(gdcStatuses);
  }

  /**
   * GET  /current/gdc-statuses : get all current gdcStatuses.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of gdcStatuses in body
   */
  @GetMapping("/current/gdc-statuses")
  @Timed
  public List<GdcStatusDTO> getAllCurrentGdcStatuses() {
    log.debug("REST request to get all current GdcStatuses");
    GdcStatus gdcStatus = new GdcStatus();
    gdcStatus.setStatus(Status.CURRENT);
    List<GdcStatus> gdcStatuses = gdcStatusRepository.findAll(Example.of(gdcStatus));
    return gdcStatusMapper.gdcStatusesToGdcStatusDTOs(gdcStatuses);
  }

  /**
   * GET  /gdc-statuses/:id : get the "id" gdcStatus.
   *
   * @param id the id of the gdcStatusDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the gdcStatusDTO, or with status 404 (Not Found)
   */
  @GetMapping("/gdc-statuses/{id}")
  @Timed
  public ResponseEntity<GdcStatusDTO> getGdcStatus(@PathVariable Long id) {
    log.debug("REST request to get GdcStatus : {}", id);
    GdcStatus gdcStatus = gdcStatusRepository.findOne(id);
    GdcStatusDTO gdcStatusDTO = gdcStatusMapper.gdcStatusToGdcStatusDTO(gdcStatus);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gdcStatusDTO));
  }

  /**
   * DELETE  /gdc-statuses/:id : delete the "id" gdcStatus.
   *
   * @param id the id of the gdcStatusDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/gdc-statuses/{id}")
  @Timed
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteGdcStatus(@PathVariable Long id) {
    log.debug("REST request to delete GdcStatus : {}", id);
    gdcStatusRepository.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * POST  /bulk-gdc-statuses : Bulk create a new gdc-statuses.
   *
   * @param gdcStatusDTOS List of the gdcStatusDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new gdcStatusDTOS, or with status 400 (Bad Request) if the GdcStatusDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-gdc-statuses")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<GdcStatusDTO>> bulkCreateGdcStatus(@Valid @RequestBody List<GdcStatusDTO> gdcStatusDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save GdcStatus : {}", gdcStatusDTOS);
    if (!Collections.isEmpty(gdcStatusDTOS)) {
      List<Long> entityIds = gdcStatusDTOS.stream()
          .filter(gdcStatusDTO -> gdcStatusDTO.getId() != null)
          .map(gdcStatusDTO -> gdcStatusDTO.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new gdcStatuses cannot already have an ID")).body(null);
      }
    }
    List<GdcStatus> gdcStatuses = gdcStatusMapper.gdcStatusDTOsToGdcStatuses(gdcStatusDTOS);
    gdcStatuses = gdcStatusRepository.save(gdcStatuses);
    List<GdcStatusDTO> result = gdcStatusMapper.gdcStatusesToGdcStatusDTOs(gdcStatuses);
    return ResponseEntity.ok()
        .body(result);
  }

  /**
   * PUT  /bulk-gdc-statuses : Updates an existing gdc-statuses.
   *
   * @param gdcStatusDTOS List of the gdcStatusDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated gdcStatusDTOS,
   * or with status 400 (Bad Request) if the gdcStatusDTOS is not valid,
   * or with status 500 (Internal Server Error) if the gdcStatusDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-gdc-statuses")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<GdcStatusDTO>> bulkUpdateGdcStatus(@Valid @RequestBody List<GdcStatusDTO> gdcStatusDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update gdcStatus : {}", gdcStatusDTOS);
    if (Collections.isEmpty(gdcStatusDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(gdcStatusDTOS)) {
      List<GdcStatusDTO> entitiesWithNoId = gdcStatusDTOS.stream().filter(gdcStatusDTO -> gdcStatusDTO.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<GdcStatus> gdcStatuses = gdcStatusMapper.gdcStatusDTOsToGdcStatuses(gdcStatusDTOS);
    gdcStatuses = gdcStatusRepository.save(gdcStatuses);
    List<GdcStatusDTO> results = gdcStatusMapper.gdcStatusesToGdcStatusDTOs(gdcStatuses);
    return ResponseEntity.ok()
        .body(results);
  }
}
