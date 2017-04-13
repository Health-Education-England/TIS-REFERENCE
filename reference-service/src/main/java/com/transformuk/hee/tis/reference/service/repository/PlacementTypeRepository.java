package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.domain.PlacementType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the PlacementType entity.
 */
@SuppressWarnings("unused")
public interface PlacementTypeRepository extends JpaRepository<PlacementType, Long> {

}
