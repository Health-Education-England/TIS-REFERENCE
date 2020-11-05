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

import com.transformuk.hee.tis.reference.api.dto.ProgrammeMembershipTypeDTO;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.ProgrammeMembershipType;
import com.transformuk.hee.tis.reference.service.repository.ProgrammeMembershipTypeRepository;
import com.transformuk.hee.tis.reference.service.service.impl.ProgrammeMembershipTypeServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.ProgrammeMembershipTypeMapper;
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
 * Test class for the ProgrammeMembershipTypeResource REST controller.
 *
 * @see ProgrammeMembershipTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ProgrammeMembershipTypeResourceIntTest {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "CCCCCCCCCC";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t Programme Membership Type";

  @Autowired
  private ProgrammeMembershipTypeRepository programmeMembershipTypeRepository;

  @Autowired
  private ProgrammeMembershipTypeMapper programmeMembershipTypeMapper;

  @Autowired
  private ProgrammeMembershipTypeServiceImpl programmeMembershipTypeService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restProgrammeMembershipTypeMockMvc;

  private ProgrammeMembershipType programmeMembershipType;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static ProgrammeMembershipType createEntity(EntityManager em) {
    ProgrammeMembershipType programmeMembershipType = new ProgrammeMembershipType()
        .code(DEFAULT_CODE)
        .label(DEFAULT_LABEL);
    return programmeMembershipType;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    ProgrammeMembershipTypeResource programmeMembershipTypeResource = new ProgrammeMembershipTypeResource(
        programmeMembershipTypeRepository, programmeMembershipTypeMapper,
        programmeMembershipTypeService);
    this.restProgrammeMembershipTypeMockMvc = MockMvcBuilders
        .standaloneSetup(programmeMembershipTypeResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    programmeMembershipType = createEntity(em);
  }

  @Test
  @Transactional
  public void createProgrammeMembershipType() throws Exception {
    int databaseSizeBeforeCreate = programmeMembershipTypeRepository.findAll().size();

    // Create the ProgrammeMembershipType
    ProgrammeMembershipTypeDTO programmeMembershipTypeDTO = programmeMembershipTypeMapper
        .programmeMembershipTypeToProgrammeMembershipTypeDTO(programmeMembershipType);
    restProgrammeMembershipTypeMockMvc.perform(post("/api/programme-membership-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(programmeMembershipTypeDTO)))
        .andExpect(status().isCreated());

    // Validate the ProgrammeMembershipType in the database
    List<ProgrammeMembershipType> programmeMembershipTypeList = programmeMembershipTypeRepository
        .findAll();
    assertThat(programmeMembershipTypeList).hasSize(databaseSizeBeforeCreate + 1);
    ProgrammeMembershipType testProgrammeMembershipType = programmeMembershipTypeList
        .get(programmeMembershipTypeList.size() - 1);
    assertThat(testProgrammeMembershipType.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testProgrammeMembershipType.getLabel()).isEqualTo(DEFAULT_LABEL);
  }

  @Test
  @Transactional
  public void createProgrammeMembershipTypeWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = programmeMembershipTypeRepository.findAll().size();

    // Create the ProgrammeMembershipType with an existing ID
    programmeMembershipType.setId(1L);
    ProgrammeMembershipTypeDTO programmeMembershipTypeDTO = programmeMembershipTypeMapper
        .programmeMembershipTypeToProgrammeMembershipTypeDTO(programmeMembershipType);

    // An entity with an existing ID cannot be created, so this API call must fail
    restProgrammeMembershipTypeMockMvc.perform(post("/api/programme-membership-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(programmeMembershipTypeDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<ProgrammeMembershipType> programmeMembershipTypeList = programmeMembershipTypeRepository
        .findAll();
    assertThat(programmeMembershipTypeList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = programmeMembershipTypeRepository.findAll().size();
    // set the field null
    programmeMembershipType.setCode(null);

    // Create the ProgrammeMembershipType, which fails.
    ProgrammeMembershipTypeDTO programmeMembershipTypeDTO = programmeMembershipTypeMapper
        .programmeMembershipTypeToProgrammeMembershipTypeDTO(programmeMembershipType);

    restProgrammeMembershipTypeMockMvc.perform(post("/api/programme-membership-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(programmeMembershipTypeDTO)))
        .andExpect(status().isBadRequest());

    List<ProgrammeMembershipType> programmeMembershipTypeList = programmeMembershipTypeRepository
        .findAll();
    assertThat(programmeMembershipTypeList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLabelIsRequired() throws Exception {
    int databaseSizeBeforeTest = programmeMembershipTypeRepository.findAll().size();
    // set the field null
    programmeMembershipType.setLabel(null);

    // Create the ProgrammeMembershipType, which fails.
    ProgrammeMembershipTypeDTO programmeMembershipTypeDTO = programmeMembershipTypeMapper
        .programmeMembershipTypeToProgrammeMembershipTypeDTO(programmeMembershipType);

    restProgrammeMembershipTypeMockMvc.perform(post("/api/programme-membership-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(programmeMembershipTypeDTO)))
        .andExpect(status().isBadRequest());

    List<ProgrammeMembershipType> programmeMembershipTypeList = programmeMembershipTypeRepository
        .findAll();
    assertThat(programmeMembershipTypeList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllProgrammeMembershipTypes() throws Exception {
    // Initialize the database
    programmeMembershipTypeRepository.saveAndFlush(programmeMembershipType);

    // Get all the programmeMembershipTypeList
    restProgrammeMembershipTypeMockMvc.perform(get("/api/programme-membership-types?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(programmeMembershipType.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
  }

  @Test
  @Transactional
  public void getProgrammeMembershipTypesWithQuery() throws Exception {
    // Initialize the database
    ProgrammeMembershipType unencodedProgrammeMembershipType = new ProgrammeMembershipType()
        .code(UNENCODED_CODE)
        .label(UNENCODED_LABEL);
    programmeMembershipTypeRepository.saveAndFlush(unencodedProgrammeMembershipType);

    // Get all the programmeMembershipTypeList
    restProgrammeMembershipTypeMockMvc
        .perform(get("/api/programme-membership-types?searchQuery=\"Te%24t\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].id").value(unencodedProgrammeMembershipType.getId().intValue()))
        .andExpect(jsonPath("$.[*].code").value(UNENCODED_CODE))
        .andExpect(jsonPath("$.[*].label").value(UNENCODED_LABEL));
  }

  @Test
  @Transactional
  public void getProgrammeMembershipType() throws Exception {
    // Initialize the database
    programmeMembershipTypeRepository.saveAndFlush(programmeMembershipType);

    // Get the programmeMembershipType
    restProgrammeMembershipTypeMockMvc
        .perform(get("/api/programme-membership-types/{id}", programmeMembershipType.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(programmeMembershipType.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingProgrammeMembershipType() throws Exception {
    // Get the programmeMembershipType
    restProgrammeMembershipTypeMockMvc
        .perform(get("/api/programme-membership-types/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateProgrammeMembershipType() throws Exception {
    // Initialize the database
    programmeMembershipTypeRepository.saveAndFlush(programmeMembershipType);
    int databaseSizeBeforeUpdate = programmeMembershipTypeRepository.findAll().size();

    // Update the programmeMembershipType
    ProgrammeMembershipType updatedProgrammeMembershipType = programmeMembershipTypeRepository
        .findById(programmeMembershipType.getId()).get();
    updatedProgrammeMembershipType
        .code(UPDATED_CODE)
        .label(UPDATED_LABEL);
    ProgrammeMembershipTypeDTO programmeMembershipTypeDTO = programmeMembershipTypeMapper
        .programmeMembershipTypeToProgrammeMembershipTypeDTO(updatedProgrammeMembershipType);

    restProgrammeMembershipTypeMockMvc.perform(put("/api/programme-membership-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(programmeMembershipTypeDTO)))
        .andExpect(status().isOk());

    // Validate the ProgrammeMembershipType in the database
    List<ProgrammeMembershipType> programmeMembershipTypeList = programmeMembershipTypeRepository
        .findAll();
    assertThat(programmeMembershipTypeList).hasSize(databaseSizeBeforeUpdate);
    ProgrammeMembershipType testProgrammeMembershipType = programmeMembershipTypeList
        .get(programmeMembershipTypeList.size() - 1);
    assertThat(testProgrammeMembershipType.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testProgrammeMembershipType.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  @Transactional
  public void updateNonExistingProgrammeMembershipType() throws Exception {
    int databaseSizeBeforeUpdate = programmeMembershipTypeRepository.findAll().size();

    // Create the ProgrammeMembershipType
    ProgrammeMembershipTypeDTO programmeMembershipTypeDTO = programmeMembershipTypeMapper
        .programmeMembershipTypeToProgrammeMembershipTypeDTO(programmeMembershipType);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restProgrammeMembershipTypeMockMvc.perform(put("/api/programme-membership-types")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(programmeMembershipTypeDTO)))
        .andExpect(status().isCreated());

    // Validate the ProgrammeMembershipType in the database
    List<ProgrammeMembershipType> programmeMembershipTypeList = programmeMembershipTypeRepository
        .findAll();
    assertThat(programmeMembershipTypeList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteProgrammeMembershipType() throws Exception {
    // Initialize the database
    programmeMembershipTypeRepository.saveAndFlush(programmeMembershipType);
    int databaseSizeBeforeDelete = programmeMembershipTypeRepository.findAll().size();

    // Get the programmeMembershipType
    restProgrammeMembershipTypeMockMvc
        .perform(delete("/api/programme-membership-types/{id}", programmeMembershipType.getId())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<ProgrammeMembershipType> programmeMembershipTypeList = programmeMembershipTypeRepository
        .findAll();
    assertThat(programmeMembershipTypeList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ProgrammeMembershipType.class);
  }
}
