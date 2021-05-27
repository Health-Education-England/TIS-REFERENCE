package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.GmcStatus;
import com.transformuk.hee.tis.reference.service.repository.GmcStatusRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for GmcStatus.
 */
@Service
public class GmcStatusServiceImpl extends AbstractReferenceService<GmcStatus> {

  private GmcStatusRepository repository;

  GmcStatusServiceImpl(GmcStatusRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<GmcStatus, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<GmcStatus> getSpecificationExecutor() {
    return repository;
  }
}
