package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.domain.LocalOffice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the LocalOffice entity.
 */
@SuppressWarnings("unused")
public interface LocalOfficeRepository extends JpaRepository<LocalOffice, Long> {

}
