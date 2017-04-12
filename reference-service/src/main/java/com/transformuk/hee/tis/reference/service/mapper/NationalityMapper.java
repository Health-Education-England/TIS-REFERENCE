package com.transformuk.hee.tis.reference.service.mapper;

import com.transformuk.hee.tis.reference.domain.Nationality;
import com.transformuk.hee.tis.reference.api.dto.NationalityDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity Nationality and its DTO NationalityDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NationalityMapper {

	NationalityDTO nationalityToNationalityDTO(Nationality nationality);

	List<NationalityDTO> nationalitiesToNationalityDTOs(List<Nationality> nationalities);

	Nationality nationalityDTOToNationality(NationalityDTO nationalityDTO);

	List<Nationality> nationalityDTOsToNationalities(List<NationalityDTO> nationalityDTOs);
}
