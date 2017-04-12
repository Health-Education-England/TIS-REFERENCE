package com.transformuk.hee.tis.reference.web.rest;

import com.transformuk.hee.tis.reference.ReferenceApp;
import com.transformuk.hee.tis.reference.domain.Trust;
import com.transformuk.hee.tis.reference.repository.TrustRepository;
import com.transformuk.hee.tis.reference.api.dto.TrustDTO;
import com.transformuk.hee.tis.reference.service.impl.SitesTrustsService;
import com.transformuk.hee.tis.reference.service.mapper.TrustMapper;
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
 * Test class for the TrustResource REST controller.
 *
 * @see TrustResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReferenceApp.class)
public class TrustResourceIntTest {

	private static final String DEFAULT_CODE = "AAAAAAAAAA";
	private static final String UPDATED_CODE = "BBBBBBBBBB";

	private static final String DEFAULT_LOCAL_OFFICE = "AAAAAAAAAA";
	private static final String UPDATED_LOCAL_OFFICE = "BBBBBBBBBB";

	private static final String DEFAULT_STATUS = "AAAAAAAAAA";
	private static final String UPDATED_STATUS = "BBBBBBBBBB";

	private static final String DEFAULT_TRUST_KNOWN_AS = "AAAAAAAAAA";
	private static final String UPDATED_TRUST_KNOWN_AS = "BBBBBBBBBB";

	private static final String DEFAULT_TRUST_NAME = "AAAAAAAAAA";
	private static final String UPDATED_TRUST_NAME = "BBBBBBBBBB";

	private static final String DEFAULT_TRUST_NUMBER = "AAAAAAAAAA";
	private static final String UPDATED_TRUST_NUMBER = "BBBBBBBBBB";

	private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
	private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

	private static final String DEFAULT_POST_CODE = "AAAAAAAAAA";
	private static final String UPDATED_POST_CODE = "BBBBBBBBBB";

	@Autowired
	private TrustRepository trustRepository;

	@Autowired
	private TrustMapper trustMapper;

	@Autowired
	private SitesTrustsService sitesTrustsService;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restTrustMockMvc;

	private Trust trust;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		TrustResource trustResource = new TrustResource(trustRepository, trustMapper, sitesTrustsService, 100);
		this.restTrustMockMvc = MockMvcBuilders.standaloneSetup(trustResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 * <p>
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static Trust createEntity(EntityManager em) {
		Trust trust = new Trust()
				.code(DEFAULT_CODE)
				.localOffice(DEFAULT_LOCAL_OFFICE)
				.status(DEFAULT_STATUS)
				.trustKnownAs(DEFAULT_TRUST_KNOWN_AS)
				.trustName(DEFAULT_TRUST_NAME)
				.trustNumber(DEFAULT_TRUST_NUMBER)
				.address(DEFAULT_ADDRESS)
				.postCode(DEFAULT_POST_CODE);
		return trust;
	}

	@Before
	public void initTest() {
		trust = createEntity(em);
	}

	@Test
	@Transactional
	public void createTrust() throws Exception {
		int databaseSizeBeforeCreate = trustRepository.findAll().size();

		// Create the Trust
		TrustDTO trustDTO = trustMapper.trustToTrustDTO(trust);
		restTrustMockMvc.perform(post("/api/trusts")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(trustDTO)))
				.andExpect(status().isCreated());

		// Validate the Trust in the database
		List<Trust> trustList = trustRepository.findAll();
		assertThat(trustList).hasSize(databaseSizeBeforeCreate + 1);
		Trust testTrust = trustList.get(trustList.size() - 1);
		assertThat(testTrust.getCode()).isEqualTo(DEFAULT_CODE);
		assertThat(testTrust.getLocalOffice()).isEqualTo(DEFAULT_LOCAL_OFFICE);
		assertThat(testTrust.getStatus()).isEqualTo(DEFAULT_STATUS);
		assertThat(testTrust.getTrustKnownAs()).isEqualTo(DEFAULT_TRUST_KNOWN_AS);
		assertThat(testTrust.getTrustName()).isEqualTo(DEFAULT_TRUST_NAME);
		assertThat(testTrust.getTrustNumber()).isEqualTo(DEFAULT_TRUST_NUMBER);
		assertThat(testTrust.getAddress()).isEqualTo(DEFAULT_ADDRESS);
		assertThat(testTrust.getPostCode()).isEqualTo(DEFAULT_POST_CODE);
	}

	@Test
	@Transactional
	public void createTrustWithExistingId() throws Exception {
		int databaseSizeBeforeCreate = trustRepository.findAll().size();

		// Create the Trust with an existing ID
		trust.setId(1L);
		TrustDTO trustDTO = trustMapper.trustToTrustDTO(trust);

		// An entity with an existing ID cannot be created, so this API call must fail
		restTrustMockMvc.perform(post("/api/trusts")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(trustDTO)))
				.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		List<Trust> trustList = trustRepository.findAll();
		assertThat(trustList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void checkCodeIsRequired() throws Exception {
		int databaseSizeBeforeTest = trustRepository.findAll().size();
		// set the field null
		trust.setCode(null);

		// Create the Trust, which fails.
		TrustDTO trustDTO = trustMapper.trustToTrustDTO(trust);

		restTrustMockMvc.perform(post("/api/trusts")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(trustDTO)))
				.andExpect(status().isBadRequest());

		List<Trust> trustList = trustRepository.findAll();
		assertThat(trustList).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void getAllTrusts() throws Exception {
		// Initialize the database
		trustRepository.saveAndFlush(trust);

		// Get all the trustList
		restTrustMockMvc.perform(get("/api/trusts?sort=id,desc"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(trust.getId().intValue())))
				.andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
				.andExpect(jsonPath("$.[*].localOffice").value(hasItem(DEFAULT_LOCAL_OFFICE.toString())))
				.andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
				.andExpect(jsonPath("$.[*].trustKnownAs").value(hasItem(DEFAULT_TRUST_KNOWN_AS.toString())))
				.andExpect(jsonPath("$.[*].trustName").value(hasItem(DEFAULT_TRUST_NAME.toString())))
				.andExpect(jsonPath("$.[*].trustNumber").value(hasItem(DEFAULT_TRUST_NUMBER.toString())))
				.andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
				.andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE.toString())));
	}

	@Test
	@Transactional
	public void searchTrusts() throws Exception {
		// Initialize the database
		trustRepository.saveAndFlush(trust);

		// Get all the trustList
		restTrustMockMvc.perform(get("/api/trusts/search/?searchString=R1A"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.total").value(1))
				.andExpect(jsonPath("$.list[0].code").value("R1A"))
				.andExpect(jsonPath("$.list[0].trustName").value("Worcestershire Health and Care NHS Trust"));
	}

	@Test
	@Transactional
	public void getTrust() throws Exception {
		// Initialize the database
		trustRepository.saveAndFlush(trust);

		// Get the trust
		restTrustMockMvc.perform(get("/api/trusts/{id}", trust.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(trust.getId().intValue()))
				.andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
				.andExpect(jsonPath("$.localOffice").value(DEFAULT_LOCAL_OFFICE.toString()))
				.andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
				.andExpect(jsonPath("$.trustKnownAs").value(DEFAULT_TRUST_KNOWN_AS.toString()))
				.andExpect(jsonPath("$.trustName").value(DEFAULT_TRUST_NAME.toString()))
				.andExpect(jsonPath("$.trustNumber").value(DEFAULT_TRUST_NUMBER.toString()))
				.andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
				.andExpect(jsonPath("$.postCode").value(DEFAULT_POST_CODE.toString()));
	}

	@Test
	@Transactional
	public void getTrustByCode() throws Exception {
		// Initialize the database
		trustRepository.saveAndFlush(trust);

		// Get all the trustList
		restTrustMockMvc.perform(get("/api/trusts/code/R1A"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.code").value("R1A"))
				.andExpect(jsonPath("$.trustName").value("Worcestershire Health and Care NHS Trust"));
	}

	@Test
	@Transactional
	public void getNonExistingTrust() throws Exception {
		// Get the trust
		restTrustMockMvc.perform(get("/api/trusts/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateTrust() throws Exception {
		// Initialize the database
		trustRepository.saveAndFlush(trust);
		int databaseSizeBeforeUpdate = trustRepository.findAll().size();

		// Update the trust
		Trust updatedTrust = trustRepository.findOne(trust.getId());
		updatedTrust
				.code(UPDATED_CODE)
				.localOffice(UPDATED_LOCAL_OFFICE)
				.status(UPDATED_STATUS)
				.trustKnownAs(UPDATED_TRUST_KNOWN_AS)
				.trustName(UPDATED_TRUST_NAME)
				.trustNumber(UPDATED_TRUST_NUMBER)
				.address(UPDATED_ADDRESS)
				.postCode(UPDATED_POST_CODE);
		TrustDTO trustDTO = trustMapper.trustToTrustDTO(updatedTrust);

		restTrustMockMvc.perform(put("/api/trusts")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(trustDTO)))
				.andExpect(status().isOk());

		// Validate the Trust in the database
		List<Trust> trustList = trustRepository.findAll();
		assertThat(trustList).hasSize(databaseSizeBeforeUpdate);
		Trust testTrust = trustList.get(trustList.size() - 1);
		assertThat(testTrust.getCode()).isEqualTo(UPDATED_CODE);
		assertThat(testTrust.getLocalOffice()).isEqualTo(UPDATED_LOCAL_OFFICE);
		assertThat(testTrust.getStatus()).isEqualTo(UPDATED_STATUS);
		assertThat(testTrust.getTrustKnownAs()).isEqualTo(UPDATED_TRUST_KNOWN_AS);
		assertThat(testTrust.getTrustName()).isEqualTo(UPDATED_TRUST_NAME);
		assertThat(testTrust.getTrustNumber()).isEqualTo(UPDATED_TRUST_NUMBER);
		assertThat(testTrust.getAddress()).isEqualTo(UPDATED_ADDRESS);
		assertThat(testTrust.getPostCode()).isEqualTo(UPDATED_POST_CODE);
	}

	@Test
	@Transactional
	public void updateNonExistingTrust() throws Exception {
		int databaseSizeBeforeUpdate = trustRepository.findAll().size();

		// Create the Trust
		TrustDTO trustDTO = trustMapper.trustToTrustDTO(trust);

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restTrustMockMvc.perform(put("/api/trusts")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(trustDTO)))
				.andExpect(status().isCreated());

		// Validate the Trust in the database
		List<Trust> trustList = trustRepository.findAll();
		assertThat(trustList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deleteTrust() throws Exception {
		// Initialize the database
		trustRepository.saveAndFlush(trust);
		int databaseSizeBeforeDelete = trustRepository.findAll().size();

		// Get the trust
		restTrustMockMvc.perform(delete("/api/trusts/{id}", trust.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());

		// Validate the database is empty
		List<Trust> trustList = trustRepository.findAll();
		assertThat(trustList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Trust.class);
	}
}
