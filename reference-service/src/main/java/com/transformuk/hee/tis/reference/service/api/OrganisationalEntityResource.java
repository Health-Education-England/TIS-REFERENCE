package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.validation.OrganisationalEntityDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.OrganisationalEntity;
import com.transformuk.hee.tis.reference.service.repository.OrganisationalEntityRepository;
import com.transformuk.hee.tis.reference.service.service.impl.OrganisationalEntityServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.OrganisationalEntityMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.nhs.tis.StringConverter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing OrganisationalEntity.
 */
@RestController
@RequestMapping("/api")
public class OrganisationalEntityResource {

    private static final String ENTITY_NAME = "organisationalEntity";
    private final Logger log = LoggerFactory.getLogger(OrganisationalEntityResource.class);
    private final OrganisationalEntityRepository organisationalEntityRepository;
    private final OrganisationalEntityMapper organisationalEntityMapper;
    private final OrganisationalEntityServiceImpl organisationalEntityService;

    public OrganisationalEntityResource(OrganisationalEntityRepository organisationalEntityRepository,
                                        OrganisationalEntityMapper organisationalEntityMapper, OrganisationalEntityServiceImpl organisationalEntityService) {
        this.organisationalEntityRepository = organisationalEntityRepository;
        this.organisationalEntityMapper = organisationalEntityMapper;
        this.organisationalEntityService = organisationalEntityService;
    }

    /**
     * GET /organisational-entities : get all organisational entities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of organisational entities in body
     */
    @ApiOperation(value = "Lists organisational entities",
            notes = "Returns a list of organisational entities with support for pagination, sorting, smart search and column filters \n")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "organisational entities list")})
    @GetMapping("/organisational-entities")
    @Timed
    public ResponseEntity<List<OrganisationalEntityDTO>> getAllOrganisationalEntities(@ApiParam Pageable pageable,
                                                   @ApiParam(value = "any wildcard string to be searched") @RequestParam(value = "searchQuery",
                                                           required = false) String searchQuery,
                                                   @ApiParam(
                                                           value = "json object by column name and value. (Eg: columnFilters={ \"status\": [\"CURRENT\"]}\"") @RequestParam(
                                                           value = "columnFilters", required = false) String columnFilterJson)
            throws IOException {
        log.info("REST request to get a page of organisational entities begin");
        searchQuery = StringConverter.getConverter(searchQuery).fromJson().decodeUrl().escapeForSql()
                .toString();
        List<Class> filterEnumList = Lists.newArrayList(Status.class);
        List<ColumnFilter> columnFilters =
                ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);
        Page<OrganisationalEntity> page;
        if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
            page = organisationalEntityRepository.findAll(pageable);
        } else {
            page = organisationalEntityService.advancedSearch(searchQuery, columnFilters, pageable);
        }
        Page<OrganisationalEntityDTO> results = page.map(organisationalEntityMapper::organisationalEntityToOrganisationalEntityDTO);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organisational-entities");
        return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /organisational-entities/:id : get the "id" organisational entity.
     *
     * @param id the id of the OrganisationalEntityDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the OrganisationalEntityDTO, or with status 404
     * (Not Found)
     */
    @GetMapping("/organisational-entities/{id}")
    @Timed
    public ResponseEntity<OrganisationalEntityDTO> getOrganisationalEntity(@PathVariable Long id) {
        log.debug("REST request to get Organisational Entity : {}", id);
        OrganisationalEntity organisationalEntity = organisationalEntityRepository.findOne(id);
        OrganisationalEntityDTO organisationalEntityDTO = organisationalEntityMapper.organisationalEntityToOrganisationalEntityDTO(organisationalEntity);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(organisationalEntityDTO));
    }
}
