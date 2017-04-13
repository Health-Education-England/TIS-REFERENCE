package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.domain.ProgrammeMembershipType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the ProgrammeMembershipType entity.
 */
@SuppressWarnings("unused")
public interface ProgrammeMembershipTypeRepository extends JpaRepository<ProgrammeMembershipType, Long> {

}
