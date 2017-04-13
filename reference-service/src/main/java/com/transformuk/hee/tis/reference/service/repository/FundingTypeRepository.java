package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.domain.FundingType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the FundingType entity.
 */
@SuppressWarnings("unused")
public interface FundingTypeRepository extends JpaRepository<FundingType, Long> {

}
