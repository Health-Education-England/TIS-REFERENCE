package com.transformuk.hee.tis.reference.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.TariffRateDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.TariffRate;
import com.transformuk.hee.tis.reference.service.repository.TariffRateRepository;
import com.transformuk.hee.tis.reference.service.service.impl.TariffRateServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.TariffRateMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.StringUtils;
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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.transformuk.hee.tis.reference.service.api.util.StringUtil.sanitize;

/**
 * REST controller for managing TariffRate.
 */
@RestController
@RequestMapping("/api")
public class TariffRateResource {

  private static final String ENTITY_NAME = "tariffRate";
  private final Logger log = LoggerFactory.getLogger(TariffRateResource.class);
  private final TariffRateRepository tariffRateRepository;

  private final TariffRateMapper tariffRateMapper;
  private final TariffRateServiceImpl tariffRateService;

  public TariffRateResource(TariffRateRepository tariffRateRepository, TariffRateMapper tariffRateMapper,
                            TariffRateServiceImpl tariffRateService) {
    this.tariffRateRepository = tariffRateRepository;
    this.tariffRateMapper = tariffRateMapper;
    this.tariffRateService = tariffRateService;
  }

  /**
   * POST  /tariff-rates : Create a new tariffRate.
   *
   * @param tariffRateDTO the tariffRateDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new tariffRateDTO, or with status 400 (Bad Request) if the tariffRate has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/tariff-rates")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<TariffRateDTO> createTariffRate(@Valid @RequestBody TariffRateDTO tariffRateDTO) throws URISyntaxException {
    log.debug("REST request to save TariffRate : {}", tariffRateDTO);
    if (tariffRateDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tariffRate cannot already have an ID")).body(null);
    }
    TariffRate tariffRate = tariffRateMapper.tariffRateDTOToTariffRate(tariffRateDTO);
    tariffRate = tariffRateRepository.save(tariffRate);
    TariffRateDTO result = tariffRateMapper.tariffRateToTariffRateDTO(tariffRate);
    return ResponseEntity.created(new URI("/api/tariff-rates/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /tariff-rates : Updates an existing tariffRate.
   *
   * @param tariffRateDTO the tariffRateDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated tariffRateDTO,
   * or with status 400 (Bad Request) if the tariffRateDTO is not valid,
   * or with status 500 (Internal Server Error) if the tariffRateDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/tariff-rates")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<TariffRateDTO> updateTariffRate(@Valid @RequestBody TariffRateDTO tariffRateDTO) throws URISyntaxException {
    log.debug("REST request to update TariffRate : {}", tariffRateDTO);
    if (tariffRateDTO.getId() == null) {
      return createTariffRate(tariffRateDTO);
    }
    TariffRate tariffRate = tariffRateMapper.tariffRateDTOToTariffRate(tariffRateDTO);
    tariffRate = tariffRateRepository.save(tariffRate);
    TariffRateDTO result = tariffRateMapper.tariffRateToTariffRateDTO(tariffRate);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tariffRateDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /tariff-rates : get all tariff rates.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of tariff rates in body
   */
  @GetMapping("/tariff-rates")
  public ResponseEntity<List<TariffRateDTO>> getAllTariffRates(
          Pageable pageable,
          @RequestParam(value = "searchQuery", required = false) String searchQuery,
          @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.info("REST request to get a page of tariff rates begin");
    searchQuery = sanitize(searchQuery);
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    Page<TariffRate> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = tariffRateRepository.findAll(pageable);
    } else {
      page = tariffRateService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<TariffRateDTO> results = page.map(tariffRateMapper::tariffRateToTariffRateDTO);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tariff-rates");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET  /tariff-rates/:id : get the "id" tariffRate.
   *
   * @param id the id of the tariffRateDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the tariffRateDTO, or with status 404 (Not Found)
   */
  @GetMapping("/tariff-rates/{id}")
  public ResponseEntity<TariffRateDTO> getTariffRate(@PathVariable Long id) {
    log.debug("REST request to get TariffRate : {}", id);
      TariffRate tariffRate = tariffRateRepository.findById(id).orElse(null);
    TariffRateDTO tariffRateDTO = tariffRateMapper.tariffRateToTariffRateDTO(tariffRate);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tariffRateDTO));
  }

  /**
   * DELETE  /tariff-rates/:id : delete the "id" tariffRate.
   *
   * @param id the id of the tariffRateDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/tariff-rates/{id}")
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteTariffRate(@PathVariable Long id) {
    log.debug("REST request to delete TariffRate : {}", id);
      tariffRateRepository.deleteById(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }


  /**
   * POST  /bulk-tariff-rates : Bulk create a new tariff-rates.
   *
   * @param tariffRateDTOS List of the tariffRateDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new tariffRateDTOS, or with status 400 (Bad Request) if the TariffRateDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-tariff-rates")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<TariffRateDTO>> bulkCreateTariffRate(@Valid @RequestBody List<TariffRateDTO> tariffRateDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save TariffRateDtos : {}", tariffRateDTOS);
    if (!Collections.isEmpty(tariffRateDTOS)) {
      List<Long> entityIds = tariffRateDTOS.stream()
          .filter(tariffRateDTO -> tariffRateDTO.getId() != null)
          .map(tariffRateDTO -> tariffRateDTO.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new tariffRates cannot already have an ID")).body(null);
      }
    }
    List<TariffRate> tariffRates = tariffRateMapper.tariffRateDTOsToTariffRates(tariffRateDTOS);
      tariffRates = tariffRateRepository.saveAll(tariffRates);
    List<TariffRateDTO> result = tariffRateMapper.tariffRatesToTariffRateDTOs(tariffRates);
    return ResponseEntity.ok()
        .body(result);
  }

  /**
   * PUT  /bulk-tariff-rates : Updates an existing tariff-rates.
   *
   * @param tariffRateDTOS List of the tariffRateDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated tariffRateDTOS,
   * or with status 400 (Bad Request) if the tariffRateDTOS is not valid,
   * or with status 500 (Internal Server Error) if the tariffRateDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-tariff-rates")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<TariffRateDTO>> bulkUpdateTariffRate(@Valid @RequestBody List<TariffRateDTO> tariffRateDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update TariffRateDto : {}", tariffRateDTOS);
    if (Collections.isEmpty(tariffRateDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(tariffRateDTOS)) {
      List<TariffRateDTO> entitiesWithNoId = tariffRateDTOS.stream().filter(tariffRateDTO -> tariffRateDTO.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<TariffRate> tariffRates = tariffRateMapper.tariffRateDTOsToTariffRates(tariffRateDTOS);
      tariffRates = tariffRateRepository.saveAll(tariffRates);
    List<TariffRateDTO> results = tariffRateMapper.tariffRatesToTariffRateDTOs(tariffRates);
    return ResponseEntity.ok()
        .body(results);
  }
}
