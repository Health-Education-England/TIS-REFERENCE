package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.service.domain.Gender;
import com.transformuk.hee.tis.reference.service.api.dto.GenderDTO;
import org.mapstruct.Mapper;

import java.util.List;

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
