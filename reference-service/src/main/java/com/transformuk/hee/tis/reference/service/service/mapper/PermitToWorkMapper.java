package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.PermitToWorkDTO;
import com.transformuk.hee.tis.reference.service.model.PermitToWork;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity PermitToWork and its DTO PermitToWorkDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PermitToWorkMapper {

  PermitToWorkDTO permitToWorkToPermitToWorkDTO(PermitToWork permitToWork);

  List<PermitToWorkDTO> permitToWorksToPermitToWorkDTOs(List<PermitToWork> permitToWorks);

  PermitToWork permitToWorkDTOToPermitToWork(PermitToWorkDTO permitToWorkDTO);

  List<PermitToWork> permitToWorkDTOsToPermitToWorks(List<PermitToWorkDTO> permitToWorkDTOs);
}
