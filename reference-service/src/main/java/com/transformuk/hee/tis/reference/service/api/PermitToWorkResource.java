package com.transformuk.hee.tis.reference.service.api;

import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.in;
import static com.transformuk.hee.tis.reference.service.service.impl.SpecificationFactory.isEqual;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.PermitToWorkDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.PermitToWork;
import com.transformuk.hee.tis.reference.service.repository.PermitToWorkRepository;
import com.transformuk.hee.tis.reference.service.service.impl.PermitToWorkServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.PermitToWorkMapper;
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
 * REST controller for managing PermitToWork.
 */
@RestController
@RequestMapping("/api")
public class PermitToWorkResource {

  private static final String ENTITY_NAME = "permitToWork";
  private final Logger log = LoggerFactory.getLogger(PermitToWorkResource.class);

  private final PermitToWorkRepository permitToWorkRepository;
  private final PermitToWorkMapper permitToWorkMapper;
  private final PermitToWorkServiceImpl permitToWorkService;

  public PermitToWorkResource(PermitToWorkRepository permitToWorkRepository,
      PermitToWorkMapper permitToWorkMapper,
      PermitToWorkServiceImpl permitToWorkService) {
    this.permitToWorkRepository = permitToWorkRepository;
    this.permitToWorkMapper = permitToWorkMapper;
    this.permitToWorkService = permitToWorkService;
  }

  /**
   * POST  /permit-to-works : Create a new permitToWork.
   *
   * @param permitToWorkDTO the permitToWorkDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new permitToWorkDTO, or
   *     with status 400 (Bad Request) if the permitToWork has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/permit-to-works")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<PermitToWorkDTO> createPermitToWork(
      @Valid @RequestBody PermitToWorkDTO permitToWorkDTO) throws URISyntaxException {
    log.debug("REST request to save PermitToWork : {}", permitToWorkDTO);
    if (permitToWorkDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil
          .createFailureAlert(ENTITY_NAME, "idexists",
              "A new permitToWork cannot already have an ID")).body(null);
    }
    PermitToWork permitToWork = permitToWorkMapper.permitToWorkDTOToPermitToWork(permitToWorkDTO);
    permitToWork = permitToWorkRepository.save(permitToWork);
    PermitToWorkDTO result = permitToWorkMapper.permitToWorkToPermitToWorkDTO(permitToWork);
    return ResponseEntity.created(new URI("/api/permit-to-works/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /permit-to-works : Updates an existing permitToWork.
   *
   * @param permitToWorkDTO the permitToWorkDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated permitToWorkDTO, or
   *     with status 400 (Bad Request) if the permitToWorkDTO is not valid, or with status 500
   *     (Internal Server Error) if the permitToWorkDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/permit-to-works")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<PermitToWorkDTO> updatePermitToWork(
      @Valid @RequestBody PermitToWorkDTO permitToWorkDTO) throws URISyntaxException {
    log.debug("REST request to update PermitToWork : {}", permitToWorkDTO);
    if (permitToWorkDTO.getId() == null) {
      return createPermitToWork(permitToWorkDTO);
    }
    PermitToWork permitToWork = permitToWorkMapper.permitToWorkDTOToPermitToWork(permitToWorkDTO);
    permitToWork = permitToWorkRepository.save(permitToWork);
    PermitToWorkDTO result = permitToWorkMapper.permitToWorkToPermitToWorkDTO(permitToWork);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, permitToWorkDTO.getId().toString()))
        .body(result);
  }


  /**
   * GET  /permit-to-works : get all permit to work.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of permit to work in body
   */
  @ApiOperation(value = "Lists permit to work",
      notes = "Returns a list of permit to work with support for pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "permit to work list")})
  @GetMapping("/permit-to-works")
  @Timed
  public ResponseEntity<List<PermitToWorkDTO>> getAllPermitToWorks(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson)
      throws IOException {
    log.info("REST request to get a page of permit to work begin");
    searchQuery = StringConverter.getConverter(searchQuery).fromJson().decodeUrl().escapeForSql()
        .toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil
        .getColumnFilters(columnFilterJson, filterEnumList);
    Page<PermitToWork> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = permitToWorkRepository.findAll(pageable);
    } else {
      page = permitToWorkService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<PermitToWorkDTO> results = page.map(permitToWorkMapper::permitToWorkToPermitToWorkDTO);
    HttpHeaders headers = PaginationUtil
        .generatePaginationHttpHeaders(page, "/api/permit-to-works");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /permit-to-works/:id : get the "id" permitToWork.
   *
   * @param id the id of the permitToWorkDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the permitToWorkDTO, or with
   *     status 404 (Not Found)
   */
  @GetMapping("/permit-to-works/{id}")
  @Timed
  public ResponseEntity<PermitToWorkDTO> getPermitToWork(@PathVariable Long id) {
    log.debug("REST request to get PermitToWork : {}", id);
    PermitToWork permitToWork = permitToWorkRepository.findOne(id);
    PermitToWorkDTO permitToWorkDTO = permitToWorkMapper
        .permitToWorkToPermitToWorkDTO(permitToWork);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(permitToWorkDTO));
  }

  /**
   * DELETE  /permit-to-works/:id : delete the "id" permitToWork.
   *
   * @param id the id of the permitToWorkDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/permit-to-works/{id}")
  @Timed
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deletePermitToWork(@PathVariable Long id) {
    log.debug("REST request to delete PermitToWork : {}", id);
    permitToWorkRepository.delete(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * POST  /bulk-permit-to-works : Bulk create a new permit-to-works.
   *
   * @param permitToWorkDTOS List of the permitToWorkDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new permitToWorkDTOS, or
   *     with status 400 (Bad Request) if the PermitToWorkDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-permit-to-works")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<PermitToWorkDTO>> bulkCreatePermitToWork(
      @Valid @RequestBody List<PermitToWorkDTO> permitToWorkDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save PermitToWork : {}", permitToWorkDTOS);
    if (!Collections.isEmpty(permitToWorkDTOS)) {
      List<Long> entityIds = permitToWorkDTOS.stream()
          .filter(permitToWorkDTO -> permitToWorkDTO.getId() != null)
          .map(permitToWorkDTO -> permitToWorkDTO.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil
            .createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist",
                "A new permitToWorks cannot already have an ID")).body(null);
      }
    }
    List<PermitToWork> permitToWorks = permitToWorkMapper
        .permitToWorkDTOsToPermitToWorks(permitToWorkDTOS);
    permitToWorks = permitToWorkRepository.save(permitToWorks);
    List<PermitToWorkDTO> result = permitToWorkMapper
        .permitToWorksToPermitToWorkDTOs(permitToWorks);
    return ResponseEntity.ok()
        .body(result);
  }

  /**
   * PUT  /bulk-permit-to-works : Updates an existing permit-to-works.
   *
   * @param permitToWorkDTOS List of the permitToWorkDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated permitToWorkDTOS, or
   *     with status 400 (Bad Request) if the permitToWorkDTOS is not valid, or with status 500
   *     (Internal Server Error) if the permitToWorkDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-permit-to-works")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<PermitToWorkDTO>> bulkUpdatePermitToWork(
      @Valid @RequestBody List<PermitToWorkDTO> permitToWorkDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update PermitToWork : {}", permitToWorkDTOS);
    if (Collections.isEmpty(permitToWorkDTOS)) {
      return ResponseEntity.badRequest()
          .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
              "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(permitToWorkDTOS)) {
      List<PermitToWorkDTO> entitiesWithNoId = permitToWorkDTOS.stream()
          .filter(permitToWorkDTO -> permitToWorkDTO.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest()
            .headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
                "bulk.update.failed.noId",
                "Some DTOs you've provided have no Id, cannot update entities that dont exist"))
            .body(entitiesWithNoId);
      }
    }
    List<PermitToWork> permitToWorks = permitToWorkMapper
        .permitToWorkDTOsToPermitToWorks(permitToWorkDTOS);
    permitToWorks = permitToWorkRepository.save(permitToWorks);
    List<PermitToWorkDTO> results = permitToWorkMapper
        .permitToWorksToPermitToWorkDTOs(permitToWorks);
    return ResponseEntity.ok()
        .body(results);
  }

  /**
   * EXISTS /permit-to-works/exists/ : check is permit to works exists
   *
   * @param code             the code of the permit to work to check
   * @param columnFilterJson The column filters to apply
   * @return boolean true if exists otherwise false
   */
  @PostMapping("/permit-to-works/exists")
  @Timed
  public ResponseEntity<Boolean> permitToWorkExists(@RequestBody String code,
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson)
      throws IOException {
    log.debug("REST request to check PermitToWork exists : {}", code);
    Specifications<PermitToWork> specs = Specifications.where(isEqual("code", code));

    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters =
        ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);

    for (ColumnFilter columnFilter : columnFilters) {
      specs = specs.and(in(columnFilter.getName(), columnFilter.getValues()));
    }

    boolean exists = permitToWorkRepository.findOne(specs) != null;
    return new ResponseEntity<>(exists, HttpStatus.OK);
  }
}
