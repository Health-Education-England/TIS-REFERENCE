package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.RecordType;
import com.transformuk.hee.tis.reference.service.repository.RecordTypeRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for RecordType.
 */
@Service
public class RecordTypeServiceImpl extends AbstractReferenceService<RecordType, Long> {

  private RecordTypeRepository repository;

  RecordTypeServiceImpl(RecordTypeRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<RecordType, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<RecordType> getSpecificationExecutor() {
    return repository;
  }
}
