package com.transformuk.hee.tis.reference.web.rest;

import com.transformuk.hee.tis.reference.Application;
import com.transformuk.hee.tis.reference.domain.DBC;
import com.transformuk.hee.tis.reference.repository.DBCRepository;
import com.transformuk.hee.tis.reference.api.dto.DBCDTO;
import com.transformuk.hee.tis.reference.service.mapper.DBCMapper;
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
 * Test class for the DBCResource REST controller.
 *
 * @see DBCResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DBCResourceIntTest {

	private static final String DEFAULT_DBC = "AAAAAAAAAA";
	private static final String UPDATED_DBC = "BBBBBBBBBB";

	private static final String DEFAULT_NAME = "AAAAAAAAAA";
	private static final String UPDATED_NAME = "BBBBBBBBBB";

	private static final String DEFAULT_ABBR = "AAAAAAAAAA";
	private static final String UPDATED_ABBR = "BBBBBBBBBB";

	@Autowired
	private DBCRepository dBCRepository;

	@Autowired
	private DBCMapper dBCMapper;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restDBCMockMvc;

	private DBC dBC;

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static DBC createEntity(EntityManager em) {
		DBC dBC = new DBC()
				.dbc(DEFAULT_DBC)
				.name(DEFAULT_NAME)
				.abbr(DEFAULT_ABBR);
		return dBC;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		DBCResource dBCResource = new DBCResource(dBCRepository, dBCMapper);
		this.restDBCMockMvc = MockMvcBuilders.standaloneSetup(dBCResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	@Before
	public void initTest() {
		dBC = createEntity(em);
	}

	@Test
	@Transactional
	public void createDBC() throws Exception {
		int databaseSizeBeforeCreate = dBCRepository.findAll().size();

		// Create the DBC
		DBCDTO dBCDTO = dBCMapper.dBCToDBCDTO(dBC);
		restDBCMockMvc.perform(post("/api/dbcs")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(dBCDTO)))
				.andExpect(status().isCreated());

		// Validate the DBC in the database
		List<DBC> dBCList = dBCRepository.findAll();
		assertThat(dBCList).hasSize(databaseSizeBeforeCreate + 1);
		DBC testDBC = dBCList.get(dBCList.size() - 1);
		assertThat(testDBC.getDbc()).isEqualTo(DEFAULT_DBC);
		assertThat(testDBC.getName()).isEqualTo(DEFAULT_NAME);
		assertThat(testDBC.getAbbr()).isEqualTo(DEFAULT_ABBR);
	}

	@Test
	@Transactional
	public void createDBCWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = dBCRepository.findAll().size();

		// Create the DBC with an existing ID
		dBC.setId(1L);
		DBCDTO dBCDTO = dBCMapper.dBCToDBCDTO(dBC);

		// An entity with an existing ID cannot be created, so this API call must fail
		restDBCMockMvc.perform(post("/api/dbcs")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(dBCDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<DBC> dBCList = dBCRepository.findAll();
		assertThat(dBCList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void checkDbcIsRequired() throws Exception {
		int databaseSizeBeforeTest = dBCRepository.findAll().size();
		// set the field null
		dBC.setDbc(null);

		// Create the DBC, which fails.
		DBCDTO dBCDTO = dBCMapper.dBCToDBCDTO(dBC);

		restDBCMockMvc.perform(post("/api/dbcs")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(dBCDTO)))
				.andExpect(status().isBadRequest());

		List<DBC> dBCList = dBCRepository.findAll();
		assertThat(dBCList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkNameIsRequired() throws Exception {
		int databaseSizeBeforeTest = dBCRepository.findAll().size();
		// set the field null
		dBC.setName(null);

		// Create the DBC, which fails.
		DBCDTO dBCDTO = dBCMapper.dBCToDBCDTO(dBC);

		restDBCMockMvc.perform(post("/api/dbcs")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(dBCDTO)))
				.andExpect(status().isBadRequest());

		List<DBC> dBCList = dBCRepository.findAll();
		assertThat(dBCList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkAbbrIsRequired() throws Exception {
		int databaseSizeBeforeTest = dBCRepository.findAll().size();
		// set the field null
		dBC.setAbbr(null);

		// Create the DBC, which fails.
		DBCDTO dBCDTO = dBCMapper.dBCToDBCDTO(dBC);

		restDBCMockMvc.perform(post("/api/dbcs")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(dBCDTO)))
				.andExpect(status().isBadRequest());

		List<DBC> dBCList = dBCRepository.findAll();
		assertThat(dBCList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void getAllDBCS() throws Exception {
		// Initialize the database
		dBCRepository.saveAndFlush(dBC);

		// Get all the dBCList
		restDBCMockMvc.perform(get("/api/dbcs?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(dBC.getId().intValue())))
				.andExpect(jsonPath("$.[*].dbc").value(hasItem(DEFAULT_DBC.toString())))
				.andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
				.andExpect(jsonPath("$.[*].abbr").value(hasItem(DEFAULT_ABBR.toString())));
	}

	@Test
	@Transactional
	public void getDBC() throws Exception {
		// Initialize the database
		dBCRepository.saveAndFlush(dBC);

		// Get the dBC
		restDBCMockMvc.perform(get("/api/dbcs/{id}", dBC.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(dBC.getId().intValue()))
				.andExpect(jsonPath("$.dbc").value(DEFAULT_DBC.toString()))
				.andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
				.andExpect(jsonPath("$.abbr").value(DEFAULT_ABBR.toString()));
	}

	@Test
	@Transactional
	public void getDBCByCode() throws Exception {
		// Initialize the database
		dBCRepository.saveAndFlush(dBC);

		// Get the dBC
		restDBCMockMvc.perform(get("/api/dbcs/code/1-AIIDH1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.dbc").value("1-AIIDH1"))
				.andExpect(jsonPath("$.name").value("Health Education Thames Valley"))
				.andExpect(jsonPath("$.abbr").value("TV"));
	}

	@Test
	@Transactional
	public void getNonExistingDBC() throws Exception {
		// Get the dBC
		restDBCMockMvc.perform(get("/api/dbcs/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateDBC() throws Exception {
		// Initialize the database
		dBCRepository.saveAndFlush(dBC);
		int databaseSizeBeforeUpdate = dBCRepository.findAll().size();

		// Update the dBC
		DBC updatedDBC = dBCRepository.findOne(dBC.getId());
		updatedDBC
				.dbc(UPDATED_DBC)
				.name(UPDATED_NAME)
				.abbr(UPDATED_ABBR);
		DBCDTO dBCDTO = dBCMapper.dBCToDBCDTO(updatedDBC);

		restDBCMockMvc.perform(put("/api/dbcs")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(dBCDTO)))
				.andExpect(status().isOk());

		// Validate the DBC in the database
		List<DBC> dBCList = dBCRepository.findAll();
		assertThat(dBCList).hasSize(databaseSizeBeforeUpdate);
		DBC testDBC = dBCList.get(dBCList.size() - 1);
		assertThat(testDBC.getDbc()).isEqualTo(UPDATED_DBC);
		assertThat(testDBC.getName()).isEqualTo(UPDATED_NAME);
		assertThat(testDBC.getAbbr()).isEqualTo(UPDATED_ABBR);
	}

	@Test
	@Transactional
	public void updateNonExistingDBC() throws Exception {
		int databaseSizeBeforeUpdate = dBCRepository.findAll().size();

		// Create the DBC
		DBCDTO dBCDTO = dBCMapper.dBCToDBCDTO(dBC);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restDBCMockMvc.perform(put("/api/dbcs")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(dBCDTO)))
				.andExpect(status().isCreated());

		// Validate the DBC in the database
		List<DBC> dBCList = dBCRepository.findAll();
		assertThat(dBCList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteDBC() throws Exception {
		// Initialize the database
		dBCRepository.saveAndFlush(dBC);
		int databaseSizeBeforeDelete = dBCRepository.findAll().size();

		// Get the dBC
		restDBCMockMvc.perform(delete("/api/dbcs/{id}", dBC.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<DBC> dBCList = dBCRepository.findAll();
		assertThat(dBCList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(DBC.class);
	}
}
