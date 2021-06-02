package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.service.model.Site;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Site and its DTO SiteDTO.
 */
@Mapper(componentModel = "spring", uses = {OrganizationTypeMapper.class})
public interface SiteMapper {

  SiteDTO siteToSiteDTO(Site site);

  List<SiteDTO> sitesToSiteDTOs(List<Site> sites);

  Site siteDTOToSite(SiteDTO siteDTO);

  List<Site> siteDTOsToSites(List<SiteDTO> siteDTOs);
}
