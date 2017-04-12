package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.domain.TariffRate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the TariffRate entity.
 */
@SuppressWarnings("unused")
public interface TariffRateRepository extends JpaRepository<TariffRate, Long> {

}
