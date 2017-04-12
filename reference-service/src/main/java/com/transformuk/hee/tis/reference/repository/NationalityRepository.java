package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.domain.Nationality;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Nationality entity.
 */
@SuppressWarnings("unused")
public interface NationalityRepository extends JpaRepository<Nationality, Long> {

}
