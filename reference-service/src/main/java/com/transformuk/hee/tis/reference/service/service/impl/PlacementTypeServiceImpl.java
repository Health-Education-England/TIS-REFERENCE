package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.PlacementType;
import com.transformuk.hee.tis.reference.service.repository.PlacementTypeRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for PlacementType.
 */
@Service
public class PlacementTypeServiceImpl extends AbstractReferenceService<PlacementType> {

  private PlacementTypeRepository repository;

  PlacementTypeServiceImpl(PlacementTypeRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<PlacementType, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<PlacementType> getSpecificationExecutor() {
    return repository;
  }
}
