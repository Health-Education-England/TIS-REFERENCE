package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.RoleCategoryDTO;
import com.transformuk.hee.tis.reference.api.dto.RoleDTO;
import com.transformuk.hee.tis.reference.service.model.Role;
import com.transformuk.hee.tis.reference.service.model.RoleCategory;
import java.util.Arrays;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {}, imports = Arrays.class)
public interface RoleMapper {

  RoleDTO roleToRoleDTO(Role role);

  List<RoleDTO> rolesToRoleDTOs(List<Role> roles);

  Role roleDTOToRole(RoleDTO roleDTO);

  List<Role> roleDTOsToRoles(List<RoleDTO> roleDTOs);

  RoleCategory map(RoleCategoryDTO roleCategoryDTO);

  RoleCategoryDTO map(RoleCategory roleCategory);
}
