package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.api.dto.SettledDTO;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.Settled;
import com.transformuk.hee.tis.reference.service.repository.SettledRepository;
import com.transformuk.hee.tis.reference.service.service.impl.SettledServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.SettledMapper;
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
 * Test class for the SettledResource REST controller.
 *
 * @see SettledResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SettledResourceIntTest {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "CCCCCCCCCC";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t Settled";

  @Autowired
  private SettledRepository settledRepository;

  @Autowired
  private SettledMapper settledMapper;

  @Autowired
  private SettledServiceImpl settledService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restSettledMockMvc;

  private Settled settled;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Settled createEntity(EntityManager em) {
    Settled settled = new Settled()
        .code(DEFAULT_CODE)
        .label(DEFAULT_LABEL);
    return settled;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    SettledResource settledResource = new SettledResource(settledRepository, settledMapper, settledService);
    this.restSettledMockMvc = MockMvcBuilders.standaloneSetup(settledResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    settled = createEntity(em);
  }

  @Test
  @Transactional
  public void createSettled() throws Exception {
    int databaseSizeBeforeCreate = settledRepository.findAll().size();

    // Create the Settled
    SettledDTO settledDTO = settledMapper.settledToSettledDTO(settled);
    restSettledMockMvc.perform(post("/api/settleds")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(settledDTO)))
        .andExpect(status().isCreated());

    // Validate the Settled in the database
    List<Settled> settledList = settledRepository.findAll();
    assertThat(settledList).hasSize(databaseSizeBeforeCreate + 1);
    Settled testSettled = settledList.get(settledList.size() - 1);
    assertThat(testSettled.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testSettled.getLabel()).isEqualTo(DEFAULT_LABEL);
  }

  @Test
  @Transactional
  public void createSettledWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = settledRepository.findAll().size();

    // Create the Settled with an existing ID
    settled.setId(1L);
    SettledDTO settledDTO = settledMapper.settledToSettledDTO(settled);

    // An entity with an existing ID cannot be created, so this API call must fail
    restSettledMockMvc.perform(post("/api/settleds")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(settledDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<Settled> settledList = settledRepository.findAll();
    assertThat(settledList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = settledRepository.findAll().size();
    // set the field null
    settled.setCode(null);

    // Create the Settled, which fails.
    SettledDTO settledDTO = settledMapper.settledToSettledDTO(settled);

    restSettledMockMvc.perform(post("/api/settleds")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(settledDTO)))
        .andExpect(status().isBadRequest());

    List<Settled> settledList = settledRepository.findAll();
    assertThat(settledList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLabelIsRequired() throws Exception {
    int databaseSizeBeforeTest = settledRepository.findAll().size();
    // set the field null
    settled.setLabel(null);

    // Create the Settled, which fails.
    SettledDTO settledDTO = settledMapper.settledToSettledDTO(settled);

    restSettledMockMvc.perform(post("/api/settleds")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(settledDTO)))
        .andExpect(status().isBadRequest());

    List<Settled> settledList = settledRepository.findAll();
    assertThat(settledList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllSettleds() throws Exception {
    // Initialize the database
    settledRepository.saveAndFlush(settled);

    // Get all the settledList
    restSettledMockMvc.perform(get("/api/settleds?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(settled.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
  }
  
  @Test
  @Transactional
  public void getSettledsWithQuery() throws Exception {
    // Initialize the database
    Settled unencodedSettled = new Settled()
        .code(UNENCODED_CODE)
        .label(UNENCODED_LABEL);
    settledRepository.saveAndFlush(unencodedSettled);
    
    // Get all the settledList
    restSettledMockMvc.perform(get("/api/settleds?searchQuery=%22Te%24t%22&sort=id,desc"))
    .andExpect(status().isOk())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    .andExpect(jsonPath("$.[*].id").value(hasItem(unencodedSettled.getId().intValue())))
    .andExpect(jsonPath("$.[*].code").value(UNENCODED_CODE))
    .andExpect(jsonPath("$.[*].label").value(UNENCODED_LABEL));
  }

  @Test
  @Transactional
  public void getSettled() throws Exception {
    // Initialize the database
    settledRepository.saveAndFlush(settled);

    // Get the settled
    restSettledMockMvc.perform(get("/api/settleds/{id}", settled.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(settled.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingSettled() throws Exception {
    // Get the settled
    restSettledMockMvc.perform(get("/api/settleds/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateSettled() throws Exception {
    // Initialize the database
    settledRepository.saveAndFlush(settled);
    int databaseSizeBeforeUpdate = settledRepository.findAll().size();

    // Update the settled
    Settled updatedSettled = settledRepository.findOne(settled.getId());
    updatedSettled
        .code(UPDATED_CODE)
        .label(UPDATED_LABEL);
    SettledDTO settledDTO = settledMapper.settledToSettledDTO(updatedSettled);

    restSettledMockMvc.perform(put("/api/settleds")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(settledDTO)))
        .andExpect(status().isOk());

    // Validate the Settled in the database
    List<Settled> settledList = settledRepository.findAll();
    assertThat(settledList).hasSize(databaseSizeBeforeUpdate);
    Settled testSettled = settledList.get(settledList.size() - 1);
    assertThat(testSettled.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testSettled.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  @Transactional
  public void updateNonExistingSettled() throws Exception {
    int databaseSizeBeforeUpdate = settledRepository.findAll().size();

    // Create the Settled
    SettledDTO settledDTO = settledMapper.settledToSettledDTO(settled);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restSettledMockMvc.perform(put("/api/settleds")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(settledDTO)))
        .andExpect(status().isCreated());

    // Validate the Settled in the database
    List<Settled> settledList = settledRepository.findAll();
    assertThat(settledList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteSettled() throws Exception {
    // Initialize the database
    settledRepository.saveAndFlush(settled);
    int databaseSizeBeforeDelete = settledRepository.findAll().size();

    // Get the settled
    restSettledMockMvc.perform(delete("/api/settleds/{id}", settled.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<Settled> settledList = settledRepository.findAll();
    assertThat(settledList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Settled.class);
  }
}
