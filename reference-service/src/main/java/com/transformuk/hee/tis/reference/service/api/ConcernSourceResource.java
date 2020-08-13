package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.api.dto.ConcernSourceDto;
import com.transformuk.hee.tis.reference.service.model.ConcernSource;
import com.transformuk.hee.tis.reference.service.repository.ConcernSourceRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.ConcernSourceMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ConcernSourceResource {

  private final Logger log = LoggerFactory.getLogger(ConcernSourceResource.class);
  private final ConcernSourceRepository concernSourceRepository;
  private final ConcernSourceMapper concernSourceMapper;

  public ConcernSourceResource(ConcernSourceRepository concernSourceRepository,
      ConcernSourceMapper concernSourceMapper) {
    this.concernSourceRepository = concernSourceRepository;
    this.concernSourceMapper = concernSourceMapper;
  }

  @GetMapping("/sources")
  public ResponseEntity<List<ConcernSourceDto>> getAllConcernSources() {
    List<ConcernSource> concernSourceList = concernSourceRepository.findAll();
    List<ConcernSourceDto> results = concernSourceMapper
        .concernSourcesToConcernSourceDtos(concernSourceList);
    return new ResponseEntity<>(results, HttpStatus.OK);
  }
}