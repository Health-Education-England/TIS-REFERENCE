package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.domain.ReligiousBelief;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the ReligiousBelief entity.
 */
@SuppressWarnings("unused")
public interface ReligiousBeliefRepository extends JpaRepository<ReligiousBelief, Long> {

}
