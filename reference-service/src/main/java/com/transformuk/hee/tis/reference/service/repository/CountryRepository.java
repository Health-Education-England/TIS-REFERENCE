package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Country entity.
 */
@SuppressWarnings("unused")
public interface CountryRepository extends JpaRepository<Country, Long> {

  @Query("SELECT c.nationality from Country c WHERE c.nationality in :values")
  List<String> findByNationality(@Param("values") List<String> values);

}
