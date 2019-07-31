package com.transformuk.hee.tis.reference.service.api;

import static com.transformuk.hee.tis.security.util.TisSecurityHelper.getProfileFromContext;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.DBC;
import com.transformuk.hee.tis.reference.service.repository.DBCRepository;
import com.transformuk.hee.tis.reference.service.service.impl.DBCServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.DBCMapper;
import com.transformuk.hee.tis.security.model.UserProfile;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
 * REST controller for managing DBC.
 */
@RestController
@RequestMapping("/api")
public class DBCResource {

  private static final String ENTITY_NAME = "dBC";
  private final Logger log = LoggerFactory.getLogger(DBCResource.class);

  private final DBCRepository dBCRepository;
  private final DBCMapper dBCMapper;
  private final DBCServiceImpl dbcService;

  public DBCResource(DBCRepository dBCRepository, DBCMapper dBCMapper, DBCServiceImpl dbcService) {
    this.dBCRepository = dBCRepository;
    this.dBCMapper = dBCMapper;
    this.dbcService = dbcService;
  }

  /**
   * POST /dbcs : Create a new dBC.
   *
   * @param dBCDTO the dBCDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new dBCDTO, or with
   * status 400 (Bad Request) if the dBC has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/dbcs")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<DBCDTO> createDBC(@Valid @RequestBody DBCDTO dBCDTO)
      throws URISyntaxException {
    log.debug("REST request to save DBC : {}", dBCDTO);
    if (dBCDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME,
          "idexists", "A new dBC cannot already have an ID")).body(null);
    }
    DBC dBC = dBCMapper.dBCDTOToDBC(dBCDTO);
    dBC = dBCRepository.save(dBC);
    DBCDTO result = dBCMapper.dBCToDBCDTO(dBC);
    return ResponseEntity.created(new URI("/api/dbcs/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT /dbcs : Updates an existing dBC.
   *
   * @param dBCDTO the dBCDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated dBCDTO, or with
   * status 400 (Bad Request) if the dBCDTO is not valid, or with status 500 (Internal Server Error)
   * if the dBCDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/dbcs")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<DBCDTO> updateDBC(@Valid @RequestBody DBCDTO dBCDTO)
      throws URISyntaxException {
    log.debug("REST request to update DBC : {}", dBCDTO);
    if (dBCDTO.getId() == null) {
      return createDBC(dBCDTO);
    }
    DBC dBC = dBCMapper.dBCDTOToDBC(dBCDTO);
    dBC = dBCRepository.save(dBC);
    DBCDTO result = dBCMapper.dBCToDBCDTO(dBC);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dBCDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET /dbcs : get all dbcs.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and the list of colleges in body
   */
  @ApiOperation(value = "Lists countries",
      notes = "Returns a list of countries with support for pagination, sorting, smart search and column filters \n")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "dbcs list")})
  @GetMapping("/dbcs")
  @Timed
  public ResponseEntity<List<DBCDTO>> getAllDbcs(@ApiParam Pageable pageable,
      @ApiParam(value = "any wildcard string to be searched") @RequestParam(value = "searchQuery",
          required = false) String searchQuery,
      @ApiParam(
          value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"") @RequestParam(
          value = "columnFilters", required = false) String columnFilterJson)
      throws IOException {
    log.info("REST request to get a page of dbcs begin");
    searchQuery = StringConverter.getConverter(searchQuery).fromJson().decodeUrl().escapeForSql()
        .toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    List<ColumnFilter> columnFilters =
        ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
    Page<DBC> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = dBCRepository.findAll(pageable);
    } else {
      page = dbcService.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<DBCDTO> results = page.map(dBCMapper::dBCToDBCDTO);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dbcs");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }

  /**
   * GET /dbcs/:id : get the "id" dBC.
   *
   * @param id the id of the dBCDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the dBCDTO, or with status 404
   * (Not Found)
   */
  @GetMapping("/dbcs/{id}")
  @Timed
  public ResponseEntity<DBCDTO> getDBC(@PathVariable Long id) {
    log.debug("REST request to get DBC : {}", id);
    DBC dBC = dBCRepository.findOne(id);
    DBCDTO dBCDTO = dBCMapper.dBCToDBCDTO(dBC);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dBCDTO));
  }

  /**
   * GET /dbcs/user : get all the allowed dbcs for the logged in user.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of localOffices in body
   * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
   */
  @GetMapping("/dbcs/user")
  @Timed
  public ResponseEntity<List<DBCDTO>> getUserDbcs() {
    log.debug("REST request to get page of DBCs for current user");
    UserProfile userProfile = getProfileFromContext();
    Set<String> allowedBodyCodes = userProfile.getDesignatedBodyCodes();
    List<DBC> dbcList = dBCRepository.findByDbcIn(allowedBodyCodes);
    List<DBCDTO> dbcDtoList = dBCMapper.dBCSToDBCDTOs(dbcList);
    return new ResponseEntity<>(dbcDtoList, HttpStatus.OK);
  }

  /**
   * GET /dbcs/code/:code : get the "code" dBC.
   *
   * @param code the code of the dBCDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the dBCDTO, or with status 404
   * (Not Found)
   */
  @GetMapping("/dbcs/code/{code}")
  @Timed
  public ResponseEntity<DBCDTO> getDBCByCode(@PathVariable String code) {
    log.debug("REST request to get DBC by code: {}", code);
    DBC dBC = dBCRepository.findByDbc(code);
    DBCDTO dBCDTO = dBCMapper.dBCToDBCDTO(dBC);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dBCDTO));
  }

  /**
   * DELETE /dbcs/:id : delete the "id" dBC.
   *
   * @param id the id of the dBCDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/dbcs/{id}")
  @Timed
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteDBC(@PathVariable Long id) {
    log.debug("REST request to delete DBC : {}", id);
    dBCRepository.delete(id);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }

  /**
   * POST /bulk-dbcs : Bulk create a new dbcs.
   *
   * @param dbcdtos List of the dbcdtos to create
   * @return the ResponseEntity with status 200 (Created) and with body the new dbcdtos, or with
   * status 400 (Bad Request) if the DBCDto has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-dbcs")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<DBCDTO>> bulkCreateDBC(@Valid @RequestBody List<DBCDTO> dbcdtos)
      throws URISyntaxException {
    log.debug("REST request to bulk save DBCs : {}", dbcdtos);
    if (!Collections.isEmpty(dbcdtos)) {
      List<Long> entityIds = dbcdtos.stream().filter(dbcdto -> dbcdto.getId() != null)
          .map(dbc -> dbc.getId()).collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest()
            .headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist",
                "A new dbcs cannot already have an ID"))
            .body(null);
      }
    }
    List<DBC> dbcs = dBCMapper.dBCDTOsToDBCS(dbcdtos);
    dbcs = dBCRepository.save(dbcs);
    List<DBCDTO> result = dBCMapper.dBCSToDBCDTOs(dbcs);
    return ResponseEntity.ok().body(result);
  }

  /**
   * PUT /bulk-dbcs : Updates an existing DBC.
   *
   * @param dbcdtos List of the dbcdtos to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated dbcdtos, or with
   * status 400 (Bad Request) if the dbcdtos is not valid, or with status 500 (Internal Server
   * Error) if the dbcdtos couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-dbcs")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<DBCDTO>> bulkUpdateDBC(@Valid @RequestBody List<DBCDTO> dbcdtos)
      throws URISyntaxException {
    log.debug("REST request to bulk update DBCs : {}", dbcdtos);
    if (Collections.isEmpty(dbcdtos)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME,
          "request.body.empty", "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(dbcdtos)) {
      List<DBCDTO> entitiesWithNoId =
          dbcdtos.stream().filter(dbc -> dbc.getId() == null).collect(Collectors.toList());
      return ResponseEntity.badRequest()
          .headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
              "bulk.update.failed.noId", "The request body for this end point cannot be empty"))
          .body(null);
    }
    List<DBC> dbcs = dBCMapper.dBCDTOsToDBCS(dbcdtos);
    dbcs = dBCRepository.save(dbcs);
    List<DBCDTO> results = dBCMapper.dBCSToDBCDTOs(dbcs);
    return ResponseEntity.ok().body(results);
  }
}
