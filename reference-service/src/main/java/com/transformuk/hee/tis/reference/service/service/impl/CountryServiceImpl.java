package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.Country;
import com.transformuk.hee.tis.reference.service.repository.CountryRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for Country.
 */
@Service
public class CountryServiceImpl extends AbstractReferenceService<Country> {

  private CountryRepository repository;

  CountryServiceImpl(CountryRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("countryNumber", "nationality");
  }

  @Override
  protected JpaRepository<Country, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<Country> getSpecificationExecutor() {
    return repository;
  }
}
