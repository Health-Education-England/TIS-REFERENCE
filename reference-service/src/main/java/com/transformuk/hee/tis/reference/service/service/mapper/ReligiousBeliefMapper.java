package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.ReligiousBeliefDTO;
import com.transformuk.hee.tis.reference.service.model.ReligiousBelief;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity ReligiousBelief and its DTO ReligiousBeliefDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReligiousBeliefMapper {

  ReligiousBeliefDTO religiousBeliefToReligiousBeliefDTO(ReligiousBelief religiousBelief);

  List<ReligiousBeliefDTO> religiousBeliefsToReligiousBeliefDTOs(List<ReligiousBelief> religiousBeliefs);

  ReligiousBelief religiousBeliefDTOToReligiousBelief(ReligiousBeliefDTO religiousBeliefDTO);

  List<ReligiousBelief> religiousBeliefDTOsToReligiousBeliefs(List<ReligiousBeliefDTO> religiousBeliefDTOs);
}
