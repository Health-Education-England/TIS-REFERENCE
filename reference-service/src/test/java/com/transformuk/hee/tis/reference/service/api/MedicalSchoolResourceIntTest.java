package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.model.MedicalSchool;
import com.transformuk.hee.tis.reference.service.repository.MedicalSchoolRepository;
import com.transformuk.hee.tis.reference.service.api.dto.MedicalSchoolDTO;
import com.transformuk.hee.tis.reference.service.service.mapper.MedicalSchoolMapper;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
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
 * Test class for the MedicalSchoolResource REST controller.
 *
 * @see MedicalSchoolResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MedicalSchoolResourceIntTest {

	private static final String DEFAULT_CODE = "AAAAAAAAAA";
	private static final String UPDATED_CODE = "BBBBBBBBBB";

	private static final String DEFAULT_LABEL = "AAAAAAAAAA";
	private static final String UPDATED_LABEL = "BBBBBBBBBB";

	@Autowired
	private MedicalSchoolRepository medicalSchoolRepository;

	@Autowired
	private MedicalSchoolMapper medicalSchoolMapper;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restMedicalSchoolMockMvc;

	private MedicalSchool medicalSchool;

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static MedicalSchool createEntity(EntityManager em) {
		MedicalSchool medicalSchool = new MedicalSchool()
				.code(DEFAULT_CODE)
				.label(DEFAULT_LABEL);
		return medicalSchool;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		MedicalSchoolResource medicalSchoolResource = new MedicalSchoolResource(medicalSchoolRepository, medicalSchoolMapper);
		this.restMedicalSchoolMockMvc = MockMvcBuilders.standaloneSetup(medicalSchoolResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	@Before
	public void initTest() {
		medicalSchool = createEntity(em);
	}

	@Test
	@Transactional
	public void createMedicalSchool() throws Exception {
		int databaseSizeBeforeCreate = medicalSchoolRepository.findAll().size();

		// Create the MedicalSchool
		MedicalSchoolDTO medicalSchoolDTO = medicalSchoolMapper.medicalSchoolToMedicalSchoolDTO(medicalSchool);
		restMedicalSchoolMockMvc.perform(post("/api/medical-schools")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(medicalSchoolDTO)))
				.andExpect(status().isCreated());

		// Validate the MedicalSchool in the database
		List<MedicalSchool> medicalSchoolList = medicalSchoolRepository.findAll();
		assertThat(medicalSchoolList).hasSize(databaseSizeBeforeCreate + 1);
		MedicalSchool testMedicalSchool = medicalSchoolList.get(medicalSchoolList.size() - 1);
		assertThat(testMedicalSchool.getCode()).isEqualTo(DEFAULT_CODE);
		assertThat(testMedicalSchool.getLabel()).isEqualTo(DEFAULT_LABEL);
	}

	@Test
	@Transactional
	public void createMedicalSchoolWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = medicalSchoolRepository.findAll().size();

		// Create the MedicalSchool with an existing ID
		medicalSchool.setId(1L);
		MedicalSchoolDTO medicalSchoolDTO = medicalSchoolMapper.medicalSchoolToMedicalSchoolDTO(medicalSchool);

		// An entity with an existing ID cannot be created, so this API call must fail
		restMedicalSchoolMockMvc.perform(post("/api/medical-schools")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(medicalSchoolDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<MedicalSchool> medicalSchoolList = medicalSchoolRepository.findAll();
		assertThat(medicalSchoolList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void checkCodeIsRequired() throws Exception {
		int databaseSizeBeforeTest = medicalSchoolRepository.findAll().size();
		// set the field null
		medicalSchool.setCode(null);

		// Create the MedicalSchool, which fails.
		MedicalSchoolDTO medicalSchoolDTO = medicalSchoolMapper.medicalSchoolToMedicalSchoolDTO(medicalSchool);

		restMedicalSchoolMockMvc.perform(post("/api/medical-schools")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(medicalSchoolDTO)))
				.andExpect(status().isBadRequest());

		List<MedicalSchool> medicalSchoolList = medicalSchoolRepository.findAll();
		assertThat(medicalSchoolList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkLabelIsRequired() throws Exception {
		int databaseSizeBeforeTest = medicalSchoolRepository.findAll().size();
		// set the field null
		medicalSchool.setLabel(null);

		// Create the MedicalSchool, which fails.
		MedicalSchoolDTO medicalSchoolDTO = medicalSchoolMapper.medicalSchoolToMedicalSchoolDTO(medicalSchool);

		restMedicalSchoolMockMvc.perform(post("/api/medical-schools")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(medicalSchoolDTO)))
				.andExpect(status().isBadRequest());

		List<MedicalSchool> medicalSchoolList = medicalSchoolRepository.findAll();
		assertThat(medicalSchoolList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void getAllMedicalSchools() throws Exception {
		// Initialize the database
		medicalSchoolRepository.saveAndFlush(medicalSchool);

		// Get all the medicalSchoolList
		restMedicalSchoolMockMvc.perform(get("/api/medical-schools?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(medicalSchool.getId().intValue())))
				.andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
				.andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
	}

	@Test
	@Transactional
	public void getMedicalSchool() throws Exception {
		// Initialize the database
		medicalSchoolRepository.saveAndFlush(medicalSchool);

		// Get the medicalSchool
		restMedicalSchoolMockMvc.perform(get("/api/medical-schools/{id}", medicalSchool.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(medicalSchool.getId().intValue()))
				.andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
				.andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingMedicalSchool() throws Exception {
		// Get the medicalSchool
		restMedicalSchoolMockMvc.perform(get("/api/medical-schools/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateMedicalSchool() throws Exception {
		// Initialize the database
		medicalSchoolRepository.saveAndFlush(medicalSchool);
		int databaseSizeBeforeUpdate = medicalSchoolRepository.findAll().size();

		// Update the medicalSchool
		MedicalSchool updatedMedicalSchool = medicalSchoolRepository.findOne(medicalSchool.getId());
		updatedMedicalSchool
				.code(UPDATED_CODE)
				.label(UPDATED_LABEL);
		MedicalSchoolDTO medicalSchoolDTO = medicalSchoolMapper.medicalSchoolToMedicalSchoolDTO(updatedMedicalSchool);

		restMedicalSchoolMockMvc.perform(put("/api/medical-schools")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(medicalSchoolDTO)))
				.andExpect(status().isOk());

		// Validate the MedicalSchool in the database
		List<MedicalSchool> medicalSchoolList = medicalSchoolRepository.findAll();
		assertThat(medicalSchoolList).hasSize(databaseSizeBeforeUpdate);
		MedicalSchool testMedicalSchool = medicalSchoolList.get(medicalSchoolList.size() - 1);
		assertThat(testMedicalSchool.getCode()).isEqualTo(UPDATED_CODE);
		assertThat(testMedicalSchool.getLabel()).isEqualTo(UPDATED_LABEL);
	}

	@Test
	@Transactional
	public void updateNonExistingMedicalSchool() throws Exception {
		int databaseSizeBeforeUpdate = medicalSchoolRepository.findAll().size();

		// Create the MedicalSchool
		MedicalSchoolDTO medicalSchoolDTO = medicalSchoolMapper.medicalSchoolToMedicalSchoolDTO(medicalSchool);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restMedicalSchoolMockMvc.perform(put("/api/medical-schools")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(medicalSchoolDTO)))
				.andExpect(status().isCreated());

		// Validate the MedicalSchool in the database
		List<MedicalSchool> medicalSchoolList = medicalSchoolRepository.findAll();
		assertThat(medicalSchoolList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteMedicalSchool() throws Exception {
		// Initialize the database
		medicalSchoolRepository.saveAndFlush(medicalSchool);
		int databaseSizeBeforeDelete = medicalSchoolRepository.findAll().size();

		// Get the medicalSchool
		restMedicalSchoolMockMvc.perform(delete("/api/medical-schools/{id}", medicalSchool.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<MedicalSchool> medicalSchoolList = medicalSchoolRepository.findAll();
		assertThat(medicalSchoolList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(MedicalSchool.class);
	}
}
