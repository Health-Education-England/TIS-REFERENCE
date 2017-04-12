package com.transformuk.hee.tis.reference.service.mapper;

import com.transformuk.hee.tis.reference.domain.Grade;
import com.transformuk.hee.tis.reference.service.dto.GradeDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity Grade and its DTO GradeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GradeMapper {

	GradeDTO gradeToGradeDTO(Grade grade);

	List<GradeDTO> gradesToGradeDTOs(List<Grade> grades);

	Grade gradeDTOToGrade(GradeDTO gradeDTO);

	List<Grade> gradeDTOsToGrades(List<GradeDTO> gradeDTOs);
}
