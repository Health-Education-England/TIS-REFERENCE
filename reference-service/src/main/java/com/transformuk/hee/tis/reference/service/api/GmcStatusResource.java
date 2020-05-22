package com.transformuk.hee.tis.reference.service.api;

import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.in;
import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.isEqual;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.GmcStatusDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.GmcStatus;
import com.transformuk.hee.tis.reference.service.repository.GmcStatusRepository;
import com.transformuk.hee.tis.reference.service.service.impl.GmcStatusServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.GmcStatusMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
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
import uk.nhs.tis.StringConverter;

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
  private final GmcStatusServiceImpl gmcStatusService;

  public GmcStatusResource(GmcStatusRepository gmcStatusRepository, GmcStatusMapper gmcStatusMapper,
      GmcStatusServiceImpl gmcStatusService) {
    this.gmcStatusRepository = gmcStatusRepository;
    this.gmcStatusMapper = gmcStatusMapper;
    this.gmcStatusService = gmcStatusService;
  }

  /**
   * POST  /gmc-statuses : Create a new gmcStatus.
   *
   * @param gmcStatusDTO the gmcStatusDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new gmcStatusDTO, or
   *     with status 400 (Bad Request) if the gmcStatus has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/gmc-statuses")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<GmcStatusDTO> createGmcStatus(@Valid @RequestBody GmcStatusDTO gmcStatusDTO)
      throws URISyntaxException {
    log.debug("REST request to save GmcStatus : {}", gmcStatusDTO);
    if (gmcStatusDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil
          .createFailureAlert(ENTITY_NAME, "idexists", "A new gmcStatus cannot already have an ID"))
          .body(null);
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
   * @return the ResponseEntity with status 200 (OK) and with body the updated gmcStatusDTO, or with
   *     status 400 (Bad Request) if the gmcStatusDTO is not valid, or with status 500 (Internal
   *     Server Error) if the gmcStatusDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/gmc-statuses")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<GmcStatusDTO> updateGmcStatus(@Valid @RequestBody GmcStatusDTO gmcStatusDTO)
      throws URISyntaxException {
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
   * GET  /gmc-statuses : get all gmc statuses.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of gmc statuses in body
   */
  @ApiOperation(value = "Lists gmc statuses",
      notes = "Returns a list of gmc statuses with support for pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "country list")})
  @GetMapping("/gmc-statuses")
  @Timed
  public ResponseEntity<List<GmcStatusDTO>> getAllGmcStatuses(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson)
      throws IOException {
    log.info("REST request to get a page of gmc statuses begin");
    searchQuery = StringConverter.getConverter(searchQuery).fromJson().decodeUrl().escapeForSql()
        .toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil
        .getColumnFilters(columnFilterJson, filterEnumList);
    Page<GmcStatus> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = gmcStatusRepository.findAll(pageable);
    } else {
      page = gmcStatusService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<GmcStatusDTO> results = page.map(gmcStatusMapper::gmcStatusToGmcStatusDTO);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/countries");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /gmc-statuses/:id : get the "id" gmcStatus.
   *
   * @param id the id of the gmcStatusDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the gmcStatusDTO, or with status
   *     404 (Not Found)
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
   * EXISTS /gmc-statuses/exists/ : check is gmcStatus exists
   *
   * @param code             the code of the GmcStatusDTO to check
   * @param columnFilterJson The column filters to apply
   * @return boolean true if exists otherwise false
   */
  @PostMapping("/gmc-statuses/exists/")
  @Timed
  public ResponseEntity<Boolean> gmcStatusExists(@RequestBody String code,
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson)
      throws IOException {
    log.debug("REST request to check GmcStatus exists : {}", code);
    Specifications<GmcStatus> specs = Specifications.where(isEqual("code", code));

    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters =
        ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);

    for (ColumnFilter columnFilter : columnFilters) {
      specs = specs.and(in(columnFilter.getName(), columnFilter.getValues()));
    }

    boolean exists = gmcStatusRepository.findOne(specs) != null;
    return new ResponseEntity<>(exists, HttpStatus.OK);
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
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * POST  /bulk-gmc-statuses : Bulk create a new gmc-statuses.
   *
   * @param gmcStatusDTOS List of the gmcStatusDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new gmcStatusDTOS, or
   *     with status 400 (Bad Request) if the GmcStatusDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-gmc-statuses")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<GmcStatusDTO>> bulkCreateGmcStatus(
      @Valid @RequestBody List<GmcStatusDTO> gmcStatusDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save gmcstatus : {}", gmcStatusDTOS);
    if (!Collections.isEmpty(gmcStatusDTOS)) {
      List<Long> entityIds = gmcStatusDTOS.stream()
          .filter(gmcStatusDTO -> gmcStatusDTO.getId() != null)
          .map(GmcStatusDTO::getId)
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil
            .createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist",
                "A new gmcStatuses cannot already have an ID")).body(null);
      }
    }
    List<GmcStatus> gmcStatuses = gmcStatusMapper.gmcStatusDTOsToGmcStatuses(gmcStatusDTOS);
    gmcStatuses = gmcStatusRepository.save(gmcStatuses);
    List<GmcStatusDTO> result = gmcStatusMapper.gmcStatusesToGmcStatusDTOs(gmcStatuses);
    return ResponseEntity.ok()
        .body(result);
  }

  /**
   * PUT  /bulk-gmc-statuses : Updates an existing gmc-statuses.
   *
   * @param gmcStatusDTOS List of the gmcStatusDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated gmcStatusDTOS, or
   *     with status 400 (Bad Request) if the gmcStatusDTOS is not valid, or with status 500
   *     (Internal Server Error) if the gmcStatusDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-gmc-statuses")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<GmcStatusDTO>> bulkUpdateGmcStatus(
      @Valid @RequestBody List<GmcStatusDTO> gmcStatusDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update GmcStatus : {}", gmcStatusDTOS);
    if (Collections.isEmpty(gmcStatusDTOS)) {
      return ResponseEntity.badRequest()
          .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
              "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(gmcStatusDTOS)) {
      List<GmcStatusDTO> entitiesWithNoId = gmcStatusDTOS.stream()
          .filter(status -> status.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest()
            .headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
                "bulk.update.failed.noId",
                "Some DTOs you've provided have no Id, cannot update entities that dont exist"))
            .body(entitiesWithNoId);
      }
    }
    List<GmcStatus> gmcStatuses = gmcStatusMapper.gmcStatusDTOsToGmcStatuses(gmcStatusDTOS);
    gmcStatuses = gmcStatusRepository.save(gmcStatuses);
    List<GmcStatusDTO> results = gmcStatusMapper.gmcStatusesToGmcStatusDTOs(gmcStatuses);
    return ResponseEntity.ok()
        .body(results);
  }

}
