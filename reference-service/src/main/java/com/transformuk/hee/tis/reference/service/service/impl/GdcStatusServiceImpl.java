package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.GdcStatus;
import com.transformuk.hee.tis.reference.service.repository.GdcStatusRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for GdcStatus.
 */
@Service
public class GdcStatusServiceImpl extends AbstractReferenceService<GdcStatus> {

  private GdcStatusRepository repository;

  GdcStatusServiceImpl(GdcStatusRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<GdcStatus, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<GdcStatus> getSpecificationExecutor() {
    return repository;
  }
}
