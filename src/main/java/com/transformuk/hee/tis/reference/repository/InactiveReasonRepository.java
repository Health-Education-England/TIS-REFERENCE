package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.domain.InactiveReason;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the InactiveReason entity.
 */
@SuppressWarnings("unused")
public interface InactiveReasonRepository extends JpaRepository<InactiveReason, Long> {

}
