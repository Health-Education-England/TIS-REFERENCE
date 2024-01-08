package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.TrainingNumberType;
import com.transformuk.hee.tis.reference.service.repository.TrainingNumberTypeRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for TrainingNumber.
 */
@Service
public class TrainingNumberTypeServiceImpl extends AbstractReferenceService<TrainingNumberType, Long> {

  private TrainingNumberTypeRepository repository;

  TrainingNumberTypeServiceImpl(TrainingNumberTypeRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<TrainingNumberType, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<TrainingNumberType> getSpecificationExecutor() {
    return repository;
  }
}
