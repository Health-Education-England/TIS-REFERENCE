package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.InactiveReason;
import com.transformuk.hee.tis.reference.service.repository.InactiveReasonRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for InactiveReason.
 */
@Service
public class InactiveReasonServiceImpl extends AbstractReferenceService<InactiveReason, Long> {

  private InactiveReasonRepository repository;

  InactiveReasonServiceImpl(InactiveReasonRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<InactiveReason, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<InactiveReason> getSpecificationExecutor() {
    return repository;
  }
}
