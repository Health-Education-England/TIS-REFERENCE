package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.DBC;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the DBC entity.
 */
@SuppressWarnings("unused")
public interface DBCRepository extends JpaRepository<DBC, Long> {

	DBC findByDbc(String code);

}
