package com.transformuk.hee.tis.reference.service.api;

import static com.transformuk.hee.tis.security.util.TisSecurityHelper.getProfileFromContext;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.LocalOfficeDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.LocalOffice;
import com.transformuk.hee.tis.reference.service.repository.LocalOfficeRepository;
import com.transformuk.hee.tis.reference.service.service.impl.SitesTrustsService;
import com.transformuk.hee.tis.reference.service.service.mapper.DbcToLocalOfficeMapper;
import com.transformuk.hee.tis.reference.service.service.mapper.LocalOfficeMapper;
import com.transformuk.hee.tis.security.model.UserProfile;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.nhs.tis.StringConverter;

/**
 * REST controller for managing LocalOffice.
 */
@RestController
@RequestMapping("/api")
public class LocalOfficeResource {

  private static final String ENTITY_NAME = "localOffice";
  private final Logger log = LoggerFactory.getLogger(LocalOfficeResource.class);
  private final LocalOfficeRepository localOfficeRepository;
  private final SitesTrustsService sitesTrustsService;

  private final LocalOfficeMapper localOfficeMapper;

  public LocalOfficeResource(LocalOfficeRepository localOfficeRepository,
      SitesTrustsService sitesTrustsService, LocalOfficeMapper localOfficeMapper) {
    this.localOfficeRepository = localOfficeRepository;
    this.localOfficeMapper = localOfficeMapper;
    this.sitesTrustsService = sitesTrustsService;
  }

  /**
   * POST  /local-offices : Create a new localOffice.
   *
   * @param localOfficeDTO the localOfficeDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new localOfficeDTO, or
   * with status 400 (Bad Request) if the localOffice has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/local-offices")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<LocalOfficeDTO> createLocalOffice(
      @Valid @RequestBody LocalOfficeDTO localOfficeDTO) throws URISyntaxException {
    log.debug("REST request to save LocalOffice : {}", localOfficeDTO);
    if (localOfficeDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil
          .createFailureAlert(ENTITY_NAME, "idexists",
              "A new localOffice cannot already have an ID")).body(null);
    }
    LocalOffice localOffice = localOfficeMapper.localOfficeDTOToLocalOffice(localOfficeDTO);
    localOffice = localOfficeRepository.save(localOffice);
    LocalOfficeDTO result = localOfficeMapper.localOfficeToLocalOfficeDTO(localOffice);
    return ResponseEntity.created(new URI("/api/local-offices/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /local-offices : Updates an existing localOffice.
   *
   * @param localOfficeDTO the localOfficeDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated localOfficeDTO, or
   * with status 400 (Bad Request) if the localOfficeDTO is not valid, or with status 500 (Internal
   * Server Error) if the localOfficeDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/local-offices")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<LocalOfficeDTO> updateLocalOffice(
      @Valid @RequestBody LocalOfficeDTO localOfficeDTO) throws URISyntaxException {
    log.debug("REST request to update LocalOffice : {}", localOfficeDTO);
    if (localOfficeDTO.getId() == null) {
      return createLocalOffice(localOfficeDTO);
    }
    LocalOffice localOffice = localOfficeMapper.localOfficeDTOToLocalOffice(localOfficeDTO);
    localOffice = localOfficeRepository.save(localOffice);
    LocalOfficeDTO result = localOfficeMapper.localOfficeToLocalOfficeDTO(localOffice);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, localOfficeDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /local-offices : get all the localOffices.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of localOffices in body
   * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
   */
  @GetMapping("/local-offices")
  public ResponseEntity<List<LocalOfficeDTO>> getAllLocalOffices(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson)
      throws IOException {
    log.debug("REST request to get a page of LocalOffices");
    searchQuery = StringConverter.getConverter(searchQuery).fromJson().decodeUrl().escapeForSql()
        .toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil
        .getColumnFilters(columnFilterJson, filterEnumList);

    Page<LocalOffice> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = localOfficeRepository.findAll(pageable);
    } else {
      page = sitesTrustsService.advanceSearchLocalOffice(searchQuery, columnFilters, pageable);
    }

    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/local-offices");
    return new ResponseEntity<>(localOfficeMapper.localOfficesToLocalOfficeDTOs(page.getContent()),
        headers, HttpStatus.OK);
  }


  /**
   * GET  /local-offices/user : get all the allowed local offices for the logged in user.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of localOffices in body
   * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
   */
  @GetMapping("/local-offices/user")
  public ResponseEntity<List<LocalOfficeDTO>> getUserLocalOffices() {
    log.debug("REST request to get page of Local Offices for current user");
    UserProfile userProfile = getProfileFromContext();
    Set<String> allowedBodyCodes = userProfile.getDesignatedBodyCodes();
    Set<String> allowedLocalOffices = DbcToLocalOfficeMapper.map(allowedBodyCodes);
    List<LocalOffice> localOfficeList = localOfficeRepository.findByNameIn(allowedLocalOffices);
    List<LocalOfficeDTO> loDtoList = localOfficeMapper
        .localOfficesToLocalOfficeDTOs(localOfficeList);
    return new ResponseEntity<>(loDtoList, HttpStatus.OK);
  }


  /**
   * GET  /local-offices/:id : get the "id" localOffice.
   *
   * @param id the id of the localOfficeDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the localOfficeDTO, or with
   * status 404 (Not Found)
   */
  @GetMapping("/local-offices/{id}")
  public ResponseEntity<LocalOfficeDTO> getLocalOffice(@PathVariable Long id) {
    log.debug("REST request to get LocalOffice : {}", id);
    LocalOffice localOffice = localOfficeRepository.findById(id).orElse(null);
    LocalOfficeDTO localOfficeDTO = localOfficeMapper.localOfficeToLocalOfficeDTO(localOffice);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(localOfficeDTO));
  }

  /**
   * DELETE  /local-offices/:id : delete the "id" localOffice.
   *
   * @param id the id of the localOfficeDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/local-offices/{id}")
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteLocalOffice(@PathVariable Long id) {
    log.debug("REST request to delete LocalOffice : {}", id);
    localOfficeRepository.deleteById(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * POST  /bulk-local-offices : Bulk create a new local-offices.
   *
   * @param localOfficeDTOS List of the localOfficeDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new localOfficeDTOS, or
   * with status 400 (Bad Request) if the LocalOfficeDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-local-offices")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<LocalOfficeDTO>> bulkCreateLocalOffice(
      @Valid @RequestBody List<LocalOfficeDTO> localOfficeDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save LocalOfficeDtos : {}", localOfficeDTOS);
    if (!Collections.isEmpty(localOfficeDTOS)) {
      List<Long> entityIds = localOfficeDTOS.stream()
          .filter(localOfficeDTO -> localOfficeDTO.getId() != null)
          .map(localOfficeDTO -> localOfficeDTO.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil
            .createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist",
                "A new localOffices cannot already have an ID")).body(null);
      }
    }
    List<LocalOffice> localOffices = localOfficeMapper
        .localOfficeDTOsToLocalOffices(localOfficeDTOS);
    localOffices = localOfficeRepository.saveAll(localOffices);
    List<LocalOfficeDTO> result = localOfficeMapper.localOfficesToLocalOfficeDTOs(localOffices);
    return ResponseEntity.ok()
        .body(result);
  }

  /**
   * PUT  /bulk-local-offices : Updates an existing local-offices.
   *
   * @param localOfficeDTOS List of the localOfficeDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated localOfficeDTOS, or
   * with status 400 (Bad Request) if the localOfficeDTOS is not valid, or with status 500 (Internal
   * Server Error) if the localOfficeDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-local-offices")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<LocalOfficeDTO>> bulkUpdateLocalOffice(
      @Valid @RequestBody List<LocalOfficeDTO> localOfficeDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update LocalOfficeDtos : {}", localOfficeDTOS);
    if (Collections.isEmpty(localOfficeDTOS)) {
      return ResponseEntity.badRequest()
          .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
              "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(localOfficeDTOS)) {
      List<LocalOfficeDTO> entitiesWithNoId = localOfficeDTOS.stream()
          .filter(at -> at.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest()
            .headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
                "bulk.update.failed.noId",
                "Some DTOs you've provided have no Id, cannot update entities that dont exist"))
            .body(entitiesWithNoId);
      }
    }
    List<LocalOffice> localOffices = localOfficeMapper
        .localOfficeDTOsToLocalOffices(localOfficeDTOS);
    localOffices = localOfficeRepository.saveAll(localOffices);
    List<LocalOfficeDTO> results = localOfficeMapper.localOfficesToLocalOfficeDTOs(localOffices);
    return ResponseEntity.ok()
        .body(results);
  }
}
