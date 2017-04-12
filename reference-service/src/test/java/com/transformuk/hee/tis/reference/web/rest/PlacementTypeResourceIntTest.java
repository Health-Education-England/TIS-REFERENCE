package com.transformuk.hee.tis.reference.web.rest;

import com.transformuk.hee.tis.reference.ReferenceApp;
import com.transformuk.hee.tis.reference.domain.PlacementType;
import com.transformuk.hee.tis.reference.repository.PlacementTypeRepository;
import com.transformuk.hee.tis.reference.service.dto.PlacementTypeDTO;
import com.transformuk.hee.tis.reference.service.mapper.PlacementTypeMapper;
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
 * Test class for the PlacementTypeResource REST controller.
 *
 * @see PlacementTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReferenceApp.class)
public class PlacementTypeResourceIntTest {

	private static final String DEFAULT_CODE = "AAAAAAAAAA";
	private static final String UPDATED_CODE = "BBBBBBBBBB";

	private static final String DEFAULT_LABEL = "AAAAAAAAAA";
	private static final String UPDATED_LABEL = "BBBBBBBBBB";

	@Autowired
	private PlacementTypeRepository placementTypeRepository;

	@Autowired
	private PlacementTypeMapper placementTypeMapper;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restPlacementTypeMockMvc;

	private PlacementType placementType;

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static PlacementType createEntity(EntityManager em) {
		PlacementType placementType = new PlacementType()
				.code(DEFAULT_CODE)
				.label(DEFAULT_LABEL);
		return placementType;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		PlacementTypeResource placementTypeResource = new PlacementTypeResource(placementTypeRepository, placementTypeMapper);
		this.restPlacementTypeMockMvc = MockMvcBuilders.standaloneSetup(placementTypeResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	@Before
	public void initTest() {
		placementType = createEntity(em);
	}

	@Test
	@Transactional
	public void createPlacementType() throws Exception {
		int databaseSizeBeforeCreate = placementTypeRepository.findAll().size();

		// Create the PlacementType
		PlacementTypeDTO placementTypeDTO = placementTypeMapper.placementTypeToPlacementTypeDTO(placementType);
		restPlacementTypeMockMvc.perform(post("/api/placement-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(placementTypeDTO)))
				.andExpect(status().isCreated());

		// Validate the PlacementType in the database
		List<PlacementType> placementTypeList = placementTypeRepository.findAll();
		assertThat(placementTypeList).hasSize(databaseSizeBeforeCreate + 1);
		PlacementType testPlacementType = placementTypeList.get(placementTypeList.size() - 1);
		assertThat(testPlacementType.getCode()).isEqualTo(DEFAULT_CODE);
		assertThat(testPlacementType.getLabel()).isEqualTo(DEFAULT_LABEL);
	}

	@Test
	@Transactional
	public void createPlacementTypeWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = placementTypeRepository.findAll().size();

		// Create the PlacementType with an existing ID
		placementType.setId(1L);
		PlacementTypeDTO placementTypeDTO = placementTypeMapper.placementTypeToPlacementTypeDTO(placementType);

		// An entity with an existing ID cannot be created, so this API call must fail
		restPlacementTypeMockMvc.perform(post("/api/placement-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(placementTypeDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<PlacementType> placementTypeList = placementTypeRepository.findAll();
		assertThat(placementTypeList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void checkCodeIsRequired() throws Exception {
		int databaseSizeBeforeTest = placementTypeRepository.findAll().size();
		// set the field null
		placementType.setCode(null);

		// Create the PlacementType, which fails.
		PlacementTypeDTO placementTypeDTO = placementTypeMapper.placementTypeToPlacementTypeDTO(placementType);

		restPlacementTypeMockMvc.perform(post("/api/placement-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(placementTypeDTO)))
				.andExpect(status().isBadRequest());

		List<PlacementType> placementTypeList = placementTypeRepository.findAll();
		assertThat(placementTypeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkLabelIsRequired() throws Exception {
		int databaseSizeBeforeTest = placementTypeRepository.findAll().size();
		// set the field null
		placementType.setLabel(null);

		// Create the PlacementType, which fails.
		PlacementTypeDTO placementTypeDTO = placementTypeMapper.placementTypeToPlacementTypeDTO(placementType);

		restPlacementTypeMockMvc.perform(post("/api/placement-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(placementTypeDTO)))
				.andExpect(status().isBadRequest());

		List<PlacementType> placementTypeList = placementTypeRepository.findAll();
		assertThat(placementTypeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void getAllPlacementTypes() throws Exception {
		// Initialize the database
		placementTypeRepository.saveAndFlush(placementType);

		// Get all the placementTypeList
		restPlacementTypeMockMvc.perform(get("/api/placement-types?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(placementType.getId().intValue())))
				.andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
				.andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
	}

	@Test
	@Transactional
	public void getPlacementType() throws Exception {
		// Initialize the database
		placementTypeRepository.saveAndFlush(placementType);

		// Get the placementType
		restPlacementTypeMockMvc.perform(get("/api/placement-types/{id}", placementType.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(placementType.getId().intValue()))
				.andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
				.andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingPlacementType() throws Exception {
		// Get the placementType
		restPlacementTypeMockMvc.perform(get("/api/placement-types/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updatePlacementType() throws Exception {
		// Initialize the database
		placementTypeRepository.saveAndFlush(placementType);
		int databaseSizeBeforeUpdate = placementTypeRepository.findAll().size();

		// Update the placementType
		PlacementType updatedPlacementType = placementTypeRepository.findOne(placementType.getId());
		updatedPlacementType
				.code(UPDATED_CODE)
				.label(UPDATED_LABEL);
		PlacementTypeDTO placementTypeDTO = placementTypeMapper.placementTypeToPlacementTypeDTO(updatedPlacementType);

		restPlacementTypeMockMvc.perform(put("/api/placement-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(placementTypeDTO)))
				.andExpect(status().isOk());

		// Validate the PlacementType in the database
		List<PlacementType> placementTypeList = placementTypeRepository.findAll();
		assertThat(placementTypeList).hasSize(databaseSizeBeforeUpdate);
		PlacementType testPlacementType = placementTypeList.get(placementTypeList.size() - 1);
		assertThat(testPlacementType.getCode()).isEqualTo(UPDATED_CODE);
		assertThat(testPlacementType.getLabel()).isEqualTo(UPDATED_LABEL);
	}

	@Test
	@Transactional
	public void updateNonExistingPlacementType() throws Exception {
		int databaseSizeBeforeUpdate = placementTypeRepository.findAll().size();

		// Create the PlacementType
		PlacementTypeDTO placementTypeDTO = placementTypeMapper.placementTypeToPlacementTypeDTO(placementType);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restPlacementTypeMockMvc.perform(put("/api/placement-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(placementTypeDTO)))
				.andExpect(status().isCreated());

		// Validate the PlacementType in the database
		List<PlacementType> placementTypeList = placementTypeRepository.findAll();
		assertThat(placementTypeList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deletePlacementType() throws Exception {
		// Initialize the database
		placementTypeRepository.saveAndFlush(placementType);
		int databaseSizeBeforeDelete = placementTypeRepository.findAll().size();

		// Get the placementType
		restPlacementTypeMockMvc.perform(delete("/api/placement-types/{id}", placementType.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<PlacementType> placementTypeList = placementTypeRepository.findAll();
		assertThat(placementTypeList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(PlacementType.class);
	}
}
