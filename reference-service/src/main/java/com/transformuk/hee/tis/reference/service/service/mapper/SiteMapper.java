package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.service.model.Site;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity Site and its DTO SiteDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SiteMapper {

	SiteDTO siteToSiteDTO(Site site);

	List<SiteDTO> sitesToSiteDTOs(List<Site> sites);

	Site siteDTOToSite(SiteDTO siteDTO);

	List<Site> siteDTOsToSites(List<SiteDTO> siteDTOs);
}