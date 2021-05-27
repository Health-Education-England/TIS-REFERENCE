package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.AssessmentType;
import com.transformuk.hee.tis.reference.service.repository.AssessmentTypeRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
public class AssessmentTypeServiceImpl extends AbstractReferenceService<AssessmentType> {

  private AssessmentTypeRepository repository;

  AssessmentTypeServiceImpl(AssessmentTypeRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<AssessmentType, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<AssessmentType> getSpecificationExecutor() {
    return repository;
  }
}
