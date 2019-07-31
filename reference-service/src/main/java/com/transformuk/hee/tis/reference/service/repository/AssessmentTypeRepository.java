package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.AssessmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the Assessment type entity.
 */
@SuppressWarnings("unused")
public interface AssessmentTypeRepository extends JpaRepository<AssessmentType, String>,
    JpaSpecificationExecutor {

}
