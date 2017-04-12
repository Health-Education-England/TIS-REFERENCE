package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.domain.MedicalSchool;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the MedicalSchool entity.
 */
@SuppressWarnings("unused")
public interface MedicalSchoolRepository extends JpaRepository<MedicalSchool, Long> {

}
