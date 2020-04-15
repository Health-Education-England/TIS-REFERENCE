package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.AssessmentTypeDto;
import com.transformuk.hee.tis.reference.service.model.AssessmentType;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * A mapper to convert between {@link AssessmentType} and {@link AssessmentTypeDto}.
 */
@Mapper(componentModel = "spring")
public interface AssessmentTypeMapper {

  AssessmentTypeDto toDto(AssessmentType entity);

  List<AssessmentTypeDto> toDtos(List<AssessmentType> entities);

  AssessmentType toEntity(AssessmentTypeDto dto);

  List<AssessmentType> toEntities(List<AssessmentTypeDto> dtos);
}
