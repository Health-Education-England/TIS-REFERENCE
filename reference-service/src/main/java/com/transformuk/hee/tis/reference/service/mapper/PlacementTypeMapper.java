package com.transformuk.hee.tis.reference.service.mapper;

import com.transformuk.hee.tis.reference.domain.PlacementType;
import com.transformuk.hee.tis.reference.service.dto.PlacementTypeDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity PlacementType and its DTO PlacementTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PlacementTypeMapper {

	PlacementTypeDTO placementTypeToPlacementTypeDTO(PlacementType placementType);

	List<PlacementTypeDTO> placementTypesToPlacementTypeDTOs(List<PlacementType> placementTypes);

	PlacementType placementTypeDTOToPlacementType(PlacementTypeDTO placementTypeDTO);

	List<PlacementType> placementTypeDTOsToPlacementTypes(List<PlacementTypeDTO> placementTypeDTOs);
}
