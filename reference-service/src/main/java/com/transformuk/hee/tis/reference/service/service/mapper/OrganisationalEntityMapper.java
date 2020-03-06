package com.transformuk.hee.tis.reference.service.service.mapper;

import java.util.List;
import com.transformuk.hee.tis.reference.api.dto.validation.OrganisationalEntityDTO;
import com.transformuk.hee.tis.reference.service.model.OrganisationalEntity;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity OrganisationalEntity and its DTO OrganisationalEntityDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrganisationalEntityMapper {

    OrganisationalEntityDTO organisationalEntityToOrganisationalEntityDTO(OrganisationalEntity organisationalEntity);

    List<OrganisationalEntityDTO> organisationalEntitiesToOrganisationalEntityDTOs(List<OrganisationalEntity> organisationalEntities);

    OrganisationalEntity organisationalEntityDTOToOrganisationalEntity(OrganisationalEntityDTO organisationalEntityDTO);

    List<OrganisationalEntity> organisationalEntityDTOsToOrganisationalEntities(List<OrganisationalEntityDTO> organisationalEntityDTOs);
}
