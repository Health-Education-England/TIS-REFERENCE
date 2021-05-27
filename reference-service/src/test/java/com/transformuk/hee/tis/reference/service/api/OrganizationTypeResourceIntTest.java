package com.transformuk.hee.tis.reference.service.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.reference.api.dto.OrganizationTypeDto;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.OrganizationType;
import com.transformuk.hee.tis.reference.service.repository.OrganizationTypeRepository;
import com.transformuk.hee.tis.reference.service.service.impl.OrganizationTypeServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.OrganizationTypeMapper;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the OrganizationTypeResource REST controller.
 *
 * @see OrganizationTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class OrganizationTypeResourceIntTest {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "CCCCCCCCCC";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t & organization type";

  @Autowired
  private OrganizationTypeMapper mapper;

  @Autowired
  private OrganizationTypeRepository repository;

  @Autowired
  private OrganizationTypeServiceImpl service;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  private MockMvc mockMvc;

  private OrganizationType entity;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static OrganizationType createEntity() {
    OrganizationType entity = new OrganizationType();
    entity.setCode(DEFAULT_CODE);
    entity.setLabel(DEFAULT_LABEL);
    return entity;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    OrganizationTypeResource testObj = new OrganizationTypeResource(mapper, service);
    this.mockMvc = MockMvcBuilders.standaloneSetup(testObj)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    entity = createEntity();
  }

  @Test
  @Transactional
  public void createOrganizationType() throws Exception {
    int databaseSizeBeforeCreate = repository.findAll().size();

    // Create the OrganizationType.
    OrganizationTypeDto dto = mapper.toDto(entity);
    mockMvc.perform(post("/api/organization-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isCreated());

    // Validate the OrganizationType in the database.
    List<OrganizationType> foundEntities = repository.findAll();
    assertThat(foundEntities).hasSize(databaseSizeBeforeCreate + 1);
    OrganizationType foundEntity = foundEntities.get(foundEntities.size() - 1);
    assertThat(foundEntity.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(foundEntity.getLabel()).isEqualTo(DEFAULT_LABEL);
  }

  @Test
  @Transactional
  public void createOrganizationTypeWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = repository.findAll().size();

    // Create the OrganizationType with an existing ID.
    entity.setId(1L);
    OrganizationTypeDto dto = mapper.toDto(entity);

    // An entity with an existing ID cannot be created, so this API call must fail.
    mockMvc.perform(post("/api/organization-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<OrganizationType> foundEntities = repository.findAll();
    assertThat(foundEntities).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkAbbreviationIsRequired() throws Exception {
    int databaseSizeBeforeTest = repository.findAll().size();
    // Set the field null.
    entity.setCode(null);

    // Create the OrganizationType, which fails.
    OrganizationTypeDto dto = mapper.toDto(entity);

    mockMvc.perform(post("/api/organization-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isBadRequest());

    List<OrganizationType> foundEntities = repository.findAll();
    assertThat(foundEntities).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllOrganizationTypes() throws Exception {
    // Initialize the database.
    repository.saveAndFlush(entity);

    // Get all the entities.
    mockMvc.perform(get("/api/organization-types?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(entity.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));
  }

  @Test
  @Transactional
  public void getOrganizationTypesWithEscapedCharacterSearchShouldFindShoudReturnOrganizationType()
      throws Exception {
    // Initialize the database.
    OrganizationType unescapedEntity = new OrganizationType();
    unescapedEntity.setCode(UNENCODED_CODE);
    unescapedEntity.setLabel(UNENCODED_LABEL);
    unescapedEntity = repository.saveAndFlush(unescapedEntity);

    // Get all the entities.
    mockMvc.perform(get("/api/organization-types?searchQuery=\"Te%24t%20%26%20\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(unescapedEntity.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(UNENCODED_CODE)))
        .andExpect(jsonPath("$.[*].label").value(hasItem(UNENCODED_LABEL)));
  }

  @Test
  @Transactional
  public void getOrganizationType() throws Exception {
    // Initialize the database.
    repository.saveAndFlush(entity);

    // Get the entity.
    mockMvc.perform(get("/api/organization-types/{id}", entity.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(entity.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));
  }

  @Test
  @Transactional
  public void getNonExistingOrganizationType() throws Exception {
    // Get the entity.
    mockMvc.perform(get("/api/organization-types/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateOrganizationType() throws Exception {
    // Initialize the database.
    repository.saveAndFlush(entity);
    int databaseSizeBeforeUpdate = repository.findAll().size();

    // Update the entity.
    OrganizationType updatedEntity = service.findById(entity.getId()).get();
    updatedEntity.setCode(UPDATED_CODE);
    updatedEntity.setLabel(UPDATED_LABEL);
    OrganizationTypeDto dto = mapper.toDto(updatedEntity);

    mockMvc.perform(put("/api/organization-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isOk());

    // Validate the OrganizationType in the database
    List<OrganizationType> foundEntities = repository.findAll();
    assertThat(foundEntities).hasSize(databaseSizeBeforeUpdate);
    OrganizationType foundEntity = foundEntities.get(foundEntities.size() - 1);
    assertThat(foundEntity.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(foundEntity.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  @Transactional
  public void updateNonExistingOrganizationType() throws Exception {
    int databaseSizeBeforeUpdate = repository.findAll().size();

    // Create the OrganizationType.
    OrganizationTypeDto dto = mapper.toDto(entity);

    // If the entity doesn't have an ID, it will be created instead of just being updated.
    mockMvc.perform(put("/api/organization-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isCreated());

    // Validate the OrganizationType in the database.
    List<OrganizationType> foundEntities = repository.findAll();
    assertThat(foundEntities).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteOrganizationType() throws Exception {
    // Initialize the database.
    repository.saveAndFlush(entity);
    int databaseSizeBeforeDelete = repository.findAll().size();

    // Get the entity.
    mockMvc.perform(delete("/api/organization-types/{id}", entity.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database is empty.
    List<OrganizationType> foundEntities = repository.findAll();
    assertThat(foundEntities).hasSize(databaseSizeBeforeDelete - 1);
  }
}
