package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.Grade;
import com.transformuk.hee.tis.reference.service.repository.GradeRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for Grade.
 */
@Service
public class GradeServiceImpl extends AbstractReferenceService<Grade> {

  private GradeRepository repository;

  GradeServiceImpl(GradeRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("name", "abbreviation", "label");
  }

  @Override
  protected JpaRepository<Grade, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<Grade> getSpecificationExecutor() {
    return repository;
  }
}
