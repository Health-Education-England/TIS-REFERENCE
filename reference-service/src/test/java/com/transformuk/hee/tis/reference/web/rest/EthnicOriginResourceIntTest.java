package com.transformuk.hee.tis.reference.web.rest;

import com.transformuk.hee.tis.reference.ReferenceApp;
import com.transformuk.hee.tis.reference.domain.EthnicOrigin;
import com.transformuk.hee.tis.reference.repository.EthnicOriginRepository;
import com.transformuk.hee.tis.reference.service.dto.EthnicOriginDTO;
import com.transformuk.hee.tis.reference.service.mapper.EthnicOriginMapper;
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
 * Test class for the EthnicOriginResource REST controller.
 *
 * @see EthnicOriginResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReferenceApp.class)
public class EthnicOriginResourceIntTest {

	private static final String DEFAULT_CODE = "AAAAAAAAAA";
	private static final String UPDATED_CODE = "BBBBBBBBBB";

	@Autowired
	private EthnicOriginRepository ethnicOriginRepository;

	@Autowired
	private EthnicOriginMapper ethnicOriginMapper;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restEthnicOriginMockMvc;

	private EthnicOrigin ethnicOrigin;

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static EthnicOrigin createEntity(EntityManager em) {
		EthnicOrigin ethnicOrigin = new EthnicOrigin()
				.code(DEFAULT_CODE);
		return ethnicOrigin;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		EthnicOriginResource ethnicOriginResource = new EthnicOriginResource(ethnicOriginRepository, ethnicOriginMapper);
		this.restEthnicOriginMockMvc = MockMvcBuilders.standaloneSetup(ethnicOriginResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	@Before
	public void initTest() {
		ethnicOrigin = createEntity(em);
	}

	@Test
	@Transactional
	public void createEthnicOrigin() throws Exception {
		int databaseSizeBeforeCreate = ethnicOriginRepository.findAll().size();

		// Create the EthnicOrigin
		EthnicOriginDTO ethnicOriginDTO = ethnicOriginMapper.ethnicOriginToEthnicOriginDTO(ethnicOrigin);
		restEthnicOriginMockMvc.perform(post("/api/ethnic-origins")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(ethnicOriginDTO)))
				.andExpect(status().isCreated());

		// Validate the EthnicOrigin in the database
		List<EthnicOrigin> ethnicOriginList = ethnicOriginRepository.findAll();
		assertThat(ethnicOriginList).hasSize(databaseSizeBeforeCreate + 1);
		EthnicOrigin testEthnicOrigin = ethnicOriginList.get(ethnicOriginList.size() - 1);
		assertThat(testEthnicOrigin.getCode()).isEqualTo(DEFAULT_CODE);
	}

	@Test
	@Transactional
	public void createEthnicOriginWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = ethnicOriginRepository.findAll().size();

		// Create the EthnicOrigin with an existing ID
		ethnicOrigin.setId(1L);
		EthnicOriginDTO ethnicOriginDTO = ethnicOriginMapper.ethnicOriginToEthnicOriginDTO(ethnicOrigin);

		// An entity with an existing ID cannot be created, so this API call must fail
		restEthnicOriginMockMvc.perform(post("/api/ethnic-origins")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(ethnicOriginDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<EthnicOrigin> ethnicOriginList = ethnicOriginRepository.findAll();
		assertThat(ethnicOriginList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void checkCodeIsRequired() throws Exception {
		int databaseSizeBeforeTest = ethnicOriginRepository.findAll().size();
		// set the field null
		ethnicOrigin.setCode(null);

		// Create the EthnicOrigin, which fails.
		EthnicOriginDTO ethnicOriginDTO = ethnicOriginMapper.ethnicOriginToEthnicOriginDTO(ethnicOrigin);

		restEthnicOriginMockMvc.perform(post("/api/ethnic-origins")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(ethnicOriginDTO)))
				.andExpect(status().isBadRequest());

		List<EthnicOrigin> ethnicOriginList = ethnicOriginRepository.findAll();
		assertThat(ethnicOriginList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void getAllEthnicOrigins() throws Exception {
		// Initialize the database
		ethnicOriginRepository.saveAndFlush(ethnicOrigin);

		// Get all the ethnicOriginList
		restEthnicOriginMockMvc.perform(get("/api/ethnic-origins?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(ethnicOrigin.getId().intValue())))
				.andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
	}

	@Test
	@Transactional
	public void getEthnicOrigin() throws Exception {
		// Initialize the database
		ethnicOriginRepository.saveAndFlush(ethnicOrigin);

		// Get the ethnicOrigin
		restEthnicOriginMockMvc.perform(get("/api/ethnic-origins/{id}", ethnicOrigin.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(ethnicOrigin.getId().intValue()))
				.andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingEthnicOrigin() throws Exception {
		// Get the ethnicOrigin
		restEthnicOriginMockMvc.perform(get("/api/ethnic-origins/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateEthnicOrigin() throws Exception {
		// Initialize the database
		ethnicOriginRepository.saveAndFlush(ethnicOrigin);
		int databaseSizeBeforeUpdate = ethnicOriginRepository.findAll().size();

		// Update the ethnicOrigin
		EthnicOrigin updatedEthnicOrigin = ethnicOriginRepository.findOne(ethnicOrigin.getId());
		updatedEthnicOrigin
				.code(UPDATED_CODE);
		EthnicOriginDTO ethnicOriginDTO = ethnicOriginMapper.ethnicOriginToEthnicOriginDTO(updatedEthnicOrigin);

		restEthnicOriginMockMvc.perform(put("/api/ethnic-origins")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(ethnicOriginDTO)))
				.andExpect(status().isOk());

		// Validate the EthnicOrigin in the database
		List<EthnicOrigin> ethnicOriginList = ethnicOriginRepository.findAll();
		assertThat(ethnicOriginList).hasSize(databaseSizeBeforeUpdate);
		EthnicOrigin testEthnicOrigin = ethnicOriginList.get(ethnicOriginList.size() - 1);
		assertThat(testEthnicOrigin.getCode()).isEqualTo(UPDATED_CODE);
	}

	@Test
	@Transactional
	public void updateNonExistingEthnicOrigin() throws Exception {
		int databaseSizeBeforeUpdate = ethnicOriginRepository.findAll().size();

		// Create the EthnicOrigin
		EthnicOriginDTO ethnicOriginDTO = ethnicOriginMapper.ethnicOriginToEthnicOriginDTO(ethnicOrigin);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restEthnicOriginMockMvc.perform(put("/api/ethnic-origins")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(ethnicOriginDTO)))
				.andExpect(status().isCreated());

		// Validate the EthnicOrigin in the database
		List<EthnicOrigin> ethnicOriginList = ethnicOriginRepository.findAll();
		assertThat(ethnicOriginList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteEthnicOrigin() throws Exception {
		// Initialize the database
		ethnicOriginRepository.saveAndFlush(ethnicOrigin);
		int databaseSizeBeforeDelete = ethnicOriginRepository.findAll().size();

		// Get the ethnicOrigin
		restEthnicOriginMockMvc.perform(delete("/api/ethnic-origins/{id}", ethnicOrigin.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<EthnicOrigin> ethnicOriginList = ethnicOriginRepository.findAll();
		assertThat(ethnicOriginList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(EthnicOrigin.class);
	}
}
