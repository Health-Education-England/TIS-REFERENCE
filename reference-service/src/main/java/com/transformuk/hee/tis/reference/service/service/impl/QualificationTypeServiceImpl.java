package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.QualificationType;
import com.transformuk.hee.tis.reference.service.repository.QualificationTypeRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for QualificationType.
 */
@Service
public class QualificationTypeServiceImpl
    extends AbstractReferenceService<QualificationType, Long> {

  private QualificationTypeRepository repository;

  QualificationTypeServiceImpl(QualificationTypeRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<QualificationType, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<QualificationType> getSpecificationExecutor() {
    return repository;
  }
}
