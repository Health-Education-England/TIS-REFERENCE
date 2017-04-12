package com.transformuk.hee.tis.reference.web.rest;

import com.transformuk.hee.tis.reference.ReferenceApp;
import com.transformuk.hee.tis.reference.domain.FundingType;
import com.transformuk.hee.tis.reference.repository.FundingTypeRepository;
import com.transformuk.hee.tis.reference.api.dto.FundingTypeDTO;
import com.transformuk.hee.tis.reference.service.mapper.FundingTypeMapper;
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
 * Test class for the FundingTypeResource REST controller.
 *
 * @see FundingTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReferenceApp.class)
public class FundingTypeResourceIntTest {

	private static final String DEFAULT_CODE = "AAAAAAAAAA";
	private static final String UPDATED_CODE = "BBBBBBBBBB";

	private static final String DEFAULT_LABEL = "AAAAAAAAAA";
	private static final String UPDATED_LABEL = "BBBBBBBBBB";

	@Autowired
	private FundingTypeRepository fundingTypeRepository;

	@Autowired
	private FundingTypeMapper fundingTypeMapper;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restFundingTypeMockMvc;

	private FundingType fundingType;

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static FundingType createEntity(EntityManager em) {
		FundingType fundingType = new FundingType()
				.code(DEFAULT_CODE)
				.label(DEFAULT_LABEL);
		return fundingType;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		FundingTypeResource fundingTypeResource = new FundingTypeResource(fundingTypeRepository, fundingTypeMapper);
		this.restFundingTypeMockMvc = MockMvcBuilders.standaloneSetup(fundingTypeResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	@Before
	public void initTest() {
		fundingType = createEntity(em);
	}

	@Test
	@Transactional
	public void createFundingType() throws Exception {
		int databaseSizeBeforeCreate = fundingTypeRepository.findAll().size();

		// Create the FundingType
		FundingTypeDTO fundingTypeDTO = fundingTypeMapper.fundingTypeToFundingTypeDTO(fundingType);
		restFundingTypeMockMvc.perform(post("/api/funding-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(fundingTypeDTO)))
				.andExpect(status().isCreated());

		// Validate the FundingType in the database
		List<FundingType> fundingTypeList = fundingTypeRepository.findAll();
		assertThat(fundingTypeList).hasSize(databaseSizeBeforeCreate + 1);
		FundingType testFundingType = fundingTypeList.get(fundingTypeList.size() - 1);
		assertThat(testFundingType.getCode()).isEqualTo(DEFAULT_CODE);
		assertThat(testFundingType.getLabel()).isEqualTo(DEFAULT_LABEL);
	}

	@Test
	@Transactional
	public void createFundingTypeWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = fundingTypeRepository.findAll().size();

		// Create the FundingType with an existing ID
		fundingType.setId(1L);
		FundingTypeDTO fundingTypeDTO = fundingTypeMapper.fundingTypeToFundingTypeDTO(fundingType);

		// An entity with an existing ID cannot be created, so this API call must fail
		restFundingTypeMockMvc.perform(post("/api/funding-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(fundingTypeDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<FundingType> fundingTypeList = fundingTypeRepository.findAll();
		assertThat(fundingTypeList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void checkCodeIsRequired() throws Exception {
		int databaseSizeBeforeTest = fundingTypeRepository.findAll().size();
		// set the field null
		fundingType.setCode(null);

		// Create the FundingType, which fails.
		FundingTypeDTO fundingTypeDTO = fundingTypeMapper.fundingTypeToFundingTypeDTO(fundingType);

		restFundingTypeMockMvc.perform(post("/api/funding-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(fundingTypeDTO)))
				.andExpect(status().isBadRequest());

		List<FundingType> fundingTypeList = fundingTypeRepository.findAll();
		assertThat(fundingTypeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkLabelIsRequired() throws Exception {
		int databaseSizeBeforeTest = fundingTypeRepository.findAll().size();
		// set the field null
		fundingType.setLabel(null);

		// Create the FundingType, which fails.
		FundingTypeDTO fundingTypeDTO = fundingTypeMapper.fundingTypeToFundingTypeDTO(fundingType);

		restFundingTypeMockMvc.perform(post("/api/funding-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(fundingTypeDTO)))
				.andExpect(status().isBadRequest());

		List<FundingType> fundingTypeList = fundingTypeRepository.findAll();
		assertThat(fundingTypeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void getAllFundingTypes() throws Exception {
		// Initialize the database
		fundingTypeRepository.saveAndFlush(fundingType);

		// Get all the fundingTypeList
		restFundingTypeMockMvc.perform(get("/api/funding-types?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(fundingType.getId().intValue())))
				.andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
				.andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
	}

	@Test
	@Transactional
	public void getFundingType() throws Exception {
		// Initialize the database
		fundingTypeRepository.saveAndFlush(fundingType);

		// Get the fundingType
		restFundingTypeMockMvc.perform(get("/api/funding-types/{id}", fundingType.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(fundingType.getId().intValue()))
				.andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
				.andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingFundingType() throws Exception {
		// Get the fundingType
		restFundingTypeMockMvc.perform(get("/api/funding-types/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateFundingType() throws Exception {
		// Initialize the database
		fundingTypeRepository.saveAndFlush(fundingType);
		int databaseSizeBeforeUpdate = fundingTypeRepository.findAll().size();

		// Update the fundingType
		FundingType updatedFundingType = fundingTypeRepository.findOne(fundingType.getId());
		updatedFundingType
				.code(UPDATED_CODE)
				.label(UPDATED_LABEL);
		FundingTypeDTO fundingTypeDTO = fundingTypeMapper.fundingTypeToFundingTypeDTO(updatedFundingType);

		restFundingTypeMockMvc.perform(put("/api/funding-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(fundingTypeDTO)))
				.andExpect(status().isOk());

		// Validate the FundingType in the database
		List<FundingType> fundingTypeList = fundingTypeRepository.findAll();
		assertThat(fundingTypeList).hasSize(databaseSizeBeforeUpdate);
		FundingType testFundingType = fundingTypeList.get(fundingTypeList.size() - 1);
		assertThat(testFundingType.getCode()).isEqualTo(UPDATED_CODE);
		assertThat(testFundingType.getLabel()).isEqualTo(UPDATED_LABEL);
	}

	@Test
	@Transactional
	public void updateNonExistingFundingType() throws Exception {
		int databaseSizeBeforeUpdate = fundingTypeRepository.findAll().size();

		// Create the FundingType
		FundingTypeDTO fundingTypeDTO = fundingTypeMapper.fundingTypeToFundingTypeDTO(fundingType);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restFundingTypeMockMvc.perform(put("/api/funding-types")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(fundingTypeDTO)))
				.andExpect(status().isCreated());

		// Validate the FundingType in the database
		List<FundingType> fundingTypeList = fundingTypeRepository.findAll();
		assertThat(fundingTypeList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteFundingType() throws Exception {
		// Initialize the database
		fundingTypeRepository.saveAndFlush(fundingType);
		int databaseSizeBeforeDelete = fundingTypeRepository.findAll().size();

		// Get the fundingType
		restFundingTypeMockMvc.perform(delete("/api/funding-types/{id}", fundingType.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<FundingType> fundingTypeList = fundingTypeRepository.findAll();
		assertThat(fundingTypeList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(FundingType.class);
	}
}
