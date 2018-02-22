package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import com.transformuk.hee.tis.reference.api.dto.EthnicOriginDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.DBC;
import com.transformuk.hee.tis.reference.service.model.EthnicOrigin;
import com.transformuk.hee.tis.reference.service.repository.EthnicOriginRepository;
import com.transformuk.hee.tis.reference.service.service.impl.EthnicOriginServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.EthnicOriginMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.transformuk.hee.tis.reference.service.api.util.StringUtil.sanitize;

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
  private final EthnicOriginServiceImpl ethnicOriginService;

  public EthnicOriginResource(EthnicOriginRepository ethnicOriginRepository, EthnicOriginMapper ethnicOriginMapper,
                              EthnicOriginServiceImpl ethnicOriginService) {
    this.ethnicOriginRepository = ethnicOriginRepository;
    this.ethnicOriginMapper = ethnicOriginMapper;
    this.ethnicOriginService = ethnicOriginService;
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
   * GET  /ethnic-origins : get all ethnic origins.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of ethnic origins in body
   */
  @ApiOperation(value = "Lists ethnic origins",
      notes = "Returns a list of ethnic origins with support for pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "ethnic origins list")})
  @GetMapping("/ethnic-origins")
  @Timed
  public ResponseEntity<List<EthnicOriginDTO>> getAllEthnicOrigins(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.info("REST request to get a page of ethnic origins begin");
    searchQuery = sanitize(searchQuery);
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    Page<EthnicOrigin> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = ethnicOriginRepository.findAll(pageable);
    } else {
      page = ethnicOriginService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<EthnicOriginDTO> results = page.map(ethnicOriginMapper::ethnicOriginToEthnicOriginDTO);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ethnic-origins");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
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
    return ResponseEntity.ok()
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
    return ResponseEntity.ok()
        .body(results);
  }
}
