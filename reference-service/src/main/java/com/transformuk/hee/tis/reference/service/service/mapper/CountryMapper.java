package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.api.dto.CountryDTO;
import com.transformuk.hee.tis.reference.service.model.Country;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Country and its DTO CountryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CountryMapper {

  CountryDTO countryToCountryDTO(Country country);

  List<CountryDTO> countriesToCountryDTOs(List<Country> countries);

  Country countryDTOToCountry(CountryDTO countryDTO);

  List<Country> countryDTOsToCountries(List<CountryDTO> countryDTOs);
}
