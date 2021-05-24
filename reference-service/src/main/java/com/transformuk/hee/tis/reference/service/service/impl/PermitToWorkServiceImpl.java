package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.PermitToWork;
import com.transformuk.hee.tis.reference.service.repository.PermitToWorkRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
public class PermitToWorkServiceImpl extends AbstractReferenceService<PermitToWork> {

  private PermitToWorkRepository repository;

  PermitToWorkServiceImpl(PermitToWorkRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<PermitToWork, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<PermitToWork> getSpecificationExecutor() {
    return repository;
  }
}
