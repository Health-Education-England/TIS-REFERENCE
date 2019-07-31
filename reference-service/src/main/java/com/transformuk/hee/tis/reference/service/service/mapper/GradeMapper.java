package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.service.model.Grade;
import java.util.List;
import org.mapstruct.Mapper;

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
