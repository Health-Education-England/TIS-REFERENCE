package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.Nationality;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Nationality entity.
 */
@SuppressWarnings("unused")
public interface NationalityRepository extends JpaRepository<Nationality, Long> {

}
