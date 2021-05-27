package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.QualificationReference;
import com.transformuk.hee.tis.reference.service.repository.QualificationReferenceRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for Qualification.
 */
@Service
public class QualificationReferenceServiceImpl extends
    AbstractReferenceService<QualificationReference> {

  private QualificationReferenceRepository repository;

  QualificationReferenceServiceImpl(QualificationReferenceRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<QualificationReference, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<QualificationReference> getSpecificationExecutor() {
    return repository;
  }
}
