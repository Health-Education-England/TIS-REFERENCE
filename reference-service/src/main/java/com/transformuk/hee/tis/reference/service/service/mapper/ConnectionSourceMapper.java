package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.ConnectionSourceDto;
import com.transformuk.hee.tis.reference.service.model.ConnectionSource;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ConnectionSourceMapper {

  ConnectionSourceDto connectionSourceToConnectionSourceDto(ConnectionSource connectionSource);

  List<ConnectionSourceDto> connectionSourcesToConnectionSourceDtos(
      List<ConnectionSource> connectionSources);

}