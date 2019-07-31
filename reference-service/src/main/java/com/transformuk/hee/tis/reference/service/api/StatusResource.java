package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.api.dto.StatusDTO;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.model.Status;
import com.transformuk.hee.tis.reference.service.repository.StatusRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.StatusMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
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

/**
 * REST controller for managing Status.
 */
@RestController
@RequestMapping("/api")
public class StatusResource {

  private static final String ENTITY_NAME = "status";
  private final Logger log = LoggerFactory.getLogger(StatusResource.class);
  private final StatusRepository statusRepository;

  private final StatusMapper statusMapper;

  public StatusResource(StatusRepository statusRepository, StatusMapper statusMapper) {
    this.statusRepository = statusRepository;
    this.statusMapper = statusMapper;
  }

  /**
   * POST  /statuses : Create a new status.
   *
   * @param statusDTO the statusDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new statusDTO, or with
   * status 400 (Bad Request) if the status has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/statuses")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<StatusDTO> createStatus(@Valid @RequestBody StatusDTO statusDTO)
      throws URISyntaxException {
    log.debug("REST request to save Status : {}", statusDTO);
    if (statusDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil
          .createFailureAlert(ENTITY_NAME, "idexists", "A new status cannot already have an ID"))
          .body(null);
    }
    Status status = statusMapper.statusDTOToStatus(statusDTO);
    status = statusRepository.save(status);
    StatusDTO result = statusMapper.statusToStatusDTO(status);
    return ResponseEntity.created(new URI("/api/statuses/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /statuses : Updates an existing status.
   *
   * @param statusDTO the statusDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated statusDTO, or with
   * status 400 (Bad Request) if the statusDTO is not valid, or with status 500 (Internal Server
   * Error) if the statusDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/statuses")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<StatusDTO> updateStatus(@Valid @RequestBody StatusDTO statusDTO)
      throws URISyntaxException {
    log.debug("REST request to update Status : {}", statusDTO);
    if (statusDTO.getId() == null) {
      return createStatus(statusDTO);
    }
    Status status = statusMapper.statusDTOToStatus(statusDTO);
    status = statusRepository.save(status);
    StatusDTO result = statusMapper.statusToStatusDTO(status);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, statusDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /statuses : get all the statuses.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of statuses in body
   */
  @GetMapping("/statuses")
  @Timed
  public List<StatusDTO> getAllStatuses() {
    log.debug("REST request to get all Statuses");
    List<Status> statuses = statusRepository.findAll();
    return statusMapper.statusesToStatusDTOs(statuses);
  }

  /**
   * GET  /statuses/:id : get the "id" status.
   *
   * @param id the id of the statusDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the statusDTO, or with status 404
   * (Not Found)
   */
  @GetMapping("/statuses/{id}")
  @Timed
  public ResponseEntity<StatusDTO> getStatus(@PathVariable Long id) {
    log.debug("REST request to get Status : {}", id);
    Status status = statusRepository.findOne(id);
    StatusDTO statusDTO = statusMapper.statusToStatusDTO(status);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(statusDTO));
  }

  /**
   * DELETE  /statuses/:id : delete the "id" status.
   *
   * @param id the id of the statusDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/statuses/{id}")
  @Timed
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteStatus(@PathVariable Long id) {
    log.debug("REST request to delete Status : {}", id);
    statusRepository.delete(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }


  /**
   * POST  /bulk-statuses : Bulk create a new statuses.
   *
   * @param statusDTOS List of the statusDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new statusDTOS, or with
   * status 400 (Bad Request) if the StatusDto has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-statuses")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<StatusDTO>> bulkCreateStatus(
      @Valid @RequestBody List<StatusDTO> statusDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save StatusDtos : {}", statusDTOS);
    if (!Collections.isEmpty(statusDTOS)) {
      List<Long> entityIds = statusDTOS.stream()
          .filter(statusDTO -> statusDTO.getId() != null)
          .map(statusDTO -> statusDTO.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil
            .createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist",
                "A new statuses cannot already have an ID")).body(null);
      }
    }
    List<Status> statuses = statusMapper.statusDTOsToStatuses(statusDTOS);
    statuses = statusRepository.save(statuses);
    List<StatusDTO> result = statusMapper.statusesToStatusDTOs(statuses);
    return ResponseEntity.ok()
        .body(result);
  }

  /**
   * PUT  /bulk-statuses : Updates an existing statuses.
   *
   * @param statusDTOS List of the statusDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated statusDTOS, or with
   * status 400 (Bad Request) if the statusDTOS is not valid, or with status 500 (Internal Server
   * Error) if the statusDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-statuses")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<StatusDTO>> bulkUpdateStatus(
      @Valid @RequestBody List<StatusDTO> statusDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update StatusDtos : {}", statusDTOS);
    if (Collections.isEmpty(statusDTOS)) {
      return ResponseEntity.badRequest()
          .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
              "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(statusDTOS)) {
      List<StatusDTO> entitiesWithNoId = statusDTOS.stream()
          .filter(statusDTO -> statusDTO.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest()
            .headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
                "bulk.update.failed.noId",
                "Some DTOs you've provided have no Id, cannot update entities that dont exist"))
            .body(entitiesWithNoId);
      }
    }
    List<Status> statuses = statusMapper.statusDTOsToStatuses(statusDTOS);
    statuses = statusRepository.save(statuses);
    List<StatusDTO> results = statusMapper.statusesToStatusDTOs(statuses);
    return ResponseEntity.ok()
        .body(results);
  }

}
