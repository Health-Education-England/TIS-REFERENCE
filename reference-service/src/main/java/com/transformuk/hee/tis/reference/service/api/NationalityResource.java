package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.api.dto.NationalityDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.Nationality;
import com.transformuk.hee.tis.reference.service.repository.NationalityRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.NationalityMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiParam;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Nationality.
 */
@RestController
@RequestMapping("/api")
public class NationalityResource {

  private static final String ENTITY_NAME = "nationality";
  private final Logger log = LoggerFactory.getLogger(NationalityResource.class);
  private final NationalityRepository nationalityRepository;

  private final NationalityMapper nationalityMapper;

  public NationalityResource(NationalityRepository nationalityRepository, NationalityMapper nationalityMapper) {
    this.nationalityRepository = nationalityRepository;
    this.nationalityMapper = nationalityMapper;
  }

  /**
   * POST  /nationalities : Create a new nationality.
   *
   * @param nationalityDTO the nationalityDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new nationalityDTO, or with status 400 (Bad Request) if the nationality has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/nationalities")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<NationalityDTO> createNationality(@Valid @RequestBody NationalityDTO nationalityDTO) throws URISyntaxException {
    log.debug("REST request to save Nationality : {}", nationalityDTO);
    if (nationalityDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new nationality cannot already have an ID")).body(null);
    }
    Nationality nationality = nationalityMapper.nationalityDTOToNationality(nationalityDTO);
    nationality = nationalityRepository.save(nationality);
    NationalityDTO result = nationalityMapper.nationalityToNationalityDTO(nationality);
    return ResponseEntity.created(new URI("/api/nationalities/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /nationalities : Updates an existing nationality.
   *
   * @param nationalityDTO the nationalityDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated nationalityDTO,
   * or with status 400 (Bad Request) if the nationalityDTO is not valid,
   * or with status 500 (Internal Server Error) if the nationalityDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/nationalities")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<NationalityDTO> updateNationality(@Valid @RequestBody NationalityDTO nationalityDTO) throws URISyntaxException {
    log.debug("REST request to update Nationality : {}", nationalityDTO);
    if (nationalityDTO.getId() == null) {
      return createNationality(nationalityDTO);
    }
    Nationality nationality = nationalityMapper.nationalityDTOToNationality(nationalityDTO);
    nationality = nationalityRepository.save(nationality);
    NationalityDTO result = nationalityMapper.nationalityToNationalityDTO(nationality);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, nationalityDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /nationalities : get all the nationalities.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of nationalities in body
   * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
   */
  @GetMapping("/nationalities")
  @Timed
  public ResponseEntity<List<NationalityDTO>> getAllNationalities(@ApiParam Pageable pageable) {
    log.debug("REST request to get a page of Nationalities");
    Page<Nationality> page = nationalityRepository.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/nationalities");
    return new ResponseEntity<>(nationalityMapper.nationalitiesToNationalityDTOs(page.getContent()), headers, HttpStatus.OK);
  }

  /**
   * GET  /current/nationalities : get all the current nationalities.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of nationalities in body
   * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
   */
  @GetMapping("/current/nationalities")
  @Timed
  public ResponseEntity<List<NationalityDTO>> getAllCurrentNationalities(@ApiParam Pageable pageable) {
    log.debug("REST request to get a page of Nationalities");
    Nationality nationality = new Nationality();
    nationality.setStatus(Status.CURRENT);
    Page<Nationality> page = nationalityRepository.findAll(Example.of(nationality), pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/nationalities");
    return new ResponseEntity<>(nationalityMapper.nationalitiesToNationalityDTOs(page.getContent()), headers, HttpStatus.OK);
  }

  /**
   * GET  /nationalities/:id : get the "id" nationality.
   *
   * @param id the id of the nationalityDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the nationalityDTO, or with status 404 (Not Found)
   */
  @GetMapping("/nationalities/{id}")
  @Timed
  public ResponseEntity<NationalityDTO> getNationality(@PathVariable Long id) {
    log.debug("REST request to get Nationality : {}", id);
    Nationality nationality = nationalityRepository.findOne(id);
    NationalityDTO nationalityDTO = nationalityMapper.nationalityToNationalityDTO(nationality);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(nationalityDTO));
  }

  /**
   * DELETE  /nationalities/:id : delete the "id" nationality.
   *
   * @param id the id of the nationalityDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/nationalities/{id}")
  @Timed
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteNationality(@PathVariable Long id) {
    log.debug("REST request to delete Nationality : {}", id);
    nationalityRepository.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * POST  /bulk-nationalities : Bulk create a new nationalities.
   *
   * @param nationalityDTOS List of the nationalityDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new nationalityDTOS, or with status 400 (Bad Request) if the NationalityDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-nationalities")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<NationalityDTO>> bulkCreateNationality(@Valid @RequestBody List<NationalityDTO> nationalityDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save NationalityDtos : {}", nationalityDTOS);
    if (!Collections.isEmpty(nationalityDTOS)) {
      List<Long> entityIds = nationalityDTOS.stream()
          .filter(nationalityDTO -> nationalityDTO.getId() != null)
          .map(nationalityDTO -> nationalityDTO.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new nationalities cannot already have an ID")).body(null);
      }
    }
    List<Nationality> nationalities = nationalityMapper.nationalityDTOsToNationalities(nationalityDTOS);
    nationalities = nationalityRepository.save(nationalities);
    List<NationalityDTO> result = nationalityMapper.nationalitiesToNationalityDTOs(nationalities);
    return ResponseEntity.ok()
        .body(result);
  }

  /**
   * PUT  /bulk-nationalities : Updates an existing nationalities.
   *
   * @param nationalityDTOS List of the nationalityDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated nationalityDTOS,
   * or with status 400 (Bad Request) if the nationalityDTOS is not valid,
   * or with status 500 (Internal Server Error) if the nationalityDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-nationalities")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<NationalityDTO>> bulkUpdateNationality(@Valid @RequestBody List<NationalityDTO> nationalityDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update NationalityDtos : {}", nationalityDTOS);
    if (Collections.isEmpty(nationalityDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(nationalityDTOS)) {
      List<NationalityDTO> entitiesWithNoId = nationalityDTOS.stream().filter(nationalityDTO -> nationalityDTO.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<Nationality> nationalities = nationalityMapper.nationalityDTOsToNationalities(nationalityDTOS);
    nationalities = nationalityRepository.save(nationalities);
    List<NationalityDTO> results = nationalityMapper.nationalitiesToNationalityDTOs(nationalities);
    return ResponseEntity.ok()
        .body(results);
  }

}
