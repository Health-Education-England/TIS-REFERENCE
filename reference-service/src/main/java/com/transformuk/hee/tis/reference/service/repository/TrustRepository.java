package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.model.Trust;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Trust entity.
 */
@SuppressWarnings("unused")
public interface TrustRepository extends JpaRepository<Trust, Long>, JpaSpecificationExecutor {

  Trust findByCode(String code);

  @Query("SELECT t FROM Trust t WHERE t.code like %:param% or t.trustName like %:param%")
  Page<Trust> findBySearchString(@Param("param") String searchString, Pageable pageable);

  @Query("SELECT t " +
      "FROM Trust t " +
      "WHERE (t.code like %:param% or t.trustName like %:param%) " +
      "AND t.status = :status")
  Page<Trust> findByStatusAndSearchString(@Param("status") Status status, @Param("param") String searchString, Pageable pageable);

  @Query("SELECT t.code from Trust t WHERE t.code in :codes")
  List<String> findCodesByCodesIn(@Param("codes") List<String> codes);

  List<Trust> findByCodeIn(Set<String> codes);

}
