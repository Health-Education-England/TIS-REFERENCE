package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.Gender;
import com.transformuk.hee.tis.reference.service.repository.GenderRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for Gender.
 */
@Service
public class GenderServiceImpl extends AbstractReferenceService<Gender> {

  private GenderRepository repository;

  GenderServiceImpl(GenderRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<Gender, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<Gender> getSpecificationExecutor() {
    return repository;
  }
}
