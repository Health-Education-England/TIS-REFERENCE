package com.transformuk.hee.tis.reference.service.api;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.transformuk.hee.tis.reference.api.dto.LimitedListResponse;
import com.transformuk.hee.tis.reference.api.dto.TrustDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.api.util.UrlDecoderUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.Trust;
import com.transformuk.hee.tis.reference.service.repository.TrustRepository;
import com.transformuk.hee.tis.reference.service.service.impl.SitesTrustsService;
import com.transformuk.hee.tis.reference.service.service.mapper.TrustMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import javax.validation.Valid;
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
 * REST controller for managing Trust.
 */
@RestController
@RequestMapping("/api")
public class TrustResource {

  private static final String ENTITY_NAME = "trust";
  private final Logger log = LoggerFactory.getLogger(TrustResource.class);
  private final TrustRepository trustRepository;
  private final TrustMapper trustMapper;
  private final SitesTrustsService sitesTrustsService;

  private final int limit;

  public TrustResource(TrustRepository trustRepository, TrustMapper trustMapper,
      SitesTrustsService sitesTrustsService,
      @Value("${search.result.limit:100}") int limit) {
    this.trustRepository = trustRepository;
    this.trustMapper = trustMapper;
    this.sitesTrustsService = sitesTrustsService;
    this.limit = limit;
  }

  /**
   * POST  /trusts : Create a new trust.
   *
   * @param trustDTO the trustDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new trustDTO, or with
   *     status 400 (Bad Request) if the trust has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/trusts")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<TrustDTO> createTrust(@Valid @RequestBody TrustDTO trustDTO)
      throws URISyntaxException {
    log.debug("REST request to save Trust : {}", trustDTO);
    if (trustDTO.getId() != null && trustRepository.findById(trustDTO.getId()).isPresent()) {
      return ResponseEntity.badRequest().headers(HeaderUtil
          .createFailureAlert(ENTITY_NAME, "idexists", "A Trust already exists with that Code"))
          .body(null);
    }
    Trust trust = trustMapper.trustDTOToTrust(trustDTO);
    trust = trustRepository.save(trust);
    TrustDTO result = trustMapper.trustToTrustDTO(trust);
    return ResponseEntity.created(new URI("/api/trusts/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /trusts : Updates an existing trust.
   *
   * @param trustDTO the trustDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated trustDTO, or with
   *     status 400 (Bad Request) if the trustDTO is not valid, or with status 500 (Internal Server
   *     Error) if the trustDTO couldn't be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/trusts")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<TrustDTO> updateTrust(@RequestBody TrustDTO trustDTO)
      throws URISyntaxException {
    log.debug("REST request to update Trust : {}", trustDTO);
    if (trustDTO.getId() == null) {
      return createTrust(trustDTO);
    }
    Trust trust = trustMapper.trustDTOToTrust(trustDTO);
    trust = trustRepository.save(trust);
    TrustDTO result = trustMapper.trustToTrustDTO(trust);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, trustDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /trusts : get all the trusts.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of trusts in body
   * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
   */
  @GetMapping("/trusts")
  public ResponseEntity<List<TrustDTO>> getAllTrusts(
      @ApiParam Pageable pageable,
      @RequestParam(value = "searchQuery", required = false) String searchQuery,
      @ApiParam(value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"")
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson)
      throws IOException {
    log.debug("REST request to get a page of Trusts");
    searchQuery = StringConverter.getConverter(searchQuery).fromJson().decodeUrl().escapeForSql()
        .toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    if (columnFilterJson != null) {
      columnFilterJson = UrlDecoderUtil.decode(columnFilterJson);
    }
    List<ColumnFilter> columnFilters = ColumnFilterUtil
        .getColumnFilters(columnFilterJson, filterEnumList);
    Page<Trust> page;
    if (StringUtils.isEmpty(columnFilterJson)) {
      page = trustRepository.findAll(pageable);
    } else {
      page = sitesTrustsService.advanceSearchTrust(searchQuery, columnFilters, pageable);
    }
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/trusts");
    return new ResponseEntity<>(trustMapper.trustsToTrustDTOs(page.getContent()), headers,
        HttpStatus.OK);
  }

  /**
   * GET  /current/trusts : get all the current trusts.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of trusts in body
   * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
   */
  @GetMapping("/current/trusts")
  public ResponseEntity<List<TrustDTO>> getAllCurrentTrusts(
      @ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched")
      @RequestParam(value = "searchQuery", required = false) String searchQuery) {
    log.debug("REST request to get a page of Trusts");
    searchQuery = StringConverter.getConverter(searchQuery).fromJson().decodeUrl().escapeForSql()
        .toString();
    Page<Trust> page;
    if (StringUtils.isEmpty(searchQuery)) {
      Trust trust = new Trust();
      trust.setStatus(Status.CURRENT);
      page = trustRepository.findAll(Example.of(trust), pageable);
    } else {
      page = sitesTrustsService.searchTrusts(searchQuery, pageable);
    }
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/trusts");
    return new ResponseEntity<>(trustMapper.trustsToTrustDTOs(page.getContent()), headers,
        HttpStatus.OK);
  }

  /**
   * GET  /trusts/in/:codes : get trusts given to codes. Ignores malformed or not found trusts
   *
   * @param codes the codes to search by
   * @return the ResponseEntity with status 200 (OK) and with body the list of trustDTOs, or empty
   *     list
   */
  @GetMapping("/trusts/in/{codes}")
  public ResponseEntity<List<TrustDTO>> getTrustsIn(@PathVariable String codes) {
    log.debug("REST request to find several  Trusts");
    List<TrustDTO> resp = new ArrayList<>();
    Set<String> codeSet = new HashSet<>();

    if (codes == null || codes.isEmpty()) {
      return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    codeSet.addAll(Arrays.asList(codes.split(",")));

    if (!codeSet.isEmpty()) {
      List<Trust> trusts = trustRepository.findByCodeIn(codeSet);
      resp = trustMapper.trustsToTrustDTOs(trusts);
      return new ResponseEntity<>(resp, HttpStatus.FOUND);
    } else {
      return new ResponseEntity<>(resp, HttpStatus.OK);
    }
  }

  /**
   * GET  /trusts/ids/in : get trusts given to ids. Ignores malformed or not found trusts
   *
   * @param ids the ids to search by
   * @return the ResponseEntity with status 200 (OK) and with body the list of trustDTOs, or empty
   *     list
   */
  @ApiOperation("Get a list of trusts by id")
  @GetMapping("/trusts/ids/in")
  public ResponseEntity<List<TrustDTO>> getTrustsIdsIn(@RequestParam List<Long> ids) {
    log.debug("REST request to find several Trusts by id");
    List<TrustDTO> resp = new ArrayList<>();

    if (CollectionUtils.isEmpty(ids)) {
      return new ResponseEntity<>(resp, HttpStatus.OK);
    } else {
      List<Trust> trusts = trustRepository.findAllById(ids);
      resp = trustMapper.trustsToTrustDTOs(trusts);
      return new ResponseEntity<>(resp,
          CollectionUtils.isEmpty(resp) ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
  }

  @ApiOperation(value = "searchTrusts()",
      notes = "Returns a list of trusts matching given search string",
      response = List.class, responseContainer = "Trusts List")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Trusts list", response = LimitedListResponse.class)})
  @RequestMapping(method = GET, value = "/trusts/search")
  public LimitedListResponse<TrustDTO> searchTrusts(
      @RequestParam(value = "searchString") String searchString) {
    List<TrustDTO> ret = trustMapper
        .trustsToTrustDTOs(sitesTrustsService.searchTrusts(searchString));
    return new LimitedListResponse<>(ret, limit);
  }

  /**
   * GET  /trusts/:id : get trust by id.
   *
   * @param id the code of the trustDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the trustDTO, or with status 404
   *     (Not Found)
   */
  @GetMapping("/trusts/{id}")
  public ResponseEntity<TrustDTO> getTrust(@PathVariable Long id) {
    log.debug("REST request to get Trust by id: {}", id);
    Trust trust = trustRepository.findById(id).orElse(null);
    TrustDTO trustDTO = trustMapper.trustToTrustDTO(trust);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(trustDTO));
  }

  /**
   * GET  /trusts/code/:code : get the "code" trust.
   *
   * @param code the code of the trustDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the trustDTO, or with status 404
   *     (Not Found)
   */
  @GetMapping("/trusts/code/{code}")
  public ResponseEntity<TrustDTO> getTrustByCode(@PathVariable String code) {
    log.debug("REST request to get Trust by code: {}", code);
    List<Trust> trusts = trustRepository.findByCodeAndStatus(code, Status.CURRENT);
    TrustDTO trustDTO = null;
    if (trusts.size() > 0) {
      trustDTO = trustMapper.trustToTrustDTO(trusts.get(0));
    }
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(trustDTO));
  }

  /**
   * POST  /bulk-trusts : Create a new trust.
   *
   * @param trustDTOs the trustDTOs to create
   * @return the ResponseEntity with status 201 (Created) and with body the new trustDTOs, or with
   *     status 400 (Bad Request) if the trust has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-trusts")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<TrustDTO>> bulkCreateTrust(
      @Valid @RequestBody List<TrustDTO> trustDTOs) throws URISyntaxException {
    log.info("REST request to bulk save Trust : {}", trustDTOs);
    if (!Collections.isEmpty(trustDTOs)) {
      List<String> entityIds = trustDTOs.stream()
          .filter(trust -> trust.getId() != null)
          .map(TrustDTO::getCode)
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil
            .createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist",
                "A new Trust cannot already have an ID")).body(null);
      }
    }
    List<Trust> trusts = trustMapper.trustDTOsToTrusts(trustDTOs);
    trusts = trustRepository.saveAll(trusts);
    List<TrustDTO> results = trustMapper.trustsToTrustDTOs(trusts);
    return ResponseEntity.ok()
        .body(results);
  }

  /**
   * PUT  /bulk-trusts : Updates an existing trust.
   *
   * @param trustDTOs the trustDTOs to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated trustDTOs, or with
   *     status 400 (Bad Request) if the trustDTOs is not valid, or with status 500 (Internal Server
   *     Error) if the trustDTOs couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-trusts")
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<TrustDTO>> bulkUpdateTrust(
      @Valid @RequestBody List<TrustDTO> trustDTOs) throws URISyntaxException {
    log.info("REST request to update Trust : {}", trustDTOs);
    if (Collections.isEmpty(trustDTOs)) {
      return ResponseEntity.badRequest()
          .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
              "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(trustDTOs)) {
      List<TrustDTO> entitiesWithNoId = trustDTOs.stream().filter(trust -> trust.getId() == null)
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest()
            .headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
                "bulk.update.failed.noId",
                "Some DTOs you've provided have no Id, cannot update entities that dont exist"))
            .body(entitiesWithNoId);
      }
    }

    List<Trust> trusts = trustMapper.trustDTOsToTrusts(trustDTOs);
    trusts = trustRepository.saveAll(trusts);
    List<TrustDTO> results = trustMapper.trustsToTrustDTOs(trusts);

    return ResponseEntity.ok()
        .body(results);
  }

  /**
   * EXISTS /trusts/exists/ : check if trust exists
   *
   * @param codes the codes of the trustDTO to check
   * @return boolean true if exists otherwise false
   */
  @PostMapping("/trusts/exists/")
  public ResponseEntity<Map<String, Boolean>> trustExists(@RequestBody List<String> codes) {
    Map<String, Boolean> trustExistsMap = Maps.newHashMap();
    log.debug("REST request to check Trust exists : {}", codes);
    if (!CollectionUtils.isEmpty(codes)) {
      List<String> dbIds = trustRepository.findCodesByCodesIn(codes);
      codes.forEach(code -> {
        boolean isMatch = dbIds.stream().anyMatch(code::equalsIgnoreCase);
        trustExistsMap.put(code, isMatch);
      });
    }
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(trustExistsMap));
  }


  /**
   * EXISTS /trusts/ids/exists/ : check if trust exists
   *
   * @param ids the ids of the trustDTO to check
   * @return boolean true if exists otherwise false
   */
  @PostMapping("/trusts/ids/exists/")
  public ResponseEntity<Map<Long, Boolean>> trustIdsExists(@RequestBody List<Long> ids) {
    Map<Long, Boolean> trustExistsMap = Maps.newHashMap();
    log.debug("REST request to check Trust exists : {}", ids);
    if (!CollectionUtils.isEmpty(ids)) {
      Set<Long> dbIds = trustRepository.findAllById(ids).stream().map(Trust::getId)
          .collect(Collectors.toSet());
      ids.forEach(code -> {
        if (dbIds.contains(code)) {
          trustExistsMap.put(code, true);
        } else {
          trustExistsMap.put(code, false);
        }
      });
    }
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(trustExistsMap));
  }

  /**
   * EXISTS /trusts/codeexists/ : check if trust code exists
   *
   * @param code trust code to check
   * @return HttpStatus FOUND if exists or NOT_FOUND if doesn't exist
   */
  @PostMapping("/trusts/codeexists/")
  public ResponseEntity trustCodeExists(@RequestBody String code) {
    log.debug("REST request to check Trust exists : {}", code);
    HttpStatus trustFound = HttpStatus.NO_CONTENT;
    if (!code.isEmpty()) {
      Trust trust = trustRepository.findByCode(code);
      if (trust != null) {
        trustFound = HttpStatus.FOUND;
      }
    }
    return new ResponseEntity<>(trustFound);
  }
}
