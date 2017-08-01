package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.ProgrammeMembershipTypeDTO;
import com.transformuk.hee.tis.reference.service.model.ProgrammeMembershipType;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity ProgrammeMembershipType and its DTO ProgrammeMembershipTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProgrammeMembershipTypeMapper {

  ProgrammeMembershipTypeDTO programmeMembershipTypeToProgrammeMembershipTypeDTO(ProgrammeMembershipType programmeMembershipType);

  List<ProgrammeMembershipTypeDTO> programmeMembershipTypesToProgrammeMembershipTypeDTOs(List<ProgrammeMembershipType> programmeMembershipTypes);

  ProgrammeMembershipType programmeMembershipTypeDTOToProgrammeMembershipType(ProgrammeMembershipTypeDTO programmeMembershipTypeDTO);

  List<ProgrammeMembershipType> programmeMembershipTypeDTOsToProgrammeMembershipTypes(List<ProgrammeMembershipTypeDTO> programmeMembershipTypeDTOs);
}
