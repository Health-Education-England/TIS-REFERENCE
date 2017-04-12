package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Country entity.
 */
@SuppressWarnings("unused")
public interface CountryRepository extends JpaRepository<Country, Long> {

}
