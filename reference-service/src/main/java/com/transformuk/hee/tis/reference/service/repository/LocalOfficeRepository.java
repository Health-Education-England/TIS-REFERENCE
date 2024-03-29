package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.model.LocalOffice;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the LocalOffice entity.
 */
@SuppressWarnings("unused")
public interface LocalOfficeRepository extends JpaRepository<LocalOffice, Long>,
    JpaSpecificationExecutor {

  List<LocalOffice> findByNameIn(Set<String> localOffices);

  LocalOffice findByAbbreviation(String abbreviation);

  @Query("SELECT l FROM LocalOffice l WHERE l.abbreviation like %:param% or l.name like %:param%")
  Page<LocalOffice> findBySearchString(@Param("param") String searchString, Pageable pageable);

  @Query("SELECT l " +
      "FROM LocalOffice l " +
      "WHERE l.abbreviation like %:param% or l.name like %:param% " +
      "AND l.status = :status")
  Page<LocalOffice> findByStatusAndSearchString(@Param("status") Status status,
      @Param("param") String searchString, Pageable pageable);

  @Query("SELECT l.abbreviation from LocalOffice l WHERE l.abbreviation in :abbreviations")
  List<String> findAbbreviationsIn(@Param("abbreviations") List<String> abbreviations);

  List<LocalOffice> findByAbbreviationIn(Set<String> abbreviations);

}
