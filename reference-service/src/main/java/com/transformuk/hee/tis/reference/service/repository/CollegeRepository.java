package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the College entity.
 */
@SuppressWarnings("unused")
public interface CollegeRepository extends JpaRepository<College, Long>, JpaSpecificationExecutor {

}
