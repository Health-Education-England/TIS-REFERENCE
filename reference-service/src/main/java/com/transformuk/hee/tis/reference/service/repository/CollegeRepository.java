package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.domain.College;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the College entity.
 */
@SuppressWarnings("unused")
public interface CollegeRepository extends JpaRepository<College, Long> {

}
