package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.api.dto.RotationDTO;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.Rotation;
import com.transformuk.hee.tis.reference.service.repository.RotationRepository;
import com.transformuk.hee.tis.reference.service.service.RotationService;
import com.transformuk.hee.tis.reference.service.service.mapper.RotationMapper;
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
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the RotationResource REST controller.
 *
 * @see RotationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RotationResourceIntTest {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";

  private static final String DEFAULT_LOCAL_OFFICE = "AAAAAAAAAA";
  private static final String UPDATED_LOCAL_OFFICE = "BBBBBBBBBB";

  @Autowired
  private RotationRepository rotationRepository;

  @Autowired
  private RotationMapper rotationMapper;

  @Autowired
  private RotationService rotationService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restRotationMockMvc;

  private Rotation rotation;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final RotationResource rotationResource = new RotationResource(rotationService);
    this.restRotationMockMvc = MockMvcBuilders.standaloneSetup(rotationResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Rotation createEntity(EntityManager em) {
    Rotation rotation = new Rotation()
        .code(DEFAULT_CODE)
        .label(DEFAULT_LABEL)
        .localOffice(DEFAULT_LOCAL_OFFICE);
    return rotation;
  }

  @Before
  public void initTest() {
    rotation = createEntity(em);
  }

  @Test
  @Transactional
  public void createRotation() throws Exception {
    int databaseSizeBeforeCreate = rotationRepository.findAll().size();

    // Create the Rotation
    RotationDTO rotationDTO = rotationMapper.toDto(rotation);
    restRotationMockMvc.perform(post("/api/rotations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(rotationDTO)))
        .andExpect(status().isCreated());

    // Validate the Rotation in the database
    List<Rotation> rotationList = rotationRepository.findAll();
    assertThat(rotationList).hasSize(databaseSizeBeforeCreate + 1);
    Rotation testRotation = rotationList.get(rotationList.size() - 1);
    assertThat(testRotation.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testRotation.getLabel()).isEqualTo(DEFAULT_LABEL);
    assertThat(testRotation.getLocalOffice()).isEqualTo(DEFAULT_LOCAL_OFFICE);
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenCreating() throws Exception {
    //given
    RotationDTO rotationDTO = new RotationDTO();

    //when & then
    restRotationMockMvc.perform(post("/api/rotations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(rotationDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("code","label","localOffice")));
  }

  @Test
  @Transactional
  public void shouldValidateMandatoryFieldsWhenUpdating() throws Exception {
    //given
    RotationDTO rotationDTO = new RotationDTO();

    //when & then
    restRotationMockMvc.perform(put("/api/rotations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(rotationDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("error.validation"))
        .andExpect(jsonPath("$.fieldErrors[*].field").
            value(containsInAnyOrder("id", "code","label","localOffice")));
  }

  @Test
  @Transactional
  public void createRotationWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = rotationRepository.findAll().size();

    // Create the Rotation with an existing ID
    rotation.setId(1L);
    RotationDTO rotationDTO = rotationMapper.toDto(rotation);

    // An entity with an existing ID cannot be created, so this API call must fail
    restRotationMockMvc.perform(post("/api/rotations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(rotationDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Rotation in the database
    List<Rotation> rotationList = rotationRepository.findAll();
    assertThat(rotationList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllRotations() throws Exception {
    // Initialize the database
    rotationRepository.saveAndFlush(rotation);

    // Get all the rotationList
    restRotationMockMvc.perform(get("/api/rotations?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(rotation.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
        .andExpect(jsonPath("$.[*].localOffice").value(hasItem(DEFAULT_LOCAL_OFFICE.toString())));
  }

  @Test
  @Transactional
  public void getRotation() throws Exception {
    // Initialize the database
    rotationRepository.saveAndFlush(rotation);

    // Get the rotation
    restRotationMockMvc.perform(get("/api/rotations/{id}", rotation.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(rotation.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()))
        .andExpect(jsonPath("$.localOffice").value(DEFAULT_LOCAL_OFFICE.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingRotation() throws Exception {
    // Get the rotation
    restRotationMockMvc.perform(get("/api/rotations/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateRotation() throws Exception {
    // Initialize the database
    rotationRepository.saveAndFlush(rotation);
    int databaseSizeBeforeUpdate = rotationRepository.findAll().size();

    // Update the rotation
    Rotation updatedRotation = rotationRepository.findOne(rotation.getId());
    updatedRotation
        .code(UPDATED_CODE)
        .label(UPDATED_LABEL)
        .localOffice(UPDATED_LOCAL_OFFICE);
    RotationDTO rotationDTO = rotationMapper.toDto(updatedRotation);

    restRotationMockMvc.perform(put("/api/rotations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(rotationDTO)))
        .andExpect(status().isOk());

    // Validate the Rotation in the database
    List<Rotation> rotationList = rotationRepository.findAll();
    assertThat(rotationList).hasSize(databaseSizeBeforeUpdate);
    Rotation testRotation = rotationList.get(rotationList.size() - 1);
    assertThat(testRotation.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testRotation.getLabel()).isEqualTo(UPDATED_LABEL);
    assertThat(testRotation.getLocalOffice()).isEqualTo(UPDATED_LOCAL_OFFICE);
  }

  @Test
  @Transactional
  public void updateNonExistingRotation() throws Exception {
    int databaseSizeBeforeUpdate = rotationRepository.findAll().size();

    // Create the Rotation
    RotationDTO rotationDTO = rotationMapper.toDto(rotation);

    // If the entity doesn't have an ID, it will be not be created due to DTO validation
    restRotationMockMvc.perform(put("/api/rotations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(rotationDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Rotation in the database
    List<Rotation> rotationList = rotationRepository.findAll();
    assertThat(rotationList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteRotation() throws Exception {
    // Initialize the database
    rotationRepository.saveAndFlush(rotation);
    int databaseSizeBeforeDelete = rotationRepository.findAll().size();

    // Get the rotation
    restRotationMockMvc.perform(delete("/api/rotations/{id}", rotation.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<Rotation> rotationList = rotationRepository.findAll();
    assertThat(rotationList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Rotation.class);
    Rotation rotation1 = new Rotation();
    rotation1.setId(1L);
    Rotation rotation2 = new Rotation();
    rotation2.setId(rotation1.getId());
    assertThat(rotation1).isEqualTo(rotation2);
    rotation2.setId(2L);
    assertThat(rotation1).isNotEqualTo(rotation2);
    rotation1.setId(null);
    assertThat(rotation1).isNotEqualTo(rotation2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(RotationDTO.class);
    RotationDTO rotationDTO1 = new RotationDTO();
    rotationDTO1.setId(1L);
    RotationDTO rotationDTO2 = new RotationDTO();
    assertThat(rotationDTO1).isNotEqualTo(rotationDTO2);
    rotationDTO2.setId(rotationDTO1.getId());
    assertThat(rotationDTO1).isEqualTo(rotationDTO2);
    rotationDTO2.setId(2L);
    assertThat(rotationDTO1).isNotEqualTo(rotationDTO2);
    rotationDTO1.setId(null);
    assertThat(rotationDTO1).isNotEqualTo(rotationDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(rotationMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(rotationMapper.fromId(null)).isNull();
  }
}
