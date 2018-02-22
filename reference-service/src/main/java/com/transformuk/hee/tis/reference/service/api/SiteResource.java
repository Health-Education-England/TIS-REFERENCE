package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.transformuk.hee.tis.reference.api.dto.LimitedListResponse;
import com.transformuk.hee.tis.reference.api.dto.SexualOrientationDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.api.dto.validation.Create;
import com.transformuk.hee.tis.reference.api.dto.validation.Update;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.SexualOrientation;
import com.transformuk.hee.tis.reference.service.model.Site;
import com.transformuk.hee.tis.reference.service.repository.SiteRepository;
import com.transformuk.hee.tis.reference.service.service.impl.SitesTrustsService;
import com.transformuk.hee.tis.reference.service.service.mapper.SiteMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.transformuk.hee.tis.reference.service.api.util.StringUtil.sanitize;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * REST controller for managing Site.
 */
@RestController
@RequestMapping("/api")
public class SiteResource {

  private static final String ENTITY_NAME = "site";
  private final Logger log = LoggerFactory.getLogger(SiteResource.class);
  private final SiteRepository siteRepository;
  private final SiteMapper siteMapper;
  private final SitesTrustsService sitesTrustsService;

  private final int limit;

  public SiteResource(SiteRepository siteRepository, SiteMapper siteMapper, SitesTrustsService sitesTrustsService,
                      @Value("${search.result.limit:100}") int limit) {
    this.siteRepository = siteRepository;
    this.siteMapper = siteMapper;
    this.sitesTrustsService = sitesTrustsService;
    this.limit = limit;
  }

  /**
   * POST  /sites : Create a new site.
   *
   * @param siteDTO the siteDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new siteDTO, or with status 400 (Bad Request) if the site has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/sites")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<SiteDTO> createSite(@Validated(Create.class) @RequestBody SiteDTO siteDTO) throws URISyntaxException {
    log.debug("REST request to save Site : {}", siteDTO);
    Site site = siteMapper.siteDTOToSite(siteDTO);
    site = siteRepository.save(site);
    SiteDTO result = siteMapper.siteToSiteDTO(site);
    return ResponseEntity.created(new URI("/api/sites/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /sites : Updates an existing site.
   *
   * @param siteDTO the siteDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated siteDTO,
   * or with status 400 (Bad Request) if the siteDTO is not valid,
   * or with status 500 (Internal Server Error) if the siteDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/sites")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<SiteDTO> updateSite(@Validated(Update.class) @RequestBody SiteDTO siteDTO) {
    log.debug("REST request to update Site : {}", siteDTO);
    Site site = siteMapper.siteDTOToSite(siteDTO);
    site = siteRepository.save(site);
    SiteDTO result = siteMapper.siteToSiteDTO(site);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, siteDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /sites : get all the sites or search for sites
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of sites in body
   * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
   */
  @GetMapping("/sites")
  @Timed
  public ResponseEntity<List<SiteDTO>> getAllSites(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson) throws IOException {
    log.debug("REST request to get a page of Sites");
    searchQuery = sanitize(searchQuery);
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    Page<Site> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = siteRepository.findAll(pageable);
    } else {
      page = sitesTrustsService.advanceSearchSite(searchQuery, columnFilters, pageable);
    }
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sites");
    return new ResponseEntity<>(siteMapper.sitesToSiteDTOs(page.getContent()), headers, HttpStatus.OK);
  }

  /**
   * GET  /sites/in/:codes : get sites given to codes.
   * Ignores malformed or not found sites
   *
   * @param codes the codes to search by
   * @return the ResponseEntity with status 200 (OK) and with body the list of siteDTOs, or empty list
   */
  @GetMapping("/sites/in/{codes}")
  @Timed
  public ResponseEntity<List<SiteDTO>> getSitesIn(@PathVariable String codes) {
    log.debug("REST request to find several  Sites");
    List<SiteDTO> resp = new ArrayList<>();
    Set<String> codeSet = new HashSet<>();

    if (codes == null || codes.isEmpty()) {
      return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    codeSet.addAll(Arrays.asList(codes.split(",")));

    if (!codeSet.isEmpty()) {
      List<Site> sites = siteRepository.findBySiteCodeIn(codeSet);
      resp = siteMapper.sitesToSiteDTOs(sites);
      return new ResponseEntity<>(resp, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
    }
  }

  /**
   * GET  /sites/ids/in : get sites given to ids.
   * Ignores malformed or not found sites
   *
   * @param ids the ids to search by
   * @return the ResponseEntity with status 200 (OK) and with body the list of siteDTOs, or empty list
   */
  @ApiOperation(value = "get a collection of sites by id")
  @GetMapping("/sites/ids/in")
  @Timed
  public ResponseEntity<List<SiteDTO>> getSitesInById(@RequestParam List<Long> ids) {
    log.debug("REST request to find several Sites by ids {}", ids);
    List<SiteDTO> resp = new ArrayList<>();
    if (CollectionUtils.isEmpty(ids)) {
      return new ResponseEntity<>(resp, HttpStatus.OK);
    } else {
      List<Site> sites = siteRepository.findAll(ids);
      resp = siteMapper.sitesToSiteDTOs(sites);
      return new ResponseEntity<>(resp, CollectionUtils.isEmpty(resp) ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
  }


  @ApiOperation(value = "searchSites()",
      notes = "Returns a list of sites matching given search string",
      response = List.class, responseContainer = "Sites List")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Sites list", response = LimitedListResponse.class)})
  @RequestMapping(method = GET, value = "/sites/search")
  public LimitedListResponse<SiteDTO> searchSites(
      @RequestParam(value = "searchString", required = false) String searchString) {
    List<SiteDTO> ret = siteMapper.sitesToSiteDTOs(sitesTrustsService.searchSites(searchString));
    return new LimitedListResponse<>(ret, limit);
  }

  @ApiOperation(value = "searchSitesWithinATrustCode()",
      notes = "Returns a list of sites with a given trust code and given search string",
      response = List.class, responseContainer = "Sites List")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Sites list", response = LimitedListResponse.class)})
  @RequestMapping(method = GET, value = "/sites/search-by-trust/{trustCode}")
  public LimitedListResponse<SiteDTO> searchSitesWithinATrustCode(@PathVariable(value = "trustCode") String trustCode,
                                                                  @RequestParam(value = "searchString", required = false)
                                                                      String searchString) {
    List<SiteDTO> ret = siteMapper.sitesToSiteDTOs(sitesTrustsService.searchSitesWithinTrust(trustCode, searchString));
    return new LimitedListResponse<>(ret, limit);
  }

  /**
   * GET  /sites/:siteCode : get the site by "siteCode" site.
   *
   * @param id the siteCode of the siteDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the siteDTO, or with status 404 (Not Found)
   */
  @GetMapping("/sites/{id}")
  @Timed
  public ResponseEntity<SiteDTO> getSite(@PathVariable Long id) {
    log.debug("REST request to get Site : {}", id);
    Site site = siteRepository.findOne(id);
    SiteDTO siteDTO = siteMapper.siteToSiteDTO(site);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(siteDTO));
  }

  /**
   * GET  /sites/code/:code : get the "code" site.
   *
   * @param code the code of the siteDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the siteDTO, or with status 404 (Not Found)
   */
  @GetMapping("/sites/code/{code}")
  @Timed
  public ResponseEntity<SiteDTO> getSiteByCode(@PathVariable String code) {
    log.debug("REST request to get Site by code : {}", code);
    Site site = siteRepository.findBySiteCode(code);
    SiteDTO siteDTO = siteMapper.siteToSiteDTO(site);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(siteDTO));
  }

  /**
   * EXISTS /sites/exists : check if site exists
   *
   * @param siteCodes the sitesCodes of the siteDTO to check
   * @return boolean true if exists otherwise false
   */
  @PostMapping("/sites/exists")
  @Timed
  public ResponseEntity<Map<String, Boolean>> siteExists(@RequestBody List<String> siteCodes) {
    Map<String, Boolean> siteExistsMap = Maps.newHashMap();
    log.debug("REST request to check Site exists : {}", siteCodes);
    if (!CollectionUtils.isEmpty(siteCodes)) {
      List<String> dbIds = siteRepository.findSiteCodesBySiteCodeIn(siteCodes);
      siteCodes.forEach(siteCode -> {
        if (dbIds.contains(siteCode)) {
          siteExistsMap.put(siteCode, true);
        } else {
          siteExistsMap.put(siteCode, false);
        }
      });
    }

    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(siteExistsMap));
  }

  /**
   * EXISTS /sites/ids/exists : check if site exists
   *
   * @param ids the sitesCodes of the siteDTO to check
   * @return boolean true if exists otherwise false
   */
  @PostMapping("/sites/ids/exists")
  @Timed
  public ResponseEntity<Map<Long, Boolean>> siteIdsExists(@RequestBody List<Long> ids) {
    Map<Long, Boolean> siteExistsMap = Maps.newHashMap();
    log.debug("REST request to check Site exists : {}", ids);
    if (!CollectionUtils.isEmpty(ids)) {
      List<Site> foundSites = siteRepository.findAll(ids);
      Set<Long> dbIds = foundSites.stream().map(Site::getId).collect(Collectors.toSet());
      ids.forEach(siteCode -> {
        if (dbIds.contains(siteCode)) {
          siteExistsMap.put(siteCode, true);
        } else {
          siteExistsMap.put(siteCode, false);
        }
      });
    }

    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(siteExistsMap));
  }

  /**
   * EXISTS /sites/codeexists/ : check if site code exists
   *
   * @param code site code to check
   * @return HttpStatus FOUND if exists or NOT_FOUND if doesn't exist
   */
  @PostMapping("/sites/codeexists/")
  @Timed
  public ResponseEntity siteCodeExists(@RequestBody String code) {
    log.debug("REST request to check Site exists : {}", code);
    HttpStatus siteFound = HttpStatus.NO_CONTENT;
    if (!code.isEmpty()) {
      String foundCode = siteRepository.findIdBySiteCode(code);
      if (foundCode != null) {
        siteFound = HttpStatus.FOUND;
      }
    }
    return new ResponseEntity<>(siteFound);
  }

  /**
   * EXISTS /sites/trustmatch/{trustCode} : check is site exists
   *
   * @param siteCode the code of the site to check
   * @return HttpStatus FOUND if matched, NOT_FOUND if no match
   * @Param trustCode the code of the trust to check
   */
  @PostMapping("/sites/trustmatch/{trustCode}")
  @Timed
  public ResponseEntity siteTrustMatch(@RequestBody String siteCode, @PathVariable String trustCode) {
    log.debug("REST request to check Site exists : {} {}", siteCode, trustCode);
    HttpStatus siteTrustMatchFound = HttpStatus.NO_CONTENT;
    if (!siteCode.isEmpty() && !trustCode.isEmpty()) {
      List<String> dbsiteCodes = siteRepository.findSiteTrustMatch(siteCode, trustCode);
      if (!dbsiteCodes.isEmpty()) {
        siteTrustMatchFound = HttpStatus.FOUND;
      }
    }
    return new ResponseEntity<>(siteTrustMatchFound);
  }


  /**
   * POST  /bulk-sites : bulk creates a new site.
   *
   * @param siteDTOs the siteDTOs to create
   * @return the ResponseEntity with status 201 (Created) and with body the new siteDTOs, or with status 400 (Bad Request) if the site has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-sites")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<SiteDTO>> bulkCreateSite(@Valid @RequestBody List<SiteDTO> siteDTOs) throws URISyntaxException {
    log.debug("REST request to bulk save Site : {}", siteDTOs);

    List<Site> sites = siteMapper.siteDTOsToSites(siteDTOs);
    sites = siteRepository.save(sites);
    List<SiteDTO> results = siteMapper.sitesToSiteDTOs(sites);
    return ResponseEntity.ok()
        .body(results);
  }

  /**
   * PUT  /bulk-sites : Bulk updates an existing site.
   *
   * @param siteDTOs the siteDTOs to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated siteDTOs,
   * or with status 400 (Bad Request) if the siteDTOs is not valid,
   * or with status 500 (Internal Server Error) if the siteDTOs couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-sites")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<SiteDTO>> bulkUpdateSite(@Valid @RequestBody List<SiteDTO> siteDTOs) throws URISyntaxException {
    log.debug("REST request to bulk update Site : {}", siteDTOs);
    if (Collections.isEmpty(siteDTOs)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(siteDTOs)) {
      List<SiteDTO> entitiesWithNoId = siteDTOs.stream().filter(site -> site.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no site code, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }

    List<Site> sites = siteMapper.siteDTOsToSites(siteDTOs);
    sites = siteRepository.save(sites);
    List<SiteDTO> results = siteMapper.sitesToSiteDTOs(sites);
    return ResponseEntity.ok()
        .body(results);
  }

}
