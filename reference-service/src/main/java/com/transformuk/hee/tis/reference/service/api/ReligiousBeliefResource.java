package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.api.dto.ReligiousBeliefDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ReligiousBelief;
import com.transformuk.hee.tis.reference.service.repository.ReligiousBeliefRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.ReligiousBeliefMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
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
 * REST controller for managing ReligiousBelief.
 */
@RestController
@RequestMapping("/api")
public class ReligiousBeliefResource {

  private static final String ENTITY_NAME = "religiousBelief";
  private final Logger log = LoggerFactory.getLogger(ReligiousBeliefResource.class);
  private final ReligiousBeliefRepository religiousBeliefRepository;

  private final ReligiousBeliefMapper religiousBeliefMapper;

  public ReligiousBeliefResource(ReligiousBeliefRepository religiousBeliefRepository, ReligiousBeliefMapper religiousBeliefMapper) {
    this.religiousBeliefRepository = religiousBeliefRepository;
    this.religiousBeliefMapper = religiousBeliefMapper;
  }

  /**
   * POST  /religious-beliefs : Create a new religiousBelief.
   *
   * @param religiousBeliefDTO the religiousBeliefDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new religiousBeliefDTO, or with status 400 (Bad Request) if the religiousBelief has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/religious-beliefs")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<ReligiousBeliefDTO> createReligiousBelief(@Valid @RequestBody ReligiousBeliefDTO religiousBeliefDTO) throws URISyntaxException {
    log.debug("REST request to save ReligiousBelief : {}", religiousBeliefDTO);
    if (religiousBeliefDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new religiousBelief cannot already have an ID")).body(null);
    }
    ReligiousBelief religiousBelief = religiousBeliefMapper.religiousBeliefDTOToReligiousBelief(religiousBeliefDTO);
    religiousBelief = religiousBeliefRepository.save(religiousBelief);
    ReligiousBeliefDTO result = religiousBeliefMapper.religiousBeliefToReligiousBeliefDTO(religiousBelief);
    return ResponseEntity.created(new URI("/api/religious-beliefs/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /religious-beliefs : Updates an existing religiousBelief.
   *
   * @param religiousBeliefDTO the religiousBeliefDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated religiousBeliefDTO,
   * or with status 400 (Bad Request) if the religiousBeliefDTO is not valid,
   * or with status 500 (Internal Server Error) if the religiousBeliefDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/religious-beliefs")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<ReligiousBeliefDTO> updateReligiousBelief(@Valid @RequestBody ReligiousBeliefDTO religiousBeliefDTO) throws URISyntaxException {
    log.debug("REST request to update ReligiousBelief : {}", religiousBeliefDTO);
    if (religiousBeliefDTO.getId() == null) {
      return createReligiousBelief(religiousBeliefDTO);
    }
    ReligiousBelief religiousBelief = religiousBeliefMapper.religiousBeliefDTOToReligiousBelief(religiousBeliefDTO);
    religiousBelief = religiousBeliefRepository.save(religiousBelief);
    ReligiousBeliefDTO result = religiousBeliefMapper.religiousBeliefToReligiousBeliefDTO(religiousBelief);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, religiousBeliefDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /religious-beliefs : get all the religiousBeliefs.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of religiousBeliefs in body
   * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
   */
  @GetMapping("/religious-beliefs")
  @Timed
  public ResponseEntity<List<ReligiousBeliefDTO>> getAllReligiousBeliefs(@ApiParam Pageable pageable) {
    log.debug("REST request to get a page of ReligiousBeliefs");
    Page<ReligiousBelief> page = religiousBeliefRepository.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/religious-beliefs");
    return new ResponseEntity<>(religiousBeliefMapper.religiousBeliefsToReligiousBeliefDTOs(page.getContent()), headers, HttpStatus.OK);
  }

  /**
   * GET  /current/religious-beliefs : get all the religiousBeliefs.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of religiousBeliefs in body
   * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
   */
  @GetMapping("/current/religious-beliefs")
  @Timed
  public ResponseEntity<List<ReligiousBeliefDTO>> getAllCurrentReligiousBeliefs(@ApiParam Pageable pageable) {
    log.debug("REST request to get a page of current ReligiousBeliefs");
    ReligiousBelief religiousBelief = new ReligiousBelief();
    religiousBelief.setStatus(Status.CURRENT);
    Page<ReligiousBelief> page = religiousBeliefRepository.findAll(Example.of(religiousBelief), pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/current/religious-beliefs");
    return new ResponseEntity<>(religiousBeliefMapper.religiousBeliefsToReligiousBeliefDTOs(page.getContent()), headers, HttpStatus.OK);
  }

  /**
   * GET  /religious-beliefs/:id : get the "id" religiousBelief.
   *
   * @param id the id of the religiousBeliefDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the religiousBeliefDTO, or with status 404 (Not Found)
   */
  @GetMapping("/religious-beliefs/{id}")
  @Timed
  public ResponseEntity<ReligiousBeliefDTO> getReligiousBelief(@PathVariable Long id) {
    log.debug("REST request to get ReligiousBelief : {}", id);
    ReligiousBelief religiousBelief = religiousBeliefRepository.findOne(id);
    ReligiousBeliefDTO religiousBeliefDTO = religiousBeliefMapper.religiousBeliefToReligiousBeliefDTO(religiousBelief);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(religiousBeliefDTO));
  }

  /**
   * DELETE  /religious-beliefs/:id : delete the "id" religiousBelief.
   *
   * @param id the id of the religiousBeliefDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/religious-beliefs/{id}")
  @Timed
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteReligiousBelief(@PathVariable Long id) {
    log.debug("REST request to delete ReligiousBelief : {}", id);
    religiousBeliefRepository.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }


  /**
   * POST  /bulk-religious-beliefs : Bulk create a new religious-beliefs.
   *
   * @param religiousBeliefDTOS List of the religiousBeliefDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new religiousBeliefDTOS, or with status 400 (Bad Request) if the ReligiousBeliefDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-religious-beliefs")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<ReligiousBeliefDTO>> bulkCreateReligiousBelief(@Valid @RequestBody List<ReligiousBeliefDTO> religiousBeliefDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save ReligiousBelief : {}", religiousBeliefDTOS);
    if (!Collections.isEmpty(religiousBeliefDTOS)) {
      List<Long> entityIds = religiousBeliefDTOS.stream()
          .filter(rb -> rb.getId() != null)
          .map(rb -> rb.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new religiousBeliefs cannot already have an ID")).body(null);
      }
    }
    List<ReligiousBelief> religiousBeliefs = religiousBeliefMapper.religiousBeliefDTOsToReligiousBeliefs(religiousBeliefDTOS);
    religiousBeliefs = religiousBeliefRepository.save(religiousBeliefs);
    List<ReligiousBeliefDTO> result = religiousBeliefMapper.religiousBeliefsToReligiousBeliefDTOs(religiousBeliefs);
    List<Long> ids = result.stream().map(rb -> rb.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }

  /**
   * PUT  /bulk-religious-beliefs : Updates an existing religious-beliefs.
   *
   * @param religiousBeliefDTOS List of the religiousBeliefDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated religiousBeliefDTOS,
   * or with status 400 (Bad Request) if the religiousBeliefDTOS is not valid,
   * or with status 500 (Internal Server Error) if the religiousBeliefDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-religious-beliefs")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<ReligiousBeliefDTO>> bulkUpdateReligiousBelief(@Valid @RequestBody List<ReligiousBeliefDTO> religiousBeliefDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update ReligiousBeliefDtos : {}", religiousBeliefDTOS);
    if (Collections.isEmpty(religiousBeliefDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(religiousBeliefDTOS)) {
      List<ReligiousBeliefDTO> entitiesWithNoId = religiousBeliefDTOS.stream().filter(rb -> rb.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<ReligiousBelief> religiousBeliefs = religiousBeliefMapper.religiousBeliefDTOsToReligiousBeliefs(religiousBeliefDTOS);
    religiousBeliefs = religiousBeliefRepository.save(religiousBeliefs);
    List<ReligiousBeliefDTO> results = religiousBeliefMapper.religiousBeliefsToReligiousBeliefDTOs(religiousBeliefs);
    List<Long> ids = results.stream().map(rb -> rb.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }
}
