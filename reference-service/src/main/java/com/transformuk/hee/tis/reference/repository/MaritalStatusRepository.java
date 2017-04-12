package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.domain.MaritalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the MaritalStatus entity.
 */
@SuppressWarnings("unused")
public interface MaritalStatusRepository extends JpaRepository<MaritalStatus, Long> {

}
