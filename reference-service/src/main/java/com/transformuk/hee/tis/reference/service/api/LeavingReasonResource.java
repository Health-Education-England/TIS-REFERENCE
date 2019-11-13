package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.api.dto.LeavingReasonDto;
import com.transformuk.hee.tis.reference.service.service.LeavingReasonService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LeavingReasonResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(LeavingReasonResource.class);

  private final LeavingReasonService service;

  public LeavingReasonResource(LeavingReasonService service) {
    this.service = service;
  }

  @GetMapping("/leaving-reasons")
  public ResponseEntity<List<LeavingReasonDto>> getAllLeavingReasons() {
    LOGGER.debug("REST request to get all leaving reasons.");
    return ResponseEntity.ok(service.findAll());
  }
}
