package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.QualificationReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the QualificationReference entity.
 */
@SuppressWarnings("unused")
public interface QualificationReferenceRepository extends JpaRepository<QualificationReference, Long>, JpaSpecificationExecutor {
  QualificationReference findFirstByCode(String code);
}
