package com.transformuk.hee.tis.reference.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.FundingReasonDto;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.FundingReason;
import com.transformuk.hee.tis.reference.service.service.impl.FundingReasonServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.FundingReasonMapper;
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
 * REST controller for managing FundingReason.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class FundingReasonResource {

  private static final String ENTITY_NAME = "fundingReason";

  private final FundingReasonMapper fundingReasonMapper;
  private final FundingReasonServiceImpl fundingReasonService;

  /**
   * Constructor for FundingReasonResource.
   *
   * @param fundingReasonMapper    the mapper to convert between entity and dto
   * @param fundingReasonService   the service to handle business logic
   */
  public FundingReasonResource(FundingReasonMapper fundingReasonMapper,
      FundingReasonServiceImpl fundingReasonService) {
    this.fundingReasonMapper = fundingReasonMapper;
    this.fundingReasonService = fundingReasonService;
  }

  /**
   * POST  /funding-reason : Create a new fundingReason.
   *
   * @param fundingReasonDto the fundingReasonDto to create
   * @return the ResponseEntity with status 201 (Created) and with body the new fundingReasonDTO,
   *     or with status 400 (Bad Request) if the fundingReason has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/funding-reason")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<FundingReasonDto> createFundingReason(
      @Valid @RequestBody FundingReasonDto fundingReasonDto) throws URISyntaxException {
    log.debug("REST request to save FundingReason : {}", fundingReasonDto);
    if (fundingReasonDto.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil
          .createFailureAlert(ENTITY_NAME, "idexists",
              "A new fundingReason cannot already have an id")).body(null);
    }
    FundingReason fundingReason = fundingReasonMapper.toEntity(fundingReasonDto);
    fundingReason = fundingReasonService.save(fundingReason);
    FundingReasonDto result = fundingReasonMapper.toDto(fundingReason);
    return ResponseEntity.created(new URI("/api/funding-reason/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /funding-reason : Updates an existing fundingReason.
   *
   * @param fundingReasonDto the fundingReasonDto to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated fundingReasonDto, or
   *     with status 400 (Bad Request) if the fundingReasonDto is not valid, or with status 500
   *     (Internal Server Error) if the fundingReasonDto couldn't be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/funding-reason")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<FundingReasonDto> updateFundingReason(
      @Valid @RequestBody FundingReasonDto fundingReasonDto) throws URISyntaxException {
    log.debug("REST request to update FundingReason : {}", fundingReasonDto);
    if (fundingReasonDto.getId() == null) {
      return createFundingReason(fundingReasonDto);
    }
    FundingReason fundingReason = fundingReasonMapper.toEntity(fundingReasonDto);
    fundingReason = fundingReasonService.save(fundingReason);
    FundingReasonDto result = fundingReasonMapper.toDto(fundingReason);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * DELETE  /funding-reason/:id : delete the "id" fundingReason.
   *
   * @param id the id of the fundingReasonDto to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/funding-reason/{id}")
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteFundingReason(@PathVariable UUID id) {
    log.debug("REST request to delete FundingReason : {}", id.toString());
    fundingReasonService.deleteById(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * GET  /funding-reason/:id : get the "id" fundingReason.
   *
   * @param id the id of the fundingReasonDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the fundingReasonDTO, or with
   *     status 404 (Not Found)
   */
  @GetMapping("/funding-reason/{id}")
  public ResponseEntity<FundingReasonDto> getFundingReason(@PathVariable UUID id) {
    log.debug("REST request to get FundingReason : {}", id.toString());
    FundingReason fundingReason = fundingReasonService.findById(id)
        .orElse(null);
    FundingReasonDto fundingReasonDto = fundingReasonMapper.toDto(fundingReason);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fundingReasonDto));
  }

  /**
   * GET  /funding-reason : get all funding reasons.
   *
   * @param pageable the pagination information.
   * @return the ResponseEntity with status 200 (OK) and the list of colleges in body
   */
  @ApiOperation(value = "Lists funding reasons",
      notes = "Returns a list of funding reasons with support for "
          + "pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "funding reasons list")})
  @GetMapping("/funding-reason")
  public ResponseEntity<List<FundingReasonDto>> getAllFundingReasons(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. "
          + "(Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson)
      throws IOException {
    log.info("REST request to get a page of funding reasons begin");
    searchQuery = StringConverter.getConverter(searchQuery).fromJson().decodeUrl().escapeForSql()
        .toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil
        .getColumnFilters(columnFilterJson, filterEnumList);
    Page<FundingReason> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = fundingReasonService.findAll(pageable);
    } else {
      page = fundingReasonService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<FundingReasonDto> results = page.map(fundingReasonMapper::toDto);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
        "/api/funding-reason");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }
}
