package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.model.AssessmentType;
import com.transformuk.hee.tis.reference.service.repository.AssessmentTypeRepository;
import com.transformuk.hee.tis.reference.api.dto.AssessmentTypeDTO;
import com.transformuk.hee.tis.reference.service.service.mapper.AssessmentTypeMapper;
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
 * Test class for the AssessmentTypeResource REST controller.
 *
 * @see AssessmentTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AssessmentTypeResourceIntTest {

	private static final String DEFAULT_CODE = "AAAAAAAAAA";
	private static final String UPDATED_CODE = "BBBBBBBBBB";

	private static final String DEFAULT_LABEL = "AAAAAAAAAA";
	private static final String UPDATED_LABEL = "BBBBBBBBBB";

	@Autowired
	private AssessmentTypeRepository assessmentTypeRepository;

	@Autowired
	private AssessmentTypeMapper assessmentTypeMapper;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restAssessmentTypeMockMvc;

	private AssessmentType assessmentType;

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static AssessmentType createEntity(EntityManager em) {
		AssessmentType assessmentType = new AssessmentType()
				.code(DEFAULT_CODE)
				.label(DEFAULT_LABEL);
		return assessmentType;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		AssessmentTypeResource assessmentTypeResource = new AssessmentTypeResource(assessmentTypeRepository, assessmentTypeMapper);
		this.restAssessmentTypeMockMvc = MockMvcBuilders.standaloneSetup(assessmentTypeResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	@Before
	public void initTest() {
		assessmentType = createEntity(em);
	}

	@Test
	@Transactional
	public void createAssessmentType() throws Exception {
		int databaseSizeBeforeCreate = assessmentTypeRepository.findAll().size();

		// Create the AssessmentType
		AssessmentTypeDTO assessmentTypeDTO = assessmentTypeMapper.assessmentTypeToAssessmentTypeDTO(assessmentType);
		restAssessmentTypeMockMvc.perform(post("/api/assessment-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(assessmentTypeDTO)))
				.andExpect(status().isCreated());

		// Validate the AssessmentType in the database
		List<AssessmentType> assessmentTypeList = assessmentTypeRepository.findAll();
		assertThat(assessmentTypeList).hasSize(databaseSizeBeforeCreate + 1);
		AssessmentType testAssessmentType = assessmentTypeList.get(assessmentTypeList.size() - 1);
		assertThat(testAssessmentType.getCode()).isEqualTo(DEFAULT_CODE);
		assertThat(testAssessmentType.getLabel()).isEqualTo(DEFAULT_LABEL);
	}

	@Test
	@Transactional
	public void createAssessmentTypeWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = assessmentTypeRepository.findAll().size();

		// Create the AssessmentType with an existing ID
		assessmentType.setId(1L);
		AssessmentTypeDTO assessmentTypeDTO = assessmentTypeMapper.assessmentTypeToAssessmentTypeDTO(assessmentType);

		// An entity with an existing ID cannot be created, so this API call must fail
		restAssessmentTypeMockMvc.perform(post("/api/assessment-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(assessmentTypeDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<AssessmentType> assessmentTypeList = assessmentTypeRepository.findAll();
		assertThat(assessmentTypeList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void checkCodeIsRequired() throws Exception {
		int databaseSizeBeforeTest = assessmentTypeRepository.findAll().size();
		// set the field null
		assessmentType.setCode(null);

		// Create the AssessmentType, which fails.
		AssessmentTypeDTO assessmentTypeDTO = assessmentTypeMapper.assessmentTypeToAssessmentTypeDTO(assessmentType);

		restAssessmentTypeMockMvc.perform(post("/api/assessment-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(assessmentTypeDTO)))
				.andExpect(status().isBadRequest());

		List<AssessmentType> assessmentTypeList = assessmentTypeRepository.findAll();
		assertThat(assessmentTypeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkLabelIsRequired() throws Exception {
		int databaseSizeBeforeTest = assessmentTypeRepository.findAll().size();
		// set the field null
		assessmentType.setLabel(null);

		// Create the AssessmentType, which fails.
		AssessmentTypeDTO assessmentTypeDTO = assessmentTypeMapper.assessmentTypeToAssessmentTypeDTO(assessmentType);

		restAssessmentTypeMockMvc.perform(post("/api/assessment-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(assessmentTypeDTO)))
				.andExpect(status().isBadRequest());

		List<AssessmentType> assessmentTypeList = assessmentTypeRepository.findAll();
		assertThat(assessmentTypeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void getAllAssessmentTypes() throws Exception {
		// Initialize the database
		assessmentTypeRepository.saveAndFlush(assessmentType);

		// Get all the assessmentTypeList
		restAssessmentTypeMockMvc.perform(get("/api/assessment-types?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(assessmentType.getId().intValue())))
				.andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
				.andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
	}

	@Test
	@Transactional
	public void getAssessmentType() throws Exception {
		// Initialize the database
		assessmentTypeRepository.saveAndFlush(assessmentType);

		// Get the assessmentType
		restAssessmentTypeMockMvc.perform(get("/api/assessment-types/{id}", assessmentType.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(assessmentType.getId().intValue()))
				.andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
				.andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingAssessmentType() throws Exception {
		// Get the assessmentType
		restAssessmentTypeMockMvc.perform(get("/api/assessment-types/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateAssessmentType() throws Exception {
		// Initialize the database
		assessmentTypeRepository.saveAndFlush(assessmentType);
		int databaseSizeBeforeUpdate = assessmentTypeRepository.findAll().size();

		// Update the assessmentType
		AssessmentType updatedAssessmentType = assessmentTypeRepository.findOne(assessmentType.getId());
		updatedAssessmentType
				.code(UPDATED_CODE)
				.label(UPDATED_LABEL);
		AssessmentTypeDTO assessmentTypeDTO = assessmentTypeMapper.assessmentTypeToAssessmentTypeDTO(updatedAssessmentType);

		restAssessmentTypeMockMvc.perform(put("/api/assessment-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(assessmentTypeDTO)))
				.andExpect(status().isOk());

		// Validate the AssessmentType in the database
		List<AssessmentType> assessmentTypeList = assessmentTypeRepository.findAll();
		assertThat(assessmentTypeList).hasSize(databaseSizeBeforeUpdate);
		AssessmentType testAssessmentType = assessmentTypeList.get(assessmentTypeList.size() - 1);
		assertThat(testAssessmentType.getCode()).isEqualTo(UPDATED_CODE);
		assertThat(testAssessmentType.getLabel()).isEqualTo(UPDATED_LABEL);
	}

	@Test
	@Transactional
	public void updateNonExistingAssessmentType() throws Exception {
		int databaseSizeBeforeUpdate = assessmentTypeRepository.findAll().size();

		// Create the AssessmentType
		AssessmentTypeDTO assessmentTypeDTO = assessmentTypeMapper.assessmentTypeToAssessmentTypeDTO(assessmentType);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restAssessmentTypeMockMvc.perform(put("/api/assessment-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(assessmentTypeDTO)))
				.andExpect(status().isCreated());

		// Validate the AssessmentType in the database
		List<AssessmentType> assessmentTypeList = assessmentTypeRepository.findAll();
		assertThat(assessmentTypeList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteAssessmentType() throws Exception {
		// Initialize the database
		assessmentTypeRepository.saveAndFlush(assessmentType);
		int databaseSizeBeforeDelete = assessmentTypeRepository.findAll().size();

		// Get the assessmentType
		restAssessmentTypeMockMvc.perform(delete("/api/assessment-types/{id}", assessmentType.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<AssessmentType> assessmentTypeList = assessmentTypeRepository.findAll();
		assertThat(assessmentTypeList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(AssessmentType.class);
	}
}
