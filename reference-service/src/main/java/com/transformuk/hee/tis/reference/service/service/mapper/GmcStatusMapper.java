package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.GmcStatusDTO;
import com.transformuk.hee.tis.reference.service.model.GmcStatus;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity GmcStatus and its DTO GmcStatusDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GmcStatusMapper {

  GmcStatusDTO gmcStatusToGmcStatusDTO(GmcStatus gmcStatus);

  List<GmcStatusDTO> gmcStatusesToGmcStatusDTOs(List<GmcStatus> gmcStatuses);

  GmcStatus gmcStatusDTOToGmcStatus(GmcStatusDTO gmcStatusDTO);

  List<GmcStatus> gmcStatusDTOsToGmcStatuses(List<GmcStatusDTO> gmcStatusDTOs);
}
