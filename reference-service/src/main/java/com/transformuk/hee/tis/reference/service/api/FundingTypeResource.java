package com.transformuk.hee.tis.reference.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.FundingType;
import com.transformuk.hee.tis.reference.service.repository.FundingTypeRepository;
import com.transformuk.hee.tis.reference.service.service.impl.FundingTypeServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.FundingTypeMapper;
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
 * REST controller for managing FundingType.
 */
@RestController
@RequestMapping("/api")
public class FundingTypeResource {

  private static final String ENTITY_NAME = "fundingType";
  private final Logger log = LoggerFactory.getLogger(FundingTypeResource.class);

  private final FundingTypeRepository fundingTypeRepository;
  private final FundingTypeMapper fundingTypeMapper;
  private final FundingTypeServiceImpl fundingTypeService;

  public FundingTypeResource(FundingTypeRepository fundingTypeRepository, FundingTypeMapper fundingTypeMapper,
                             FundingTypeServiceImpl fundingTypeService) {
    this.fundingTypeRepository = fundingTypeRepository;
    this.fundingTypeMapper = fundingTypeMapper;
    this.fundingTypeService = fundingTypeService;
  }

  /**
   * POST  /funding-types : Create a new fundingType.
   *
   * @param fundingTypeDTO the fundingTypeDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new fundingTypeDTO, or with status 400 (Bad Request) if the fundingType has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/funding-types")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<FundingTypeDTO> createFundingType(@Valid @RequestBody FundingTypeDTO fundingTypeDTO) throws URISyntaxException {
    log.debug("REST request to save FundingType : {}", fundingTypeDTO);
    if (fundingTypeDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new fundingType cannot already have an ID")).body(null);
    }
    FundingType fundingType = fundingTypeMapper.fundingTypeDTOToFundingType(fundingTypeDTO);
    fundingType = fundingTypeRepository.save(fundingType);
    FundingTypeDTO result = fundingTypeMapper.fundingTypeToFundingTypeDTO(fundingType);
    return ResponseEntity.created(new URI("/api/funding-types/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /funding-types : Updates an existing fundingType.
   *
   * @param fundingTypeDTO the fundingTypeDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated fundingTypeDTO,
   * or with status 400 (Bad Request) if the fundingTypeDTO is not valid,
   * or with status 500 (Internal Server Error) if the fundingTypeDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/funding-types")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<FundingTypeDTO> updateFundingType(@Valid @RequestBody FundingTypeDTO fundingTypeDTO) throws URISyntaxException {
    log.debug("REST request to update FundingType : {}", fundingTypeDTO);
    if (fundingTypeDTO.getId() == null) {
      return createFundingType(fundingTypeDTO);
    }
    FundingType fundingType = fundingTypeMapper.fundingTypeDTOToFundingType(fundingTypeDTO);
    fundingType = fundingTypeRepository.save(fundingType);
    FundingTypeDTO result = fundingTypeMapper.fundingTypeToFundingTypeDTO(fundingType);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fundingTypeDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /funding-types : get all funding types.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of colleges in body
   */
  @GetMapping("/funding-types")
  public ResponseEntity<List<FundingTypeDTO>> getAllFundingTypes(
          Pageable pageable,
          @RequestParam(value = "searchQuery", required = false) String searchQuery,
          @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.info("REST request to get a page of funding types begin");
    searchQuery = sanitize(searchQuery);
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    Page<FundingType> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = fundingTypeRepository.findAll(pageable);
    } else {
      page = fundingTypeService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<FundingTypeDTO> results = page.map(fundingTypeMapper::fundingTypeToFundingTypeDTO);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/funding-types");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }


  /**
   * GET  /funding-types/:id : get the "id" fundingType.
   *
   * @param id the id of the fundingTypeDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the fundingTypeDTO, or with status 404 (Not Found)
   */
  @GetMapping("/funding-types/{id}")
  public ResponseEntity<FundingTypeDTO> getFundingType(@PathVariable Long id) {
    log.debug("REST request to get FundingType : {}", id);
      FundingType fundingType = fundingTypeRepository.findById(id).orElse(null);
    FundingTypeDTO fundingTypeDTO = fundingTypeMapper.fundingTypeToFundingTypeDTO(fundingType);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fundingTypeDTO));
  }

  /**
   * DELETE  /funding-types/:id : delete the "id" fundingType.
   *
   * @param id the id of the fundingTypeDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/funding-types/{id}")
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteFundingType(@PathVariable Long id) {
    log.debug("REST request to delete FundingType : {}", id);
      fundingTypeRepository.deleteById(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * POST  /bulk-funding-types : Bulk create a new funding-types.
   *
   * @param fundingTypeDTOS List of the fundingTypeDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new fundingTypeDTOS, or with status 400 (Bad Request) if the FundingTypeDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-funding-types")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<FundingTypeDTO>> bulkCreateFundingType(@Valid @RequestBody List<FundingTypeDTO> fundingTypeDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save FundingTypeDTOs : {}", fundingTypeDTOS);
    if (!Collections.isEmpty(fundingTypeDTOS)) {
      List<Long> entityIds = fundingTypeDTOS.stream()
          .filter(fundingTypeDTO -> fundingTypeDTO.getId() != null)
          .map(fundingTypeDTO -> fundingTypeDTO.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new fundingTypes cannot already have an ID")).body(null);
      }
    }
    List<FundingType> fundingTypes = fundingTypeMapper.fundingTypeDTOsToFundingTypes(fundingTypeDTOS);
      fundingTypes = fundingTypeRepository.saveAll(fundingTypes);
    List<FundingTypeDTO> result = fundingTypeMapper.fundingTypesToFundingTypeDTOs(fundingTypes);
    return ResponseEntity.ok()
        .body(result);
  }

  /**
   * PUT  /bulk-funding-types : Updates an existing FundingType.
   *
   * @param fundingTypeDTOS List of the fundingTypeDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated fundingTypeDTOS,
   * or with status 400 (Bad Request) if the fundingTypeDTOS is not valid,
   * or with status 500 (Internal Server Error) if the fundingTypeDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-funding-types")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<FundingTypeDTO>> bulkUpdateFundingType(@Valid @RequestBody List<FundingTypeDTO> fundingTypeDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update AssessmentTypesDTO : {}", fundingTypeDTOS);
    if (Collections.isEmpty(fundingTypeDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(fundingTypeDTOS)) {
      List<FundingTypeDTO> entitiesWithNoId = fundingTypeDTOS.stream().filter(ft -> ft.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<FundingType> fundingTypes = fundingTypeMapper.fundingTypeDTOsToFundingTypes(fundingTypeDTOS);
      fundingTypes = fundingTypeRepository.saveAll(fundingTypes);
    List<FundingTypeDTO> results = fundingTypeMapper.fundingTypesToFundingTypeDTOs(fundingTypes);
    return ResponseEntity.ok()
        .body(results);
  }
}
