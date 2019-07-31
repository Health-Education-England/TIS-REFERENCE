package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.SettledDTO;
import com.transformuk.hee.tis.reference.service.model.Settled;
import java.util.List;
import org.mapstruct.Mapper;

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
