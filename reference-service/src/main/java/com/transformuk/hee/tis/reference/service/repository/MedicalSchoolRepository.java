package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.MedicalSchool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the MedicalSchool entity.
 */
@SuppressWarnings("unused")
public interface MedicalSchoolRepository extends JpaRepository<MedicalSchool, Long> {

  @Query("SELECT m.label from MedicalSchool m WHERE m.label in :values")
  List<String> findByLabel(@Param("values") List<String> values);
}
