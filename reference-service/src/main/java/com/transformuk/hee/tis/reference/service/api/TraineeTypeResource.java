package com.transformuk.hee.tis.reference.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.TraineeTypeDto;
import com.transformuk.hee.tis.reference.api.dto.validation.Create;
import com.transformuk.hee.tis.reference.api.dto.validation.Update;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.api.util.UrlDecoderUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.TraineeType;
import com.transformuk.hee.tis.reference.service.service.impl.TraineeTypeServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.TraineeTypeMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
 * REST controller for managing TraineeType.
 */
@Slf4j
@RestController
@RequestMapping("/api/trainee-types")
public class TraineeTypeResource {

  private static final String ENTITY_NAME = "traineeType";

  private final TraineeTypeServiceImpl service;
  private final TraineeTypeMapper mapper;

  public TraineeTypeResource(TraineeTypeServiceImpl service, TraineeTypeMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  /**
   * POST : Create a new trainee type.
   *
   * @param dto the dto of the trainee type to create.
   * @return the ResponseEntity with status 201 (Created) and with body the new dto, or with status
   *     400 (Bad Request) if the trainee type already has an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<TraineeTypeDto> createTraineeType(
      @Validated(Create.class) @RequestBody TraineeTypeDto dto) throws URISyntaxException {
    log.debug("REST request to save TraineeType : {}", dto);

    TraineeType entity = mapper.toEntity(dto);
    entity = service.save(entity);
    TraineeTypeDto result = mapper.toDto(entity);
    return ResponseEntity.created(new URI("/api/trainee-types/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT : Updates an existing trainee type.
   *
   * @param dto the trainee type to update.
   * @return the ResponseEntity with status 200 (OK) and with body the updated trainee type, or with
   *     status 400 (Bad Request) if the dto is not valid.
   */
  @PutMapping
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<TraineeTypeDto> updateTraineeType(
      @Validated(Update.class) @RequestBody TraineeTypeDto dto) {
    log.debug("REST request to update TraineeType : {}", dto);

    TraineeType entity = mapper.toEntity(dto);
    entity = service.save(entity);
    TraineeTypeDto result = mapper.toDto(entity);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dto.getId().toString()))
        .body(result);
  }

  /**
   * GET /:id : get the trainee type by id.
   *
   * @param id the id of the trainee type to retrieve.
   * @return the ResponseEntity with status 200 (OK) and with body the trainee type, or with status
   *     404 (Not Found).
   */
  @GetMapping("/{id}")
  public ResponseEntity<TraineeTypeDto> getTraineeType(@PathVariable UUID id) {
    log.debug("REST request to get TraineeType : {}", id);
    TraineeType entity = service.findById(id).orElse(null);
    TraineeTypeDto dto = mapper.toDto(entity);
    return ResponseEntity.of(Optional.ofNullable(dto));
  }

  /**
   * GET : get all trainee types.
   *
   * @param pageable         the pagination information.
   * @param searchQuery      the free text to search the code and label for.
   * @param columnFilterJson the column filters to apply.
   * @return the ResponseEntity with status 200 (OK) and the list of trainee types in body.
   * @throws IOException if the column filters could not be parsed.
   */
  @GetMapping
  public ResponseEntity<List<TraineeTypeDto>> getAllTraineeTypes(Pageable pageable,
      @RequestParam(required = false) String searchQuery,
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson)
      throws IOException {
    log.info("REST request to get a page of trainee types begin");
    searchQuery = StringConverter.getConverter(searchQuery).fromJson().decodeUrl().escapeForSql()
        .toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    if (columnFilterJson != null) {
      columnFilterJson = UrlDecoderUtil.decode(columnFilterJson);
    }
    List<ColumnFilter> columnFilters = ColumnFilterUtil
        .getColumnFilters(columnFilterJson, filterEnumList);
    Page<TraineeType> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = service.findAll(pageable);
    } else {
      page = service.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<TraineeTypeDto> results = page.map(mapper::toDto);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/trainee-types");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }
}
