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

import com.transformuk.hee.tis.reference.api.dto.InactiveReasonDTO;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.InactiveReason;
import com.transformuk.hee.tis.reference.service.repository.InactiveReasonRepository;
import com.transformuk.hee.tis.reference.service.service.impl.InactiveReasonServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.InactiveReasonMapper;
import java.util.List;
import javax.persistence.EntityManager;
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
 * Test class for the InactiveReasonResource REST controller.
 *
 * @see InactiveReasonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class InactiveReasonResourceIntTest {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "CCCCCCCCCC";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t Reason";

  @Autowired
  private InactiveReasonRepository inactiveReasonRepository;

  @Autowired
  private InactiveReasonMapper inactiveReasonMapper;

  @Autowired
  private InactiveReasonServiceImpl inactiveReasonService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restInactiveReasonMockMvc;

  private InactiveReason inactiveReason;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static InactiveReason createEntity(EntityManager em) {
    InactiveReason inactiveReason = new InactiveReason();
    inactiveReason.setCode(DEFAULT_CODE);
    inactiveReason.setLabel(DEFAULT_LABEL);
    return inactiveReason;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    InactiveReasonResource inactiveReasonResource = new InactiveReasonResource(
        inactiveReasonRepository,
        inactiveReasonMapper, inactiveReasonService);
    this.restInactiveReasonMockMvc = MockMvcBuilders.standaloneSetup(inactiveReasonResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    inactiveReason = createEntity(em);
  }

  @Test
  @Transactional
  public void createInactiveReason() throws Exception {
    int databaseSizeBeforeCreate = inactiveReasonRepository.findAll().size();

    // Create the InactiveReason
    InactiveReasonDTO inactiveReasonDTO = inactiveReasonMapper
        .inactiveReasonToInactiveReasonDTO(inactiveReason);
    restInactiveReasonMockMvc.perform(post("/api/inactive-reasons")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(inactiveReasonDTO)))
        .andExpect(status().isCreated());

    // Validate the InactiveReason in the database
    List<InactiveReason> inactiveReasonList = inactiveReasonRepository.findAll();
    assertThat(inactiveReasonList).hasSize(databaseSizeBeforeCreate + 1);
    InactiveReason testInactiveReason = inactiveReasonList.get(inactiveReasonList.size() - 1);
    assertThat(testInactiveReason.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testInactiveReason.getLabel()).isEqualTo(DEFAULT_LABEL);
  }

  @Test
  @Transactional
  public void createInactiveReasonWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = inactiveReasonRepository.findAll().size();

    // Create the InactiveReason with an existing ID
    inactiveReason.setId(1L);
    InactiveReasonDTO inactiveReasonDTO = inactiveReasonMapper
        .inactiveReasonToInactiveReasonDTO(inactiveReason);

    // An entity with an existing ID cannot be created, so this API call must fail
    restInactiveReasonMockMvc.perform(post("/api/inactive-reasons")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(inactiveReasonDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<InactiveReason> inactiveReasonList = inactiveReasonRepository.findAll();
    assertThat(inactiveReasonList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = inactiveReasonRepository.findAll().size();
    // set the field null
    inactiveReason.setCode(null);

    // Create the InactiveReason, which fails.
    InactiveReasonDTO inactiveReasonDTO = inactiveReasonMapper
        .inactiveReasonToInactiveReasonDTO(inactiveReason);

    restInactiveReasonMockMvc.perform(post("/api/inactive-reasons")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(inactiveReasonDTO)))
        .andExpect(status().isBadRequest());

    List<InactiveReason> inactiveReasonList = inactiveReasonRepository.findAll();
    assertThat(inactiveReasonList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLabelIsRequired() throws Exception {
    int databaseSizeBeforeTest = inactiveReasonRepository.findAll().size();
    // set the field null
    inactiveReason.setLabel(null);

    // Create the InactiveReason, which fails.
    InactiveReasonDTO inactiveReasonDTO = inactiveReasonMapper
        .inactiveReasonToInactiveReasonDTO(inactiveReason);

    restInactiveReasonMockMvc.perform(post("/api/inactive-reasons")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(inactiveReasonDTO)))
        .andExpect(status().isBadRequest());

    List<InactiveReason> inactiveReasonList = inactiveReasonRepository.findAll();
    assertThat(inactiveReasonList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllInactiveReasons() throws Exception {
    // Initialize the database
    inactiveReasonRepository.saveAndFlush(inactiveReason);

    // Get all the inactiveReasonList
    restInactiveReasonMockMvc.perform(get("/api/inactive-reasons?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(inactiveReason.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
  }

  @Test
  @Transactional
  public void getInactiveReasonsWithQuery() throws Exception {
    // Initialize the database
    InactiveReason unencodedInactiveReason = new InactiveReason();
    unencodedInactiveReason.setCode(UNENCODED_CODE);
    unencodedInactiveReason.setLabel(UNENCODED_LABEL);
    inactiveReasonRepository.saveAndFlush(unencodedInactiveReason);

    // Get all the inactiveReasonList
    restInactiveReasonMockMvc
        .perform(get("/api/inactive-reasons?searchQuery=\"Te%24t\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(unencodedInactiveReason.getId().intValue()))
        .andExpect(jsonPath("$.[*].code").value(UNENCODED_CODE))
        .andExpect(jsonPath("$.[*].label").value(UNENCODED_LABEL));
  }

  @Test
  @Transactional
  public void getInactiveReason() throws Exception {
    // Initialize the database
    inactiveReasonRepository.saveAndFlush(inactiveReason);

    // Get the inactiveReason
    restInactiveReasonMockMvc.perform(get("/api/inactive-reasons/{id}", inactiveReason.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(inactiveReason.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingInactiveReason() throws Exception {
    // Get the inactiveReason
    restInactiveReasonMockMvc.perform(get("/api/inactive-reasons/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateInactiveReason() throws Exception {
    // Initialize the database
    inactiveReasonRepository.saveAndFlush(inactiveReason);
    int databaseSizeBeforeUpdate = inactiveReasonRepository.findAll().size();

    // Update the inactiveReason
    InactiveReason updatedInactiveReason = inactiveReasonRepository
        .findById(inactiveReason.getId()).get();
    updatedInactiveReason.setCode(UPDATED_CODE);
    updatedInactiveReason.setLabel(UPDATED_LABEL);
    InactiveReasonDTO inactiveReasonDTO = inactiveReasonMapper
        .inactiveReasonToInactiveReasonDTO(updatedInactiveReason);

    restInactiveReasonMockMvc.perform(put("/api/inactive-reasons")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(inactiveReasonDTO)))
        .andExpect(status().isOk());

    // Validate the InactiveReason in the database
    List<InactiveReason> inactiveReasonList = inactiveReasonRepository.findAll();
    assertThat(inactiveReasonList).hasSize(databaseSizeBeforeUpdate);
    InactiveReason testInactiveReason = inactiveReasonList.get(inactiveReasonList.size() - 1);
    assertThat(testInactiveReason.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testInactiveReason.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  @Transactional
  public void updateNonExistingInactiveReason() throws Exception {
    int databaseSizeBeforeUpdate = inactiveReasonRepository.findAll().size();

    // Create the InactiveReason
    InactiveReasonDTO inactiveReasonDTO = inactiveReasonMapper
        .inactiveReasonToInactiveReasonDTO(inactiveReason);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restInactiveReasonMockMvc.perform(put("/api/inactive-reasons")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(inactiveReasonDTO)))
        .andExpect(status().isCreated());

    // Validate the InactiveReason in the database
    List<InactiveReason> inactiveReasonList = inactiveReasonRepository.findAll();
    assertThat(inactiveReasonList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteInactiveReason() throws Exception {
    // Initialize the database
    inactiveReasonRepository.saveAndFlush(inactiveReason);
    int databaseSizeBeforeDelete = inactiveReasonRepository.findAll().size();

    // Get the inactiveReason
    restInactiveReasonMockMvc.perform(delete("/api/inactive-reasons/{id}", inactiveReason.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<InactiveReason> inactiveReasonList = inactiveReasonRepository.findAll();
    assertThat(inactiveReasonList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(InactiveReason.class);
  }
}
