package com.transformuk.hee.tis.reference.service.api;

import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.in;
import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.isEqual;
import static uk.nhs.tis.StringConverter.getConverter;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.MaritalStatusDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.MaritalStatus;
import com.transformuk.hee.tis.reference.service.repository.MaritalStatusRepository;
import com.transformuk.hee.tis.reference.service.service.impl.MaritalStatusServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.MaritalStatusMapper;
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
import org.springframework.data.jpa.domain.Specification;
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

/**
 * REST controller for managing MaritalStatus.
 */
@RestController
@RequestMapping("/api")
public class MaritalStatusResource {

  private static final String ENTITY_NAME = "maritalStatus";
  private final Logger log = LoggerFactory.getLogger(MaritalStatusResource.class);

  private final MaritalStatusRepository maritalStatusRepository;
  private final MaritalStatusMapper maritalStatusMapper;
  private final MaritalStatusServiceImpl maritalStatusService;

  public MaritalStatusResource(MaritalStatusRepository maritalStatusRepository,
      MaritalStatusMapper maritalStatusMapper,
      MaritalStatusServiceImpl maritalStatusService) {
    this.maritalStatusRepository = maritalStatusRepository;
    this.maritalStatusMapper = maritalStatusMapper;
    this.maritalStatusService = maritalStatusService;
  }

  /**
   * POST  /marital-statuses : Create a new maritalStatus.
   *
   * @param maritalStatusDTO the maritalStatusDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new maritalStatusDTO, or
   *     with status 400 (Bad Request) if the maritalStatus has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/marital-statuses")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<MaritalStatusDTO> createMaritalStatus(
      @Valid @RequestBody MaritalStatusDTO maritalStatusDTO) throws URISyntaxException {
    log.debug("REST request to save MaritalStatus : {}", maritalStatusDTO);
    if (maritalStatusDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil
          .createFailureAlert(ENTITY_NAME, "idexists",
              "A new maritalStatus cannot already have an ID")).body(null);
    }
    MaritalStatus maritalStatus = maritalStatusMapper
        .maritalStatusDTOToMaritalStatus(maritalStatusDTO);
    maritalStatus = maritalStatusRepository.save(maritalStatus);
    MaritalStatusDTO result = maritalStatusMapper.maritalStatusToMaritalStatusDTO(maritalStatus);
    return ResponseEntity.created(new URI("/api/marital-statuses/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /marital-statuses : Updates an existing maritalStatus.
   *
   * @param maritalStatusDTO the maritalStatusDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated maritalStatusDTO, or
   *     with status 400 (Bad Request) if the maritalStatusDTO is not valid, or with status 500
   *     (Internal Server Error) if the maritalStatusDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/marital-statuses")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<MaritalStatusDTO> updateMaritalStatus(
      @Valid @RequestBody MaritalStatusDTO maritalStatusDTO) throws URISyntaxException {
    log.debug("REST request to update MaritalStatus : {}", maritalStatusDTO);
    if (maritalStatusDTO.getId() == null) {
      return createMaritalStatus(maritalStatusDTO);
    }
    MaritalStatus maritalStatus = maritalStatusMapper
        .maritalStatusDTOToMaritalStatus(maritalStatusDTO);
    maritalStatus = maritalStatusRepository.save(maritalStatus);
    MaritalStatusDTO result = maritalStatusMapper.maritalStatusToMaritalStatusDTO(maritalStatus);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, maritalStatusDTO.getId().toString()))
        .body(result);
  }


  /**
   * GET  /marital-statuses : get all marital statuses.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of marital statuses in body
   */
  @ApiOperation(value = "Lists marital statuses",
      notes = "Returns a list of marital statuses with support for pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "marital statuses list")})
  @GetMapping("/marital-statuses")
  public ResponseEntity<List<MaritalStatusDTO>> getAllMaritalStatuses(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson)
      throws IOException {
    log.info("REST request to get a page of marital statuses begin");
    searchQuery = getConverter(searchQuery).fromJson().decodeUrl().escapeForSql().toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil
        .getColumnFilters(columnFilterJson, filterEnumList);
    Page<MaritalStatus> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = maritalStatusRepository.findAll(pageable);
    } else {
      page = maritalStatusService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<MaritalStatusDTO> results = page.map(maritalStatusMapper::maritalStatusToMaritalStatusDTO);
    HttpHeaders headers = PaginationUtil
        .generatePaginationHttpHeaders(page, "/api/marital-statuses");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /marital-statuses/:id : get the "id" maritalStatus.
   *
   * @param id the id of the maritalStatusDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the maritalStatusDTO, or with
   *     status 404 (Not Found)
   */
  @GetMapping("/marital-statuses/{id}")
  public ResponseEntity<MaritalStatusDTO> getMaritalStatus(@PathVariable Long id) {
    log.debug("REST request to get MaritalStatus : {}", id);
    MaritalStatus maritalStatus = maritalStatusRepository.findById(id).orElse(null);
    MaritalStatusDTO maritalStatusDTO = maritalStatusMapper
        .maritalStatusToMaritalStatusDTO(maritalStatus);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(maritalStatusDTO));
  }

  /**
   * EXISTS /marital-statuses/exists/ : check is maritalStatus exists
   *
   * @param code             the code of the maritalStatusDTO to check
   * @param columnFilterJson The column filters to apply
   * @return boolean true if exists otherwise false
   */
  @PostMapping("/marital-statuses/exists/")
  public ResponseEntity<Boolean> maritalStatusExists(@RequestBody String code,
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson)
      throws IOException {
    code = getConverter(code).decodeUrl().toString();
    log.debug("REST request to check MaritalStatus exists : {}", code);
    Specification<MaritalStatus> specs = Specification.where(isEqual("code", code));

    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters =
        ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);

    for (ColumnFilter columnFilter : columnFilters) {
      specs = specs.and(in(columnFilter.getName(), columnFilter.getValues()));
    }

    boolean exists = maritalStatusRepository.findOne(specs).isPresent();
    return new ResponseEntity<>(exists, HttpStatus.OK);
  }

  /**
   * DELETE  /marital-statuses/:id : delete the "id" maritalStatus.
   *
   * @param id the id of the maritalStatusDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/marital-statuses/{id}")
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteMaritalStatus(@PathVariable Long id) {
    log.debug("REST request to delete MaritalStatus : {}", id);
    maritalStatusRepository.deleteById(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * POST  /bulk-marital-statuses : Bulk create a new marital-statuses.
   *
   * @param maritalStatusDTOS List of the maritalStatusDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new maritalStatusDTOS,
   *     or with status 400 (Bad Request) if the MaritalStatusDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-marital-statuses")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<MaritalStatusDTO>> bulkCreateMaritalStatus(
      @Valid @RequestBody List<MaritalStatusDTO> maritalStatusDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save MaritalStatus : {}", maritalStatusDTOS);
    if (!Collections.isEmpty(maritalStatusDTOS)) {
      List<Long> entityIds = maritalStatusDTOS.stream()
          .filter(maritalStatusDTO -> maritalStatusDTO.getId() != null)
          .map(maritalStatusDTO -> maritalStatusDTO.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil
            .createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist",
                "A new maritalStatuses cannot already have an ID")).body(null);
      }
    }
    List<MaritalStatus> maritalStatuses = maritalStatusMapper
        .maritalStatusDTOsToMaritalStatuses(maritalStatusDTOS);
    maritalStatuses = maritalStatusRepository.saveAll(maritalStatuses);
    List<MaritalStatusDTO> result = maritalStatusMapper
        .maritalStatusesToMaritalStatusDTOs(maritalStatuses);
    return ResponseEntity.ok()
        .body(result);
  }

  /**
   * PUT  /bulk-marital-statuses : Updates an existing marital-statuses.
   *
   * @param maritalStatusDTOS List of the maritalStatusDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated maritalStatusDTOS, or
   *     with status 400 (Bad Request) if the maritalStatusDTOS is not valid, or with status 500
   *     (Internal Server Error) if the maritalStatusDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-marital-statuses")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<MaritalStatusDTO>> bulkUpdateMaritalStatus(
      @Valid @RequestBody List<MaritalStatusDTO> maritalStatusDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update MaritalStatus : {}", maritalStatusDTOS);
    if (Collections.isEmpty(maritalStatusDTOS)) {
      return ResponseEntity.badRequest()
          .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
              "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(maritalStatusDTOS)) {
      List<MaritalStatusDTO> entitiesWithNoId = maritalStatusDTOS.stream()
          .filter(maritalStatusDTO -> maritalStatusDTO.getId() == null)
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest()
            .headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
                "bulk.update.failed.noId",
                "Some DTOs you've provided have no Id, cannot update entities that dont exist"))
            .body(entitiesWithNoId);
      }
    }
    List<MaritalStatus> maritalStatuses = maritalStatusMapper
        .maritalStatusDTOsToMaritalStatuses(maritalStatusDTOS);
    maritalStatuses = maritalStatusRepository.saveAll(maritalStatuses);
    List<MaritalStatusDTO> results = maritalStatusMapper
        .maritalStatusesToMaritalStatusDTOs(maritalStatuses);
    return ResponseEntity.ok()
        .body(results);
  }

}
