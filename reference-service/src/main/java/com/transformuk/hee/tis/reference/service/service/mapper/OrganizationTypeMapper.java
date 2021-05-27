package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.OrganizationTypeDto;
import com.transformuk.hee.tis.reference.service.model.OrganizationType;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity OrganizationType and its DTO OrganizationTypeDto.
 */
@Mapper(componentModel = "spring")
public interface OrganizationTypeMapper {

  OrganizationTypeDto toDto(OrganizationType entity);

  OrganizationType toEntity(OrganizationTypeDto dto);
}
