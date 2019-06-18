package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.ProgrammeMembershipTypeDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.ProgrammeMembershipType;
import com.transformuk.hee.tis.reference.service.repository.ProgrammeMembershipTypeRepository;
import com.transformuk.hee.tis.reference.service.service.impl.ProgrammeMembershipTypeServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.ProgrammeMembershipTypeMapper;
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
 * REST controller for managing ProgrammeMembershipType.
 */
@RestController
@RequestMapping("/api")
public class ProgrammeMembershipTypeResource {

  private static final String ENTITY_NAME = "programmeMembershipType";
  private final Logger log = LoggerFactory.getLogger(ProgrammeMembershipTypeResource.class);

  private final ProgrammeMembershipTypeRepository programmeMembershipTypeRepository;
  private final ProgrammeMembershipTypeMapper programmeMembershipTypeMapper;
  private final ProgrammeMembershipTypeServiceImpl programmeMembershipTypeService;

  public ProgrammeMembershipTypeResource(ProgrammeMembershipTypeRepository programmeMembershipTypeRepository,
                                         ProgrammeMembershipTypeMapper programmeMembershipTypeMapper,
                                         ProgrammeMembershipTypeServiceImpl programmeMembershipTypeService) {
    this.programmeMembershipTypeRepository = programmeMembershipTypeRepository;
    this.programmeMembershipTypeMapper = programmeMembershipTypeMapper;
    this.programmeMembershipTypeService = programmeMembershipTypeService;
  }

  /**
   * POST  /programme-membership-types : Create a new programmeMembershipType.
   *
   * @param programmeMembershipTypeDTO the programmeMembershipTypeDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new programmeMembershipTypeDTO, or with status 400 (Bad Request) if the programmeMembershipType has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/programme-membership-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<ProgrammeMembershipTypeDTO> createProgrammeMembershipType(@Valid @RequestBody ProgrammeMembershipTypeDTO programmeMembershipTypeDTO) throws URISyntaxException {
    log.debug("REST request to save ProgrammeMembershipType : {}", programmeMembershipTypeDTO);
    if (programmeMembershipTypeDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new programmeMembershipType cannot already have an ID")).body(null);
    }
    ProgrammeMembershipType programmeMembershipType = programmeMembershipTypeMapper.programmeMembershipTypeDTOToProgrammeMembershipType(programmeMembershipTypeDTO);
    programmeMembershipType = programmeMembershipTypeRepository.save(programmeMembershipType);
    ProgrammeMembershipTypeDTO result = programmeMembershipTypeMapper.programmeMembershipTypeToProgrammeMembershipTypeDTO(programmeMembershipType);
    return ResponseEntity.created(new URI("/api/programme-membership-types/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /programme-membership-types : Updates an existing programmeMembershipType.
   *
   * @param programmeMembershipTypeDTO the programmeMembershipTypeDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated programmeMembershipTypeDTO,
   * or with status 400 (Bad Request) if the programmeMembershipTypeDTO is not valid,
   * or with status 500 (Internal Server Error) if the programmeMembershipTypeDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/programme-membership-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<ProgrammeMembershipTypeDTO> updateProgrammeMembershipType(@Valid @RequestBody ProgrammeMembershipTypeDTO programmeMembershipTypeDTO) throws URISyntaxException {
    log.debug("REST request to update ProgrammeMembershipType : {}", programmeMembershipTypeDTO);
    if (programmeMembershipTypeDTO.getId() == null) {
      return createProgrammeMembershipType(programmeMembershipTypeDTO);
    }
    ProgrammeMembershipType programmeMembershipType = programmeMembershipTypeMapper.programmeMembershipTypeDTOToProgrammeMembershipType(programmeMembershipTypeDTO);
    programmeMembershipType = programmeMembershipTypeRepository.save(programmeMembershipType);
    ProgrammeMembershipTypeDTO result = programmeMembershipTypeMapper.programmeMembershipTypeToProgrammeMembershipTypeDTO(programmeMembershipType);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, programmeMembershipTypeDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /programme-membership-types : get programme membership types.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of programme membership types in body
   */
  @ApiOperation(value = "Lists programme membership types",
      notes = "Returns a list of programme membership types with support for pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "programme membership types list")})
  @GetMapping("/programme-membership-types")
  @Timed
  public ResponseEntity<List<ProgrammeMembershipTypeDTO>> getAllProgrammeMembershipTypes(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.info("REST request to get a page of programme membership types begin");
    searchQuery = StringConverter.getConverter(searchQuery).fromJson().decodeUrl().escapeForSql().toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    Page<ProgrammeMembershipType> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = programmeMembershipTypeRepository.findAll(pageable);
    } else {
      page = programmeMembershipTypeService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<ProgrammeMembershipTypeDTO> results = page.map(programmeMembershipTypeMapper::programmeMembershipTypeToProgrammeMembershipTypeDTO);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/programme-membership-types");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }


  /**
   * GET  /programme-membership-types/:id : get the "id" programmeMembershipType.
   *
   * @param id the id of the programmeMembershipTypeDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the programmeMembershipTypeDTO, or with status 404 (Not Found)
   */
  @GetMapping("/programme-membership-types/{id}")
  @Timed
  public ResponseEntity<ProgrammeMembershipTypeDTO> getProgrammeMembershipType(@PathVariable Long id) {
    log.debug("REST request to get ProgrammeMembershipType : {}", id);
    ProgrammeMembershipType programmeMembershipType = programmeMembershipTypeRepository.findOne(id);
    ProgrammeMembershipTypeDTO programmeMembershipTypeDTO = programmeMembershipTypeMapper.programmeMembershipTypeToProgrammeMembershipTypeDTO(programmeMembershipType);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(programmeMembershipTypeDTO));
  }

  /**
   * DELETE  /programme-membership-types/:id : delete the "id" programmeMembershipType.
   *
   * @param id the id of the programmeMembershipTypeDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/programme-membership-types/{id}")
  @Timed
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteProgrammeMembershipType(@PathVariable Long id) {
    log.debug("REST request to delete ProgrammeMembershipType : {}", id);
    programmeMembershipTypeRepository.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }


  /**
   * POST  /bulk-programme-membership-types : Bulk create a new programme-membership-types.
   *
   * @param programmeMembershipTypeDTOS List of the programmeMembershipTypeDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new programmeMembershipTypeDTOS, or with status 400 (Bad Request) if the ProgrammeMembershipTypeDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-programme-membership-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<ProgrammeMembershipTypeDTO>> bulkCreateProgrammeMembershipType(@Valid @RequestBody List<ProgrammeMembershipTypeDTO> programmeMembershipTypeDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save ProgrammeMembershipTypeDtos : {}", programmeMembershipTypeDTOS);
    if (!Collections.isEmpty(programmeMembershipTypeDTOS)) {
      List<Long> entityIds = programmeMembershipTypeDTOS.stream()
          .filter(pmt -> pmt.getId() != null)
          .map(pmt -> pmt.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new programmeMembershipTypes cannot already have an ID")).body(null);
      }
    }
    List<ProgrammeMembershipType> programmeMembershipTypes = programmeMembershipTypeMapper.programmeMembershipTypeDTOsToProgrammeMembershipTypes(programmeMembershipTypeDTOS);
    programmeMembershipTypes = programmeMembershipTypeRepository.save(programmeMembershipTypes);
    List<ProgrammeMembershipTypeDTO> result = programmeMembershipTypeMapper.programmeMembershipTypesToProgrammeMembershipTypeDTOs(programmeMembershipTypes);
    return ResponseEntity.ok()
        .body(result);
  }

  /**
   * PUT  /bulk-programme-membership-types : Updates an existing programme-membership-types.
   *
   * @param programmeMembershipTypeDTOS List of the programmeMembershipTypeDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated programmeMembershipTypeDTOS,
   * or with status 400 (Bad Request) if the programmeMembershipTypeDTOS is not valid,
   * or with status 500 (Internal Server Error) if the programmeMembershipTypeDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-programme-membership-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<ProgrammeMembershipTypeDTO>> bulkUpdateProgrammeMembershipType(@Valid @RequestBody List<ProgrammeMembershipTypeDTO> programmeMembershipTypeDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update ProgrammeMembershipTypeDtos : {}", programmeMembershipTypeDTOS);
    if (Collections.isEmpty(programmeMembershipTypeDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(programmeMembershipTypeDTOS)) {
      List<ProgrammeMembershipTypeDTO> entitiesWithNoId = programmeMembershipTypeDTOS.stream().filter(pmt -> pmt.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<ProgrammeMembershipType> programmeMembershipTypes = programmeMembershipTypeMapper.programmeMembershipTypeDTOsToProgrammeMembershipTypes(programmeMembershipTypeDTOS);
    programmeMembershipTypes = programmeMembershipTypeRepository.save(programmeMembershipTypes);
    List<ProgrammeMembershipTypeDTO> results = programmeMembershipTypeMapper.programmeMembershipTypesToProgrammeMembershipTypeDTOs(programmeMembershipTypes);
    return ResponseEntity.ok()
        .body(results);
  }

}
