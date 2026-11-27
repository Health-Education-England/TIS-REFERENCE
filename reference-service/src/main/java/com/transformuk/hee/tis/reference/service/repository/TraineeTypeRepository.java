package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.TraineeType;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TraineeType entity.
 */
@Repository
public interface TraineeTypeRepository extends JpaRepository<TraineeType, UUID>,
    JpaSpecificationExecutor<TraineeType> {

}
