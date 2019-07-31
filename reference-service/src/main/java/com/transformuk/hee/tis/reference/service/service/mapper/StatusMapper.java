package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.StatusDTO;
import com.transformuk.hee.tis.reference.service.model.Status;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Status and its DTO StatusDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StatusMapper {

  StatusDTO statusToStatusDTO(Status status);

  List<StatusDTO> statusesToStatusDTOs(List<Status> statuses);

  Status statusDTOToStatus(StatusDTO statusDTO);

  List<Status> statusDTOsToStatuses(List<StatusDTO> statusDTOs);
}
