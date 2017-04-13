package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.model.TrainingNumberType;
import com.transformuk.hee.tis.reference.service.repository.TrainingNumberTypeRepository;
import com.transformuk.hee.tis.reference.api.dto.TrainingNumberTypeDTO;
import com.transformuk.hee.tis.reference.service.service.mapper.TrainingNumberTypeMapper;
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
 * Test class for the TrainingNumberTypeResource REST controller.
 *
 * @see TrainingNumberTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TrainingNumberTypeResourceIntTest {

	private static final String DEFAULT_CODE = "AAAAAAAAAA";
	private static final String UPDATED_CODE = "BBBBBBBBBB";

	private static final String DEFAULT_LABEL = "AAAAAAAAAA";
	private static final String UPDATED_LABEL = "BBBBBBBBBB";

	@Autowired
	private TrainingNumberTypeRepository trainingNumberTypeRepository;

	@Autowired
	private TrainingNumberTypeMapper trainingNumberTypeMapper;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restTrainingNumberTypeMockMvc;

	private TrainingNumberType trainingNumberType;

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static TrainingNumberType createEntity(EntityManager em) {
		TrainingNumberType trainingNumberType = new TrainingNumberType()
				.code(DEFAULT_CODE)
				.label(DEFAULT_LABEL);
		return trainingNumberType;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		TrainingNumberTypeResource trainingNumberTypeResource = new TrainingNumberTypeResource(trainingNumberTypeRepository, trainingNumberTypeMapper);
		this.restTrainingNumberTypeMockMvc = MockMvcBuilders.standaloneSetup(trainingNumberTypeResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	@Before
	public void initTest() {
		trainingNumberType = createEntity(em);
	}

	@Test
	@Transactional
	public void createTrainingNumberType() throws Exception {
		int databaseSizeBeforeCreate = trainingNumberTypeRepository.findAll().size();

		// Create the TrainingNumberType
		TrainingNumberTypeDTO trainingNumberTypeDTO = trainingNumberTypeMapper.trainingNumberTypeToTrainingNumberTypeDTO(trainingNumberType);
		restTrainingNumberTypeMockMvc.perform(post("/api/training-number-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(trainingNumberTypeDTO)))
				.andExpect(status().isCreated());

		// Validate the TrainingNumberType in the database
		List<TrainingNumberType> trainingNumberTypeList = trainingNumberTypeRepository.findAll();
		assertThat(trainingNumberTypeList).hasSize(databaseSizeBeforeCreate + 1);
		TrainingNumberType testTrainingNumberType = trainingNumberTypeList.get(trainingNumberTypeList.size() - 1);
		assertThat(testTrainingNumberType.getCode()).isEqualTo(DEFAULT_CODE);
		assertThat(testTrainingNumberType.getLabel()).isEqualTo(DEFAULT_LABEL);
	}

	@Test
	@Transactional
	public void createTrainingNumberTypeWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = trainingNumberTypeRepository.findAll().size();

		// Create the TrainingNumberType with an existing ID
		trainingNumberType.setId(1L);
		TrainingNumberTypeDTO trainingNumberTypeDTO = trainingNumberTypeMapper.trainingNumberTypeToTrainingNumberTypeDTO(trainingNumberType);

		// An entity with an existing ID cannot be created, so this API call must fail
		restTrainingNumberTypeMockMvc.perform(post("/api/training-number-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(trainingNumberTypeDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<TrainingNumberType> trainingNumberTypeList = trainingNumberTypeRepository.findAll();
		assertThat(trainingNumberTypeList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void checkCodeIsRequired() throws Exception {
		int databaseSizeBeforeTest = trainingNumberTypeRepository.findAll().size();
		// set the field null
		trainingNumberType.setCode(null);

		// Create the TrainingNumberType, which fails.
		TrainingNumberTypeDTO trainingNumberTypeDTO = trainingNumberTypeMapper.trainingNumberTypeToTrainingNumberTypeDTO(trainingNumberType);

		restTrainingNumberTypeMockMvc.perform(post("/api/training-number-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(trainingNumberTypeDTO)))
				.andExpect(status().isBadRequest());

		List<TrainingNumberType> trainingNumberTypeList = trainingNumberTypeRepository.findAll();
		assertThat(trainingNumberTypeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkLabelIsRequired() throws Exception {
		int databaseSizeBeforeTest = trainingNumberTypeRepository.findAll().size();
		// set the field null
		trainingNumberType.setLabel(null);

		// Create the TrainingNumberType, which fails.
		TrainingNumberTypeDTO trainingNumberTypeDTO = trainingNumberTypeMapper.trainingNumberTypeToTrainingNumberTypeDTO(trainingNumberType);

		restTrainingNumberTypeMockMvc.perform(post("/api/training-number-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(trainingNumberTypeDTO)))
				.andExpect(status().isBadRequest());

		List<TrainingNumberType> trainingNumberTypeList = trainingNumberTypeRepository.findAll();
		assertThat(trainingNumberTypeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void getAllTrainingNumberTypes() throws Exception {
		// Initialize the database
		trainingNumberTypeRepository.saveAndFlush(trainingNumberType);

		// Get all the trainingNumberTypeList
		restTrainingNumberTypeMockMvc.perform(get("/api/training-number-types?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(trainingNumberType.getId().intValue())))
				.andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
				.andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
	}

	@Test
	@Transactional
	public void getTrainingNumberType() throws Exception {
		// Initialize the database
		trainingNumberTypeRepository.saveAndFlush(trainingNumberType);

		// Get the trainingNumberType
		restTrainingNumberTypeMockMvc.perform(get("/api/training-number-types/{id}", trainingNumberType.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(trainingNumberType.getId().intValue()))
				.andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
				.andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingTrainingNumberType() throws Exception {
		// Get the trainingNumberType
		restTrainingNumberTypeMockMvc.perform(get("/api/training-number-types/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateTrainingNumberType() throws Exception {
		// Initialize the database
		trainingNumberTypeRepository.saveAndFlush(trainingNumberType);
		int databaseSizeBeforeUpdate = trainingNumberTypeRepository.findAll().size();

		// Update the trainingNumberType
		TrainingNumberType updatedTrainingNumberType = trainingNumberTypeRepository.findOne(trainingNumberType.getId());
		updatedTrainingNumberType
				.code(UPDATED_CODE)
				.label(UPDATED_LABEL);
		TrainingNumberTypeDTO trainingNumberTypeDTO = trainingNumberTypeMapper.trainingNumberTypeToTrainingNumberTypeDTO(updatedTrainingNumberType);

		restTrainingNumberTypeMockMvc.perform(put("/api/training-number-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(trainingNumberTypeDTO)))
				.andExpect(status().isOk());

		// Validate the TrainingNumberType in the database
		List<TrainingNumberType> trainingNumberTypeList = trainingNumberTypeRepository.findAll();
		assertThat(trainingNumberTypeList).hasSize(databaseSizeBeforeUpdate);
		TrainingNumberType testTrainingNumberType = trainingNumberTypeList.get(trainingNumberTypeList.size() - 1);
		assertThat(testTrainingNumberType.getCode()).isEqualTo(UPDATED_CODE);
		assertThat(testTrainingNumberType.getLabel()).isEqualTo(UPDATED_LABEL);
	}

	@Test
	@Transactional
	public void updateNonExistingTrainingNumberType() throws Exception {
		int databaseSizeBeforeUpdate = trainingNumberTypeRepository.findAll().size();

		// Create the TrainingNumberType
		TrainingNumberTypeDTO trainingNumberTypeDTO = trainingNumberTypeMapper.trainingNumberTypeToTrainingNumberTypeDTO(trainingNumberType);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restTrainingNumberTypeMockMvc.perform(put("/api/training-number-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(trainingNumberTypeDTO)))
				.andExpect(status().isCreated());

		// Validate the TrainingNumberType in the database
		List<TrainingNumberType> trainingNumberTypeList = trainingNumberTypeRepository.findAll();
		assertThat(trainingNumberTypeList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteTrainingNumberType() throws Exception {
		// Initialize the database
		trainingNumberTypeRepository.saveAndFlush(trainingNumberType);
		int databaseSizeBeforeDelete = trainingNumberTypeRepository.findAll().size();

		// Get the trainingNumberType
		restTrainingNumberTypeMockMvc.perform(delete("/api/training-number-types/{id}", trainingNumberType.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<TrainingNumberType> trainingNumberTypeList = trainingNumberTypeRepository.findAll();
		assertThat(trainingNumberTypeList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(TrainingNumberType.class);
	}
}
