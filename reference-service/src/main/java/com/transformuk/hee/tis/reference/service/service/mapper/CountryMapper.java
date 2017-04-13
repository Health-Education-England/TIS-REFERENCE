package com.transformuk.hee.tis.reference.service.service.mapper;

import com.transformuk.hee.tis.reference.service.model.Country;
import com.transformuk.hee.tis.reference.service.api.dto.CountryDTO;
import org.mapstruct.Mapper;

import java.util.List;

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
