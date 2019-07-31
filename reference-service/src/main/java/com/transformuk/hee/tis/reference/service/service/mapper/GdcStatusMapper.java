package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.GdcStatusDTO;
import com.transformuk.hee.tis.reference.service.model.GdcStatus;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity GdcStatus and its DTO GdcStatusDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GdcStatusMapper {

  GdcStatusDTO gdcStatusToGdcStatusDTO(GdcStatus gdcStatus);

  List<GdcStatusDTO> gdcStatusesToGdcStatusDTOs(List<GdcStatus> gdcStatuses);

  GdcStatus gdcStatusDTOToGdcStatus(GdcStatusDTO gdcStatusDTO);

  List<GdcStatus> gdcStatusDTOsToGdcStatuses(List<GdcStatusDTO> gdcStatusDTOs);
}
