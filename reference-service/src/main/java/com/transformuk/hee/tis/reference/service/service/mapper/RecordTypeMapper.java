package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.service.model.RecordType;
import com.transformuk.hee.tis.reference.service.api.dto.RecordTypeDTO;
import org.mapstruct.Mapper;

import java.util.List;

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
