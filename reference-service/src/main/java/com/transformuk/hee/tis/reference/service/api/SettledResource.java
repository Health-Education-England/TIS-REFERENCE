package com.transformuk.hee.tis.reference.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.SettledDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.Settled;
import com.transformuk.hee.tis.reference.service.repository.SettledRepository;
import com.transformuk.hee.tis.reference.service.service.impl.SettledServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.SettledMapper;
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
 * REST controller for managing Settled.
 */
@RestController
@RequestMapping("/api")
public class SettledResource {

  private static final String ENTITY_NAME = "settled";
  private final Logger log = LoggerFactory.getLogger(SettledResource.class);

  private final SettledRepository settledRepository;
  private final SettledMapper settledMapper;
  private final SettledServiceImpl settledService;

  public SettledResource(SettledRepository settledRepository, SettledMapper settledMapper,
      SettledServiceImpl settledService) {
    this.settledRepository = settledRepository;
    this.settledMapper = settledMapper;
    this.settledService = settledService;
  }

  /**
   * POST  /settleds : Create a new settled.
   *
   * @param settledDTO the settledDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new settledDTO, or with
   * status 400 (Bad Request) if the settled has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/settleds")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<SettledDTO> createSettled(@Valid @RequestBody SettledDTO settledDTO)
      throws URISyntaxException {
    log.debug("REST request to save Settled : {}", settledDTO);
    if (settledDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil
          .createFailureAlert(ENTITY_NAME, "idexists", "A new settled cannot already have an ID"))
          .body(null);
    }
    Settled settled = settledMapper.settledDTOToSettled(settledDTO);
    settled = settledRepository.save(settled);
    SettledDTO result = settledMapper.settledToSettledDTO(settled);
    return ResponseEntity.created(new URI("/api/settleds/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /settleds : Updates an existing settled.
   *
   * @param settledDTO the settledDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated settledDTO, or with
   * status 400 (Bad Request) if the settledDTO is not valid, or with status 500 (Internal Server
   * Error) if the settledDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/settleds")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<SettledDTO> updateSettled(@Valid @RequestBody SettledDTO settledDTO)
      throws URISyntaxException {
    log.debug("REST request to update Settled : {}", settledDTO);
    if (settledDTO.getId() == null) {
      return createSettled(settledDTO);
    }
    Settled settled = settledMapper.settledDTOToSettled(settledDTO);
    settled = settledRepository.save(settled);
    SettledDTO result = settledMapper.settledToSettledDTO(settled);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, settledDTO.getId().toString()))
        .body(result);
  }


  /**
   * GET  /settleds : get all settled.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of settled in body
   */
  @ApiOperation(value = "Lists settled",
      notes = "Returns a list of settled with support for pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "settled list")})
  @GetMapping("/settleds")
  public ResponseEntity<List<SettledDTO>> getAllSettleds(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson)
      throws IOException {
    log.info("REST request to get a page of settled begin");
    searchQuery = StringConverter.getConverter(searchQuery).fromJson().decodeUrl().escapeForSql()
        .toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil
        .getColumnFilters(columnFilterJson, filterEnumList);
    Page<Settled> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = settledRepository.findAll(pageable);
    } else {
      page = settledService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<SettledDTO> results = page.map(settledMapper::settledToSettledDTO);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/settleds");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /settleds/:id : get the "id" settled.
   *
   * @param id the id of the settledDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the settledDTO, or with status
   * 404 (Not Found)
   */
  @GetMapping("/settleds/{id}")
  public ResponseEntity<SettledDTO> getSettled(@PathVariable Long id) {
    log.debug("REST request to get Settled : {}", id);
    Settled settled = settledRepository.findById(id).orElse(null);
    SettledDTO settledDTO = settledMapper.settledToSettledDTO(settled);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(settledDTO));
  }

  /**
   * DELETE  /settleds/:id : delete the "id" settled.
   *
   * @param id the id of the settledDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/settleds/{id}")
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteSettled(@PathVariable Long id) {
    log.debug("REST request to delete Settled : {}", id);
    settledRepository.deleteById(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }


  /**
   * POST  /bulk-settleds : Bulk create a new settleds.
   *
   * @param settledDTOS List of the settledDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new settledDTOS, or with
   * status 400 (Bad Request) if the SettledDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-settleds")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<SettledDTO>> bulkCreateSettled(
      @Valid @RequestBody List<SettledDTO> settledDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save SettledDtos : {}", settledDTOS);
    if (!Collections.isEmpty(settledDTOS)) {
      List<Long> entityIds = settledDTOS.stream()
          .filter(settledDTO -> settledDTO.getId() != null)
          .map(settledDTO -> settledDTO.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil
            .createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist",
                "A new settledList cannot already have an ID")).body(null);
      }
    }
    List<Settled> settledList = settledMapper.settledDTOsToSettleds(settledDTOS);
    settledList = settledRepository.saveAll(settledList);
    List<SettledDTO> result = settledMapper.settledsToSettledDTOs(settledList);
    return ResponseEntity.ok()
        .body(result);
  }

  /**
   * PUT  /bulk-settleds : Updates an existing settleds.
   *
   * @param settledDTOS List of the settledDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated settledDTOS, or with
   * status 400 (Bad Request) if the settledDTOS is not valid, or with status 500 (Internal Server
   * Error) if the settledDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-settleds")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<SettledDTO>> bulkUpdateSettled(
      @Valid @RequestBody List<SettledDTO> settledDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update SettledDtos : {}", settledDTOS);
    if (Collections.isEmpty(settledDTOS)) {
      return ResponseEntity.badRequest()
          .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
              "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(settledDTOS)) {
      List<SettledDTO> entitiesWithNoId = settledDTOS.stream()
          .filter(settledDTO -> settledDTO.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest()
            .headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
                "bulk.update.failed.noId",
                "Some DTOs you've provided have no Id, cannot update entities that dont exist"))
            .body(entitiesWithNoId);
      }
    }
    List<Settled> settledList = settledMapper.settledDTOsToSettleds(settledDTOS);
    settledList = settledRepository.saveAll(settledList);
    List<SettledDTO> results = settledMapper.settledsToSettledDTOs(settledList);
    return ResponseEntity.ok()
        .body(results);
  }

}
