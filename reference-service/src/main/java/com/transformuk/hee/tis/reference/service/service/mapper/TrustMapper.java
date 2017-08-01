package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.TrustDTO;
import com.transformuk.hee.tis.reference.service.model.Trust;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity Trust and its DTO TrustDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TrustMapper {

  TrustDTO trustToTrustDTO(Trust trust);

  List<TrustDTO> trustsToTrustDTOs(List<Trust> trusts);

  Trust trustDTOToTrust(TrustDTO trustDTO);

  List<Trust> trustDTOsToTrusts(List<TrustDTO> trustDTOs);
}
