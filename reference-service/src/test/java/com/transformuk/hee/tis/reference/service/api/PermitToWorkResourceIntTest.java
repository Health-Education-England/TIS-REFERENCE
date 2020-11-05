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

import com.transformuk.hee.tis.reference.api.dto.PermitToWorkDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.PermitToWork;
import com.transformuk.hee.tis.reference.service.repository.PermitToWorkRepository;
import com.transformuk.hee.tis.reference.service.service.impl.PermitToWorkServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.PermitToWorkMapper;
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
 * Test class for the PermitToWorkResource REST controller.
 *
 * @see PermitToWorkResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PermitToWorkResourceIntTest {

  private static final String EXISTS_ENDPOINT = "/api/permit-to-works/exists/";

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "CCCCCCCCCC";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t Permit";

  @Autowired
  private PermitToWorkRepository permitToWorkRepository;

  @Autowired
  private PermitToWorkMapper permitToWorkMapper;

  @Autowired
  private PermitToWorkServiceImpl permitToWorkService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc mockMvc;

  private PermitToWork permitToWork;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static PermitToWork createEntity() {
    PermitToWork permitToWork = new PermitToWork()
        .code(DEFAULT_CODE)
        .label(DEFAULT_LABEL);
    return permitToWork;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    PermitToWorkResource permitToWorkResource = new PermitToWorkResource(permitToWorkRepository,
        permitToWorkMapper,
        permitToWorkService);
    this.mockMvc = MockMvcBuilders.standaloneSetup(permitToWorkResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    permitToWork = createEntity();
  }

  @Test
  @Transactional
  public void createPermitToWork() throws Exception {
    int databaseSizeBeforeCreate = permitToWorkRepository.findAll().size();

    // Create the PermitToWork
    PermitToWorkDTO permitToWorkDTO = permitToWorkMapper
        .permitToWorkToPermitToWorkDTO(permitToWork);
    mockMvc.perform(post("/api/permit-to-works")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(permitToWorkDTO)))
        .andExpect(status().isCreated());

    // Validate the PermitToWork in the database
    List<PermitToWork> maritalStatusList = permitToWorkRepository.findAll();
    assertThat(maritalStatusList).hasSize(databaseSizeBeforeCreate + 1);
    PermitToWork testPermitToWork = maritalStatusList.get(maritalStatusList.size() - 1);
    assertThat(testPermitToWork.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testPermitToWork.getLabel()).isEqualTo(DEFAULT_LABEL);
  }

  @Test
  @Transactional
  public void createPermitToWorkWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = permitToWorkRepository.findAll().size();

    // Create the PermitToWork with an existing ID
    permitToWork.setId(1L);
    PermitToWorkDTO maritalStatusDTO = permitToWorkMapper
        .permitToWorkToPermitToWorkDTO(permitToWork);

    // An entity with an existing ID cannot be created, so this API call must fail
    mockMvc.perform(post("/api/permit-to-works")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(maritalStatusDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<PermitToWork> maritalStatusList = permitToWorkRepository.findAll();
    assertThat(maritalStatusList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = permitToWorkRepository.findAll().size();
    // set the field null
    permitToWork.setCode(null);

    // Create the PermitToWork, which fails.
    PermitToWorkDTO maritalStatusDTO = permitToWorkMapper
        .permitToWorkToPermitToWorkDTO(permitToWork);

    mockMvc.perform(post("/api/permit-to-works")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(maritalStatusDTO)))
        .andExpect(status().isBadRequest());

    List<PermitToWork> maritalStatusList = permitToWorkRepository.findAll();
    assertThat(maritalStatusList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLabelIsRequired() throws Exception {
    int databaseSizeBeforeTest = permitToWorkRepository.findAll().size();
    // set the field null
    permitToWork.setLabel(null);

    // Create the PermitToWork, which fails.
    PermitToWorkDTO maritalStatusDTO = permitToWorkMapper
        .permitToWorkToPermitToWorkDTO(permitToWork);

    mockMvc.perform(post("/api/permit-to-works")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(maritalStatusDTO)))
        .andExpect(status().isBadRequest());

    List<PermitToWork> maritalStatusList = permitToWorkRepository.findAll();
    assertThat(maritalStatusList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllPermitToWorks() throws Exception {
    // Initialize the database
    permitToWorkRepository.saveAndFlush(permitToWork);

    // Get all the permitToWorkList
    mockMvc.perform(get("/api/permit-to-works?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(permitToWork.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
  }

  @Test
  @Transactional
  public void getPermitToWorksWithQuery() throws Exception {
    // Initialize the database
    PermitToWork unencodedPermitToWork = new PermitToWork()
        .code(UNENCODED_CODE)
        .label(UNENCODED_LABEL);
    permitToWorkRepository.saveAndFlush(unencodedPermitToWork);

    // Get the permitToWorkList
    mockMvc.perform(get("/api/permit-to-works?searchQuery=\"Te%24t\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(unencodedPermitToWork.getId().intValue()))
        .andExpect(jsonPath("$.[*].code").value(UNENCODED_CODE))
        .andExpect(jsonPath("$.[*].label").value(UNENCODED_LABEL));
  }

  @Test
  @Transactional
  public void getPermitToWork() throws Exception {
    // Initialize the database
    permitToWorkRepository.saveAndFlush(permitToWork);

    // Get the maritalStatus
    mockMvc.perform(get("/api/permit-to-works/{id}", permitToWork.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(permitToWork.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingPermitToWork() throws Exception {
    // Get the maritalStatus
    mockMvc.perform(get("/api/permit-to-works/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updatePermitToWork() throws Exception {
    // Initialize the database
    permitToWorkRepository.saveAndFlush(permitToWork);
    int databaseSizeBeforeUpdate = permitToWorkRepository.findAll().size();

    // Update the maritalStatus
    PermitToWork updatedPermitToWork = permitToWorkRepository.findById(permitToWork.getId()).get();
    updatedPermitToWork
        .code(UPDATED_CODE)
        .label(UPDATED_LABEL);
    PermitToWorkDTO maritalStatusDTO = permitToWorkMapper
        .permitToWorkToPermitToWorkDTO(updatedPermitToWork);

    mockMvc.perform(put("/api/permit-to-works")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(maritalStatusDTO)))
        .andExpect(status().isOk());

    // Validate the PermitToWork in the database
    List<PermitToWork> maritalStatusList = permitToWorkRepository.findAll();
    assertThat(maritalStatusList).hasSize(databaseSizeBeforeUpdate);
    PermitToWork testPermitToWork = maritalStatusList.get(maritalStatusList.size() - 1);
    assertThat(testPermitToWork.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testPermitToWork.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  @Transactional
  public void updateNonExistingPermitToWork() throws Exception {
    int databaseSizeBeforeUpdate = permitToWorkRepository.findAll().size();

    // Create the PermitToWork
    PermitToWorkDTO maritalStatusDTO = permitToWorkMapper
        .permitToWorkToPermitToWorkDTO(permitToWork);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    mockMvc.perform(put("/api/permit-to-works")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(maritalStatusDTO)))
        .andExpect(status().isCreated());

    // Validate the PermitToWork in the database
    List<PermitToWork> maritalStatusList = permitToWorkRepository.findAll();
    assertThat(maritalStatusList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deletePermitToWork() throws Exception {
    // Initialize the database
    permitToWorkRepository.saveAndFlush(permitToWork);
    int databaseSizeBeforeDelete = permitToWorkRepository.findAll().size();

    // Get the permitToWork
    mockMvc.perform(delete("/api/permit-to-works/{id}", permitToWork.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<PermitToWork> maritalStatusList = permitToWorkRepository.findAll();
    assertThat(maritalStatusList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(PermitToWork.class);
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
    permitToWorkRepository.saveAndFlush(permitToWork);

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
    permitToWork.setStatus(Status.INACTIVE);
    permitToWorkRepository.saveAndFlush(permitToWork);

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
    permitToWork.setStatus(Status.CURRENT);
    permitToWorkRepository.saveAndFlush(permitToWork);

    String columnFilter = URLEncoder.encode("{\"status\":[\"CURRENT\"]}", CharEncoding.UTF_8);

    mockMvc.perform(post(EXISTS_ENDPOINT + "?columnFilters=" + columnFilter)
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(DEFAULT_CODE)))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }
}
