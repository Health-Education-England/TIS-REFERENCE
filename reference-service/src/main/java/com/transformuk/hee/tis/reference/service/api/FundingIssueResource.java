package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.FundingIssueDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.FundingIssue;
import com.transformuk.hee.tis.reference.service.repository.FundingIssueRepository;
import com.transformuk.hee.tis.reference.service.service.impl.FundingIssueServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.FundingIssueMapper;
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
 * REST controller for managing FundingIssue.
 */
@RestController
@RequestMapping("/api")
public class FundingIssueResource {

  private static final String ENTITY_NAME = "fundingIssue";
  private final Logger log = LoggerFactory.getLogger(FundingIssueResource.class);

  private final FundingIssueRepository fundingIssueRepository;
  private final FundingIssueMapper fundingIssueMapper;
  private final FundingIssueServiceImpl fundingIssueService;

  public FundingIssueResource(FundingIssueRepository fundingIssueRepository, FundingIssueMapper fundingIssueMapper,
                              FundingIssueServiceImpl fundingIssueService) {
    this.fundingIssueRepository = fundingIssueRepository;
    this.fundingIssueMapper = fundingIssueMapper;
    this.fundingIssueService = fundingIssueService;
  }

  /**
   * POST  /funding-issues : Create a new fundingIssue.
   *
   * @param fundingIssueDTO the fundingIssueDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new fundingIssueDTO, or with status 400 (Bad Request) if the fundingIssue has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/funding-issues")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<FundingIssueDTO> createFundingIssue(@Valid @RequestBody FundingIssueDTO fundingIssueDTO) throws URISyntaxException {
    log.debug("REST request to save FundingIssue : {}", fundingIssueDTO);
    if (fundingIssueDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new fundingIssue cannot already have an ID")).body(null);
    }
    FundingIssue fundingIssue = fundingIssueMapper.fundingIssueDTOToFundingIssue(fundingIssueDTO);
    fundingIssue = fundingIssueRepository.save(fundingIssue);
    FundingIssueDTO result = fundingIssueMapper.fundingIssueToFundingIssueDTO(fundingIssue);
    return ResponseEntity.created(new URI("/api/funding-issues/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /funding-issues : Updates an existing fundingIssue.
   *
   * @param fundingIssueDTO the fundingIssueDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated fundingIssueDTO,
   * or with status 400 (Bad Request) if the fundingIssueDTO is not valid,
   * or with status 500 (Internal Server Error) if the fundingIssueDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/funding-issues")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<FundingIssueDTO> updateFundingIssue(@Valid @RequestBody FundingIssueDTO fundingIssueDTO) throws URISyntaxException {
    log.debug("REST request to update FundingIssue : {}", fundingIssueDTO);
    if (fundingIssueDTO.getId() == null) {
      return createFundingIssue(fundingIssueDTO);
    }
    FundingIssue fundingIssue = fundingIssueMapper.fundingIssueDTOToFundingIssue(fundingIssueDTO);
    fundingIssue = fundingIssueRepository.save(fundingIssue);
    FundingIssueDTO result = fundingIssueMapper.fundingIssueToFundingIssueDTO(fundingIssue);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fundingIssueDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /funding-issues : get all funding issues.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of funding issues in body
   */
  @ApiOperation(value = "Lists funding issues",
      notes = "Returns a list of funding issues with support for pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "funding issues list")})
  @GetMapping("/funding-issues")
  @Timed
  public ResponseEntity<List<FundingIssueDTO>> getAllFundingIssues(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.info("REST request to get a page of countries begin");
    searchQuery = StringConverter.getConverter(searchQuery).decodeUrl().escapeForSql().toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    Page<FundingIssue> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = fundingIssueRepository.findAll(pageable);
    } else {
      page = fundingIssueService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<FundingIssueDTO> results = page.map(fundingIssueMapper::fundingIssueToFundingIssueDTO);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/countries");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /funding-issues/:id : get the "id" fundingIssue.
   *
   * @param id the id of the fundingIssueDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the fundingIssueDTO, or with status 404 (Not Found)
   */
  @GetMapping("/funding-issues/{id}")
  @Timed
  public ResponseEntity<FundingIssueDTO> getFundingIssue(@PathVariable Long id) {
    log.debug("REST request to get FundingIssue : {}", id);
    FundingIssue fundingIssue = fundingIssueRepository.findOne(id);
    FundingIssueDTO fundingIssueDTO = fundingIssueMapper.fundingIssueToFundingIssueDTO(fundingIssue);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fundingIssueDTO));
  }

  /**
   * DELETE  /funding-issues/:id : delete the "id" fundingIssue.
   *
   * @param id the id of the fundingIssueDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/funding-issues/{id}")
  @Timed
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteFundingIssue(@PathVariable Long id) {
    log.debug("REST request to delete FundingIssue : {}", id);
    fundingIssueRepository.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * POST  /bulk-funding-issues : Bulk create a new funding-issues.
   *
   * @param fundingIssueDTOS List of the fundingIssueDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new fundingIssueDTOS, or with status 400 (Bad Request) if the FundingIssueDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-funding-issues")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<FundingIssueDTO>> bulkCreateFundingIssue(@Valid @RequestBody List<FundingIssueDTO> fundingIssueDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save FundingIssueDTO : {}", fundingIssueDTOS);
    if (!Collections.isEmpty(fundingIssueDTOS)) {
      List<Long> entityIds = fundingIssueDTOS.stream()
          .filter(fi -> fi.getId() != null)
          .map(fi -> fi.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new assessmentTypes cannot already have an ID")).body(null);
      }
    }
    List<FundingIssue> fundingIssues = fundingIssueMapper.fundingIssueDTOsToFundingIssues(fundingIssueDTOS);
    fundingIssues = fundingIssueRepository.save(fundingIssues);
    List<FundingIssueDTO> result = fundingIssueMapper.fundingIssuesToFundingIssueDTOs(fundingIssues);
    return ResponseEntity.ok()
        .body(result);
  }

  /**
   * PUT  /bulk-funding-issues : Updates an existing FundingIssue.
   *
   * @param fundingIssueDTOS List of the fundingIssueDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated fundingIssueDTOS,
   * or with status 400 (Bad Request) if the fundingIssueDTOS is not valid,
   * or with status 500 (Internal Server Error) if the fundingIssueDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-funding-issues")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<FundingIssueDTO>> bulkUpdateFundingIssue(@Valid @RequestBody List<FundingIssueDTO> fundingIssueDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update FundingIssues : {}", fundingIssueDTOS);
    if (Collections.isEmpty(fundingIssueDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(fundingIssueDTOS)) {
      List<FundingIssueDTO> entitiesWithNoId = fundingIssueDTOS.stream().filter(fi -> fi.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<FundingIssue> fundingIssues = fundingIssueMapper.fundingIssueDTOsToFundingIssues(fundingIssueDTOS);
    fundingIssues = fundingIssueRepository.save(fundingIssues);
    List<FundingIssueDTO> results = fundingIssueMapper.fundingIssuesToFundingIssueDTOs(fundingIssues);
    return ResponseEntity.ok()
        .body(results);
  }
}
