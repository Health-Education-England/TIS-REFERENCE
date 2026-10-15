package com.transformuk.hee.tis.reference.service.service.impl;

import com.transformuk.hee.tis.reference.service.model.FundingType;
import com.transformuk.hee.tis.reference.service.repository.FundingTypeRepository;
import com.transformuk.hee.tis.reference.service.service.AbstractReferenceService;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

/**
 * The reference service implementation for FundingType.
 */
@Service
public class FundingTypeServiceImpl extends AbstractReferenceService<FundingType, Long> {

  private FundingTypeRepository repository;

  FundingTypeServiceImpl(FundingTypeRepository repository) {
    this.repository = repository;
  }

  @Override
  protected List<String> getSearchFields() {
    return Arrays.asList("code", "label");
  }

  @Override
  protected JpaRepository<FundingType, Long> getRepository() {
    return repository;
  }

  @Override
  protected JpaSpecificationExecutor<FundingType> getSpecificationExecutor() {
    return repository;
  }

  /**
   * Finds a list of FundingType by their ids.
   *
   * @param ids the list of ids to search by
   * @return the list of FundingType found by the ids
   */
  public List<FundingType> findByIds(List<Long> ids) {
    return repository.findAllById(ids);
  }
}
