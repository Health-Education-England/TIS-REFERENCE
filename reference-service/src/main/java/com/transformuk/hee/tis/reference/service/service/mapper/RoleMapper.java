package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.service.model.Role;
import com.transformuk.hee.tis.reference.api.dto.RoleDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity Role and its DTO RoleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RoleMapper {

	RoleDTO roleToRoleDTO(Role role);

	List<RoleDTO> rolesToRoleDTOs(List<Role> roles);

	Role roleDTOToRole(RoleDTO roleDTO);

	List<Role> roleDTOsToRoles(List<RoleDTO> roleDTOs);
}
