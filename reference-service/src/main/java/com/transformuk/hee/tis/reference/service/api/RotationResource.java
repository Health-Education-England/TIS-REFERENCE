package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.transformuk.hee.tis.reference.api.dto.RotationDTO;
import com.transformuk.hee.tis.reference.api.dto.validation.Create;
import com.transformuk.hee.tis.reference.api.dto.validation.Update;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.Rotation;
import com.transformuk.hee.tis.reference.service.repository.RotationRepository;
import com.transformuk.hee.tis.reference.service.service.RotationService;
import com.transformuk.hee.tis.reference.service.service.mapper.RotationMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
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
 * REST controller for managing Rotation.
 */
@RestController
@RequestMapping("/api")
public class RotationResource {

  private static final String ENTITY_NAME = "rotation";
  private final Logger log = LoggerFactory.getLogger(RotationResource.class);
  private final RotationService rotationService;
  private final RotationMapper rotationMapper;
  private final RotationRepository rotationRepository;


  public RotationResource(RotationRepository rotationRepository, RotationMapper rotationMapper,
      RotationService rotationService) {
    this.rotationService = rotationService;
    this.rotationMapper = rotationMapper;
    this.rotationRepository = rotationRepository;
  }

  /**
   * POST  /rotations : Create a new rotation.
   *
   * @param rotationDTO the rotationDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new rotationDTO, or with
   * status 400 (Bad Request) if the rotation has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/rotations")
  @Timed
  @PreAuthorize("hasPermission('tis:references::reference:', 'Create')")
  public ResponseEntity<RotationDTO> createRotation(
      @RequestBody @Validated(Create.class) RotationDTO rotationDTO) throws URISyntaxException {
    log.debug("REST request to save Rotation : {}", rotationDTO);
    if (rotationDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil
          .createFailureAlert(ENTITY_NAME, "idexists", "A new rotation cannot already have an ID"))
          .body(null);
    }
    RotationDTO result = rotationService.save(rotationDTO);
    return ResponseEntity.created(new URI("/api/rotations/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /rotations : Updates an existing rotation.
   *
   * @param rotationDTO the rotationDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated rotationDTO, or with
   * status 400 (Bad Request) if the rotationDTO is not valid, or with status 500 (Internal Server
   * Error) if the rotationDTO couldn't be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/rotations")
  @Timed
  @PreAuthorize("hasPermission('tis:references::reference:', 'Update')")
  public ResponseEntity<RotationDTO> updateRotation(
      @RequestBody @Validated(Update.class) RotationDTO rotationDTO) throws URISyntaxException {
    log.debug("REST request to update Rotation : {}", rotationDTO);
    if (rotationDTO.getId() == null) {
      return createRotation(rotationDTO);
    }
    RotationDTO result = rotationService.save(rotationDTO);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rotationDTO.getId().toString()))
        .body(result);
  }


  /**
   * GET  /rotations : get all rotations.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of rotations in body
   */
  @ApiOperation(value = "Lists rotations",
      notes = "Returns a list of rotations with support for pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "rotations list")})
  @GetMapping("/rotations")
  @Timed
  public ResponseEntity<List<RotationDTO>> getAllRotations(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson)
      throws IOException {
    log.info("REST request to get a page of rotations begin");
    searchQuery = StringConverter.getConverter(searchQuery).fromJson().decodeUrl().escapeForSql()
        .toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil
        .getColumnFilters(columnFilterJson, filterEnumList);
    Page<Rotation> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = rotationRepository.findAll(pageable);
    } else {
      page = rotationService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<RotationDTO> results = page.map(rotationMapper::toDto);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rotations");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }


  /**
   * EXISTS /rotations/exists/ : check is countries exists
   *
   * @param values the values of the countryDTO to check
   * @return boolean true if exists otherwise false
   */
  @PostMapping("/rotations/exists/")
  @Timed
  public ResponseEntity<Map<String, Boolean>> rotationsExists(@RequestBody List<String> values) {
    Map<String, Boolean> rotationExistsMap = Maps.newHashMap();
    log.debug("REST request to check Rotations exists : {}", values);
    if (!CollectionUtils.isEmpty(values)) {
      List<String> dbLabels = rotationService.findByLabelsIn(values);
      values.forEach(label -> {
        boolean isMatch = dbLabels.stream().anyMatch(label::equalsIgnoreCase);
        rotationExistsMap.put(label, isMatch);
      });
    }

    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rotationExistsMap));
  }

  /**
   * GET  /rotations/:id : get the "id" rotation.
   *
   * @param id the id of the rotationDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the rotationDTO, or with status
   * 404 (Not Found)
   */
  @GetMapping("/rotations/{id}")
  @Timed
  public ResponseEntity<RotationDTO> getRotation(@PathVariable Long id) {
    log.debug("REST request to get Rotation : {}", id);
    RotationDTO rotationDTO = rotationService.findOne(id);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rotationDTO));
  }

  /**
   * DELETE  /rotations/:id : delete the "id" rotation.
   *
   * @param id the id of the rotationDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/rotations/{id}")
  @Timed
  @PreAuthorize("hasPermission('tis:references::reference:', 'Delete')")
  public ResponseEntity<Void> deleteRotation(@PathVariable Long id) {
    log.debug("REST request to delete Rotation : {}", id);
    rotationService.delete(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }
}
