package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.model.FundingIssue;
import com.transformuk.hee.tis.reference.service.repository.FundingIssueRepository;
import com.transformuk.hee.tis.reference.service.api.dto.FundingIssueDTO;
import com.transformuk.hee.tis.reference.service.service.mapper.FundingIssueMapper;
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
 * Test class for the FundingIssueResource REST controller.
 *
 * @see FundingIssueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class FundingIssueResourceIntTest {

	private static final String DEFAULT_CODE = "AAAAAAAAAA";
	private static final String UPDATED_CODE = "BBBBBBBBBB";

	@Autowired
	private FundingIssueRepository fundingIssueRepository;

	@Autowired
	private FundingIssueMapper fundingIssueMapper;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restFundingIssueMockMvc;

	private FundingIssue fundingIssue;

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static FundingIssue createEntity(EntityManager em) {
		FundingIssue fundingIssue = new FundingIssue()
				.code(DEFAULT_CODE);
		return fundingIssue;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		FundingIssueResource fundingIssueResource = new FundingIssueResource(fundingIssueRepository, fundingIssueMapper);
		this.restFundingIssueMockMvc = MockMvcBuilders.standaloneSetup(fundingIssueResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	@Before
	public void initTest() {
		fundingIssue = createEntity(em);
	}

	@Test
	@Transactional
	public void createFundingIssue() throws Exception {
		int databaseSizeBeforeCreate = fundingIssueRepository.findAll().size();

		// Create the FundingIssue
		FundingIssueDTO fundingIssueDTO = fundingIssueMapper.fundingIssueToFundingIssueDTO(fundingIssue);
		restFundingIssueMockMvc.perform(post("/api/funding-issues")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(fundingIssueDTO)))
				.andExpect(status().isCreated());

		// Validate the FundingIssue in the database
		List<FundingIssue> fundingIssueList = fundingIssueRepository.findAll();
		assertThat(fundingIssueList).hasSize(databaseSizeBeforeCreate + 1);
		FundingIssue testFundingIssue = fundingIssueList.get(fundingIssueList.size() - 1);
		assertThat(testFundingIssue.getCode()).isEqualTo(DEFAULT_CODE);
	}

	@Test
	@Transactional
	public void createFundingIssueWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = fundingIssueRepository.findAll().size();

		// Create the FundingIssue with an existing ID
		fundingIssue.setId(1L);
		FundingIssueDTO fundingIssueDTO = fundingIssueMapper.fundingIssueToFundingIssueDTO(fundingIssue);

		// An entity with an existing ID cannot be created, so this API call must fail
		restFundingIssueMockMvc.perform(post("/api/funding-issues")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(fundingIssueDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<FundingIssue> fundingIssueList = fundingIssueRepository.findAll();
		assertThat(fundingIssueList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void checkCodeIsRequired() throws Exception {
		int databaseSizeBeforeTest = fundingIssueRepository.findAll().size();
		// set the field null
		fundingIssue.setCode(null);

		// Create the FundingIssue, which fails.
		FundingIssueDTO fundingIssueDTO = fundingIssueMapper.fundingIssueToFundingIssueDTO(fundingIssue);

		restFundingIssueMockMvc.perform(post("/api/funding-issues")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(fundingIssueDTO)))
				.andExpect(status().isBadRequest());

		List<FundingIssue> fundingIssueList = fundingIssueRepository.findAll();
		assertThat(fundingIssueList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void getAllFundingIssues() throws Exception {
		// Initialize the database
		fundingIssueRepository.saveAndFlush(fundingIssue);

		// Get all the fundingIssueList
		restFundingIssueMockMvc.perform(get("/api/funding-issues?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(fundingIssue.getId().intValue())))
				.andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
	}

	@Test
	@Transactional
	public void getFundingIssue() throws Exception {
		// Initialize the database
		fundingIssueRepository.saveAndFlush(fundingIssue);

		// Get the fundingIssue
		restFundingIssueMockMvc.perform(get("/api/funding-issues/{id}", fundingIssue.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(fundingIssue.getId().intValue()))
				.andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingFundingIssue() throws Exception {
		// Get the fundingIssue
		restFundingIssueMockMvc.perform(get("/api/funding-issues/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateFundingIssue() throws Exception {
		// Initialize the database
		fundingIssueRepository.saveAndFlush(fundingIssue);
		int databaseSizeBeforeUpdate = fundingIssueRepository.findAll().size();

		// Update the fundingIssue
		FundingIssue updatedFundingIssue = fundingIssueRepository.findOne(fundingIssue.getId());
		updatedFundingIssue
				.code(UPDATED_CODE);
		FundingIssueDTO fundingIssueDTO = fundingIssueMapper.fundingIssueToFundingIssueDTO(updatedFundingIssue);

		restFundingIssueMockMvc.perform(put("/api/funding-issues")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(fundingIssueDTO)))
				.andExpect(status().isOk());

		// Validate the FundingIssue in the database
		List<FundingIssue> fundingIssueList = fundingIssueRepository.findAll();
		assertThat(fundingIssueList).hasSize(databaseSizeBeforeUpdate);
		FundingIssue testFundingIssue = fundingIssueList.get(fundingIssueList.size() - 1);
		assertThat(testFundingIssue.getCode()).isEqualTo(UPDATED_CODE);
	}

	@Test
	@Transactional
	public void updateNonExistingFundingIssue() throws Exception {
		int databaseSizeBeforeUpdate = fundingIssueRepository.findAll().size();

		// Create the FundingIssue
		FundingIssueDTO fundingIssueDTO = fundingIssueMapper.fundingIssueToFundingIssueDTO(fundingIssue);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restFundingIssueMockMvc.perform(put("/api/funding-issues")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(fundingIssueDTO)))
				.andExpect(status().isCreated());

		// Validate the FundingIssue in the database
		List<FundingIssue> fundingIssueList = fundingIssueRepository.findAll();
		assertThat(fundingIssueList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteFundingIssue() throws Exception {
		// Initialize the database
		fundingIssueRepository.saveAndFlush(fundingIssue);
		int databaseSizeBeforeDelete = fundingIssueRepository.findAll().size();

		// Get the fundingIssue
		restFundingIssueMockMvc.perform(delete("/api/funding-issues/{id}", fundingIssue.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<FundingIssue> fundingIssueList = fundingIssueRepository.findAll();
		assertThat(fundingIssueList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(FundingIssue.class);
	}
}
