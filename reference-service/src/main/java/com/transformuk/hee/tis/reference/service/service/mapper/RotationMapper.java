package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.RotationDTO;
import com.transformuk.hee.tis.reference.service.model.Rotation;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Rotation and its DTO RotationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RotationMapper extends EntityMapper<RotationDTO, Rotation> {


  default Rotation fromId(Long id) {
    if (id == null) {
      return null;
    }
    Rotation rotation = new Rotation();
    rotation.setId(id);
    return rotation;
  }
}
