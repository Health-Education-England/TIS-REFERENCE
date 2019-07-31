package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.RoleCategoryDTO;
import com.transformuk.hee.tis.reference.api.dto.RoleDTO;
import com.transformuk.hee.tis.reference.service.model.Role;
import com.transformuk.hee.tis.reference.service.model.RoleCategory;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleCategoryMapper {

  RoleCategoryDTO roleCategoryToRoleCategoryDTO(RoleCategory roleCategory);

  List<RoleCategoryDTO> roleCategorysToRoleCategoryDTOs(List<RoleCategory> roleCategorys);

  RoleCategory roleCategoryDTOToRoleCategory(RoleCategoryDTO roleCategoryDTO);

  List<RoleCategory> roleCategoryDTOsToRoleCategorys(List<RoleCategoryDTO> roleCategoryDTOs);

  Role map(RoleDTO roleDTO);

  RoleDTO map(Role role);
}
