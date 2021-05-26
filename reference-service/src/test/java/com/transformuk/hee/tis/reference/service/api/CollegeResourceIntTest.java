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

import com.transformuk.hee.tis.reference.api.dto.CollegeDTO;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.College;
import com.transformuk.hee.tis.reference.service.repository.CollegeRepository;
import com.transformuk.hee.tis.reference.service.service.impl.CollegeServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.CollegeMapper;
import java.util.List;
import javax.persistence.EntityManager;
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
 * Test class for the CollegeResource REST controller.
 *
 * @see CollegeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class CollegeResourceIntTest {

  private static final String DEFAULT_ABBREVIATION = "AAAAAAAAAA";
  private static final String UPDATED_ABBREVIATION = "BBBBBBBBBB";
  private static final String UNENCODED_ABBREVIATION = "CCCCCCCCCC";

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";
  private static final String UNENCODED_NAME = "Faculty of Te$ting & Validation";

  @Autowired
  private CollegeRepository collegeRepository;

  @Autowired
  private CollegeMapper collegeMapper;

  @Autowired
  private CollegeServiceImpl collegeService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restCollegeMockMvc;

  private College college;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static College createEntity(EntityManager em) {
    College college = new College()
        .abbreviation(DEFAULT_ABBREVIATION)
        .name(DEFAULT_NAME);
    return college;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    CollegeResource testObj = new CollegeResource(collegeRepository, collegeMapper, collegeService);
    this.restCollegeMockMvc = MockMvcBuilders.standaloneSetup(testObj)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    college = createEntity(em);
  }

  @Test
  @Transactional
  public void createCollege() throws Exception {
    int databaseSizeBeforeCreate = collegeRepository.findAll().size();

    // Create the College
    CollegeDTO collegeDTO = collegeMapper.collegeToCollegeDTO(college);
    restCollegeMockMvc.perform(post("/api/colleges")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(collegeDTO)))
        .andExpect(status().isCreated());

    // Validate the College in the database
    List<College> collegeList = collegeRepository.findAll();
    assertThat(collegeList).hasSize(databaseSizeBeforeCreate + 1);
    College testCollege = collegeList.get(collegeList.size() - 1);
    assertThat(testCollege.getAbbreviation()).isEqualTo(DEFAULT_ABBREVIATION);
    assertThat(testCollege.getName()).isEqualTo(DEFAULT_NAME);
  }

  @Test
  @Transactional
  public void createCollegeWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = collegeRepository.findAll().size();

    // Create the College with an existing ID
    college.setId(1L);
    CollegeDTO collegeDTO = collegeMapper.collegeToCollegeDTO(college);

    // An entity with an existing ID cannot be created, so this API call must fail
    restCollegeMockMvc.perform(post("/api/colleges")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(collegeDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<College> collegeList = collegeRepository.findAll();
    assertThat(collegeList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkAbbreviationIsRequired() throws Exception {
    int databaseSizeBeforeTest = collegeRepository.findAll().size();
    // set the field null
    college.setAbbreviation(null);

    // Create the College, which fails.
    CollegeDTO collegeDTO = collegeMapper.collegeToCollegeDTO(college);

    restCollegeMockMvc.perform(post("/api/colleges")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(collegeDTO)))
        .andExpect(status().isBadRequest());

    List<College> collegeList = collegeRepository.findAll();
    assertThat(collegeList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllColleges() throws Exception {
    // Initialize the database
    collegeRepository.saveAndFlush(college);

    // Get all the collegeList
    restCollegeMockMvc.perform(get("/api/colleges?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(college.getId().intValue())))
        .andExpect(jsonPath("$.[*].abbreviation").value(hasItem(DEFAULT_ABBREVIATION.toString())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
  }

  @Test
  @Transactional
  public void getCollegesWithEscapedCharacterSearchShouldFindShoudReturnCollege() throws Exception {
    // Initialize the database
    College unescapedCollege = new College()
        .abbreviation(UNENCODED_ABBREVIATION)
        .name(UNENCODED_NAME);
    unescapedCollege = collegeRepository.saveAndFlush(unescapedCollege);

    // Get all the collegeList
    restCollegeMockMvc.perform(get("/api/colleges?searchQuery=\"Te%24ting%20%26%20\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(unescapedCollege.getId().intValue())))
        .andExpect(jsonPath("$.[*].abbreviation").value(hasItem(UNENCODED_ABBREVIATION.toString())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(UNENCODED_NAME.toString())));
  }

  @Test
  @Transactional
  public void getCollege() throws Exception {
    // Initialize the database
    collegeRepository.saveAndFlush(college);

    // Get the college
    restCollegeMockMvc.perform(get("/api/colleges/{id}", college.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(college.getId().intValue()))
        .andExpect(jsonPath("$.abbreviation").value(DEFAULT_ABBREVIATION.toString()))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingCollege() throws Exception {
    // Get the college
    restCollegeMockMvc.perform(get("/api/colleges/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateCollege() throws Exception {
    // Initialize the database
    collegeRepository.saveAndFlush(college);
    int databaseSizeBeforeUpdate = collegeRepository.findAll().size();

    // Update the college
    College updatedCollege = collegeRepository.findById(college.getId()).get();
    updatedCollege
        .abbreviation(UPDATED_ABBREVIATION)
        .name(UPDATED_NAME);
    CollegeDTO collegeDTO = collegeMapper.collegeToCollegeDTO(updatedCollege);

    restCollegeMockMvc.perform(put("/api/colleges")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(collegeDTO)))
        .andExpect(status().isOk());

    // Validate the College in the database
    List<College> collegeList = collegeRepository.findAll();
    assertThat(collegeList).hasSize(databaseSizeBeforeUpdate);
    College testCollege = collegeList.get(collegeList.size() - 1);
    assertThat(testCollege.getAbbreviation()).isEqualTo(UPDATED_ABBREVIATION);
    assertThat(testCollege.getName()).isEqualTo(UPDATED_NAME);
  }

  @Test
  @Transactional
  public void updateNonExistingCollege() throws Exception {
    int databaseSizeBeforeUpdate = collegeRepository.findAll().size();

    // Create the College
    CollegeDTO collegeDTO = collegeMapper.collegeToCollegeDTO(college);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restCollegeMockMvc.perform(put("/api/colleges")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(collegeDTO)))
        .andExpect(status().isCreated());

    // Validate the College in the database
    List<College> collegeList = collegeRepository.findAll();
    assertThat(collegeList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteCollege() throws Exception {
    // Initialize the database
    collegeRepository.saveAndFlush(college);
    int databaseSizeBeforeDelete = collegeRepository.findAll().size();

    // Get the college
    restCollegeMockMvc.perform(delete("/api/colleges/{id}", college.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<College> collegeList = collegeRepository.findAll();
    assertThat(collegeList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(College.class);
  }
}
