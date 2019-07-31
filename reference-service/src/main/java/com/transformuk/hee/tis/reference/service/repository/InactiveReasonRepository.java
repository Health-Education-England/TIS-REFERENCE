package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.InactiveReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the InactiveReason entity.
 */
@SuppressWarnings("unused")
public interface InactiveReasonRepository extends JpaRepository<InactiveReason, Long>,
    JpaSpecificationExecutor {

}
