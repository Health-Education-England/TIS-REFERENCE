package com.transformuk.hee.tis.reference.service.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.reference.api.dto.GdcStatusDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.GdcStatus;
import com.transformuk.hee.tis.reference.service.repository.GdcStatusRepository;
import com.transformuk.hee.tis.reference.service.service.impl.GdcStatusServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.GdcStatusMapper;
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
 * Test class for the GdcStatusResource REST controller.
 *
 * @see GdcStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GdcStatusResourceIntTest {

  private static final String EXISTS_ENDPOINT = "/api/gdc-statuses/exists/";

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "CCCCCCCCCC";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t GDC Status";

  @Autowired
  private GdcStatusRepository gdcStatusRepository;

  @Autowired
  private GdcStatusMapper gdcStatusMapper;
  @Autowired
  private GdcStatusServiceImpl gdcStatusService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc mockMvc;

  private GdcStatus gdcStatus;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static GdcStatus createEntity(EntityManager em) {
    GdcStatus gdcStatus = new GdcStatus();
    gdcStatus.setCode(DEFAULT_CODE);
    gdcStatus.setLabel(DEFAULT_LABEL);
    return gdcStatus;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    GdcStatusResource gdcStatusResource = new GdcStatusResource(gdcStatusRepository,
        gdcStatusMapper, gdcStatusService);
    this.mockMvc = MockMvcBuilders.standaloneSetup(gdcStatusResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    gdcStatus = createEntity(em);
  }

  @Test
  @Transactional
  public void createGdcStatus() throws Exception {
    int databaseSizeBeforeCreate = gdcStatusRepository.findAll().size();

    // Create the GdcStatus
    GdcStatusDTO gdcStatusDTO = gdcStatusMapper.gdcStatusToGdcStatusDTO(gdcStatus);
    mockMvc.perform(post("/api/gdc-statuses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(gdcStatusDTO)))
        .andExpect(status().isCreated());

    // Validate the GdcStatus in the database
    List<GdcStatus> gdcStatusList = gdcStatusRepository.findAll();
    assertThat(gdcStatusList).hasSize(databaseSizeBeforeCreate + 1);
    GdcStatus testGdcStatus = gdcStatusList.get(gdcStatusList.size() - 1);
    assertThat(testGdcStatus.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testGdcStatus.getLabel()).isEqualTo(DEFAULT_LABEL);
  }

  @Test
  @Transactional
  public void createGdcStatusWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = gdcStatusRepository.findAll().size();

    // Create the GdcStatus with an existing ID
    gdcStatus.setId(1L);
    GdcStatusDTO gdcStatusDTO = gdcStatusMapper.gdcStatusToGdcStatusDTO(gdcStatus);

    // An entity with an existing ID cannot be created, so this API call must fail
    mockMvc.perform(post("/api/gdc-statuses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(gdcStatusDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<GdcStatus> gdcStatusList = gdcStatusRepository.findAll();
    assertThat(gdcStatusList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = gdcStatusRepository.findAll().size();
    // set the field null
    gdcStatus.setCode(null);

    // Create the GdcStatus, which fails.
    GdcStatusDTO gdcStatusDTO = gdcStatusMapper.gdcStatusToGdcStatusDTO(gdcStatus);

    mockMvc.perform(post("/api/gdc-statuses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(gdcStatusDTO)))
        .andExpect(status().isBadRequest());

    List<GdcStatus> gdcStatusList = gdcStatusRepository.findAll();
    assertThat(gdcStatusList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLabelIsRequired() throws Exception {
    int databaseSizeBeforeTest = gdcStatusRepository.findAll().size();
    // set the field null
    gdcStatus.setLabel(null);

    // Create the GdcStatus, which fails.
    GdcStatusDTO gdcStatusDTO = gdcStatusMapper.gdcStatusToGdcStatusDTO(gdcStatus);

    mockMvc.perform(post("/api/gdc-statuses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(gdcStatusDTO)))
        .andExpect(status().isBadRequest());

    List<GdcStatus> gdcStatusList = gdcStatusRepository.findAll();
    assertThat(gdcStatusList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllGdcStatuses() throws Exception {
    // Initialize the database
    GdcStatus unencodedGdcStatus = new GdcStatus();
    unencodedGdcStatus.setCode(UNENCODED_CODE);
    unencodedGdcStatus.setLabel(UNENCODED_LABEL);
    gdcStatusRepository.saveAndFlush(unencodedGdcStatus);

    // Get all the gdcStatusList
    mockMvc.perform(get("/api/gdc-statuses?searchQuery=\"Te$t\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(unencodedGdcStatus.getId().intValue()))
        .andExpect(jsonPath("$.[*].code").value(UNENCODED_CODE))
        .andExpect(jsonPath("$.[*].label").value(UNENCODED_LABEL));
  }

  @Test
  @Transactional
  public void getGdcStatus() throws Exception {
    // Initialize the database
    gdcStatusRepository.saveAndFlush(gdcStatus);

    // Get the gdcStatus
    mockMvc.perform(get("/api/gdc-statuses/{id}", gdcStatus.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(gdcStatus.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingGdcStatus() throws Exception {
    // Get the gdcStatus
    mockMvc.perform(get("/api/gdc-statuses/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateGdcStatus() throws Exception {
    // Initialize the database
    gdcStatusRepository.saveAndFlush(gdcStatus);
    int databaseSizeBeforeUpdate = gdcStatusRepository.findAll().size();

    // Update the gdcStatus
    GdcStatus updatedGdcStatus = gdcStatusRepository.findById(gdcStatus.getId()).get();
    updatedGdcStatus.setCode(UPDATED_CODE);
    updatedGdcStatus.setLabel(UPDATED_LABEL);
    GdcStatusDTO gdcStatusDTO = gdcStatusMapper.gdcStatusToGdcStatusDTO(updatedGdcStatus);

    mockMvc.perform(put("/api/gdc-statuses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(gdcStatusDTO)))
        .andExpect(status().isOk());

    // Validate the GdcStatus in the database
    List<GdcStatus> gdcStatusList = gdcStatusRepository.findAll();
    assertThat(gdcStatusList).hasSize(databaseSizeBeforeUpdate);
    GdcStatus testGdcStatus = gdcStatusList.get(gdcStatusList.size() - 1);
    assertThat(testGdcStatus.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testGdcStatus.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  @Transactional
  public void updateNonExistingGdcStatus() throws Exception {
    int databaseSizeBeforeUpdate = gdcStatusRepository.findAll().size();

    // Create the GdcStatus
    GdcStatusDTO gdcStatusDTO = gdcStatusMapper.gdcStatusToGdcStatusDTO(gdcStatus);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    mockMvc.perform(put("/api/gdc-statuses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(gdcStatusDTO)))
        .andExpect(status().isCreated());

    // Validate the GdcStatus in the database
    List<GdcStatus> gdcStatusList = gdcStatusRepository.findAll();
    assertThat(gdcStatusList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteGdcStatus() throws Exception {
    // Initialize the database
    gdcStatusRepository.saveAndFlush(gdcStatus);
    int databaseSizeBeforeDelete = gdcStatusRepository.findAll().size();

    // Get the gdcStatus
    mockMvc.perform(delete("/api/gdc-statuses/{id}", gdcStatus.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<GdcStatus> gdcStatusList = gdcStatusRepository.findAll();
    assertThat(gdcStatusList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(GdcStatus.class);
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
    gdcStatusRepository.saveAndFlush(gdcStatus);

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
    gdcStatus.setStatus(Status.INACTIVE);
    gdcStatusRepository.saveAndFlush(gdcStatus);

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
    gdcStatus.setStatus(Status.CURRENT);
    gdcStatusRepository.saveAndFlush(gdcStatus);

    String columnFilter = URLEncoder.encode("{\"status\":[\"CURRENT\"]}", CharEncoding.UTF_8);

    mockMvc.perform(post(EXISTS_ENDPOINT + "?columnFilters=" + columnFilter)
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(DEFAULT_CODE)))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }
}
