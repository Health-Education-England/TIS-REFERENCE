package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.CurriculumSubTypeDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.CurriculumSubType;
import com.transformuk.hee.tis.reference.service.repository.CurriculumSubTypeRepository;
import com.transformuk.hee.tis.reference.service.service.CurriculumSubTypeService;
import com.transformuk.hee.tis.reference.service.service.mapper.CurriculumSubTypeMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiParam;
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

import static com.transformuk.hee.tis.reference.service.api.util.StringUtil.sanitize;

/**
 * REST controller for managing CurriculumSubType.
 */
@RestController
@RequestMapping("/api")
public class CurriculumSubTypeResource {

  private static final String ENTITY_NAME = "curriculumSubType";
  private final Logger log = LoggerFactory.getLogger(CurriculumSubTypeResource.class);
  private final CurriculumSubTypeRepository curriculumSubTypeRepository;

  private final CurriculumSubTypeMapper curriculumSubTypeMapper;
  private final CurriculumSubTypeService curriculumSubTypeService;

  public CurriculumSubTypeResource(CurriculumSubTypeRepository curriculumSubTypeRepository,
                                   CurriculumSubTypeMapper curriculumSubTypeMapper,
                                   CurriculumSubTypeService curriculumSubTypeService) {
    this.curriculumSubTypeRepository = curriculumSubTypeRepository;
    this.curriculumSubTypeMapper = curriculumSubTypeMapper;
    this.curriculumSubTypeService = curriculumSubTypeService;
  }

  /**
   * POST  /curriculum-sub-types : Create a new curriculumSubType.
   *
   * @param curriculumSubTypeDTO the curriculumSubTypeDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new curriculumSubTypeDTO, or with status 400 (Bad Request) if the curriculumSubType has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/curriculum-sub-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<CurriculumSubTypeDTO> createCurriculumSubType(@Valid @RequestBody CurriculumSubTypeDTO curriculumSubTypeDTO) throws URISyntaxException {
    log.debug("REST request to save CurriculumSubType : {}", curriculumSubTypeDTO);
    if (curriculumSubTypeDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new curriculumSubType cannot already have an ID")).body(null);
    }
    CurriculumSubType curriculumSubType = curriculumSubTypeMapper.curriculumSubTypeDTOToCurriculumSubType(curriculumSubTypeDTO);
    curriculumSubType = curriculumSubTypeRepository.save(curriculumSubType);
    CurriculumSubTypeDTO result = curriculumSubTypeMapper.curriculumSubTypeToCurriculumSubTypeDTO(curriculumSubType);
    return ResponseEntity.created(new URI("/api/curriculum-sub-types/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /curriculum-sub-types : Updates an existing curriculumSubType.
   *
   * @param curriculumSubTypeDTO the curriculumSubTypeDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated curriculumSubTypeDTO,
   * or with status 400 (Bad Request) if the curriculumSubTypeDTO is not valid,
   * or with status 500 (Internal Server Error) if the curriculumSubTypeDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/curriculum-sub-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<CurriculumSubTypeDTO> updateCurriculumSubType(@Valid @RequestBody CurriculumSubTypeDTO curriculumSubTypeDTO) throws URISyntaxException {
    log.debug("REST request to update CurriculumSubType : {}", curriculumSubTypeDTO);
    if (curriculumSubTypeDTO.getId() == null) {
      return createCurriculumSubType(curriculumSubTypeDTO);
    }
    CurriculumSubType curriculumSubType = curriculumSubTypeMapper.curriculumSubTypeDTOToCurriculumSubType(curriculumSubTypeDTO);
    curriculumSubType = curriculumSubTypeRepository.save(curriculumSubType);
    CurriculumSubTypeDTO result = curriculumSubTypeMapper.curriculumSubTypeToCurriculumSubTypeDTO(curriculumSubType);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, curriculumSubTypeDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /curriculum-sub-types : get all the curriculumSubTypes.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of curriculumSubTypes in body
   */
  @GetMapping("/curriculum-sub-types")
  @Timed
  public ResponseEntity<List<CurriculumSubTypeDTO>> getAllCurriculumSubTypes(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.debug("REST request to get all CurriculumSubTypes");
    searchQuery = sanitize(searchQuery);
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);

    Page<CurriculumSubTypeDTO> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = curriculumSubTypeService.findAll(pageable);
    } else {
      page = curriculumSubTypeService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/curriculum-sub-types");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /curriculum-sub-types/:id : get the "id" curriculumSubType.
   *
   * @param id the id of the curriculumSubTypeDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the curriculumSubTypeDTO, or with status 404 (Not Found)
   */
  @GetMapping("/curriculum-sub-types/{id}")
  @Timed
  public ResponseEntity<CurriculumSubTypeDTO> getCurriculumSubType(@PathVariable Long id) {
    log.debug("REST request to get CurriculumSubType : {}", id);
    CurriculumSubType curriculumSubType = curriculumSubTypeRepository.findOne(id);
    CurriculumSubTypeDTO curriculumSubTypeDTO = curriculumSubTypeMapper.curriculumSubTypeToCurriculumSubTypeDTO(curriculumSubType);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(curriculumSubTypeDTO));
  }

  /**
   * GET  /curriculum-sub-types/:code : get curriculumSubType by code.
   *
   * @param code the code of the curriculumSubTypeDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the curriculumSubTypeDTO, or with status 404 (Not Found)
   */
  @GetMapping("/curriculum-sub-types/code/{code}")
  @Timed
  public ResponseEntity<CurriculumSubTypeDTO> getCurriculumSubTypeByCode(@PathVariable String code) {
    log.debug("REST request to get CurriculumSubType code: [{}]", code);
    CurriculumSubType curriculumSubType = curriculumSubTypeRepository.findByCode(code);
    CurriculumSubTypeDTO curriculumSubTypeDTO = curriculumSubTypeMapper.curriculumSubTypeToCurriculumSubTypeDTO(curriculumSubType);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(curriculumSubTypeDTO));
  }

  /**
   * DELETE  /curriculum-sub-types/:id : delete the "id" curriculumSubType.
   *
   * @param id the id of the curriculumSubTypeDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/curriculum-sub-types/{id}")
  @Timed
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteCurriculumSubType(@PathVariable Long id) {
    log.debug("REST request to delete CurriculumSubType : {}", id);
    curriculumSubTypeRepository.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }


  /**
   * POST  /bulk-curriculum-sub-types : Bulk create a new curriculumSubTypes.
   *
   * @param curriculumSubTypeDTOs List of the curriculumSubTypeDTOs to create
   * @return the ResponseEntity with status 200 (Created) and with body the new curriculumSubTypeDTOs, or with status 400 (Bad Request) if the curriculumSubType has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-curriculum-sub-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<CurriculumSubTypeDTO>> bulkCreateCurriculumSubType(@Valid @RequestBody List<CurriculumSubTypeDTO> curriculumSubTypeDTOs) throws URISyntaxException {
    log.debug("REST request to bulk save CurriculumSubType : {}", curriculumSubTypeDTOs);
    if (!Collections.isEmpty(curriculumSubTypeDTOs)) {
      List<Long> entityIds = curriculumSubTypeDTOs.stream()
          .filter(cst -> cst.getId() != null)
          .map(cst -> cst.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new curriculumSubTypes cannot already have an ID")).body(null);
      }
    }
    List<CurriculumSubType> curriculumSubTypes = curriculumSubTypeMapper.curriculumSubTypeDTOsToCurriculumSubTypes(curriculumSubTypeDTOs);
    curriculumSubTypes = curriculumSubTypeRepository.save(curriculumSubTypes);
    List<CurriculumSubTypeDTO> result = curriculumSubTypeMapper.curriculumSubTypesToCurriculumSubTypeDTOs(curriculumSubTypes);
    return ResponseEntity.ok()
        .body(result);
  }

  /**
   * PUT  /bulk-curriculum-sub-types : Updates an existing curriculumSubType.
   *
   * @param curriculumSubTypeDTOs List of the curriculumSubTypeDTOs to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated curriculumSubTypeDTOs,
   * or with status 400 (Bad Request) if the curriculumSubTypeDTOs is not valid,
   * or with status 500 (Internal Server Error) if the curriculumSubTypeDTOs couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-curriculum-sub-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<CurriculumSubTypeDTO>> bulkUpdateCurriculumSubType(@Valid @RequestBody List<CurriculumSubTypeDTO> curriculumSubTypeDTOs) throws URISyntaxException {
    log.debug("REST request to bulk update CurriculumSubType : {}", curriculumSubTypeDTOs);
    if (Collections.isEmpty(curriculumSubTypeDTOs)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(curriculumSubTypeDTOs)) {
      List<CurriculumSubTypeDTO> entitiesWithNoId = curriculumSubTypeDTOs.stream().filter(cst -> cst.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<CurriculumSubType> curriculumSubTypes = curriculumSubTypeMapper.curriculumSubTypeDTOsToCurriculumSubTypes(curriculumSubTypeDTOs);
    curriculumSubTypes = curriculumSubTypeRepository.save(curriculumSubTypes);
    List<CurriculumSubTypeDTO> results = curriculumSubTypeMapper.curriculumSubTypesToCurriculumSubTypeDTOs(curriculumSubTypes);
    return ResponseEntity.ok()
        .body(results);
  }
}
