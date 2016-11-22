package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.model.DBC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Designated Body Codes
 */
@Repository
public interface DBCRepository extends JpaRepository<DBC, String> {
}
