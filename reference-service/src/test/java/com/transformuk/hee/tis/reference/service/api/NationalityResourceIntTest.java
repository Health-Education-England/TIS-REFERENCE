package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.api.dto.NationalityDTO;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.Nationality;
import com.transformuk.hee.tis.reference.service.repository.NationalityRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.NationalityMapper;
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
 * Test class for the NationalityResource REST controller.
 *
 * @see NationalityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class NationalityResourceIntTest {

  private static final String DEFAULT_COUNTRY_NUMBER = "AAAAAAAAAA";
  private static final String UPDATED_COUNTRY_NUMBER = "BBBBBBBBBB";

  private static final String DEFAULT_NATIONALITY = "AAAAAAAAAA";
  private static final String UPDATED_NATIONALITY = "BBBBBBBBBB";

  @Autowired
  private NationalityRepository nationalityRepository;

  @Autowired
  private NationalityMapper nationalityMapper;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restNationalityMockMvc;

  private Nationality nationality;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Nationality createEntity(EntityManager em) {
    Nationality nationality = new Nationality()
        .countryNumber(DEFAULT_COUNTRY_NUMBER)
        .nationality(DEFAULT_NATIONALITY);
    return nationality;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    NationalityResource nationalityResource = new NationalityResource(nationalityRepository, nationalityMapper);
    this.restNationalityMockMvc = MockMvcBuilders.standaloneSetup(nationalityResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    nationality = createEntity(em);
  }

  @Test
  @Transactional
  public void createNationality() throws Exception {
    int databaseSizeBeforeCreate = nationalityRepository.findAll().size();

    // Create the Nationality
    NationalityDTO nationalityDTO = nationalityMapper.nationalityToNationalityDTO(nationality);
    restNationalityMockMvc.perform(post("/api/nationalities")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(nationalityDTO)))
        .andExpect(status().isCreated());

    // Validate the Nationality in the database
    List<Nationality> nationalityList = nationalityRepository.findAll();
    assertThat(nationalityList).hasSize(databaseSizeBeforeCreate + 1);
    Nationality testNationality = nationalityList.get(nationalityList.size() - 1);
    assertThat(testNationality.getCountryNumber()).isEqualTo(DEFAULT_COUNTRY_NUMBER);
    assertThat(testNationality.getNationality()).isEqualTo(DEFAULT_NATIONALITY);
  }

  @Test
  @Transactional
  public void createNationalityWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = nationalityRepository.findAll().size();

    // Create the Nationality with an existing ID
    nationality.setId(1L);
    NationalityDTO nationalityDTO = nationalityMapper.nationalityToNationalityDTO(nationality);

    // An entity with an existing ID cannot be created, so this API call must fail
    restNationalityMockMvc.perform(post("/api/nationalities")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(nationalityDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<Nationality> nationalityList = nationalityRepository.findAll();
    assertThat(nationalityList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkCountryNumberIsRequired() throws Exception {
    int databaseSizeBeforeTest = nationalityRepository.findAll().size();
    // set the field null
    nationality.setCountryNumber(null);

    // Create the Nationality, which fails.
    NationalityDTO nationalityDTO = nationalityMapper.nationalityToNationalityDTO(nationality);

    restNationalityMockMvc.perform(post("/api/nationalities")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(nationalityDTO)))
        .andExpect(status().isBadRequest());

    List<Nationality> nationalityList = nationalityRepository.findAll();
    assertThat(nationalityList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkNationalityIsRequired() throws Exception {
    int databaseSizeBeforeTest = nationalityRepository.findAll().size();
    // set the field null
    nationality.setNationality(null);

    // Create the Nationality, which fails.
    NationalityDTO nationalityDTO = nationalityMapper.nationalityToNationalityDTO(nationality);

    restNationalityMockMvc.perform(post("/api/nationalities")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(nationalityDTO)))
        .andExpect(status().isBadRequest());

    List<Nationality> nationalityList = nationalityRepository.findAll();
    assertThat(nationalityList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllNationalities() throws Exception {
    // Initialize the database
    nationalityRepository.saveAndFlush(nationality);

    // Get all the nationalityList
    restNationalityMockMvc.perform(get("/api/nationalities?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(nationality.getId().intValue())))
        .andExpect(jsonPath("$.[*].countryNumber").value(hasItem(DEFAULT_COUNTRY_NUMBER.toString())))
        .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY.toString())));
  }

  @Test
  @Transactional
  public void getNationality() throws Exception {
    // Initialize the database
    nationalityRepository.saveAndFlush(nationality);

    // Get the nationality
    restNationalityMockMvc.perform(get("/api/nationalities/{id}", nationality.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(nationality.getId().intValue()))
        .andExpect(jsonPath("$.countryNumber").value(DEFAULT_COUNTRY_NUMBER.toString()))
        .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingNationality() throws Exception {
    // Get the nationality
    restNationalityMockMvc.perform(get("/api/nationalities/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateNationality() throws Exception {
    // Initialize the database
    nationalityRepository.saveAndFlush(nationality);
    int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();

    // Update the nationality
    Nationality updatedNationality = nationalityRepository.findOne(nationality.getId());
    updatedNationality
        .countryNumber(UPDATED_COUNTRY_NUMBER)
        .nationality(UPDATED_NATIONALITY);
    NationalityDTO nationalityDTO = nationalityMapper.nationalityToNationalityDTO(updatedNationality);

    restNationalityMockMvc.perform(put("/api/nationalities")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(nationalityDTO)))
        .andExpect(status().isOk());

    // Validate the Nationality in the database
    List<Nationality> nationalityList = nationalityRepository.findAll();
    assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate);
    Nationality testNationality = nationalityList.get(nationalityList.size() - 1);
    assertThat(testNationality.getCountryNumber()).isEqualTo(UPDATED_COUNTRY_NUMBER);
    assertThat(testNationality.getNationality()).isEqualTo(UPDATED_NATIONALITY);
  }

  @Test
  @Transactional
  public void updateNonExistingNationality() throws Exception {
    int databaseSizeBeforeUpdate = nationalityRepository.findAll().size();

    // Create the Nationality
    NationalityDTO nationalityDTO = nationalityMapper.nationalityToNationalityDTO(nationality);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restNationalityMockMvc.perform(put("/api/nationalities")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(nationalityDTO)))
        .andExpect(status().isCreated());

    // Validate the Nationality in the database
    List<Nationality> nationalityList = nationalityRepository.findAll();
    assertThat(nationalityList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteNationality() throws Exception {
    // Initialize the database
    nationalityRepository.saveAndFlush(nationality);
    int databaseSizeBeforeDelete = nationalityRepository.findAll().size();

    // Get the nationality
    restNationalityMockMvc.perform(delete("/api/nationalities/{id}", nationality.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<Nationality> nationalityList = nationalityRepository.findAll();
    assertThat(nationalityList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Nationality.class);
  }
}
