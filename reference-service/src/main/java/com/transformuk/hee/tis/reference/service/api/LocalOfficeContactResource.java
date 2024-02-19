package com.transformuk.hee.tis.reference.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.LocalOfficeContactDto;
import com.transformuk.hee.tis.reference.api.dto.validation.Create;
import com.transformuk.hee.tis.reference.api.dto.validation.Update;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.reference.service.api.util.HeaderUtil;
import com.transformuk.hee.tis.reference.service.api.util.PaginationUtil;
import com.transformuk.hee.tis.reference.service.api.util.UrlDecoderUtil;
import com.transformuk.hee.tis.reference.service.api.validation.LocalOfficeContactValidator;
import com.transformuk.hee.tis.reference.service.model.ColumnFilter;
import com.transformuk.hee.tis.reference.service.model.LocalOfficeContact;
import com.transformuk.hee.tis.reference.service.service.impl.LocalOfficeContactServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.LocalOfficeContactMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
 * REST controller for managing LocalOfficeContact.
 */
@Slf4j
@RestController
@RequestMapping("/api/local-office-contacts")
public class LocalOfficeContactResource {

  private static final String ENTITY_NAME = "localOfficeContact";

  private final LocalOfficeContactServiceImpl service;
  private final LocalOfficeContactMapper mapper;
  private final LocalOfficeContactValidator validator;

  public LocalOfficeContactResource(LocalOfficeContactServiceImpl service,
      LocalOfficeContactMapper mapper, LocalOfficeContactValidator validator) {
    this.service = service;
    this.mapper = mapper;
    this.validator = validator;
  }


  /**
   * POST : Create a new local office contact.
   *
   * @param dto the dto of the local office contact to create.
   * @return the ResponseEntity with status 201 (Created) and with body the new dto, or with status
   * 400 (Bad Request) if the contact already has an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<LocalOfficeContactDto> createLocalOfficeContact(
      @Validated(Create.class) @RequestBody LocalOfficeContactDto dto)
      throws URISyntaxException {
    log.debug("REST request to save LocalOfficeContact : {}", dto);
    validator.validate(dto);

    LocalOfficeContact entity = mapper.toEntity(dto);
    entity = service.save(entity);
    LocalOfficeContactDto result = mapper.toDto(entity);
    return ResponseEntity.created(new URI("/api/local-office-contact/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * PUT : Updates an existing local office contact.
   *
   * @param dto the contact to update.
   * @return the ResponseEntity with status 200 (OK) and with body the updated contact, or with
   * status 400 (Bad Request) if the dto is not valid, or with status 500 (Internal Server Error) if
   * the contact couldn't be updated.
   */
  @PutMapping
  @PreAuthorize("hasAuthority('reference:add:modify:entities')")
  public ResponseEntity<LocalOfficeContactDto> updateLocalOfficeContact(
      @Validated(Update.class) @RequestBody LocalOfficeContactDto dto) {
    log.debug("REST request to update LocalOfficeContact : {}", dto);
    validator.validate(dto);

    LocalOfficeContact entity = mapper.toEntity(dto);
    entity = service.save(entity);
    LocalOfficeContactDto result = mapper.toDto(entity);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dto.getId().toString()))
        .body(result);
  }

  /**
   * GET /:id : get the local office contact by id.
   *
   * @param id the id of the contact to retrieve.
   * @return the ResponseEntity with status 200 (OK) and with body the contact, or with status 404
   * (Not Found).
   */
  @GetMapping("/{id}")
  public ResponseEntity<LocalOfficeContactDto> getLocalOfficeContact(
      @PathVariable UUID id) {
    log.debug("REST request to get LocalOfficeContact : {}", id.toString());
    LocalOfficeContact entity = service.findById(id).orElse(null);
    LocalOfficeContactDto dto = mapper.toDto(entity);
    return ResponseEntity.of(Optional.ofNullable(dto));
  }

  /**
   * GET : get all local office contacts.
   *
   * @param pageable the pagination information.
   * @return the ResponseEntity with status 200 (OK) and the list of contacts in body.
   */
  @GetMapping
  public ResponseEntity<List<LocalOfficeContactDto>> getAllLocalOfficeContacts(
      Pageable pageable,
      @RequestParam(required = false) String searchQuery,
      @RequestParam(value = "columnFilters", required = false) String columnFilterJson)
      throws IOException {
    log.info("REST request to get a page of local office contacts begin");
    searchQuery = StringConverter.getConverter(searchQuery).fromJson().decodeUrl().escapeForSql()
        .toString();
    List<Class> filterEnumList = Lists.newArrayList(Status.class);
    if (columnFilterJson != null) {
      columnFilterJson = UrlDecoderUtil.decode(columnFilterJson);
    }
    List<ColumnFilter> columnFilters = ColumnFilterUtil
        .getColumnFilters(columnFilterJson, filterEnumList);
    Page<LocalOfficeContact> page;
    if (StringUtils.isEmpty(searchQuery) && StringUtils.isEmpty(columnFilterJson)) {
      page = service.findAll(pageable);
    } else {
      page = service.advancedSearch(searchQuery, columnFilters, pageable);
    }
    Page<LocalOfficeContactDto> results = page.map(mapper::toDto);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
        "/api/local-office-contact");
    return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
  }
}
