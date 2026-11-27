package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.TraineeTypeDto;
import com.transformuk.hee.tis.reference.service.model.TraineeType;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity TraineeType and its DTO TraineeTypeDto.
 */
@Mapper(componentModel = "spring")
public interface TraineeTypeMapper {

  TraineeTypeDto toDto(TraineeType entity);

  TraineeType toEntity(TraineeTypeDto dto);
}
