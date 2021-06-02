package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.TrustDTO;
import com.transformuk.hee.tis.reference.service.model.Trust;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Trust and its DTO TrustDTO.
 */
@Mapper(componentModel = "spring", uses = {OrganizationTypeMapper.class})
public interface TrustMapper {

  TrustDTO trustToTrustDTO(Trust trust);

  List<TrustDTO> trustsToTrustDTOs(List<Trust> trusts);

  Trust trustDTOToTrust(TrustDTO trustDTO);

  List<Trust> trustDTOsToTrusts(List<TrustDTO> trustDTOs);
}
