package com.transformuk.hee.tis.reference.service.api;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.Site;
import com.transformuk.hee.tis.reference.service.repository.SiteRepository;
import com.transformuk.hee.tis.reference.service.service.impl.SitesTrustsService;
import com.transformuk.hee.tis.reference.service.service.mapper.SiteMapper;
import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the SiteResource REST controller.
 *
 * @see SiteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SiteResourceIntTest {

  private static final String DEFAULT_SITE_CODE = "AAAAAAAAAA";
  private static final String UPDATED_SITE_CODE = "BBBBBBBBBB";

  private static final String DEFAULT_LOCAL_OFFICE = "AAAAAAAAAA";
  private static final String UPDATED_LOCAL_OFFICE = "BBBBBBBBBB";

  private static final String DEFAULT_TRUST_CODE = "AAAAAAAAAA";
  private static final String UPDATED_TRUST_CODE = "BBBBBBBBBB";

  private static final String DEFAULT_SITE_NAME = "AAAAAAAAAA";
  private static final String UPDATED_SITE_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
  private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

  private static final String DEFAULT_POST_CODE = "AAAAAAAAAA";
  private static final String UPDATED_POST_CODE = "BBBBBBBBBB";

  private static final String DEFAULT_SITE_KNOWN_AS = "AAAAAAAAAA";
  private static final String UPDATED_SITE_KNOWN_AS = "BBBBBBBBBB";

  private static final String DEFAULT_SITE_NUMBER = "AAAAAAAAAA";
  private static final String UPDATED_SITE_NUMBER = "BBBBBBBBBB";

  private static final String DEFAULT_ORGANISATIONAL_UNIT = "AAAAAAAAAA";
  private static final String UPDATED_ORGANISATIONAL_UNIT = "BBBBBBBBBB";

  private static final String SITE_CODE_NOT_IN_DB = "X1G5H9V";
  private static final String TRUST_CODE_NOT_IN_DB = "J8D4VTF";
  private static final String SITE_CODE = "AAAA";

  @Autowired
  private SiteRepository siteRepository;

  @Autowired
  private SiteMapper siteMapper;

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

  private MockMvc restSiteMockMvc;

  private Site site;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static Site createEntity(EntityManager em) {
    Site site = new Site()
        .siteCode(DEFAULT_SITE_CODE)
        .localOffice(DEFAULT_LOCAL_OFFICE)
        .trustCode(DEFAULT_TRUST_CODE)
        .siteName(DEFAULT_SITE_NAME)
        .address(DEFAULT_ADDRESS)
        .postCode(DEFAULT_POST_CODE)
        .siteKnownAs(DEFAULT_SITE_KNOWN_AS)
        .siteNumber(DEFAULT_SITE_NUMBER)
        .organisationalUnit(DEFAULT_ORGANISATIONAL_UNIT);
    return site;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    SiteResource siteResource = new SiteResource(siteRepository, siteMapper, sitesTrustsService, 100);
    this.restSiteMockMvc = MockMvcBuilders.standaloneSetup(siteResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();
  }

  @Before
  public void initTest() {
    site = createEntity(em);
  }

  @Test
  @Transactional
  public void createSite() throws Exception {
    int databaseSizeBeforeCreate = siteRepository.findAll().size();

    // Create the Site
    SiteDTO siteDTO = siteMapper.siteToSiteDTO(site);
    restSiteMockMvc.perform(post("/api/sites")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(siteDTO)))
        .andExpect(status().isCreated());

    // Validate the Site in the database
    List<Site> siteList = siteRepository.findAll();
    assertThat(siteList).hasSize(databaseSizeBeforeCreate + 1);
    Site testSite = siteList.get(siteList.size() - 1);
    assertThat(testSite.getSiteCode()).isEqualTo(DEFAULT_SITE_CODE);
    assertThat(testSite.getLocalOffice()).isEqualTo(DEFAULT_LOCAL_OFFICE);
    assertThat(testSite.getTrustCode()).isEqualTo(DEFAULT_TRUST_CODE);
    assertThat(testSite.getSiteName()).isEqualTo(DEFAULT_SITE_NAME);
    assertThat(testSite.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    assertThat(testSite.getPostCode()).isEqualTo(DEFAULT_POST_CODE);
    assertThat(testSite.getSiteKnownAs()).isEqualTo(DEFAULT_SITE_KNOWN_AS);
    assertThat(testSite.getSiteNumber()).isEqualTo(DEFAULT_SITE_NUMBER);
    assertThat(testSite.getOrganisationalUnit()).isEqualTo(DEFAULT_ORGANISATIONAL_UNIT);
  }

  @Test
  @Transactional
  public void createSiteWithExistingSiteCode() throws Exception {
    int databaseSizeBeforeCreate = siteRepository.findAll().size();
    String siteCode = siteRepository.findAll().get(0).getSiteCode();
    // Create the Site with an existing siteCode
    site.setSiteCode(siteCode);
    SiteDTO siteDTO = siteMapper.siteToSiteDTO(site);

    // An entity with an existing siteCode cannot be created, so this API call must fail
    restSiteMockMvc.perform(post("/api/sites")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(siteDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<Site> siteList = siteRepository.findAll();
    assertThat(siteList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkSiteCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = siteRepository.findAll().size();
    // set the field null
    site.setSiteCode(null);

    // Create the Site, which fails.
    SiteDTO siteDTO = siteMapper.siteToSiteDTO(site);

    restSiteMockMvc.perform(post("/api/sites")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(siteDTO)))
        .andExpect(status().isBadRequest());

    List<Site> siteList = siteRepository.findAll();
    assertThat(siteList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllSites() throws Exception {
    // Initialize the database
    siteRepository.saveAndFlush(site);

    // Get all the siteList
    restSiteMockMvc.perform(get("/api/sites?sort=siteCode,asc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].siteCode").value(hasItem(DEFAULT_SITE_CODE)))
        .andExpect(jsonPath("$.[*].localOffice").value(hasItem(DEFAULT_LOCAL_OFFICE)))
        .andExpect(jsonPath("$.[*].trustCode").value(hasItem(DEFAULT_TRUST_CODE)))
        .andExpect(jsonPath("$.[*].siteName").value(hasItem(DEFAULT_SITE_NAME)))
        .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
        .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE)))
        .andExpect(jsonPath("$.[*].siteKnownAs").value(hasItem(DEFAULT_SITE_KNOWN_AS)))
        .andExpect(jsonPath("$.[*].siteNumber").value(hasItem(DEFAULT_SITE_NUMBER)))
        .andExpect(jsonPath("$.[*].organisationalUnit").value(hasItem(DEFAULT_ORGANISATIONAL_UNIT)));
  }

  @Test
  @Transactional
  public void getAllSitesIn() throws Exception {
    // Initialize the database
    siteRepository.saveAndFlush(site);

    // Get the sites given codes
    restSiteMockMvc.perform(get("/api/sites/in/invalid," + DEFAULT_SITE_CODE))
        .andExpect(status().isFound())
        .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].siteCode").value(hasItem(DEFAULT_SITE_CODE)))
        .andExpect(jsonPath("$.[*].localOffice").value(hasItem(DEFAULT_LOCAL_OFFICE)))
        .andExpect(jsonPath("$.[*].trustCode").value(hasItem(DEFAULT_TRUST_CODE)))
        .andExpect(jsonPath("$.[*].siteName").value(hasItem(DEFAULT_SITE_NAME)))
        .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
        .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE)))
        .andExpect(jsonPath("$.[*].siteKnownAs").value(hasItem(DEFAULT_SITE_KNOWN_AS)))
        .andExpect(jsonPath("$.[*].siteNumber").value(hasItem(DEFAULT_SITE_NUMBER)))
        .andExpect(jsonPath("$.[*].organisationalUnit").value(hasItem(DEFAULT_ORGANISATIONAL_UNIT)));
  }

  @Test
  @Transactional
  public void searchForSite() throws Exception {
    // Initialize the database
    siteRepository.saveAndFlush(site);

    // Get all the siteList
    restSiteMockMvc.perform(get("/api/sites/search?searchString=R1AAA"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.total").value(1))
        .andExpect(jsonPath("$.list[0].siteCode").value("R1AAA"))
        .andExpect(jsonPath("$.list[0].trustCode").value("R1A"))
        .andExpect(jsonPath("$.list[0].siteName").value("Malvern Friends Meeting House"))
        .andExpect(jsonPath("$.list[0].address").value("1 Orchard Road Malvern Worcestershire"))
        .andExpect(jsonPath("$.list[0].postCode").value("WR14 3DA"));
  }

  @Test
  @Transactional
  public void searchForSiteInTrust() throws Exception {
    // Initialize the database
    siteRepository.saveAndFlush(site);

    // Get all the siteList
    restSiteMockMvc.perform(get("/api/sites/search-by-trust/R1A?searchString=R1AAA"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.total").value(1))
        .andExpect(jsonPath("$.list[0].siteCode").value("R1AAA"))
        .andExpect(jsonPath("$.list[0].trustCode").value("R1A"))
        .andExpect(jsonPath("$.list[0].siteName").value("Malvern Friends Meeting House"))
        .andExpect(jsonPath("$.list[0].address").value("1 Orchard Road Malvern Worcestershire"))
        .andExpect(jsonPath("$.list[0].postCode").value("WR14 3DA"));
  }

  @Test
  @Transactional
  public void getSiteByCode() throws Exception {
    // Initialize the database
    siteRepository.saveAndFlush(site);

    // Get all the siteList
    restSiteMockMvc.perform(get("/api/sites/code/R1AAA"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.siteCode").value("R1AAA"))
        .andExpect(jsonPath("$.trustCode").value("R1A"))
        .andExpect(jsonPath("$.siteName").value("Malvern Friends Meeting House"))
        .andExpect(jsonPath("$.address").value("1 Orchard Road Malvern Worcestershire"))
        .andExpect(jsonPath("$.postCode").value("WR14 3DA"));
  }

  @Test
  @Transactional
  public void getSite() throws Exception {
    // Initialize the database
    siteRepository.saveAndFlush(site);

    // Get the site
    restSiteMockMvc.perform(get("/api/sites/{siteCode}", site.getSiteCode()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.siteCode").value(DEFAULT_SITE_CODE))
        .andExpect(jsonPath("$.localOffice").value(DEFAULT_LOCAL_OFFICE))
        .andExpect(jsonPath("$.trustCode").value(DEFAULT_TRUST_CODE))
        .andExpect(jsonPath("$.siteName").value(DEFAULT_SITE_NAME))
        .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
        .andExpect(jsonPath("$.postCode").value(DEFAULT_POST_CODE))
        .andExpect(jsonPath("$.siteKnownAs").value(DEFAULT_SITE_KNOWN_AS))
        .andExpect(jsonPath("$.siteNumber").value(DEFAULT_SITE_NUMBER))
        .andExpect(jsonPath("$.organisationalUnit").value(DEFAULT_ORGANISATIONAL_UNIT));
  }

  @Test
  @Transactional
  public void shouldReturnTrueIfSiteExistsAndFalseIfNotExists() throws Exception {
    // Initialize the database
    siteRepository.saveAndFlush(site);
    Map<String, Boolean> expectedMap = Maps.newHashMap(site.getSiteCode(), true);
    restSiteMockMvc.perform(
        post("/api/sites/exists")
            .contentType(APPLICATION_JSON_UTF8_VALUE)
            .content(TestUtil.convertObjectToJsonBytes(Lists.newArrayList(site.getSiteCode()))))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
        .andExpect(content().string(TestUtil.convertObjectToJson(expectedMap)));
  }

  @Test
  @Transactional
  public void shouldReturnFoundIfSiteCodeExists() throws Exception {
    // given
    // Initialise the DB
    siteRepository.saveAndFlush(site);
    HttpStatus expectedStatus = HttpStatus.FOUND;
    String siteCode = site.getSiteCode();

    // when and then
    restSiteMockMvc.perform(post("/api/sites/codeexists/")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJson(siteCode)))
        .andExpect(status().isFound());
  }

  @Test
  @Transactional
  public void shouldReturnNoContentIfSiteCodeNotExists() throws Exception {
    // given
    // Initialise the DB
    siteRepository.saveAndFlush(site);
    HttpStatus expectedStatus = HttpStatus.NO_CONTENT;
    String siteCode = SITE_CODE_NOT_IN_DB;

    // when and then
    restSiteMockMvc.perform(post("/api/sites/codeexists/")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJson(siteCode)))
        .andExpect(status().isNoContent());
  }

  @Test
  @Transactional
  public void shouldReturnFoundIfSiteTrustMatchExists() throws Exception {
    // given
    // Initialise the DB
    siteRepository.saveAndFlush(site);
    HttpStatus expectedStatus = HttpStatus.FOUND;
    String siteCode = site.getSiteCode();
    String trustCode = site.getTrustCode();

    // when and then
    restSiteMockMvc.perform(post("/api/sites/trustmatch/" + trustCode)
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJson(siteCode)))
        .andExpect(status().isFound());
  }

  @Test
  @Transactional
  public void shouldReturnNoContentIfSiteTrustMatchNotExistsBadSite() throws Exception {
    // given
    // Initialise the DB
    siteRepository.saveAndFlush(site);
    HttpStatus expectedStatus = HttpStatus.NO_CONTENT;
    String siteCode = SITE_CODE_NOT_IN_DB;
    String trustCode = site.getTrustCode();

    // when and then
    restSiteMockMvc.perform(post("/api/sites/trustmatch/" + trustCode)
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJson(siteCode)))
        .andExpect(status().isNoContent());
  }

  @Test
  @Transactional
  public void shouldReturnNoContentIfSiteTrustMatchNotExistsBadTrust() throws Exception {
    // given
    // Initialise the DB
    siteRepository.saveAndFlush(site);
    HttpStatus expectedStatus = HttpStatus.NO_CONTENT;
    String siteCode = site.getSiteCode();
    String trustCode = TRUST_CODE_NOT_IN_DB;

    // when and then
    restSiteMockMvc.perform(post("/api/sites/trustmatch/" + trustCode)
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJson(siteCode)))
        .andExpect(status().isNoContent());
  }

  @Test
  @Transactional
  public void getNonExistingSite() throws Exception {
    // Get the site
    restSiteMockMvc.perform(get("/api/sites/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateSite() throws Exception {
    // Initialize the database
    siteRepository.saveAndFlush(site);
    int databaseSizeBeforeUpdate = siteRepository.findAll().size();

    // Update the site
    Site updatedSite = siteRepository.findOne(site.getSiteCode());
    updatedSite
        .localOffice(UPDATED_LOCAL_OFFICE)
        .trustCode(UPDATED_TRUST_CODE)
        .siteName(UPDATED_SITE_NAME)
        .address(UPDATED_ADDRESS)
        .postCode(UPDATED_POST_CODE)
        .siteKnownAs(UPDATED_SITE_KNOWN_AS)
        .siteNumber(UPDATED_SITE_NUMBER)
        .organisationalUnit(UPDATED_ORGANISATIONAL_UNIT);
    SiteDTO siteDTO = siteMapper.siteToSiteDTO(updatedSite);

    restSiteMockMvc.perform(put("/api/sites")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(siteDTO)))
        .andExpect(status().isOk());

    // Validate the Site in the database
    List<Site> siteList = siteRepository.findAll();
    assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    Site testSite = siteList.get(siteList.size() - 1);
    assertThat(testSite.getSiteCode()).isEqualTo(DEFAULT_SITE_CODE);
    assertThat(testSite.getLocalOffice()).isEqualTo(UPDATED_LOCAL_OFFICE);
    assertThat(testSite.getTrustCode()).isEqualTo(UPDATED_TRUST_CODE);
    assertThat(testSite.getSiteName()).isEqualTo(UPDATED_SITE_NAME);
    assertThat(testSite.getAddress()).isEqualTo(UPDATED_ADDRESS);
    assertThat(testSite.getPostCode()).isEqualTo(UPDATED_POST_CODE);
    assertThat(testSite.getSiteKnownAs()).isEqualTo(UPDATED_SITE_KNOWN_AS);
    assertThat(testSite.getSiteNumber()).isEqualTo(UPDATED_SITE_NUMBER);
    assertThat(testSite.getOrganisationalUnit()).isEqualTo(UPDATED_ORGANISATIONAL_UNIT);
  }

  @Test
  @Transactional
  public void updateNonExistingSite() throws Exception {
    int databaseSizeBeforeUpdate = siteRepository.findAll().size();

    // Create the Site
    SiteDTO siteDTO = siteMapper.siteToSiteDTO(site);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restSiteMockMvc.perform(put("/api/sites")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(siteDTO)))
        .andExpect(status().isOk());

    // Validate the Site in the database
    List<Site> siteList = siteRepository.findAll();
    assertThat(siteList).hasSize(databaseSizeBeforeUpdate + 1);
  }

  @Test
  @Transactional
  public void deleteSite() throws Exception {
    // Initialize the database
    siteRepository.saveAndFlush(site);
    int databaseSizeBeforeDelete = siteRepository.findAll().size();

    // Get the site
    restSiteMockMvc.perform(delete("/api/sites/{siteCode}", site.getSiteCode())
        .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

    // Validate the database is empty
    List<Site> siteList = siteRepository.findAll();
    assertThat(siteList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Site.class);
  }

  @Test
  @Transactional
  public void findSiteShouldReturnSiteWithAttributesMatchingSearchTerm() throws Exception {
    siteRepository.saveAndFlush(site);
    Site anotherSite = new Site()
        .siteCode(UPDATED_SITE_CODE)
        .localOffice(UPDATED_LOCAL_OFFICE)
        .trustCode(UPDATED_TRUST_CODE)
        .siteName(UPDATED_SITE_NAME)
        .address(UPDATED_ADDRESS)
        .postCode(UPDATED_POST_CODE)
        .siteKnownAs(UPDATED_SITE_KNOWN_AS)
        .siteNumber(UPDATED_SITE_NUMBER)
        .organisationalUnit(UPDATED_ORGANISATIONAL_UNIT);
    siteRepository.saveAndFlush(anotherSite);


    restSiteMockMvc.perform(get("/api/sites?searchQuery=AAA"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].siteCode").value(hasItem(site.getSiteCode())))
        .andExpect(jsonPath("$.[*].siteName").value(hasItem(site.getSiteName())))
        .andExpect(jsonPath("$.[*].address").value(hasItem(site.getAddress())))
        .andExpect(jsonPath("$.[*].siteCode").value(not(hasItem(anotherSite.getSiteCode()))))
        .andExpect(jsonPath("$.[*].siteName").value(not(hasItem(anotherSite.getSiteName()))))
        .andExpect(jsonPath("$.[*].address").value(not(hasItem(anotherSite.getAddress()))));

  }
}
