package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.GmcStatus;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the GmcStatus entity.
 */
@SuppressWarnings("unused")
public interface GmcStatusRepository extends JpaRepository<GmcStatus, Long> {

}
