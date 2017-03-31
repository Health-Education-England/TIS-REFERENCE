package com.transformuk.hee.tis.reference.web.rest;

import com.transformuk.hee.tis.reference.ReferenceApp;
import com.transformuk.hee.tis.reference.domain.GdcStatus;
import com.transformuk.hee.tis.reference.repository.GdcStatusRepository;
import com.transformuk.hee.tis.reference.service.dto.GdcStatusDTO;
import com.transformuk.hee.tis.reference.service.mapper.GdcStatusMapper;
import com.transformuk.hee.tis.reference.web.rest.errors.ExceptionTranslator;
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
 * Test class for the GdcStatusResource REST controller.
 *
 * @see GdcStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReferenceApp.class)
public class GdcStatusResourceIntTest {

	private static final String DEFAULT_CODE = "AAAAAAAAAA";
	private static final String UPDATED_CODE = "BBBBBBBBBB";

	private static final String DEFAULT_LABEL = "AAAAAAAAAA";
	private static final String UPDATED_LABEL = "BBBBBBBBBB";

	@Autowired
	private GdcStatusRepository gdcStatusRepository;

	@Autowired
	private GdcStatusMapper gdcStatusMapper;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restGdcStatusMockMvc;

	private GdcStatus gdcStatus;

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static GdcStatus createEntity(EntityManager em) {
		GdcStatus gdcStatus = new GdcStatus()
				.code(DEFAULT_CODE)
				.label(DEFAULT_LABEL);
		return gdcStatus;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		GdcStatusResource gdcStatusResource = new GdcStatusResource(gdcStatusRepository, gdcStatusMapper);
		this.restGdcStatusMockMvc = MockMvcBuilders.standaloneSetup(gdcStatusResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	@Before
	public void initTest() {
		gdcStatus = createEntity(em);
	}

	@Test
	@Transactional
	public void createGdcStatus() throws Exception {
		int databaseSizeBeforeCreate = gdcStatusRepository.findAll().size();

		// Create the GdcStatus
		GdcStatusDTO gdcStatusDTO = gdcStatusMapper.gdcStatusToGdcStatusDTO(gdcStatus);
		restGdcStatusMockMvc.perform(post("/api/gdc-statuses")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gdcStatusDTO)))
				.andExpect(status().isCreated());

		// Validate the GdcStatus in the database
		List<GdcStatus> gdcStatusList = gdcStatusRepository.findAll();
		assertThat(gdcStatusList).hasSize(databaseSizeBeforeCreate + 1);
		GdcStatus testGdcStatus = gdcStatusList.get(gdcStatusList.size() - 1);
		assertThat(testGdcStatus.getCode()).isEqualTo(DEFAULT_CODE);
		assertThat(testGdcStatus.getLabel()).isEqualTo(DEFAULT_LABEL);
	}

	@Test
	@Transactional
	public void createGdcStatusWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = gdcStatusRepository.findAll().size();

		// Create the GdcStatus with an existing ID
		gdcStatus.setId(1L);
		GdcStatusDTO gdcStatusDTO = gdcStatusMapper.gdcStatusToGdcStatusDTO(gdcStatus);

		// An entity with an existing ID cannot be created, so this API call must fail
		restGdcStatusMockMvc.perform(post("/api/gdc-statuses")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gdcStatusDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<GdcStatus> gdcStatusList = gdcStatusRepository.findAll();
		assertThat(gdcStatusList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void checkCodeIsRequired() throws Exception {
		int databaseSizeBeforeTest = gdcStatusRepository.findAll().size();
		// set the field null
		gdcStatus.setCode(null);

		// Create the GdcStatus, which fails.
		GdcStatusDTO gdcStatusDTO = gdcStatusMapper.gdcStatusToGdcStatusDTO(gdcStatus);

		restGdcStatusMockMvc.perform(post("/api/gdc-statuses")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gdcStatusDTO)))
				.andExpect(status().isBadRequest());

		List<GdcStatus> gdcStatusList = gdcStatusRepository.findAll();
		assertThat(gdcStatusList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkLabelIsRequired() throws Exception {
		int databaseSizeBeforeTest = gdcStatusRepository.findAll().size();
		// set the field null
		gdcStatus.setLabel(null);

		// Create the GdcStatus, which fails.
		GdcStatusDTO gdcStatusDTO = gdcStatusMapper.gdcStatusToGdcStatusDTO(gdcStatus);

		restGdcStatusMockMvc.perform(post("/api/gdc-statuses")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gdcStatusDTO)))
				.andExpect(status().isBadRequest());

		List<GdcStatus> gdcStatusList = gdcStatusRepository.findAll();
		assertThat(gdcStatusList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void getAllGdcStatuses() throws Exception {
		// Initialize the database
		gdcStatusRepository.saveAndFlush(gdcStatus);

		// Get all the gdcStatusList
		restGdcStatusMockMvc.perform(get("/api/gdc-statuses?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(gdcStatus.getId().intValue())))
				.andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
				.andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
	}

	@Test
	@Transactional
	public void getGdcStatus() throws Exception {
		// Initialize the database
		gdcStatusRepository.saveAndFlush(gdcStatus);

		// Get the gdcStatus
		restGdcStatusMockMvc.perform(get("/api/gdc-statuses/{id}", gdcStatus.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(gdcStatus.getId().intValue()))
				.andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
				.andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingGdcStatus() throws Exception {
		// Get the gdcStatus
		restGdcStatusMockMvc.perform(get("/api/gdc-statuses/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateGdcStatus() throws Exception {
		// Initialize the database
		gdcStatusRepository.saveAndFlush(gdcStatus);
		int databaseSizeBeforeUpdate = gdcStatusRepository.findAll().size();

		// Update the gdcStatus
		GdcStatus updatedGdcStatus = gdcStatusRepository.findOne(gdcStatus.getId());
		updatedGdcStatus
				.code(UPDATED_CODE)
				.label(UPDATED_LABEL);
		GdcStatusDTO gdcStatusDTO = gdcStatusMapper.gdcStatusToGdcStatusDTO(updatedGdcStatus);

		restGdcStatusMockMvc.perform(put("/api/gdc-statuses")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gdcStatusDTO)))
				.andExpect(status().isOk());

		// Validate the GdcStatus in the database
		List<GdcStatus> gdcStatusList = gdcStatusRepository.findAll();
		assertThat(gdcStatusList).hasSize(databaseSizeBeforeUpdate);
		GdcStatus testGdcStatus = gdcStatusList.get(gdcStatusList.size() - 1);
		assertThat(testGdcStatus.getCode()).isEqualTo(UPDATED_CODE);
		assertThat(testGdcStatus.getLabel()).isEqualTo(UPDATED_LABEL);
	}

	@Test
	@Transactional
	public void updateNonExistingGdcStatus() throws Exception {
		int databaseSizeBeforeUpdate = gdcStatusRepository.findAll().size();

		// Create the GdcStatus
		GdcStatusDTO gdcStatusDTO = gdcStatusMapper.gdcStatusToGdcStatusDTO(gdcStatus);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restGdcStatusMockMvc.perform(put("/api/gdc-statuses")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gdcStatusDTO)))
				.andExpect(status().isCreated());

		// Validate the GdcStatus in the database
		List<GdcStatus> gdcStatusList = gdcStatusRepository.findAll();
		assertThat(gdcStatusList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteGdcStatus() throws Exception {
		// Initialize the database
		gdcStatusRepository.saveAndFlush(gdcStatus);
		int databaseSizeBeforeDelete = gdcStatusRepository.findAll().size();

		// Get the gdcStatus
		restGdcStatusMockMvc.perform(delete("/api/gdc-statuses/{id}", gdcStatus.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<GdcStatus> gdcStatusList = gdcStatusRepository.findAll();
		assertThat(gdcStatusList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(GdcStatus.class);
	}
}
