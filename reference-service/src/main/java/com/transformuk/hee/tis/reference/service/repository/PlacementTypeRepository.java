package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.PlacementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the PlacementType entity.
 */
@SuppressWarnings("unused")
public interface PlacementTypeRepository extends JpaRepository<PlacementType, Long> {

  @Query("SELECT pt.id from PlacementType pt WHERE pt.id in :ids")
  List<Long> findByIdsIn(@Param("ids") List<Long> ids);
}
