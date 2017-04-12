package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.domain.AssessmentType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the AssessmentType entity.
 */
@SuppressWarnings("unused")
public interface AssessmentTypeRepository extends JpaRepository<AssessmentType, Long> {

}
