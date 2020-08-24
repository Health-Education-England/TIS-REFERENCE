package com.transformuk.hee.tis.reference.service.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.TrustDTO;
import com.transformuk.hee.tis.reference.api.enums.Status;
import com.transformuk.hee.tis.reference.service.Application;
import com.transformuk.hee.tis.reference.service.exception.ExceptionTranslator;
import com.transformuk.hee.tis.reference.service.model.Trust;
import com.transformuk.hee.tis.reference.service.repository.TrustRepository;
import com.transformuk.hee.tis.reference.service.service.impl.PermissionService;
import com.transformuk.hee.tis.reference.service.service.impl.SitesTrustsService;
import com.transformuk.hee.tis.reference.service.service.mapper.TrustMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

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
  private static final String UNENCODED_CODE = "cccccccccc";

  private static final String DEFAULT_LOCAL_OFFICE = "AAAAAAAAAA";
  private static final String UPDATED_LOCAL_OFFICE = "BBBBBBBBBB";
  private static final String UNENCODED_LOCAL_OFFICE = "CCCCCCCCCC";

  private static final Status DEFAULT_STATUS = Status.CURRENT;
  private static final Status UPDATED_STATUS = Status.INACTIVE;
  private static final Status UNENCODED_STATUS = Status.CURRENT;

  private static final String DEFAULT_TRUST_KNOWN_AS = "AAAAAAAAAA";
  private static final String UPDATED_TRUST_KNOWN_AS = "BBBBBBBBBB";
  private static final String UNENCODED_TRUST_KNOWN_AS =
      "Guy's and $t Thomas' NHS Foundation Trust (G$T)";

  private static final String DEFAULT_TRUST_NAME = "AAAAAAAAAA";
  private static final String UPDATED_TRUST_NAME = "BBBBBBBBBB";
  private static final String UNENCODED_TRUST_NAME = "Guy's & $t Thomas' NHS Foundation Trust";

  private static final String DEFAULT_TRUST_NUMBER = "AAAAAAAAAA";
  private static final String UPDATED_TRUST_NUMBER = "BBBBBBBBBB";
  private static final String UNENCODED_TRUST_NUMBER = "BBBBBBBBBB";

  private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
  private static final String UPDATED_ADDRESS = "BBBBBBBBBB";
  private static final String UNENCODED_ADDRESS = "BBBBBBBBBB";

  private static final String DEFAULT_POST_CODE = "AAAAAAAAAA";
  private static final String UPDATED_POST_CODE = "BBBBBBBBBB";
  private static final String UNENCODED_POST_CODE = "BBBBBBBBBB";

  private static final String NON_EXISTING_TRUST_CODE = "XFK43F6";

  // The ADMINS-UI, json encodes the string, which wraps the value in quotes.
  private static final String ENCODED_SEARCH_QUERY = "\"Guy's%20%26%20%24t%20T\"";
  private static final String ENCODED_KNOWN_AS_QUERY = "\"(G%24T)\"";

  @Autowired
  private TrustRepository trustRepository;

  @Autowired
  private TrustMapper trustMapper;

  @MockBean
  private PermissionService permissionServiceMock;

  @Autowired
  private MutableAclService mutableAclService;

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
   * This is a static method, as tests for other entities might also need it, if they test an entity
   * which requires the current entity.
   */
  public static Trust createEntity(EntityManager em) {
    Trust trust = new Trust().code(DEFAULT_CODE).localOffice(DEFAULT_LOCAL_OFFICE)
        .status(DEFAULT_STATUS).trustKnownAs(DEFAULT_TRUST_KNOWN_AS).trustName(DEFAULT_TRUST_NAME)
        .trustNumber(DEFAULT_TRUST_NUMBER).address(DEFAULT_ADDRESS).postCode(DEFAULT_POST_CODE);
    return trust;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    TrustResource trustResource =
        new TrustResource(trustRepository, trustMapper, sitesTrustsService, 100);
    this.restTrustMockMvc = MockMvcBuilders.standaloneSetup(trustResource)
        .setCustomArgumentResolvers(pageableArgumentResolver)
        .setControllerAdvice(exceptionTranslator).setMessageConverters(jacksonMessageConverter)
        .build();
  }

  @Before
  public void initTest() {
    trust = createEntity(em);
  }

  @Test
  @WithMockUser(authorities = {"HEE", "NI"})
  @Transactional
  public void createTrust() throws Exception {
    when(permissionServiceMock.getUserEntities()).thenReturn(Sets.newHashSet("HEE", "NI"));

    int databaseSizeBeforeCreate = trustRepository.findAll().size();

    // Create the Trust
    TrustDTO trustDTO = trustMapper.trustToTrustDTO(trust);
    restTrustMockMvc.perform(post("/api/trusts").contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(trustDTO))).andExpect(status().isCreated());

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

    Acl acl = mutableAclService.readAclById(new ObjectIdentityImpl(Trust.class.getName(), testTrust.getId()));
    List<AccessControlEntry> aclEntires = acl.getEntries();
    assertThat(aclEntires.size()).isEqualTo(4);
    Set<Sid> sids = aclEntires.stream().map(e -> e.getSid()).collect(Collectors.toSet());
    assertThat(sids).contains(new GrantedAuthoritySid("HEE"), new GrantedAuthoritySid("NI"));
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
    restTrustMockMvc
        .perform(post("/api/trusts").contentType(TestUtil.APPLICATION_JSON_UTF8)
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
    restTrustMockMvc.perform(get("/api/trusts?sort=code,asc")).andExpect(status().isOk())
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
    restTrustMockMvc.perform(get("/api/trusts/search/?searchString=R1A")).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.total").value(1)).andExpect(jsonPath("$.list[0].code").value("R1A"))
        .andExpect(
            jsonPath("$.list[0].trustName").value("Worcestershire Health and Care NHS Trust"));
  }

  @Test
  @Transactional
  public void getTrustByCode() throws Exception {
    // Initialize the database
    trustRepository.saveAndFlush(trust);

    // Get all the trustList
    restTrustMockMvc.perform(get("/api/trusts/{id}", trust.getId())).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(trust.getId().intValue()))
        .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
        .andExpect(jsonPath("$.trustName").value(DEFAULT_TRUST_NAME));
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
    restTrustMockMvc
        .perform(post("/api/trusts/codeexists/").contentType(TestUtil.APPLICATION_JSON_UTF8)
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
    restTrustMockMvc
        .perform(post("/api/trusts/codeexists/").contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJson(trustCode)))
        .andExpect(status().isNoContent());
  }

  @Test
  @Transactional
  public void shouldNotReturnInactiveTrust() throws Exception {
    Trust inactiveTrust = createEntity(em);
    inactiveTrust.status(Status.INACTIVE);
    trustRepository.saveAndFlush((inactiveTrust));

    restTrustMockMvc.perform(get("/api/trusts/code/{code}", inactiveTrust.getCode()))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void getNonExistingTrust() throws Exception {
    // Get the trust
    restTrustMockMvc.perform(get("/api/trusts/code/{code}", UUID.randomUUID().toString()))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(authorities = {"HEE", "ROLE_RUN_AS_Machine User"})
  @Transactional
  public void updateTrust() throws Exception {
    // Initialize the database
    trust = trustRepository.saveAndFlush(trust);
    int databaseSizeBeforeUpdate = trustRepository.findAll().size();

    ObjectIdentityImpl siteIdentity = new ObjectIdentityImpl(trust);
    MutableAcl acl = mutableAclService.createAcl(siteIdentity);
    GrantedAuthoritySid heeSid = new GrantedAuthoritySid("HEE");
    acl.setOwner(heeSid);
    acl.insertAce(0, BasePermission.WRITE, heeSid, true);
    mutableAclService.updateAcl(acl);

    // Update the trust
    Trust updatedTrust = trustRepository.findOne(trust.getId());
    updatedTrust.localOffice(UPDATED_LOCAL_OFFICE).status(UPDATED_STATUS)
        .trustKnownAs(UPDATED_TRUST_KNOWN_AS).trustName(UPDATED_TRUST_NAME)
        .trustNumber(UPDATED_TRUST_NUMBER).address(UPDATED_ADDRESS).postCode(UPDATED_POST_CODE);
    TrustDTO trustDTO = trustMapper.trustToTrustDTO(updatedTrust);

    restTrustMockMvc.perform(put("/api/trusts").contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(trustDTO))).andExpect(status().isOk());

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
  @WithMockUser(authorities = {"HEE"})
  @Transactional
  public void updateNonExistingTrust() throws Exception {
    int databaseSizeBeforeUpdate = trustRepository.findAll().size();

    // Create the Trust
    TrustDTO trustDTO = trustMapper.trustToTrustDTO(trust);

    // If the entity doesn't have an ID, it will be created instead of just being updated
    restTrustMockMvc.perform(put("/api/trusts").contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(trustDTO))).andExpect(status().isCreated());

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
    Trust anotherTrust = new Trust().code(UPDATED_CODE).localOffice(UPDATED_LOCAL_OFFICE)
        .status(UPDATED_STATUS).trustKnownAs(UPDATED_TRUST_KNOWN_AS).address(UPDATED_ADDRESS)
        .postCode(UPDATED_POST_CODE).trustName(UPDATED_TRUST_NAME)
        .trustNumber(UPDATED_TRUST_NUMBER);

    trustRepository.saveAndFlush(anotherTrust);

    restTrustMockMvc
        .perform(get("/api/trusts").param("page", "0").param("size", "200").param("sort", "id,asc")
            .param("columnFilters", "{\"status\":[\"CURRENT\"]}"))
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

  @Test
  @Transactional
  public void getTrustsWithEncodedCharacterSearchShouldReturnTrust() throws Exception {
    // Set up trust with reserved character.
    Trust unencodedTrust = new Trust().code(UNENCODED_CODE).localOffice(UNENCODED_LOCAL_OFFICE)
        .status(UNENCODED_STATUS).trustKnownAs(UNENCODED_TRUST_KNOWN_AS).address(UNENCODED_ADDRESS)
        .postCode(UNENCODED_POST_CODE).trustName(UNENCODED_TRUST_NAME)
        .trustNumber(UNENCODED_TRUST_NUMBER);

    trustRepository.saveAndFlush(unencodedTrust);

    // Search using URLEncoded characters
    restTrustMockMvc
        .perform(get("/api/trusts").param("searchQuery", ENCODED_SEARCH_QUERY).param("page", "0")
            .param("size", "200").param("sort", "trustKnownAs,asc")
            .param("columnFilters", "{\"status\":[\"CURRENT\"]}"))
        // Verify field values
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].trustName").value(UNENCODED_TRUST_NAME));


    // Search using the 'trustKnownAs'
    restTrustMockMvc
        .perform(get("/api/trusts").param("searchQuery", ENCODED_KNOWN_AS_QUERY).param("page", "0")
            .param("size", "200").param("sort", "trustKnownAs,asc")
            .param("columnFilters", "{\"status\":[\"CURRENT\"]}"))
        // Verify field values
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].trustName").value(UNENCODED_TRUST_NAME));
  }

  @Test
  @Transactional
  public void getCurrentTrustsWithEncodedCharacterSearchShouldReturnTrust() throws Exception {
    // Set up trust with reserved character.
    Trust unencodedTrust = new Trust().code(UNENCODED_CODE).localOffice(UNENCODED_LOCAL_OFFICE)
        .status(UNENCODED_STATUS).trustKnownAs(UNENCODED_TRUST_KNOWN_AS).address(UNENCODED_ADDRESS)
        .postCode(UNENCODED_POST_CODE).trustName(UNENCODED_TRUST_NAME)
        .trustNumber(UNENCODED_TRUST_NUMBER);

    trustRepository.saveAndFlush(unencodedTrust);

    // Search using URLEncoded characters
    restTrustMockMvc
        .perform(get("/api/current/trusts").param("searchQuery", ENCODED_SEARCH_QUERY)
            .param("page", "0").param("size", "200").param("sort", "trustKnownAs,asc"))
        // Verify field values
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].trustName").value(UNENCODED_TRUST_NAME));
  }
}
