package com.transformuk.hee.tis.reference.service.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.api.validation.SiteValidator;
import com.transformuk.hee.tis.reference.service.exception.CustomParameterizedException;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.OrganizationType;
import com.transformuk.hee.tis.reference.service.model.Site;
import com.transformuk.hee.tis.reference.service.model.Trust;
import com.transformuk.hee.tis.reference.service.repository.OrganizationTypeRepository;
import com.transformuk.hee.tis.reference.service.repository.SiteRepository;
import com.transformuk.hee.tis.reference.service.repository.TrustRepository;
import com.transformuk.hee.tis.reference.service.service.impl.SitesTrustsService;
import com.transformuk.hee.tis.reference.service.service.mapper.SiteMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.assertj.core.util.Maps;
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
  private static final String UNENCODED_SITE_CODE = "CCCCCCCCCC";
  private static final String UNENCODED_SITE_CODE1 = "RCC25";

  private static final LocalDate DEFAULT_START_DATE = LocalDate.of(2000, 12, 01);
  private static final LocalDate DEFAULT_END_DATE = LocalDate.of(2020, 12, 31);
  private static final LocalDate UPDATED_START_DATE = LocalDate.of(2001, 01, 01);
  private static final LocalDate UPDATED_END_DATE = LocalDate.of(2021, 01, 01);

  private static final String DEFAULT_LOCAL_OFFICE = "AAAAAAAAAA";
  private static final String UPDATED_LOCAL_OFFICE = "BBBBBBBBBB";
  private static final String UNENCODED_LOCAL_OFFICE = "CCCCCCCCCC";
  private static final String UNENCODED_LOCAL_OFFICE1 = "Scarborough General Hospital";

  private static final String DEFAULT_TRUST_CODE = "AAAAAAAAAA";
  private static final String UPDATED_TRUST_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_TRUST_CODE = "CCCCCCCCCC";
  private static final String UNENCODED_TRUST_CODE1 = "RCB";

  private static final Long DEFAULT_TRUST_ID = 1L;
  private static final Long UPDATED_TRUST_ID = 2L;
  private static final Long UNENCODED_TRUST_ID = 1L;

  private static final String DEFAULT_SITE_NAME = "AAAAAAAAAA";
  private static final String UPDATED_SITE_NAME = "BBBBBBBBBB";
  private static final String UNENCODED_SITE_NAME = "Te$t Site";
  private static final String UNENCODED_SITE_NAME1 = "Scarborough General Hospital";

  private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
  private static final String UPDATED_ADDRESS = "BBBBBBBBBB";
  private static final String UNENCODED_ADDRESS = "CCCCCCCCCC";
  private static final String UNENCODED_ADDRESS1 =
      "Scarborough Hospital Woodlands Drive Scarborough North Yorkshire";

  private static final String DEFAULT_POST_CODE = "AAAAAAAAAA";
  private static final String UPDATED_POST_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_POST_CODE = "CCCCCCCCCC";
  private static final String UNENCODED_POST_CODE1 = "YO12 6QL";

  private static final String DEFAULT_SITE_KNOWN_AS = "AAAAAAAAAA";
  private static final String UPDATED_SITE_KNOWN_AS = "BBBBBBBBBB";
  private static final String UNENCODED_SITE_KNOWN_AS = "Te$t Site";
  private static final String UNENCODED_SITE_KNOWN_AS1 = "Scarborough General Hospital (RCBCA)";

  private static final String DEFAULT_SITE_NUMBER = "AAAAAAAAAA";
  private static final String UPDATED_SITE_NUMBER = "BBBBBBBBBB";
  private static final String UNENCODED_SITE_NUMBER = "CCCCCCCCCC";
  private static final String UNENCODED_SITE_NUMBER1 = "RCC25";

  private static final Status UNENCODED_STATUS = Status.CURRENT;

  private static final String DEFAULT_ORGANISATIONAL_UNIT = "AAAAAAAAAA";
  private static final String UPDATED_ORGANISATIONAL_UNIT = "BBBBBBBBBB";
  private static final String UNENCODED_ORGANISATIONAL_UNIT = "CCCCCCCCCC";
  private static final String UNENCODED_ORGANISATIONAL_UNIT1 = "OU1023";

  private static final String ORGANIZATION_TYPE_CODE = "org_type_code";
  private static final String ORGANIZATION_TYPE_LABEL = "org_type_label";

  private static final String SITE_CODE_NOT_IN_DB = "X1G5H9V";
  private static final String TRUST_CODE_NOT_IN_DB = "J8D4VTF";
  private static final String SITE_CODE = "AAAA";

  private static final String ENCODED_SEARCH_STRING = "\"Scarborough%20General%20Hospital\"";
  private static final String ENCODED_SITE_KNOWN_AS_STRING =
      "\"Scarborough%20General%20Hospital%20(RCBCA)\"";

  @Autowired
  private SiteRepository siteRepository;

  @Autowired
  private TrustRepository trustRepository;

  @Autowired
  private OrganizationTypeRepository organizationTypeRepository;

  @Autowired
  private SiteMapper siteMapper;

  @Autowired
  private SitesTrustsService sitesTrustsService;

  @Autowired
  private SiteValidator siteValidator;

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

  private Trust trust;

  private OrganizationType organizationType;

  /**
   * Create an entity for this test.
   * <p>
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static Site createEntity(EntityManager em) {
    Site site = new Site();
    site.setSiteCode(DEFAULT_SITE_CODE);
    site.setStartDate(DEFAULT_START_DATE);
    site.setEndDate(DEFAULT_END_DATE);
    site.setLocalOffice(DEFAULT_LOCAL_OFFICE);
    site.setTrustCode(DEFAULT_TRUST_CODE);
    site.setSiteName(DEFAULT_SITE_NAME);
    site.setAddress(DEFAULT_ADDRESS);
    site.setPostCode(DEFAULT_POST_CODE);
    site.setSiteKnownAs(DEFAULT_SITE_KNOWN_AS);
    site.setSiteNumber(DEFAULT_SITE_NUMBER);
    site.setOrganisationalUnit(DEFAULT_ORGANISATIONAL_UNIT);
    return site;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    SiteResource siteResource = new SiteResource(siteRepository, siteMapper, sitesTrustsService,
        siteValidator, 100);
    this.restSiteMockMvc = MockMvcBuilders.standaloneSetup(siteResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator)
        .setMessageConverters(jacksonMessageConverter).build();

    trust = new Trust();
    trust.setId(DEFAULT_TRUST_ID);
    trust.setCode(DEFAULT_TRUST_CODE);

    organizationType = new OrganizationType();
    organizationType.setCode(ORGANIZATION_TYPE_CODE);
    organizationType.setLabel(ORGANIZATION_TYPE_LABEL);
    organizationType.setStatus(Status.CURRENT);
    organizationType = organizationTypeRepository.saveAndFlush(organizationType);

    site = createEntity(em);
    site.setOrganizationType(organizationType);
  }

  @Test
  @Transactional
  public void createSite() throws Exception {
    int databaseSizeBeforeCreate = siteRepository.findAll().size();
    trustRepository.saveAndFlush(trust);

    // Create the Site
    SiteDTO siteDTO = siteMapper.siteToSiteDTO(site);
    restSiteMockMvc.perform(post("/api/sites")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(siteDTO)))
        .andExpect(status().isCreated());

    // Validate the Site in the database
    List<Site> siteList = siteRepository.findAll();
    assertThat(siteList).hasSize(databaseSizeBeforeCreate + 1);
    Site testSite = siteList.get(siteList.size() - 1);
    assertThat(testSite.getSiteCode()).isEqualTo(DEFAULT_SITE_CODE);
    assertThat(testSite.getStartDate()).isEqualTo(DEFAULT_START_DATE);
    assertThat(testSite.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    assertThat(testSite.getLocalOffice()).isEqualTo(DEFAULT_LOCAL_OFFICE);
    assertThat(testSite.getTrustCode()).isEqualTo(DEFAULT_TRUST_CODE);
    assertThat(testSite.getTrustId()).isEqualTo(DEFAULT_TRUST_ID);
    assertThat(testSite.getSiteName()).isEqualTo(DEFAULT_SITE_NAME);
    assertThat(testSite.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    assertThat(testSite.getPostCode()).isEqualTo(DEFAULT_POST_CODE);
    assertThat(testSite.getSiteKnownAs()).isEqualTo(DEFAULT_SITE_KNOWN_AS);
    assertThat(testSite.getSiteNumber()).isEqualTo(DEFAULT_SITE_NUMBER);
    assertThat(testSite.getOrganisationalUnit()).isEqualTo(DEFAULT_ORGANISATIONAL_UNIT);
    assertThat(testSite.getOrganizationType()).isEqualTo(organizationType);
  }

  @Test
  @Transactional
  public void createSiteWithExistingSiteId() throws Exception {
    int databaseSizeBeforeCreate = siteRepository.findAll().size();
    Long siteId = siteRepository.findAll().get(0).getId();
    // Create the Site with an existing siteCode
    site.setId(siteId);
    SiteDTO siteDTO = siteMapper.siteToSiteDTO(site);

    // An entity with an existing siteCode cannot be created, so this API call must fail
    restSiteMockMvc.perform(post("/api/sites")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(siteDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Alice in the database
    List<Site> siteList = siteRepository.findAll();
    assertThat(siteList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllSites() throws Exception {
    // Initialize the database
    siteRepository.deleteAllInBatch();
    siteRepository.saveAndFlush(site);

    // Get all the siteList
    restSiteMockMvc.perform(get("/api/sites?sort=siteCode,asc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].siteCode").value(hasItem(DEFAULT_SITE_CODE)))
        .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
        .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
        .andExpect(jsonPath("$.[*].localOffice").value(hasItem(DEFAULT_LOCAL_OFFICE)))
        .andExpect(jsonPath("$.[*].trustCode").value(hasItem(DEFAULT_TRUST_CODE)))
        .andExpect(jsonPath("$.[*].siteName").value(hasItem(DEFAULT_SITE_NAME)))
        .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
        .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE)))
        .andExpect(jsonPath("$.[*].siteKnownAs").value(hasItem(DEFAULT_SITE_KNOWN_AS)))
        .andExpect(jsonPath("$.[*].siteNumber").value(hasItem(DEFAULT_SITE_NUMBER)))
        .andExpect(jsonPath("$.[*].organisationalUnit").value(hasItem(DEFAULT_ORGANISATIONAL_UNIT)))
        .andExpect(jsonPath("$.[*].organizationType.id")
            .value(hasItem(organizationType.getId().intValue())))
        .andExpect(jsonPath("$.[*].organizationType.code").value(hasItem(ORGANIZATION_TYPE_CODE)))
        .andExpect(jsonPath("$.[*].organizationType.label").value(hasItem(ORGANIZATION_TYPE_LABEL)))
        .andExpect(
            jsonPath("$.[*].organizationType.status").value(hasItem(Status.CURRENT.toString())));
  }

  @Test
  @Transactional
  public void getSitesWithQuery() throws Exception {
    // Initialize the database
    Site unencodedSite = new Site();
    unencodedSite.setSiteCode(UNENCODED_SITE_CODE);
    unencodedSite.setLocalOffice(UNENCODED_LOCAL_OFFICE);
    unencodedSite.setTrustCode(UNENCODED_TRUST_CODE);
    unencodedSite.setTrustId(DEFAULT_TRUST_ID);
    unencodedSite.setSiteName(UNENCODED_SITE_NAME);
    unencodedSite.setAddress(UNENCODED_ADDRESS);
    unencodedSite.setPostCode(UNENCODED_POST_CODE);
    unencodedSite.setSiteKnownAs(UNENCODED_SITE_KNOWN_AS);
    unencodedSite.setSiteNumber(UNENCODED_SITE_NUMBER);
    unencodedSite.setOrganisationalUnit(UNENCODED_ORGANISATIONAL_UNIT);
    unencodedSite.setOrganizationType(organizationType);
    siteRepository.saveAndFlush(unencodedSite);

    trustRepository.saveAndFlush(trust);

    // Get all the siteList
    restSiteMockMvc.perform(get("/api/sites?searchQuery=\"Te%24t\"&sort=siteCode,asc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].siteCode").value(UNENCODED_SITE_CODE))
        .andExpect(jsonPath("$.[*].localOffice").value(UNENCODED_LOCAL_OFFICE))
        .andExpect(jsonPath("$.[*].trustCode").value(UNENCODED_TRUST_CODE))
        .andExpect(jsonPath("$.[*].trustId").value(UNENCODED_TRUST_ID.intValue()))
        .andExpect(jsonPath("$.[*].siteName").value(UNENCODED_SITE_NAME))
        .andExpect(jsonPath("$.[*].address").value(UNENCODED_ADDRESS))
        .andExpect(jsonPath("$.[*].postCode").value(UNENCODED_POST_CODE))
        .andExpect(jsonPath("$.[*].siteKnownAs").value(UNENCODED_SITE_KNOWN_AS))
        .andExpect(jsonPath("$.[*].siteNumber").value(UNENCODED_SITE_NUMBER))
        .andExpect(jsonPath("$.[*].organisationalUnit").value(UNENCODED_ORGANISATIONAL_UNIT))
        .andExpect(jsonPath("$.[*].organizationType.id")
            .value(hasItem(organizationType.getId().intValue())))
        .andExpect(jsonPath("$.[*].organizationType.code").value(hasItem(ORGANIZATION_TYPE_CODE)))
        .andExpect(jsonPath("$.[*].organizationType.label").value(hasItem(ORGANIZATION_TYPE_LABEL)))
        .andExpect(
            jsonPath("$.[*].organizationType.status").value(hasItem(Status.CURRENT.toString())));
  }

  @Test
  @Transactional
  public void getAllSitesIn() throws Exception {
    // Initialize the database
    siteRepository.saveAndFlush(site);

    // Get the sites given codes
    restSiteMockMvc.perform(get("/api/sites/in/invalid," + DEFAULT_SITE_CODE))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].siteCode").value(hasItem(DEFAULT_SITE_CODE)))
        .andExpect(jsonPath("$.[*].localOffice").value(hasItem(DEFAULT_LOCAL_OFFICE)))
        .andExpect(jsonPath("$.[*].trustCode").value(hasItem(DEFAULT_TRUST_CODE)))
        .andExpect(jsonPath("$.[*].siteName").value(hasItem(DEFAULT_SITE_NAME)))
        .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
        .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE)))
        .andExpect(jsonPath("$.[*].siteKnownAs").value(hasItem(DEFAULT_SITE_KNOWN_AS)))
        .andExpect(jsonPath("$.[*].siteNumber").value(hasItem(DEFAULT_SITE_NUMBER)))
        .andExpect(jsonPath("$.[*].organisationalUnit").value(hasItem(DEFAULT_ORGANISATIONAL_UNIT)))
        .andExpect(jsonPath("$.[*].organizationType.id")
            .value(hasItem(organizationType.getId().intValue())))
        .andExpect(jsonPath("$.[*].organizationType.code").value(hasItem(ORGANIZATION_TYPE_CODE)))
        .andExpect(jsonPath("$.[*].organizationType.label").value(hasItem(ORGANIZATION_TYPE_LABEL)))
        .andExpect(
            jsonPath("$.[*].organizationType.status").value(hasItem(Status.CURRENT.toString())));
  }

  @Test
  @Transactional
  public void searchForSite() throws Exception {
    // Initialize the database
    siteRepository.saveAndFlush(site);

    // Get all the siteList
    restSiteMockMvc.perform(get("/api/sites/search?searchString=R1AAA"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
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
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
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
    site = siteRepository.saveAndFlush(this.site);

    // Get all the siteList
    restSiteMockMvc.perform(get("/api/sites/code/{code}", DEFAULT_SITE_CODE))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(site.getId().intValue()))
        .andExpect(jsonPath("$.siteCode").value(DEFAULT_SITE_CODE))
        .andExpect(jsonPath("$.trustCode").value(DEFAULT_TRUST_CODE))
        .andExpect(jsonPath("$.siteName").value(DEFAULT_SITE_NAME))
        .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
        .andExpect(jsonPath("$.postCode").value(DEFAULT_POST_CODE));
  }

  @Test
  @Transactional
  public void getSite() throws Exception {
    // Initialize the database
    site = siteRepository.saveAndFlush(this.site);

    // Get the site
    restSiteMockMvc.perform(get("/api/sites/{id}", this.site.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.siteCode").value(DEFAULT_SITE_CODE))
        .andExpect(jsonPath("$.localOffice").value(DEFAULT_LOCAL_OFFICE))
        .andExpect(jsonPath("$.trustCode").value(DEFAULT_TRUST_CODE))
        .andExpect(jsonPath("$.siteName").value(DEFAULT_SITE_NAME))
        .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
        .andExpect(jsonPath("$.postCode").value(DEFAULT_POST_CODE))
        .andExpect(jsonPath("$.siteKnownAs").value(DEFAULT_SITE_KNOWN_AS))
        .andExpect(jsonPath("$.siteNumber").value(DEFAULT_SITE_NUMBER))
        .andExpect(jsonPath("$.organisationalUnit").value(DEFAULT_ORGANISATIONAL_UNIT))
        .andExpect(jsonPath("$.organizationType.id").value(organizationType.getId().intValue()))
        .andExpect(jsonPath("$.organizationType.code").value(ORGANIZATION_TYPE_CODE))
        .andExpect(jsonPath("$.organizationType.label").value(ORGANIZATION_TYPE_LABEL))
        .andExpect(jsonPath("$.organizationType.status").value(Status.CURRENT.toString()));
  }

  @Test
  @Transactional
  public void shouldReturnTrueIfSiteExistsAndFalseIfNotExists() throws Exception {
    // Initialize the database
    siteRepository.saveAndFlush(site);
    Map<String, Boolean> expectedMap = Maps.newHashMap(site.getSiteCode(), true);
    restSiteMockMvc.perform(
        post("/api/sites/exists")
            .contentType(APPLICATION_JSON_VALUE)
            .content(TestUtil.convertObjectToJsonBytes(Lists.newArrayList(site.getSiteCode()))))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
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
        .contentType(MediaType.APPLICATION_JSON)
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
        .contentType(MediaType.APPLICATION_JSON)
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
        .contentType(MediaType.APPLICATION_JSON)
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
        .contentType(MediaType.APPLICATION_JSON)
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
        .contentType(MediaType.APPLICATION_JSON)
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
    site = siteRepository.saveAndFlush(site);
    trustRepository.saveAndFlush(trust);
    int databaseSizeBeforeUpdate = siteRepository.findAll().size();

    // Update the site
    Site updatedSite = siteRepository.findById(site.getId()).get();
    updatedSite.setLocalOffice(UPDATED_LOCAL_OFFICE);
    updatedSite.setStartDate(UPDATED_START_DATE);
    updatedSite.setEndDate(UPDATED_END_DATE);
    updatedSite.setTrustCode(UPDATED_TRUST_CODE);
    updatedSite.setTrustId(UPDATED_TRUST_ID);
    updatedSite.setSiteName(UPDATED_SITE_NAME);
    updatedSite.setAddress(UPDATED_ADDRESS);
    updatedSite.setPostCode(UPDATED_POST_CODE);
    updatedSite.setSiteKnownAs(UPDATED_SITE_KNOWN_AS);
    updatedSite.setSiteNumber(UPDATED_SITE_NUMBER);
    updatedSite.setOrganisationalUnit(UPDATED_ORGANISATIONAL_UNIT);
    SiteDTO siteDTO = siteMapper.siteToSiteDTO(updatedSite);

    // have a different Trust ready in the database
    Trust trust2 = new Trust();
    trust2.setId(UPDATED_TRUST_ID);
    trust2.setCode(UPDATED_TRUST_CODE);
    trustRepository.saveAndFlush(trust2);

    restSiteMockMvc.perform(put("/api/sites")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(siteDTO)))
        .andExpect(status().isOk());

    // Validate the Site in the database
    List<Site> siteList = siteRepository.findAll();
    assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    Site testSite = siteList.get(siteList.size() - 1);
    assertThat(testSite.getSiteCode()).isEqualTo(DEFAULT_SITE_CODE);
    assertThat(testSite.getStartDate()).isEqualTo(UPDATED_START_DATE.toString());
    assertThat(testSite.getEndDate()).isEqualTo(UPDATED_END_DATE.toString());
    assertThat(testSite.getLocalOffice()).isEqualTo(UPDATED_LOCAL_OFFICE);
    assertThat(testSite.getTrustCode()).isEqualTo(UPDATED_TRUST_CODE);
    assertThat(testSite.getTrustId()).isEqualTo(UPDATED_TRUST_ID);
    assertThat(testSite.getSiteName()).isEqualTo(UPDATED_SITE_NAME);
    assertThat(testSite.getAddress()).isEqualTo(UPDATED_ADDRESS);
    assertThat(testSite.getPostCode()).isEqualTo(UPDATED_POST_CODE);
    assertThat(testSite.getSiteKnownAs()).isEqualTo(UPDATED_SITE_KNOWN_AS);
    assertThat(testSite.getSiteNumber()).isEqualTo(UPDATED_SITE_NUMBER);
    assertThat(testSite.getOrganisationalUnit()).isEqualTo(UPDATED_ORGANISATIONAL_UNIT);
    assertThat(testSite.getOrganizationType()).isEqualTo(organizationType);
  }

  @Test
  @Transactional
  public void updateNonExistingSiteShouldFailValidation() throws Exception {
    int databaseSizeBeforeUpdate = siteRepository.findAll().size();

    // Create the Site
    SiteDTO siteDTO = siteMapper.siteToSiteDTO(site);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restSiteMockMvc.perform(put("/api/sites")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(siteDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Site in the database
    List<Site> siteList = siteRepository.findAll();
    assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
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
    Site anotherSite = new Site();
    anotherSite.setSiteCode(UPDATED_SITE_CODE);
    anotherSite.setLocalOffice(UPDATED_LOCAL_OFFICE);
    anotherSite.setTrustCode(UPDATED_TRUST_CODE);
    anotherSite.setSiteName(UPDATED_SITE_NAME);
    anotherSite.setAddress(UPDATED_ADDRESS);
    anotherSite.setPostCode(UPDATED_POST_CODE);
    anotherSite.setSiteKnownAs(UPDATED_SITE_KNOWN_AS);
    anotherSite.setSiteNumber(UPDATED_SITE_NUMBER);
    anotherSite.setOrganisationalUnit(UPDATED_ORGANISATIONAL_UNIT);
    siteRepository.saveAndFlush(anotherSite);

    restSiteMockMvc.perform(get("/api/sites?searchQuery=\"AAA\""))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].siteCode").value(hasItem(site.getSiteCode())))
        .andExpect(jsonPath("$.[*].siteName").value(hasItem(site.getSiteName())))
        .andExpect(jsonPath("$.[*].address").value(hasItem(site.getAddress())))
        .andExpect(jsonPath("$.[*].siteCode").value(not(hasItem(anotherSite.getSiteCode()))))
        .andExpect(jsonPath("$.[*].siteName").value(not(hasItem(anotherSite.getSiteName()))))
        .andExpect(jsonPath("$.[*].address").value(not(hasItem(anotherSite.getAddress()))));

  }

  @Test
  @Transactional
  public void searchSitesWithEncodedCharacterShouldReturnCorrectSite() throws Exception {

    //Given //Initialise test data
    Site unEncodedSite = new Site();
    unEncodedSite.setSiteCode(UNENCODED_SITE_CODE1);
    unEncodedSite.setLocalOffice(UNENCODED_LOCAL_OFFICE1);
    unEncodedSite.setTrustCode(UNENCODED_TRUST_CODE1);
    unEncodedSite.setSiteName(UNENCODED_SITE_NAME1);
    unEncodedSite.setAddress(UNENCODED_ADDRESS1);
    unEncodedSite.setPostCode(UNENCODED_POST_CODE1);
    unEncodedSite.setSiteKnownAs(UNENCODED_SITE_KNOWN_AS1);
    unEncodedSite.setSiteNumber(UNENCODED_SITE_NUMBER1);
    unEncodedSite.setStatus(UNENCODED_STATUS);
    unEncodedSite.setOrganisationalUnit(UNENCODED_ORGANISATIONAL_UNIT1);
    siteRepository.saveAndFlush(unEncodedSite);

    //When Then //Search using URLEncoded characters
    restSiteMockMvc
        .perform(get("/api/sites")
            .param("searchQuery", ENCODED_SEARCH_STRING).param("page", "0")
            .param("size", "200").param("sort", "siteKnownAs,asc")
            .param("columnFilters", "{\"status\":[\"CURRENT\"]}"))
        //Verify field values
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].siteName").value(UNENCODED_SITE_NAME1));

    //When Then //Search using 'siteKnownAs'
    restSiteMockMvc
        .perform(get("/api/sites")
            .param("searchQuery", ENCODED_SITE_KNOWN_AS_STRING).param("page", "0")
            .param("size", "200").param("sort", "siteKnownAs,asc")
            .param("columnFilters", "{\"status\":[\"CURRENT\"]}"))
        // Verify field values
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.[*].siteName").value(UNENCODED_SITE_NAME1));
  }

  @Test(expected = CustomParameterizedException.class)
  @Transactional
  public void startDateShouldBeBeforeEndDate() {
    //Given
    SiteDTO siteDTO = new SiteDTO();
    siteDTO.setStartDate(LocalDate.of(2021, 12, 01));
    siteDTO.setEndDate(LocalDate.of(2020, 12, 01));

    //When
    siteValidator.validate(siteDTO);

    //Then
    throw new CustomParameterizedException("Start Date needs to be before End Date");

  }
}
