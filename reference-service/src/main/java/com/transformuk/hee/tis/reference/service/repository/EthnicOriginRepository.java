package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.EthnicOrigin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the EthnicOrigin entity.
 */
@SuppressWarnings("unused")
public interface EthnicOriginRepository extends JpaRepository<EthnicOrigin, Long>,
    JpaSpecificationExecutor {

  EthnicOrigin findFirstByCode(String code);
}
