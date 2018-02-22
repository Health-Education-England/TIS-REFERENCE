package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.CountryDTO;
import com.transformuk.hee.tis.reference.api.dto.GdcStatusDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.Country;
import com.transformuk.hee.tis.reference.service.model.GdcStatus;
import com.transformuk.hee.tis.reference.service.repository.GdcStatusRepository;
import com.transformuk.hee.tis.reference.service.service.impl.GdcStatusServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.GdcStatusMapper;
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
 * REST controller for managing GdcStatus.
 */
@RestController
@RequestMapping("/api")
public class GdcStatusResource {

  private static final String ENTITY_NAME = "gdcStatus";
  private final Logger log = LoggerFactory.getLogger(GdcStatusResource.class);

  private final GdcStatusRepository gdcStatusRepository;
  private final GdcStatusMapper gdcStatusMapper;
  private final GdcStatusServiceImpl gdcStatusService;

  public GdcStatusResource(GdcStatusRepository gdcStatusRepository, GdcStatusMapper gdcStatusMapper,
                           GdcStatusServiceImpl gdcStatusService) {
    this.gdcStatusRepository = gdcStatusRepository;
    this.gdcStatusMapper = gdcStatusMapper;
    this.gdcStatusService = gdcStatusService;
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
   * GET  /gdc-statuses : get all gdc statuses.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of gdc statuses in body
   */
  @ApiOperation(value = "Lists gdc statuses",
      notes = "Returns a list of gdc statuses with support for pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "gdc statuses list")})
  @GetMapping("/gdc-statuses")
  @Timed
  public ResponseEntity<List<GdcStatusDTO>> getAllGdcStatuses(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.info("REST request to get a page of gdc statuses begin");
    searchQuery = sanitize(searchQuery);
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    Page<GdcStatus> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = gdcStatusRepository.findAll(pageable);
    } else {
      page = gdcStatusService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<GdcStatusDTO> results = page.map(gdcStatusMapper::gdcStatusToGdcStatusDTO);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/gdc-statuses");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
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
