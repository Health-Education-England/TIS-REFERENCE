package com.transformuk.hee.tis.reference.service.api;

import com.transformuk.hee.tis.reference.api.dto.TrustDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.Trust;
import com.transformuk.hee.tis.reference.service.repository.TrustRepository;
import com.transformuk.hee.tis.reference.service.service.impl.SitesTrustsService;
import com.transformuk.hee.tis.reference.service.service.mapper.TrustMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the TrustResource REST controller.
 *
 * @see TrustResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TrustResourceIntTest {

  private static final String DEFAULT_CODE = "AAAAAAAAAA";
  private static final String UPDATED_CODE = "BBBBBBBBBB";

  private static final String DEFAULT_LOCAL_OFFICE = "AAAAAAAAAA";
  private static final String UPDATED_LOCAL_OFFICE = "BBBBBBBBBB";

  private static final Status DEFAULT_STATUS = Status.CURRENT;
  private static final Status UPDATED_STATUS = Status.INACTIVE;

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

  private static final String NON_EXISTING_TRUST_CODE = "XFK43F6";

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
  public void setup() {
    MockitoAnnotations.initMocks(this);
    TrustResource trustResource = new TrustResource(trustRepository, trustMapper, sitesTrustsService, 100);
    this.restTrustMockMvc = MockMvcBuilders.standaloneSetup(trustResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
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
  public void createTrustWithExistingCode() throws Exception {
    trust = trustRepository.save(this.trust);
    int databaseSizeBeforeCreate = trustRepository.findAll().size();

    // Create the Trust with an existing ID
    TrustDTO anotherTrust = new TrustDTO();
    anotherTrust.setId(trust.getId());

    // An entity with an existing ID cannot be created, so this API call must fail
    restTrustMockMvc.perform(post("/api/trusts")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(anotherTrust)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<Trust> trustList = trustRepository.findAll();
    assertThat(trustList).hasSize(databaseSizeBeforeCreate);
  }


  @Test
  @Transactional
  public void getAllTrusts() throws Exception {
    // Initialize the database
    trustRepository.saveAndFlush(trust);

    // Get all the trustList
    restTrustMockMvc.perform(get("/api/trusts?sort=code,asc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
        .andExpect(jsonPath("$.[*].localOffice").value(hasItem(DEFAULT_LOCAL_OFFICE)))
        .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
        .andExpect(jsonPath("$.[*].trustKnownAs").value(hasItem(DEFAULT_TRUST_KNOWN_AS)))
        .andExpect(jsonPath("$.[*].trustName").value(hasItem(DEFAULT_TRUST_NAME)))
        .andExpect(jsonPath("$.[*].trustNumber").value(hasItem(DEFAULT_TRUST_NUMBER)))
        .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
        .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE)));
  }

  @Test
  @Transactional
  public void getAllTrustsIn() throws Exception {
    // Initialize the database
    trustRepository.saveAndFlush(trust);

    // Get all the trustList
    restTrustMockMvc.perform(get("/api/trusts/in/invalid," + DEFAULT_CODE))
        .andExpect(status().isFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
        .andExpect(jsonPath("$.[*].localOffice").value(hasItem(DEFAULT_LOCAL_OFFICE)))
        .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
        .andExpect(jsonPath("$.[*].trustKnownAs").value(hasItem(DEFAULT_TRUST_KNOWN_AS)))
        .andExpect(jsonPath("$.[*].trustName").value(hasItem(DEFAULT_TRUST_NAME)))
        .andExpect(jsonPath("$.[*].trustNumber").value(hasItem(DEFAULT_TRUST_NUMBER)))
        .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
        .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE)));
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
  public void getTrustByCode() throws Exception {
    // Initialize the database
    trustRepository.saveAndFlush(trust);

    // Get all the trustList
    restTrustMockMvc.perform(get("/api/trusts/R1A"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.code").value("R1A"))
        .andExpect(jsonPath("$.trustName").value("Worcestershire Health and Care NHS Trust"));
  }


  @Test
  @Transactional
  public void shouldReturnFoundIfTrustCodeExists() throws Exception {
    // given
    // Initialise the DB
    trustRepository.saveAndFlush(trust);
    HttpStatus expectedStatus = HttpStatus.FOUND;
    String trustCode = trust.getCode();

    // when and then
    restTrustMockMvc.perform(post("/api/trusts/codeexists/")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJson(trustCode)))
        .andExpect(status().isFound());
  }

  @Test
  @Transactional
  public void shouldReturnNoContentIfTrustCodeNotExists() throws Exception {
    // given
    // Initialise the DB
    trustRepository.saveAndFlush(trust);
    HttpStatus expectedStatus = HttpStatus.NO_CONTENT;
    String trustCode = NON_EXISTING_TRUST_CODE;

    // when and then
    restTrustMockMvc.perform(post("/api/trusts/codeexists/")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJson(trustCode)))
        .andExpect(status().isNoContent());
  }

  @Test
  @Transactional
  public void getNonExistingTrust() throws Exception {
    // Get the trust
    restTrustMockMvc.perform(get("/api/trusts/code/{code}", UUID.randomUUID().toString()))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateTrust() throws Exception {
    // Initialize the database
    trust = trustRepository.saveAndFlush(trust);
    int databaseSizeBeforeUpdate = trustRepository.findAll().size();

    // Update the trust
    Trust updatedTrust = trustRepository.findOne(trust.getId());
    updatedTrust
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
    assertThat(testTrust.getCode()).isEqualTo(DEFAULT_CODE);
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
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Trust.class);
  }


  @Test
  @Transactional
  public void findTrustShouldReturnTrustWithAttributesMatchingSearchTerm() throws Exception {
    trust = trustRepository.saveAndFlush(trust);
    Trust anotherTrust = new Trust()
        .code(UPDATED_CODE)
        .localOffice(UPDATED_LOCAL_OFFICE)
        .status(UPDATED_STATUS)
        .trustKnownAs(UPDATED_TRUST_KNOWN_AS)
        .address(UPDATED_ADDRESS)
        .postCode(UPDATED_POST_CODE)
        .trustName(UPDATED_TRUST_NAME)
        .trustNumber(UPDATED_TRUST_NUMBER);

    trustRepository.saveAndFlush(anotherTrust);

    restTrustMockMvc.perform(get("/api/trusts?page=0&size=200&sort=asc&searchQuery=AAA"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].code").value(DEFAULT_CODE))
        .andExpect(jsonPath("$.[*].localOffice").value(DEFAULT_LOCAL_OFFICE))
        .andExpect(jsonPath("$.[*].status").value(DEFAULT_STATUS.toString()))
        .andExpect(jsonPath("$.[*].trustKnownAs").value(DEFAULT_TRUST_KNOWN_AS))
        .andExpect(jsonPath("$.[*].trustName").value(DEFAULT_TRUST_NAME))
        .andExpect(jsonPath("$.[*].trustNumber").value(DEFAULT_TRUST_NUMBER))
        .andExpect(jsonPath("$.[*].address").value(DEFAULT_ADDRESS))
        .andExpect(jsonPath("$.[*].postCode").value(DEFAULT_POST_CODE));
  }
}
