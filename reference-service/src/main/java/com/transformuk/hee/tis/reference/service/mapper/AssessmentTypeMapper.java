package com.transformuk.hee.tis.reference.service.mapper;

import com.transformuk.hee.tis.reference.domain.AssessmentType;
import com.transformuk.hee.tis.reference.service.dto.AssessmentTypeDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity AssessmentType and its DTO AssessmentTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AssessmentTypeMapper {

	AssessmentTypeDTO assessmentTypeToAssessmentTypeDTO(AssessmentType assessmentType);

	List<AssessmentTypeDTO> assessmentTypesToAssessmentTypeDTOs(List<AssessmentType> assessmentTypes);

	AssessmentType assessmentTypeDTOToAssessmentType(AssessmentTypeDTO assessmentTypeDTO);

	List<AssessmentType> assessmentTypeDTOsToAssessmentTypes(List<AssessmentTypeDTO> assessmentTypeDTOs);
}
