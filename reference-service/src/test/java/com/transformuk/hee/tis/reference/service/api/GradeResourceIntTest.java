package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.domain.Grade;
import com.transformuk.hee.tis.reference.service.repository.GradeRepository;
import com.transformuk.hee.tis.reference.service.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.service.service.mapper.GradeMapper;
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
 * Test class for the GradeResource REST controller.
 *
 * @see GradeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GradeResourceIntTest {

	private static final String DEFAULT_ABBREVIATION = "AAAAAAAAAA";
	private static final String UPDATED_ABBREVIATION = "BBBBBBBBBB";

	private static final String DEFAULT_NAME = "AAAAAAAAAA";
	private static final String UPDATED_NAME = "BBBBBBBBBB";

	private static final String DEFAULT_LABEL = "AAAAAAAAAA";
	private static final String UPDATED_LABEL = "BBBBBBBBBB";

	private static final Boolean DEFAULT_TRAINING_GRADE = false;
	private static final Boolean UPDATED_TRAINING_GRADE = true;

	private static final Boolean DEFAULT_POST_GRADE = false;
	private static final Boolean UPDATED_POST_GRADE = true;

	private static final Boolean DEFAULT_PLACEMENT_GRADE = false;
	private static final Boolean UPDATED_PLACEMENT_GRADE = true;

	@Autowired
	private GradeRepository gradeRepository;

	@Autowired
	private GradeMapper gradeMapper;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restGradeMockMvc;

	private Grade grade;

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static Grade createEntity(EntityManager em) {
		Grade grade = new Grade()
				.abbreviation(DEFAULT_ABBREVIATION)
				.name(DEFAULT_NAME)
				.label(DEFAULT_LABEL)
				.trainingGrade(DEFAULT_TRAINING_GRADE)
				.postGrade(DEFAULT_POST_GRADE)
				.placementGrade(DEFAULT_PLACEMENT_GRADE);
		return grade;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		GradeResource gradeResource = new GradeResource(gradeRepository, gradeMapper);
		this.restGradeMockMvc = MockMvcBuilders.standaloneSetup(gradeResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	@Before
	public void initTest() {
		grade = createEntity(em);
	}

	@Test
	@Transactional
	public void createGrade() throws Exception {
		int databaseSizeBeforeCreate = gradeRepository.findAll().size();

		// Create the Grade
		GradeDTO gradeDTO = gradeMapper.gradeToGradeDTO(grade);
		restGradeMockMvc.perform(post("/api/grades")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gradeDTO)))
				.andExpect(status().isCreated());

		// Validate the Grade in the database
		List<Grade> gradeList = gradeRepository.findAll();
		assertThat(gradeList).hasSize(databaseSizeBeforeCreate + 1);
		Grade testGrade = gradeList.get(gradeList.size() - 1);
		assertThat(testGrade.getAbbreviation()).isEqualTo(DEFAULT_ABBREVIATION);
		assertThat(testGrade.getName()).isEqualTo(DEFAULT_NAME);
		assertThat(testGrade.getLabel()).isEqualTo(DEFAULT_LABEL);
		assertThat(testGrade.isTrainingGrade()).isEqualTo(DEFAULT_TRAINING_GRADE);
		assertThat(testGrade.isPostGrade()).isEqualTo(DEFAULT_POST_GRADE);
		assertThat(testGrade.isPlacementGrade()).isEqualTo(DEFAULT_PLACEMENT_GRADE);
	}

	@Test
	@Transactional
	public void createGradeWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = gradeRepository.findAll().size();

		// Create the Grade with an existing ID
		grade.setId(1L);
		GradeDTO gradeDTO = gradeMapper.gradeToGradeDTO(grade);

		// An entity with an existing ID cannot be created, so this API call must fail
		restGradeMockMvc.perform(post("/api/grades")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gradeDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<Grade> gradeList = gradeRepository.findAll();
		assertThat(gradeList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void checkAbbreviationIsRequired() throws Exception {
		int databaseSizeBeforeTest = gradeRepository.findAll().size();
		// set the field null
		grade.setAbbreviation(null);

		// Create the Grade, which fails.
		GradeDTO gradeDTO = gradeMapper.gradeToGradeDTO(grade);

		restGradeMockMvc.perform(post("/api/grades")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gradeDTO)))
				.andExpect(status().isBadRequest());

		List<Grade> gradeList = gradeRepository.findAll();
		assertThat(gradeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkTrainingGradeIsRequired() throws Exception {
		int databaseSizeBeforeTest = gradeRepository.findAll().size();
		// set the field null
		grade.setTrainingGrade(null);

		// Create the Grade, which fails.
		GradeDTO gradeDTO = gradeMapper.gradeToGradeDTO(grade);

		restGradeMockMvc.perform(post("/api/grades")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gradeDTO)))
				.andExpect(status().isBadRequest());

		List<Grade> gradeList = gradeRepository.findAll();
		assertThat(gradeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkPostGradeIsRequired() throws Exception {
		int databaseSizeBeforeTest = gradeRepository.findAll().size();
		// set the field null
		grade.setPostGrade(null);

		// Create the Grade, which fails.
		GradeDTO gradeDTO = gradeMapper.gradeToGradeDTO(grade);

		restGradeMockMvc.perform(post("/api/grades")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gradeDTO)))
				.andExpect(status().isBadRequest());

		List<Grade> gradeList = gradeRepository.findAll();
		assertThat(gradeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkPlacementGradeIsRequired() throws Exception {
		int databaseSizeBeforeTest = gradeRepository.findAll().size();
		// set the field null
		grade.setPlacementGrade(null);

		// Create the Grade, which fails.
		GradeDTO gradeDTO = gradeMapper.gradeToGradeDTO(grade);

		restGradeMockMvc.perform(post("/api/grades")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gradeDTO)))
				.andExpect(status().isBadRequest());

		List<Grade> gradeList = gradeRepository.findAll();
		assertThat(gradeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void getAllGrades() throws Exception {
		// Initialize the database
		gradeRepository.saveAndFlush(grade);

		// Get all the gradeList
		restGradeMockMvc.perform(get("/api/grades?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(grade.getId().intValue())))
				.andExpect(jsonPath("$.[*].abbreviation").value(hasItem(DEFAULT_ABBREVIATION.toString())))
				.andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
				.andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
				.andExpect(jsonPath("$.[*].trainingGrade").value(hasItem(DEFAULT_TRAINING_GRADE.booleanValue())))
				.andExpect(jsonPath("$.[*].postGrade").value(hasItem(DEFAULT_POST_GRADE.booleanValue())))
				.andExpect(jsonPath("$.[*].placementGrade").value(hasItem(DEFAULT_PLACEMENT_GRADE.booleanValue())));
	}

	@Test
	@Transactional
	public void getGrade() throws Exception {
		// Initialize the database
		gradeRepository.saveAndFlush(grade);

		// Get the grade
		restGradeMockMvc.perform(get("/api/grades/{id}", grade.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(grade.getId().intValue()))
				.andExpect(jsonPath("$.abbreviation").value(DEFAULT_ABBREVIATION.toString()))
				.andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
				.andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()))
				.andExpect(jsonPath("$.trainingGrade").value(DEFAULT_TRAINING_GRADE.booleanValue()))
				.andExpect(jsonPath("$.postGrade").value(DEFAULT_POST_GRADE.booleanValue()))
				.andExpect(jsonPath("$.placementGrade").value(DEFAULT_PLACEMENT_GRADE.booleanValue()));
	}

	@Test
	@Transactional
	public void getGradeByCode() throws Exception {
		// Initialize the database
		gradeRepository.saveAndFlush(grade);

		// Get all the trustList
		restGradeMockMvc.perform(get("/api/grades/code/ACF"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.abbreviation").value("ACF"))
				.andExpect(jsonPath("$.label").value("Academic Clinical Fellow"));
	}

	@Test
	@Transactional
	public void getNonExistingGrade() throws Exception {
		// Get the grade
		restGradeMockMvc.perform(get("/api/grades/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateGrade() throws Exception {
		// Initialize the database
		gradeRepository.saveAndFlush(grade);
		int databaseSizeBeforeUpdate = gradeRepository.findAll().size();

		// Update the grade
		Grade updatedGrade = gradeRepository.findOne(grade.getId());
		updatedGrade
				.abbreviation(UPDATED_ABBREVIATION)
				.name(UPDATED_NAME)
				.label(UPDATED_LABEL)
				.trainingGrade(UPDATED_TRAINING_GRADE)
				.postGrade(UPDATED_POST_GRADE)
				.placementGrade(UPDATED_PLACEMENT_GRADE);
		GradeDTO gradeDTO = gradeMapper.gradeToGradeDTO(updatedGrade);

		restGradeMockMvc.perform(put("/api/grades")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gradeDTO)))
				.andExpect(status().isOk());

		// Validate the Grade in the database
		List<Grade> gradeList = gradeRepository.findAll();
		assertThat(gradeList).hasSize(databaseSizeBeforeUpdate);
		Grade testGrade = gradeList.get(gradeList.size() - 1);
		assertThat(testGrade.getAbbreviation()).isEqualTo(UPDATED_ABBREVIATION);
		assertThat(testGrade.getName()).isEqualTo(UPDATED_NAME);
		assertThat(testGrade.getLabel()).isEqualTo(UPDATED_LABEL);
		assertThat(testGrade.isTrainingGrade()).isEqualTo(UPDATED_TRAINING_GRADE);
		assertThat(testGrade.isPostGrade()).isEqualTo(UPDATED_POST_GRADE);
		assertThat(testGrade.isPlacementGrade()).isEqualTo(UPDATED_PLACEMENT_GRADE);
	}

	@Test
	@Transactional
	public void updateNonExistingGrade() throws Exception {
		int databaseSizeBeforeUpdate = gradeRepository.findAll().size();

		// Create the Grade
		GradeDTO gradeDTO = gradeMapper.gradeToGradeDTO(grade);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restGradeMockMvc.perform(put("/api/grades")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(gradeDTO)))
				.andExpect(status().isCreated());

		// Validate the Grade in the database
		List<Grade> gradeList = gradeRepository.findAll();
		assertThat(gradeList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteGrade() throws Exception {
		// Initialize the database
		gradeRepository.saveAndFlush(grade);
		int databaseSizeBeforeDelete = gradeRepository.findAll().size();

		// Get the grade
		restGradeMockMvc.perform(delete("/api/grades/{id}", grade.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<Grade> gradeList = gradeRepository.findAll();
		assertThat(gradeList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Grade.class);
	}
}
