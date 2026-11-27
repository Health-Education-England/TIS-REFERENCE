package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.TraineeType;
import com.transformuk.hee.tis.reference.service.repository.TraineeTypeRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for TraineeType.
 */
@Service
public class TraineeTypeServiceImpl extends AbstractReferenceService<TraineeType, UUID> {

  private final TraineeTypeRepository repository;

  TraineeTypeServiceImpl(TraineeTypeRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<TraineeType, UUID> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<TraineeType> getSpecificationExecutor() {
    return repository;
  }
}
