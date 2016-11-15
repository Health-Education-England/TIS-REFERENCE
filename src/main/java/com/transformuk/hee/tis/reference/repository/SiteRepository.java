package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Sites
 */
@Repository
public interface SiteRepository extends JpaRepository<Site, String> {

	@Query("SELECT s FROM Site s WHERE s.siteCode like %:param% or s.trustCode like %:param% " +
			"or s.siteName like %:param% or s.address like %:param% or s.postCode like %:param%")
	List<Site> findBySearchString(@Param("param") String searchString);

	@Query("SELECT s FROM Site s WHERE s.trustCode =:trust and (s.siteCode like %:param% " +
			"or s.siteName like %:param% or s.address like %:param% or s.postCode like %:param%)")
	List<Site> findBySearchStringAndTrustCode(@Param("trust") String trustCode, @Param("param") String searchString);

	@Query("SELECT s FROM Site s WHERE s.trustCode =:trust")
	List<Site> findByTrustCode(@Param("trust") String trustCode);
}
