package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.LocalOffice;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the LocalOffice entity.
 */
@SuppressWarnings("unused")
public interface LocalOfficeRepository extends JpaRepository<LocalOffice, Long> {

	List<LocalOffice> findByNameIn(Set<String> localOffices);

	LocalOffice findByAbbreviation(String abbreviation);

	@Query("SELECT l FROM LocalOffice l WHERE l.abbreviation like %:param% or l.name like %:param%")
  Page<LocalOffice> findBySearchString(@Param("param") String searchString, Pageable pageable);

	@Query("SELECT l.abbreviation from LocalOffice l WHERE l.abbreviation in :abbreviations")
  List<String> findAbbreviationsIn(@Param("abbreviations") List<String> abbreviations);

	List<LocalOffice> findByAbbreviationIn(Set<String> abbreviations);

}
