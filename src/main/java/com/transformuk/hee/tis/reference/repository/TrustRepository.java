package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.model.Trust;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Trusts
 */
@Repository
public interface TrustRepository extends JpaRepository<Trust, String> {

	@Query("SELECT t FROM Trust t WHERE t.code like %:param% or t.name like %:param%")
	List<Trust> findBySearchString(@Param("param") String searchString, Pageable pageable);
}
