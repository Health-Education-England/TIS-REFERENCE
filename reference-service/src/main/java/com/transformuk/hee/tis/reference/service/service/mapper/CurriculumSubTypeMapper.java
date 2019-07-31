package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.CurriculumSubTypeDTO;
import com.transformuk.hee.tis.reference.service.model.CurriculumSubType;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity CurriculumSubType and its DTO CurriculumSubTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CurriculumSubTypeMapper {

  CurriculumSubTypeDTO curriculumSubTypeToCurriculumSubTypeDTO(CurriculumSubType curriculumSubType);

  List<CurriculumSubTypeDTO> curriculumSubTypesToCurriculumSubTypeDTOs(
      List<CurriculumSubType> curriculumSubTypes);

  CurriculumSubType curriculumSubTypeDTOToCurriculumSubType(
      CurriculumSubTypeDTO curriculumSubTypeDTO);

  List<CurriculumSubType> curriculumSubTypeDTOsToCurriculumSubTypes(
      List<CurriculumSubTypeDTO> curriculumSubTypeDTOs);
}
