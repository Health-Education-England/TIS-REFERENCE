package com.transformuk.hee.tis.reference.client;

import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface ReferenceService {

	<DTO> List<DTO> getAllDto(String endpointUrl, Class<DTO> dtoClass) throws IOException;

	<DTO> DTO createDto(DTO objectDTO, String endpointUrl, Class<DTO> dtoClass);

	<DTO> DTO updateDto(DTO objectDTO, String endpointUrl, Class<DTO> dtoClass);

	<DTO> List<DTO> bulkCreateDtos(List<DTO> objectDTOs, String endpointUrl);

	<DTO> List<DTO> bulkUpdateDtos(List<DTO> objectDTOs, String endpointUrl);

	ResponseEntity<DBCDTO> getDBCByCode(String code);
}
