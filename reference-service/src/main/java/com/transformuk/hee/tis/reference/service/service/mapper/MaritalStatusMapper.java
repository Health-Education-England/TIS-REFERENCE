package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.service.domain.MaritalStatus;
import com.transformuk.hee.tis.reference.service.api.dto.MaritalStatusDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity MaritalStatus and its DTO MaritalStatusDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MaritalStatusMapper {

	MaritalStatusDTO maritalStatusToMaritalStatusDTO(MaritalStatus maritalStatus);

	List<MaritalStatusDTO> maritalStatusesToMaritalStatusDTOs(List<MaritalStatus> maritalStatuses);

	MaritalStatus maritalStatusDTOToMaritalStatus(MaritalStatusDTO maritalStatusDTO);

	List<MaritalStatus> maritalStatusDTOsToMaritalStatuses(List<MaritalStatusDTO> maritalStatusDTOs);
}
