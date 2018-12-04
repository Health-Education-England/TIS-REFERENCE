package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.api.dto.StatusDTO;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.Status;
import com.transformuk.hee.tis.reference.service.repository.StatusRepository;
import com.transformuk.hee.tis.reference.service.service.mapper.StatusMapper;
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
 * Test class for the StatusResource REST controller.
 *
 * @see StatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class StatusResourceIntTest {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";

  @Autowired
  private StatusRepository statusRepository;

  @Autowired
  private StatusMapper statusMapper;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private EntityManager em;

  private MockMvc restStatusMockMvc;

  private Status status;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Status createEntity(EntityManager em) {
    Status status = new Status()
        .code(DEFAULT_CODE)
        .label(DEFAULT_LABEL);
    return status;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    StatusResource statusResource = new StatusResource(statusRepository, statusMapper);
    this.restStatusMockMvc = MockMvcBuilders.standaloneSetup(statusResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    status = createEntity(em);
  }

  @Test
  @Transactional
  public void createStatus() throws Exception {
    int databaseSizeBeforeCreate = statusRepository.findAll().size();

    // Create the Status
    StatusDTO statusDTO = statusMapper.statusToStatusDTO(status);
    restStatusMockMvc.perform(post("/api/statuses")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(statusDTO)))
        .andExpect(status().isCreated());

    // Validate the Status in the database
    List<Status> statusList = statusRepository.findAll();
    assertThat(statusList).hasSize(databaseSizeBeforeCreate + 1);
    Status testStatus = statusList.get(statusList.size() - 1);
    assertThat(testStatus.getCode()).isEqualTo(DEFAULT_CODE);
    assertThat(testStatus.getLabel()).isEqualTo(DEFAULT_LABEL);
  }

  @Test
  @Transactional
  public void createStatusWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = statusRepository.findAll().size();

    // Create the Status with an existing ID
    status.setId(1L);
    StatusDTO statusDTO = statusMapper.statusToStatusDTO(status);

    // An entity with an existing ID cannot be created, so this API call must fail
    restStatusMockMvc.perform(post("/api/statuses")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(statusDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<Status> statusList = statusRepository.findAll();
    assertThat(statusList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = statusRepository.findAll().size();
    // set the field null
    status.setCode(null);

    // Create the Status, which fails.
    StatusDTO statusDTO = statusMapper.statusToStatusDTO(status);

    restStatusMockMvc.perform(post("/api/statuses")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(statusDTO)))
        .andExpect(status().isBadRequest());

    List<Status> statusList = statusRepository.findAll();
    assertThat(statusList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkLabelIsRequired() throws Exception {
    int databaseSizeBeforeTest = statusRepository.findAll().size();
    // set the field null
    status.setLabel(null);

    // Create the Status, which fails.
    StatusDTO statusDTO = statusMapper.statusToStatusDTO(status);

    restStatusMockMvc.perform(post("/api/statuses")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(statusDTO)))
        .andExpect(status().isBadRequest());

    List<Status> statusList = statusRepository.findAll();
    assertThat(statusList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllStatuses() throws Exception {
    // Initialize the database
    statusRepository.saveAndFlush(status);

    // Get all the statusList
    restStatusMockMvc.perform(get("/api/statuses?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(status.getId().intValue())))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
  }

  @Test
  @Transactional
  public void getStatus() throws Exception {
    // Initialize the database
    statusRepository.saveAndFlush(status);

    // Get the status
    restStatusMockMvc.perform(get("/api/statuses/{id}", status.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(status.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingStatus() throws Exception {
    // Get the status
    restStatusMockMvc.perform(get("/api/statuses/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateStatus() throws Exception {
    // Initialize the database
    statusRepository.saveAndFlush(status);
    int databaseSizeBeforeUpdate = statusRepository.findAll().size();

    // Update the status
      Status updatedStatus = statusRepository.findById(status.getId()).orElse(null);
    updatedStatus
        .code(UPDATED_CODE)
        .label(UPDATED_LABEL);
    StatusDTO statusDTO = statusMapper.statusToStatusDTO(updatedStatus);

    restStatusMockMvc.perform(put("/api/statuses")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(statusDTO)))
        .andExpect(status().isOk());

    // Validate the Status in the database
    List<Status> statusList = statusRepository.findAll();
    assertThat(statusList).hasSize(databaseSizeBeforeUpdate);
    Status testStatus = statusList.get(statusList.size() - 1);
    assertThat(testStatus.getCode()).isEqualTo(UPDATED_CODE);
    assertThat(testStatus.getLabel()).isEqualTo(UPDATED_LABEL);
  }

  @Test
  @Transactional
  public void updateNonExistingStatus() throws Exception {
    int databaseSizeBeforeUpdate = statusRepository.findAll().size();

    // Create the Status
    StatusDTO statusDTO = statusMapper.statusToStatusDTO(status);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restStatusMockMvc.perform(put("/api/statuses")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(statusDTO)))
        .andExpect(status().isCreated());

    // Validate the Status in the database
    List<Status> statusList = statusRepository.findAll();
    assertThat(statusList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteStatus() throws Exception {
    // Initialize the database
    statusRepository.saveAndFlush(status);
    int databaseSizeBeforeDelete = statusRepository.findAll().size();

    // Get the status
    restStatusMockMvc.perform(delete("/api/statuses/{id}", status.getId())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<Status> statusList = statusRepository.findAll();
    assertThat(statusList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Status.class);
  }
}
