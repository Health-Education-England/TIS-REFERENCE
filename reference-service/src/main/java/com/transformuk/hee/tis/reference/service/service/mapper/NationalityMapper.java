package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.NationalityDTO;
import com.transformuk.hee.tis.reference.service.model.Nationality;
import java.util.List;
import org.mapstruct.Mapper;

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
