package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.service.model.FundingType;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper for the entity FundingType and its DTO FundingTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FundingTypeMapper {

  FundingTypeDTO fundingTypeToFundingTypeDTO(FundingType fundingType);

  List<FundingTypeDTO> fundingTypesToFundingTypeDTOs(List<FundingType> fundingTypes);

  FundingType fundingTypeDTOToFundingType(FundingTypeDTO fundingTypeDTO);

  List<FundingType> fundingTypeDTOsToFundingTypes(List<FundingTypeDTO> fundingTypeDTOs);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "fundingSubTypes", ignore = true)
  void updateFundingTypeFromDto(FundingTypeDTO dto, @MappingTarget FundingType entity);
}
