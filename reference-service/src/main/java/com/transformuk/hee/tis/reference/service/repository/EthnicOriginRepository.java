package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.domain.EthnicOrigin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the EthnicOrigin entity.
 */
@SuppressWarnings("unused")
public interface EthnicOriginRepository extends JpaRepository<EthnicOrigin, Long> {

}
