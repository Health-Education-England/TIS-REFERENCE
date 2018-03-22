package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.api.dto.PermitToWorkDTO;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.PermitToWork;
import com.transformuk.hee.tis.reference.service.repository.PermitToWorkRepository;
import com.transformuk.hee.tis.reference.service.service.impl.PermitToWorkServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.PermitToWorkMapper;
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
 * Test class for the PermitToWorkResource REST controller.
 *
 * @see PermitToWorkResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PermitToWorkResourceIntTest {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";

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

  private MockMvc restPermitToWorkMockMvc;

  private PermitToWork permitToWork;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static PermitToWork createEntity(EntityManager em) {
    PermitToWork permitToWork = new PermitToWork()
        .code(DEFAULT_CODE)
        .label(DEFAULT_LABEL);
    return permitToWork;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    PermitToWorkResource permitToWorkResource = new PermitToWorkResource(permitToWorkRepository, permitToWorkMapper,
            permitToWorkService);
    this.restPermitToWorkMockMvc = MockMvcBuilders.standaloneSetup(permitToWorkResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    permitToWork = createEntity(em);
  }

  @Test
  @Transactional
  public void createPermitToWork() throws Exception {
    int databaseSizeBeforeCreate = permitToWorkRepository.findAll().size();

    // Create the PermitToWork
    PermitToWorkDTO permitToWorkDTO = permitToWorkMapper.permitToWorkToPermitToWorkDTO(permitToWork);
    restPermitToWorkMockMvc.perform(post("/api/permit-to-works")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
    PermitToWorkDTO maritalStatusDTO = permitToWorkMapper.permitToWorkToPermitToWorkDTO(permitToWork);

    // An entity with an existing ID cannot be created, so this API call must fail
    restPermitToWorkMockMvc.perform(post("/api/permit-to-works")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
    PermitToWorkDTO maritalStatusDTO = permitToWorkMapper.permitToWorkToPermitToWorkDTO(permitToWork);

    restPermitToWorkMockMvc.perform(post("/api/permit-to-works")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
    PermitToWorkDTO maritalStatusDTO = permitToWorkMapper.permitToWorkToPermitToWorkDTO(permitToWork);

    restPermitToWorkMockMvc.perform(post("/api/permit-to-works")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(maritalStatusDTO)))
        .andExpect(status().isBadRequest());

    List<PermitToWork> maritalStatusList = permitToWorkRepository.findAll();
    assertThat(maritalStatusList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllPermitToWorkes() throws Exception {
    // Initialize the database
    permitToWorkRepository.saveAndFlush(permitToWork);

    // Get all the maritalStatusList
    restPermitToWorkMockMvc.perform(get("/api/permit-to-works?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(permitToWork.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
  }

  @Test
  @Transactional
  public void getPermitToWork() throws Exception {
    // Initialize the database
    permitToWorkRepository.saveAndFlush(permitToWork);

    // Get the maritalStatus
    restPermitToWorkMockMvc.perform(get("/api/permit-to-works/{id}", permitToWork.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(permitToWork.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingPermitToWork() throws Exception {
    // Get the maritalStatus
    restPermitToWorkMockMvc.perform(get("/api/permit-to-works/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updatePermitToWork() throws Exception {
    // Initialize the database
    permitToWorkRepository.saveAndFlush(permitToWork);
    int databaseSizeBeforeUpdate = permitToWorkRepository.findAll().size();

    // Update the maritalStatus
    PermitToWork updatedPermitToWork = permitToWorkRepository.findOne(permitToWork.getId());
    updatedPermitToWork
        .code(UPDATED_CODE)
        .label(UPDATED_LABEL);
    PermitToWorkDTO maritalStatusDTO = permitToWorkMapper.permitToWorkToPermitToWorkDTO(updatedPermitToWork);

    restPermitToWorkMockMvc.perform(put("/api/permit-to-works")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
    PermitToWorkDTO maritalStatusDTO = permitToWorkMapper.permitToWorkToPermitToWorkDTO(permitToWork);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restPermitToWorkMockMvc.perform(put("/api/permit-to-works")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
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
    restPermitToWorkMockMvc.perform(delete("/api/permit-to-works/{id}", permitToWork.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
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
}
