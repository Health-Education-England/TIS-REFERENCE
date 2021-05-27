package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.MaritalStatus;
import com.transformuk.hee.tis.reference.service.repository.MaritalStatusRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
public class MaritalStatusServiceImpl extends AbstractReferenceService<MaritalStatus> {

  private MaritalStatusRepository repository;

  MaritalStatusServiceImpl(MaritalStatusRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<MaritalStatus, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<MaritalStatus> getSpecificationExecutor() {
    return repository;
  }
}
