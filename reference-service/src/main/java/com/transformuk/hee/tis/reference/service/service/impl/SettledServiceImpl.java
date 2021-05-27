package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.Settled;
import com.transformuk.hee.tis.reference.service.repository.SettledRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
public class SettledServiceImpl extends AbstractReferenceService<Settled> {

  private SettledRepository repository;

  SettledServiceImpl(SettledRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<Settled, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<Settled> getSpecificationExecutor() {
    return repository;
  }
}
