package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.api.dto.GenderDTO;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.Gender;
import com.transformuk.hee.tis.reference.service.repository.GenderRepository;
import com.transformuk.hee.tis.reference.service.service.impl.GenderServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.GenderMapper;
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

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the GenderResource REST controller.
 *
 * @see GenderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GenderResourceIntTest {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "CCCCCCCCCC";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t Gender";

  @Autowired
  private GenderRepository genderRepository;

  @Autowired
  private GenderMapper genderMapper;

  @Autowired
  private GenderServiceImpl genderService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restGenderMockMvc;

  private Gender gender;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Gender createEntity(EntityManager em) {
    Gender gender = new Gender()
        .code(DEFAULT_CODE)
        .label(DEFAULT_LABEL);
    return gender;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    GenderResource genderResource = new GenderResource(genderRepository, genderMapper, genderService);
    this.restGenderMockMvc = MockMvcBuilders.standaloneSetup(genderResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    gender = createEntity(em);
  }

  @Test
  @Transactional
  public void createGender() throws Exception {
    int databaseSizeBeforeCreate = genderRepository.findAll().size();

    // Create the Gender
    GenderDTO genderDTO = genderMapper.genderToGenderDTO(gender);
    restGenderMockMvc.perform(post("/api/genders")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(genderDTO)))
        .andExpect(status().isCreated());

    // Validate the Gender in the database
    List<Gender> genderList = genderRepository.findAll();
    assertThat(genderList).hasSize(databaseSizeBeforeCreate + 1);
    Gender testGender = genderList.get(genderList.size() - 1);
    assertThat(testGender.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testGender.getLabel()).isEqualTo(DEFAULT_LABEL);
  }

  @Test
  @Transactional
  public void createGenderWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = genderRepository.findAll().size();

    // Create the Gender with an existing ID
    gender.setId(1L);
    GenderDTO genderDTO = genderMapper.genderToGenderDTO(gender);

    // An entity with an existing ID cannot be created, so this API call must fail
    restGenderMockMvc.perform(post("/api/genders")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(genderDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<Gender> genderList = genderRepository.findAll();
    assertThat(genderList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = genderRepository.findAll().size();
    // set the field null
    gender.setCode(null);

    // Create the Gender, which fails.
    GenderDTO genderDTO = genderMapper.genderToGenderDTO(gender);

    restGenderMockMvc.perform(post("/api/genders")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(genderDTO)))
        .andExpect(status().isBadRequest());

    List<Gender> genderList = genderRepository.findAll();
    assertThat(genderList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLabelIsRequired() throws Exception {
    int databaseSizeBeforeTest = genderRepository.findAll().size();
    // set the field null
    gender.setLabel(null);

    // Create the Gender, which fails.
    GenderDTO genderDTO = genderMapper.genderToGenderDTO(gender);

    restGenderMockMvc.perform(post("/api/genders")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(genderDTO)))
        .andExpect(status().isBadRequest());

    List<Gender> genderList = genderRepository.findAll();
    assertThat(genderList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllGenders() throws Exception {
    // Initialize the database
    genderRepository.saveAndFlush(gender);

    // Get all the genderList
    restGenderMockMvc.perform(get("/api/genders?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(gender.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
  }

  @Test
  @Transactional
  public void getGendersWithQuery() throws Exception {
    // Initialize the database
    Gender unencodedGender = new Gender()
        .code(UNENCODED_CODE)
        .label(UNENCODED_LABEL);
    genderRepository.saveAndFlush(unencodedGender);

    // Get all the genderList
    restGenderMockMvc.perform(get("/api/genders?searchQuery=\"Te%24t\"&sort=id,desc"))
    .andExpect(status().isOk())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    .andExpect(jsonPath("$.[*].id").value(unencodedGender.getId().intValue()))
    .andExpect(jsonPath("$.[*].code").value(UNENCODED_CODE))
    .andExpect(jsonPath("$.[*].label").value(UNENCODED_LABEL));
  }

  @Test
  @Transactional
  public void getGender() throws Exception {
    // Initialize the database
    genderRepository.saveAndFlush(gender);

    // Get the gender
    restGenderMockMvc.perform(get("/api/genders/{id}", gender.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(gender.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingGender() throws Exception {
    // Get the gender
    restGenderMockMvc.perform(get("/api/genders/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateGender() throws Exception {
    // Initialize the database
    genderRepository.saveAndFlush(gender);
    int databaseSizeBeforeUpdate = genderRepository.findAll().size();

    // Update the gender
    Gender updatedGender = genderRepository.findOne(gender.getId());
    updatedGender
        .code(UPDATED_CODE)
        .label(UPDATED_LABEL);
    GenderDTO genderDTO = genderMapper.genderToGenderDTO(updatedGender);

    restGenderMockMvc.perform(put("/api/genders")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(genderDTO)))
        .andExpect(status().isOk());

    // Validate the Gender in the database
    List<Gender> genderList = genderRepository.findAll();
    assertThat(genderList).hasSize(databaseSizeBeforeUpdate);
    Gender testGender = genderList.get(genderList.size() - 1);
    assertThat(testGender.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testGender.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  @Transactional
  public void updateNonExistingGender() throws Exception {
    int databaseSizeBeforeUpdate = genderRepository.findAll().size();

    // Create the Gender
    GenderDTO genderDTO = genderMapper.genderToGenderDTO(gender);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restGenderMockMvc.perform(put("/api/genders")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(genderDTO)))
        .andExpect(status().isCreated());

    // Validate the Gender in the database
    List<Gender> genderList = genderRepository.findAll();
    assertThat(genderList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteGender() throws Exception {
    // Initialize the database
    genderRepository.saveAndFlush(gender);
    int databaseSizeBeforeDelete = genderRepository.findAll().size();

    // Get the gender
    restGenderMockMvc.perform(delete("/api/genders/{id}", gender.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<Gender> genderList = genderRepository.findAll();
    assertThat(genderList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Gender.class);
  }
}
