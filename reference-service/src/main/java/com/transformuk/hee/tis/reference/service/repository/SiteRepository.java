package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Site entity.
 */
@SuppressWarnings("unused")
public interface SiteRepository extends JpaRepository<Site, String> {

  Site findBySiteCode(String code);

  @Query("SELECT s FROM Site s WHERE s.siteCode like %:param% or s.trustCode like %:param% " +
      "or s.siteName like %:param% or s.address like %:param% or s.postCode like %:param%")
  Page<Site> findBySearchString(@Param("param") String searchString, Pageable pageable);

  @Query("SELECT s FROM Site s WHERE s.trustCode =:trust and (s.siteCode like %:param% " +
      "or s.siteName like %:param% or s.address like %:param% or s.postCode like %:param%)")
  List<Site> findBySearchStringAndTrustCode(@Param("trust") String trustCode, @Param("param") String searchString,
                                            Pageable pageable);

  @Query("SELECT s FROM Site s WHERE s.trustCode =:trust")
  List<Site> findByTrustCode(@Param("trust") String trustCode, Pageable pageable);

  @Query("SELECT s.siteCode from Site s WHERE s.siteCode in :siteCode")
  List<String> findSiteCodesBySiteCodeIn(@Param("siteCode") List<String> siteCodes);

  List<Site> findBySiteCodeIn(Set<String> codes);

  @Query("SELECT s.trustCode FROM Site s WHERE s.siteCode =:siteCode")
  String findTrustCodeBySiteCode(@Param("siteCode") String siteCode);

  @Query("SELECT s.siteCode FROM Site s WHERE s.siteCode =:siteCode AND s.trustCode =:trustCode")
  List<String> findSiteTrustMatch(@Param("siteCode") String siteCode, @Param("trustCode") String trustCode);

  @Query("SELECT s.siteCode FROM Site s WHERE s.siteCode =:siteCode")
  String findIdBySiteCode(@Param("siteCode") String siteCode);
}
