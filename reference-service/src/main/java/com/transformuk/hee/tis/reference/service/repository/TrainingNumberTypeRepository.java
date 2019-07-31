package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.TrainingNumberType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the TrainingNumberType entity.
 */
@SuppressWarnings("unused")
public interface TrainingNumberTypeRepository extends JpaRepository<TrainingNumberType, Long>,
    JpaSpecificationExecutor {

}
