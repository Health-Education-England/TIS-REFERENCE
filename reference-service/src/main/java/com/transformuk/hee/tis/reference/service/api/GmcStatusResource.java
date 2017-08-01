package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.api.dto.GmcStatusDTO;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.model.GmcStatus;
import com.transformuk.hee.tis.reference.service.repository.GmcStatusRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.GmcStatusMapper;
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
 * REST controller for managing GmcStatus.
 */
@RestController
@RequestMapping("/api")
public class GmcStatusResource {

  private static final String ENTITY_NAME = "gmcStatus";
  private final Logger log = LoggerFactory.getLogger(GmcStatusResource.class);
  private final GmcStatusRepository gmcStatusRepository;

  private final GmcStatusMapper gmcStatusMapper;

  public GmcStatusResource(GmcStatusRepository gmcStatusRepository, GmcStatusMapper gmcStatusMapper) {
    this.gmcStatusRepository = gmcStatusRepository;
    this.gmcStatusMapper = gmcStatusMapper;
  }

  /**
   * POST  /gmc-statuses : Create a new gmcStatus.
   *
   * @param gmcStatusDTO the gmcStatusDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new gmcStatusDTO, or with status 400 (Bad Request) if the gmcStatus has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/gmc-statuses")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<GmcStatusDTO> createGmcStatus(@Valid @RequestBody GmcStatusDTO gmcStatusDTO) throws URISyntaxException {
    log.debug("REST request to save GmcStatus : {}", gmcStatusDTO);
    if (gmcStatusDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new gmcStatus cannot already have an ID")).body(null);
    }
    GmcStatus gmcStatus = gmcStatusMapper.gmcStatusDTOToGmcStatus(gmcStatusDTO);
    gmcStatus = gmcStatusRepository.save(gmcStatus);
    GmcStatusDTO result = gmcStatusMapper.gmcStatusToGmcStatusDTO(gmcStatus);
    return ResponseEntity.created(new URI("/api/gmc-statuses/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /gmc-statuses : Updates an existing gmcStatus.
   *
   * @param gmcStatusDTO the gmcStatusDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated gmcStatusDTO,
   * or with status 400 (Bad Request) if the gmcStatusDTO is not valid,
   * or with status 500 (Internal Server Error) if the gmcStatusDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/gmc-statuses")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<GmcStatusDTO> updateGmcStatus(@Valid @RequestBody GmcStatusDTO gmcStatusDTO) throws URISyntaxException {
    log.debug("REST request to update GmcStatus : {}", gmcStatusDTO);
    if (gmcStatusDTO.getId() == null) {
      return createGmcStatus(gmcStatusDTO);
    }
    GmcStatus gmcStatus = gmcStatusMapper.gmcStatusDTOToGmcStatus(gmcStatusDTO);
    gmcStatus = gmcStatusRepository.save(gmcStatus);
    GmcStatusDTO result = gmcStatusMapper.gmcStatusToGmcStatusDTO(gmcStatus);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gmcStatusDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /gmc-statuses : get all the gmcStatuses.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of gmcStatuses in body
   */
  @GetMapping("/gmc-statuses")
  @Timed
  public List<GmcStatusDTO> getAllGmcStatuses() {
    log.debug("REST request to get all GmcStatuses");
    List<GmcStatus> gmcStatuses = gmcStatusRepository.findAll();
    return gmcStatusMapper.gmcStatusesToGmcStatusDTOs(gmcStatuses);
  }

  /**
   * GET  /gmc-statuses/:id : get the "id" gmcStatus.
   *
   * @param id the id of the gmcStatusDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the gmcStatusDTO, or with status 404 (Not Found)
   */
  @GetMapping("/gmc-statuses/{id}")
  @Timed
  public ResponseEntity<GmcStatusDTO> getGmcStatus(@PathVariable Long id) {
    log.debug("REST request to get GmcStatus : {}", id);
    GmcStatus gmcStatus = gmcStatusRepository.findOne(id);
    GmcStatusDTO gmcStatusDTO = gmcStatusMapper.gmcStatusToGmcStatusDTO(gmcStatus);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gmcStatusDTO));
  }

  /**
   * DELETE  /gmc-statuses/:id : delete the "id" gmcStatus.
   *
   * @param id the id of the gmcStatusDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/gmc-statuses/{id}")
  @Timed
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteGmcStatus(@PathVariable Long id) {
    log.debug("REST request to delete GmcStatus : {}", id);
    gmcStatusRepository.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * POST  /bulk-gmc-statuses : Bulk create a new gmc-statuses.
   *
   * @param gmcStatusDTOS List of the gmcStatusDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new gmcStatusDTOS, or with status 400 (Bad Request) if the GmcStatusDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-gmc-statuses")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<GmcStatusDTO>> bulkCreateGmcStatus(@Valid @RequestBody List<GmcStatusDTO> gmcStatusDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save gmcstatus : {}", gmcStatusDTOS);
    if (!Collections.isEmpty(gmcStatusDTOS)) {
      List<Long> entityIds = gmcStatusDTOS.stream()
          .filter(gmcStatusDTO -> gmcStatusDTO.getId() != null)
          .map(GmcStatusDTO::getId)
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new gmcStatuses cannot already have an ID")).body(null);
      }
    }
    List<GmcStatus> gmcStatuses = gmcStatusMapper.gmcStatusDTOsToGmcStatuses(gmcStatusDTOS);
    gmcStatuses = gmcStatusRepository.save(gmcStatuses);
    List<GmcStatusDTO> result = gmcStatusMapper.gmcStatusesToGmcStatusDTOs(gmcStatuses);
    List<Long> ids = result.stream().map(gmcStatusDTO -> gmcStatusDTO.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }

  /**
   * PUT  /bulk-gmc-statuses : Updates an existing gmc-statuses.
   *
   * @param gmcStatusDTOS List of the gmcStatusDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated gmcStatusDTOS,
   * or with status 400 (Bad Request) if the gmcStatusDTOS is not valid,
   * or with status 500 (Internal Server Error) if the gmcStatusDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-gmc-statuses")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<GmcStatusDTO>> bulkUpdateGmcStatus(@Valid @RequestBody List<GmcStatusDTO> gmcStatusDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update GmcStatus : {}", gmcStatusDTOS);
    if (Collections.isEmpty(gmcStatusDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(gmcStatusDTOS)) {
      List<GmcStatusDTO> entitiesWithNoId = gmcStatusDTOS.stream().filter(status -> status.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<GmcStatus> gmcStatuses = gmcStatusMapper.gmcStatusDTOsToGmcStatuses(gmcStatusDTOS);
    gmcStatuses = gmcStatusRepository.save(gmcStatuses);
    List<GmcStatusDTO> results = gmcStatusMapper.gmcStatusesToGmcStatusDTOs(gmcStatuses);
    List<Long> ids = results.stream().map(gmcStatusDTO -> gmcStatusDTO.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }

}
