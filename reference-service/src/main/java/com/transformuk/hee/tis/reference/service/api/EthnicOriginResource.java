package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.api.dto.EthnicOriginDTO;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.model.EthnicOrigin;
import com.transformuk.hee.tis.reference.service.repository.EthnicOriginRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.EthnicOriginMapper;
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
 * REST controller for managing EthnicOrigin.
 */
@RestController
@RequestMapping("/api")
public class EthnicOriginResource {

  private static final String ENTITY_NAME = "ethnicOrigin";
  private final Logger log = LoggerFactory.getLogger(EthnicOriginResource.class);
  private final EthnicOriginRepository ethnicOriginRepository;

  private final EthnicOriginMapper ethnicOriginMapper;

  public EthnicOriginResource(EthnicOriginRepository ethnicOriginRepository, EthnicOriginMapper ethnicOriginMapper) {
    this.ethnicOriginRepository = ethnicOriginRepository;
    this.ethnicOriginMapper = ethnicOriginMapper;
  }

  /**
   * POST  /ethnic-origins : Create a new ethnicOrigin.
   *
   * @param ethnicOriginDTO the ethnicOriginDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new ethnicOriginDTO, or with status 400 (Bad Request) if the ethnicOrigin has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/ethnic-origins")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<EthnicOriginDTO> createEthnicOrigin(@Valid @RequestBody EthnicOriginDTO ethnicOriginDTO) throws URISyntaxException {
    log.debug("REST request to save EthnicOrigin : {}", ethnicOriginDTO);
    if (ethnicOriginDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new ethnicOrigin cannot already have an ID")).body(null);
    }
    EthnicOrigin ethnicOrigin = ethnicOriginMapper.ethnicOriginDTOToEthnicOrigin(ethnicOriginDTO);
    ethnicOrigin = ethnicOriginRepository.save(ethnicOrigin);
    EthnicOriginDTO result = ethnicOriginMapper.ethnicOriginToEthnicOriginDTO(ethnicOrigin);
    return ResponseEntity.created(new URI("/api/ethnic-origins/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /ethnic-origins : Updates an existing ethnicOrigin.
   *
   * @param ethnicOriginDTO the ethnicOriginDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated ethnicOriginDTO,
   * or with status 400 (Bad Request) if the ethnicOriginDTO is not valid,
   * or with status 500 (Internal Server Error) if the ethnicOriginDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/ethnic-origins")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<EthnicOriginDTO> updateEthnicOrigin(@Valid @RequestBody EthnicOriginDTO ethnicOriginDTO) throws URISyntaxException {
    log.debug("REST request to update EthnicOrigin : {}", ethnicOriginDTO);
    if (ethnicOriginDTO.getId() == null) {
      return createEthnicOrigin(ethnicOriginDTO);
    }
    EthnicOrigin ethnicOrigin = ethnicOriginMapper.ethnicOriginDTOToEthnicOrigin(ethnicOriginDTO);
    ethnicOrigin = ethnicOriginRepository.save(ethnicOrigin);
    EthnicOriginDTO result = ethnicOriginMapper.ethnicOriginToEthnicOriginDTO(ethnicOrigin);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ethnicOriginDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /ethnic-origins : get all the ethnicOrigins.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of ethnicOrigins in body
   */
  @GetMapping("/ethnic-origins")
  @Timed
  public List<EthnicOriginDTO> getAllEthnicOrigins() {
    log.debug("REST request to get all EthnicOrigins");
    List<EthnicOrigin> ethnicOrigins = ethnicOriginRepository.findAll();
    return ethnicOriginMapper.ethnicOriginsToEthnicOriginDTOs(ethnicOrigins);
  }

  /**
   * GET  /ethnic-origins/:id : get the "id" ethnicOrigin.
   *
   * @param id the id of the ethnicOriginDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the ethnicOriginDTO, or with status 404 (Not Found)
   */
  @GetMapping("/ethnic-origins/{id}")
  @Timed
  public ResponseEntity<EthnicOriginDTO> getEthnicOrigin(@PathVariable Long id) {
    log.debug("REST request to get EthnicOrigin : {}", id);
    EthnicOrigin ethnicOrigin = ethnicOriginRepository.findOne(id);
    EthnicOriginDTO ethnicOriginDTO = ethnicOriginMapper.ethnicOriginToEthnicOriginDTO(ethnicOrigin);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ethnicOriginDTO));
  }

  /**
   * DELETE  /ethnic-origins/:id : delete the "id" ethnicOrigin.
   *
   * @param id the id of the ethnicOriginDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/ethnic-origins/{id}")
  @Timed
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteEthnicOrigin(@PathVariable Long id) {
    log.debug("REST request to delete EthnicOrigin : {}", id);
    ethnicOriginRepository.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * POST  /bulk-ethnic-origins : Bulk create a new ethnic-origins.
   *
   * @param ethnicOriginDTOS List of the ethnicOriginDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new ethnicOriginDTOS, or with status 400 (Bad Request) if the EthnicOriginDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-ethnic-origins")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<EthnicOriginDTO>> bulkCreateEthnicOrigin(@Valid @RequestBody List<EthnicOriginDTO> ethnicOriginDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save EthnicOriginDtos : {}", ethnicOriginDTOS);
    if (!Collections.isEmpty(ethnicOriginDTOS)) {
      List<Long> entityIds = ethnicOriginDTOS.stream()
          .filter(ethnicOriginDTO -> ethnicOriginDTO.getId() != null)
          .map(ethnicOriginDTO -> ethnicOriginDTO.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new ethnicOrigins cannot already have an ID")).body(null);
      }
    }
    List<EthnicOrigin> ethnicOrigins = ethnicOriginMapper.ethnicOriginDTOsToEthnicOrigins(ethnicOriginDTOS);
    ethnicOrigins = ethnicOriginRepository.save(ethnicOrigins);
    List<EthnicOriginDTO> result = ethnicOriginMapper.ethnicOriginsToEthnicOriginDTOs(ethnicOrigins);
    List<Long> ids = result.stream().map(eo -> eo.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }

  /**
   * PUT  /bulk-ethnic-origins : Updates an existing EthnicOrigin.
   *
   * @param ethnicOriginDTOS List of the ethnicOriginDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated ethnicOriginDTOS,
   * or with status 400 (Bad Request) if the ethnicOriginDTOS is not valid,
   * or with status 500 (Internal Server Error) if the ethnicOriginDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-ethnic-origins")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<EthnicOriginDTO>> bulkUpdateEthnicOrigin(@Valid @RequestBody List<EthnicOriginDTO> ethnicOriginDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update EthnicOriginDTOs : {}", ethnicOriginDTOS);
    if (Collections.isEmpty(ethnicOriginDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(ethnicOriginDTOS)) {
      List<EthnicOriginDTO> entitiesWithNoId = ethnicOriginDTOS.stream().filter(eo -> eo.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<EthnicOrigin> ethnicOrigins = ethnicOriginMapper.ethnicOriginDTOsToEthnicOrigins(ethnicOriginDTOS);
    ethnicOrigins = ethnicOriginRepository.save(ethnicOrigins);
    List<EthnicOriginDTO> results = ethnicOriginMapper.ethnicOriginsToEthnicOriginDTOs(ethnicOrigins);
    List<Long> ids = results.stream().map(eo -> eo.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }
}
