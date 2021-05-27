package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.FundingIssue;
import com.transformuk.hee.tis.reference.service.repository.FundingIssueRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Collections;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for FundingIssue.
 */
@Service
public class FundingIssueServiceImpl extends AbstractReferenceService<FundingIssue> {

  private FundingIssueRepository repository;

  FundingIssueServiceImpl(FundingIssueRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Collections.singletonList("code");
  }

  @Override
  protected JpaRepository<FundingIssue, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<FundingIssue> getSpecificationExecutor() {
    return repository;
  }
}
