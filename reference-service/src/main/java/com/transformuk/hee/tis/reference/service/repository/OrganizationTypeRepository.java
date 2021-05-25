package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.OrganizationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the OrganizationType entity.
 */
@SuppressWarnings("unused")
public interface OrganizationTypeRepository extends JpaRepository<OrganizationType, Long>,
    JpaSpecificationExecutor<OrganizationType> {

}
