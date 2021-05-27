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

import com.transformuk.hee.tis.reference.api.dto.GmcStatusDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.GmcStatus;
import com.transformuk.hee.tis.reference.service.repository.GmcStatusRepository;
import com.transformuk.hee.tis.reference.service.service.impl.GmcStatusServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.GmcStatusMapper;
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
 * Test class for the GmcStatusResource REST controller.
 *
 * @see GmcStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GmcStatusResourceIntTest {

  private static final String EXISTS_ENDPOINT = "/api/gmc-statuses/exists/";

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "Te$t Code";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t Label";

  @Autowired
  private GmcStatusRepository gmcStatusRepository;

  @Autowired
  private GmcStatusMapper gmcStatusMapper;

  @Autowired
  private GmcStatusServiceImpl gmcStatusService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc mockMvc;

  private GmcStatus gmcStatus;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static GmcStatus createEntity(EntityManager em) {
    GmcStatus gmcStatus = new GmcStatus();
    gmcStatus.setCode(DEFAULT_CODE);
    gmcStatus.setLabel(DEFAULT_LABEL);
    return gmcStatus;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    GmcStatusResource gmcStatusResource = new GmcStatusResource(gmcStatusRepository,
        gmcStatusMapper, gmcStatusService);
    this.mockMvc = MockMvcBuilders.standaloneSetup(gmcStatusResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    gmcStatus = createEntity(em);
  }

  @Test
  @Transactional
  public void createGmcStatus() throws Exception {
    int databaseSizeBeforeCreate = gmcStatusRepository.findAll().size();

    // Create the GmcStatus
    GmcStatusDTO gmcStatusDTO = gmcStatusMapper.gmcStatusToGmcStatusDTO(gmcStatus);
    mockMvc.perform(post("/api/gmc-statuses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(gmcStatusDTO)))
        .andExpect(status().isCreated());

    // Validate the GmcStatus in the database
    List<GmcStatus> gmcStatusList = gmcStatusRepository.findAll();
    assertThat(gmcStatusList).hasSize(databaseSizeBeforeCreate + 1);
    GmcStatus testGmcStatus = gmcStatusList.get(gmcStatusList.size() - 1);
    assertThat(testGmcStatus.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testGmcStatus.getLabel()).isEqualTo(DEFAULT_LABEL);
  }

  @Test
  @Transactional
  public void createGmcStatusWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = gmcStatusRepository.findAll().size();

    // Create the GmcStatus with an existing ID
    gmcStatus.setId(1L);
    GmcStatusDTO gmcStatusDTO = gmcStatusMapper.gmcStatusToGmcStatusDTO(gmcStatus);

    // An entity with an existing ID cannot be created, so this API call must fail
    mockMvc.perform(post("/api/gmc-statuses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(gmcStatusDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<GmcStatus> gmcStatusList = gmcStatusRepository.findAll();
    assertThat(gmcStatusList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = gmcStatusRepository.findAll().size();
    // set the field null
    gmcStatus.setCode(null);

    // Create the GmcStatus, which fails.
    GmcStatusDTO gmcStatusDTO = gmcStatusMapper.gmcStatusToGmcStatusDTO(gmcStatus);

    mockMvc.perform(post("/api/gmc-statuses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(gmcStatusDTO)))
        .andExpect(status().isBadRequest());

    List<GmcStatus> gmcStatusList = gmcStatusRepository.findAll();
    assertThat(gmcStatusList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLabelIsRequired() throws Exception {
    int databaseSizeBeforeTest = gmcStatusRepository.findAll().size();
    // set the field null
    gmcStatus.setLabel(null);

    // Create the GmcStatus, which fails.
    GmcStatusDTO gmcStatusDTO = gmcStatusMapper.gmcStatusToGmcStatusDTO(gmcStatus);

    mockMvc.perform(post("/api/gmc-statuses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(gmcStatusDTO)))
        .andExpect(status().isBadRequest());

    List<GmcStatus> gmcStatusList = gmcStatusRepository.findAll();
    assertThat(gmcStatusList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllGmcStatuses() throws Exception {
    // Initialize the database
    gmcStatusRepository.saveAndFlush(gmcStatus);

    // Get all the gmcStatusList
    mockMvc.perform(get("/api/gmc-statuses?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(gmcStatus.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
  }

  @Test
  @Transactional
  public void getEntitiesWithQueryMatchingCode() throws Exception {
    // Initialize the database.
    GmcStatus entity = new GmcStatus();
    entity.setCode(UNENCODED_CODE);
    entity.setLabel(DEFAULT_LABEL);
    gmcStatusRepository.saveAndFlush(entity);

    // Get all the matching entities.
    mockMvc.perform(get("/api/gmc-statuses?searchQuery=\"Te%24t\"&sort=id,desc"))
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
    GmcStatus entity = new GmcStatus();
    entity.setCode(DEFAULT_CODE);
    entity.setLabel(UNENCODED_LABEL);
    gmcStatusRepository.saveAndFlush(entity);

    // Get all the matching entities.
    mockMvc.perform(get("/api/gmc-statuses?searchQuery=\"Te%24t\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(entity.getId().intValue()))
        .andExpect(jsonPath("$.[*].code").value(DEFAULT_CODE))
        .andExpect(jsonPath("$.[*].label").value(UNENCODED_LABEL));
  }

  @Test
  @Transactional
  public void getGmcStatus() throws Exception {
    // Initialize the database
    gmcStatusRepository.saveAndFlush(gmcStatus);

    // Get the gmcStatus
    mockMvc.perform(get("/api/gmc-statuses/{id}", gmcStatus.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(gmcStatus.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingGmcStatus() throws Exception {
    // Get the gmcStatus
    mockMvc.perform(get("/api/gmc-statuses/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateGmcStatus() throws Exception {
    // Initialize the database
    gmcStatusRepository.saveAndFlush(gmcStatus);
    int databaseSizeBeforeUpdate = gmcStatusRepository.findAll().size();

    // Update the gmcStatus
    GmcStatus updatedGmcStatus = gmcStatusRepository.findById(gmcStatus.getId()).get();
    updatedGmcStatus.setCode(UPDATED_CODE);
    updatedGmcStatus.setLabel(UPDATED_LABEL);
    GmcStatusDTO gmcStatusDTO = gmcStatusMapper.gmcStatusToGmcStatusDTO(updatedGmcStatus);

    mockMvc.perform(put("/api/gmc-statuses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(gmcStatusDTO)))
        .andExpect(status().isOk());

    // Validate the GmcStatus in the database
    List<GmcStatus> gmcStatusList = gmcStatusRepository.findAll();
    assertThat(gmcStatusList).hasSize(databaseSizeBeforeUpdate);
    GmcStatus testGmcStatus = gmcStatusList.get(gmcStatusList.size() - 1);
    assertThat(testGmcStatus.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testGmcStatus.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  @Transactional
  public void updateNonExistingGmcStatus() throws Exception {
    int databaseSizeBeforeUpdate = gmcStatusRepository.findAll().size();

    // Create the GmcStatus
    GmcStatusDTO gmcStatusDTO = gmcStatusMapper.gmcStatusToGmcStatusDTO(gmcStatus);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    mockMvc.perform(put("/api/gmc-statuses")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(gmcStatusDTO)))
        .andExpect(status().isCreated());

    // Validate the GmcStatus in the database
    List<GmcStatus> gmcStatusList = gmcStatusRepository.findAll();
    assertThat(gmcStatusList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteGmcStatus() throws Exception {
    // Initialize the database
    gmcStatusRepository.saveAndFlush(gmcStatus);
    int databaseSizeBeforeDelete = gmcStatusRepository.findAll().size();

    // Get the gmcStatus
    mockMvc.perform(delete("/api/gmc-statuses/{id}", gmcStatus.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<GmcStatus> gmcStatusList = gmcStatusRepository.findAll();
    assertThat(gmcStatusList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(GmcStatus.class);
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
    gmcStatusRepository.saveAndFlush(gmcStatus);

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
    gmcStatus.setStatus(Status.INACTIVE);
    gmcStatusRepository.saveAndFlush(gmcStatus);

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
    gmcStatus.setStatus(Status.CURRENT);
    gmcStatusRepository.saveAndFlush(gmcStatus);

    String columnFilter = URLEncoder.encode("{\"status\":[\"CURRENT\"]}", CharEncoding.UTF_8);

    mockMvc.perform(post(EXISTS_ENDPOINT + "?columnFilters=" + columnFilter)
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(DEFAULT_CODE)))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }
}
