package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.FundingSubType;
import com.transformuk.hee.tis.reference.service.model.FundingType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the FundingSubType entity.
 */
@SuppressWarnings("unused")
public interface FundingSubTypeRepository extends JpaRepository<FundingSubType, UUID>,
    JpaSpecificationExecutor<FundingSubType> {
}
