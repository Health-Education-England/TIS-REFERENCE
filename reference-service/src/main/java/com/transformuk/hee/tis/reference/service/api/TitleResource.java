package com.transformuk.hee.tis.reference.service.api;

import com.codahale.metrics.annotation.Timed;
import com.transformuk.hee.tis.reference.api.dto.TitleDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.model.Title;
import com.transformuk.hee.tis.reference.service.repository.TitleRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.TitleMapper;
import io.github.jhipster.web.util.ResponseUtil;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
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
 * REST controller for managing Title.
 */
@RestController
@RequestMapping("/api")
public class TitleResource {

  private static final String ENTITY_NAME = "title";
  private final Logger log = LoggerFactory.getLogger(TitleResource.class);
  private final TitleRepository titleRepository;

  private final TitleMapper titleMapper;

  public TitleResource(TitleRepository titleRepository, TitleMapper titleMapper) {
    this.titleRepository = titleRepository;
    this.titleMapper = titleMapper;
  }

  /**
   * POST  /titles : Create a new title.
   *
   * @param titleDTO the titleDTO to create
   * @return the ResponseEntity with status 201 (Created) and with body the new titleDTO, or with status 400 (Bad Request) if the title has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/titles")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<TitleDTO> createTitle(@Valid @RequestBody TitleDTO titleDTO) throws URISyntaxException {
    log.debug("REST request to save Title : {}", titleDTO);
    if (titleDTO.getId() != null) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new title cannot already have an ID")).body(null);
    }
    Title title = titleMapper.titleDTOToTitle(titleDTO);
    title = titleRepository.save(title);
    TitleDTO result = titleMapper.titleToTitleDTO(title);
    return ResponseEntity.created(new URI("/api/titles/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT  /titles : Updates an existing title.
   *
   * @param titleDTO the titleDTO to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated titleDTO,
   * or with status 400 (Bad Request) if the titleDTO is not valid,
   * or with status 500 (Internal Server Error) if the titleDTO couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/titles")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<TitleDTO> updateTitle(@Valid @RequestBody TitleDTO titleDTO) throws URISyntaxException {
    log.debug("REST request to update Title : {}", titleDTO);
    if (titleDTO.getId() == null) {
      return createTitle(titleDTO);
    }
    Title title = titleMapper.titleDTOToTitle(titleDTO);
    title = titleRepository.save(title);
    TitleDTO result = titleMapper.titleToTitleDTO(title);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, titleDTO.getId().toString()))
        .body(result);
  }

  /**
   * GET  /titles : get all the titles.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of titles in body
   */
  @GetMapping("/titles")
  @Timed
  public List<TitleDTO> getAllTitles() {
    log.debug("REST request to get all Titles");
    List<Title> titles = titleRepository.findAll();
    return titleMapper.titlesToTitleDTOs(titles);
  }

  /**
   * GET  /current/titles : get all the current titles.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of titles in body
   */
  @GetMapping("/current/titles")
  @Timed
  public List<TitleDTO> getAllCurrentTitles() {
    log.debug("REST request to get all current Titles");
    Title title = new Title().status(Status.CURRENT);
    List<Title> titles = titleRepository.findAll(Example.of(title));
    return titleMapper.titlesToTitleDTOs(titles);
  }

  /**
   * GET  /titles/:id : get the "id" title.
   *
   * @param id the id of the titleDTO to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body the titleDTO, or with status 404 (Not Found)
   */
  @GetMapping("/titles/{id}")
  @Timed
  public ResponseEntity<TitleDTO> getTitle(@PathVariable Long id) {
    log.debug("REST request to get Title : {}", id);
    Title title = titleRepository.findOne(id);
    TitleDTO titleDTO = titleMapper.titleToTitleDTO(title);
    return ResponseUtil.wrapOrNotFound(Optional.ofNullable(titleDTO));
  }

  /**
   * DELETE  /titles/:id : delete the "id" title.
   *
   * @param id the id of the titleDTO to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/titles/{id}")
  @Timed
  @PreAuthorize("hasAuthority('reference:delete:entities')")
  public ResponseEntity<Void> deleteTitle(@PathVariable Long id) {
    log.debug("REST request to delete Title : {}", id);
    titleRepository.delete(id);
    return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }


  /**
   * POST  /bulk-titles : Bulk create a new titles.
   *
   * @param titleDTOS List of the titleDTOS to create
   * @return the ResponseEntity with status 200 (Created) and with body the new titleDTOS, or with status 400 (Bad Request) if the TitleDto has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PostMapping("/bulk-titles")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<TitleDTO>> bulkCreateTitle(@Valid @RequestBody List<TitleDTO> titleDTOS) throws URISyntaxException {
    log.debug("REST request to bulk save TitleDtos : {}", titleDTOS);
    if (!Collections.isEmpty(titleDTOS)) {
      List<Long> entityIds = titleDTOS.stream()
          .filter(titleDTO -> titleDTO.getId() != null)
          .map(titleDTO -> titleDTO.getId())
          .collect(Collectors.toList());
      if (!Collections.isEmpty(entityIds)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entityIds, ","), "ids.exist", "A new titles cannot already have an ID")).body(null);
      }
    }
    List<Title> titles = titleMapper.titleDTOsToTitles(titleDTOS);
    titles = titleRepository.save(titles);
    List<TitleDTO> result = titleMapper.titlesToTitleDTOs(titles);
    return ResponseEntity.ok()
        .body(result);
  }

  /**
   * PUT  /bulk-titles : Updates an existing titles.
   *
   * @param titleDTOS List of the titleDTOS to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated titleDTOS,
   * or with status 400 (Bad Request) if the titleDTOS is not valid,
   * or with status 500 (Internal Server Error) if the titleDTOS couldnt be updated
   * @throws URISyntaxException if the Location URI syntax is incorrect
   */
  @PutMapping("/bulk-titles")
  @Timed
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<List<TitleDTO>> bulkUpdateTitle(@Valid @RequestBody List<TitleDTO> titleDTOS) throws URISyntaxException {
    log.debug("REST request to bulk update TitleDtos : {}", titleDTOS);
    if (Collections.isEmpty(titleDTOS)) {
      return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "request.body.empty",
          "The request body for this end point cannot be empty")).body(null);
    } else if (!Collections.isEmpty(titleDTOS)) {
      List<TitleDTO> entitiesWithNoId = titleDTOS.stream().filter(titleDTO -> titleDTO.getId() == null).collect(Collectors.toList());
      if (!Collections.isEmpty(entitiesWithNoId)) {
        return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(StringUtils.join(entitiesWithNoId, ","),
            "bulk.update.failed.noId", "Some DTOs you've provided have no Id, cannot update entities that dont exist")).body(entitiesWithNoId);
      }
    }
    List<Title> titles = titleMapper.titleDTOsToTitles(titleDTOS);
    titles = titleRepository.save(titles);
    List<TitleDTO> results = titleMapper.titlesToTitleDTOs(titles);
    return ResponseEntity.ok()
        .body(results);
  }
}
