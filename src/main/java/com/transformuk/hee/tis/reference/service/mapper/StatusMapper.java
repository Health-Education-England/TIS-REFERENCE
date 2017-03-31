package com.transformuk.hee.tis.reference.service.mapper;

import com.transformuk.hee.tis.reference.domain.Status;
import com.transformuk.hee.tis.reference.service.dto.StatusDTO;
import org.mapstruct.Mapper;

import java.util.List;

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
