package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.EthnicOriginDTO;
import com.transformuk.hee.tis.reference.service.model.EthnicOrigin;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity EthnicOrigin and its DTO EthnicOriginDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EthnicOriginMapper {

  EthnicOriginDTO ethnicOriginToEthnicOriginDTO(EthnicOrigin ethnicOrigin);

  List<EthnicOriginDTO> ethnicOriginsToEthnicOriginDTOs(List<EthnicOrigin> ethnicOrigins);

  EthnicOrigin ethnicOriginDTOToEthnicOrigin(EthnicOriginDTO ethnicOriginDTO);

  List<EthnicOrigin> ethnicOriginDTOsToEthnicOrigins(List<EthnicOriginDTO> ethnicOriginDTOs);
}
