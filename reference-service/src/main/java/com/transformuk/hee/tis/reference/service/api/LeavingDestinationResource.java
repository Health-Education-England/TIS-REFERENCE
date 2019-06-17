package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.LeavingDestinationDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.LeavingDestination;
import com.transformuk.hee.tis.reference.service.repository.LeavingDestinationRepository;
import com.transformuk.hee.tis.reference.service.service.impl.LeavingDestinationServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.LeavingDestinationMapper;
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
 * REST controller for managing LeavingDestination.
 */
@RestController
@RequestMapping("/api")
public class LeavingDestinationResource {

  private static final String ENTITY_NAME = "leavingDestination";
  private final Logger log = LoggerFactory.getLogger(LeavingDestinationResource.class);

  private final LeavingDestinationRepository leavingDestinationRepository;
  private final LeavingDestinationMapper leavingDestinationMapper;
  private final LeavingDestinationServiceImpl leavingDestinationService;

  public LeavingDestinationResource(LeavingDestinationRepository leavingDestinationRepository,
                                    LeavingDestinationMapper leavingDestinationMapper,
                                    LeavingDestinationServiceImpl leavingDestinationService) {
    this.leavingDestinationRepository = leavingDestinationRepository;
    this.leavingDestinationMapper = leavingDestinationMapper;
    this.leavingDestinationService = leavingDestinationService;
  }

  /**
   * POST  /leaving-destinations : Create a new leavingDestination.
   *
   * @param leavingDestinationDTO the leavingDestinationDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new leavingDestinationDTO, or with status 400 (Bad Request) if the leavingDestination has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/leaving-destinations")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<LeavingDestinationDTO> createLeavingDestination(@Valid @RequestBody LeavingDestinationDTO leavingDestinationDTO) throws URISyntaxException {
    log.debug("REST request to save LeavingDestination : {}", leavingDestinationDTO);
    if (leavingDestinationDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new leavingDestination cannot already have an ID")).body(null);
    }
    LeavingDestination leavingDestination = leavingDestinationMapper.leavingDestinationDTOToLeavingDestination(leavingDestinationDTO);
    leavingDestination = leavingDestinationRepository.save(leavingDestination);
    LeavingDestinationDTO result = leavingDestinationMapper.leavingDestinationToLeavingDestinationDTO(leavingDestination);
    return ResponseEntity.created(new URI("/api/leaving-destinations/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /leaving-destinations : Updates an existing leavingDestination.
   *
   * @param leavingDestinationDTO the leavingDestinationDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated leavingDestinationDTO,
   * or with status 400 (Bad Request) if the leavingDestinationDTO is not valid,
   * or with status 500 (Internal Server Error) if the leavingDestinationDTO couldn't be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/leaving-destinations")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<LeavingDestinationDTO> updateLeavingDestination(@Valid @RequestBody LeavingDestinationDTO leavingDestinationDTO) throws URISyntaxException {
    log.debug("REST request to update LeavingDestination : {}", leavingDestinationDTO);
    if (leavingDestinationDTO.getId() == null) {
      return createLeavingDestination(leavingDestinationDTO);
    }
    LeavingDestination leavingDestination = leavingDestinationMapper.leavingDestinationDTOToLeavingDestination(leavingDestinationDTO);
    leavingDestination = leavingDestinationRepository.save(leavingDestination);
    LeavingDestinationDTO result = leavingDestinationMapper.leavingDestinationToLeavingDestinationDTO(leavingDestination);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, leavingDestinationDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /leaving-destinations : get all leaving destinations.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of leaving destinations in body
   */
  @ApiOperation(value = "Lists leaving destinations",
      notes = "Returns a list of leaving destinations with support for pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "country list")})
  @GetMapping("/leaving-destinations")
  @Timed
  public ResponseEntity<List<LeavingDestinationDTO>> getAllLeavingDestinations(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.info("REST request to get a page of leaving destinations begin");
    searchQuery = StringConverter.getConverter(searchQuery).decodeUrl().escapeForSql().toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    Page<LeavingDestination> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = leavingDestinationRepository.findAll(pageable);
    } else {
      page = leavingDestinationService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<LeavingDestinationDTO> results = page.map(leavingDestinationMapper::leavingDestinationToLeavingDestinationDTO);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/leaving-destinations");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }


  /**
   * GET  /leaving-destinations/:id : get the "id" leavingDestination.
   *
   * @param id the id of the leavingDestinationDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the leavingDestinationDTO, or with status 404 (Not Found)
   */
  @GetMapping("/leaving-destinations/{id}")
  @Timed
  public ResponseEntity<LeavingDestinationDTO> getLeavingDestination(@PathVariable Long id) {
    log.debug("REST request to get LeavingDestination : {}", id);
    LeavingDestination leavingDestination = leavingDestinationRepository.findOne(id);
    LeavingDestinationDTO leavingDestinationDTO = leavingDestinationMapper.leavingDestinationToLeavingDestinationDTO(leavingDestination);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(leavingDestinationDTO));
  }

  /**
   * DELETE  /leaving-destinations/:id : delete the "id" leavingDestination.
   *
   * @param id the id of the leavingDestinationDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/leaving-destinations/{id}")
  @Timed
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteLeavingDestination(@PathVariable Long id) {
    log.debug("REST request to delete LeavingDestination : {}", id);
    leavingDestinationRepository.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * POST  /bulk-leaving-destinations : Bulk create a new leaving-destinations.
   *
   * @param leavingDestinationDTOS List of the leavingDestinationDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new leavingDestinationDTOS, or with status 400 (Bad Request) if the LeavingDestinationDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-leaving-destinations")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<LeavingDestinationDTO>> bulkCreateLeavingDestination(@Valid @RequestBody List<LeavingDestinationDTO> leavingDestinationDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save LeavingDestination : {}", leavingDestinationDTOS);
    if (!Collections.isEmpty(leavingDestinationDTOS)) {
      List<Long> entityIds = leavingDestinationDTOS.stream()
          .filter(leavingDestinationDTO -> leavingDestinationDTO.getId() != null)
          .map(leavingDestinationDTO -> leavingDestinationDTO.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new leavingDestinations cannot already have an ID")).body(null);
      }
    }
    List<LeavingDestination> leavingDestinations = leavingDestinationMapper.leavingDestinationDTOsToLeavingDestinations(leavingDestinationDTOS);
    leavingDestinations = leavingDestinationRepository.save(leavingDestinations);
    List<LeavingDestinationDTO> result = leavingDestinationMapper.leavingDestinationsToLeavingDestinationDTOs(leavingDestinations);
    return ResponseEntity.ok()
        .body(result);
  }

  /**
   * PUT  /bulk-leaving-destinations : Updates an existing leaving-destinations.
   *
   * @param leavingDestinationDTOS List of the leavingDestinationDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated leavingDestinationDTOS,
   * or with status 400 (Bad Request) if the leavingDestinationDTOS is not valid,
   * or with status 500 (Internal Server Error) if the leavingDestinationDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-leaving-destinations")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<LeavingDestinationDTO>> bulkUpdateLeavingDestination(@Valid @RequestBody List<LeavingDestinationDTO> leavingDestinationDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update LeavingDestinationDtos : {}", leavingDestinationDTOS);
    if (Collections.isEmpty(leavingDestinationDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(leavingDestinationDTOS)) {
      List<LeavingDestinationDTO> entitiesWithNoId = leavingDestinationDTOS.stream().filter(leavingDestinationDTO -> leavingDestinationDTO.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<LeavingDestination> leavingDestinations = leavingDestinationMapper.leavingDestinationDTOsToLeavingDestinations(leavingDestinationDTOS);
    leavingDestinations = leavingDestinationRepository.save(leavingDestinations);
    List<LeavingDestinationDTO> results = leavingDestinationMapper.leavingDestinationsToLeavingDestinationDTOs(leavingDestinations);
    return ResponseEntity.ok()
        .body(results);
  }
}
