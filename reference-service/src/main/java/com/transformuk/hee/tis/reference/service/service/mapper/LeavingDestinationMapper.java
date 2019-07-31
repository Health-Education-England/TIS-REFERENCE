package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.LeavingDestinationDTO;
import com.transformuk.hee.tis.reference.service.model.LeavingDestination;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity LeavingDestination and its DTO LeavingDestinationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LeavingDestinationMapper {

  LeavingDestinationDTO leavingDestinationToLeavingDestinationDTO(
      LeavingDestination leavingDestination);

  List<LeavingDestinationDTO> leavingDestinationsToLeavingDestinationDTOs(
      List<LeavingDestination> leavingDestinations);

  LeavingDestination leavingDestinationDTOToLeavingDestination(
      LeavingDestinationDTO leavingDestinationDTO);

  List<LeavingDestination> leavingDestinationDTOsToLeavingDestinations(
      List<LeavingDestinationDTO> leavingDestinationDTOs);
}
