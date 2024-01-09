package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.FundingSubTypeDto;
import com.transformuk.hee.tis.reference.service.model.FundingSubType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity FundingSubType and its DTO FundingSubTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {FundingTypeMapper.class})
public interface FundingSubTypeMapper {

  @Mapping(source = "fundingType", target = "fundingTypeDto")
  FundingSubTypeDto toDto(FundingSubType fundingSubType);

  @Mapping(source = "fundingTypeDto", target = "fundingType")
  FundingSubType toEntity(FundingSubTypeDto fundingSubTypeDto);
}
