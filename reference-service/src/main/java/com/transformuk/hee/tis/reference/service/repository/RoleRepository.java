package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for the Role entity.
 */
@SuppressWarnings("unused")
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor {

}
