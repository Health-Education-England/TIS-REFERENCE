package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.ProgrammeMembershipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the ProgrammeMembershipType entity.
 */
@SuppressWarnings("unused")
public interface ProgrammeMembershipTypeRepository extends
    JpaRepository<ProgrammeMembershipType, Long>, JpaSpecificationExecutor {

}
