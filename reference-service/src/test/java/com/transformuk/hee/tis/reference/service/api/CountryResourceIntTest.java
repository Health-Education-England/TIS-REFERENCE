package com.transformuk.hee.tis.reference.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.CountryDTO;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.Country;
import com.transformuk.hee.tis.reference.service.repository.CountryRepository;
import com.transformuk.hee.tis.reference.service.service.impl.CountryServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.CountryMapper;
import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Ignore;
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
import java.util.Map;

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
 * Test class for the CountryResource REST controller.
 *
 * @see CountryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class CountryResourceIntTest {

  private static final String DEFAULT_COUNTRY_NUMBER = "AAAAAAAAAA";
  private static final String UPDATED_COUNTRY_NUMBER = "BBBBBBBBBB";
  private static final String UNENCODED_COUNTRY_NUMBER = "CCCCCCCCCC";

  private static final String DEFAULT_NATIONALITY = "AAAAAAAAAA";
  private static final String UPDATED_NATIONALITY = "BBBBBBBBBB";
  private static final String UNENCODED_NATIONALITY = "Check! national";

  @Autowired
  private CountryRepository countryRepository;

  @Autowired
  private CountryMapper countryMapper;

  @Autowired
  private CountryServiceImpl countryService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restCountryMockMvc;

  private Country country;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Country createEntity(EntityManager em) {
    Country country = new Country()
        .countryNumber(DEFAULT_COUNTRY_NUMBER)
        .nationality(DEFAULT_NATIONALITY);
    return country;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    CountryResource countryResource = new CountryResource(countryRepository, countryMapper, countryService);
    this.restCountryMockMvc = MockMvcBuilders.standaloneSetup(countryResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    country = createEntity(em);
  }

  @Test
  @Transactional
  public void createCountry() throws Exception {
    int databaseSizeBeforeCreate = countryRepository.findAll().size();

    // Create the Country
    CountryDTO countryDTO = countryMapper.countryToCountryDTO(country);
    restCountryMockMvc.perform(post("/api/countries")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
        .andExpect(status().isCreated());

    // Validate the Country in the database
    List<Country> countryList = countryRepository.findAll();
    assertThat(countryList).hasSize(databaseSizeBeforeCreate + 1);
    Country testCountry = countryList.get(countryList.size() - 1);
    assertThat(testCountry.getCountryNumber()).isEqualTo(DEFAULT_COUNTRY_NUMBER);
    assertThat(testCountry.getNationality()).isEqualTo(DEFAULT_NATIONALITY);
  }

  @Test
  @Transactional
  public void createCountryWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = countryRepository.findAll().size();

    // Create the Country with an existing ID
    country.setId(1L);
    CountryDTO countryDTO = countryMapper.countryToCountryDTO(country);

    // An entity with an existing ID cannot be created, so this API call must fail
    restCountryMockMvc.perform(post("/api/countries")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<Country> countryList = countryRepository.findAll();
    assertThat(countryList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkCountryNumberIsRequired() throws Exception {
    int databaseSizeBeforeTest = countryRepository.findAll().size();
    // set the field null
    country.setCountryNumber(null);

    // Create the Country, which fails.
    CountryDTO countryDTO = countryMapper.countryToCountryDTO(country);

    restCountryMockMvc.perform(post("/api/countries")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
        .andExpect(status().isBadRequest());

    List<Country> countryList = countryRepository.findAll();
    assertThat(countryList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkNationalityIsRequired() throws Exception {
    int databaseSizeBeforeTest = countryRepository.findAll().size();
    // set the field null
    country.setNationality(null);

    // Create the Country, which fails.
    CountryDTO countryDTO = countryMapper.countryToCountryDTO(country);

    restCountryMockMvc.perform(post("/api/countries")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
        .andExpect(status().isBadRequest());

    List<Country> countryList = countryRepository.findAll();
    assertThat(countryList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllCountries() throws Exception {
    // Initialize the database
    countryRepository.saveAndFlush(country);

    // Get all the countryList
    restCountryMockMvc.perform(get("/api/countries?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
        .andExpect(jsonPath("$.[*].countryNumber").value(hasItem(DEFAULT_COUNTRY_NUMBER.toString())))
        .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY.toString())));
  }

  @Test
  @Transactional
  public void getCountriesWithQueryShouldReturnMatch() throws Exception {
    // Initialize the database
    countryRepository.saveAndFlush(country);
    
    // Get all the countryList
    restCountryMockMvc.perform(get("/api/countries?searchQuery=AAAAA&sort=id,desc"))
    .andExpect(status().isOk())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
    .andExpect(jsonPath("$.[*].countryNumber").value(hasItem(DEFAULT_COUNTRY_NUMBER.toString())))
    .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY.toString())));
  }
  
  @Test
  @Transactional
  public void getCountriesMatchingEncodedQueryShouldReturnMatch() throws Exception {
    // Initialize the database
    Country unencodedCountry = new Country()
        .countryNumber(UNENCODED_COUNTRY_NUMBER)
        .nationality(UNENCODED_NATIONALITY);
    unencodedCountry = countryRepository.saveAndFlush(unencodedCountry);
    
    // Get all the countryList
    restCountryMockMvc.perform(get("/api/countries?searchQuery=Check%21%20&sort=id,desc"))
    .andExpect(status().isOk())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    .andExpect(jsonPath("$.[*].id").value(hasItem(unencodedCountry.getId().intValue())))
    .andExpect(jsonPath("$.[*].countryNumber").value(hasItem(UNENCODED_COUNTRY_NUMBER.toString())))
    .andExpect(jsonPath("$.[*].nationality").value(hasItem(UNENCODED_NATIONALITY.toString())));
  }

  @Test
  @Transactional
  public void getCountry() throws Exception {
    // Initialize the database
    countryRepository.saveAndFlush(country);

    // Get the country
    restCountryMockMvc.perform(get("/api/countries/{id}", country.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(country.getId().intValue()))
        .andExpect(jsonPath("$.countryNumber").value(DEFAULT_COUNTRY_NUMBER.toString()))
        .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY.toString()));
  }

  @Test
  @Transactional
  public void shouldReturnTrueIfCountriesExistsAndFalseIfNotExists() throws Exception {
    // Initialize the database
    countryRepository.saveAndFlush(country);
    Map<String, Boolean> expectedMap = Maps.newHashMap(country.getNationality(), true);
    expectedMap.put("XYZ", false);
    List<String> nationalities = Lists.newArrayList(country.getNationality(), "XYZ");
    restCountryMockMvc.perform(post("/api/countries/exists/")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(nationalities)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(content().string(TestUtil.convertObjectToJson(expectedMap)));
  }

  @Test
  @Transactional
  public void getNonExistingCountry() throws Exception {
    // Get the country
    restCountryMockMvc.perform(get("/api/countries/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateCountry() throws Exception {
    // Initialize the database
    countryRepository.saveAndFlush(country);
    int databaseSizeBeforeUpdate = countryRepository.findAll().size();

    // Update the country
    Country updatedCountry = countryRepository.findOne(country.getId());
    updatedCountry
        .countryNumber(UPDATED_COUNTRY_NUMBER)
        .nationality(UPDATED_NATIONALITY);
    CountryDTO countryDTO = countryMapper.countryToCountryDTO(updatedCountry);

    restCountryMockMvc.perform(put("/api/countries")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
        .andExpect(status().isOk());

    // Validate the Country in the database
    List<Country> countryList = countryRepository.findAll();
    assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    Country testCountry = countryList.get(countryList.size() - 1);
    assertThat(testCountry.getCountryNumber()).isEqualTo(UPDATED_COUNTRY_NUMBER);
    assertThat(testCountry.getNationality()).isEqualTo(UPDATED_NATIONALITY);
  }

  @Test
  @Transactional
  public void updateNonExistingCountry() throws Exception {
    int databaseSizeBeforeUpdate = countryRepository.findAll().size();

    // Create the Country
    CountryDTO countryDTO = countryMapper.countryToCountryDTO(country);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restCountryMockMvc.perform(put("/api/countries")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
        .andExpect(status().isCreated());

    // Validate the Country in the database
    List<Country> countryList = countryRepository.findAll();
    assertThat(countryList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteCountry() throws Exception {
    // Initialize the database
    countryRepository.saveAndFlush(country);
    int databaseSizeBeforeDelete = countryRepository.findAll().size();

    // Get the country
    restCountryMockMvc.perform(delete("/api/countries/{id}", country.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<Country> countryList = countryRepository.findAll();
    assertThat(countryList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Country.class);
  }
}
