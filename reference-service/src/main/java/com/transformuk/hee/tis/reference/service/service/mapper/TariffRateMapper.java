package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.TariffRateDTO;
import com.transformuk.hee.tis.reference.service.model.TariffRate;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity TariffRate and its DTO TariffRateDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TariffRateMapper {

  TariffRateDTO tariffRateToTariffRateDTO(TariffRate tariffRate);

  List<TariffRateDTO> tariffRatesToTariffRateDTOs(List<TariffRate> tariffRates);

  TariffRate tariffRateDTOToTariffRate(TariffRateDTO tariffRateDTO);

  List<TariffRate> tariffRateDTOsToTariffRates(List<TariffRateDTO> tariffRateDTOs);
}
