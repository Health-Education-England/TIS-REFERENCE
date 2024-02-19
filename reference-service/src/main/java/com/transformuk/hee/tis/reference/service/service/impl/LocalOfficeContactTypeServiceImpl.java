package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.LocalOfficeContactType;
import com.transformuk.hee.tis.reference.service.repository.LocalOfficeContactTypeRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for LocalOfficeContactType.
 */
@Service
public class LocalOfficeContactTypeServiceImpl extends
    AbstractReferenceService<LocalOfficeContactType, UUID> {

  private final LocalOfficeContactTypeRepository repository;

  LocalOfficeContactTypeServiceImpl(LocalOfficeContactTypeRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<LocalOfficeContactType, UUID> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<LocalOfficeContactType> getSpecificationExecutor() {
    return repository;
  }
}
