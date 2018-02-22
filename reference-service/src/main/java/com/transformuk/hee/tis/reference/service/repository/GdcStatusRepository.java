package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.GdcStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the GdcStatus entity.
 */
@SuppressWarnings("unused")
public interface GdcStatusRepository extends JpaRepository<GdcStatus, Long>, JpaSpecificationExecutor {

}
