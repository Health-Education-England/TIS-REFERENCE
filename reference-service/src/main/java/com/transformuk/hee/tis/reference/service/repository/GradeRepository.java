package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.model.Grade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Grade entity.
 */
@SuppressWarnings("unused")
public interface GradeRepository extends JpaRepository<Grade, Long> {

  @Query("SELECT g FROM Grade g WHERE g.name like %:param% or g.label like %:param% or g.abbreviation like %:param%")
  Page<Grade> findBySearchString(@Param("param") String searchString, Pageable pageable);

  @Query(
      "SELECT g " +
          "FROM Grade g " +
          "WHERE ( g.name like %:param% or g.label like %:param% or g.abbreviation like %:param% ) " +
          "AND g.status = :status")
  Page<Grade> findByStatusAndSearchString(@Param("status") Status status, @Param("param") String searchString, Pageable pageable);

  Grade findByAbbreviation(String code);

  List<Grade> findByAbbreviationIn(Set<String> codes);

  @Query("SELECT g.abbreviation from Grade g WHERE g.abbreviation in :abbreviations")
  List<String> findByAbbreviationsIn(@Param("abbreviations") List<String> abbreviations);

}