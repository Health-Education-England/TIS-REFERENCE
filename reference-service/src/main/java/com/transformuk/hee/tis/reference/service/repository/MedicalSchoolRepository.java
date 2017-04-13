package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.MedicalSchool;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the MedicalSchool entity.
 */
@SuppressWarnings("unused")
public interface MedicalSchoolRepository extends JpaRepository<MedicalSchool, Long> {

}
