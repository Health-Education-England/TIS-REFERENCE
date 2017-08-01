package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.MedicalSchoolDTO;
import com.transformuk.hee.tis.reference.service.model.MedicalSchool;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity MedicalSchool and its DTO MedicalSchoolDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MedicalSchoolMapper {

  MedicalSchoolDTO medicalSchoolToMedicalSchoolDTO(MedicalSchool medicalSchool);

  List<MedicalSchoolDTO> medicalSchoolsToMedicalSchoolDTOs(List<MedicalSchool> medicalSchools);

  MedicalSchool medicalSchoolDTOToMedicalSchool(MedicalSchoolDTO medicalSchoolDTO);

  List<MedicalSchool> medicalSchoolDTOsToMedicalSchools(List<MedicalSchoolDTO> medicalSchoolDTOs);
}
