package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.CurriculumSubType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the CurriculumSubType entity.
 */
@SuppressWarnings("unused")
public interface CurriculumSubTypeRepository extends JpaRepository<CurriculumSubType, Long>, JpaSpecificationExecutor {

  CurriculumSubType findByCode(String code);
}
