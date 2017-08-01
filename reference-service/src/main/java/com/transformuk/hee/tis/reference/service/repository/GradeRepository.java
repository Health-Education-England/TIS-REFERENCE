package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Grade entity.
 */
@SuppressWarnings("unused")
public interface GradeRepository extends JpaRepository<Grade, Long> {

  Grade findByAbbreviation(String code);

}
