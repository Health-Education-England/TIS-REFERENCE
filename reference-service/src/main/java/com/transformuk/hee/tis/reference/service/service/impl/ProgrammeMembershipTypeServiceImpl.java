package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.ProgrammeMembershipType;
import com.transformuk.hee.tis.reference.service.repository.ProgrammeMembershipTypeRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for ProgrammeMembership.
 */
@Service
public class ProgrammeMembershipTypeServiceImpl extends
    AbstractReferenceService<ProgrammeMembershipType> {

  private ProgrammeMembershipTypeRepository repository;

  ProgrammeMembershipTypeServiceImpl(ProgrammeMembershipTypeRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<ProgrammeMembershipType, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<ProgrammeMembershipType> getSpecificationExecutor() {
    return repository;
  }
}
