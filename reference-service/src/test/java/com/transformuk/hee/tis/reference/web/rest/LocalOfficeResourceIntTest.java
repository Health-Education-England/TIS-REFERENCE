package com.transformuk.hee.tis.reference.web.rest;

import com.transformuk.hee.tis.reference.ReferenceApp;
import com.transformuk.hee.tis.reference.domain.LocalOffice;
import com.transformuk.hee.tis.reference.repository.LocalOfficeRepository;
import com.transformuk.hee.tis.reference.api.dto.LocalOfficeDTO;
import com.transformuk.hee.tis.reference.service.mapper.LocalOfficeMapper;
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
 * Test class for the LocalOfficeResource REST controller.
 *
 * @see LocalOfficeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReferenceApp.class)
public class LocalOfficeResourceIntTest {

	private static final String DEFAULT_ABBREVIATION = "AAAAAAAAAA";
	private static final String UPDATED_ABBREVIATION = "BBBBBBBBBB";

	private static final String DEFAULT_NAME = "AAAAAAAAAA";
	private static final String UPDATED_NAME = "BBBBBBBBBB";

	@Autowired
	private LocalOfficeRepository localOfficeRepository;

	@Autowired
	private LocalOfficeMapper localOfficeMapper;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restLocalOfficeMockMvc;

	private LocalOffice localOffice;

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static LocalOffice createEntity(EntityManager em) {
		LocalOffice localOffice = new LocalOffice()
				.abbreviation(DEFAULT_ABBREVIATION)
				.name(DEFAULT_NAME);
		return localOffice;
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		LocalOfficeResource localOfficeResource = new LocalOfficeResource(localOfficeRepository, localOfficeMapper);
		this.restLocalOfficeMockMvc = MockMvcBuilders.standaloneSetup(localOfficeResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	@Before
	public void initTest() {
		localOffice = createEntity(em);
	}

	@Test
	@Transactional
	public void createLocalOffice() throws Exception {
		int databaseSizeBeforeCreate = localOfficeRepository.findAll().size();

		// Create the LocalOffice
		LocalOfficeDTO localOfficeDTO = localOfficeMapper.localOfficeToLocalOfficeDTO(localOffice);
		restLocalOfficeMockMvc.perform(post("/api/local-offices")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(localOfficeDTO)))
				.andExpect(status().isCreated());

		// Validate the LocalOffice in the database
		List<LocalOffice> localOfficeList = localOfficeRepository.findAll();
		assertThat(localOfficeList).hasSize(databaseSizeBeforeCreate + 1);
		LocalOffice testLocalOffice = localOfficeList.get(localOfficeList.size() - 1);
		assertThat(testLocalOffice.getAbbreviation()).isEqualTo(DEFAULT_ABBREVIATION);
		assertThat(testLocalOffice.getName()).isEqualTo(DEFAULT_NAME);
	}

	@Test
	@Transactional
	public void createLocalOfficeWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = localOfficeRepository.findAll().size();

		// Create the LocalOffice with an existing ID
		localOffice.setId(1L);
		LocalOfficeDTO localOfficeDTO = localOfficeMapper.localOfficeToLocalOfficeDTO(localOffice);

		// An entity with an existing ID cannot be created, so this API call must fail
		restLocalOfficeMockMvc.perform(post("/api/local-offices")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(localOfficeDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<LocalOffice> localOfficeList = localOfficeRepository.findAll();
		assertThat(localOfficeList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void checkAbbreviationIsRequired() throws Exception {
		int databaseSizeBeforeTest = localOfficeRepository.findAll().size();
		// set the field null
		localOffice.setAbbreviation(null);

		// Create the LocalOffice, which fails.
		LocalOfficeDTO localOfficeDTO = localOfficeMapper.localOfficeToLocalOfficeDTO(localOffice);

		restLocalOfficeMockMvc.perform(post("/api/local-offices")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(localOfficeDTO)))
				.andExpect(status().isBadRequest());

		List<LocalOffice> localOfficeList = localOfficeRepository.findAll();
		assertThat(localOfficeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void checkNameIsRequired() throws Exception {
		int databaseSizeBeforeTest = localOfficeRepository.findAll().size();
		// set the field null
		localOffice.setName(null);

		// Create the LocalOffice, which fails.
		LocalOfficeDTO localOfficeDTO = localOfficeMapper.localOfficeToLocalOfficeDTO(localOffice);

		restLocalOfficeMockMvc.perform(post("/api/local-offices")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(localOfficeDTO)))
				.andExpect(status().isBadRequest());

		List<LocalOffice> localOfficeList = localOfficeRepository.findAll();
		assertThat(localOfficeList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void getAllLocalOffices() throws Exception {
		// Initialize the database
		localOfficeRepository.saveAndFlush(localOffice);

		// Get all the localOfficeList
		restLocalOfficeMockMvc.perform(get("/api/local-offices?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(localOffice.getId().intValue())))
				.andExpect(jsonPath("$.[*].abbreviation").value(hasItem(DEFAULT_ABBREVIATION.toString())))
				.andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
	}

	@Test
	@Transactional
	public void getLocalOffice() throws Exception {
		// Initialize the database
		localOfficeRepository.saveAndFlush(localOffice);

		// Get the localOffice
		restLocalOfficeMockMvc.perform(get("/api/local-offices/{id}", localOffice.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(localOffice.getId().intValue()))
				.andExpect(jsonPath("$.abbreviation").value(DEFAULT_ABBREVIATION.toString()))
				.andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
	}

	@Test
	@Transactional
	public void getNonExistingLocalOffice() throws Exception {
		// Get the localOffice
		restLocalOfficeMockMvc.perform(get("/api/local-offices/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateLocalOffice() throws Exception {
		// Initialize the database
		localOfficeRepository.saveAndFlush(localOffice);
		int databaseSizeBeforeUpdate = localOfficeRepository.findAll().size();

		// Update the localOffice
		LocalOffice updatedLocalOffice = localOfficeRepository.findOne(localOffice.getId());
		updatedLocalOffice
				.abbreviation(UPDATED_ABBREVIATION)
				.name(UPDATED_NAME);
		LocalOfficeDTO localOfficeDTO = localOfficeMapper.localOfficeToLocalOfficeDTO(updatedLocalOffice);

		restLocalOfficeMockMvc.perform(put("/api/local-offices")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(localOfficeDTO)))
				.andExpect(status().isOk());

		// Validate the LocalOffice in the database
		List<LocalOffice> localOfficeList = localOfficeRepository.findAll();
		assertThat(localOfficeList).hasSize(databaseSizeBeforeUpdate);
		LocalOffice testLocalOffice = localOfficeList.get(localOfficeList.size() - 1);
		assertThat(testLocalOffice.getAbbreviation()).isEqualTo(UPDATED_ABBREVIATION);
		assertThat(testLocalOffice.getName()).isEqualTo(UPDATED_NAME);
	}

	@Test
	@Transactional
	public void updateNonExistingLocalOffice() throws Exception {
		int databaseSizeBeforeUpdate = localOfficeRepository.findAll().size();

		// Create the LocalOffice
		LocalOfficeDTO localOfficeDTO = localOfficeMapper.localOfficeToLocalOfficeDTO(localOffice);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restLocalOfficeMockMvc.perform(put("/api/local-offices")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(localOfficeDTO)))
				.andExpect(status().isCreated());

		// Validate the LocalOffice in the database
		List<LocalOffice> localOfficeList = localOfficeRepository.findAll();
		assertThat(localOfficeList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteLocalOffice() throws Exception {
		// Initialize the database
		localOfficeRepository.saveAndFlush(localOffice);
		int databaseSizeBeforeDelete = localOfficeRepository.findAll().size();

		// Get the localOffice
		restLocalOfficeMockMvc.perform(delete("/api/local-offices/{id}", localOffice.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<LocalOffice> localOfficeList = localOfficeRepository.findAll();
		assertThat(localOfficeList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(LocalOffice.class);
	}
}
