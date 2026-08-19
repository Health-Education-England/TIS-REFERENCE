package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.model.FundingSubType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the FundingSubType entity.
 */
@SuppressWarnings("unused")
public interface FundingSubTypeRepository extends JpaRepository<FundingSubType, UUID>,
    JpaSpecificationExecutor<FundingSubType> {

  List<FundingSubType> findByStatusAndFundingTypeId(Status status, Long fundingTypeId);

  void deleteByFundingTypeId(Long fundingTypeId);

  /**
   * Find all FundingSubTypes by a list of ids
   *
   * @param ids the list of ids to find
   * @return the list of FundingSubTypes found
   */
  List<FundingSubType> findByIdIn(List<UUID> ids);
}
