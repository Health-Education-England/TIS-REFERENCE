package com.transformuk.hee.tis.reference.service.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.transformuk.hee.tis.reference.api.dto.CountryDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.Country;
import com.transformuk.hee.tis.reference.service.repository.CountryRepository;
import com.transformuk.hee.tis.reference.service.service.impl.CountryServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.CountryMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.transformuk.hee.tis.reference.service.api.util.StringUtil.sanitize;

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
  private final CountryServiceImpl countryService;

  public CountryResource(CountryRepository countryRepository, CountryMapper countryMapper, CountryServiceImpl countryService) {
    this.countryRepository = countryRepository;
    this.countryMapper = countryMapper;
    this.countryService = countryService;
  }

  /**
   * POST  /countries : Create a new country.
   *
   * @param countryDTO the countryDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new countryDTO, or with status 400 (Bad Request) if the country has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/countries")
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
   * GET  /countries : get all countries.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of colleges in body
   */
  @GetMapping("/countries")
  public ResponseEntity<List<CountryDTO>> getAllCountries(
          Pageable pageable,
          @RequestParam(value = "searchQuery", required = false) String searchQuery,
          @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.info("REST request to get a page of countries begin");
    searchQuery = sanitize(searchQuery);
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    Page<Country> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = countryRepository.findAll(pageable);
    } else {
      page = countryService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<CountryDTO> results = page.map(countryMapper::countryToCountryDTO);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/countries");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /current/countries : get all current countries.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of countries in body
   * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
   */
  @GetMapping("/current/countries")
  public ResponseEntity<List<CountryDTO>> getAllCurrentCountries(Pageable pageable) {
    log.debug("REST request to get a page of current Countries");
    Country country = new Country();
    country.setStatus(Status.CURRENT);
    Page<Country> page = countryRepository.findAll(Example.of(country), pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/countries");
    return new ResponseEntity<>(countryMapper.countriesToCountryDTOs(page.getContent()), headers, HttpStatus.OK);
  }

  /**
   * EXISTS /countries/exists/ : check is countries exists
   *
   * @param values the values of the countryDTO to check
   * @return boolean true if exists otherwise false
   */
  @PostMapping("/countries/exists/")
  public ResponseEntity<Map<String, Boolean>> countriesExists(@RequestBody List<String> values) {
    Map<String, Boolean> countriesExistsMap = Maps.newHashMap();
    log.debug("REST request to check Countries exists : {}", values);
    if (!CollectionUtils.isEmpty(values)) {
      List<String> dbLabels = countryRepository.findByNationality(values);
      values.forEach(label -> {
        boolean isMatch = dbLabels.stream().anyMatch(label::equalsIgnoreCase);
        countriesExistsMap.put(label, isMatch);
      });
    }

    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(countriesExistsMap));
  }

  /**
   * GET  /countries/:id : get the "id" country.
   *
   * @param id the id of the countryDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the countryDTO, or with status 404 (Not Found)
   */
  @GetMapping("/countries/{id}")
  public ResponseEntity<CountryDTO> getCountry(@PathVariable Long id) {
    log.debug("REST request to get Country : {}", id);
      Country country = countryRepository.findById(id).orElse(null);
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
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
    log.debug("REST request to delete Country : {}", id);
      countryRepository.deleteById(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }


  /**
   * POST  /bulk-countries : Bulk create a new countries.
   *
   * @param countryDTOS List of the countryDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new countryDTOS, or with status 400 (Bad Request) if the CountryDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-countries")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<CountryDTO>> bulkCreateCountry(@Valid @RequestBody List<CountryDTO> countryDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save CountryDTOs : {}", countryDTOS);
    if (!Collections.isEmpty(countryDTOS)) {
      List<Long> entityIds = countryDTOS.stream()
          .filter(c -> c.getId() != null)
          .map(c -> c.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new countries cannot already have an ID")).body(null);
      }
    }
    List<Country> countries = countryMapper.countryDTOsToCountries(countryDTOS);
      countries = countryRepository.saveAll(countries);
    List<CountryDTO> result = countryMapper.countriesToCountryDTOs(countries);
    return ResponseEntity.ok()
        .body(result);
  }

  /**
   * PUT  /bulk-countries : Updates an existing country.
   *
   * @param countryDTOS List of the countryDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated countryDTOS,
   * or with status 400 (Bad Request) if the countryDTOS is not valid,
   * or with status 500 (Internal Server Error) if the countryDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-countries")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<CountryDTO>> bulkUpdateCountry(@Valid @RequestBody List<CountryDTO> countryDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update CountryDTO : {}", countryDTOS);
    if (Collections.isEmpty(countryDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(countryDTOS)) {
      List<CountryDTO> entitiesWithNoId = countryDTOS.stream().filter(c -> c.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<Country> countries = countryMapper.countryDTOsToCountries(countryDTOS);
      countries = countryRepository.saveAll(countries);
    List<CountryDTO> results = countryMapper.countriesToCountryDTOs(countries);
    return ResponseEntity.ok()
        .body(results);
  }

}
