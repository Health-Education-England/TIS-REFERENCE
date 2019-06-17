package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.InactiveReasonDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.InactiveReason;
import com.transformuk.hee.tis.reference.service.repository.InactiveReasonRepository;
import com.transformuk.hee.tis.reference.service.service.impl.InactiveReasonServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.InactiveReasonMapper;
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
 * REST controller for managing InactiveReason.
 */
@RestController
@RequestMapping("/api")
public class InactiveReasonResource {

  private static final String ENTITY_NAME = "inactiveReason";
  private final Logger log = LoggerFactory.getLogger(InactiveReasonResource.class);

  private final InactiveReasonRepository inactiveReasonRepository;
  private final InactiveReasonMapper inactiveReasonMapper;
  private final InactiveReasonServiceImpl inactiveReasonService;

  public InactiveReasonResource(InactiveReasonRepository inactiveReasonRepository, InactiveReasonMapper inactiveReasonMapper,
                                InactiveReasonServiceImpl inactiveReasonService) {
    this.inactiveReasonRepository = inactiveReasonRepository;
    this.inactiveReasonMapper = inactiveReasonMapper;
    this.inactiveReasonService = inactiveReasonService;
  }

  /**
   * POST  /inactive-reasons : Create a new inactiveReason.
   *
   * @param inactiveReasonDTO the inactiveReasonDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new inactiveReasonDTO, or with status 400 (Bad Request) if the inactiveReason has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/inactive-reasons")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<InactiveReasonDTO> createInactiveReason(@Valid @RequestBody InactiveReasonDTO inactiveReasonDTO) throws URISyntaxException {
    log.debug("REST request to save InactiveReason : {}", inactiveReasonDTO);
    if (inactiveReasonDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new inactiveReason cannot already have an ID")).body(null);
    }
    InactiveReason inactiveReason = inactiveReasonMapper.inactiveReasonDTOToInactiveReason(inactiveReasonDTO);
    inactiveReason = inactiveReasonRepository.save(inactiveReason);
    InactiveReasonDTO result = inactiveReasonMapper.inactiveReasonToInactiveReasonDTO(inactiveReason);
    return ResponseEntity.created(new URI("/api/inactive-reasons/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /inactive-reasons : Updates an existing inactiveReason.
   *
   * @param inactiveReasonDTO the inactiveReasonDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated inactiveReasonDTO,
   * or with status 400 (Bad Request) if the inactiveReasonDTO is not valid,
   * or with status 500 (Internal Server Error) if the inactiveReasonDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/inactive-reasons")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<InactiveReasonDTO> updateInactiveReason(@Valid @RequestBody InactiveReasonDTO inactiveReasonDTO) throws URISyntaxException {
    log.debug("REST request to update InactiveReason : {}", inactiveReasonDTO);
    if (inactiveReasonDTO.getId() == null) {
      return createInactiveReason(inactiveReasonDTO);
    }
    InactiveReason inactiveReason = inactiveReasonMapper.inactiveReasonDTOToInactiveReason(inactiveReasonDTO);
    inactiveReason = inactiveReasonRepository.save(inactiveReason);
    InactiveReasonDTO result = inactiveReasonMapper.inactiveReasonToInactiveReasonDTO(inactiveReason);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, inactiveReasonDTO.getId().toString()))
        .body(result);
  }


  /**
   * GET  /inactive-reasons : get all inactive reasons.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of inactive reasons in body
   */
  @ApiOperation(value = "Lists inactive reasons",
      notes = "Returns a list of inactive reasons with support for pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "country list")})
  @GetMapping("/inactive-reasons")
  @Timed
  public ResponseEntity<List<InactiveReasonDTO>> getAllInactiveReasons(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.info("REST request to get a page of inactive reasons begin");
    searchQuery = StringConverter.getConverter(searchQuery).decodeUrl().fromJson().escapeForSql().toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    Page<InactiveReason> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = inactiveReasonRepository.findAll(pageable);
    } else {
      page = inactiveReasonService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<InactiveReasonDTO> results = page.map(inactiveReasonMapper::inactiveReasonToInactiveReasonDTO);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/inactive-reasons");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }


  /**
   * GET  /inactive-reasons/:id : get the "id" inactiveReason.
   *
   * @param id the id of the inactiveReasonDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the inactiveReasonDTO, or with status 404 (Not Found)
   */
  @GetMapping("/inactive-reasons/{id}")
  @Timed
  public ResponseEntity<InactiveReasonDTO> getInactiveReason(@PathVariable Long id) {
    log.debug("REST request to get InactiveReason : {}", id);
    InactiveReason inactiveReason = inactiveReasonRepository.findOne(id);
    InactiveReasonDTO inactiveReasonDTO = inactiveReasonMapper.inactiveReasonToInactiveReasonDTO(inactiveReason);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(inactiveReasonDTO));
  }

  /**
   * DELETE  /inactive-reasons/:id : delete the "id" inactiveReason.
   *
   * @param id the id of the inactiveReasonDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/inactive-reasons/{id}")
  @Timed
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteInactiveReason(@PathVariable Long id) {
    log.debug("REST request to delete InactiveReason : {}", id);
    inactiveReasonRepository.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * POST  /bulk-inactive-reasons : Bulk create a new inactive-reasons.
   *
   * @param inactiveReasonDTOS List of the inactiveReasonDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new inactiveReasonDTOS, or with status 400 (Bad Request) if the InactiveReasonDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-inactive-reasons")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<InactiveReasonDTO>> bulkCreateInactiveReason(@Valid @RequestBody List<InactiveReasonDTO> inactiveReasonDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save InactiveReason : {}", inactiveReasonDTOS);
    if (!Collections.isEmpty(inactiveReasonDTOS)) {
      List<Long> entityIds = inactiveReasonDTOS.stream()
          .filter(inactiveReasonDTO -> inactiveReasonDTO.getId() != null)
          .map(inactiveReasonDTO -> inactiveReasonDTO.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new inactiveReasons cannot already have an ID")).body(null);
      }
    }
    List<InactiveReason> inactiveReasons = inactiveReasonMapper.inactiveReasonDTOsToInactiveReasons(inactiveReasonDTOS);
    inactiveReasons = inactiveReasonRepository.save(inactiveReasons);
    List<InactiveReasonDTO> result = inactiveReasonMapper.inactiveReasonsToInactiveReasonDTOs(inactiveReasons);
    return ResponseEntity.ok()
        .body(result);
  }

  /**
   * PUT  /bulk-inactive-reasons : Updates an existing inactive-reasons.
   *
   * @param inactiveReasonDTOS List of the inactiveReasonDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated inactiveReasonDTOS,
   * or with status 400 (Bad Request) if the inactiveReasonDTOS is not valid,
   * or with status 500 (Internal Server Error) if the inactiveReasonDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-inactive-reasons")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<InactiveReasonDTO>> bulkUpdateInactiveReason(@Valid @RequestBody List<InactiveReasonDTO> inactiveReasonDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update InactiveReason : {}", inactiveReasonDTOS);
    if (Collections.isEmpty(inactiveReasonDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(inactiveReasonDTOS)) {
      List<InactiveReasonDTO> entitiesWithNoId = inactiveReasonDTOS.stream().filter(inactiveReasonDTO -> inactiveReasonDTO.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<InactiveReason> inactiveReasons = inactiveReasonMapper.inactiveReasonDTOsToInactiveReasons(inactiveReasonDTOS);
    inactiveReasons = inactiveReasonRepository.save(inactiveReasons);
    List<InactiveReasonDTO> results = inactiveReasonMapper.inactiveReasonsToInactiveReasonDTOs(inactiveReasons);
    return ResponseEntity.ok()
        .body(results);
  }
}
