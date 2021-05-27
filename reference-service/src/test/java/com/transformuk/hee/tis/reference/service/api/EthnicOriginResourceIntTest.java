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

import com.transformuk.hee.tis.reference.api.dto.EthnicOriginDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.EthnicOrigin;
import com.transformuk.hee.tis.reference.service.repository.EthnicOriginRepository;
import com.transformuk.hee.tis.reference.service.service.impl.EthnicOriginServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.EthnicOriginMapper;
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
 * Test class for the EthnicOriginResource REST controller.
 *
 * @see EthnicOriginResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class EthnicOriginResourceIntTest {

  private static final String EXISTS_ENDPOINT = "/api/ethnic-origins/exists/";

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "$omeEthnicity";

  @Autowired
  private EthnicOriginRepository ethnicOriginRepository;

  @Autowired
  private EthnicOriginMapper ethnicOriginMapper;

  @Autowired
  private EthnicOriginServiceImpl ethnicOriginService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc mockMvc;

  private EthnicOrigin ethnicOrigin;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static EthnicOrigin createEntity(EntityManager em) {
    EthnicOrigin ethnicOrigin = new EthnicOrigin();
    ethnicOrigin.setCode(DEFAULT_CODE);
    return ethnicOrigin;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    EthnicOriginResource ethnicOriginResource = new EthnicOriginResource(ethnicOriginRepository,
        ethnicOriginMapper,
        ethnicOriginService);
    this.mockMvc = MockMvcBuilders.standaloneSetup(ethnicOriginResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    ethnicOrigin = createEntity(em);
  }

  @Test
  @Transactional
  public void createEthnicOrigin() throws Exception {
    int databaseSizeBeforeCreate = ethnicOriginRepository.findAll().size();

    // Create the EthnicOrigin
    EthnicOriginDTO ethnicOriginDTO = ethnicOriginMapper
        .ethnicOriginToEthnicOriginDTO(ethnicOrigin);
    mockMvc.perform(post("/api/ethnic-origins")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(ethnicOriginDTO)))
        .andExpect(status().isCreated());

    // Validate the EthnicOrigin in the database
    List<EthnicOrigin> ethnicOriginList = ethnicOriginRepository.findAll();
    assertThat(ethnicOriginList).hasSize(databaseSizeBeforeCreate + 1);
    EthnicOrigin testEthnicOrigin = ethnicOriginList.get(ethnicOriginList.size() - 1);
    assertThat(testEthnicOrigin.getCode()).isEqualTo(DEFAULT_CODE);
  }

  @Test
  @Transactional
  public void createEthnicOriginWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = ethnicOriginRepository.findAll().size();

    // Create the EthnicOrigin with an existing ID
    ethnicOrigin.setId(1L);
    EthnicOriginDTO ethnicOriginDTO = ethnicOriginMapper
        .ethnicOriginToEthnicOriginDTO(ethnicOrigin);

    // An entity with an existing ID cannot be created, so this API call must fail
    mockMvc.perform(post("/api/ethnic-origins")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(ethnicOriginDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<EthnicOrigin> ethnicOriginList = ethnicOriginRepository.findAll();
    assertThat(ethnicOriginList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = ethnicOriginRepository.findAll().size();
    // set the field null
    ethnicOrigin.setCode(null);

    // Create the EthnicOrigin, which fails.
    EthnicOriginDTO ethnicOriginDTO = ethnicOriginMapper
        .ethnicOriginToEthnicOriginDTO(ethnicOrigin);

    mockMvc.perform(post("/api/ethnic-origins")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(ethnicOriginDTO)))
        .andExpect(status().isBadRequest());

    List<EthnicOrigin> ethnicOriginList = ethnicOriginRepository.findAll();
    assertThat(ethnicOriginList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllEthnicOrigins() throws Exception {
    // Initialize the database
    ethnicOriginRepository.saveAndFlush(ethnicOrigin);

    // Get all the ethnicOriginList
    mockMvc.perform(get("/api/ethnic-origins?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(ethnicOrigin.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
  }

  @Test
  @Transactional
  public void getEthnicOriginsWithQuery() throws Exception {
    // Initialize the database
    EthnicOrigin unencodedEthnicOrigin = new EthnicOrigin();
    unencodedEthnicOrigin.setCode(UNENCODED_CODE);
    ethnicOriginRepository.saveAndFlush(unencodedEthnicOrigin);

    // Get all the ethnicOriginList
    mockMvc.perform(get("/api/ethnic-origins?searchQuery=\"%24ome\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(unencodedEthnicOrigin.getId().intValue()))
        .andExpect(jsonPath("$.[*].code").value(UNENCODED_CODE));
  }

  @Test
  @Transactional
  public void getEthnicOrigin() throws Exception {
    // Initialize the database
    ethnicOriginRepository.saveAndFlush(ethnicOrigin);

    // Get the ethnicOrigin
    mockMvc.perform(get("/api/ethnic-origins/{id}", ethnicOrigin.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(ethnicOrigin.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingEthnicOrigin() throws Exception {
    // Get the ethnicOrigin
    mockMvc.perform(get("/api/ethnic-origins/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateEthnicOrigin() throws Exception {
    // Initialize the database
    ethnicOriginRepository.saveAndFlush(ethnicOrigin);
    int databaseSizeBeforeUpdate = ethnicOriginRepository.findAll().size();

    // Update the ethnicOrigin
    EthnicOrigin updatedEthnicOrigin = ethnicOriginRepository.findById(ethnicOrigin.getId()).get();
    updatedEthnicOrigin.setCode(UPDATED_CODE);
    EthnicOriginDTO ethnicOriginDTO = ethnicOriginMapper
        .ethnicOriginToEthnicOriginDTO(updatedEthnicOrigin);

    mockMvc.perform(put("/api/ethnic-origins")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(ethnicOriginDTO)))
        .andExpect(status().isOk());

    // Validate the EthnicOrigin in the database
    List<EthnicOrigin> ethnicOriginList = ethnicOriginRepository.findAll();
    assertThat(ethnicOriginList).hasSize(databaseSizeBeforeUpdate);
    EthnicOrigin testEthnicOrigin = ethnicOriginList.get(ethnicOriginList.size() - 1);
    assertThat(testEthnicOrigin.getCode()).isEqualTo(UPDATED_CODE);
  }

  @Test
  @Transactional
  public void updateNonExistingEthnicOrigin() throws Exception {
    int databaseSizeBeforeUpdate = ethnicOriginRepository.findAll().size();

    // Create the EthnicOrigin
    EthnicOriginDTO ethnicOriginDTO = ethnicOriginMapper
        .ethnicOriginToEthnicOriginDTO(ethnicOrigin);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    mockMvc.perform(put("/api/ethnic-origins")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(ethnicOriginDTO)))
        .andExpect(status().isCreated());

    // Validate the EthnicOrigin in the database
    List<EthnicOrigin> ethnicOriginList = ethnicOriginRepository.findAll();
    assertThat(ethnicOriginList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteEthnicOrigin() throws Exception {
    // Initialize the database
    ethnicOriginRepository.saveAndFlush(ethnicOrigin);
    int databaseSizeBeforeDelete = ethnicOriginRepository.findAll().size();

    // Get the ethnicOrigin
    mockMvc.perform(delete("/api/ethnic-origins/{id}", ethnicOrigin.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<EthnicOrigin> ethnicOriginList = ethnicOriginRepository.findAll();
    assertThat(ethnicOriginList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(EthnicOrigin.class);
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
    ethnicOriginRepository.saveAndFlush(ethnicOrigin);

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
    ethnicOrigin.setStatus(Status.INACTIVE);
    ethnicOriginRepository.saveAndFlush(ethnicOrigin);

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
    ethnicOrigin.setStatus(Status.CURRENT);
    ethnicOriginRepository.saveAndFlush(ethnicOrigin);

    String columnFilter = URLEncoder.encode("{\"status\":[\"CURRENT\"]}", CharEncoding.UTF_8);

    mockMvc.perform(post(EXISTS_ENDPOINT + "?columnFilters=" + columnFilter)
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(DEFAULT_CODE)))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }
}
