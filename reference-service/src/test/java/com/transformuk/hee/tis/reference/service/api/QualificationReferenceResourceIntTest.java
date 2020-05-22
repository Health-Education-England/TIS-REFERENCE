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

import com.transformuk.hee.tis.reference.api.dto.QualificationReferenceDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.QualificationReference;
import com.transformuk.hee.tis.reference.service.repository.QualificationReferenceRepository;
import com.transformuk.hee.tis.reference.service.service.impl.QualificationReferenceServiceImpl;
import com.transformuk.hee.tis.reference.service.service.mapper.QualificationReferenceMapper;
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
 * Test class for the QualificationReferenceResource REST controller.
 *
 * @see QualificationReferenceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class QualificationReferenceResourceIntTest {

  private static final String EXISTS_ENDPOINT = "/api/qualification-reference/exists/";

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_CODE = "CCCCCCCCCC";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";
  private static final String UNENCODED_LABEL = "Te$t Qualification Reference";

  @Autowired
  private QualificationReferenceRepository qualificationReferenceRepository;

  @Autowired
  private QualificationReferenceMapper qualificationReferenceMapper;

  @Autowired
  private QualificationReferenceServiceImpl qualificationReferenceService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc mockMvc;

  private QualificationReference qualificationReference;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static QualificationReference createEntity(EntityManager em) {
    QualificationReference qualificationReference = new QualificationReference()
        .code(DEFAULT_CODE)
        .label(DEFAULT_LABEL);
    return qualificationReference;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    QualificationReferenceResource qualificationReferenceResource =
        new QualificationReferenceResource(
            qualificationReferenceRepository,
            qualificationReferenceMapper,
            qualificationReferenceService);
    this.mockMvc = MockMvcBuilders
        .standaloneSetup(qualificationReferenceResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    qualificationReference = createEntity(em);
  }

  @Test
  @Transactional
  public void createQualificationReference() throws Exception {
    int databaseSizeBeforeCreate = qualificationReferenceRepository.findAll().size();

    // Create the QualificationReference
    QualificationReferenceDTO qualificationReferenceDTO = qualificationReferenceMapper
        .qualificationReferenceToQualificationReferenceDTO(qualificationReference);
    mockMvc.perform(post("/api/qualification-reference")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationReferenceDTO)))
        .andExpect(status().isCreated());

    // Validate the QualificationReference in the database
    List<QualificationReference> qualificationReferenceList = qualificationReferenceRepository
        .findAll();
    assertThat(qualificationReferenceList).hasSize(databaseSizeBeforeCreate + 1);
    QualificationReference testQualificationReference = qualificationReferenceList
        .get(qualificationReferenceList.size() - 1);
    assertThat(testQualificationReference.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testQualificationReference.getLabel()).isEqualTo(DEFAULT_LABEL);
  }

  @Test
  @Transactional
  public void createQualificationReferenceWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = qualificationReferenceRepository.findAll().size();

    // Create the QualificationReference with an existing ID
    qualificationReference.setId(1L);
    QualificationReferenceDTO qualificationReferenceDTO = qualificationReferenceMapper
        .qualificationReferenceToQualificationReferenceDTO(qualificationReference);

    // An entity with an existing ID cannot be created, so this API call must fail
    mockMvc.perform(post("/api/qualification-reference")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationReferenceDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<QualificationReference> qualificationReferenceList = qualificationReferenceRepository
        .findAll();
    assertThat(qualificationReferenceList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = qualificationReferenceRepository.findAll().size();
    // set the field null
    qualificationReference.setCode(null);

    // Create the QualificationReference, which fails.
    QualificationReferenceDTO qualificationReferenceDTO = qualificationReferenceMapper
        .qualificationReferenceToQualificationReferenceDTO(qualificationReference);

    mockMvc.perform(post("/api/qualification-reference")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationReferenceDTO)))
        .andExpect(status().isBadRequest());

    List<QualificationReference> qualificationReferenceList = qualificationReferenceRepository
        .findAll();
    assertThat(qualificationReferenceList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLabelIsRequired() throws Exception {
    int databaseSizeBeforeTest = qualificationReferenceRepository.findAll().size();
    // set the field null
    qualificationReference.setLabel(null);

    // Create the QualificationReference, which fails.
    QualificationReferenceDTO qualificationReferenceDTO = qualificationReferenceMapper
        .qualificationReferenceToQualificationReferenceDTO(qualificationReference);

    mockMvc.perform(post("/api/qualification-reference")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationReferenceDTO)))
        .andExpect(status().isBadRequest());

    List<QualificationReference> qualificationReferenceList = qualificationReferenceRepository
        .findAll();
    assertThat(qualificationReferenceList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllQualificationReferences() throws Exception {
    // Initialize the database
    qualificationReferenceRepository.saveAndFlush(qualificationReference);

    // Get all the qualificationReferenceList
    mockMvc.perform(get("/api/qualification-reference?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(qualificationReference.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
  }

  @Test
  @Transactional
  public void getQualificationReferences() throws Exception {
    // Initialize the database
    QualificationReference unencodedQualificationReference = new QualificationReference()
        .code(UNENCODED_CODE)
        .label(UNENCODED_LABEL);
    qualificationReferenceRepository.saveAndFlush(unencodedQualificationReference);

    // Get the qualificationReferenceList
    mockMvc
        .perform(get("/api/qualification-reference?searchQuery=\"Te%24t\"&sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(unencodedQualificationReference.getId().intValue()))
        .andExpect(jsonPath("$.[*].code").value(UNENCODED_CODE))
        .andExpect(jsonPath("$.[*].label").value(UNENCODED_LABEL));
  }

  @Test
  @Transactional
  public void getQualificationReference() throws Exception {
    // Initialize the database
    qualificationReferenceRepository.saveAndFlush(qualificationReference);

    // Get the qualificationReference
    mockMvc
        .perform(get("/api/qualification-reference/{id}", qualificationReference.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(qualificationReference.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingQualificationReference() throws Exception {
    // Get the qualificationReference
    mockMvc
        .perform(get("/api/qualification-reference/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateQualificationReference() throws Exception {
    // Initialize the database
    qualificationReferenceRepository.saveAndFlush(qualificationReference);
    int databaseSizeBeforeUpdate = qualificationReferenceRepository.findAll().size();

    // Update the qualificationReference
    QualificationReference updatedQualificationReference = qualificationReferenceRepository
        .findOne(qualificationReference.getId());
    updatedQualificationReference
        .code(UPDATED_CODE)
        .label(UPDATED_LABEL);
    QualificationReferenceDTO qualificationReferenceDTO = qualificationReferenceMapper
        .qualificationReferenceToQualificationReferenceDTO(updatedQualificationReference);

    mockMvc.perform(put("/api/qualification-reference")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationReferenceDTO)))
        .andExpect(status().isOk());

    // Validate the QualificationReference in the database
    List<QualificationReference> qualificationReferenceList = qualificationReferenceRepository
        .findAll();
    assertThat(qualificationReferenceList).hasSize(databaseSizeBeforeUpdate);
    QualificationReference testQualificationReference = qualificationReferenceList
        .get(qualificationReferenceList.size() - 1);
    assertThat(testQualificationReference.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testQualificationReference.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  @Transactional
  public void updateNonExistingQualificationReference() throws Exception {
    int databaseSizeBeforeUpdate = qualificationReferenceRepository.findAll().size();

    // Create the QualificationReference
    QualificationReferenceDTO qualificationReferenceDTO = qualificationReferenceMapper
        .qualificationReferenceToQualificationReferenceDTO(qualificationReference);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    mockMvc.perform(put("/api/qualification-reference")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qualificationReferenceDTO)))
        .andExpect(status().isCreated());

    // Validate the QualificationReference in the database
    List<QualificationReference> qualificationReferenceList = qualificationReferenceRepository
        .findAll();
    assertThat(qualificationReferenceList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteQualificationReference() throws Exception {
    // Initialize the database
    qualificationReferenceRepository.saveAndFlush(qualificationReference);
    int databaseSizeBeforeDelete = qualificationReferenceRepository.findAll().size();

    // Get the QualificationReference
    mockMvc
        .perform(delete("/api/qualification-reference/{id}", qualificationReference.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<QualificationReference> qualificationReferenceList = qualificationReferenceRepository
        .findAll();
    assertThat(qualificationReferenceList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(QualificationReference.class);
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
    qualificationReferenceRepository.saveAndFlush(qualificationReference);

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
    qualificationReference.setStatus(Status.INACTIVE);
    qualificationReferenceRepository.saveAndFlush(qualificationReference);

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
    qualificationReference.setStatus(Status.CURRENT);
    qualificationReferenceRepository.saveAndFlush(qualificationReference);

    String columnFilter = URLEncoder.encode("{\"status\":[\"CURRENT\"]}", CharEncoding.UTF_8);

    mockMvc.perform(post(EXISTS_ENDPOINT + "?columnFilters=" + columnFilter)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(DEFAULT_CODE)))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }
}
