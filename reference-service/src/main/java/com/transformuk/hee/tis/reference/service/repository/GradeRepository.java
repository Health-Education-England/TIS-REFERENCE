package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.Grade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Grade entity.
 */
@SuppressWarnings("unused")
public interface GradeRepository extends JpaRepository<Grade, Long> {

  @Query("SELECT g FROM Grade g WHERE g.name like %:param% or g.label like %:param%")
  Page<Grade> findBySearchString(@Param("param") String searchString, Pageable pageable);

  Grade findByAbbreviation(String code);

  @Query("SELECT g.id from Grade g WHERE g.id in :ids")
  List<Long> findByIdsIn(@Param("ids") List<Long> ids);

}
