package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.GenderDTO;
import com.transformuk.hee.tis.reference.service.model.Gender;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Gender and its DTO GenderDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GenderMapper {

  GenderDTO genderToGenderDTO(Gender gender);

  List<GenderDTO> gendersToGenderDTOs(List<Gender> genders);

  Gender genderDTOToGender(GenderDTO genderDTO);

  List<Gender> genderDTOsToGenders(List<GenderDTO> genderDTOs);
}
