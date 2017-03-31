package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.domain.LeavingDestination;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the LeavingDestination entity.
 */
@SuppressWarnings("unused")
public interface LeavingDestinationRepository extends JpaRepository<LeavingDestination, Long> {

}
