package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.domain.FundingType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the FundingType entity.
 */
@SuppressWarnings("unused")
public interface FundingTypeRepository extends JpaRepository<FundingType, Long> {

}
