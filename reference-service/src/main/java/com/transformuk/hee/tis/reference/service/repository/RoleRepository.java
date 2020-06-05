package com.transformuk.hee.tis.reference.service.repository;

import com.transformuk.hee.tis.reference.service.model.Role;
import com.transformuk.hee.tis.reference.service.model.RoleCategory;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor {

  List<Role> findAllByRoleCategory(RoleCategory roleCategory);

  List<Role> findByCodeIn(Set<String> codes);
}
