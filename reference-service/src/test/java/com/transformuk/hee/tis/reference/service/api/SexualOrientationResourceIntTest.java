package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.api.dto.SexualOrientationDTO;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.SexualOrientation;
import com.transformuk.hee.tis.reference.service.repository.SexualOrientationRepository;
import com.transformuk.hee.tis.reference.service.service.impl.SexualOrientationServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.SexualOrientationMapper;
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
 * Test class for the SexualOrientationResource REST controller.
 *
 * @see SexualOrientationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SexualOrientationResourceIntTest {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";

  @Autowired
  private SexualOrientationRepository sexualOrientationRepository;

  @Autowired
  private SexualOrientationMapper sexualOrientationMapper;
  @Autowired
  private SexualOrientationServiceImpl sexualOrientationService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restSexualOrientationMockMvc;

  private SexualOrientation sexualOrientation;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static SexualOrientation createEntity(EntityManager em) {
    SexualOrientation sexualOrientation = new SexualOrientation()
        .code(DEFAULT_CODE)
        .label(DEFAULT_LABEL);
    return sexualOrientation;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    SexualOrientationResource sexualOrientationResource = new SexualOrientationResource(sexualOrientationRepository,
        sexualOrientationMapper, sexualOrientationService);
    this.restSexualOrientationMockMvc = MockMvcBuilders.standaloneSetup(sexualOrientationResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    sexualOrientation = createEntity(em);
  }

  @Test
  @Transactional
  public void createSexualOrientation() throws Exception {
    int databaseSizeBeforeCreate = sexualOrientationRepository.findAll().size();

    // Create the SexualOrientation
    SexualOrientationDTO sexualOrientationDTO = sexualOrientationMapper.sexualOrientationToSexualOrientationDTO(sexualOrientation);
    restSexualOrientationMockMvc.perform(post("/api/sexual-orientations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(sexualOrientationDTO)))
        .andExpect(status().isCreated());

    // Validate the SexualOrientation in the database
    List<SexualOrientation> sexualOrientationList = sexualOrientationRepository.findAll();
    assertThat(sexualOrientationList).hasSize(databaseSizeBeforeCreate + 1);
    SexualOrientation testSexualOrientation = sexualOrientationList.get(sexualOrientationList.size() - 1);
    assertThat(testSexualOrientation.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testSexualOrientation.getLabel()).isEqualTo(DEFAULT_LABEL);
  }

  @Test
  @Transactional
  public void createSexualOrientationWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = sexualOrientationRepository.findAll().size();

    // Create the SexualOrientation with an existing ID
    sexualOrientation.setId(1L);
    SexualOrientationDTO sexualOrientationDTO = sexualOrientationMapper.sexualOrientationToSexualOrientationDTO(sexualOrientation);

    // An entity with an existing ID cannot be created, so this API call must fail
    restSexualOrientationMockMvc.perform(post("/api/sexual-orientations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(sexualOrientationDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<SexualOrientation> sexualOrientationList = sexualOrientationRepository.findAll();
    assertThat(sexualOrientationList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = sexualOrientationRepository.findAll().size();
    // set the field null
    sexualOrientation.setCode(null);

    // Create the SexualOrientation, which fails.
    SexualOrientationDTO sexualOrientationDTO = sexualOrientationMapper.sexualOrientationToSexualOrientationDTO(sexualOrientation);

    restSexualOrientationMockMvc.perform(post("/api/sexual-orientations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(sexualOrientationDTO)))
        .andExpect(status().isBadRequest());

    List<SexualOrientation> sexualOrientationList = sexualOrientationRepository.findAll();
    assertThat(sexualOrientationList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLabelIsRequired() throws Exception {
    int databaseSizeBeforeTest = sexualOrientationRepository.findAll().size();
    // set the field null
    sexualOrientation.setLabel(null);

    // Create the SexualOrientation, which fails.
    SexualOrientationDTO sexualOrientationDTO = sexualOrientationMapper.sexualOrientationToSexualOrientationDTO(sexualOrientation);

    restSexualOrientationMockMvc.perform(post("/api/sexual-orientations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(sexualOrientationDTO)))
        .andExpect(status().isBadRequest());

    List<SexualOrientation> sexualOrientationList = sexualOrientationRepository.findAll();
    assertThat(sexualOrientationList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllSexualOrientations() throws Exception {
    // Initialize the database
    sexualOrientationRepository.saveAndFlush(sexualOrientation);

    // Get all the sexualOrientationList
    restSexualOrientationMockMvc.perform(get("/api/sexual-orientations?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(sexualOrientation.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
  }

  @Test
  @Transactional
  public void getSexualOrientation() throws Exception {
    // Initialize the database
    sexualOrientationRepository.saveAndFlush(sexualOrientation);

    // Get the sexualOrientation
    restSexualOrientationMockMvc.perform(get("/api/sexual-orientations/{id}", sexualOrientation.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(sexualOrientation.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingSexualOrientation() throws Exception {
    // Get the sexualOrientation
    restSexualOrientationMockMvc.perform(get("/api/sexual-orientations/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateSexualOrientation() throws Exception {
    // Initialize the database
    sexualOrientationRepository.saveAndFlush(sexualOrientation);
    int databaseSizeBeforeUpdate = sexualOrientationRepository.findAll().size();

    // Update the sexualOrientation
      SexualOrientation updatedSexualOrientation = sexualOrientationRepository.findById(sexualOrientation.getId()).orElse(null);
    updatedSexualOrientation
        .code(UPDATED_CODE)
        .label(UPDATED_LABEL);
    SexualOrientationDTO sexualOrientationDTO = sexualOrientationMapper.sexualOrientationToSexualOrientationDTO(updatedSexualOrientation);

    restSexualOrientationMockMvc.perform(put("/api/sexual-orientations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(sexualOrientationDTO)))
        .andExpect(status().isOk());

    // Validate the SexualOrientation in the database
    List<SexualOrientation> sexualOrientationList = sexualOrientationRepository.findAll();
    assertThat(sexualOrientationList).hasSize(databaseSizeBeforeUpdate);
    SexualOrientation testSexualOrientation = sexualOrientationList.get(sexualOrientationList.size() - 1);
    assertThat(testSexualOrientation.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testSexualOrientation.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  @Transactional
  public void updateNonExistingSexualOrientation() throws Exception {
    int databaseSizeBeforeUpdate = sexualOrientationRepository.findAll().size();

    // Create the SexualOrientation
    SexualOrientationDTO sexualOrientationDTO = sexualOrientationMapper.sexualOrientationToSexualOrientationDTO(sexualOrientation);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restSexualOrientationMockMvc.perform(put("/api/sexual-orientations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(sexualOrientationDTO)))
        .andExpect(status().isCreated());

    // Validate the SexualOrientation in the database
    List<SexualOrientation> sexualOrientationList = sexualOrientationRepository.findAll();
    assertThat(sexualOrientationList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteSexualOrientation() throws Exception {
    // Initialize the database
    sexualOrientationRepository.saveAndFlush(sexualOrientation);
    int databaseSizeBeforeDelete = sexualOrientationRepository.findAll().size();

    // Get the sexualOrientation
    restSexualOrientationMockMvc.perform(delete("/api/sexual-orientations/{id}", sexualOrientation.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<SexualOrientation> sexualOrientationList = sexualOrientationRepository.findAll();
    assertThat(sexualOrientationList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(SexualOrientation.class);
  }
}
