package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.TariffRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the TariffRate entity.
 */
@SuppressWarnings("unused")
public interface TariffRateRepository extends JpaRepository<TariffRate, Long>, JpaSpecificationExecutor {

}
