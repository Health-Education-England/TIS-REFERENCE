package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.model.FundingReason;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the FundingReason entity.
 */
@SuppressWarnings("unused")
public interface FundingReasonRepository extends JpaRepository<FundingReason, UUID>,
    JpaSpecificationExecutor<FundingReason> {

}
