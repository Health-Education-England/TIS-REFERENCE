package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.api.dto.ConnectionSourceDto;
import com.transformuk.hee.tis.reference.service.model.ConnectionSource;
import com.transformuk.hee.tis.reference.service.repository.ConnectionSourceRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.ConnectionSourceMapper;
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
public class ConnectionSourceResource {

  private final Logger log = LoggerFactory.getLogger(ConnectionSourceResource.class);
  private final ConnectionSourceRepository connectionSourceRepository;
  private final ConnectionSourceMapper connectionSourceMapper;

  public ConnectionSourceResource(ConnectionSourceRepository connectionSourceRepository,
      ConnectionSourceMapper connectionSourceMapper) {
    this.connectionSourceRepository = connectionSourceRepository;
    this.connectionSourceMapper = connectionSourceMapper;
  }

  @GetMapping("/sources")
  public ResponseEntity<List<ConnectionSourceDto>> getAllConnectionSources() {
    List<ConnectionSource> connectionSourceList = connectionSourceRepository.findAll();
    List<ConnectionSourceDto> results = connectionSourceMapper
        .connectionSourcesToConnectionSourceDtos(connectionSourceList);
    return new ResponseEntity<>(results, HttpStatus.OK);
  }
}