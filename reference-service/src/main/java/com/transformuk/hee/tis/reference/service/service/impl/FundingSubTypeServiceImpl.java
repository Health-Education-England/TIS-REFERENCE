package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.model.FundingSubType;
import com.transformuk.hee.tis.reference.service.repository.FundingSubTypeRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for FundingSubType.
 */
@Service
public class FundingSubTypeServiceImpl extends AbstractReferenceService<FundingSubType, UUID> {

  private final FundingSubTypeRepository repository;

  FundingSubTypeServiceImpl(FundingSubTypeRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<FundingSubType, UUID> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<FundingSubType> getSpecificationExecutor() {
    return repository;
  }

  public List<FundingSubType> findCurrentSubTypesForFundingTypeId(Long fundingTypeId) {
    return repository.findByStatusAndFundingTypeId(Status.CURRENT, fundingTypeId);
  }
}
