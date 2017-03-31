package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.domain.SexualOrientation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the SexualOrientation entity.
 */
@SuppressWarnings("unused")
public interface SexualOrientationRepository extends JpaRepository<SexualOrientation, Long> {

}
