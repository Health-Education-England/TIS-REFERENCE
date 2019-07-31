package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.QualificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the QualificationType entity.
 */
@SuppressWarnings("unused")
public interface QualificationTypeRepository extends JpaRepository<QualificationType, Long>,
    JpaSpecificationExecutor {

  QualificationType findFirstByCode(String code);
}
