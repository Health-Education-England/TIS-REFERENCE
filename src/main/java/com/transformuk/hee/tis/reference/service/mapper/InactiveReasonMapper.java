package com.transformuk.hee.tis.reference.service.mapper;

import com.transformuk.hee.tis.reference.domain.InactiveReason;
import com.transformuk.hee.tis.reference.service.dto.InactiveReasonDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity InactiveReason and its DTO InactiveReasonDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface InactiveReasonMapper {

	InactiveReasonDTO inactiveReasonToInactiveReasonDTO(InactiveReason inactiveReason);

	List<InactiveReasonDTO> inactiveReasonsToInactiveReasonDTOs(List<InactiveReason> inactiveReasons);

	InactiveReason inactiveReasonDTOToInactiveReason(InactiveReasonDTO inactiveReasonDTO);

	List<InactiveReason> inactiveReasonDTOsToInactiveReasons(List<InactiveReasonDTO> inactiveReasonDTOs);
}
