package com.transformuk.hee.tis.reference.service.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.reference.api.dto.TraineeTypeDto;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.TraineeType;
import com.transformuk.hee.tis.reference.service.repository.TraineeTypeRepository;
import com.transformuk.hee.tis.reference.service.service.impl.TraineeTypeServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.TraineeTypeMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the TraineeTypeResource REST controller.
 *
 * @see TraineeTypeResource
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class TraineeTypeResourceIntTest {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "Te$t Code";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t Label";

  @Autowired
  private TraineeTypeRepository repository;

  @Autowired
  private TraineeTypeMapper mapper;

  @Autowired
  private TraineeTypeServiceImpl service;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  private MockMvc mockMvc;

  private TraineeType entity;

  /**
   * Create an entity for this test.
   *
   * @return The created, unsaved, TraineeType.
   */
  static TraineeType createEntity() {
    TraineeType entity = new TraineeType();
    entity.setCode(DEFAULT_CODE);
    entity.setLabel(DEFAULT_LABEL);
    entity.setStatus(Status.CURRENT);
    return entity;
  }

  @BeforeEach
  void setup() {
    TraineeTypeResource controller = new TraineeTypeResource(service, mapper);
    mockMvc = MockMvcBuilders.standaloneSetup(controller)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();

    entity = createEntity();
  }

  @Test
  @Transactional
  void createTraineeType() throws Exception {
    int databaseSizeBeforeCreate = repository.findAll().size();

    TraineeTypeDto dto = mapper.toDto(entity);
    mockMvc.perform(post("/api/trainee-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
        .andExpect(jsonPath("$.status").value(Status.CURRENT.toString()));

    List<TraineeType> entityList = repository.findAll();
    assertThat(entityList).hasSize(databaseSizeBeforeCreate + 1);
    TraineeType testTraineeType = entityList.stream()
        .filter(traineeType -> traineeType.getCode().equals(DEFAULT_CODE))
        .findFirst().orElseThrow();
    assertThat(testTraineeType.getLabel()).isEqualTo(DEFAULT_LABEL);
    assertThat(testTraineeType.getStatus()).isEqualTo(Status.CURRENT);
  }

  @Test
  @Transactional
  void createTraineeTypeWithDuplicateCode() throws Exception {
    repository.saveAndFlush(createEntity());

    TraineeTypeDto dto = mapper.toDto(entity);
    mockMvc.perform(post("/api/trainee-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isCreated());

    // The above insert will not fail until flushed.
    assertThrows(DataIntegrityViolationException.class, () -> repository.flush());
  }

  @Test
  @Transactional
  void createTraineeTypeWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = repository.findAll().size();

    entity.setId(UUID.randomUUID());
    TraineeTypeDto dto = mapper.toDto(entity);

    mockMvc.perform(post("/api/trainee-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isBadRequest());

    assertThat(repository.findAll()).hasSize(databaseSizeBeforeCreate);
  }

  @ParameterizedTest
  @ValueSource(strings = {"code", "label"})
  @Transactional
  void shouldNotCreateTraineeTypeWhenRequiredFieldMissing(String field) throws Exception {
    int databaseSizeBeforeTest = repository.findAll().size();

    if (field.equals("code")) {
      entity.setCode(null);
    } else {
      entity.setLabel(null);
    }
    TraineeTypeDto dto = mapper.toDto(entity);

    mockMvc.perform(post("/api/trainee-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isBadRequest());

    assertThat(repository.findAll()).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  void getAllTraineeTypes() throws Exception {
    repository.saveAndFlush(entity);

    mockMvc.perform(get("/api/trainee-types?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(entity.getId().toString())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));
  }

  @Test
  @Transactional
  void getAllTraineeTypesIncludesMigratedTypes() throws Exception {
    mockMvc.perform(get("/api/trainee-types"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].code").value(hasItem("SPECIALTY")))
        .andExpect(jsonPath("$.[*].code").value(hasItem("FOUNDATION")));
  }

  @Test
  @Transactional
  void getTraineeTypesWithColumnFilter() throws Exception {
    repository.saveAndFlush(entity);

    TraineeType inactiveEntity = new TraineeType();
    String inactiveCode = DEFAULT_CODE + "_INACTIVE";
    inactiveEntity.setCode(inactiveCode);
    String inactiveLabel = DEFAULT_LABEL + "(inactive)";
    inactiveEntity.setLabel(inactiveLabel);
    inactiveEntity.setStatus(Status.INACTIVE);
    repository.saveAndFlush(inactiveEntity);

    mockMvc.perform(get("/api/trainee-types?columnFilters=%7B\"status\":[\"INACTIVE\"]%7D"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(inactiveEntity.getId().toString())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(inactiveCode)))
        .andExpect(jsonPath("$.[*].label").value(hasItem(inactiveLabel)))
        .andExpect(jsonPath("$.[*].status").value(not(hasItem(Status.CURRENT.toString()))));
  }

  @Test
  @Transactional
  void getTraineeTypesWithQueryMatchingCode() throws Exception {
    entity.setCode(UNENCODED_CODE);
    repository.saveAndFlush(entity);

    mockMvc.perform(get("/api/trainee-types?searchQuery=\"Te%24t\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(entity.getId().toString()))
        .andExpect(jsonPath("$.[*].code").value(UNENCODED_CODE))
        .andExpect(jsonPath("$.[*].label").value(DEFAULT_LABEL));
  }

  @Test
  @Transactional
  void getTraineeTypesWithQueryMatchingLabel() throws Exception {
    entity.setLabel(UNENCODED_LABEL);
    repository.saveAndFlush(entity);

    mockMvc.perform(get("/api/trainee-types?searchQuery=\"Te%24t\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(entity.getId().toString()))
        .andExpect(jsonPath("$.[*].code").value(DEFAULT_CODE))
        .andExpect(jsonPath("$.[*].label").value(UNENCODED_LABEL));
  }

  @Test
  @Transactional
  void getTraineeType() throws Exception {
    repository.saveAndFlush(entity);

    mockMvc.perform(get("/api/trainee-types/{id}", entity.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(entity.getId().toString()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
        .andExpect(jsonPath("$.status").value(Status.CURRENT.toString()));
  }

  @Test
  @Transactional
  void getNonExistingTraineeType() throws Exception {
    mockMvc.perform(get("/api/trainee-types/{id}", UUID.randomUUID()))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void updateTraineeType() throws Exception {
    repository.saveAndFlush(entity);
    int databaseSizeBeforeUpdate = repository.findAll().size();

    TraineeTypeDto dto = mapper.toDto(entity);
    dto.setCode(UPDATED_CODE);
    dto.setLabel(UPDATED_LABEL);
    dto.setStatus(Status.INACTIVE);

    mockMvc.perform(put("/api/trainee-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(entity.getId().toString()))
        .andExpect(jsonPath("$.code").value(UPDATED_CODE))
        .andExpect(jsonPath("$.label").value(UPDATED_LABEL))
        .andExpect(jsonPath("$.status").value(Status.INACTIVE.toString()));

    assertThat(repository.findAll()).hasSize(databaseSizeBeforeUpdate);
    TraineeType testTraineeType = repository.findById(entity.getId()).orElseThrow();
    assertThat(testTraineeType.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testTraineeType.getLabel()).isEqualTo(UPDATED_LABEL);
    assertThat(testTraineeType.getStatus()).isEqualTo(Status.INACTIVE);
  }

  @Test
  @Transactional
  void updateNonExistingTraineeType() throws Exception {
    int databaseSizeBeforeUpdate = repository.findAll().size();

    // If the entity doesn't have an ID, it will cause a bad request.
    TraineeTypeDto dto = mapper.toDto(entity);

    mockMvc.perform(put("/api/trainee-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isBadRequest());

    assertThat(repository.findAll()).hasSize(databaseSizeBeforeUpdate);
  }
}
