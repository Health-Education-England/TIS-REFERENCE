package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.ConcernTypeDto;
import com.transformuk.hee.tis.reference.service.model.ConcernType;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ConcernTypeMapper {

  ConcernTypeDto concernTypeToConcernTypeDto(ConcernType concernType);

  List<ConcernTypeDto> concernTypesToConcernTypeDtos(
      List<ConcernType> concernTypes);

}
