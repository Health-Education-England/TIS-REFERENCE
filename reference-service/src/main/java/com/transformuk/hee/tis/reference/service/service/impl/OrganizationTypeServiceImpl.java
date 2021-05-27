package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.OrganizationType;
import com.transformuk.hee.tis.reference.service.repository.OrganizationTypeRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
public class OrganizationTypeServiceImpl extends AbstractReferenceService<OrganizationType> {

  private OrganizationTypeRepository repository;

  OrganizationTypeServiceImpl(OrganizationTypeRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<OrganizationType, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<OrganizationType> getSpecificationExecutor() {
    return repository;
  }
}
