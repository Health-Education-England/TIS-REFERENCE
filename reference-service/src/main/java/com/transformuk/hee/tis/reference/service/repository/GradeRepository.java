package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Grade entity.
 */
@SuppressWarnings("unused")
public interface GradeRepository extends JpaRepository<Grade, Long> {

  Grade findByAbbreviation(String code);

  @Query("SELECT g.id from Grade g WHERE g.id in :ids")
  List<Long> findByIdsIn(@Param("ids") List<Long> ids);

}
