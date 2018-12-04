package com.transformuk.hee.tis.reference.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.GenderDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.Gender;
import com.transformuk.hee.tis.reference.service.repository.GenderRepository;
import com.transformuk.hee.tis.reference.service.service.impl.GenderServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.GenderMapper;
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
 * REST controller for managing Gender.
 */
@RestController
@RequestMapping("/api")
public class GenderResource {

  private static final String ENTITY_NAME = "gender";
  private final Logger log = LoggerFactory.getLogger(GenderResource.class);

  private final GenderRepository genderRepository;
  private final GenderMapper genderMapper;
  private final GenderServiceImpl genderService;

  public GenderResource(GenderRepository genderRepository, GenderMapper genderMapper, GenderServiceImpl genderService) {
    this.genderRepository = genderRepository;
    this.genderMapper = genderMapper;
    this.genderService = genderService;
  }

  /**
   * POST  /genders : Create a new gender.
   *
   * @param genderDTO the genderDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new genderDTO, or with status 400 (Bad Request) if the gender has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/genders")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<GenderDTO> createGender(@Valid @RequestBody GenderDTO genderDTO) throws URISyntaxException {
    log.debug("REST request to save Gender : {}", genderDTO);
    if (genderDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new gender cannot already have an ID")).body(null);
    }
    Gender gender = genderMapper.genderDTOToGender(genderDTO);
    gender = genderRepository.save(gender);
    GenderDTO result = genderMapper.genderToGenderDTO(gender);
    return ResponseEntity.created(new URI("/api/genders/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /genders : Updates an existing gender.
   *
   * @param genderDTO the genderDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated genderDTO,
   * or with status 400 (Bad Request) if the genderDTO is not valid,
   * or with status 500 (Internal Server Error) if the genderDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/genders")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<GenderDTO> updateGender(@Valid @RequestBody GenderDTO genderDTO) throws URISyntaxException {
    log.debug("REST request to update Gender : {}", genderDTO);
    if (genderDTO.getId() == null) {
      return createGender(genderDTO);
    }
    Gender gender = genderMapper.genderDTOToGender(genderDTO);
    gender = genderRepository.save(gender);
    GenderDTO result = genderMapper.genderToGenderDTO(gender);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, genderDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /genders : get all genders.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of genders in body
   */
  @GetMapping("/genders")
  public ResponseEntity<List<GenderDTO>> getAllGenders(
          Pageable pageable,
          @RequestParam(value = "searchQuery", required = false) String searchQuery,
          @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.info("REST request to get a page of genders begin");
    searchQuery = sanitize(searchQuery);
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    Page<Gender> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = genderRepository.findAll(pageable);
    } else {
      page = genderService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<GenderDTO> results = page.map(genderMapper::genderToGenderDTO);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/genders");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }


  /**
   * GET  /genders/:id : get the "id" gender.
   *
   * @param id the id of the genderDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the genderDTO, or with status 404 (Not Found)
   */
  @GetMapping("/genders/{id}")
  public ResponseEntity<GenderDTO> getGender(@PathVariable Long id) {
    log.debug("REST request to get Gender : {}", id);
      Gender gender = genderRepository.findById(id).orElse(null);
    GenderDTO genderDTO = genderMapper.genderToGenderDTO(gender);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(genderDTO));
  }

  /**
   * EXISTS /genders/exists/ : check is genders exists
   *
   * @param code the code of the genderDTO to check
   * @return boolean true if exists otherwise false
   */
  @PostMapping("/genders/exists/")
  public ResponseEntity<Boolean> genderExists(@RequestBody String code) {
    log.debug("REST request to check Title exists : {}", code);
    Gender gender = genderRepository.findFirstByCode(code);
    if(gender == null){
      return new ResponseEntity<>(false, HttpStatus.OK);
    }
    return new ResponseEntity<>(true, HttpStatus.OK);
  }

  /**
   * DELETE  /genders/:id : delete the "id" gender.
   *
   * @param id the id of the genderDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/genders/{id}")
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteGender(@PathVariable Long id) {
    log.debug("REST request to delete Gender : {}", id);
      genderRepository.deleteById(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * POST  /bulk-genders : Bulk create a new genders.
   *
   * @param genderDTOS List of the genderDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new genderDTOS, or with status 400 (Bad Request) if the GenderDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-genders")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<GenderDTO>> bulkCreateGender(@Valid @RequestBody List<GenderDTO> genderDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save GenderDto : {}", genderDTOS);
    if (!Collections.isEmpty(genderDTOS)) {
      List<Long> entityIds = genderDTOS.stream()
          .filter(genderDTO -> genderDTO.getId() != null)
          .map(genderDTO -> genderDTO.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new genders cannot already have an ID")).body(null);
      }
    }
    List<Gender> genders = genderMapper.genderDTOsToGenders(genderDTOS);
      genders = genderRepository.saveAll(genders);
    List<GenderDTO> result = genderMapper.gendersToGenderDTOs(genders);
    return ResponseEntity.ok()
        .body(result);
  }

  /**
   * PUT  /bulk-genders : Updates an existing genders.
   *
   * @param genderDTOS List of the genderDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated genderDTOS,
   * or with status 400 (Bad Request) if the genderDTOS is not valid,
   * or with status 500 (Internal Server Error) if the genderDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-genders")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<GenderDTO>> bulkUpdateGender(@Valid @RequestBody List<GenderDTO> genderDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update GenderDto : {}", genderDTOS);
    if (Collections.isEmpty(genderDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(genderDTOS)) {
      List<GenderDTO> entitiesWithNoId = genderDTOS.stream().filter(g -> g.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<Gender> genders = genderMapper.genderDTOsToGenders(genderDTOS);
      genders = genderRepository.saveAll(genders);
    List<GenderDTO> results = genderMapper.gendersToGenderDTOs(genders);
    return ResponseEntity.ok()
        .body(results);
  }
}
