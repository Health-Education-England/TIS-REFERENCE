package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the Status entity.
 */
@SuppressWarnings("unused")
public interface StatusRepository extends JpaRepository<Status, Long>, JpaSpecificationExecutor {

}
