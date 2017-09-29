package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.Rotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the Rotation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RotationRepository extends JpaRepository<Rotation, Long> {

}
