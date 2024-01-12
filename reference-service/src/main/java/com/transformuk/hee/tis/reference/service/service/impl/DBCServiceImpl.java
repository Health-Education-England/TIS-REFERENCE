package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.DBC;
import com.transformuk.hee.tis.reference.service.repository.DBCRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for DBC.
 */
@Service
public class DBCServiceImpl extends AbstractReferenceService<DBC, Long> {

  private DBCRepository repository;

  DBCServiceImpl(DBCRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("dbc", "name", "abbr");
  }

  @Override
  protected JpaRepository<DBC, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<DBC> getSpecificationExecutor() {
    return repository;
  }
}
