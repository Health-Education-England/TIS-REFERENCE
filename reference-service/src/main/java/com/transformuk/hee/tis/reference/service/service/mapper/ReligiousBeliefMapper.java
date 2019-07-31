package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.ReligiousBeliefDTO;
import com.transformuk.hee.tis.reference.service.model.ReligiousBelief;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity ReligiousBelief and its DTO ReligiousBeliefDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReligiousBeliefMapper {

  ReligiousBeliefDTO religiousBeliefToReligiousBeliefDTO(ReligiousBelief religiousBelief);

  List<ReligiousBeliefDTO> religiousBeliefsToReligiousBeliefDTOs(
      List<ReligiousBelief> religiousBeliefs);

  ReligiousBelief religiousBeliefDTOToReligiousBelief(ReligiousBeliefDTO religiousBeliefDTO);

  List<ReligiousBelief> religiousBeliefDTOsToReligiousBeliefs(
      List<ReligiousBeliefDTO> religiousBeliefDTOs);
}
