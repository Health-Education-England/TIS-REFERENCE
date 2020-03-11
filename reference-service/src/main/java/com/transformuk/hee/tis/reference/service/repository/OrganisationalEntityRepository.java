package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.OrganisationalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the OrganisationalEntity entity.
 */
@SuppressWarnings("unused")
public interface OrganisationalEntityRepository extends JpaRepository<OrganisationalEntity, Long>, JpaSpecificationExecutor {

}