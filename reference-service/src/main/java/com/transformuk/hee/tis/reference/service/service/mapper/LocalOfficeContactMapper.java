package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.LocalOfficeContactDto;
import com.transformuk.hee.tis.reference.service.model.LocalOfficeContact;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity LocalOfficeContact and its DTO LocalOfficeContactDto.
 */
@Mapper(componentModel = "spring", uses = {LocalOfficeMapper.class,
    LocalOfficeContactTypeMapper.class})
public interface LocalOfficeContactMapper {

  LocalOfficeContactDto toDto(LocalOfficeContact entity);

  LocalOfficeContact toEntity(LocalOfficeContactDto dto);
}
