package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.TrainingNumberTypeDTO;
import com.transformuk.hee.tis.reference.service.model.TrainingNumberType;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity TrainingNumberType and its DTO TrainingNumberTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TrainingNumberTypeMapper {

  TrainingNumberTypeDTO trainingNumberTypeToTrainingNumberTypeDTO(
      TrainingNumberType trainingNumberType);

  List<TrainingNumberTypeDTO> trainingNumberTypesToTrainingNumberTypeDTOs(
      List<TrainingNumberType> trainingNumberTypes);

  TrainingNumberType trainingNumberTypeDTOToTrainingNumberType(
      TrainingNumberTypeDTO trainingNumberTypeDTO);

  List<TrainingNumberType> trainingNumberTypeDTOsToTrainingNumberTypes(
      List<TrainingNumberTypeDTO> trainingNumberTypeDTOs);
}
