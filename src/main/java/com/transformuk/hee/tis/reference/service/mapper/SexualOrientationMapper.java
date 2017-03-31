package com.transformuk.hee.tis.reference.service.mapper;

import com.transformuk.hee.tis.reference.domain.SexualOrientation;
import com.transformuk.hee.tis.reference.service.dto.SexualOrientationDTO;
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
