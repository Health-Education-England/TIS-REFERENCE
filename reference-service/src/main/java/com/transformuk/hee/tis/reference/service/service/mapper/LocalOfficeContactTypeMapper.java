package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.LocalOfficeContactTypeDto;
import com.transformuk.hee.tis.reference.service.model.LocalOfficeContactType;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity LocalOfficeContactType and its DTO LocalOfficeContactTypeDto.
 */
@Mapper(componentModel = "spring")
public interface LocalOfficeContactTypeMapper {

  LocalOfficeContactTypeDto toDto(LocalOfficeContactType entity);

  LocalOfficeContactType toEntity(LocalOfficeContactTypeDto dto);
}
