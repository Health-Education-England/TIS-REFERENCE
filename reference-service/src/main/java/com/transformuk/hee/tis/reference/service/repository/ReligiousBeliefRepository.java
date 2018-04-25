package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.ReligiousBelief;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the ReligiousBelief entity.
 */
@SuppressWarnings("unused")
public interface ReligiousBeliefRepository extends JpaRepository<ReligiousBelief, Long>, JpaSpecificationExecutor {
  ReligiousBelief findFirstByCode(String code);
}
