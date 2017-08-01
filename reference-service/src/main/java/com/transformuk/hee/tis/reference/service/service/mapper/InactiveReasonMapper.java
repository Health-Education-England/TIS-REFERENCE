package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.InactiveReasonDTO;
import com.transformuk.hee.tis.reference.service.model.InactiveReason;
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
