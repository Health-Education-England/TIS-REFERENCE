package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.TrainingNumberTypeDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.TrainingNumberType;
import com.transformuk.hee.tis.reference.service.repository.TrainingNumberTypeRepository;
import com.transformuk.hee.tis.reference.service.service.impl.TrainingNumberTypeServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.TrainingNumberTypeMapper;
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
 * REST controller for managing TrainingNumberType.
 */
@RestController
@RequestMapping("/api")
public class TrainingNumberTypeResource {

  private static final String ENTITY_NAME = "trainingNumberType";
  private final Logger log = LoggerFactory.getLogger(TrainingNumberTypeResource.class);
  private final TrainingNumberTypeRepository trainingNumberTypeRepository;

  private final TrainingNumberTypeMapper trainingNumberTypeMapper;
  private final TrainingNumberTypeServiceImpl trainingNumberTypeService;

  public TrainingNumberTypeResource(TrainingNumberTypeRepository trainingNumberTypeRepository,
                                    TrainingNumberTypeMapper trainingNumberTypeMapper,
                                    TrainingNumberTypeServiceImpl trainingNumberTypeService) {
    this.trainingNumberTypeRepository = trainingNumberTypeRepository;
    this.trainingNumberTypeMapper = trainingNumberTypeMapper;
    this.trainingNumberTypeService = trainingNumberTypeService;
  }

  /**
   * POST  /training-number-types : Create a new trainingNumberType.
   *
   * @param trainingNumberTypeDTO the trainingNumberTypeDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new trainingNumberTypeDTO, or with status 400 (Bad Request) if the trainingNumberType has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/training-number-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<TrainingNumberTypeDTO> createTrainingNumberType(@Valid @RequestBody TrainingNumberTypeDTO trainingNumberTypeDTO) throws URISyntaxException {
    log.debug("REST request to save TrainingNumberType : {}", trainingNumberTypeDTO);
    if (trainingNumberTypeDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new trainingNumberType cannot already have an ID")).body(null);
    }
    TrainingNumberType trainingNumberType = trainingNumberTypeMapper.trainingNumberTypeDTOToTrainingNumberType(trainingNumberTypeDTO);
    trainingNumberType = trainingNumberTypeRepository.save(trainingNumberType);
    TrainingNumberTypeDTO result = trainingNumberTypeMapper.trainingNumberTypeToTrainingNumberTypeDTO(trainingNumberType);
    return ResponseEntity.created(new URI("/api/training-number-types/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /training-number-types : Updates an existing trainingNumberType.
   *
   * @param trainingNumberTypeDTO the trainingNumberTypeDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated trainingNumberTypeDTO,
   * or with status 400 (Bad Request) if the trainingNumberTypeDTO is not valid,
   * or with status 500 (Internal Server Error) if the trainingNumberTypeDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/training-number-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<TrainingNumberTypeDTO> updateTrainingNumberType(@Valid @RequestBody TrainingNumberTypeDTO trainingNumberTypeDTO) throws URISyntaxException {
    log.debug("REST request to update TrainingNumberType : {}", trainingNumberTypeDTO);
    if (trainingNumberTypeDTO.getId() == null) {
      return createTrainingNumberType(trainingNumberTypeDTO);
    }
    TrainingNumberType trainingNumberType = trainingNumberTypeMapper.trainingNumberTypeDTOToTrainingNumberType(trainingNumberTypeDTO);
    trainingNumberType = trainingNumberTypeRepository.save(trainingNumberType);
    TrainingNumberTypeDTO result = trainingNumberTypeMapper.trainingNumberTypeToTrainingNumberTypeDTO(trainingNumberType);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, trainingNumberTypeDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /training-number-types : get all training number types.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of training number types in body
   */
  @ApiOperation(value = "Lists training number types",
      notes = "Returns a list of training number types with support for pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "training number types list")})
  @GetMapping("/training-number-types")
  @Timed
  public ResponseEntity<List<TrainingNumberTypeDTO>> getAllTrainingNumberTypes(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.info("REST request to get a page of training number types begin");
    searchQuery = StringConverter.getConverter(searchQuery).fromJson().decodeUrl().escapeForSql().toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    Page<TrainingNumberType> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = trainingNumberTypeRepository.findAll(pageable);
    } else {
      page = trainingNumberTypeService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<TrainingNumberTypeDTO> results = page.map(trainingNumberTypeMapper::trainingNumberTypeToTrainingNumberTypeDTO);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/training-number-types");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /training-number-types/:id : get the "id" trainingNumberType.
   *
   * @param id the id of the trainingNumberTypeDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the trainingNumberTypeDTO, or with status 404 (Not Found)
   */
  @GetMapping("/training-number-types/{id}")
  @Timed
  public ResponseEntity<TrainingNumberTypeDTO> getTrainingNumberType(@PathVariable Long id) {
    log.debug("REST request to get TrainingNumberType : {}", id);
    TrainingNumberType trainingNumberType = trainingNumberTypeRepository.findOne(id);
    TrainingNumberTypeDTO trainingNumberTypeDTO = trainingNumberTypeMapper.trainingNumberTypeToTrainingNumberTypeDTO(trainingNumberType);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(trainingNumberTypeDTO));
  }

  /**
   * DELETE  /training-number-types/:id : delete the "id" trainingNumberType.
   *
   * @param id the id of the trainingNumberTypeDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/training-number-types/{id}")
  @Timed
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteTrainingNumberType(@PathVariable Long id) {
    log.debug("REST request to delete TrainingNumberType : {}", id);
    trainingNumberTypeRepository.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }


  /**
   * POST  /bulk-training-number-types : Bulk create a new training-number-types.
   *
   * @param trainingNumberTypeDTOS List of the trainingNumberTypeDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new trainingNumberTypeDTOS, or with status 400 (Bad Request) if the TrainingNumberTypeDto has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-training-number-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<TrainingNumberTypeDTO>> bulkCreateTrainingNumberType(@Valid @RequestBody List<TrainingNumberTypeDTO> trainingNumberTypeDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save TrainingNumberTypeDtos : {}", trainingNumberTypeDTOS);
    if (!Collections.isEmpty(trainingNumberTypeDTOS)) {
      List<Long> entityIds = trainingNumberTypeDTOS.stream()
          .filter(tnt -> tnt.getId() != null)
          .map(tnt -> tnt.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new trainingNumberTypes cannot already have an ID")).body(null);
      }
    }
    List<TrainingNumberType> trainingNumberTypes = trainingNumberTypeMapper.trainingNumberTypeDTOsToTrainingNumberTypes(trainingNumberTypeDTOS);
    trainingNumberTypes = trainingNumberTypeRepository.save(trainingNumberTypes);
    List<TrainingNumberTypeDTO> result = trainingNumberTypeMapper.trainingNumberTypesToTrainingNumberTypeDTOs(trainingNumberTypes);
    return ResponseEntity.ok()
        .body(result);
  }

  /**
   * PUT  /bulk-training-number-types : Updates an existing training-number-types.
   *
   * @param trainingNumberTypeDTOS List of the trainingNumberTypeDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated trainingNumberTypeDTOS,
   * or with status 400 (Bad Request) if the trainingNumberTypeDTOS is not valid,
   * or with status 500 (Internal Server Error) if the trainingNumberTypeDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-training-number-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<TrainingNumberTypeDTO>> bulkUpdateTrainingNumberType(@Valid @RequestBody List<TrainingNumberTypeDTO> trainingNumberTypeDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update TrainingNumberTypeDtos : {}", trainingNumberTypeDTOS);
    if (Collections.isEmpty(trainingNumberTypeDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(trainingNumberTypeDTOS)) {
      List<TrainingNumberTypeDTO> entitiesWithNoId = trainingNumberTypeDTOS.stream().filter(tnt -> tnt.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<TrainingNumberType> trainingNumberTypes = trainingNumberTypeMapper.trainingNumberTypeDTOsToTrainingNumberTypes(trainingNumberTypeDTOS);
    trainingNumberTypes = trainingNumberTypeRepository.save(trainingNumberTypes);
    List<TrainingNumberTypeDTO> results = trainingNumberTypeMapper.trainingNumberTypesToTrainingNumberTypeDTOs(trainingNumberTypes);
    return ResponseEntity.ok()
        .body(results);
  }
}
