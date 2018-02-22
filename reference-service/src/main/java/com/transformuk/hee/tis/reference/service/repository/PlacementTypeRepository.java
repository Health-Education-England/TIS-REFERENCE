package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.PlacementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the PlacementType entity.
 */
@SuppressWarnings("unused")
public interface PlacementTypeRepository extends JpaRepository<PlacementType, Long>, JpaSpecificationExecutor {

  @Query("SELECT pt.code from PlacementType pt WHERE pt.code in :codes")
  List<String> findCodeByCodesIn(@Param("codes") List<String> codes);
}
