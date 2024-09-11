package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.model.FundingReason;
import com.transformuk.hee.tis.reference.service.repository.FundingReasonRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for FundingReason.
 */
@Service
public class FundingReasonServiceImpl extends AbstractReferenceService<FundingReason, UUID> {

  private final FundingReasonRepository repository;

  FundingReasonServiceImpl(FundingReasonRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("reason");
  }

  @Override
  protected JpaRepository<FundingReason, UUID> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<FundingReason> getSpecificationExecutor() {
    return repository;
  }
}
