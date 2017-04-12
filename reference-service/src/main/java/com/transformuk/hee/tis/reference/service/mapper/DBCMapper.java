package com.transformuk.hee.tis.reference.service.mapper;

import com.transformuk.hee.tis.reference.domain.DBC;
import com.transformuk.hee.tis.reference.service.dto.DBCDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity DBC and its DTO DBCDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DBCMapper {

	DBCDTO dBCToDBCDTO(DBC dBC);

	List<DBCDTO> dBCSToDBCDTOs(List<DBC> dBCS);

	DBC dBCDTOToDBC(DBCDTO dBCDTO);

	List<DBC> dBCDTOsToDBCS(List<DBCDTO> dBCDTOs);
}
