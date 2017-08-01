package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.SexualOrientationDTO;
import com.transformuk.hee.tis.reference.service.model.SexualOrientation;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity SexualOrientation and its DTO SexualOrientationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SexualOrientationMapper {

  SexualOrientationDTO sexualOrientationToSexualOrientationDTO(SexualOrientation sexualOrientation);

  List<SexualOrientationDTO> sexualOrientationsToSexualOrientationDTOs(List<SexualOrientation> sexualOrientations);

  SexualOrientation sexualOrientationDTOToSexualOrientation(SexualOrientationDTO sexualOrientationDTO);

  List<SexualOrientation> sexualOrientationDTOsToSexualOrientations(List<SexualOrientationDTO> sexualOrientationDTOs);
}
