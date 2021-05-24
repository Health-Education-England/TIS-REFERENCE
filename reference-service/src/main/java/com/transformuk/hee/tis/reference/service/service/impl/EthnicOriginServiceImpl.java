package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.EthnicOrigin;
import com.transformuk.hee.tis.reference.service.repository.EthnicOriginRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Collections;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
public class EthnicOriginServiceImpl extends AbstractReferenceService<EthnicOrigin> {

  private EthnicOriginRepository repository;

  EthnicOriginServiceImpl(EthnicOriginRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Collections.singletonList("code");
  }

  @Override
  protected JpaRepository<EthnicOrigin, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<EthnicOrigin> getSpecificationExecutor() {
    return repository;
  }
}
