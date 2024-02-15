package com.transformuk.hee.tis.reference.service.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.reference.api.dto.LocalOfficeContactTypeDto;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.LocalOfficeContactType;
import com.transformuk.hee.tis.reference.service.repository.LocalOfficeContactTypeRepository;
import com.transformuk.hee.tis.reference.service.service.impl.LocalOfficeContactTypeServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.LocalOfficeContactTypeMapper;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the LocalOfficeContactTypeResource REST controller.
 *
 * @see LocalOfficeContactTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class LocalOfficeContactTypeResourceIntTest {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "Te$t Code";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t Label";

  @Autowired
  private LocalOfficeContactTypeRepository repository;

  @Autowired
  private LocalOfficeContactTypeMapper mapper;

  @Autowired
  private LocalOfficeContactTypeServiceImpl service;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc mockMvc;

  private LocalOfficeContactType entity;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static LocalOfficeContactType createEntity(EntityManager em) {
    LocalOfficeContactType entity = new LocalOfficeContactType();
    entity.setCode(DEFAULT_CODE);
    entity.setLabel(DEFAULT_LABEL);
    entity.setStatus(Status.CURRENT);
    return entity;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    LocalOfficeContactTypeResource controller = new LocalOfficeContactTypeResource(service, mapper);
    this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    entity = createEntity(em);
  }

  @Test
  @Transactional
  public void createLocalOfficeContactType() throws Exception {
    int databaseSizeBeforeCreate = repository.findAll().size();

    // Create the LocalOfficeContactType.
    LocalOfficeContactTypeDto dto = mapper.toDto(entity);
    mockMvc.perform(post("/api/local-office-contact-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isCreated());

    // Validate the LocalOfficeContactType in the database.
    List<LocalOfficeContactType> entityList = repository.findAll();
    assertThat(entityList).hasSize(databaseSizeBeforeCreate + 1);
    LocalOfficeContactType testLocalOfficeContactType = entityList.get(entityList.size() - 1);
    assertThat(testLocalOfficeContactType.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testLocalOfficeContactType.getLabel()).isEqualTo(DEFAULT_LABEL);
  }

  @Test(expected = DataIntegrityViolationException.class)
  @Transactional
  public void createLocalOfficeContactTypeWithDuplicateCode() throws Exception {
    // Create the LocalOfficeContactType.
    LocalOfficeContactTypeDto dto = mapper.toDto(entity);
    mockMvc.perform(post("/api/local-office-contact-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isCreated());

    mockMvc.perform(post("/api/local-office-contact-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isCreated());

    // The above insert will not fail until flushed.
    repository.flush();
  }

  @Test
  @Transactional
  public void createLocalOfficeContactTypeWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = repository.findAll().size();

    // Create the LocalOfficeContactType with an existing ID.
    entity.setId(UUID.randomUUID());
    LocalOfficeContactTypeDto dto = mapper.toDto(entity);

    // An entity with an existing ID cannot be created, so this API call must fail.
    mockMvc.perform(post("/api/local-office-contact-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database.
    List<LocalOfficeContactType> entityList = repository.findAll();
    assertThat(entityList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = repository.findAll().size();
    // Set the field null.
    entity.setCode(null);

    // Create the LocalOfficeContactType, which fails.
    LocalOfficeContactTypeDto dto = mapper.toDto(entity);

    mockMvc.perform(post("/api/local-office-contact-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isBadRequest());

    List<LocalOfficeContactType> entityList = repository.findAll();
    assertThat(entityList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLabelIsRequired() throws Exception {
    int databaseSizeBeforeTest = repository.findAll().size();
    // Set the field null.
    entity.setLabel(null);

    // Create the LocalOfficeContactType, which fails.
    LocalOfficeContactTypeDto dto = mapper.toDto(entity);

    mockMvc.perform(post("/api/local-office-contact-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isBadRequest());

    List<LocalOfficeContactType> entityList = repository.findAll();
    assertThat(entityList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllLocalOfficeContactTypes() throws Exception {
    // Initialize the database.
    repository.saveAndFlush(entity);

    // Get all the entityList.
    mockMvc.perform(get("/api/local-office-contact-types?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(entity.getId().toString())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));
  }

  @Test
  @Transactional
  public void getLocalOfficeContactTypesWithColumnFilter() throws Exception {
    // Initialize the database.
    repository.saveAndFlush(entity);

    LocalOfficeContactType inactiveEntity = new LocalOfficeContactType();
    String inactiveCode = DEFAULT_CODE + "_INACTIVE";
    inactiveEntity.setCode(inactiveCode);
    String inactiveLabel = DEFAULT_LABEL + "(inactive)";
    inactiveEntity.setLabel(inactiveLabel);
    inactiveEntity.setStatus(Status.INACTIVE);
    repository.saveAndFlush(inactiveEntity);

    // Get all the matching entities.
    mockMvc.perform(
            get("/api/local-office-contact-types?columnFilters=%7B\"status\":[\"INACTIVE\"]%7D"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(inactiveEntity.getId().toString())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(inactiveCode)))
        .andExpect(jsonPath("$.[*].label").value(hasItem(inactiveLabel)))
        .andExpect(jsonPath("$.[*].status").value(hasItem(Status.INACTIVE.toString())))
        .andExpect(jsonPath("$.[*].status").value(not(hasItem(Status.CURRENT.toString()))));
  }

  @Test
  @Transactional
  public void getLocalOfficeContactTypesWithQueryMatchingCode() throws Exception {
    // Initialize the database.
    LocalOfficeContactType entity = new LocalOfficeContactType();
    entity.setCode(UNENCODED_CODE);
    entity.setLabel(DEFAULT_LABEL);
    repository.saveAndFlush(entity);

    // Get all the matching entities.
    mockMvc.perform(get("/api/local-office-contact-types?searchQuery=\"Te%24t\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(entity.getId().toString()))
        .andExpect(jsonPath("$.[*].code").value(UNENCODED_CODE))
        .andExpect(jsonPath("$.[*].label").value(DEFAULT_LABEL));
  }

  @Test
  @Transactional
  public void getLocalOfficeContactTypesWithQueryMatchingLabel() throws Exception {
    // Initialize the database.
    LocalOfficeContactType entity = new LocalOfficeContactType();
    entity.setCode(DEFAULT_CODE);
    entity.setLabel(UNENCODED_LABEL);
    repository.saveAndFlush(entity);

    // Get all the matching entities.
    mockMvc.perform(get("/api/local-office-contact-types?searchQuery=\"Te%24t\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(entity.getId().toString()))
        .andExpect(jsonPath("$.[*].code").value(DEFAULT_CODE))
        .andExpect(jsonPath("$.[*].label").value(UNENCODED_LABEL));
  }

  @Test
  @Transactional
  public void getLocalOfficeContactType() throws Exception {
    // Initialize the database.
    repository.saveAndFlush(entity);

    // Get the contact type.
    mockMvc.perform(get("/api/local-office-contact-types/{id}", entity.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(entity.getId().toString()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));
  }

  @Test
  @Transactional
  public void getNonExistingLocalOfficeContactType() throws Exception {
    // Get the contact type.
    mockMvc.perform(get("/api/local-office-contact-types/{id}", UUID.randomUUID()))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateLocalOfficeContactType() throws Exception {
    // Initialize the database.
    repository.saveAndFlush(entity);
    int databaseSizeBeforeUpdate = repository.findAll().size();

    // Update the contact type.
    LocalOfficeContactType updatedEntity = repository.findById(entity.getId()).get();
    updatedEntity.setCode(UPDATED_CODE);
    updatedEntity.setLabel(UPDATED_LABEL);
    LocalOfficeContactTypeDto dto = mapper.toDto(updatedEntity);

    mockMvc.perform(put("/api/local-office-contact-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isOk());

    // Validate the LocalOfficeContactType in the database.
    List<LocalOfficeContactType> entityList = repository.findAll();
    assertThat(entityList).hasSize(databaseSizeBeforeUpdate);
    LocalOfficeContactType testContactType = entityList.get(entityList.size() - 1);
    assertThat(testContactType.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testContactType.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  @Transactional
  public void updateNonExistingLocalOfficeContactType() throws Exception {
    int databaseSizeBeforeUpdate = repository.findAll().size();

    // Create the LocalOfficeContactType.
    LocalOfficeContactTypeDto dto = mapper.toDto(entity);

    // If the entity doesn't have an ID, it will cause a bad request.
    mockMvc.perform(put("/api/local-office-contact-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isBadRequest());

    // Validate the LocalOfficeContactType in the database.
    List<LocalOfficeContactType> entityList = repository.findAll();
    assertThat(entityList).hasSize(databaseSizeBeforeUpdate);
  }
}
