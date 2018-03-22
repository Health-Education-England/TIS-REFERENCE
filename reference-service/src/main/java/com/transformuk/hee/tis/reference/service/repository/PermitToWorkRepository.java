package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.PermitToWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the PermitToWork entity.
 */
@SuppressWarnings("unused")
public interface PermitToWorkRepository extends JpaRepository<PermitToWork, Long>, JpaSpecificationExecutor {

}
