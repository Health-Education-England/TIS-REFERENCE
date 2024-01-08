package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.ReligiousBelief;
import com.transformuk.hee.tis.reference.service.repository.ReligiousBeliefRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for ReligiousBelief.
 */
@Service
public class ReligiousBeliefServiceImpl extends AbstractReferenceService<ReligiousBelief, Long> {

  private ReligiousBeliefRepository repository;

  ReligiousBeliefServiceImpl(ReligiousBeliefRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<ReligiousBelief, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<ReligiousBelief> getSpecificationExecutor() {
    return repository;
  }
}
