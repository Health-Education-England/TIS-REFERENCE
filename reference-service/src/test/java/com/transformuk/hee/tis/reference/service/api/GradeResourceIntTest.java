package com.transformuk.hee.tis.reference.service.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.Grade;
import com.transformuk.hee.tis.reference.service.repository.GradeRepository;
import com.transformuk.hee.tis.reference.service.service.impl.GradeServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.GradeMapper;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.assertj.core.util.Maps;
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
 * Test class for the GradeResource REST controller.
 *
 * @see GradeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GradeResourceIntTest {

  private static final String DEFAULT_ABBREVIATION = "AAAAAAAAAA";
  private static final String UPDATED_ABBREVIATION = "BBBBBBBBBB";
  private static final String UNENCODED_ABBREVIATION = "CCCCCCCCCC";

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";
  private static final String UNENCODED_NAME = "Te$t Grade";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t Grade Label";

  private static final Boolean DEFAULT_TRAINING_GRADE = false;
  private static final Boolean UPDATED_TRAINING_GRADE = true;

  private static final Boolean DEFAULT_POST_GRADE = false;
  private static final Boolean UPDATED_POST_GRADE = true;

  private static final Boolean DEFAULT_PLACEMENT_GRADE = false;
  private static final Boolean UPDATED_PLACEMENT_GRADE = true;

  @Autowired
  private GradeRepository gradeRepository;

  @Autowired
  private GradeMapper gradeMapper;

  @Autowired
  private GradeServiceImpl gradeService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restGradeMockMvc;

  private Grade grade;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static Grade createEntity() {
    Grade grade = new Grade();
    grade.setAbbreviation(DEFAULT_ABBREVIATION);
    grade.setName(DEFAULT_NAME);
    grade.setLabel(DEFAULT_LABEL);
    grade.setTrainingGrade(DEFAULT_TRAINING_GRADE);
    grade.setPostGrade(DEFAULT_POST_GRADE);
    grade.setPlacementGrade(DEFAULT_PLACEMENT_GRADE);
    return grade;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    GradeResource gradeResource = new GradeResource(gradeRepository, gradeMapper, gradeService);
    this.restGradeMockMvc = MockMvcBuilders.standaloneSetup(gradeResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    grade = createEntity();
  }

  @Test
  @Transactional
  public void createGrade() throws Exception {
    int databaseSizeBeforeCreate = gradeRepository.findAll().size();

    // Create the Grade
    GradeDTO gradeDTO = gradeMapper.gradeToGradeDTO(grade);
    restGradeMockMvc.perform(post("/api/grades")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(gradeDTO)))
        .andExpect(status().isCreated());

    // Validate the Grade in the database
    List<Grade> gradeList = gradeRepository.findAll();
    assertThat(gradeList).hasSize(databaseSizeBeforeCreate + 1);
    Grade testGrade = gradeList.get(gradeList.size() - 1);
    assertThat(testGrade.getAbbreviation()).isEqualTo(DEFAULT_ABBREVIATION);
    assertThat(testGrade.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testGrade.getLabel()).isEqualTo(DEFAULT_LABEL);
    assertThat(testGrade.getTrainingGrade()).isEqualTo(DEFAULT_TRAINING_GRADE);
    assertThat(testGrade.getPostGrade()).isEqualTo(DEFAULT_POST_GRADE);
    assertThat(testGrade.getPlacementGrade()).isEqualTo(DEFAULT_PLACEMENT_GRADE);
  }

  @Test
  @Transactional
  public void createGradeWithExistingId() throws Exception {
    em.persist(grade);
    int databaseSizeBeforeCreate = gradeRepository.findAll().size();

    // Create the Grade with an existing ID
    GradeDTO gradeDTO = gradeMapper.gradeToGradeDTO(grade);

    // An entity with an existing ID cannot be created, so this API call must fail
    restGradeMockMvc.perform(post("/api/grades")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(gradeDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<Grade> gradeList = gradeRepository.findAll();
    assertThat(gradeList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllGrades() throws Exception {
    // Initialize the database
    gradeRepository.saveAndFlush(grade);

    // Get all the gradeList
    restGradeMockMvc.perform(get("/api/grades?sort=abbreviation,asc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].abbreviation").value(hasItem(DEFAULT_ABBREVIATION)))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
        .andExpect(jsonPath("$.[*].trainingGrade").value(hasItem(DEFAULT_TRAINING_GRADE)))
        .andExpect(jsonPath("$.[*].postGrade").value(hasItem(DEFAULT_POST_GRADE)))
        .andExpect(jsonPath("$.[*].placementGrade").value(hasItem(DEFAULT_PLACEMENT_GRADE)));
  }

  @Test
  @Transactional
  public void getAllGradesIn() throws Exception {
    // Initialize the database
    gradeRepository.saveAndFlush(grade);

    // Get grades given the codes
    restGradeMockMvc.perform(get("/api/grades/in/invalid," + DEFAULT_ABBREVIATION))
        .andExpect(status().isFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].abbreviation").value(hasItem(DEFAULT_ABBREVIATION)))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
        .andExpect(jsonPath("$.[*].trainingGrade").value(hasItem(DEFAULT_TRAINING_GRADE)))
        .andExpect(jsonPath("$.[*].postGrade").value(hasItem(DEFAULT_POST_GRADE)))
        .andExpect(jsonPath("$.[*].placementGrade").value(hasItem(DEFAULT_PLACEMENT_GRADE)));
  }

  @Test
  @Transactional
  public void getGradesWithQuery() throws Exception {
    // Initialize the database
    Grade unencodedGrade = new Grade();
    unencodedGrade.setAbbreviation(UNENCODED_ABBREVIATION);
    unencodedGrade.setName(UNENCODED_NAME);
    unencodedGrade.setLabel(UNENCODED_LABEL);
    unencodedGrade.setTrainingGrade(DEFAULT_TRAINING_GRADE);
    unencodedGrade.setPostGrade(DEFAULT_POST_GRADE);
    unencodedGrade.setPlacementGrade(DEFAULT_PLACEMENT_GRADE);
    gradeRepository.saveAndFlush(unencodedGrade);

    // Get grades given the codes
    restGradeMockMvc.perform(get("/api/grades?page=0&size=200&sort=asc&searchQuery=\"Te%24t\""))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].abbreviation").value(hasItem(UNENCODED_ABBREVIATION)))
        .andExpect(jsonPath("$.[*].name").value(UNENCODED_NAME))
        .andExpect(jsonPath("$.[*].label").value(UNENCODED_LABEL));
  }

  @Test
  @Transactional
  public void shouldReturnTrueIfGradeExists() throws Exception {
    // Initialize the database
    gradeRepository.saveAndFlush(grade);
    Map<String, Boolean> expectedMap = Maps.newHashMap(grade.getAbbreviation(), true);

    List<String> ids = Lists.newArrayList(grade.getAbbreviation());
    restGradeMockMvc.perform(post("/api/grades/exists/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(ids)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(content().string(TestUtil.convertObjectToJson(expectedMap)));
  }

  @Test
  @Transactional
  public void getGrade() throws Exception {
    // Initialize the database
    grade = gradeRepository.saveAndFlush(grade);

    // Get the grade
    restGradeMockMvc.perform(get("/api/grades/{id}", grade.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(grade.getId()))
        .andExpect(jsonPath("$.abbreviation").value(DEFAULT_ABBREVIATION))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
        .andExpect(jsonPath("$.trainingGrade").value(DEFAULT_TRAINING_GRADE))
        .andExpect(jsonPath("$.postGrade").value(DEFAULT_POST_GRADE))
        .andExpect(jsonPath("$.placementGrade").value(DEFAULT_PLACEMENT_GRADE));
  }

  @Test
  @Transactional
  public void findTrustShouldReturnGradeWithAttributesMatchingSearchTerm() throws Exception {
    gradeRepository.saveAndFlush(grade);
    Grade anotherGrade = new Grade();
    anotherGrade.setAbbreviation(UPDATED_ABBREVIATION);
    anotherGrade.setName(UPDATED_NAME);
    anotherGrade.setLabel(DEFAULT_LABEL);
    anotherGrade.setTrainingGrade(DEFAULT_TRAINING_GRADE);
    anotherGrade.setPostGrade(DEFAULT_POST_GRADE);
    anotherGrade.setPlacementGrade(DEFAULT_PLACEMENT_GRADE);

    gradeRepository.saveAndFlush(anotherGrade);

    restGradeMockMvc.perform(get("/api/grades?page=0&size=200&sort=asc&searchQuery=\"AAA\""))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].abbreviation").value(hasItem(DEFAULT_ABBREVIATION)))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
        .andExpect(jsonPath("$.[*].trainingGrade").value(hasItem(DEFAULT_TRAINING_GRADE)))
        .andExpect(jsonPath("$.[*].postGrade").value(hasItem(DEFAULT_POST_GRADE)))
        .andExpect(jsonPath("$.[*].placementGrade").value(hasItem(DEFAULT_PLACEMENT_GRADE)));
  }

  @Test
  @Transactional
  public void getNonExistingGrade() throws Exception {
    // Get the grade
    restGradeMockMvc.perform(get("/api/grades/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateGrade() throws Exception {
    // Initialize the database
    grade = gradeRepository.saveAndFlush(grade);
    int databaseSizeBeforeUpdate = gradeRepository.findAll().size();

    // Update the grade
    Grade updatedGrade = gradeRepository.findById(grade.getId()).get();
    updatedGrade.setName(UPDATED_NAME);
    updatedGrade.setLabel(UPDATED_LABEL);
    updatedGrade.setTrainingGrade(UPDATED_TRAINING_GRADE);
    updatedGrade.setPostGrade(UPDATED_POST_GRADE);
    updatedGrade.setPlacementGrade(UPDATED_PLACEMENT_GRADE);
    GradeDTO gradeDTO = gradeMapper.gradeToGradeDTO(updatedGrade);

    restGradeMockMvc.perform(put("/api/grades")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(gradeDTO)))
        .andExpect(status().isOk());

    // Validate the Grade in the database
    List<Grade> gradeList = gradeRepository.findAll();
    assertThat(gradeList).hasSize(databaseSizeBeforeUpdate);
    Grade testGrade = gradeList.get(gradeList.size() - 1);
    assertThat(testGrade.getAbbreviation()).isEqualTo(DEFAULT_ABBREVIATION);
    assertThat(testGrade.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testGrade.getLabel()).isEqualTo(UPDATED_LABEL);
    assertThat(testGrade.getTrainingGrade()).isEqualTo(UPDATED_TRAINING_GRADE);
    assertThat(testGrade.getPostGrade()).isEqualTo(UPDATED_POST_GRADE);
    assertThat(testGrade.getPlacementGrade()).isEqualTo(UPDATED_PLACEMENT_GRADE);
  }

  @Test
  @Transactional
  public void updateNonExistingGrade() throws Exception {
    int databaseSizeBeforeUpdate = gradeRepository.findAll().size();

    // Create the Grade
    GradeDTO gradeDTO = gradeMapper.gradeToGradeDTO(grade);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restGradeMockMvc.perform(put("/api/grades")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(gradeDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Grade is not saved
    List<Grade> gradeList = gradeRepository.findAll();
    assertThat(gradeList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Grade.class);
  }
}
