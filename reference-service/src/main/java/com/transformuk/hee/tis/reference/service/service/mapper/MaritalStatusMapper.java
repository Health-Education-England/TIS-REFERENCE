package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.MaritalStatusDTO;
import com.transformuk.hee.tis.reference.service.model.MaritalStatus;
import java.util.List;
import org.mapstruct.Mapper;

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
