package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.RecordTypeDTO;
import com.transformuk.hee.tis.reference.service.model.RecordType;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity RecordType and its DTO RecordTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RecordTypeMapper {

  RecordTypeDTO recordTypeToRecordTypeDTO(RecordType recordType);

  List<RecordTypeDTO> recordTypesToRecordTypeDTOs(List<RecordType> recordTypes);

  RecordType recordTypeDTOToRecordType(RecordTypeDTO recordTypeDTO);

  List<RecordType> recordTypeDTOsToRecordTypes(List<RecordTypeDTO> recordTypeDTOs);
}
