package com.transformuk.hee.tis.reference.service.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.reference.api.dto.MedicalSchoolDTO;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.MedicalSchool;
import com.transformuk.hee.tis.reference.service.repository.MedicalSchoolRepository;
import com.transformuk.hee.tis.reference.service.service.impl.MedicalSchoolServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.MedicalSchoolMapper;
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
 * Test class for the MedicalSchoolResource REST controller.
 *
 * @see MedicalSchoolResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MedicalSchoolResourceIntTest {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "CCCCCCCCCC";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t Medical School";

  @Autowired
  private MedicalSchoolRepository medicalSchoolRepository;

  @Autowired
  private MedicalSchoolMapper medicalSchoolMapper;

  @Autowired
  private MedicalSchoolServiceImpl medicalSchoolService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restMedicalSchoolMockMvc;

  private MedicalSchool medicalSchool;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static MedicalSchool createEntity(EntityManager em) {
    MedicalSchool medicalSchool = new MedicalSchool();
    medicalSchool.setCode(DEFAULT_CODE);
    medicalSchool.setLabel(DEFAULT_LABEL);
    return medicalSchool;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    MedicalSchoolResource medicalSchoolResource = new MedicalSchoolResource(medicalSchoolRepository,
        medicalSchoolMapper, medicalSchoolService);
    this.restMedicalSchoolMockMvc = MockMvcBuilders.standaloneSetup(medicalSchoolResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    medicalSchool = createEntity(em);
  }

  @Test
  @Transactional
  public void createMedicalSchool() throws Exception {
    int databaseSizeBeforeCreate = medicalSchoolRepository.findAll().size();

    // Create the MedicalSchool
    MedicalSchoolDTO medicalSchoolDTO = medicalSchoolMapper
        .medicalSchoolToMedicalSchoolDTO(medicalSchool);
    restMedicalSchoolMockMvc.perform(post("/api/medical-schools")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(medicalSchoolDTO)))
        .andExpect(status().isCreated());

    // Validate the MedicalSchool in the database
    List<MedicalSchool> medicalSchoolList = medicalSchoolRepository.findAll();
    assertThat(medicalSchoolList).hasSize(databaseSizeBeforeCreate + 1);
    MedicalSchool testMedicalSchool = medicalSchoolList.get(medicalSchoolList.size() - 1);
    assertThat(testMedicalSchool.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testMedicalSchool.getLabel()).isEqualTo(DEFAULT_LABEL);
  }

  @Test
  @Transactional
  public void createMedicalSchoolWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = medicalSchoolRepository.findAll().size();

    // Create the MedicalSchool with an existing ID
    medicalSchool.setId(1L);
    MedicalSchoolDTO medicalSchoolDTO = medicalSchoolMapper
        .medicalSchoolToMedicalSchoolDTO(medicalSchool);

    // An entity with an existing ID cannot be created, so this API call must fail
    restMedicalSchoolMockMvc.perform(post("/api/medical-schools")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(medicalSchoolDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<MedicalSchool> medicalSchoolList = medicalSchoolRepository.findAll();
    assertThat(medicalSchoolList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = medicalSchoolRepository.findAll().size();
    // set the field null
    medicalSchool.setCode(null);

    // Create the MedicalSchool, which fails.
    MedicalSchoolDTO medicalSchoolDTO = medicalSchoolMapper
        .medicalSchoolToMedicalSchoolDTO(medicalSchool);

    restMedicalSchoolMockMvc.perform(post("/api/medical-schools")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(medicalSchoolDTO)))
        .andExpect(status().isBadRequest());

    List<MedicalSchool> medicalSchoolList = medicalSchoolRepository.findAll();
    assertThat(medicalSchoolList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLabelIsRequired() throws Exception {
    int databaseSizeBeforeTest = medicalSchoolRepository.findAll().size();
    // set the field null
    medicalSchool.setLabel(null);

    // Create the MedicalSchool, which fails.
    MedicalSchoolDTO medicalSchoolDTO = medicalSchoolMapper
        .medicalSchoolToMedicalSchoolDTO(medicalSchool);

    restMedicalSchoolMockMvc.perform(post("/api/medical-schools")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(medicalSchoolDTO)))
        .andExpect(status().isBadRequest());

    List<MedicalSchool> medicalSchoolList = medicalSchoolRepository.findAll();
    assertThat(medicalSchoolList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllMedicalSchools() throws Exception {
    // Initialize the database
    MedicalSchool unencodedMedicalSchool = new MedicalSchool();
    unencodedMedicalSchool.setCode(UNENCODED_CODE);
    unencodedMedicalSchool.setLabel(UNENCODED_LABEL);
    medicalSchoolRepository.saveAndFlush(unencodedMedicalSchool);

    // Get all the medicalSchoolList
    restMedicalSchoolMockMvc
        .perform(get("/api/medical-schools?searchQuery=\"Te%24t\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(unencodedMedicalSchool.getId().intValue()))
        .andExpect(jsonPath("$.[*].code").value(UNENCODED_CODE))
        .andExpect(jsonPath("$.[*].label").value(UNENCODED_LABEL));
  }

  @Test
  @Transactional
  public void getMedicalSchool() throws Exception {
    // Initialize the database
    medicalSchoolRepository.saveAndFlush(medicalSchool);

    // Get the medicalSchool
    restMedicalSchoolMockMvc.perform(get("/api/medical-schools/{id}", medicalSchool.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(medicalSchool.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingMedicalSchool() throws Exception {
    // Get the medicalSchool
    restMedicalSchoolMockMvc.perform(get("/api/medical-schools/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateMedicalSchool() throws Exception {
    // Initialize the database
    medicalSchoolRepository.saveAndFlush(medicalSchool);
    int databaseSizeBeforeUpdate = medicalSchoolRepository.findAll().size();

    // Update the medicalSchool
    MedicalSchool updatedMedicalSchool = medicalSchoolRepository.findById(medicalSchool.getId())
        .get();
    updatedMedicalSchool.setCode(UPDATED_CODE);
    updatedMedicalSchool.setLabel(UPDATED_LABEL);
    MedicalSchoolDTO medicalSchoolDTO = medicalSchoolMapper
        .medicalSchoolToMedicalSchoolDTO(updatedMedicalSchool);

    restMedicalSchoolMockMvc.perform(put("/api/medical-schools")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(medicalSchoolDTO)))
        .andExpect(status().isOk());

    // Validate the MedicalSchool in the database
    List<MedicalSchool> medicalSchoolList = medicalSchoolRepository.findAll();
    assertThat(medicalSchoolList).hasSize(databaseSizeBeforeUpdate);
    MedicalSchool testMedicalSchool = medicalSchoolList.get(medicalSchoolList.size() - 1);
    assertThat(testMedicalSchool.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testMedicalSchool.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  @Transactional
  public void updateNonExistingMedicalSchool() throws Exception {
    int databaseSizeBeforeUpdate = medicalSchoolRepository.findAll().size();

    // Create the MedicalSchool
    MedicalSchoolDTO medicalSchoolDTO = medicalSchoolMapper
        .medicalSchoolToMedicalSchoolDTO(medicalSchool);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restMedicalSchoolMockMvc.perform(put("/api/medical-schools")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(medicalSchoolDTO)))
        .andExpect(status().isCreated());

    // Validate the MedicalSchool in the database
    List<MedicalSchool> medicalSchoolList = medicalSchoolRepository.findAll();
    assertThat(medicalSchoolList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteMedicalSchool() throws Exception {
    // Initialize the database
    medicalSchoolRepository.saveAndFlush(medicalSchool);
    int databaseSizeBeforeDelete = medicalSchoolRepository.findAll().size();

    // Get the medicalSchool
    restMedicalSchoolMockMvc.perform(delete("/api/medical-schools/{id}", medicalSchool.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<MedicalSchool> medicalSchoolList = medicalSchoolRepository.findAll();
    assertThat(medicalSchoolList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(MedicalSchool.class);
  }
}
