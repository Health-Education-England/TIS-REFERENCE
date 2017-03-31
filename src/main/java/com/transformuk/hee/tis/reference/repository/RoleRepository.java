package com.transformuk.hee.tis.reference.repository;

import com.transformuk.hee.tis.reference.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Role entity.
 */
@SuppressWarnings("unused")
public interface RoleRepository extends JpaRepository<Role, Long> {

}
