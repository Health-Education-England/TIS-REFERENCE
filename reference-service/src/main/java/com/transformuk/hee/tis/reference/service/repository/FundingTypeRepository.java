package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.FundingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the FundingType entity.
 */
@SuppressWarnings("unused")
public interface FundingTypeRepository extends JpaRepository<FundingType, Long>,
    JpaSpecificationExecutor {

}
