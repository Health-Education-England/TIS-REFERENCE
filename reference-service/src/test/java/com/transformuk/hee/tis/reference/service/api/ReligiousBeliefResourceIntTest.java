package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.api.dto.ReligiousBeliefDTO;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.ReligiousBelief;
import com.transformuk.hee.tis.reference.service.repository.ReligiousBeliefRepository;
import com.transformuk.hee.tis.reference.service.service.impl.ReligiousBeliefServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.ReligiousBeliefMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ReligiousBeliefResource REST controller.
 *
 * @see ReligiousBeliefResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ReligiousBeliefResourceIntTest {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";

  @Autowired
  private ReligiousBeliefRepository religiousBeliefRepository;

  @Autowired
  private ReligiousBeliefMapper religiousBeliefMapper;
  @Autowired
  private ReligiousBeliefServiceImpl religiousBeliefService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restReligiousBeliefMockMvc;

  private ReligiousBelief religiousBelief;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static ReligiousBelief createEntity(EntityManager em) {
    ReligiousBelief religiousBelief = new ReligiousBelief()
        .code(DEFAULT_CODE)
        .label(DEFAULT_LABEL);
    return religiousBelief;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    ReligiousBeliefResource religiousBeliefResource = new ReligiousBeliefResource(religiousBeliefRepository,
        religiousBeliefMapper, religiousBeliefService);
    this.restReligiousBeliefMockMvc = MockMvcBuilders.standaloneSetup(religiousBeliefResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    religiousBelief = createEntity(em);
  }

  @Test
  @Transactional
  public void createReligiousBelief() throws Exception {
    int databaseSizeBeforeCreate = religiousBeliefRepository.findAll().size();

    // Create the ReligiousBelief
    ReligiousBeliefDTO religiousBeliefDTO = religiousBeliefMapper.religiousBeliefToReligiousBeliefDTO(religiousBelief);
    restReligiousBeliefMockMvc.perform(post("/api/religious-beliefs")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(religiousBeliefDTO)))
        .andExpect(status().isCreated());

    // Validate the ReligiousBelief in the database
    List<ReligiousBelief> religiousBeliefList = religiousBeliefRepository.findAll();
    assertThat(religiousBeliefList).hasSize(databaseSizeBeforeCreate + 1);
    ReligiousBelief testReligiousBelief = religiousBeliefList.get(religiousBeliefList.size() - 1);
    assertThat(testReligiousBelief.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testReligiousBelief.getLabel()).isEqualTo(DEFAULT_LABEL);
  }

  @Test
  @Transactional
  public void createReligiousBeliefWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = religiousBeliefRepository.findAll().size();

    // Create the ReligiousBelief with an existing ID
    religiousBelief.setId(1L);
    ReligiousBeliefDTO religiousBeliefDTO = religiousBeliefMapper.religiousBeliefToReligiousBeliefDTO(religiousBelief);

    // An entity with an existing ID cannot be created, so this API call must fail
    restReligiousBeliefMockMvc.perform(post("/api/religious-beliefs")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(religiousBeliefDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<ReligiousBelief> religiousBeliefList = religiousBeliefRepository.findAll();
    assertThat(religiousBeliefList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = religiousBeliefRepository.findAll().size();
    // set the field null
    religiousBelief.setCode(null);

    // Create the ReligiousBelief, which fails.
    ReligiousBeliefDTO religiousBeliefDTO = religiousBeliefMapper.religiousBeliefToReligiousBeliefDTO(religiousBelief);

    restReligiousBeliefMockMvc.perform(post("/api/religious-beliefs")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(religiousBeliefDTO)))
        .andExpect(status().isBadRequest());

    List<ReligiousBelief> religiousBeliefList = religiousBeliefRepository.findAll();
    assertThat(religiousBeliefList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLabelIsRequired() throws Exception {
    int databaseSizeBeforeTest = religiousBeliefRepository.findAll().size();
    // set the field null
    religiousBelief.setLabel(null);

    // Create the ReligiousBelief, which fails.
    ReligiousBeliefDTO religiousBeliefDTO = religiousBeliefMapper.religiousBeliefToReligiousBeliefDTO(religiousBelief);

    restReligiousBeliefMockMvc.perform(post("/api/religious-beliefs")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(religiousBeliefDTO)))
        .andExpect(status().isBadRequest());

    List<ReligiousBelief> religiousBeliefList = religiousBeliefRepository.findAll();
    assertThat(religiousBeliefList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllReligiousBeliefs() throws Exception {
    // Initialize the database
    religiousBeliefRepository.saveAndFlush(religiousBelief);

    // Get all the religiousBeliefList
    restReligiousBeliefMockMvc.perform(get("/api/religious-beliefs?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(religiousBelief.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
  }

  @Test
  @Transactional
  public void getReligiousBelief() throws Exception {
    // Initialize the database
    religiousBeliefRepository.saveAndFlush(religiousBelief);

    // Get the religiousBelief
    restReligiousBeliefMockMvc.perform(get("/api/religious-beliefs/{id}", religiousBelief.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(religiousBelief.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingReligiousBelief() throws Exception {
    // Get the religiousBelief
    restReligiousBeliefMockMvc.perform(get("/api/religious-beliefs/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateReligiousBelief() throws Exception {
    // Initialize the database
    religiousBeliefRepository.saveAndFlush(religiousBelief);
    int databaseSizeBeforeUpdate = religiousBeliefRepository.findAll().size();

    // Update the religiousBelief
      ReligiousBelief updatedReligiousBelief = religiousBeliefRepository.findById(religiousBelief.getId()).orElse(null);
    updatedReligiousBelief
        .code(UPDATED_CODE)
        .label(UPDATED_LABEL);
    ReligiousBeliefDTO religiousBeliefDTO = religiousBeliefMapper.religiousBeliefToReligiousBeliefDTO(updatedReligiousBelief);

    restReligiousBeliefMockMvc.perform(put("/api/religious-beliefs")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(religiousBeliefDTO)))
        .andExpect(status().isOk());

    // Validate the ReligiousBelief in the database
    List<ReligiousBelief> religiousBeliefList = religiousBeliefRepository.findAll();
    assertThat(religiousBeliefList).hasSize(databaseSizeBeforeUpdate);
    ReligiousBelief testReligiousBelief = religiousBeliefList.get(religiousBeliefList.size() - 1);
    assertThat(testReligiousBelief.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testReligiousBelief.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  @Transactional
  public void updateNonExistingReligiousBelief() throws Exception {
    int databaseSizeBeforeUpdate = religiousBeliefRepository.findAll().size();

    // Create the ReligiousBelief
    ReligiousBeliefDTO religiousBeliefDTO = religiousBeliefMapper.religiousBeliefToReligiousBeliefDTO(religiousBelief);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restReligiousBeliefMockMvc.perform(put("/api/religious-beliefs")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(religiousBeliefDTO)))
        .andExpect(status().isCreated());

    // Validate the ReligiousBelief in the database
    List<ReligiousBelief> religiousBeliefList = religiousBeliefRepository.findAll();
    assertThat(religiousBeliefList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteReligiousBelief() throws Exception {
    // Initialize the database
    religiousBeliefRepository.saveAndFlush(religiousBelief);
    int databaseSizeBeforeDelete = religiousBeliefRepository.findAll().size();

    // Get the religiousBelief
    restReligiousBeliefMockMvc.perform(delete("/api/religious-beliefs/{id}", religiousBelief.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<ReligiousBelief> religiousBeliefList = religiousBeliefRepository.findAll();
    assertThat(religiousBeliefList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ReligiousBelief.class);
  }
}
