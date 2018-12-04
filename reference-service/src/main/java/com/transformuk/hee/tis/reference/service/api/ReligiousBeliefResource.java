package com.transformuk.hee.tis.reference.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.ReligiousBeliefDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.ReligiousBelief;
import com.transformuk.hee.tis.reference.service.repository.ReligiousBeliefRepository;
import com.transformuk.hee.tis.reference.service.service.impl.ReligiousBeliefServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.ReligiousBeliefMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
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
 * REST controller for managing ReligiousBelief.
 */
@RestController
@RequestMapping("/api")
public class ReligiousBeliefResource {

  private static final String ENTITY_NAME = "religiousBelief";
  private final Logger log = LoggerFactory.getLogger(ReligiousBeliefResource.class);

  private final ReligiousBeliefRepository religiousBeliefRepository;
  private final ReligiousBeliefMapper religiousBeliefMapper;
  private final ReligiousBeliefServiceImpl religiousBeliefsService;

  public ReligiousBeliefResource(ReligiousBeliefRepository religiousBeliefRepository,
                                 ReligiousBeliefMapper religiousBeliefMapper,
                                 ReligiousBeliefServiceImpl religiousBeliefsService) {
    this.religiousBeliefRepository = religiousBeliefRepository;
    this.religiousBeliefMapper = religiousBeliefMapper;
    this.religiousBeliefsService = religiousBeliefsService;
  }

  /**
   * POST  /religious-beliefs : Create a new religiousBelief.
   *
   * @param religiousBeliefDTO the religiousBeliefDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new religiousBeliefDTO, or with status 400 (Bad Request) if the religiousBelief has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/religious-beliefs")
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
   * GET  /religious-beliefs : get all religious beliefs.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of religious beliefs in body
   */
  @GetMapping("/religious-beliefs")
  public ResponseEntity<List<ReligiousBeliefDTO>> getAllReligiousBeliefs(
          Pageable pageable,
          @RequestParam(value = "searchQuery", required = false) String searchQuery,
          @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.info("REST request to get a page of religious beliefs begin");
    searchQuery = sanitize(searchQuery);
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    Page<ReligiousBelief> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = religiousBeliefRepository.findAll(pageable);
    } else {
      page = religiousBeliefsService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<ReligiousBeliefDTO> results = page.map(religiousBeliefMapper::religiousBeliefToReligiousBeliefDTO);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/religious-beliefs");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }


  /**
   * GET  /religious-beliefs/:id : get the "id" religiousBelief.
   *
   * @param id the id of the religiousBeliefDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the religiousBeliefDTO, or with status 404 (Not Found)
   */
  @GetMapping("/religious-beliefs/{id}")
  public ResponseEntity<ReligiousBeliefDTO> getReligiousBelief(@PathVariable Long id) {
    log.debug("REST request to get ReligiousBelief : {}", id);
      ReligiousBelief religiousBelief = religiousBeliefRepository.findById(id).orElse(null);
    ReligiousBeliefDTO religiousBeliefDTO = religiousBeliefMapper.religiousBeliefToReligiousBeliefDTO(religiousBelief);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(religiousBeliefDTO));
  }

  /**
   * EXISTS /religious-beliefs/exists/ : check is religiousBelief exists
   *
   * @param code the code of the religiousBeliefDTO to check
   * @return boolean true if exists otherwise false
   */
  @PostMapping("/religious-beliefs/exists/")
  public ResponseEntity<Boolean> religiousBeliefExists(@RequestBody String code) {
    log.debug("REST request to check ReligiousBelief exists : {}", code);
    ReligiousBelief religiousBelief = religiousBeliefRepository.findFirstByCode(code);
    if(religiousBelief == null){
      return new ResponseEntity<>(false, HttpStatus.OK);
    }
    return new ResponseEntity<>(true, HttpStatus.OK);
  }

  /**
   * DELETE  /religious-beliefs/:id : delete the "id" religiousBelief.
   *
   * @param id the id of the religiousBeliefDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/religious-beliefs/{id}")
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteReligiousBelief(@PathVariable Long id) {
    log.debug("REST request to delete ReligiousBelief : {}", id);
      religiousBeliefRepository.deleteById(id);
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
      religiousBeliefs = religiousBeliefRepository.saveAll(religiousBeliefs);
    List<ReligiousBeliefDTO> result = religiousBeliefMapper.religiousBeliefsToReligiousBeliefDTOs(religiousBeliefs);
    return ResponseEntity.ok()
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
      religiousBeliefs = religiousBeliefRepository.saveAll(religiousBeliefs);
    List<ReligiousBeliefDTO> results = religiousBeliefMapper.religiousBeliefsToReligiousBeliefDTOs(religiousBeliefs);
    return ResponseEntity.ok()
        .body(results);
  }
}
