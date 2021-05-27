package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.SexualOrientation;
import com.transformuk.hee.tis.reference.service.repository.SexualOrientationRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for SexualOrientation.
 */
@Service
public class SexualOrientationServiceImpl extends AbstractReferenceService<SexualOrientation> {

  private SexualOrientationRepository repository;

  SexualOrientationServiceImpl(SexualOrientationRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<SexualOrientation, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<SexualOrientation> getSpecificationExecutor() {
    return repository;
  }
}
