package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.api.dto.QualificationTypeDTO;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.QualificationType;
import com.transformuk.hee.tis.reference.service.repository.QualificationTypeRepository;
import com.transformuk.hee.tis.reference.service.service.impl.QualificationTypeServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.QualificationTypeMapper;
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
 * Test class for the QualificationTypeResource REST controller.
 *
 * @see QualificationTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class QualificationTypeResourceIntTest {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";

  @Autowired
  private QualificationTypeRepository qualificationTypeRepository;

  @Autowired
  private QualificationTypeMapper qualificationTypeMapper;

  @Autowired
  private QualificationTypeServiceImpl qualificationTypeService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restQualificationTypeMockMvc;

  private QualificationType qualificationType;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static QualificationType createEntity(EntityManager em) {
    QualificationType qualificationType = new QualificationType()
            .code(DEFAULT_CODE)
            .label(DEFAULT_LABEL);
    return qualificationType;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    QualificationTypeResource qualificationTypeResource = new QualificationTypeResource(qualificationTypeRepository,
            qualificationTypeMapper,
            qualificationTypeService);
    this.restQualificationTypeMockMvc = MockMvcBuilders.standaloneSetup(qualificationTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    qualificationType = createEntity(em);
  }

  @Test
  @Transactional
  public void createQualificationType() throws Exception {
    int databaseSizeBeforeCreate = qualificationTypeRepository.findAll().size();

    // Create the QualificationType
    QualificationTypeDTO qualificationTypeDTO = qualificationTypeMapper.qualificationTypeToQualificationTypeDTO(qualificationType);
    restQualificationTypeMockMvc.perform(post("/api/qualification-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(qualificationTypeDTO)))
            .andExpect(status().isCreated());

    // Validate the QualificationType in the database
    List<QualificationType> qualificationTypeList = qualificationTypeRepository.findAll();
    assertThat(qualificationTypeList).hasSize(databaseSizeBeforeCreate + 1);
    QualificationType testQualificationType = qualificationTypeList.get(qualificationTypeList.size() - 1);
    assertThat(testQualificationType.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testQualificationType.getLabel()).isEqualTo(DEFAULT_LABEL);
  }

  @Test
  @Transactional
  public void createQualificationTypeWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = qualificationTypeRepository.findAll().size();

    // Create the QualificationType with an existing ID
    qualificationType.setId(1L);
    QualificationTypeDTO qualificationTypeDTO = qualificationTypeMapper.qualificationTypeToQualificationTypeDTO(qualificationType);

    // An entity with an existing ID cannot be created, so this API call must fail
    restQualificationTypeMockMvc.perform(post("/api/qualification-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(qualificationTypeDTO)))
            .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<QualificationType> qualificationTypeList = qualificationTypeRepository.findAll();
    assertThat(qualificationTypeList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = qualificationTypeRepository.findAll().size();
    // set the field null
    qualificationType.setCode(null);

    // Create the QualificationType, which fails.
    QualificationTypeDTO qualificationTypeDTO = qualificationTypeMapper.qualificationTypeToQualificationTypeDTO(qualificationType);

    restQualificationTypeMockMvc.perform(post("/api/qualification-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(qualificationTypeDTO)))
            .andExpect(status().isBadRequest());

    List<QualificationType> qualificationTypeList = qualificationTypeRepository.findAll();
    assertThat(qualificationTypeList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLabelIsRequired() throws Exception {
    int databaseSizeBeforeTest = qualificationTypeRepository.findAll().size();
    // set the field null
    qualificationType.setLabel(null);

    // Create the QualificationType, which fails.
    QualificationTypeDTO qualificationTypeDTO = qualificationTypeMapper.qualificationTypeToQualificationTypeDTO(qualificationType);

    restQualificationTypeMockMvc.perform(post("/api/qualification-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(qualificationTypeDTO)))
            .andExpect(status().isBadRequest());

    List<QualificationType> qualificationTypeList = qualificationTypeRepository.findAll();
    assertThat(qualificationTypeList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllQualificationTypes() throws Exception {
    // Initialize the database
    qualificationTypeRepository.saveAndFlush(qualificationType);

    // Get all the qualificationTypeList
    restQualificationTypeMockMvc.perform(get("/api/qualification-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qualificationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
  }

  @Test
  @Transactional
  public void getQualificationType() throws Exception {
    // Initialize the database
    qualificationTypeRepository.saveAndFlush(qualificationType);

    // Get the qualificationType
    restQualificationTypeMockMvc.perform(get("/api/qualification-types/{id}", qualificationType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(qualificationType.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingQualificationType() throws Exception {
    // Get the qualificationType
    restQualificationTypeMockMvc.perform(get("/api/qualification-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateQualificationType() throws Exception {
    // Initialize the database
    qualificationTypeRepository.saveAndFlush(qualificationType);
    int databaseSizeBeforeUpdate = qualificationTypeRepository.findAll().size();

    // Update the qualificationType
      QualificationType updatedQualificationType = qualificationTypeRepository.findById(qualificationType.getId()).orElse(null);
    updatedQualificationType
            .code(UPDATED_CODE)
            .label(UPDATED_LABEL);
    QualificationTypeDTO qualificationTypeDTO = qualificationTypeMapper.qualificationTypeToQualificationTypeDTO(updatedQualificationType);

    restQualificationTypeMockMvc.perform(put("/api/qualification-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(qualificationTypeDTO)))
            .andExpect(status().isOk());

    // Validate the QualificationType in the database
    List<QualificationType> qualificationTypeList = qualificationTypeRepository.findAll();
    assertThat(qualificationTypeList).hasSize(databaseSizeBeforeUpdate);
    QualificationType testQualificationType = qualificationTypeList.get(qualificationTypeList.size() - 1);
    assertThat(testQualificationType.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testQualificationType.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  @Transactional
  public void updateNonExistingQualificationType() throws Exception {
    int databaseSizeBeforeUpdate = qualificationTypeRepository.findAll().size();

    // Create the QualificationType
    QualificationTypeDTO qualificationTypeDTO = qualificationTypeMapper.qualificationTypeToQualificationTypeDTO(qualificationType);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restQualificationTypeMockMvc.perform(put("/api/qualification-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(qualificationTypeDTO)))
            .andExpect(status().isCreated());

    // Validate the QualificationType in the database
    List<QualificationType> qualificationTypeList = qualificationTypeRepository.findAll();
    assertThat(qualificationTypeList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteQualificationType() throws Exception {
    // Initialize the database
    qualificationTypeRepository.saveAndFlush(qualificationType);
    int databaseSizeBeforeDelete = qualificationTypeRepository.findAll().size();

    // Get the QualificationType
    restQualificationTypeMockMvc.perform(delete("/api/qualification-types/{id}", qualificationType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

    // Validate the database is empty
    List<QualificationType> qualificationTypeList = qualificationTypeRepository.findAll();
    assertThat(qualificationTypeList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(QualificationType.class);
  }
}
