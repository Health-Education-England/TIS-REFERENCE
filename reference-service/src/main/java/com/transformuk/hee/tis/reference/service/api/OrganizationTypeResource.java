package com.transformuk.hee.tis.reference.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.OrganizationTypeDto;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.OrganizationType;
import com.transformuk.hee.tis.reference.service.service.impl.OrganizationTypeServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.OrganizationTypeMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import uk.nhs.tis.StringConverter;

/**
 * REST controller for managing OrganizationType.
 */
@Slf4j
@RestController
@RequestMapping("/api/organization-types")
public class OrganizationTypeResource {

  private static final String ENTITY_NAME = "organizationType";

  private OrganizationTypeMapper mapper;
  private OrganizationTypeServiceImpl service;

  public OrganizationTypeResource(OrganizationTypeMapper mapper,
      OrganizationTypeServiceImpl service) {
    this.mapper = mapper;
    this.service = service;
  }

  /**
   * POST : Create a new organization type.
   *
   * @param dto the organization type to create.
   * @return the ResponseEntity with status 201 (Created) and with body the new dto, or with status
   *     400 (Bad Request) if the organization type has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<OrganizationTypeDto> createOrganizationType(
      @Valid @RequestBody OrganizationTypeDto dto)
      throws URISyntaxException {
    log.debug("REST request to save OrganizationType : {}", dto);
    if (dto.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil
          .createFailureAlert(ENTITY_NAME, "idexists", "A new entity cannot already have an ID"))
          .body(null);
    }
    OrganizationType entity = mapper.toEntity(dto);
    entity = service.save(entity);
    OrganizationTypeDto result = mapper.toDto(entity);
    return ResponseEntity.created(new URI("/api/organization-types/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT : Updates an existing organization type.
   *
   * @param dto the organization type to update.
   * @return the ResponseEntity with status 200 (OK) and with body the updated dto, or with status
   *     400 (Bad Request) if the dto is not valid, or with status 500 (Internal Server Error) if
   *     the dto couldnt be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<OrganizationTypeDto> updateOrganizationType(
      @Valid @RequestBody OrganizationTypeDto dto)
      throws URISyntaxException {
    log.debug("REST request to update OrganizationType : {}", dto);
    if (dto.getId() == null) {
      return createOrganizationType(dto);
    }
    OrganizationType entity = mapper.toEntity(dto);
    entity = service.save(entity);
    OrganizationTypeDto result = mapper.toDto(entity);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dto.getId().toString()))
        .body(result);
  }

  /**
   * GET : get all organization types.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of organization types in body
   */
  @ApiOperation(value = "Lists organization types",
      notes = "Returns a list of organization types with support for pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "organization type list")})
  @GetMapping
  public ResponseEntity<List<OrganizationTypeDto>> getAllOrganizationTypes(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson)
      throws IOException {
    log.info("REST request to get a page of assessment types begin");
    searchQuery = StringConverter.getConverter(searchQuery).fromJson().decodeUrl().escapeForSql()
        .toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil
        .getColumnFilters(columnFilterJson, filterEnumList);
    Page<OrganizationType> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = service.findAll(pageable);
    } else {
      page = service.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<OrganizationTypeDto> results = page.map(mapper::toDto);
    HttpHeaders headers = PaginationUtil
        .generatePaginationHttpHeaders(page, "/api/organization-types");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET /:id : get the "id" organization type.
   *
   * @param id the id of the OrganizationTypeDTO to retrieve.
   * @return the ResponseEntity with status 200 (OK) and with body the OrganizationTypeDTO, or with
   *     status 404 (Not Found).
   */
  @GetMapping("/{id}")
  public ResponseEntity<OrganizationTypeDto> getOrganizationType(@PathVariable Long id) {
    log.debug("REST request to get OrganizationType : {}", id);
    OrganizationType entity = service.findById(id).orElse(null);
    OrganizationTypeDto dto = mapper.toDto(entity);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dto));
  }

  /**
   * DELETE /:id : delete the "id" organization type.
   *
   * @param id the id of the organization type to delete.
   * @return the ResponseEntity with status 200 (OK).
   */
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteOrganizationType(@PathVariable Long id) {
    log.debug("REST request to delete OrganizationType : {}", id);
    service.deleteById(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }
}
