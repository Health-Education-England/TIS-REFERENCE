package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.PlacementTypeDTO;
import com.transformuk.hee.tis.reference.service.model.PlacementType;
import java.util.List;
import org.mapstruct.Mapper;

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
