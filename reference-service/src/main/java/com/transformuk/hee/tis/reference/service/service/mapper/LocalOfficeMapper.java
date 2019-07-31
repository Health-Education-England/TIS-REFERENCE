package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.LocalOfficeDTO;
import com.transformuk.hee.tis.reference.service.model.LocalOffice;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity LocalOffice and its DTO LocalOfficeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LocalOfficeMapper {

  LocalOfficeDTO localOfficeToLocalOfficeDTO(LocalOffice localOffice);

  List<LocalOfficeDTO> localOfficesToLocalOfficeDTOs(List<LocalOffice> localOffices);

  LocalOffice localOfficeDTOToLocalOffice(LocalOfficeDTO localOfficeDTO);

  List<LocalOffice> localOfficeDTOsToLocalOffices(List<LocalOfficeDTO> localOfficeDTOs);
}
