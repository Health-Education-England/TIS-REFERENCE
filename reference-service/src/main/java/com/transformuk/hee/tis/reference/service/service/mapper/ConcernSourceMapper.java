package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.ConcernSourceDto;
import com.transformuk.hee.tis.reference.service.model.ConcernSource;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ConcernSourceMapper {

  ConcernSourceDto concernSourceToConcernSourceDto(ConcernSource concernSource);

  List<ConcernSourceDto> concernSourcesToConcernSourceDtos(
      List<ConcernSource> concernSources);

}