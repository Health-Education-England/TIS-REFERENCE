package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.api.dto.LeavingReasonDto;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.LeavingReason;
import com.transformuk.hee.tis.reference.service.service.LeavingReasonService;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import javax.validation.Valid;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
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
 * REST controller for managing LeavingReason.
 */
@RestController
@RequestMapping("/api")
public class LeavingReasonResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(LeavingReasonResource.class);

  private static final String ENTITY_NAME = LeavingReason.class.getSimpleName();

  private final LeavingReasonService service;

  public LeavingReasonResource(LeavingReasonService service) {
    this.service = service;
  }

  /**
   * POST /leavning-reasons : Create a new leaving reason.
   *
   * @param leavingReasonDto The LeavingReasonDto to create.
   * @return A {@link ResponseEntity} with status 201 (CREATED) and a body of the created leaving
   *     reason if the creation was successful, or with status 400 (BAD REQUEST) if the DTO
   *     contained an ID.
   * @throws URISyntaxException If the new ID could not be built in to a URI to return.
   */
  @PostMapping("/leaving-reasons")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<LeavingReasonDto> createLeavingReason(
      @Valid @RequestBody LeavingReasonDto leavingReasonDto) throws URISyntaxException {
    LOGGER.debug("REST request to create leaving reason.");

    // If the leaving reason DTO contains an ID then it cannot be created.
    if (leavingReasonDto.getId() != null) {
      return ResponseEntity.badRequest()
          .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists",
              "A new entity cannot already have an ID."))
          .body(null);
    }

    LeavingReasonDto createdDto = service.save(leavingReasonDto);
    Long createdId = createdDto.getId();

    return ResponseEntity.created(new URI("/api/leaving-reasons/" + createdId))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, createdId.toString()))
        .body(createdDto);
  }

  /**
   * PUT /leavning-reasons : Update an existing leaving reason, or create a new one.
   *
   * @param leavingReasonDto The LeavingReasonDto to use for the update.
   * @return A {@link ResponseEntity} with a body of the new or updated leaving reason and with
   *     status 200 (OK) if the update was successful or with status 201 (CREATED) if no leaving
   *     reason exists with the ID.
   * @throws URISyntaxException If the new ID could not be built in to a URI to return.
   */
  @PutMapping("/leaving-reasons")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<LeavingReasonDto> updateLeavingReason(
      @Valid @RequestBody LeavingReasonDto leavingReasonDto) throws URISyntaxException {
    LOGGER.debug("REST request to update leaving reason.");

    // If the leaving reason DTO contains an ID then it should be created instead of updated.
    if (leavingReasonDto.getId() == null) {
      return createLeavingReason(leavingReasonDto);
    }

    LeavingReasonDto updatedDto = service.save(leavingReasonDto);

    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, updatedDto.getId().toString()))
        .body(updatedDto);
  }

  /**
   * GET /leaving-reasons/:id : get the leaving reason with matching ID.
   *
   * @param id The id of the leaving reason to retrieve.
   * @return A {@link ResponseEntity} with status 200 (OK) and body of LeavingReasonDto if found, or
   *     with status 404 (NOT FOUND) if no leaving reason exists with the ID
   */
  @GetMapping("/leaving-reasons/{id}")
  public ResponseEntity<LeavingReasonDto> getLeavingReason(@PathVariable Long id) {
    LOGGER.debug("REST request to get leaving reason with id {}.", id);

    LeavingReasonDto leavingReasonDto = service.find(id);

    if (leavingReasonDto != null) {
      return ResponseEntity.ok(leavingReasonDto);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idnotexists",
              "A entity with the given ID could not be found."))
          .body(null);
    }
  }

  /**
   * GET /leaving-reasons : get all leaving reasons.
   *
   * @param columnFiltersJson The column filter to apply as JSON. e.g. "columnFilters={ "status" :
   *                          ["CURRENT"] }"
   * @param searchQuery       The string to filter leaving reasons by.
   * @return A {@link ResponseEntity} with status 200 (OK) and body of a List of LeavingReasonDtos,
   *     matching the colummn filter, list is empty if no leaving reasons are found.
   * @throws IOException If the column filter JSON could not be parsed.
   */
  @GetMapping("/leaving-reasons")
  public ResponseEntity<List<LeavingReasonDto>> getAllLeavingReasons(
      @RequestParam(name = "columnFilters", required = false) String columnFiltersJson,
      @RequestParam(value = "searchQuery", required = false) String searchQuery)
      throws IOException {
    LOGGER.debug("REST request to get all leaving reasons.");
    searchQuery = StringConverter.getConverter(searchQuery).fromJson().decodeUrl().escapeForSql()
        .toString();

    List<Class> filterEnums = Collections.singletonList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil
        .getColumnFilters(columnFiltersJson, filterEnums);

    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFiltersJson)) {
      return ResponseEntity.ok(service.findAll());
    } else {
      return ResponseEntity.ok(service.findAll(searchQuery, columnFilters));
    }
  }

  /**
   * DELETE /leaving-reasons/:id : delete the leaving reason with matching ID.
   *
   * @param id The id of the leaving reason to delete.
   * @return A {@link ResponseEntity} with status 200 (OK) if the delete was successful, or with
   *     status 404 (NOT FOUND) if no leaving reason exists with the ID.
   */
  @DeleteMapping("/leaving-reasons/{id}")
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteLeavingReason(@PathVariable Long id) {
    LOGGER.debug("REST request to delete leaving reason with id {}.", id);

    try {
      service.delete(id);
      return ResponseEntity.ok()
          .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString()))
          .build();
    } catch (EmptyResultDataAccessException e) {
      return ResponseEntity.notFound()
          .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idnotexists",
              "A entity with the given ID could not be found."))
          .build();
    }
  }
}
