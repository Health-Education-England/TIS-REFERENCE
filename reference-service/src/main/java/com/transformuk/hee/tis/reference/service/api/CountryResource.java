package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.service.model.Country;
import com.transformuk.hee.tis.reference.service.repository.CountryRepository;
import com.transformuk.hee.tis.reference.api.dto.CountryDTO;
import com.transformuk.hee.tis.reference.service.service.mapper.CountryMapper;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Country.
 */
@RestController
@RequestMapping("/api")
public class CountryResource {

	private static final String ENTITY_NAME = "country";
	private final Logger log = LoggerFactory.getLogger(CountryResource.class);
	private final CountryRepository countryRepository;

	private final CountryMapper countryMapper;

	public CountryResource(CountryRepository countryRepository, CountryMapper countryMapper) {
		this.countryRepository = countryRepository;
		this.countryMapper = countryMapper;
	}

	/**
	 * POST  /countries : Create a new country.
	 *
	 * @param countryDTO the countryDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new countryDTO, or with status 400 (Bad Request) if the country has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/countries")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<CountryDTO> createCountry(@Valid @RequestBody CountryDTO countryDTO) throws URISyntaxException {
		log.debug("REST request to save Country : {}", countryDTO);
		if (countryDTO.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new country cannot already have an ID")).body(null);
		}
		Country country = countryMapper.countryDTOToCountry(countryDTO);
		country = countryRepository.save(country);
		CountryDTO result = countryMapper.countryToCountryDTO(country);
		return ResponseEntity.created(new URI("/api/countries/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /countries : Updates an existing country.
	 *
	 * @param countryDTO the countryDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated countryDTO,
	 * or with status 400 (Bad Request) if the countryDTO is not valid,
	 * or with status 500 (Internal Server Error) if the countryDTO couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/countries")
	@Timed
	@PreAuthorize("hasAuthority('reference:add:modify:entities')")
	public ResponseEntity<CountryDTO> updateCountry(@Valid @RequestBody CountryDTO countryDTO) throws URISyntaxException {
		log.debug("REST request to update Country : {}", countryDTO);
		if (countryDTO.getId() == null) {
			return createCountry(countryDTO);
		}
		Country country = countryMapper.countryDTOToCountry(countryDTO);
		country = countryRepository.save(country);
		CountryDTO result = countryMapper.countryToCountryDTO(country);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, countryDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /countries : get all the countries.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of countries in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/countries")
	@Timed
	public ResponseEntity<List<CountryDTO>> getAllCountries(@ApiParam Pageable pageable) {
		log.debug("REST request to get a page of Countries");
		Page<Country> page = countryRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/countries");
		return new ResponseEntity<>(countryMapper.countriesToCountryDTOs(page.getContent()), headers, HttpStatus.OK);
	}

	/**
	 * GET  /countries/:id : get the "id" country.
	 *
	 * @param id the id of the countryDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the countryDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/countries/{id}")
	@Timed
	public ResponseEntity<CountryDTO> getCountry(@PathVariable Long id) {
		log.debug("REST request to get Country : {}", id);
		Country country = countryRepository.findOne(id);
		CountryDTO countryDTO = countryMapper.countryToCountryDTO(country);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(countryDTO));
	}

	/**
	 * DELETE  /countries/:id : delete the "id" country.
	 *
	 * @param id the id of the countryDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/countries/{id}")
	@Timed
	@PreAuthorize("hasAuthority('reference:delete:entities')")
	public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
		log.debug("REST request to delete Country : {}", id);
		countryRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

}
