package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.domain.LeavingDestination;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the LeavingDestination entity.
 */
@SuppressWarnings("unused")
public interface LeavingDestinationRepository extends JpaRepository<LeavingDestination, Long> {

}
