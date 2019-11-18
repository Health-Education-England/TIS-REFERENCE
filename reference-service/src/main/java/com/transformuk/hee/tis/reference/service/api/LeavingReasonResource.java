package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.api.dto.LeavingReasonDto;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.model.LeavingReason;
import com.transformuk.hee.tis.reference.service.service.LeavingReasonService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LeavingReasonResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(LeavingReasonResource.class);

  private static final String ENTITY_NAME = LeavingReason.class.getSimpleName();

  private final LeavingReasonService service;

  public LeavingReasonResource(LeavingReasonService service) {
    this.service = service;
  }

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

  @GetMapping("/leaving-reasons")
  public ResponseEntity<List<LeavingReasonDto>> getAllLeavingReasons() {
    LOGGER.debug("REST request to get all leaving reasons.");
    return ResponseEntity.ok(service.findAll());
  }

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
