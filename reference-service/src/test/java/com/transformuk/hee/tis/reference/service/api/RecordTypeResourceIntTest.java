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

import com.transformuk.hee.tis.reference.api.dto.RecordTypeDTO;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.RecordType;
import com.transformuk.hee.tis.reference.service.repository.RecordTypeRepository;
import com.transformuk.hee.tis.reference.service.service.impl.RecordTypeServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.RecordTypeMapper;
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
 * Test class for the RecordTypeResource REST controller.
 *
 * @see RecordTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RecordTypeResourceIntTest {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "CCCCCCCCCC";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t Record Type";

  @Autowired
  private RecordTypeRepository recordTypeRepository;

  @Autowired
  private RecordTypeMapper recordTypeMapper;

  @Autowired
  private RecordTypeServiceImpl recordTypeService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restRecordTypeMockMvc;

  private RecordType recordType;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static RecordType createEntity(EntityManager em) {
    RecordType recordType = new RecordType();
    recordType.setCode(DEFAULT_CODE);
    recordType.setLabel(DEFAULT_LABEL);
    return recordType;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    RecordTypeResource recordTypeResource = new RecordTypeResource(recordTypeRepository,
        recordTypeMapper, recordTypeService);
    this.restRecordTypeMockMvc = MockMvcBuilders.standaloneSetup(recordTypeResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    recordType = createEntity(em);
  }

  @Test
  @Transactional
  public void createRecordType() throws Exception {
    int databaseSizeBeforeCreate = recordTypeRepository.findAll().size();

    // Create the RecordType
    RecordTypeDTO recordTypeDTO = recordTypeMapper.recordTypeToRecordTypeDTO(recordType);
    restRecordTypeMockMvc.perform(post("/api/record-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(recordTypeDTO)))
        .andExpect(status().isCreated());

    // Validate the RecordType in the database
    List<RecordType> recordTypeList = recordTypeRepository.findAll();
    assertThat(recordTypeList).hasSize(databaseSizeBeforeCreate + 1);
    RecordType testRecordType = recordTypeList.get(recordTypeList.size() - 1);
    assertThat(testRecordType.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testRecordType.getLabel()).isEqualTo(DEFAULT_LABEL);
  }

  @Test
  @Transactional
  public void createRecordTypeWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = recordTypeRepository.findAll().size();

    // Create the RecordType with an existing ID
    recordType.setId(1L);
    RecordTypeDTO recordTypeDTO = recordTypeMapper.recordTypeToRecordTypeDTO(recordType);

    // An entity with an existing ID cannot be created, so this API call must fail
    restRecordTypeMockMvc.perform(post("/api/record-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(recordTypeDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<RecordType> recordTypeList = recordTypeRepository.findAll();
    assertThat(recordTypeList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = recordTypeRepository.findAll().size();
    // set the field null
    recordType.setCode(null);

    // Create the RecordType, which fails.
    RecordTypeDTO recordTypeDTO = recordTypeMapper.recordTypeToRecordTypeDTO(recordType);

    restRecordTypeMockMvc.perform(post("/api/record-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(recordTypeDTO)))
        .andExpect(status().isBadRequest());

    List<RecordType> recordTypeList = recordTypeRepository.findAll();
    assertThat(recordTypeList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLabelIsRequired() throws Exception {
    int databaseSizeBeforeTest = recordTypeRepository.findAll().size();
    // set the field null
    recordType.setLabel(null);

    // Create the RecordType, which fails.
    RecordTypeDTO recordTypeDTO = recordTypeMapper.recordTypeToRecordTypeDTO(recordType);

    restRecordTypeMockMvc.perform(post("/api/record-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(recordTypeDTO)))
        .andExpect(status().isBadRequest());

    List<RecordType> recordTypeList = recordTypeRepository.findAll();
    assertThat(recordTypeList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllRecordTypes() throws Exception {
    // Initialize the database
    recordTypeRepository.saveAndFlush(recordType);

    // Get all the recordTypeList
    restRecordTypeMockMvc.perform(get("/api/record-types?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(recordType.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
  }

  @Test
  @Transactional
  public void getRecordTypesWithQuery() throws Exception {
    // Initialize the database
    RecordType unencodedRecordType = new RecordType();
    unencodedRecordType.setCode(UNENCODED_CODE);
    unencodedRecordType.setLabel(UNENCODED_LABEL);
    recordTypeRepository.saveAndFlush(unencodedRecordType);

    // Get all the recordTypeList
    restRecordTypeMockMvc.perform(get("/api/record-types?searchQuery=\"Te%24t\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(unencodedRecordType.getId().intValue()))
        .andExpect(jsonPath("$.[*].code").value(UNENCODED_CODE))
        .andExpect(jsonPath("$.[*].label").value(UNENCODED_LABEL));
  }

  @Test
  @Transactional
  public void getRecordType() throws Exception {
    // Initialize the database
    recordTypeRepository.saveAndFlush(recordType);

    // Get the recordType
    restRecordTypeMockMvc.perform(get("/api/record-types/{id}", recordType.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(recordType.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingRecordType() throws Exception {
    // Get the recordType
    restRecordTypeMockMvc.perform(get("/api/record-types/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateRecordType() throws Exception {
    // Initialize the database
    recordTypeRepository.saveAndFlush(recordType);
    int databaseSizeBeforeUpdate = recordTypeRepository.findAll().size();

    // Update the recordType
    RecordType updatedRecordType = recordTypeRepository.findById(recordType.getId()).get();
    updatedRecordType.setCode(UPDATED_CODE);
    updatedRecordType.setLabel(UPDATED_LABEL);
    RecordTypeDTO recordTypeDTO = recordTypeMapper.recordTypeToRecordTypeDTO(updatedRecordType);

    restRecordTypeMockMvc.perform(put("/api/record-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(recordTypeDTO)))
        .andExpect(status().isOk());

    // Validate the RecordType in the database
    List<RecordType> recordTypeList = recordTypeRepository.findAll();
    assertThat(recordTypeList).hasSize(databaseSizeBeforeUpdate);
    RecordType testRecordType = recordTypeList.get(recordTypeList.size() - 1);
    assertThat(testRecordType.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testRecordType.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  @Transactional
  public void updateNonExistingRecordType() throws Exception {
    int databaseSizeBeforeUpdate = recordTypeRepository.findAll().size();

    // Create the RecordType
    RecordTypeDTO recordTypeDTO = recordTypeMapper.recordTypeToRecordTypeDTO(recordType);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restRecordTypeMockMvc.perform(put("/api/record-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(recordTypeDTO)))
        .andExpect(status().isCreated());

    // Validate the RecordType in the database
    List<RecordType> recordTypeList = recordTypeRepository.findAll();
    assertThat(recordTypeList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteRecordType() throws Exception {
    // Initialize the database
    recordTypeRepository.saveAndFlush(recordType);
    int databaseSizeBeforeDelete = recordTypeRepository.findAll().size();

    // Get the recordType
    restRecordTypeMockMvc.perform(delete("/api/record-types/{id}", recordType.getId())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<RecordType> recordTypeList = recordTypeRepository.findAll();
    assertThat(recordTypeList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(RecordType.class);
  }
}
