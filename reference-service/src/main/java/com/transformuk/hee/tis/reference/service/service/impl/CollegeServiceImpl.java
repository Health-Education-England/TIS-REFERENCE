package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.College;
import com.transformuk.hee.tis.reference.service.repository.CollegeRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for College.
 */
@Service
public class CollegeServiceImpl extends AbstractReferenceService<College> {

  private CollegeRepository repository;

  CollegeServiceImpl(CollegeRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("abbreviation", "name");
  }

  @Override
  protected JpaRepository<College, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<College> getSpecificationExecutor() {
    return repository;
  }
}
