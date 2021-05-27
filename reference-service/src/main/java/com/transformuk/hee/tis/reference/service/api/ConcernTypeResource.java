package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.api.dto.ConcernTypeDto;
import com.transformuk.hee.tis.reference.service.model.ConcernType;
import com.transformuk.hee.tis.reference.service.repository.ConcernTypeRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.ConcernTypeMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing ConcernType.
 */
@RestController
@RequestMapping("/api")
public class ConcernTypeResource {

  private final Logger log = LoggerFactory.getLogger(ConcernTypeResource.class);
  private final ConcernTypeRepository concernTypeRepository;
  private final ConcernTypeMapper concernTypeMapper;

  public ConcernTypeResource(ConcernTypeRepository concernTypeRepository,
      ConcernTypeMapper concernTypeMapper) {
    this.concernTypeRepository = concernTypeRepository;
    this.concernTypeMapper = concernTypeMapper;
  }

  @GetMapping("/concern-types")
  public ResponseEntity<List<ConcernTypeDto>> getAllConcernTypes() {
    List<ConcernType> concernTypeList = concernTypeRepository.findAll();
    List<ConcernTypeDto> results = concernTypeMapper
        .concernTypesToConcernTypeDtos(concernTypeList);
    return new ResponseEntity<>(results, HttpStatus.OK);
  }
}
