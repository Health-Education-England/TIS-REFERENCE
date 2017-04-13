package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.service.domain.Settled;
import com.transformuk.hee.tis.reference.service.api.dto.SettledDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity Settled and its DTO SettledDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SettledMapper {

	SettledDTO settledToSettledDTO(Settled settled);

	List<SettledDTO> settledsToSettledDTOs(List<Settled> settleds);

	Settled settledDTOToSettled(SettledDTO settledDTO);

	List<Settled> settledDTOsToSettleds(List<SettledDTO> settledDTOs);
}
