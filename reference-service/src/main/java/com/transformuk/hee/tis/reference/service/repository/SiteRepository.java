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
public interface SiteRepository extends JpaRepository<Site, Long> {

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

  @Query("SELECT s.id from Site s WHERE s.id in :ids")
  List<Long> findByIdsIn(@Param("ids") List<Long> ids);

  @Query("SELECT s from Site s WHERE s.id in :siteIds")
  Set<Site> findBySiteIdIn(@Param("siteIds")Set<Long> siteIds);

  @Query("SELECT s.trustCode FROM Site s WHERE s.siteCode =:siteCode")
  String findTrustCodeBySiteCode(@Param("siteCode") String siteCode);

  @Query("SELECT s.id FROM Site s WHERE s.siteCode =:siteCode AND s.trustCode =:trustCode")
  List<Long> findSiteTrustMatch (@Param("siteCode") String siteCode, @Param("trustCode") String trustCode);

  @Query("SELECT s.id FROM Site s WHERE s.siteCode =:siteCode")
  Long findIdBySiteCode(@Param("siteCode") String siteCode);
}
