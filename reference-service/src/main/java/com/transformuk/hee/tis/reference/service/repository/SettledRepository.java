package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.Settled;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the Settled entity.
 */
@SuppressWarnings("unused")
public interface SettledRepository extends JpaRepository<Settled, Long>, JpaSpecificationExecutor {

}
