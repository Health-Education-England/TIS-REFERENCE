package com.transformuk.hee.tis.reference.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.FundingSubTypeDto;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.api.validation.FundingSubTypeValidator;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.FundingSubType;
import com.transformuk.hee.tis.reference.service.service.impl.FundingSubTypeServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.FundingSubTypeMapper;
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
import java.util.UUID;
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
 * REST controller for managing FundingType.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class FundingSubTypeResource {

  private static final String ENTITY_NAME = "fundingSubType";

  private final FundingSubTypeMapper fundingSubTypeMapper;
  private final FundingSubTypeServiceImpl fundingSubTypeService;
  private final FundingSubTypeValidator fundingSubTypeValidator;

  /**
   * Constructor for FundingSubTypeResource.
   *
   * @param fundingSubTypeMapper the mapper to convert between entity and dto
   * @param fundingSubTypeService the service to handle business logic
   * @param fundingSubTypeValidator the validator to validate user data
   */
  public FundingSubTypeResource(FundingSubTypeMapper fundingSubTypeMapper,
      FundingSubTypeServiceImpl fundingSubTypeService,
      FundingSubTypeValidator fundingSubTypeValidator) {
    this.fundingSubTypeMapper = fundingSubTypeMapper;
    this.fundingSubTypeService = fundingSubTypeService;
    this.fundingSubTypeValidator = fundingSubTypeValidator;
  }

  /**
   * POST  /funding-sub-type : Create a new fundingType.
   *
   * @param fundingSubTypeDto the fundingSubTypeDto to create
   * @return the ResponseEntity with status 201 (Created) and with body the new fundingSubTypeDTO,
   *     or with status 400 (Bad Request) if the fundingSubType has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/funding-sub-type")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<FundingSubTypeDto> createFundingSubType(
      @Valid @RequestBody FundingSubTypeDto fundingSubTypeDto) throws URISyntaxException {
    log.debug("REST request to save FundingType : {}", fundingSubTypeDto);
    fundingSubTypeValidator.validate(fundingSubTypeDto);
    if (fundingSubTypeDto.getUuid() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil
          .createFailureAlert(ENTITY_NAME, "idexists",
              "A new fundingSubType cannot already have an uuid")).body(null);
    }
    FundingSubType fundingSubType = fundingSubTypeMapper.toEntity(fundingSubTypeDto);
    fundingSubType = fundingSubTypeService.save(fundingSubType);
    FundingSubTypeDto result = fundingSubTypeMapper.toDto(fundingSubType);
    return ResponseEntity.created(new URI("/api/funding-sub-type/" + result.getUuid()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getUuid().toString()))
        .body(result);
  }

  /**
   * PUT  /funding-sub-type : Updates an existing fundingType.
   *
   * @param fundingSubTypeDto the fundingSubTypeDto to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated fundingSubTypeDto, or
   *     with status 400 (Bad Request) if the fundingSubTypeDto is not valid, or with status 500
   *     (Internal Server Error) if the fundingSubTypeDto couldn't be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/funding-sub-type")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<FundingSubTypeDto> updateFundingSubType(
      @Valid @RequestBody FundingSubTypeDto fundingSubTypeDto) throws URISyntaxException {
    log.debug("REST request to update FundingSubType : {}", fundingSubTypeDto);
    fundingSubTypeValidator.validate(fundingSubTypeDto);
    if (fundingSubTypeDto.getUuid() == null) {
      return createFundingSubType(fundingSubTypeDto);
    }
    FundingSubType fundingSubType = fundingSubTypeMapper.toEntity(fundingSubTypeDto);
    fundingSubType = fundingSubTypeService.save(fundingSubType);
    FundingSubTypeDto result = fundingSubTypeMapper.toDto(fundingSubType);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fundingSubTypeDto.getUuid().toString()))
        .body(result);
  }

  /**
   * DELETE  /funding-sub-type/:uuid : delete the "uuid" fundingSubType.
   *
   * @param uuid the uuid of the fundingSubTypeDto to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/funding-sub-type/{uuid}")
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteFundingSubType(@PathVariable UUID uuid) {
    log.debug("REST request to delete FundingSubType : {}", uuid.toString());
    fundingSubTypeService.deleteById(uuid);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, uuid.toString())).build();
  }

  /**
   * GET  /funding-sub-type/:uuid : get the "uuid" fundingSubType.
   *
   * @param uuid the uuid of the fundingSubTypeDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the fundingSubTypeDTO, or with
   *     status 404 (Not Found)
   */
  @GetMapping("/funding-sub-type/{uuid}")
  public ResponseEntity<FundingSubTypeDto> getFundingSubType(@PathVariable UUID uuid) {
    log.debug("REST request to get FundingType : {}", uuid.toString());
    FundingSubType fundingSubType = fundingSubTypeService.findById(uuid)
        .orElse(null);
    FundingSubTypeDto fundingSubTypeDto = fundingSubTypeMapper.toDto(fundingSubType);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fundingSubTypeDto));
  }

  /**
   * GET  /funding-sub-types : get all funding sub types.
   *
   * @param pageable the pagination information. Please note: if you want to sort by id, use uuid
   *                 instead
   * @return the ResponseEntity with status 200 (OK) and the list of colleges in body
   */
  @ApiOperation(value = "Lists funding sub types",
      notes = "Returns a list of funding sub types with support for "
          + "pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "funding sub types list")})
  @GetMapping("/funding-sub-types")
  public ResponseEntity<List<FundingSubTypeDto>> getAllFundingSubTypes(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. "
          + "(Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson)
      throws IOException {
    log.info("REST request to get a page of funding types begin");
    searchQuery = StringConverter.getConverter(searchQuery).fromJson().decodeUrl().escapeForSql()
        .toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil
        .getColumnFilters(columnFilterJson, filterEnumList);
    Page<FundingSubType> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = fundingSubTypeService.findAll(pageable);
    } else {
      page = fundingSubTypeService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<FundingSubTypeDto> results = page.map(fundingSubTypeMapper::toDto);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
        "/api/funding-sub-types");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }
}
