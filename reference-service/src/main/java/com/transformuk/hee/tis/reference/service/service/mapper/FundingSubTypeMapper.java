package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.FundingSubTypeDto;
import com.transformuk.hee.tis.reference.service.model.FundingSubType;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity FundingSubType and its DTO FundingSubTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {FundingTypeMapper.class})
public interface FundingSubTypeMapper {

  @Mapping(target = "id", source = "uuid")
  FundingSubTypeDto toDto(FundingSubType fundingSubType);

  @Mapping(target = "uuid", source = "fundingSubTypeDto", qualifiedByName = "setUuid")
  FundingSubType toEntity(FundingSubTypeDto fundingSubTypeDto);

  @Named("setUuid")
  default UUID toUuid(FundingSubTypeDto dto) {
    if (dto.getId() != null) {
      return dto.getId();
    } else {
      return dto.getUuid();
    }
  }
}
