package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.FundingType;
import com.transformuk.hee.tis.reference.service.repository.FundingTypeRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
public class FundingTypeServiceImpl extends AbstractReferenceService<FundingType> {

  private FundingTypeRepository repository;

  FundingTypeServiceImpl(FundingTypeRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<FundingType, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<FundingType> getSpecificationExecutor() {
    return repository;
  }
}
