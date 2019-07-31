package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the Gender entity.
 */
@SuppressWarnings("unused")
public interface GenderRepository extends JpaRepository<Gender, Long>, JpaSpecificationExecutor {

  Gender findFirstByCode(String code);
}
