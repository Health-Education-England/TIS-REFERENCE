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

import com.transformuk.hee.tis.reference.api.dto.ReligiousBeliefDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.ReligiousBelief;
import com.transformuk.hee.tis.reference.service.repository.ReligiousBeliefRepository;
import com.transformuk.hee.tis.reference.service.service.impl.ReligiousBeliefServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.ReligiousBeliefMapper;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.codec.CharEncoding;
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
 * Test class for the ReligiousBeliefResource REST controller.
 *
 * @see ReligiousBeliefResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ReligiousBeliefResourceIntTest {

  private static final String EXISTS_ENDPOINT = "/api/religious-beliefs/exists/";

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "CCCCCCCCCC";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t Religious Belief";

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

  private MockMvc mockMvc;

  private ReligiousBelief religiousBelief;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static ReligiousBelief createEntity(EntityManager em) {
    ReligiousBelief religiousBelief = new ReligiousBelief();
    religiousBelief.setCode(DEFAULT_CODE);
    religiousBelief.setLabel(DEFAULT_LABEL);
    return religiousBelief;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    ReligiousBeliefResource religiousBeliefResource = new ReligiousBeliefResource(
        religiousBeliefRepository,
        religiousBeliefMapper, religiousBeliefService);
    this.mockMvc = MockMvcBuilders.standaloneSetup(religiousBeliefResource)
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
    ReligiousBeliefDTO religiousBeliefDTO = religiousBeliefMapper
        .religiousBeliefToReligiousBeliefDTO(religiousBelief);
    mockMvc.perform(post("/api/religious-beliefs")
        .contentType(MediaType.APPLICATION_JSON)
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
    ReligiousBeliefDTO religiousBeliefDTO = religiousBeliefMapper
        .religiousBeliefToReligiousBeliefDTO(religiousBelief);

    // An entity with an existing ID cannot be created, so this API call must fail
    mockMvc.perform(post("/api/religious-beliefs")
        .contentType(MediaType.APPLICATION_JSON)
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
    ReligiousBeliefDTO religiousBeliefDTO = religiousBeliefMapper
        .religiousBeliefToReligiousBeliefDTO(religiousBelief);

    mockMvc.perform(post("/api/religious-beliefs")
        .contentType(MediaType.APPLICATION_JSON)
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
    ReligiousBeliefDTO religiousBeliefDTO = religiousBeliefMapper
        .religiousBeliefToReligiousBeliefDTO(religiousBelief);

    mockMvc.perform(post("/api/religious-beliefs")
        .contentType(MediaType.APPLICATION_JSON)
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
    mockMvc.perform(get("/api/religious-beliefs?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(religiousBelief.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
  }

  @Test
  @Transactional
  public void getReligiousBeliefsWithQuery() throws Exception {
    // Initialize the database
    ReligiousBelief unencodedReligiousBelief = new ReligiousBelief();
    unencodedReligiousBelief.setCode(UNENCODED_CODE);
    unencodedReligiousBelief.setLabel(UNENCODED_LABEL);
    religiousBeliefRepository.saveAndFlush(unencodedReligiousBelief);

    // Get the religiousBeliefList
    mockMvc
        .perform(get("/api/religious-beliefs?searchQuery=\"Te%24t\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(unencodedReligiousBelief.getId().intValue()))
        .andExpect(jsonPath("$.[*].code").value(UNENCODED_CODE))
        .andExpect(jsonPath("$.[*].label").value(UNENCODED_LABEL));
  }

  @Test
  @Transactional
  public void getReligiousBelief() throws Exception {
    // Initialize the database
    religiousBeliefRepository.saveAndFlush(religiousBelief);

    // Get the religiousBelief
    mockMvc.perform(get("/api/religious-beliefs/{id}", religiousBelief.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(religiousBelief.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingReligiousBelief() throws Exception {
    // Get the religiousBelief
    mockMvc.perform(get("/api/religious-beliefs/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateReligiousBelief() throws Exception {
    // Initialize the database
    religiousBeliefRepository.saveAndFlush(religiousBelief);
    int databaseSizeBeforeUpdate = religiousBeliefRepository.findAll().size();

    // Update the religiousBelief
    ReligiousBelief updatedReligiousBelief = religiousBeliefRepository
        .findById(religiousBelief.getId()).get();
    updatedReligiousBelief.setCode(UPDATED_CODE);
    updatedReligiousBelief.setLabel(UPDATED_LABEL);
    ReligiousBeliefDTO religiousBeliefDTO = religiousBeliefMapper
        .religiousBeliefToReligiousBeliefDTO(updatedReligiousBelief);

    mockMvc.perform(put("/api/religious-beliefs")
        .contentType(MediaType.APPLICATION_JSON)
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
    ReligiousBeliefDTO religiousBeliefDTO = religiousBeliefMapper
        .religiousBeliefToReligiousBeliefDTO(religiousBelief);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    mockMvc.perform(put("/api/religious-beliefs")
        .contentType(MediaType.APPLICATION_JSON)
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
    mockMvc
        .perform(delete("/api/religious-beliefs/{id}", religiousBelief.getId())
            .accept(MediaType.APPLICATION_JSON))
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

  @Test
  @Transactional
  public void shouldReturnFalseWhenNotExistsAndFilterNotApplied() throws Exception {
    mockMvc.perform(post(EXISTS_ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes("notExists_" + LocalDate.now())))
        .andExpect(status().isOk())
        .andExpect(content().string("false"));
  }

  @Test
  @Transactional
  public void shouldReturnTrueWhenExistsAndFilterNotApplied() throws Exception {
    religiousBeliefRepository.saveAndFlush(religiousBelief);

    mockMvc.perform(post(EXISTS_ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(DEFAULT_CODE)))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }

  @Test
  @Transactional
  public void shouldReturnFalseWhenNotExistsAndFilterApplied() throws Exception {
    String columnFilter = URLEncoder.encode("{\"status\":[\"CURRENT\"]}", CharEncoding.UTF_8);

    mockMvc.perform(post(EXISTS_ENDPOINT + "?columnFilters=" + columnFilter)
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes("notExists_" + LocalDate.now())))
        .andExpect(status().isOk())
        .andExpect(content().string("false"));
  }

  @Test
  @Transactional
  public void shouldReturnFalseWhenExistsAndFilterExcludes() throws Exception {
    religiousBelief.setStatus(Status.INACTIVE);
    religiousBeliefRepository.saveAndFlush(religiousBelief);

    String columnFilter = URLEncoder.encode("{\"status\":[\"CURRENT\"]}", CharEncoding.UTF_8);

    mockMvc.perform(post(EXISTS_ENDPOINT + "?columnFilters=" + columnFilter)
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(DEFAULT_CODE)))
        .andExpect(status().isOk())
        .andExpect(content().string("false"));
  }

  @Test
  @Transactional
  public void shouldReturnTrueWhenExistsAndFilterIncludes() throws Exception {
    religiousBelief.setStatus(Status.CURRENT);
    religiousBeliefRepository.saveAndFlush(religiousBelief);

    String columnFilter = URLEncoder.encode("{\"status\":[\"CURRENT\"]}", CharEncoding.UTF_8);

    mockMvc.perform(post(EXISTS_ENDPOINT + "?columnFilters=" + columnFilter)
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(DEFAULT_CODE)))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }
}
