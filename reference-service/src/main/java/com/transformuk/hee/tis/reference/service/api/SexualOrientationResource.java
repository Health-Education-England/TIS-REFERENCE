package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.SexualOrientationDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.SexualOrientation;
import com.transformuk.hee.tis.reference.service.repository.SexualOrientationRepository;
import com.transformuk.hee.tis.reference.service.service.impl.SexualOrientationServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.SexualOrientationMapper;
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
 * REST controller for managing SexualOrientation.
 */
@RestController
@RequestMapping("/api")
public class SexualOrientationResource {

  private static final String ENTITY_NAME = "sexualOrientation";
  private final Logger log = LoggerFactory.getLogger(SexualOrientationResource.class);

  private final SexualOrientationRepository sexualOrientationRepository;
  private final SexualOrientationMapper sexualOrientationMapper;
  private final SexualOrientationServiceImpl sexualOrientationService;
  public SexualOrientationResource(SexualOrientationRepository sexualOrientationRepository, SexualOrientationMapper sexualOrientationMapper,
                                   SexualOrientationServiceImpl sexualOrientationService) {
    this.sexualOrientationRepository = sexualOrientationRepository;
    this.sexualOrientationMapper = sexualOrientationMapper;
    this.sexualOrientationService = sexualOrientationService;
  }

  /**
   * POST  /sexual-orientations : Create a new sexualOrientation.
   *
   * @param sexualOrientationDTO the sexualOrientationDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new sexualOrientationDTO, or with status 400 (Bad Request) if the sexualOrientation has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/sexual-orientations")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<SexualOrientationDTO> createSexualOrientation(@Valid @RequestBody SexualOrientationDTO sexualOrientationDTO) throws URISyntaxException {
    log.debug("REST request to save SexualOrientation : {}", sexualOrientationDTO);
    if (sexualOrientationDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new sexualOrientation cannot already have an ID")).body(null);
    }
    SexualOrientation sexualOrientation = sexualOrientationMapper.sexualOrientationDTOToSexualOrientation(sexualOrientationDTO);
    sexualOrientation = sexualOrientationRepository.save(sexualOrientation);
    SexualOrientationDTO result = sexualOrientationMapper.sexualOrientationToSexualOrientationDTO(sexualOrientation);
    return ResponseEntity.created(new URI("/api/sexual-orientations/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /sexual-orientations : Updates an existing sexualOrientation.
   *
   * @param sexualOrientationDTO the sexualOrientationDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated sexualOrientationDTO,
   * or with status 400 (Bad Request) if the sexualOrientationDTO is not valid,
   * or with status 500 (Internal Server Error) if the sexualOrientationDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/sexual-orientations")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<SexualOrientationDTO> updateSexualOrientation(@Valid @RequestBody SexualOrientationDTO sexualOrientationDTO) throws URISyntaxException {
    log.debug("REST request to update SexualOrientation : {}", sexualOrientationDTO);
    if (sexualOrientationDTO.getId() == null) {
      return createSexualOrientation(sexualOrientationDTO);
    }
    SexualOrientation sexualOrientation = sexualOrientationMapper.sexualOrientationDTOToSexualOrientation(sexualOrientationDTO);
    sexualOrientation = sexualOrientationRepository.save(sexualOrientation);
    SexualOrientationDTO result = sexualOrientationMapper.sexualOrientationToSexualOrientationDTO(sexualOrientation);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sexualOrientationDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /sexual-orientations : get all sexual orientations.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of sexual orientations in body
   */
  @ApiOperation(value = "Lists sexual orientations",
      notes = "Returns a list of sexual orientations with support for pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "sexual orientations list")})
  @GetMapping("/sexual-orientations")
  @Timed
  public ResponseEntity<List<SexualOrientationDTO>> getAllSexualOrientations(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.info("REST request to get a page of sexual orientations begin");
    searchQuery = StringConverter.getConverter(searchQuery).fromJson().decodeUrl().escapeForSql().toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    Page<SexualOrientation> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = sexualOrientationRepository.findAll(pageable);
    } else {
      page = sexualOrientationService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<SexualOrientationDTO> results = page.map(sexualOrientationMapper::sexualOrientationToSexualOrientationDTO);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sexual-orientations");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /sexual-orientations/:id : get the "id" sexualOrientation.
   *
   * @param id the id of the sexualOrientationDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the sexualOrientationDTO, or with status 404 (Not Found)
   */
  @GetMapping("/sexual-orientations/{id}")
  @Timed
  public ResponseEntity<SexualOrientationDTO> getSexualOrientation(@PathVariable Long id) {
    log.debug("REST request to get SexualOrientation : {}", id);
    SexualOrientation sexualOrientation = sexualOrientationRepository.findOne(id);
    SexualOrientationDTO sexualOrientationDTO = sexualOrientationMapper.sexualOrientationToSexualOrientationDTO(sexualOrientation);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sexualOrientationDTO));
  }

  /**
   * EXISTS /sexual-orientations/exists/ : check is sexualOrientation exists
   *
   * @param code the code of the sexualOrientationDTO to check
   * @return boolean true if exists otherwise false
   */
  @PostMapping("/sexual-orientations/exists/")
  @Timed
  public ResponseEntity<Boolean> maritalStatusExists(@RequestBody String code) {
    log.debug("REST request to check SexualOrientation exists : {}", code);
    SexualOrientation sexualOrientation = sexualOrientationRepository.findFirstByCode(code);
    if(sexualOrientation == null){
      return new ResponseEntity<>(false, HttpStatus.OK);
    }
    return new ResponseEntity<>(true, HttpStatus.OK);
  }

  /**
   * DELETE  /sexual-orientations/:id : delete the "id" sexualOrientation.
   *
   * @param id the id of the sexualOrientationDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/sexual-orientations/{id}")
  @Timed
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteSexualOrientation(@PathVariable Long id) {
    log.debug("REST request to delete SexualOrientation : {}", id);
    sexualOrientationRepository.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }


  /**
   * POST  /bulk-sexual-orientations : Bulk create a new sexual-orientations.
   *
   * @param sexualOrientationDTOS List of the sexualOrientationDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new sexualOrientationDTOS, or with status 400 (Bad Request) if the sexualOrientation has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-sexual-orientations")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<SexualOrientationDTO>> bulkCreateSexualOrientation(@Valid @RequestBody List<SexualOrientationDTO> sexualOrientationDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save SexualOrientationDtos : {}", sexualOrientationDTOS);
    if (!Collections.isEmpty(sexualOrientationDTOS)) {
      List<Long> entityIds = sexualOrientationDTOS.stream()
          .filter(so -> so.getId() != null)
          .map(so -> so.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new sexualOrientations cannot already have an ID")).body(null);
      }
    }
    List<SexualOrientation> sexualOrientations = sexualOrientationMapper.sexualOrientationDTOsToSexualOrientations(sexualOrientationDTOS);
    sexualOrientations = sexualOrientationRepository.save(sexualOrientations);
    List<SexualOrientationDTO> result = sexualOrientationMapper.sexualOrientationsToSexualOrientationDTOs(sexualOrientations);
    return ResponseEntity.ok()
        .body(result);
  }

  /**
   * PUT  /bulk-sexual-orientations : Updates an existing sexual-orientations.
   *
   * @param sexualOrientationDTOS List of the sexualOrientationDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated sexualOrientationDTOS,
   * or with status 400 (Bad Request) if the sexualOrientationDTOS is not valid,
   * or with status 500 (Internal Server Error) if the sexualOrientationDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-sexual-orientations")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<SexualOrientationDTO>> bulkUpdateSexualOrientation(@Valid @RequestBody List<SexualOrientationDTO> sexualOrientationDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update AssessmentTypesDTO : {}", sexualOrientationDTOS);
    if (Collections.isEmpty(sexualOrientationDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(sexualOrientationDTOS)) {
      List<SexualOrientationDTO> entitiesWithNoId = sexualOrientationDTOS.stream().filter(so -> so.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<SexualOrientation> sexualOrientations = sexualOrientationMapper.sexualOrientationDTOsToSexualOrientations(sexualOrientationDTOS);
    sexualOrientations = sexualOrientationRepository.save(sexualOrientations);
    List<SexualOrientationDTO> results = sexualOrientationMapper.sexualOrientationsToSexualOrientationDTOs(sexualOrientations);
    return ResponseEntity.ok()
        .body(results);
  }
}
