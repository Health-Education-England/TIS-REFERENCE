package com.transformuk.hee.tis.reference.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.AssessmentTypeDto;
import com.transformuk.hee.tis.reference.api.dto.validation.Create;
import com.transformuk.hee.tis.reference.api.dto.validation.Update;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.AssessmentType;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.repository.AssessmentTypeRepository;
import com.transformuk.hee.tis.reference.service.service.impl.AssessmentTypeServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.AssessmentTypeMapper;
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
import org.springframework.validation.annotation.Validated;
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
 * REST controller for managing Assessment Types.
 */
@RestController
@RequestMapping("/api")
public class AssessmentTypeResource {

  private static final String ENTITY_NAME = "AssessmentType";
  private final Logger log = LoggerFactory.getLogger(AssessmentTypeResource.class);

  private final AssessmentTypeRepository assessmentTypeRepository;
  private final AssessmentTypeServiceImpl assessmentTypeService;
  private final AssessmentTypeMapper mapper;

  public AssessmentTypeResource(AssessmentTypeRepository assessmentTypeRepository,
      AssessmentTypeServiceImpl assessmentTypeService, AssessmentTypeMapper mapper) {
    this.assessmentTypeRepository = assessmentTypeRepository;
    this.assessmentTypeService = assessmentTypeService;
    this.mapper = mapper;
  }

  /**
   * POST  /assessment-types : Create a new Assessment type.
   *
   * @param assessmentType the Assessment type to create
   * @return the ResponseEntity with status 201 (Created) and with body the new AssessmentType, or
   *     with status 400 (Bad Request) if the AssessmentType has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/assessment-types")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<AssessmentTypeDto> createAssessmentType(
      @Validated(Create.class) @RequestBody AssessmentTypeDto assessmentType)
      throws URISyntaxException {
    log.debug("REST request to save AssessmentType : {}", assessmentType);
    if (assessmentType.getId() != null) {
      return ResponseEntity.badRequest().headers(
          HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "Assessment type must have an ID"))
          .body(null);
    }
    AssessmentType newAssessmentType =
        assessmentTypeRepository.save(mapper.toEntity(assessmentType));
    return ResponseEntity.created(new URI("/api/assessment-types/" + newAssessmentType.getCode()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, newAssessmentType.getCode()))
        .body(mapper.toDto(newAssessmentType));
  }

  /**
   * PUT  /assessment-types : Updates an existing Assessment Type.
   *
   * @param assessmentType the assessmentType to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated assessmentType, or
   *     with status 400 (Bad Request) if the assessmentType is not valid, or with status 500
   *     (Internal Server Error) if the assessmentType couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/assessment-types")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<AssessmentTypeDto> updateAssessmentType(
      @Validated(Update.class) @RequestBody AssessmentTypeDto assessmentType)
      throws URISyntaxException {
    log.debug("REST request to update AssessmentType : {}", assessmentType);

    if (!assessmentTypeRepository.findById(assessmentType.getId()).isPresent()) {
      return createAssessmentType(assessmentType);
    }

    AssessmentType result = assessmentTypeRepository.save(mapper.toEntity(assessmentType));
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getCode()))
        .body(mapper.toDto(result));
  }

  /**
   * GET  /assessment-types : get all assessment types.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of assessment types in body
   */
  @ApiOperation(value = "Lists assessment types",
      notes = "Returns a list of assessment types with support for pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "assessment types list")})
  @GetMapping("/assessment-types")
  public ResponseEntity<List<AssessmentTypeDto>> getAllAssessmentTypes(
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
    Page<AssessmentType> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = assessmentTypeRepository.findAll(pageable);
    } else {
      page = assessmentTypeService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    HttpHeaders headers = PaginationUtil
        .generatePaginationHttpHeaders(page, "/api/assessment-type");
    return new ResponseEntity<>(mapper.toDtos(page.getContent()), headers, HttpStatus.OK);
  }

  /**
   * GET  /assessment-types/:code : get the Assessment Type based on the code.
   *
   * @param code the code of the AssessmentType to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the AssessmentType, or with
   *     status 404 (Not Found)
   */
  @GetMapping("/assessment-types/{code}")
  public ResponseEntity<AssessmentTypeDto> getAssessmentTypeByCode(@PathVariable String code) {
    log.debug("REST request to get Assessment Type : {}", code);
    AssessmentType resultEntity = assessmentTypeRepository.findOneByCode(code);
    AssessmentTypeDto resultDto = mapper.toDto(resultEntity);

    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(resultDto));
  }

  /**
   * POST  /bulk-assessment-types : Bulk create new assessment-types.
   *
   * @param assessmentTypeDtos List of the assessmentTypeDtos to create
   * @return the ResponseEntity with status 200 (Created) and with body the new assessmentTypeDtos,
   *     or with status 400 (Bad Request) if the assessmentTypeDto already has an ID.
   */
  @PostMapping("/bulk-assessment-types")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<AssessmentTypeDto>> bulkCreateAssessmentTypes(
      @Valid @RequestBody List<AssessmentTypeDto> assessmentTypeDtos) {
    log.debug("REST request to bulk save AssessmentTypeDTOs : {}", assessmentTypeDtos);
    if (!Collections.isEmpty(assessmentTypeDtos)) {
      List<Long> entityIds = assessmentTypeDtos.stream()
          .filter(assessmentTypeDto -> assessmentTypeDto.getId() != null)
          .map(AssessmentTypeDto::getId)
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil
            .createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist",
                "A new assessmentTypes cannot already have an ID")).body(null);
      }
    }
    List<AssessmentType> assessmentTypes = mapper.toEntities(assessmentTypeDtos);
    assessmentTypes = assessmentTypeRepository.saveAll(assessmentTypes);
    List<AssessmentTypeDto> result = mapper.toDtos(assessmentTypes);
    return ResponseEntity.ok(result);
  }
}
