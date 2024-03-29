package com.transformuk.hee.tis.reference.service.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.transformuk.hee.tis.reference.api.dto.CurriculumSubTypeDTO;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.CurriculumSubType;
import com.transformuk.hee.tis.reference.service.repository.CurriculumSubTypeRepository;
import com.transformuk.hee.tis.reference.service.service.CurriculumSubTypeService;
import com.transformuk.hee.tis.reference.service.service.mapper.CurriculumSubTypeMapper;
import java.util.List;
import javax.persistence.EntityManager;
import org.assertj.core.util.Lists;
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
 * Test class for the CurriculumSubTypeResource REST controller.
 *
 * @see CurriculumSubTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class CurriculumSubTypeResourceIntTest {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "Te$t Code";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "\"Te$t/Validation Curriculum\"";

  @Autowired
  private CurriculumSubTypeRepository curriculumSubTypeRepository;

  @Autowired
  private CurriculumSubTypeMapper curriculumSubTypeMapper;

  @Autowired
  private CurriculumSubTypeService curriculumSubTypeService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restCurriculumSubTypeMockMvc;

  private CurriculumSubType curriculumSubType;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static CurriculumSubType createEntity(EntityManager em) {
    CurriculumSubType curriculumSubType = new CurriculumSubType();
    curriculumSubType.setCode(DEFAULT_CODE);
    curriculumSubType.setLabel(DEFAULT_LABEL);
    return curriculumSubType;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    CurriculumSubTypeResource curriculumSubTypeResource = new CurriculumSubTypeResource(
        curriculumSubTypeRepository, curriculumSubTypeMapper, curriculumSubTypeService);
    this.restCurriculumSubTypeMockMvc = MockMvcBuilders.standaloneSetup(curriculumSubTypeResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    curriculumSubType = createEntity(em);
  }

  @Test
  @Transactional
  public void createCurriculumSubType() throws Exception {
    int databaseSizeBeforeCreate = curriculumSubTypeRepository.findAll().size();

    // Create the CurriculumSubType
    CurriculumSubTypeDTO curriculumSubTypeDTO = curriculumSubTypeMapper
        .curriculumSubTypeToCurriculumSubTypeDTO(curriculumSubType);
    restCurriculumSubTypeMockMvc.perform(post("/api/curriculum-sub-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(curriculumSubTypeDTO)))
        .andExpect(status().isCreated());

    // Validate the CurriculumSubType in the database
    List<CurriculumSubType> curriculumSubTypeList = curriculumSubTypeRepository.findAll();
    assertThat(curriculumSubTypeList).hasSize(databaseSizeBeforeCreate + 1);
    CurriculumSubType testCurriculumSubType = curriculumSubTypeList
        .get(curriculumSubTypeList.size() - 1);
    assertThat(testCurriculumSubType.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testCurriculumSubType.getLabel()).isEqualTo(DEFAULT_LABEL);
  }

  @Test
  @Transactional
  public void createCurriculumSubTypeWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = curriculumSubTypeRepository.findAll().size();

    // Create the CurriculumSubType with an existing ID
    curriculumSubType.setId(1L);
    CurriculumSubTypeDTO curriculumSubTypeDTO = curriculumSubTypeMapper
        .curriculumSubTypeToCurriculumSubTypeDTO(curriculumSubType);

    // An entity with an existing ID cannot be created, so this API call must fail
    restCurriculumSubTypeMockMvc.perform(post("/api/curriculum-sub-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(curriculumSubTypeDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<CurriculumSubType> curriculumSubTypeList = curriculumSubTypeRepository.findAll();
    assertThat(curriculumSubTypeList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = curriculumSubTypeRepository.findAll().size();
    // set the field null
    curriculumSubType.setCode(null);

    // Create the CurriculumSubType, which fails.
    CurriculumSubTypeDTO curriculumSubTypeDTO = curriculumSubTypeMapper
        .curriculumSubTypeToCurriculumSubTypeDTO(curriculumSubType);

    restCurriculumSubTypeMockMvc.perform(post("/api/curriculum-sub-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(curriculumSubTypeDTO)))
        .andExpect(status().isBadRequest());

    List<CurriculumSubType> curriculumSubTypeList = curriculumSubTypeRepository.findAll();
    assertThat(curriculumSubTypeList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLabelIsRequired() throws Exception {
    int databaseSizeBeforeTest = curriculumSubTypeRepository.findAll().size();
    // set the field null
    curriculumSubType.setLabel(null);

    // Create the CurriculumSubType, which fails.
    CurriculumSubTypeDTO curriculumSubTypeDTO = curriculumSubTypeMapper
        .curriculumSubTypeToCurriculumSubTypeDTO(curriculumSubType);

    restCurriculumSubTypeMockMvc.perform(post("/api/curriculum-sub-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(curriculumSubTypeDTO)))
        .andExpect(status().isBadRequest());

    List<CurriculumSubType> curriculumSubTypeList = curriculumSubTypeRepository.findAll();
    assertThat(curriculumSubTypeList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllCurriculumSubTypes() throws Exception {
    // Initialize the database
    curriculumSubTypeRepository.saveAndFlush(curriculumSubType);

    // Get all the curriculumSubTypeList
    restCurriculumSubTypeMockMvc.perform(get("/api/curriculum-sub-types?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(curriculumSubType.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
  }

  @Test
  @Transactional
  public void getCurriculumSubType() throws Exception {
    // Initialize the database
    curriculumSubTypeRepository.saveAndFlush(curriculumSubType);

    // Get the curriculumSubType
    restCurriculumSubTypeMockMvc
        .perform(get("/api/curriculum-sub-types/{id}", curriculumSubType.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(curriculumSubType.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
  }

  @Test
  @Transactional
  public void getCurriculumSubTypeByCode() throws Exception {
    // Initialize the database
    curriculumSubTypeRepository.saveAndFlush(curriculumSubType);

    // Get the curriculumSubType
    restCurriculumSubTypeMockMvc
        .perform(get("/api/curriculum-sub-types/code/{code}", curriculumSubType.getCode()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(curriculumSubType.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
  }

  @Test
  @Transactional
  public void getAllCurriculumSubTypeSmartSearch() throws Exception {
    // Initialize the database

    CurriculumSubType medicalCurriculum = new CurriculumSubType();
    medicalCurriculum.setCode("MEDICAL1");
    medicalCurriculum.setLabel("Medical Curriculum - As defined by the GMC");

    CurriculumSubType medicalSPR = new CurriculumSubType();
    medicalSPR.setCode("MEDICAL2");
    medicalSPR.setLabel("Medical SpR - As defined by the GMC");

    List<CurriculumSubType> curriculumSubTypes = Lists
        .newArrayList(curriculumSubType, medicalCurriculum, medicalSPR);
    curriculumSubTypeRepository.saveAll(curriculumSubTypes);
    curriculumSubTypeRepository.flush();

    // Get the curriculumSubType
    restCurriculumSubTypeMockMvc.perform(get("/api/curriculum-sub-types?searchQuery=\"med\""))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].code")
            .value(hasItems(medicalCurriculum.getCode(), medicalSPR.getCode())))
        .andExpect(jsonPath("$.[*].label")
            .value(hasItems(medicalCurriculum.getLabel(), medicalSPR.getLabel())));
  }

  @Test
  @Transactional
  public void getEntitiesWithQueryMatchingCode() throws Exception {
    // Initialize the database.
    CurriculumSubType entity = new CurriculumSubType();
    entity.setCode(UNENCODED_CODE);
    entity.setLabel(DEFAULT_LABEL);
    curriculumSubTypeRepository.saveAndFlush(entity);

    // Get all the matching entities.
    restCurriculumSubTypeMockMvc
        .perform(get("/api/curriculum-sub-types?searchQuery=\"Te%24t\"&sort=id,desc"))
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
    CurriculumSubType entity = new CurriculumSubType();
    entity.setCode(DEFAULT_CODE);
    entity.setLabel(UNENCODED_LABEL);
    curriculumSubTypeRepository.saveAndFlush(entity);

    // Get all the matching entities.
    restCurriculumSubTypeMockMvc
        .perform(get("/api/curriculum-sub-types?searchQuery=\"Te%24t\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(entity.getId().intValue()))
        .andExpect(jsonPath("$.[*].code").value(DEFAULT_CODE))
        .andExpect(jsonPath("$.[*].label").value(UNENCODED_LABEL));
  }

  @Test
  @Transactional
  public void getNonExistingCurriculumSubType() throws Exception {
    // Get the curriculumSubType
    restCurriculumSubTypeMockMvc.perform(get("/api/curriculum-sub-types/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateCurriculumSubType() throws Exception {
    // Initialize the database
    curriculumSubTypeRepository.saveAndFlush(curriculumSubType);
    int databaseSizeBeforeUpdate = curriculumSubTypeRepository.findAll().size();

    // Update the curriculumSubType
    CurriculumSubType updatedCurriculumSubType = curriculumSubTypeRepository
        .findById(curriculumSubType.getId()).get();
    updatedCurriculumSubType.setCode(UPDATED_CODE);
    updatedCurriculumSubType.setLabel(UPDATED_LABEL);
    CurriculumSubTypeDTO curriculumSubTypeDTO = curriculumSubTypeMapper
        .curriculumSubTypeToCurriculumSubTypeDTO(updatedCurriculumSubType);

    restCurriculumSubTypeMockMvc.perform(put("/api/curriculum-sub-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(curriculumSubTypeDTO)))
        .andExpect(status().isOk());

    // Validate the CurriculumSubType in the database
    List<CurriculumSubType> curriculumSubTypeList = curriculumSubTypeRepository.findAll();
    assertThat(curriculumSubTypeList).hasSize(databaseSizeBeforeUpdate);
    CurriculumSubType testCurriculumSubType = curriculumSubTypeList
        .get(curriculumSubTypeList.size() - 1);
    assertThat(testCurriculumSubType.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testCurriculumSubType.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  @Transactional
  public void updateNonExistingCurriculumSubType() throws Exception {
    int databaseSizeBeforeUpdate = curriculumSubTypeRepository.findAll().size();

    // Create the CurriculumSubType
    CurriculumSubTypeDTO curriculumSubTypeDTO = curriculumSubTypeMapper
        .curriculumSubTypeToCurriculumSubTypeDTO(curriculumSubType);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restCurriculumSubTypeMockMvc.perform(put("/api/curriculum-sub-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(curriculumSubTypeDTO)))
        .andExpect(status().isCreated());

    // Validate the CurriculumSubType in the database
    List<CurriculumSubType> curriculumSubTypeList = curriculumSubTypeRepository.findAll();
    assertThat(curriculumSubTypeList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteCurriculumSubType() throws Exception {
    // Initialize the database
    curriculumSubTypeRepository.saveAndFlush(curriculumSubType);
    int databaseSizeBeforeDelete = curriculumSubTypeRepository.findAll().size();

    // Get the curriculumSubType
    restCurriculumSubTypeMockMvc
        .perform(delete("/api/curriculum-sub-types/{id}", curriculumSubType.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<CurriculumSubType> curriculumSubTypeList = curriculumSubTypeRepository.findAll();
    assertThat(curriculumSubTypeList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(CurriculumSubType.class);
  }
}
