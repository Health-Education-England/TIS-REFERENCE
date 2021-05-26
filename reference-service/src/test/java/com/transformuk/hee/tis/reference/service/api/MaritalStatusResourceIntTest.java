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

import com.transformuk.hee.tis.reference.api.dto.MaritalStatusDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.MaritalStatus;
import com.transformuk.hee.tis.reference.service.repository.MaritalStatusRepository;
import com.transformuk.hee.tis.reference.service.service.impl.MaritalStatusServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.MaritalStatusMapper;
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
 * Test class for the MaritalStatusResource REST controller.
 *
 * @see MaritalStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MaritalStatusResourceIntTest {

  private static final String EXISTS_ENDPOINT = "/api/marital-statuses/exists/";

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "CCCCCCCCCC";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t Marital Status";

  @Autowired
  private MaritalStatusRepository maritalStatusRepository;

  @Autowired
  private MaritalStatusMapper maritalStatusMapper;

  @Autowired
  private MaritalStatusServiceImpl maritalStatusService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc mockMvc;

  private MaritalStatus maritalStatus;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static MaritalStatus createEntity(EntityManager em) {
    MaritalStatus maritalStatus = new MaritalStatus()
        .code(DEFAULT_CODE)
        .label(DEFAULT_LABEL);
    return maritalStatus;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    MaritalStatusResource maritalStatusResource = new MaritalStatusResource(maritalStatusRepository,
        maritalStatusMapper,
        maritalStatusService);
    this.mockMvc = MockMvcBuilders.standaloneSetup(maritalStatusResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    maritalStatus = createEntity(em);
  }

  @Test
  @Transactional
  public void createMaritalStatus() throws Exception {
    int databaseSizeBeforeCreate = maritalStatusRepository.findAll().size();

    // Create the MaritalStatus
    MaritalStatusDTO maritalStatusDTO = maritalStatusMapper
        .maritalStatusToMaritalStatusDTO(maritalStatus);
    mockMvc.perform(post("/api/marital-statuses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(maritalStatusDTO)))
        .andExpect(status().isCreated());

    // Validate the MaritalStatus in the database
    List<MaritalStatus> maritalStatusList = maritalStatusRepository.findAll();
    assertThat(maritalStatusList).hasSize(databaseSizeBeforeCreate + 1);
    MaritalStatus testMaritalStatus = maritalStatusList.get(maritalStatusList.size() - 1);
    assertThat(testMaritalStatus.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testMaritalStatus.getLabel()).isEqualTo(DEFAULT_LABEL);
  }

  @Test
  @Transactional
  public void createMaritalStatusWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = maritalStatusRepository.findAll().size();

    // Create the MaritalStatus with an existing ID
    maritalStatus.setId(1L);
    MaritalStatusDTO maritalStatusDTO = maritalStatusMapper
        .maritalStatusToMaritalStatusDTO(maritalStatus);

    // An entity with an existing ID cannot be created, so this API call must fail
    mockMvc.perform(post("/api/marital-statuses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(maritalStatusDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<MaritalStatus> maritalStatusList = maritalStatusRepository.findAll();
    assertThat(maritalStatusList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = maritalStatusRepository.findAll().size();
    // set the field null
    maritalStatus.setCode(null);

    // Create the MaritalStatus, which fails.
    MaritalStatusDTO maritalStatusDTO = maritalStatusMapper
        .maritalStatusToMaritalStatusDTO(maritalStatus);

    mockMvc.perform(post("/api/marital-statuses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(maritalStatusDTO)))
        .andExpect(status().isBadRequest());

    List<MaritalStatus> maritalStatusList = maritalStatusRepository.findAll();
    assertThat(maritalStatusList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLabelIsRequired() throws Exception {
    int databaseSizeBeforeTest = maritalStatusRepository.findAll().size();
    // set the field null
    maritalStatus.setLabel(null);

    // Create the MaritalStatus, which fails.
    MaritalStatusDTO maritalStatusDTO = maritalStatusMapper
        .maritalStatusToMaritalStatusDTO(maritalStatus);

    mockMvc.perform(post("/api/marital-statuses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(maritalStatusDTO)))
        .andExpect(status().isBadRequest());

    List<MaritalStatus> maritalStatusList = maritalStatusRepository.findAll();
    assertThat(maritalStatusList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllMaritalStatuses() throws Exception {
    // Initialize the database
    maritalStatusRepository.saveAndFlush(maritalStatus);

    // Get all the maritalStatusList
    mockMvc.perform(get("/api/marital-statuses?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(maritalStatus.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
  }

  @Test
  @Transactional
  public void getMaritalStatusesWithQuery() throws Exception {
    // Initialize the database
    MaritalStatus unencodedMaritalStatus = new MaritalStatus()
        .code(UNENCODED_CODE)
        .label(UNENCODED_LABEL);
    maritalStatusRepository.saveAndFlush(unencodedMaritalStatus);

    // Get all the maritalStatusList
    mockMvc
        .perform(get("/api/marital-statuses?searchQuery=\"Te%24t\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(unencodedMaritalStatus.getId().intValue()))
        .andExpect(jsonPath("$.[*].code").value(UNENCODED_CODE))
        .andExpect(jsonPath("$.[*].label").value(UNENCODED_LABEL));
  }

  @Test
  @Transactional
  public void getMaritalStatus() throws Exception {
    // Initialize the database
    maritalStatusRepository.saveAndFlush(maritalStatus);

    // Get the maritalStatus
    mockMvc.perform(get("/api/marital-statuses/{id}", maritalStatus.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(maritalStatus.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingMaritalStatus() throws Exception {
    // Get the maritalStatus
    mockMvc.perform(get("/api/marital-statuses/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateMaritalStatus() throws Exception {
    // Initialize the database
    maritalStatusRepository.saveAndFlush(maritalStatus);
    int databaseSizeBeforeUpdate = maritalStatusRepository.findAll().size();

    // Update the maritalStatus
    MaritalStatus updatedMaritalStatus = maritalStatusRepository.findById(maritalStatus.getId()).get();
    updatedMaritalStatus
        .code(UPDATED_CODE)
        .label(UPDATED_LABEL);
    MaritalStatusDTO maritalStatusDTO = maritalStatusMapper
        .maritalStatusToMaritalStatusDTO(updatedMaritalStatus);

    mockMvc.perform(put("/api/marital-statuses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(maritalStatusDTO)))
        .andExpect(status().isOk());

    // Validate the MaritalStatus in the database
    List<MaritalStatus> maritalStatusList = maritalStatusRepository.findAll();
    assertThat(maritalStatusList).hasSize(databaseSizeBeforeUpdate);
    MaritalStatus testMaritalStatus = maritalStatusList.get(maritalStatusList.size() - 1);
    assertThat(testMaritalStatus.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testMaritalStatus.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  @Transactional
  public void updateNonExistingMaritalStatus() throws Exception {
    int databaseSizeBeforeUpdate = maritalStatusRepository.findAll().size();

    // Create the MaritalStatus
    MaritalStatusDTO maritalStatusDTO = maritalStatusMapper
        .maritalStatusToMaritalStatusDTO(maritalStatus);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    mockMvc.perform(put("/api/marital-statuses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(maritalStatusDTO)))
        .andExpect(status().isCreated());

    // Validate the MaritalStatus in the database
    List<MaritalStatus> maritalStatusList = maritalStatusRepository.findAll();
    assertThat(maritalStatusList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteMaritalStatus() throws Exception {
    // Initialize the database
    maritalStatusRepository.saveAndFlush(maritalStatus);
    int databaseSizeBeforeDelete = maritalStatusRepository.findAll().size();

    // Get the maritalStatus
    mockMvc.perform(delete("/api/marital-statuses/{id}", maritalStatus.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<MaritalStatus> maritalStatusList = maritalStatusRepository.findAll();
    assertThat(maritalStatusList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(MaritalStatus.class);
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
    maritalStatusRepository.saveAndFlush(maritalStatus);

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
    maritalStatus.setStatus(Status.INACTIVE);
    maritalStatusRepository.saveAndFlush(maritalStatus);

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
    maritalStatus.setStatus(Status.CURRENT);
    maritalStatusRepository.saveAndFlush(maritalStatus);

    String columnFilter = URLEncoder.encode("{\"status\":[\"CURRENT\"]}", CharEncoding.UTF_8);

    mockMvc.perform(post(EXISTS_ENDPOINT + "?columnFilters=" + columnFilter)
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(DEFAULT_CODE)))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }
}
