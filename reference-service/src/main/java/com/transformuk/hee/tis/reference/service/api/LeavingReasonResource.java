package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.api.dto.LeavingReasonDto;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.model.LeavingReason;
import com.transformuk.hee.tis.reference.service.service.LeavingReasonService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
