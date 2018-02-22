package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.Rotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data JPA repository for the Rotation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RotationRepository extends JpaRepository<Rotation, Long>, JpaSpecificationExecutor {

  @Query("SELECT r.label from Rotation r WHERE r.label in :values")
  List<String> findByLabelsIn(@Param("values") List<String> values);
}
