package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.FundingSubTypeDto;
import com.transformuk.hee.tis.reference.service.model.FundingSubType;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity FundingSubType and its DTO FundingSubTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {FundingTypeMapper.class})
public interface FundingSubTypeMapper {

  FundingSubTypeDto toDto(FundingSubType fundingSubType);

  FundingSubType toEntity(FundingSubTypeDto fundingSubTypeDto);

  List<FundingSubTypeDto> toDtos(List<FundingSubType> fundingTypeList);
}
