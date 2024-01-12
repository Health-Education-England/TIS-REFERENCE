package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.Nationality;
import com.transformuk.hee.tis.reference.service.repository.NationalityRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for Nationality.
 */
@Service
public class NationalityServiceImpl extends AbstractReferenceService<Nationality, Long> {

  private NationalityRepository repository;

  NationalityServiceImpl(NationalityRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("countryNumber", "nationality");
  }

  @Override
  protected JpaRepository<Nationality, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<Nationality> getSpecificationExecutor() {
    return repository;
  }
}
