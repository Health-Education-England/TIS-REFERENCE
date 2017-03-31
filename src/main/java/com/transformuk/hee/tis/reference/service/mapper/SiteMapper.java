package com.transformuk.hee.tis.reference.service.mapper;

import com.transformuk.hee.tis.reference.domain.LocalOffice;
import com.transformuk.hee.tis.reference.domain.Site;
import com.transformuk.hee.tis.reference.domain.Trust;
import com.transformuk.hee.tis.reference.service.dto.SiteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Site and its DTO SiteDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SiteMapper {

	@Mapping(source = "localOffice.id", target = "localOfficeId")
	@Mapping(source = "trustCode.id", target = "trustCodeId")
	SiteDTO siteToSiteDTO(Site site);

	List<SiteDTO> sitesToSiteDTOs(List<Site> sites);

	@Mapping(source = "localOfficeId", target = "localOffice")
	@Mapping(source = "trustCodeId", target = "trustCode")
	Site siteDTOToSite(SiteDTO siteDTO);

	List<Site> siteDTOsToSites(List<SiteDTO> siteDTOs);

	default LocalOffice localOfficeFromId(Long id) {
		if (id == null) {
			return null;
		}
		LocalOffice localOffice = new LocalOffice();
		localOffice.setId(id);
		return localOffice;
	}

	default Trust trustFromId(Long id) {
		if (id == null) {
			return null;
		}
		Trust trust = new Trust();
		trust.setId(id);
		return trust;
	}
}
