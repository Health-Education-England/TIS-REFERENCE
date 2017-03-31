package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.domain.DBC;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the DBC entity.
 */
@SuppressWarnings("unused")
public interface DBCRepository extends JpaRepository<DBC, Long> {

}
