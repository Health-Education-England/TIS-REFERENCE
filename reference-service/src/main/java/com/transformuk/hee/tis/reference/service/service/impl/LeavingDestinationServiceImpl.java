package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.LeavingDestination;
import com.transformuk.hee.tis.reference.service.repository.LeavingDestinationRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for LeavingDestination.
 */
@Service
public class LeavingDestinationServiceImpl
    extends AbstractReferenceService<LeavingDestination, Long> {

  private LeavingDestinationRepository repository;

  LeavingDestinationServiceImpl(LeavingDestinationRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<LeavingDestination, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<LeavingDestination> getSpecificationExecutor() {
    return repository;
  }
}
