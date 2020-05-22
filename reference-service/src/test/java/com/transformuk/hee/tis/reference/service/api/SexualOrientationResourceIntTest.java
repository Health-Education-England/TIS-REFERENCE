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

import com.transformuk.hee.tis.reference.api.dto.SexualOrientationDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.SexualOrientation;
import com.transformuk.hee.tis.reference.service.repository.SexualOrientationRepository;
import com.transformuk.hee.tis.reference.service.service.impl.SexualOrientationServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.SexualOrientationMapper;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.codec.CharEncoding;
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
 * Test class for the SexualOrientationResource REST controller.
 *
 * @see SexualOrientationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SexualOrientationResourceIntTest {

  private static final String EXISTS_ENDPOINT = "/api/sexual-orientations/exists/";

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "CCCCCCCCCC";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t Sexual Orientation";

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

  private MockMvc mockMvc;

  private SexualOrientation sexualOrientation;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
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
    SexualOrientationResource sexualOrientationResource = new SexualOrientationResource(
        sexualOrientationRepository,
        sexualOrientationMapper, sexualOrientationService);
    this.mockMvc = MockMvcBuilders.standaloneSetup(sexualOrientationResource)
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
    SexualOrientationDTO sexualOrientationDTO = sexualOrientationMapper
        .sexualOrientationToSexualOrientationDTO(sexualOrientation);
    mockMvc.perform(post("/api/sexual-orientations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(sexualOrientationDTO)))
        .andExpect(status().isCreated());

    // Validate the SexualOrientation in the database
    List<SexualOrientation> sexualOrientationList = sexualOrientationRepository.findAll();
    assertThat(sexualOrientationList).hasSize(databaseSizeBeforeCreate + 1);
    SexualOrientation testSexualOrientation = sexualOrientationList
        .get(sexualOrientationList.size() - 1);
    assertThat(testSexualOrientation.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testSexualOrientation.getLabel()).isEqualTo(DEFAULT_LABEL);
  }

  @Test
  @Transactional
  public void createSexualOrientationWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = sexualOrientationRepository.findAll().size();

    // Create the SexualOrientation with an existing ID
    sexualOrientation.setId(1L);
    SexualOrientationDTO sexualOrientationDTO = sexualOrientationMapper
        .sexualOrientationToSexualOrientationDTO(sexualOrientation);

    // An entity with an existing ID cannot be created, so this API call must fail
    mockMvc.perform(post("/api/sexual-orientations")
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
    SexualOrientationDTO sexualOrientationDTO = sexualOrientationMapper
        .sexualOrientationToSexualOrientationDTO(sexualOrientation);

    mockMvc.perform(post("/api/sexual-orientations")
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
    SexualOrientationDTO sexualOrientationDTO = sexualOrientationMapper
        .sexualOrientationToSexualOrientationDTO(sexualOrientation);

    mockMvc.perform(post("/api/sexual-orientations")
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
    mockMvc.perform(get("/api/sexual-orientations?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(sexualOrientation.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
  }

  @Test
  @Transactional
  public void getSexualOrientationsWithQuery() throws Exception {
    // Initialize the database
    SexualOrientation unencodedSexualOrientation = new SexualOrientation()
        .code(UNENCODED_CODE)
        .label(UNENCODED_LABEL);
    sexualOrientationRepository.saveAndFlush(unencodedSexualOrientation);

    // Get the sexualOrientationList
    mockMvc
        .perform(get("/api/sexual-orientations?searchQuery=\"Te%24t\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(unencodedSexualOrientation.getId().intValue()))
        .andExpect(jsonPath("$.[*].code").value(UNENCODED_CODE))
        .andExpect(jsonPath("$.[*].label").value(UNENCODED_LABEL));
  }

  @Test
  @Transactional
  public void getSexualOrientation() throws Exception {
    // Initialize the database
    sexualOrientationRepository.saveAndFlush(sexualOrientation);

    // Get the sexualOrientation
    mockMvc
        .perform(get("/api/sexual-orientations/{id}", sexualOrientation.getId()))
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
    mockMvc.perform(get("/api/sexual-orientations/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateSexualOrientation() throws Exception {
    // Initialize the database
    sexualOrientationRepository.saveAndFlush(sexualOrientation);
    int databaseSizeBeforeUpdate = sexualOrientationRepository.findAll().size();

    // Update the sexualOrientation
    SexualOrientation updatedSexualOrientation = sexualOrientationRepository
        .findOne(sexualOrientation.getId());
    updatedSexualOrientation
        .code(UPDATED_CODE)
        .label(UPDATED_LABEL);
    SexualOrientationDTO sexualOrientationDTO = sexualOrientationMapper
        .sexualOrientationToSexualOrientationDTO(updatedSexualOrientation);

    mockMvc.perform(put("/api/sexual-orientations")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(sexualOrientationDTO)))
        .andExpect(status().isOk());

    // Validate the SexualOrientation in the database
    List<SexualOrientation> sexualOrientationList = sexualOrientationRepository.findAll();
    assertThat(sexualOrientationList).hasSize(databaseSizeBeforeUpdate);
    SexualOrientation testSexualOrientation = sexualOrientationList
        .get(sexualOrientationList.size() - 1);
    assertThat(testSexualOrientation.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testSexualOrientation.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  @Transactional
  public void updateNonExistingSexualOrientation() throws Exception {
    int databaseSizeBeforeUpdate = sexualOrientationRepository.findAll().size();

    // Create the SexualOrientation
    SexualOrientationDTO sexualOrientationDTO = sexualOrientationMapper
        .sexualOrientationToSexualOrientationDTO(sexualOrientation);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    mockMvc.perform(put("/api/sexual-orientations")
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
    mockMvc
        .perform(delete("/api/sexual-orientations/{id}", sexualOrientation.getId())
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

  @Test
  @Transactional
  public void shouldReturnFalseWhenNotExistsAndFilterNotApplied() throws Exception {
    mockMvc.perform(post(EXISTS_ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes("notExists_" + LocalDate.now())))
        .andExpect(status().isOk())
        .andExpect(content().string("false"));
  }

  @Test
  @Transactional
  public void shouldReturnTrueWhenExistsAndFilterNotApplied() throws Exception {
    sexualOrientationRepository.saveAndFlush(sexualOrientation);

    mockMvc.perform(post(EXISTS_ENDPOINT)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(DEFAULT_CODE)))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }

  @Test
  @Transactional
  public void shouldReturnFalseWhenNotExistsAndFilterApplied() throws Exception {
    String columnFilter = URLEncoder.encode("{\"status\":[\"CURRENT\"]}", CharEncoding.UTF_8);

    mockMvc.perform(post(EXISTS_ENDPOINT + "?columnFilters=" + columnFilter)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes("notExists_" + LocalDate.now())))
        .andExpect(status().isOk())
        .andExpect(content().string("false"));
  }

  @Test
  @Transactional
  public void shouldReturnFalseWhenExistsAndFilterExcludes() throws Exception {
    sexualOrientation.setStatus(Status.INACTIVE);
    sexualOrientationRepository.saveAndFlush(sexualOrientation);

    String columnFilter = URLEncoder.encode("{\"status\":[\"CURRENT\"]}", CharEncoding.UTF_8);

    mockMvc.perform(post(EXISTS_ENDPOINT + "?columnFilters=" + columnFilter)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(DEFAULT_CODE)))
        .andExpect(status().isOk())
        .andExpect(content().string("false"));
  }

  @Test
  @Transactional
  public void shouldReturnTrueWhenExistsAndFilterIncludes() throws Exception {
    sexualOrientation.setStatus(Status.CURRENT);
    sexualOrientationRepository.saveAndFlush(sexualOrientation);

    String columnFilter = URLEncoder.encode("{\"status\":[\"CURRENT\"]}", CharEncoding.UTF_8);

    mockMvc.perform(post(EXISTS_ENDPOINT + "?columnFilters=" + columnFilter)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(DEFAULT_CODE)))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }
}
