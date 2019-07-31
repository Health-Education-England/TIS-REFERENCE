package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.SexualOrientation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the SexualOrientation entity.
 */
@SuppressWarnings("unused")
public interface SexualOrientationRepository extends JpaRepository<SexualOrientation, Long>,
    JpaSpecificationExecutor {

  SexualOrientation findFirstByCode(String code);
}
