package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.domain.EthnicOrigin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the EthnicOrigin entity.
 */
@SuppressWarnings("unused")
public interface EthnicOriginRepository extends JpaRepository<EthnicOrigin, Long> {

}
