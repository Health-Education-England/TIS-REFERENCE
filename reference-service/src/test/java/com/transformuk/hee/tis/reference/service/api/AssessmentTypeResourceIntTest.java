package com.transformuk.hee.tis.reference.service.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.AssessmentType;
import com.transformuk.hee.tis.reference.service.repository.AssessmentTypeRepository;
import com.transformuk.hee.tis.reference.service.service.impl.AssessmentTypeServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.AssessmentTypeMapper;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the AssessmentType REST controller.
 *
 * @see AssessmentTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AssessmentTypeResourceIntTest {

  private static final String LOCATION_HEADER = "Location";
  private static final Long DEFAULT_ID = 1L;
  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "Te$t Code";
  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t Label";

  @Autowired
  private AssessmentTypeRepository repository;

  @Autowired
  private AssessmentTypeServiceImpl service;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  private MockMvc mockMvc;
  private AssessmentType assessmentType;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static AssessmentType createEntity() {
    AssessmentType assessmentType = new AssessmentType();
    assessmentType.setId(DEFAULT_ID);
    assessmentType.setCode(DEFAULT_CODE);
    assessmentType.setLabel(DEFAULT_LABEL);
    return assessmentType;
  }

  @Before
  public void setup() {
    AssessmentTypeMapper mapper = Mappers.getMapper(AssessmentTypeMapper.class);
    AssessmentTypeResource testObj = new AssessmentTypeResource(repository,
        service, mapper);
    this.mockMvc = MockMvcBuilders.standaloneSetup(testObj)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    assessmentType = createEntity();
  }

  @Test
  @Transactional
  public void createAssessmentTypeWithNoIdShouldReturnThePersistedAssessmentType()
      throws Exception {
    assessmentType.setId(null);

    mockMvc.perform(post("/api/assessment-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(assessmentType)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.code").value(equalTo(DEFAULT_CODE)))
        .andExpect(jsonPath("$.label").value(equalTo(DEFAULT_LABEL)))
        .andExpect(header().string(LOCATION_HEADER, "/api/assessment-types/" + DEFAULT_CODE));

    AssessmentType createdEntity = repository.findOneByCode(DEFAULT_CODE);
    assertThat("Unexpected entity.", createdEntity, notNullValue());
    assertThat("Unexpected code.", createdEntity.getCode(), is(DEFAULT_CODE));
    assertThat("Unexpected label.", createdEntity.getLabel(), is(DEFAULT_LABEL));
  }

  @Test
  @Transactional
  public void createAssessmentTypeWithIdShouldReturnBadRequest() throws Exception {
    mockMvc.perform(post("/api/assessment-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(assessmentType)))
        .andExpect(status().isBadRequest());

    AssessmentType createdEntity = repository.findOneByCode(DEFAULT_CODE);
    assertThat("Unexpected entity.", createdEntity, nullValue());
  }

  @Test
  @Transactional
  public void bulkCreateAssessmentTypesWithNoIdsShouldReturnThePersistedAssessmentTypes()
      throws Exception {
    assessmentType.setId(null);
    AssessmentType assessmentType2 = new AssessmentType();
    assessmentType2.setId(null);
    assessmentType2.setCode("ABC");
    assessmentType2.setLabel("ABC_123");
    List<AssessmentType> assessmentTypes = Arrays.asList(assessmentType, assessmentType2);

    mockMvc.perform(post("/api/bulk-assessment-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(assessmentTypes)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*].id").exists())
        .andExpect(jsonPath("$.[*].code").value(hasItems(DEFAULT_CODE, "ABC")))
        .andExpect(jsonPath("$.[*].label").value(hasItems(DEFAULT_LABEL, "ABC_123")));

    AssessmentType createdEntity = repository.findOneByCode(DEFAULT_CODE);
    assertThat("Unexpected entity.", createdEntity, notNullValue());
    assertThat("Unexpected code.", createdEntity.getCode(), is(DEFAULT_CODE));
    assertThat("Unexpected label.", createdEntity.getLabel(), is(DEFAULT_LABEL));

    createdEntity = repository.findOneByCode("ABC");
    assertThat("Unexpected entity.", createdEntity, notNullValue());
    assertThat("Unexpected code.", createdEntity.getCode(), is("ABC"));
    assertThat("Unexpected label.", createdEntity.getLabel(), is("ABC_123"));
  }

  @Test
  @Transactional
  public void bulkCreateAssessmentTypesWithIdShouldReturnBadRequest() throws Exception {
    List<AssessmentType> assessmentTypes = Arrays.asList(assessmentType, createEntity());

    mockMvc.perform(post("/api/bulk-assessment-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(assessmentTypes)))
        .andExpect(status().isBadRequest());

    AssessmentType createdEntity = repository.findOneByCode(DEFAULT_CODE);
    assertThat("Unexpected entity.", createdEntity, nullValue());
  }

  @Test
  @Transactional
  public void updateAssessmentTypeShouldReturnUpdateAssessmentType() throws Exception {
    assessmentType = repository.saveAndFlush(assessmentType);

    assessmentType.setCode(UPDATED_CODE);
    assessmentType.setLabel(UPDATED_LABEL);

    mockMvc.perform(MockMvcRequestBuilders.put("/api/assessment-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(assessmentType)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(DEFAULT_ID))
        .andExpect(jsonPath("$.code").value(equalTo(UPDATED_CODE)))
        .andExpect(jsonPath("$.label").value(equalTo(UPDATED_LABEL)));

    AssessmentType createdEntity = repository.getOne(assessmentType.getId());
    assertThat("Unexpected entity.", createdEntity, notNullValue());
    assertThat("Unexpected code.", createdEntity.getCode(), is(UPDATED_CODE));
    assertThat("Unexpected label.", createdEntity.getLabel(), is(UPDATED_LABEL));
  }

  @Test
  @Transactional
  public void updateAssessmentTypeShouldReturnCreateAssessmentTypeWhenItDoesntExist()
      throws Exception {
    assessmentType.setId(null);

    mockMvc.perform(MockMvcRequestBuilders.put("/api/assessment-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(assessmentType)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.code").value(equalTo(DEFAULT_CODE)))
        .andExpect(jsonPath("$.label").value(equalTo(DEFAULT_LABEL)));

    AssessmentType createdEntity = repository.findOneByCode(DEFAULT_CODE);
    assertThat("Unexpected entity.", createdEntity, notNullValue());
    assertThat("Unexpected code.", createdEntity.getCode(), is(DEFAULT_CODE));
    assertThat("Unexpected label.", createdEntity.getLabel(), is(DEFAULT_LABEL));
  }

  @Test
  @Transactional
  public void updateAssessmentTypeWithNoCodeShouldReturnBadRequest() throws Exception {
    assessmentType.setCode(null);

    mockMvc.perform(MockMvcRequestBuilders.put("/api/assessment-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(assessmentType)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  public void getAllAssessmentTypes() throws Exception {
    AssessmentType assessmentType1 = createEntity();
    AssessmentType assessmentType2 = new AssessmentType();
    assessmentType2.setId(2L);
    assessmentType2.setCode("ABC");
    assessmentType2.setLabel("ABC_123");

    List<AssessmentType> allAssessmentTypes = Lists.newArrayList(assessmentType1, assessmentType2);
    repository.saveAll(allAssessmentTypes);
    repository.flush();

    mockMvc.perform(get("/api/assessment-types?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItems(DEFAULT_ID.intValue(), 2)))
        .andExpect(jsonPath("$.[*].code").value(hasItems(DEFAULT_CODE, "ABC")))
        .andExpect(jsonPath("$.[*].label").value(hasItems(DEFAULT_LABEL, "ABC_123")));

    AssessmentType createdEntity = repository.findOneByCode(DEFAULT_CODE);
    assertThat("Unexpected entity.", createdEntity, notNullValue());
    assertThat("Unexpected code.", createdEntity.getCode(), is(DEFAULT_CODE));
    assertThat("Unexpected label.", createdEntity.getLabel(), is(DEFAULT_LABEL));

    createdEntity = repository.findOneByCode("ABC");
    assertThat("Unexpected entity.", createdEntity, notNullValue());
    assertThat("Unexpected code.", createdEntity.getCode(), is("ABC"));
    assertThat("Unexpected label.", createdEntity.getLabel(), is("ABC_123"));
  }

  @Test
  @Transactional
  public void getEntitiesWithQueryMatchingCode() throws Exception {
    // Initialize the database.
    AssessmentType entity = new AssessmentType();
    entity.setCode(UNENCODED_CODE);
    entity.setLabel(DEFAULT_LABEL);
    repository.saveAndFlush(entity);

    // Get all the matching entities.
    mockMvc.perform(get("/api/assessment-types?searchQuery=\"Te%24t\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(entity.getId().intValue()))
        .andExpect(jsonPath("$.[*].code").value(UNENCODED_CODE))
        .andExpect(jsonPath("$.[*].label").value(DEFAULT_LABEL));
  }

  @Test
  @Transactional
  public void getEntitiesWithQueryMatchingLabel() throws Exception {
    // Initialize the database.
    AssessmentType entity = new AssessmentType();
    entity.setCode(DEFAULT_CODE);
    entity.setLabel(UNENCODED_LABEL);
    repository.saveAndFlush(entity);

    // Get all the matching entities.
    mockMvc.perform(get("/api/assessment-types?searchQuery=\"Te%24t\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(entity.getId().intValue()))
        .andExpect(jsonPath("$.[*].code").value(DEFAULT_CODE))
        .andExpect(jsonPath("$.[*].label").value(UNENCODED_LABEL));
  }

  @Test
  @Transactional
  public void getAssessmentTypeByCodeShouldReturnSingleAssessmentType() throws Exception {
    repository.saveAndFlush(assessmentType);

    mockMvc.perform(get("/api/assessment-types/{code}", assessmentType.getCode()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(DEFAULT_ID))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));
  }

  @Test
  @Transactional
  public void getAssessmentTypeByCodeShouldReturn404() throws Exception {
    mockMvc.perform(get("/api/assessment-types/{code}", assessmentType.getCode()))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(AssessmentType.class);
  }
}
