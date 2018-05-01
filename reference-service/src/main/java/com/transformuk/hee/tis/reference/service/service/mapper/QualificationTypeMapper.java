package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.QualificationTypeDTO;
import com.transformuk.hee.tis.reference.service.model.QualificationType;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity QualificationType and its DTO QualificationTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QualificationTypeMapper {

  QualificationTypeDTO qualificationTypeToQualificationTypeDTO(QualificationType qualificationType);

  List<QualificationTypeDTO> qualificationTypesToQualificationTypeDTOs(List<QualificationType> qualificationTypes);

  QualificationType qualificationTypeDTOToQualificationType(QualificationTypeDTO qualificationTypeDTO);

  List<QualificationType> qualificationTypeDTOsToQualificationTypes(List<QualificationTypeDTO> qualificationTypeDTOs);
}
