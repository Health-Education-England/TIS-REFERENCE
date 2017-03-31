package com.transformuk.hee.tis.reference.service.mapper;

import com.transformuk.hee.tis.reference.domain.LocalOffice;
import com.transformuk.hee.tis.reference.domain.Trust;
import com.transformuk.hee.tis.reference.service.dto.TrustDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Trust and its DTO TrustDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TrustMapper {

	@Mapping(source = "localOffice.id", target = "localOfficeId")
	TrustDTO trustToTrustDTO(Trust trust);

	List<TrustDTO> trustsToTrustDTOs(List<Trust> trusts);

	@Mapping(source = "localOfficeId", target = "localOffice")
	Trust trustDTOToTrust(TrustDTO trustDTO);

	List<Trust> trustDTOsToTrusts(List<TrustDTO> trustDTOs);

	default LocalOffice localOfficeFromId(Long id) {
		if (id == null) {
			return null;
		}
		LocalOffice localOffice = new LocalOffice();
		localOffice.setId(id);
		return localOffice;
	}
}
