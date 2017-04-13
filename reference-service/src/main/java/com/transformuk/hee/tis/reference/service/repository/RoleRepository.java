package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Role entity.
 */
@SuppressWarnings("unused")
public interface RoleRepository extends JpaRepository<Role, Long> {

}
