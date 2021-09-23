package com.transformuk.hee.tis.reference.service.api;

import static uk.nhs.tis.StringConverter.getConverter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.validation.Create;
import com.transformuk.hee.tis.reference.api.dto.validation.Update;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.api.util.UrlDecoderUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.Grade;
import com.transformuk.hee.tis.reference.service.repository.GradeRepository;
import com.transformuk.hee.tis.reference.service.service.impl.GradeServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.GradeMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
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
 * REST controller for managing Grade.
 */
@RestController
@RequestMapping("/api")
public class GradeResource {

  private static final String ENTITY_NAME = "grade";
  private final Logger log = LoggerFactory.getLogger(GradeResource.class);

  private final GradeRepository gradeRepository;
  private final GradeMapper gradeMapper;
  private final GradeServiceImpl gradeService;

  public GradeResource(GradeRepository gradeRepository, GradeMapper gradeMapper,
      GradeServiceImpl gradeService) {
    this.gradeRepository = gradeRepository;
    this.gradeMapper = gradeMapper;
    this.gradeService = gradeService;
  }

  /**
   * POST  /grades : Create a new grade.
   *
   * @param gradeDTO the gradeDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new gradeDTO, or with
   * status 400 (Bad Request) if the grade has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/grades")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<GradeDTO> createGrade(
      @Validated(Create.class) @RequestBody GradeDTO gradeDTO) throws URISyntaxException {
    log.debug("REST request to save Grade : {}", gradeDTO);
    Grade grade = gradeMapper.gradeDTOToGrade(gradeDTO);
    grade = gradeRepository.save(grade);
    GradeDTO result = gradeMapper.gradeToGradeDTO(grade);
    return ResponseEntity.created(new URI("/api/grades/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /grades : Updates an existing grade.
   *
   * @param gradeDTO the gradeDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated gradeDTO, or with
   * status 400 (Bad Request) if the gradeDTO is not valid, or with status 500 (Internal Server
   * Error) if the gradeDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/grades")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<GradeDTO> updateGrade(
      @Validated(Update.class) @RequestBody GradeDTO gradeDTO) throws URISyntaxException {
    log.debug("REST request to update Grade : {}", gradeDTO);
    Grade grade = gradeMapper.gradeDTOToGrade(gradeDTO);
    grade = gradeRepository.save(grade);
    GradeDTO result = gradeMapper.gradeToGradeDTO(grade);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gradeDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /grades/:id : get the grade by id.
   *
   * @param id the id of the gradeDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the gradeDTO, or with status 404
   * (Not Found)
   */
  @GetMapping("/grades/{id}")
  public ResponseEntity<GradeDTO> getGrade(@PathVariable Long id) {
    log.debug("REST request to get Grade : {}", id);
    Grade grade = gradeRepository.findById(id).orElse(null);
    GradeDTO gradeDTO = gradeMapper.gradeToGradeDTO(grade);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gradeDTO));
  }

  /**
   * GET  /grades/in/:codes : get grades given to codes. Ignores malformed or not found grades
   *
   * @param codes the codes to search by
   * @return the ResponseEntity with status 200 (OK) and with body the list of gradeDTOs, or empty
   * list
   */
  @GetMapping("/grades/in/{codes}")
  public ResponseEntity<List<GradeDTO>> getGradesIn(@PathVariable String codes) {
    log.debug("REST request to find several  Grades");
    List<GradeDTO> resp = new ArrayList<>();
    Set<String> codeSet = new HashSet<>();

    if (codes == null || codes.isEmpty()) {
      return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    codeSet.addAll(Arrays.asList(codes.split(",")));

    if (!codeSet.isEmpty()) {
      List<Grade> grades = gradeRepository.findByAbbreviationIn(codeSet);
      resp = gradeMapper.gradesToGradeDTOs(grades);
      return new ResponseEntity<>(resp, HttpStatus.FOUND);
    } else {
      return new ResponseEntity<>(resp, HttpStatus.OK);
    }
  }

  /**
   * GET  /grades/ids/in : get grades by a list of ids in query param. Ignores malformed or not
   * found grades
   *
   * @param ids the ids to search by
   * @return the ResponseEntity with status 200 (OK) and with body the list of gradeDTOs, or empty
   * list
   */
  @GetMapping("/grades/ids/in")
  public ResponseEntity<List<GradeDTO>> getGradesByIds(@RequestParam List<Long> ids) {
    log.debug("REST request to find several Grades by ids");
    List<GradeDTO> resp = new ArrayList<>();

    if (CollectionUtils.isEmpty(ids)) {
      return new ResponseEntity<>(resp, HttpStatus.OK);
    } else {
      List<Grade> grades = gradeRepository.findAllById(ids);
      resp = gradeMapper.gradesToGradeDTOs(grades);
      return new ResponseEntity<>(resp,
          CollectionUtils.isEmpty(resp) ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
  }


  /**
   * GET  /grades : get all grades.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of colleges in body
   */
  @ApiOperation(value = "Lists grades",
      notes = "Returns a list of grades with support for pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "grades list")})
  @GetMapping("/grades")
  public ResponseEntity<List<GradeDTO>> getAllGrades(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson)
      throws IOException {
    log.info("REST request to get a page of grades begin");
    searchQuery = StringConverter.getConverter(searchQuery).fromJson().decodeUrl().escapeForSql()
        .toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    if (columnFilterJson != null) {
      columnFilterJson = UrlDecoderUtil.decode(columnFilterJson);
    }
    List<ColumnFilter> columnFilters = ColumnFilterUtil
        .getColumnFilters(columnFilterJson, filterEnumList);
    Page<Grade> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = gradeRepository.findAll(pageable);
    } else {
      page = gradeService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<GradeDTO> results = page.map(gradeMapper::gradeToGradeDTO);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/grades");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }

  @ApiOperation("Get all current grades using pagination or smart search")
  @GetMapping("/current/grades")
  @Transactional(readOnly = true)
  public ResponseEntity<List<GradeDTO>> getAllCurrentGrades(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery) {
    log.debug("REST request to get a page of Grades");
    searchQuery = StringConverter.getConverter(searchQuery).fromJson().decodeUrl().escapeForSql()
        .toString();
    Page<Grade> page;
    if (StringUtils.isEmpty(searchQuery)) {
      Grade gradeExample = new Grade();
      gradeExample.setStatus(Status.CURRENT);
      page = gradeRepository.findAll(Example.of(gradeExample), pageable);
    } else {
      page = gradeRepository.findByStatusAndSearchString(Status.CURRENT, searchQuery, pageable);
    }
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/grades");
    return new ResponseEntity<>(gradeMapper.gradesToGradeDTOs(page.getContent()), headers,
        HttpStatus.OK);
  }

  /**
   * EXISTS /grades/exists/ : check is site exists
   *
   * @param abbreviations the abbreviations of the gradeDTO to check
   * @return boolean true if exists otherwise false
   */
  @PostMapping("/grades/exists/")
  public ResponseEntity<Map<String, Boolean>> gradeExists(@RequestBody List<String> abbreviations) {
    Map<String, Boolean> gradeExistsMap = Maps.newHashMap();
    abbreviations = abbreviations.stream()
        .map(val -> getConverter(val).decodeUrl().toString())
        .collect(Collectors.toList());
    log.debug("REST request to check Grade exists : {}", abbreviations);
    if (!CollectionUtils.isEmpty(abbreviations)) {
      List<String> dbGradeIds = gradeRepository.findByAbbreviationsIn(abbreviations);
      abbreviations.forEach(id -> {
        boolean isMatch = dbGradeIds.stream().anyMatch(id::equalsIgnoreCase);
        gradeExistsMap.put(id, isMatch);
      });
    }
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gradeExistsMap));
  }

  /**
   * EXISTS /grades/ids/exists/ : check is site exists
   *
   * @param ids the ids of the gradeDTO to check
   * @return boolean true if exists otherwise false
   */
  @PostMapping("/grades/ids/exists/")
  public ResponseEntity<Map<Long, Boolean>> gradeIdsExists(@RequestBody List<Long> ids) {
    Map<Long, Boolean> gradeExistsMap = Maps.newHashMap();
    log.debug("REST request to check Grade exists : {}", ids);
    if (!CollectionUtils.isEmpty(ids)) {
      List<Grade> found = gradeRepository.findAllById(ids);
      Set<Long> foundIds = found.stream().map(Grade::getId).collect(Collectors.toSet());
      ids.forEach(id -> {
        if (foundIds.contains(id)) {
          gradeExistsMap.put(id, true);
        } else {
          gradeExistsMap.put(id, false);
        }
      });
    }
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gradeExistsMap));
  }


  /**
   * POST  /bulk-grades : Bulk create a new grade.
   *
   * @param gradeDTOs the gradeDTOs to create
   * @return the ResponseEntity with status 201 (Created) and with body the new gradeDTOs, or with
   * status 400 (Bad Request) if the grade has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-grades")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<GradeDTO>> bulkCreateGrade(
      @Valid @RequestBody List<GradeDTO> gradeDTOs) throws URISyntaxException {
    log.debug("REST request to bulk save Grade : {}", gradeDTOs);
    if (!Collections.isEmpty(gradeDTOs)) {
      List<GradeDTO> entitiesWithIds = gradeDTOs.stream()
          .filter(grade -> grade.getId() != null)
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil
            .createFailureAlert(StringUtils.join(entitiesWithIds, ","), "ids.exist",
                "Bulk create grades failed because grades cannot be created with ids")).body(null);
      }
    }
    List<Grade> grades = gradeMapper.gradeDTOsToGrades(gradeDTOs);
    grades = gradeRepository.saveAll(grades);
    List<GradeDTO> results = gradeMapper.gradesToGradeDTOs(grades);
    return ResponseEntity.ok()
        .body(results);
  }

  /**
   * PUT  /grades : Bulk updates an existing grade.
   *
   * @param gradeDTOs the gradeDTOs to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated gradeDTOs, or with
   * status 400 (Bad Request) if the gradeDTOs is not valid, or with status 500 (Internal Server
   * Error) if the gradeDTOs couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-grades")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<GradeDTO>> bulkUpdateGrade(
      @Valid @RequestBody List<GradeDTO> gradeDTOs) throws URISyntaxException {
    log.debug("REST request to bulk update Grade : {}", gradeDTOs);
    if (Collections.isEmpty(gradeDTOs)) {
      return ResponseEntity.badRequest()
          .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
              "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(gradeDTOs)) {
      List<GradeDTO> entitiesWithNoId = gradeDTOs.stream().filter(grades -> grades.getId() == null)
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest()
            .headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
                "bulk.update.failed.noId",
                "Some DTOs you've provided have no id, cannot update entities that don't exist"))
            .body(entitiesWithNoId);
      }
    }

    List<Grade> grades = gradeMapper.gradeDTOsToGrades(gradeDTOs);
    grades = gradeRepository.saveAll(grades);
    List<GradeDTO> results = gradeMapper.gradesToGradeDTOs(grades);

    return ResponseEntity.ok()
        .body(results);
  }

}
