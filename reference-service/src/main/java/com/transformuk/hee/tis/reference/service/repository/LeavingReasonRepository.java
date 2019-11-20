package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.LeavingReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the {@link LeavingReason} entity.
 */
public interface LeavingReasonRepository extends JpaRepository<LeavingReason, Long>,
    JpaSpecificationExecutor {

}
