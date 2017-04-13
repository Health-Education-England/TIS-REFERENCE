package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.service.model.FundingType;
import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity FundingType and its DTO FundingTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FundingTypeMapper {

	FundingTypeDTO fundingTypeToFundingTypeDTO(FundingType fundingType);

	List<FundingTypeDTO> fundingTypesToFundingTypeDTOs(List<FundingType> fundingTypes);

	FundingType fundingTypeDTOToFundingType(FundingTypeDTO fundingTypeDTO);

	List<FundingType> fundingTypeDTOsToFundingTypes(List<FundingTypeDTO> fundingTypeDTOs);
}
