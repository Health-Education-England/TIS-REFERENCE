package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.api.dto.LeavingDestinationDTO;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.LeavingDestination;
import com.transformuk.hee.tis.reference.service.repository.LeavingDestinationRepository;
import com.transformuk.hee.tis.reference.service.service.impl.LeavingDestinationServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.LeavingDestinationMapper;
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
 * Test class for the LeavingDestinationResource REST controller.
 *
 * @see LeavingDestinationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class LeavingDestinationResourceIntTest {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "CCCCCCCCCC";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t Leaving Destination";

  @Autowired
  private LeavingDestinationRepository leavingDestinationRepository;

  @Autowired
  private LeavingDestinationMapper leavingDestinationMapper;

  @Autowired
  private LeavingDestinationServiceImpl leavingDestinationService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restLeavingDestinationMockMvc;

  private LeavingDestination leavingDestination;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static LeavingDestination createEntity(EntityManager em) {
    LeavingDestination leavingDestination = new LeavingDestination()
        .code(DEFAULT_CODE)
        .label(DEFAULT_LABEL);
    return leavingDestination;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    LeavingDestinationResource leavingDestinationResource = new LeavingDestinationResource(
        leavingDestinationRepository, leavingDestinationMapper, leavingDestinationService);
    this.restLeavingDestinationMockMvc = MockMvcBuilders.standaloneSetup(leavingDestinationResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    leavingDestination = createEntity(em);
  }

  @Test
  @Transactional
  public void createLeavingDestination() throws Exception {
    int databaseSizeBeforeCreate = leavingDestinationRepository.findAll().size();

    // Create the LeavingDestination
    LeavingDestinationDTO leavingDestinationDTO = leavingDestinationMapper.leavingDestinationToLeavingDestinationDTO(leavingDestination);
    restLeavingDestinationMockMvc.perform(post("/api/leaving-destinations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(leavingDestinationDTO)))
        .andExpect(status().isCreated());

    // Validate the LeavingDestination in the database
    List<LeavingDestination> leavingDestinationList = leavingDestinationRepository.findAll();
    assertThat(leavingDestinationList).hasSize(databaseSizeBeforeCreate + 1);
    LeavingDestination testLeavingDestination = leavingDestinationList.get(leavingDestinationList.size() - 1);
    assertThat(testLeavingDestination.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testLeavingDestination.getLabel()).isEqualTo(DEFAULT_LABEL);
  }

  @Test
  @Transactional
  public void createLeavingDestinationWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = leavingDestinationRepository.findAll().size();

    // Create the LeavingDestination with an existing ID
    leavingDestination.setId(1L);
    LeavingDestinationDTO leavingDestinationDTO = leavingDestinationMapper.leavingDestinationToLeavingDestinationDTO(leavingDestination);

    // An entity with an existing ID cannot be created, so this API call must fail
    restLeavingDestinationMockMvc.perform(post("/api/leaving-destinations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(leavingDestinationDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<LeavingDestination> leavingDestinationList = leavingDestinationRepository.findAll();
    assertThat(leavingDestinationList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = leavingDestinationRepository.findAll().size();
    // set the field null
    leavingDestination.setCode(null);

    // Create the LeavingDestination, which fails.
    LeavingDestinationDTO leavingDestinationDTO = leavingDestinationMapper.leavingDestinationToLeavingDestinationDTO(leavingDestination);

    restLeavingDestinationMockMvc.perform(post("/api/leaving-destinations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(leavingDestinationDTO)))
        .andExpect(status().isBadRequest());

    List<LeavingDestination> leavingDestinationList = leavingDestinationRepository.findAll();
    assertThat(leavingDestinationList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLabelIsRequired() throws Exception {
    int databaseSizeBeforeTest = leavingDestinationRepository.findAll().size();
    // set the field null
    leavingDestination.setLabel(null);

    // Create the LeavingDestination, which fails.
    LeavingDestinationDTO leavingDestinationDTO = leavingDestinationMapper.leavingDestinationToLeavingDestinationDTO(leavingDestination);

    restLeavingDestinationMockMvc.perform(post("/api/leaving-destinations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(leavingDestinationDTO)))
        .andExpect(status().isBadRequest());

    List<LeavingDestination> leavingDestinationList = leavingDestinationRepository.findAll();
    assertThat(leavingDestinationList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllLeavingDestinations() throws Exception {
    // Initialize the database
    leavingDestinationRepository.saveAndFlush(leavingDestination);

    // Get all the leavingDestinationList
    restLeavingDestinationMockMvc.perform(get("/api/leaving-destinations?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(leavingDestination.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
  }
  @Test
  @Transactional
  public void getLeavingDestinationsWithQuery() throws Exception {
    // Initialize the database
    LeavingDestination unencodedLeavingDestination = new LeavingDestination()
        .code(UNENCODED_CODE)
        .label(UNENCODED_LABEL);
    leavingDestinationRepository.saveAndFlush(unencodedLeavingDestination);

    // Get all the leavingDestinationList
    restLeavingDestinationMockMvc.perform(get("/api/leaving-destinations?searchQuery=\"Te%24t\"&sort=id,desc"))
    .andExpect(status().isOk())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    .andExpect(jsonPath("$.[*].id").value(unencodedLeavingDestination.getId().intValue()))
    .andExpect(jsonPath("$.[*].code").value(UNENCODED_CODE))
    .andExpect(jsonPath("$.[*].label").value(UNENCODED_LABEL));
  }

  @Test
  @Transactional
  public void getLeavingDestination() throws Exception {
    // Initialize the database
    leavingDestinationRepository.saveAndFlush(leavingDestination);

    // Get the leavingDestination
    restLeavingDestinationMockMvc.perform(get("/api/leaving-destinations/{id}", leavingDestination.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(leavingDestination.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingLeavingDestination() throws Exception {
    // Get the leavingDestination
    restLeavingDestinationMockMvc.perform(get("/api/leaving-destinations/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateLeavingDestination() throws Exception {
    // Initialize the database
    leavingDestinationRepository.saveAndFlush(leavingDestination);
    int databaseSizeBeforeUpdate = leavingDestinationRepository.findAll().size();

    // Update the leavingDestination
    LeavingDestination updatedLeavingDestination = leavingDestinationRepository.findOne(leavingDestination.getId());
    updatedLeavingDestination
        .code(UPDATED_CODE)
        .label(UPDATED_LABEL);
    LeavingDestinationDTO leavingDestinationDTO = leavingDestinationMapper.leavingDestinationToLeavingDestinationDTO(updatedLeavingDestination);

    restLeavingDestinationMockMvc.perform(put("/api/leaving-destinations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(leavingDestinationDTO)))
        .andExpect(status().isOk());

    // Validate the LeavingDestination in the database
    List<LeavingDestination> leavingDestinationList = leavingDestinationRepository.findAll();
    assertThat(leavingDestinationList).hasSize(databaseSizeBeforeUpdate);
    LeavingDestination testLeavingDestination = leavingDestinationList.get(leavingDestinationList.size() - 1);
    assertThat(testLeavingDestination.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testLeavingDestination.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  @Transactional
  public void updateNonExistingLeavingDestination() throws Exception {
    int databaseSizeBeforeUpdate = leavingDestinationRepository.findAll().size();

    // Create the LeavingDestination
    LeavingDestinationDTO leavingDestinationDTO = leavingDestinationMapper.leavingDestinationToLeavingDestinationDTO(leavingDestination);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restLeavingDestinationMockMvc.perform(put("/api/leaving-destinations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(leavingDestinationDTO)))
        .andExpect(status().isCreated());

    // Validate the LeavingDestination in the database
    List<LeavingDestination> leavingDestinationList = leavingDestinationRepository.findAll();
    assertThat(leavingDestinationList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteLeavingDestination() throws Exception {
    // Initialize the database
    leavingDestinationRepository.saveAndFlush(leavingDestination);
    int databaseSizeBeforeDelete = leavingDestinationRepository.findAll().size();

    // Get the leavingDestination
    restLeavingDestinationMockMvc.perform(delete("/api/leaving-destinations/{id}", leavingDestination.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<LeavingDestination> leavingDestinationList = leavingDestinationRepository.findAll();
    assertThat(leavingDestinationList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(LeavingDestination.class);
  }
}
