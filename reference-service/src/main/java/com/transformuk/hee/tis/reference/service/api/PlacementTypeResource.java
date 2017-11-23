package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Maps;
import com.transformuk.hee.tis.reference.api.dto.PlacementTypeDTO;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.model.PlacementType;
import com.transformuk.hee.tis.reference.service.repository.PlacementTypeRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.PlacementTypeMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing PlacementType.
 */
@RestController
@RequestMapping("/api")
public class PlacementTypeResource {

  private static final String ENTITY_NAME = "placementType";
  private final Logger log = LoggerFactory.getLogger(PlacementTypeResource.class);
  private final PlacementTypeRepository placementTypeRepository;

  private final PlacementTypeMapper placementTypeMapper;

  public PlacementTypeResource(PlacementTypeRepository placementTypeRepository, PlacementTypeMapper placementTypeMapper) {
    this.placementTypeRepository = placementTypeRepository;
    this.placementTypeMapper = placementTypeMapper;
  }

  /**
   * EXISTS /placement-types/exists/ : check if placement type exists
   *
   * @param codes the Codes of the placeTypeDTO to check
   * @return boolean true if exists otherwise false
   */
  @PostMapping("/placement-types/exists/")
  @Timed
  public ResponseEntity<Map<String, Boolean>> placementTypeExists(@RequestBody List<String> codes) {
    Map<String, Boolean> placementTypeExistsMap = Maps.newHashMap();
    log.debug("REST request to check PlaceType exists : {}", codes);
    if (!CollectionUtils.isEmpty(codes)) {
      List<String> dbPlaceTypeCodes = placementTypeRepository.findCodeByCodesIn(codes);
      codes.forEach(code -> {
        if (dbPlaceTypeCodes.contains(code)) {
          placementTypeExistsMap.put(code, true);
        } else {
          placementTypeExistsMap.put(code, false);
        }
      });
    }
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(placementTypeExistsMap));
  }

  /**
   * POST  /placement-types : Create a new placementType.
   *
   * @param placementTypeDTO the placementTypeDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new placementTypeDTO, or with status 400 (Bad Request) if the placementType has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/placement-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<PlacementTypeDTO> createPlacementType(@Valid @RequestBody PlacementTypeDTO placementTypeDTO) throws URISyntaxException {
    log.debug("REST request to save PlacementType : {}", placementTypeDTO);
    if (placementTypeDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new placementType cannot already have an ID")).body(null);
    }
    PlacementType placementType = placementTypeMapper.placementTypeDTOToPlacementType(placementTypeDTO);
    placementType = placementTypeRepository.save(placementType);
    PlacementTypeDTO result = placementTypeMapper.placementTypeToPlacementTypeDTO(placementType);
    return ResponseEntity.created(new URI("/api/placement-types/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /placement-types : Updates an existing placementType.
   *
   * @param placementTypeDTO the placementTypeDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated placementTypeDTO,
   * or with status 400 (Bad Request) if the placementTypeDTO is not valid,
   * or with status 500 (Internal Server Error) if the placementTypeDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/placement-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<PlacementTypeDTO> updatePlacementType(@Valid @RequestBody PlacementTypeDTO placementTypeDTO) throws URISyntaxException {
    log.debug("REST request to update PlacementType : {}", placementTypeDTO);
    if (placementTypeDTO.getId() == null) {
      return createPlacementType(placementTypeDTO);
    }
    PlacementType placementType = placementTypeMapper.placementTypeDTOToPlacementType(placementTypeDTO);
    placementType = placementTypeRepository.save(placementType);
    PlacementTypeDTO result = placementTypeMapper.placementTypeToPlacementTypeDTO(placementType);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, placementTypeDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /placement-types : get all the placementTypes.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of placementTypes in body
   */
  @GetMapping("/placement-types")
  @Timed
  public List<PlacementTypeDTO> getAllPlacementTypes() {
    log.debug("REST request to get all PlacementTypes");
    List<PlacementType> placementTypes = placementTypeRepository.findAll();
    return placementTypeMapper.placementTypesToPlacementTypeDTOs(placementTypes);
  }

  /**
   * GET  /placement-types/:id : get the "id" placementType.
   *
   * @param id the id of the placementTypeDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the placementTypeDTO, or with status 404 (Not Found)
   */
  @GetMapping("/placement-types/{id}")
  @Timed
  public ResponseEntity<PlacementTypeDTO> getPlacementType(@PathVariable Long id) {
    log.debug("REST request to get PlacementType : {}", id);
    PlacementType placementType = placementTypeRepository.findOne(id);
    PlacementTypeDTO placementTypeDTO = placementTypeMapper.placementTypeToPlacementTypeDTO(placementType);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(placementTypeDTO));
  }

  /**
   * DELETE  /placement-types/:id : delete the "id" placementType.
   *
   * @param id the id of the placementTypeDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/placement-types/{id}")
  @Timed
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deletePlacementType(@PathVariable Long id) {
    log.debug("REST request to delete PlacementType : {}", id);
    placementTypeRepository.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }


  /**
   * POST  /bulk-placement-types : Bulk create a new placement-types.
   *
   * @param placementTypeDTOS List of the placementTypeDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new placementTypeDTOS, or with status 400 (Bad Request) if the PlacementTypeDTO has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-placement-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<PlacementTypeDTO>> bulkCreatePlacementType(@Valid @RequestBody List<PlacementTypeDTO> placementTypeDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save PlacementTypeDtos : {}", placementTypeDTOS);
    if (!Collections.isEmpty(placementTypeDTOS)) {
      List<Long> entityIds = placementTypeDTOS.stream()
          .filter(placementTypeDTO -> placementTypeDTO.getId() != null)
          .map(placementTypeDTO -> placementTypeDTO.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new placementTypes cannot already have an ID")).body(null);
      }
    }
    List<PlacementType> placementTypes = placementTypeMapper.placementTypeDTOsToPlacementTypes(placementTypeDTOS);
    placementTypes = placementTypeRepository.save(placementTypes);
    List<PlacementTypeDTO> result = placementTypeMapper.placementTypesToPlacementTypeDTOs(placementTypes);
    List<Long> ids = result.stream().map(placementTypeDTO -> placementTypeDTO.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(result);
  }

  /**
   * PUT  /bulk-placement-types : Updates an existing placement-types.
   *
   * @param placementTypeDTOS List of the placementTypeDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated placementTypeDTOS,
   * or with status 400 (Bad Request) if the placementTypeDTOS is not valid,
   * or with status 500 (Internal Server Error) if the placementTypeDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-placement-types")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<PlacementTypeDTO>> bulkUpdatePlacementType(@Valid @RequestBody List<PlacementTypeDTO> placementTypeDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update placementTypeDto : {}", placementTypeDTOS);
    if (Collections.isEmpty(placementTypeDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(placementTypeDTOS)) {
      List<PlacementTypeDTO> entitiesWithNoId = placementTypeDTOS.stream().filter(placementTypeDTO -> placementTypeDTO.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<PlacementType> placementTypes = placementTypeMapper.placementTypeDTOsToPlacementTypes(placementTypeDTOS);
    placementTypes = placementTypeRepository.save(placementTypes);
    List<PlacementTypeDTO> results = placementTypeMapper.placementTypesToPlacementTypeDTOs(placementTypes);
    List<Long> ids = results.stream().map(placementTypeDTO -> placementTypeDTO.getId()).collect(Collectors.toList());
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, StringUtils.join(ids, ",")))
        .body(results);
  }
}
