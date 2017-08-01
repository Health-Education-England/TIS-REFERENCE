package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.Trust;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Trust entity.
 */
@SuppressWarnings("unused")
public interface TrustRepository extends JpaRepository<Trust, Long> {

  Trust findByCode(String code);

  @Query("SELECT t FROM Trust t WHERE t.code like %:param% or t.trustName like %:param%")
  List<Trust> findBySearchString(@Param("param") String searchString, Pageable pageable);

}
