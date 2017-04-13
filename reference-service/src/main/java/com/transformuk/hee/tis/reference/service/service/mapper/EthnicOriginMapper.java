package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.service.domain.EthnicOrigin;
import com.transformuk.hee.tis.reference.service.api.dto.EthnicOriginDTO;
import org.mapstruct.Mapper;

import java.util.List;

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
