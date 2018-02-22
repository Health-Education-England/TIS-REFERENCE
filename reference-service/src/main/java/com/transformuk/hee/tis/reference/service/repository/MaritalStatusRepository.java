package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.MaritalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the MaritalStatus entity.
 */
@SuppressWarnings("unused")
public interface MaritalStatusRepository extends JpaRepository<MaritalStatus, Long>, JpaSpecificationExecutor {

}
