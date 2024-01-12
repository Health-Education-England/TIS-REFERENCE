package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.TariffRate;
import com.transformuk.hee.tis.reference.service.repository.TariffRateRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for TariffRate.
 */
@Service
public class TariffRateServiceImpl extends AbstractReferenceService<TariffRate, Long> {

  private TariffRateRepository repository;

  TariffRateServiceImpl(TariffRateRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "gradeAbbreviation");
  }

  @Override
  protected JpaRepository<TariffRate, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<TariffRate> getSpecificationExecutor() {
    return repository;
  }
}
