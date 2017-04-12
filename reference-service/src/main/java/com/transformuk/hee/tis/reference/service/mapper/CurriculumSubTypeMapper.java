package com.transformuk.hee.tis.reference.service.mapper;

import com.transformuk.hee.tis.reference.domain.CurriculumSubType;
import com.transformuk.hee.tis.reference.service.dto.CurriculumSubTypeDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity CurriculumSubType and its DTO CurriculumSubTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CurriculumSubTypeMapper {

	CurriculumSubTypeDTO curriculumSubTypeToCurriculumSubTypeDTO(CurriculumSubType curriculumSubType);

	List<CurriculumSubTypeDTO> curriculumSubTypesToCurriculumSubTypeDTOs(List<CurriculumSubType> curriculumSubTypes);

	CurriculumSubType curriculumSubTypeDTOToCurriculumSubType(CurriculumSubTypeDTO curriculumSubTypeDTO);

	List<CurriculumSubType> curriculumSubTypeDTOsToCurriculumSubTypes(List<CurriculumSubTypeDTO> curriculumSubTypeDTOs);
}
